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

import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;

// FIXME make this command suitable for VSAM and VSAM CALC records as well

/**
 * A command that will change the record's location mode to DIRECT.  This command can only be used 
 * for CALC and VIA records and will definitely run into trouble when executed for a record that is 
 * already defined as DIRECT.
 */
public class MakeRecordDirectCommand extends AbstractChangeLocationModeCommand {
	
	protected SchemaRecord record;
	
	public MakeRecordDirectCommand(SchemaRecord record) {
		super("Set 'Location mode' to 'DIRECT'", record);
		this.record = record;
	}
	
	@Override
	public void execute() {
		
		// save the old data
		stash(0);
		
		// go finish the job
		redo();		
		
	}
	
	@Override
	public void redo() {
				
		// set the record's location mode to DIRECT
		record.setLocationMode(LocationMode.DIRECT);
		
		// remove the CALC key in the case of a CALC record, remove the VIA specification for a VIA 
		// record
		if (getStashedLocationMode(0) == LocationMode.CALC) {
			removeCalcKey();
		} else {
			// remove the VIA specification from the record AND set
			removeViaSpecification();			
		}		
		
	}
	
	@Override
	public void undo() {
		
		// reconstruct the CALC key in the case of a CALC record, and the VIA specification in the 
		// case of a VIA record
		if (getStashedLocationMode(0) == LocationMode.CALC) {
			restoreCalcKey(0);
		} else {
			restoreViaSpecification(0);
		}
		
		// restore the record's location mode
		record.setLocationMode(getStashedLocationMode(0));
		
	}
	
}
