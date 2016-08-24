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

import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaFactory

class CreateAreaProcedureCallSpecificationCommand extends ModelChangeBasicCommand {
	
	SchemaArea area 
	String callStatementArgument
	
	private String procedureName
	private AreaProcedureCallSpecification callSpec	

	CreateAreaProcedureCallSpecificationCommand(SchemaArea area, String callStatementArgument) {
		super()
		this.area = area
		this.callStatementArgument = callStatementArgument // no validity checks
	}
	
	@Override
	void execute() {
		
		procedureName = extractProcedureName()
		
		callSpec = SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification()
		callSpec.callTime = extractCallTime()
		callSpec.function = extractCallFunction()
		
		addCallSpecToProcedureAndArea()
	}
	
	private String extractProcedureName() {
		callStatementArgument.split(' ')[0]
	}
	
	private ProcedureCallTime extractCallTime() {
		ProcedureCallTime.valueOf(callStatementArgument.split(' ')[1])
	}
	
	private AreaProcedureCallFunction extractCallFunction() {
		List<String> tokens = callStatementArgument.split(' ')
		if (tokens.size() == 2) {
			AreaProcedureCallFunction.EVERY_DML_FUNCTION
		} else {
			AreaProcedureCallFunction.valueOf(tokens[2..-1].join('_'))
		}
	}
	
	@Override
	void undo() {
		removeCallSpecFromProcedureAndArea()		
	}
	
	@Override
	void redo() {
		addCallSpecToProcedureAndArea()
	}
	
	private void addCallSpecToProcedureAndArea() {
		Procedure procedure = area.schema.getProcedure(procedureName)
		assert procedure, "procedure not found: $procedureName"
		callSpec.procedure = procedure
		area.procedures << callSpec 
	}
	
	private void removeCallSpecFromProcedureAndArea() {
		assert callSpec.procedure.callSpecifications && callSpec.is(callSpec.procedure.callSpecifications[-1]), 
			   'callSpec not the last for procedure'
		assert area.procedures && callSpec.is(area.procedures[-1]), 'callSpec not the last for area'
		callSpec.procedure = null
		callSpec.area = null
	}
	
}
