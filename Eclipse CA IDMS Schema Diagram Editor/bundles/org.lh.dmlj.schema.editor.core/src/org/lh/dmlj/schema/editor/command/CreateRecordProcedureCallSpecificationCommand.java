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
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;

public class CreateRecordProcedureCallSpecificationCommand extends ModelChangeBasicCommand {
	private SchemaRecord schemaRecord;
	private String callStatementArgument;
	
	private String procedureName;
	private RecordProcedureCallSpecification callSpec;	

	public CreateRecordProcedureCallSpecificationCommand(SchemaRecord schemaRecord, String callStatementArgument) {
		this.schemaRecord = schemaRecord;
		this.callStatementArgument = callStatementArgument;
	}
	
	@Override
	public void execute() {
		procedureName = extractProcedureName();
		
		callSpec = SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification();
		callSpec.setCallTime(extractCallTime());
		callSpec.setVerb(extractCallVerb());
		
		addCallSpecToProcedureAndRecord();
	}
	
	private String extractProcedureName() {
		return callStatementArgument.split(" ")[0];
	}
	
	private ProcedureCallTime extractCallTime() {
		return ProcedureCallTime.valueOf(callStatementArgument.split(" ")[1]);
	}
	
	private RecordProcedureCallVerb extractCallVerb() {
		var tokens = Arrays.asList(callStatementArgument.split(" "));
		if (tokens.size() == 2) {
			return RecordProcedureCallVerb.EVERY_DML_FUNCTION;
		} else {
			return RecordProcedureCallVerb.valueOf(tokens.subList(2, tokens.size()).stream()
					.collect(joining("_")));
		}
	}
	
	@Override
	public void undo() {
		removeCallSpecFromProcedureAndRecord();		
	}
	
	@Override
	public void redo() {
		addCallSpecToProcedureAndRecord();
	}
	
	private void addCallSpecToProcedureAndRecord() {
		var procedure = schemaRecord.getSchema().getProcedure(procedureName);
		Assert.isNotNull(procedure, "procedure not found: " + procedureName);
		callSpec.setProcedure(procedure);
		schemaRecord.getProcedures().add(callSpec);
	}
	
	private void removeCallSpecFromProcedureAndRecord() {
		Assert.isTrue(!schemaRecord.getProcedures().isEmpty() && callSpec == schemaRecord.getProcedures().get(schemaRecord.getProcedures().size() - 1),
				"callSpec not the last for record");
		callSpec.setProcedure(null);
		callSpec.setRecord(null);
	}
	
}
