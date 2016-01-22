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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.VsamType;

/**
 * A command that will change the record's location mode to VSAM.  This command can only be used 
 * for any VSAM CALC record or a DIRECT record that does NOT participate in any set and will 
 * definitely run into trouble when executed for any other kind of record.
 */
public class MakeRecordVsamCommand extends AbstractChangeLocationModeCommand {
	
	private SchemaRecord record;
	private VsamType vsamType;
	
	public MakeRecordVsamCommand(SchemaRecord record) {
		super("Set 'Location mode' to 'VSAM'", record);
		this.record = record;
	}
	
	@Override
	public void execute() {
		
		Assert.isTrue(record.isDirect() || record.isVsamCalc(), 
					  "record should be DIRECT or VSAM CALC");
		Assert.isTrue(record.isVsamCalc() || 
					  record.getOwnerRoles().isEmpty() && record.getMemberRoles().isEmpty(), 
					  "cannot make record VSAM because it participates in 1 or more non-VSAM sets");
		
		stash(0);
		redo();
	}
	
	@Override
	public void redo() {
		
		record.setLocationMode(LocationMode.VSAM);
		
		if (getStashedLocationMode(0) == LocationMode.VSAM_CALC) {
			removeCalcKey();
		}
		
		if (getStashedLocationMode(0) == LocationMode.DIRECT) {
			if (vsamType == null) {
				createVsamType();
				vsamType = record.getVsamType();
			} else {
				// reuse the VsamType instance we created earlier but removed from the record when
				// undoing this command
				record.setVsamType(vsamType);
			}
		}
	}

	@Override
	public void undo() {
		
		if (getStashedLocationMode(0) == LocationMode.DIRECT) {
			removeVsamType();
		}
		
		if (getStashedLocationMode(0) == LocationMode.VSAM_CALC) {			
			restoreCalcKey(0);
		} 
		
		record.setLocationMode(getStashedLocationMode(0));
	}
}
