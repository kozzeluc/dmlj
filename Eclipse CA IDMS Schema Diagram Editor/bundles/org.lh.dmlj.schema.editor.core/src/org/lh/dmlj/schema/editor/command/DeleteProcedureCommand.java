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
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.Schema;

public class DeleteProcedureCommand extends ModelChangeBasicCommand {

	protected Procedure procedure;
	private Schema schema;
	private int insertionIndex;
	
	public DeleteProcedureCommand(Procedure procedure) {
		super("Delete procedure " + procedure.getName());
		this.procedure = procedure;
	}
	
	@Override
	public void execute() {
		schema = procedure.getSchema();
		insertionIndex = schema.getProcedures().indexOf(procedure);
		removeProcedureFromSchema();
	}

	@Override
	public void undo() {
		restoreProcedureInSchema();
	}
	
	@Override
	public void redo() {
		removeProcedureFromSchema();
	}

	private void removeProcedureFromSchema() {
		Assert.isTrue(procedure.getCallSpecifications().isEmpty(), 
					  "Cannot remove procedure because it is still referenced by at least 1 " +
					  "area and/or record: " + procedure.getName());
		schema.getProcedures().remove(procedure);
	}

	private void restoreProcedureInSchema() {
		Assert.isTrue(schema.getProcedure(procedure.getName()) == null, 
					  "Cannot add procedure because a procedure with that name is already " +
					  "referenced by the schema: " + procedure.getName());
		schema.getProcedures().add(insertionIndex, procedure);		
	}

}
