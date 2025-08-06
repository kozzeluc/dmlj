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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateProcedureCommand extends ModelChangeBasicCommand {
	private final Schema schema;
	private final String procedureName;
	
	private Procedure procedure;

	CreateProcedureCommand(Schema schema, String procedureName) {
		this.schema = schema;
		this.procedureName = procedureName;
	}
	
	@Override
	public void execute() {
		procedure = SchemaFactory.eINSTANCE.createProcedure();
		procedure.setName(procedureName);
		addProcedureToSchema();
	}
	
	@Override
	public void undo() {
		removeProcedureFromSchema();	
	}
	
	@Override
	public void redo() {
		addProcedureToSchema();
	}
	
	private void addProcedureToSchema() {
		Assert.isTrue(schema.getProcedure(procedureName) == null,
				String.format("duplicate procedure: %s (schema=%s)", procedureName, schema.getName()));
		schema.getProcedures().add(procedure);
	}
	
	private void removeProcedureFromSchema() {
		Assert.isTrue(procedure == schema.getProcedures().get(schema.getProcedures().size() - 1),
				String.format("\"not the last procedure in schema: %s (schema=%s)", procedureName, schema.getName()));
		schema.getProcedures().remove(schema.getProcedures().size() - 1);
	}
	
}
