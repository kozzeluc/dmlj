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
import static java.util.stream.Collectors.joining;

import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateAreaProcedureCallSpecificationCommand extends ModelChangeBasicCommand {
	private SchemaArea area; 
	private String callStatementArgument;
	
	private String procedureName;
	private AreaProcedureCallSpecification callSpec;	

	public CreateAreaProcedureCallSpecificationCommand(SchemaArea area, String callStatementArgument) {
		this.area = area;
		this.callStatementArgument = callStatementArgument;
	}
	
	@Override
	public void execute() {
		procedureName = extractProcedureName();
		
		callSpec = SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		callSpec.setCallTime(extractCallTime());
		callSpec.setFunction(extractCallFunction());
		
		addCallSpecToProcedureAndArea();
	}
	
	private String extractProcedureName() {
		return callStatementArgument.split(" ")[0];
	}
	
	private ProcedureCallTime extractCallTime() {
		return ProcedureCallTime.valueOf(callStatementArgument.split(" ")[1]);
	}
	
	private AreaProcedureCallFunction extractCallFunction() {
		var tokens = Arrays.asList(callStatementArgument.split(" "));
		if (tokens.size() == 2) {
			return AreaProcedureCallFunction.EVERY_DML_FUNCTION;
		} else {
			return AreaProcedureCallFunction.valueOf(tokens.subList(2, tokens.size()).stream()
					.collect(joining("_")));
		}
	}
	
	@Override
	public void undo() {
		removeCallSpecFromProcedureAndArea();
	}
	
	@Override
	public void redo() {
		addCallSpecToProcedureAndArea();
	}
	
	private void addCallSpecToProcedureAndArea() {
		var procedure = area.getSchema().getProcedure(procedureName);
		Assert.isNotNull(procedure, "procedure not found: " + procedureName);
		callSpec.setProcedure(procedure);
		area.getProcedures().add(callSpec);
	}
	
	private void removeCallSpecFromProcedureAndArea() {
		Assert.isTrue(area.getProcedures() != null && !area.getProcedures().isEmpty() && 
				callSpec == area.getProcedures().get(area.getProcedures().size() - 1), "callSpec not the last for area");
		callSpec.setProcedure(null);
		callSpec.setArea(null);
	}
	
}
