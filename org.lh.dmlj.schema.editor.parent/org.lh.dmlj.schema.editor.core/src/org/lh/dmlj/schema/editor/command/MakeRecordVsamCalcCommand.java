/**
 * Copyright (C) 2015  Luc Hermans
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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.VsamType;

/**
 * A command that will change the record's location mode to VSAM CALC and set the CALC key.  This 
 * command can only be used for DIRECT and VSAM records (at the time of execution) and will 
 * definitely run into trouble when executed for a record that is defined otherwise.
 */
public class MakeRecordVsamCalcCommand extends AbstractChangeLocationModeCommand {

	protected SchemaRecord record;
	private LocationMode originalLocationMode;
	
	private List<Element> calcKeyElements;
	protected DuplicatesOption duplicatesOption;
	
	protected ISupplier<List<Element>> calcKeyElementSupplier;
	
	public MakeRecordVsamCalcCommand(SchemaRecord record, List<Element> calcKeyElements,
								 	 DuplicatesOption duplicatesOption) {
		
		super("Set 'Location mode' to 'VSAM CALC'", record);
		this.record = record;
		this.calcKeyElements = new ArrayList<>(calcKeyElements);
		this.duplicatesOption = duplicatesOption;
	}
	
	public MakeRecordVsamCalcCommand(SchemaRecord record, 
									 ISupplier<List<Element>> calcKeyElementSupplier,
									 DuplicatesOption duplicatesOption) {
		
		super("Set 'Location mode' to 'VSAM CALC'", record);
		this.record = record;
		this.calcKeyElementSupplier = calcKeyElementSupplier;
		this.duplicatesOption = duplicatesOption;
	}	
	
	@Override
	public void execute() {		
		Assert.isTrue(record.getLocationMode() == LocationMode.DIRECT ||
					  record.getLocationMode() == LocationMode.VSAM, "record not DIRECT or VSAM");
		originalLocationMode = record.getLocationMode();
		if (calcKeyElementSupplier != null) {
			calcKeyElements = new ArrayList<>(calcKeyElementSupplier.supply());
		}
		createCalcKey(calcKeyElements, null, duplicatesOption, false, -1);		
		record.setLocationMode(LocationMode.VSAM_CALC);
		if (originalLocationMode == LocationMode.DIRECT) {
			VsamType vsamType = SchemaFactory.eINSTANCE.createVsamType();
			record.setVsamType(vsamType);
			vsamType.setLengthType(VsamLengthType.FIXED);
			vsamType.setSpanned(false);
		}
		stash(0);
	}
	
	@Override
	public void redo() {
		Assert.isTrue(record.getLocationMode() == LocationMode.DIRECT ||
				  	  record.getLocationMode() == LocationMode.VSAM, "record not DIRECT or VSAM");
		restoreCalcKey(0);
		restoreVsamType(0);
		record.setLocationMode(LocationMode.VSAM_CALC);
	}	
	
	@Override
	public void undo() {
		Assert.isTrue(record.getLocationMode() == LocationMode.VSAM_CALC, "record not VSAM CALC");
		record.setLocationMode(originalLocationMode);
		removeCalcKey();
		removeVsamType();
	}
	
}
