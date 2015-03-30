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

import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.SchemaRecord;

public class RemoveRecordProcedureCallSpecificationCommand extends ModelChangeBasicCommand {
	
	protected RecordProcedureCallSpecification callSpec;
	private SchemaRecord record;
	private Procedure procedure;
	private int recordInsertionIndex;
	
	public RemoveRecordProcedureCallSpecificationCommand(RecordProcedureCallSpecification callSpec) {
		super();
		this.callSpec = callSpec;
	}
	
	@Override
	public void execute() {
		
		record = callSpec.getRecord();
		procedure = callSpec.getProcedure();
		
		recordInsertionIndex = record.getProcedures().indexOf(callSpec);
		
		removeCallSpec();
	}
	
	@Override
	public void undo() {
		restoreCallSpec();
	}
	
	@Override
	public void redo() {
		removeCallSpec();
	}

	private void removeCallSpec() {
		callSpec.setRecord(null);
		callSpec.setProcedure(null);
	}

	private void restoreCallSpec() {
		record.getProcedures().add(recordInsertionIndex, callSpec);
		callSpec.setProcedure(procedure);
	}
	
}
