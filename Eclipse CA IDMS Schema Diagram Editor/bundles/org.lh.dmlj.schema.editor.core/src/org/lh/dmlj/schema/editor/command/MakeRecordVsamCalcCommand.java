/**
 * Copyright (C) 2025  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.VsamType;

/**
 * A command that will change the record's location mode to VSAM CALC and set the CALC key.  This command can
 * only be used for DIRECT and VSAM records (at the time of execution) and will definitely run into trouble when
 * executed for a record that is defined otherwise. In contrast to the CA IDMS documentation, we allow more than
 * 1 element to be part of the CALC key (which is not correct of course).
 */
public class MakeRecordVsamCalcCommand extends AbstractChangeLocationModeCommand {
	private List<Element> calcKeyElements;
	protected final DuplicatesOption duplicatesOption;
	
	protected Supplier<List<Element>> calcKeyElementSupplier;
	
	private LocationMode originalLocationMode;
	
	public MakeRecordVsamCalcCommand(SchemaRecord schemaRecord, List<Element> calcKeyElements, DuplicatesOption duplicatesOption) {
		super("Set 'Location mode' to 'VSAM CALC'", schemaRecord);
		this.calcKeyElements = new ArrayList<>(calcKeyElements);
		this.duplicatesOption = duplicatesOption;
	}
	
	public MakeRecordVsamCalcCommand(SchemaRecord schemaRecord, Supplier<List<Element>> calcKeyElementSupplier, DuplicatesOption duplicatesOption) {		
		super("Set 'Location mode' to 'VSAM CALC'", schemaRecord);
		this.calcKeyElementSupplier = calcKeyElementSupplier;
		this.duplicatesOption = duplicatesOption;
	}	
	
	@Override
	public void execute() {
		Assert.isTrue(schemaRecord.getLocationMode() == LocationMode.DIRECT || schemaRecord.getLocationMode() == LocationMode.VSAM,
				"record not DIRECT or VSAM");
		Assert.isTrue(schemaRecord.isVsam() || schemaRecord.getOwnerRoles().isEmpty() && schemaRecord.getMemberRoles().isEmpty(), 
				"cannot make record VSAM CALC because it participates in 1 or more non-VSAM sets");
		Assert.isTrue(duplicatesOption == DuplicatesOption.NOT_ALLOWED || duplicatesOption == DuplicatesOption.UNORDERED,
				"unsupported duplicates option: " + duplicatesOption);
		
		originalLocationMode = schemaRecord.getLocationMode();
		if (calcKeyElementSupplier != null) {
			calcKeyElements = List.copyOf(calcKeyElementSupplier.get());
		}
		createCalcKey(calcKeyElements, null, duplicatesOption, false, -1);		
		schemaRecord.setLocationMode(LocationMode.VSAM_CALC);
		if (originalLocationMode == LocationMode.DIRECT) {
			VsamType vsamType = SchemaFactory.eINSTANCE.createVsamType();
			schemaRecord.setVsamType(vsamType);
			vsamType.setLengthType(VsamLengthType.FIXED);
			vsamType.setSpanned(false);
		}
		stash(0);
	}
	
	@Override
	public void redo() {
		Assert.isTrue(schemaRecord.getLocationMode() == LocationMode.DIRECT || schemaRecord.getLocationMode() == LocationMode.VSAM,
				"record not DIRECT or VSAM");
		restoreCalcKey(0);
		if (originalLocationMode == LocationMode.DIRECT) {
			restoreVsamType(0);
		}
		schemaRecord.setLocationMode(LocationMode.VSAM_CALC);
	}	
	
	@Override
	public void undo() {
		Assert.isTrue(schemaRecord.getLocationMode() == LocationMode.VSAM_CALC, "record not VSAM CALC");
		schemaRecord.setLocationMode(originalLocationMode);
		removeCalcKey();
		if (originalLocationMode == LocationMode.DIRECT) {
			removeVsamType();
		}
	}
	
}
