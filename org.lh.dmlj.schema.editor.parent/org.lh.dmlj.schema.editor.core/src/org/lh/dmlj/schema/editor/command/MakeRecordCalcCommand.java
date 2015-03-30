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
import org.lh.dmlj.schema.SchemaRecord;

/**
 * A command that will change the record's location mode to CALC and set the 
 * CALC key.  This command can only be used for DIRECT records (at the time of
 * execution) and will definitely run into trouble when executed for a record 
 * that is defined as either CALC or VIA.
 */
public class MakeRecordCalcCommand extends AbstractChangeLocationModeCommand {

	protected SchemaRecord record;
	
	private List<Element> calcKeyElements;
	protected DuplicatesOption duplicatesOption;
	
	protected ISupplier<List<Element>> calcKeyElementSupplier;
	
	public MakeRecordCalcCommand(SchemaRecord record,
								 List<Element> calcKeyElements,
								 DuplicatesOption duplicatesOption) {
		super("Set 'Location mode' to 'CALC'", record);
		this.record = record;
		this.calcKeyElements = new ArrayList<>(calcKeyElements);
		this.duplicatesOption = duplicatesOption;
	}
	
	public MakeRecordCalcCommand(SchemaRecord record, ISupplier<List<Element>> calcKeyElementSupplier,
			 					 DuplicatesOption duplicatesOption) {
		
		super("Set 'Location mode' to 'CALC'", record);
		this.record = record;
		this.calcKeyElementSupplier = calcKeyElementSupplier;
		this.duplicatesOption = duplicatesOption;
	}	
	
	@Override
	public void execute() {		
		Assert.isTrue(record.getLocationMode() == LocationMode.DIRECT, "record not DIRECT");
		if (calcKeyElementSupplier != null) {
			calcKeyElements = new ArrayList<>(calcKeyElementSupplier.supply());
		}
		createCalcKey(calcKeyElements, null, duplicatesOption, false, -1);		
		record.setLocationMode(LocationMode.CALC);
		stash(0);
	}
	
	@Override
	public void redo() {
		Assert.isTrue(record.getLocationMode() == LocationMode.DIRECT, "record not DIRECT");		
		restoreCalcKey(0);
		record.setLocationMode(LocationMode.CALC);
	}	
	
	@Override
	public void undo() {
		Assert.isTrue(record.getLocationMode() == LocationMode.CALC, "record not CALC");
		record.setLocationMode(LocationMode.DIRECT);
		removeCalcKey();		
	}
	
}
