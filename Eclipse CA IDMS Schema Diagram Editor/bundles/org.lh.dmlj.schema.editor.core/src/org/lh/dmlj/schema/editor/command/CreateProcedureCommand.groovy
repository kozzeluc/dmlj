/**
 * Copyright (C) 2016  Luc Hermans
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
package org.lh.dmlj.schema.editor.command

import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory

class CreateProcedureCommand extends ModelChangeBasicCommand {
	
	Schema schema
	String procedureName
	
	private Procedure procedure

	CreateProcedureCommand(Schema schema, String procedureName) {
		super()
		this.schema = schema
		this.procedureName = procedureName
	}
	
	@Override
	void execute() {
		procedure = SchemaFactory.eINSTANCE.createProcedure()
		procedure.name = procedureName	
		addProcedureToSchema()	
	}
	
	@Override
	void undo() {
		removeProcedureFromSchema()	
	}
	
	@Override
	void redo() {
		addProcedureToSchema()
	}
	
	private void addProcedureToSchema() {
		assert !schema.getProcedure(procedureName), 
			   "duplicate procedure: $procedureName (schema=${schema.name})"
		schema.procedures << procedure
	}
	
	private void removeProcedureFromSchema() {
		assert procedure.is(schema.procedures[-1]), 
			   "not the last procedure in schema: $procedureName (${schema.name})"
		schema.procedures.remove(schema.procedures.size() - 1)
	}
	
}
