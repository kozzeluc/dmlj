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
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord

class CreateRecordProcedureCallSpecificationCommand extends ModelChangeBasicCommand {
	
	private SchemaRecord record
	private String callStatementArgument
	
	private String procedureName
	private RecordProcedureCallSpecification callSpec	

	CreateRecordProcedureCallSpecificationCommand(SchemaRecord record, String callStatementArgument) {
		super()
		this.record = record
		this.callStatementArgument = callStatementArgument // no validity checks
	}
	
	@Override
	void execute() {
		
		procedureName = extractProcedureName()
		
		callSpec = SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification()
		callSpec.callTime = extractCallTime()
		callSpec.verb = extractCallVerb()
		
		addCallSpecToProcedureAndRecord()
	}
	
	private String extractProcedureName() {
		callStatementArgument.split(' ')[0]
	}
	
	private ProcedureCallTime extractCallTime() {
		ProcedureCallTime.valueOf(callStatementArgument.split(' ')[1])
	}
	
	private RecordProcedureCallVerb extractCallVerb() {
		List<String> tokens = callStatementArgument.split(' ')
		if (tokens.size() == 2) {
			RecordProcedureCallVerb.EVERY_DML_FUNCTION
		} else {
			RecordProcedureCallVerb.valueOf(tokens[2..-1].join('_'))
		}
	}
	
	@Override
	void undo() {
		removeCallSpecFromProcedureAndRecord()		
	}
	
	@Override
	void redo() {
		addCallSpecToProcedureAndRecord()
	}
	
	private void addCallSpecToProcedureAndRecord() {
		Procedure procedure = record.schema.getProcedure(procedureName)
		assert procedure, "procedure not found: $procedureName"
		callSpec.procedure = procedure
		record.procedures << callSpec 
	}
	
	private void removeCallSpecFromProcedureAndRecord() {
		assert callSpec.procedure.callSpecifications && callSpec.is(callSpec.procedure.callSpecifications[-1]), 
			   'callSpec not the last for procedure'
		assert record.procedures && callSpec.is(record.procedures[-1]), 'callSpec not the last for record'
		callSpec.procedure = null
		callSpec.record = null
	}
	
}
