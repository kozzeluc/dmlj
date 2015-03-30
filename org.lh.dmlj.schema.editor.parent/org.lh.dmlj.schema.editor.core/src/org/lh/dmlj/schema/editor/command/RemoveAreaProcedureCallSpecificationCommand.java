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

import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.SchemaArea;

public class RemoveAreaProcedureCallSpecificationCommand extends ModelChangeBasicCommand {
	
	protected AreaProcedureCallSpecification callSpec;
	private SchemaArea area;
	private Procedure procedure;
	private int areaInsertionIndex;

	public RemoveAreaProcedureCallSpecificationCommand(AreaProcedureCallSpecification callSpec) {
		super();
		this.callSpec = callSpec;
	}
	
	@Override
	public void execute() {
		
		area = callSpec.getArea();
		procedure = callSpec.getProcedure();
		
		areaInsertionIndex = area.getProcedures().indexOf(callSpec);
		
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
		callSpec.setArea(null);
		callSpec.setProcedure(null);
	}

	private void restoreCallSpec() {
		area.getProcedures().add(areaInsertionIndex, callSpec);
		callSpec.setProcedure(procedure);
	}
	
}
