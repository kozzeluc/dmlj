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

import org.lh.dmlj.schema.SchemaRecord;

public class MakeRecordVsamCommand extends AbstractChangeLocationModeCommand {
	
	// TODO complete this class
	
	private SchemaRecord record;
	
	public MakeRecordVsamCommand(SchemaRecord record) {
		super("Set 'Location mode' to 'VSAM'", record);
		this.record = record;
	}
	
	@Override
	public void execute() {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	@Override
	public void undo() {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	@Override
	public void redo() {
		throw new UnsupportedOperationException("not yet implemented");
	}
}
