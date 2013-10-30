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

import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * A command that will change the record's location mode to VIA and set the 
 * VIA specification.  This command can only be used for DIRECT records (at time 
 * of execution) and will definitely run into trouble when executed for a record 
 * that is defined as either CALC or VIA.
 */
public class MakeRecordViaCommand extends AbstractChangeLocationModeCommand {

	private String viaSetName; 
	private String symbolicDisplacementName; 
	private Short  displacementPageCount;
	
	public MakeRecordViaCommand(SchemaRecord record,
								String viaSetName, 
								String symbolicDisplacementName, 
								Short displacementPageCount) {
		
		super("Set 'Location mode' to 'VIA'", record);	
		this.viaSetName = viaSetName;
		this.symbolicDisplacementName = symbolicDisplacementName;
		this.displacementPageCount = displacementPageCount;
	}
	
	@Override
	public void execute() {
		if (record.getLocationMode() != LocationMode.DIRECT) {
			throw new IllegalArgumentException("record not DIRECT");
		}
		createViaSpecification(viaSetName, symbolicDisplacementName, 
							   displacementPageCount, -1);
		record.setLocationMode(LocationMode.VIA);
	}
	
	@Override
	public void undo() {
		record.setLocationMode(LocationMode.DIRECT);
		removeViaSpecification();	
	}

}
