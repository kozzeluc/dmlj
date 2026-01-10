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

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;

public class ProcedureCallCommandFactory {
	private static UnaryOperator<String> extractProcedureNames = it -> it.substring(0, it.indexOf(" "));
	
	private static List<String> newProcedureNames(Schema schema, List<String> callStatementArguments) {		
		return callStatementArguments.stream()
				.map(extractProcedureNames)
				.filter(it -> schema.getProcedure(it) == null)
				.distinct()
				.toList();
	}
	
	private static List<Procedure> obsoleteProcedures(Object caller, List<String> newCallStatementArguments) {
		var schema = caller instanceof SchemaArea schemaArea ? schemaArea.getSchema() : ((SchemaRecord) caller).getSchema();
		var procedureNamesToRetain = newCallStatementArguments.stream()
				.map(extractProcedureNames)
				.distinct()
				.toList();
		var obsoleteProcedures = new ArrayList<Procedure>();
		schema.getProcedures().stream()
			.forEach(procedure -> {
				List<ProcedureCallSpecification> referencedByOtherThanCaller = procedure.getCallSpecifications().stream()
						.filter(callSpec -> extractCaller(callSpec) != caller)
						.toList();
				if (referencedByOtherThanCaller.isEmpty() && !procedureNamesToRetain.contains(procedure.getName())) {
					obsoleteProcedures.add(procedure);
				}
			});
		return obsoleteProcedures;
	}
	
	private static Object extractCaller(ProcedureCallSpecification callSpec) {
		if (callSpec instanceof RecordProcedureCallSpecification recordCallSpec) {
			return recordCallSpec.getRecord();
		} else if (callSpec instanceof AreaProcedureCallSpecification areaCallSpec) {
			return areaCallSpec.getArea();
		} else {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Creates a compound command that <b>changes an area's procedure call specifications</b>. The compound
	 * command will consist of:
	 * <ul>
	 * <li><b>zero or more</b> instances of <b>RemoveAreaProcedureCallSpecificationCommand</b>: 1 for each
	 * existing area procedure call</li>
	 * <li><b>zero or more</b> instances of <b>DeleteProcedureCommand</b>: 1 for each obsolete procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateProcedureCommand</b>: 1 for each new procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateAreaProcedureCallSpecificationCommand</b>: 1 for each new
	 * area procedure call</li>
	 * </ul>
	 * The command's model change context will be set to: <b>CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION</b>.
	 * <br><br>
	 * @param area the area to which the procedure call specifications apply
	 * @param callStatementArguments the arguments for the area DSL's 'call' methods (i.e. a list containing items
	 *        like 'IDMSCOND BEFORE FINISH')' or an empty list when no procedures are called
	 * @return a compound command
	 */
	public Command createCommand(SchemaArea area, List<String> callStatementArguments) {
		var cc = new ModelChangeCompoundCommand("Set procedure call specifications for area " + area.getName());
		cc.setContext(new ModelChangeContext(ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION));
		cc.getContext().putContextData(area, ModelChangeContext.areaContextDataAssembler);
		
		area.getProcedures().stream()
				.map(RemoveAreaProcedureCallSpecificationCommand::new)
				.forEach(cc::add);
		
		obsoleteProcedures(area, callStatementArguments).stream()
				.map(DeleteProcedureCommand::new)
				.forEach(cc::add);
		
		newProcedureNames(area.getSchema(), callStatementArguments).stream()
				.map(procedureName -> new CreateProcedureCommand(area.getSchema(), procedureName))
				.forEach(cc::add);
			
		callStatementArguments.stream()
				.map(callStatementArgument -> new CreateAreaProcedureCallSpecificationCommand(area, callStatementArgument))
				.forEach(cc::add);
		
		Assert.isTrue(!cc.getCommands().isEmpty(), "nothing to do");
		
		return cc;
	}
	
	/**
	 * Creates a compound command that <b>changes a record's procedure call specifications</b>. The compound
	 * command will consist of:
	 * <ul>
	 * <li><b>zero or more</b> instances of <b>RemoveRecordProcedureCallSpecificationCommand</b>: 1 for each
	 * existing record procedure call</li>
	 * <li><b>zero or more</b> instances of <b>DeleteProcedureCommand</b>: 1 for each obsolete procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateProcedureCommand</b>: 1 for each new procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateRecordProcedureCallSpecificationCommand</b>: 1 for each new
	 * record procedure call</li>
	 * </ul>
	 * The command's model change context will be set to: <b>CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION</b>.
	 * <br><br>
	 * @param schemaRecord the record to which the procedure call specifications apply
	 * @param callStatementArguments the arguments for the record DSL's 'call' methods (i.e. a list containing
	 *        items like 'IDMSCOND BEFORE STORE')' or an empty list when no procedures are called
	 * @return a compound command
	 */
	public Command createCommand(SchemaRecord schemaRecord, List<String> callStatementArguments) {
		var cc = new ModelChangeCompoundCommand("Set procedure call specifications for record " + schemaRecord.getName());
		cc.setContext(new ModelChangeContext(ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION));
		cc.getContext().putContextData(schemaRecord);
		
		schemaRecord.getProcedures().stream()
				.map(RemoveRecordProcedureCallSpecificationCommand::new)
				.forEach(cc::add);
		
		obsoleteProcedures(schemaRecord, callStatementArguments).stream()
				.map(DeleteProcedureCommand::new)
				.forEach(cc::add);
		
		newProcedureNames(schemaRecord.getSchema(), callStatementArguments).stream()
				.map(procedureName -> new CreateProcedureCommand(schemaRecord.getSchema(), procedureName))
				.forEach(cc::add);
			
		callStatementArguments.stream()
				.map(callStatementArgument -> new CreateRecordProcedureCallSpecificationCommand(schemaRecord, callStatementArgument))
				.forEach(cc::add);
				
		Assert.isTrue(!cc.getCommands().isEmpty(), "nothing to do");
		
		return cc;
	}

}
