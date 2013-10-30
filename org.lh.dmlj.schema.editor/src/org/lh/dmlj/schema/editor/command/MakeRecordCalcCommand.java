/**
 * Copyright (C) 2013  Luc Hermans
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

	private List<Element> 	 calcKeyElements;
	private DuplicatesOption duplicatesOption;
	
	public MakeRecordCalcCommand(SchemaRecord record,
								 List<Element> calcKeyElements,
								 DuplicatesOption duplicatesOption) {
		super("Set 'Location mode' to 'CALC'", record);
		this.calcKeyElements = new ArrayList<>(calcKeyElements);
		this.duplicatesOption = duplicatesOption;
	}	
	
	@Override
	public void execute() {
		if (record.getLocationMode() != LocationMode.DIRECT) {
			throw new IllegalArgumentException("record not DIRECT");
		}
		createCalcKey(calcKeyElements, null, duplicatesOption, false, -1);
		record.setLocationMode(LocationMode.CALC);
	}
	
	@Override
	public void undo() {
		record.setLocationMode(LocationMode.DIRECT);
		removeCalcKey();		
	}
	

}
