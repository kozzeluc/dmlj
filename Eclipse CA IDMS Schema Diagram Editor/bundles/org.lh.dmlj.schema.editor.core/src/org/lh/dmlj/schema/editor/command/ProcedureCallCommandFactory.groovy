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

import org.eclipse.gef.commands.Command
import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType

class ProcedureCallCommandFactory {
	
	static def EXTRACT_PROCEDURE_NAMES = { it.substring(0, it.indexOf(' ')) }
	
	private static List<String> newProcedureNames(Schema schema, List<String> callStatementArguments) {		
		callStatementArguments.collect(EXTRACT_PROCEDURE_NAMES).findAll( { !schema.getProcedure(it) } ).unique()		
	}
	
	private static List<Procedure> obsoleteProcedures(Object caller, List<String> newCallStatementArguments) {
		List<String> obsoleteProcedures = [ ]
		def procedureNamesToRetain = 
			newCallStatementArguments.collect(EXTRACT_PROCEDURE_NAMES).unique()
		caller.schema.procedures.each { Procedure procedure ->
			def referencedByOtherThanCaller = procedure.callSpecifications.findAll { callSpec ->
				callSpec instanceof RecordProcedureCallSpecification && callSpec.record != caller ||
				callSpec instanceof AreaProcedureCallSpecification && callSpec.area != caller
			}
			if (!referencedByOtherThanCaller && !procedureNamesToRetain.contains(procedure.name)) {
				obsoleteProcedures << procedure
			}
		}
		obsoleteProcedures
	}
	
	/**
	 * Creates a compound command that <b>changes an area's procedure call specifications</b>. The
	 * compound command will consist of:
	 * <ul>
	 * <li><b>zero or more</b> instances of <b>RemoveAreaProcedureCallSpecificationCommand</b>: 1 
	 * for each existing area procedure call</li>
	 * <li><b>zero or more</b> instances of <b>DeleteProcedureCommand</b>: 1 for each obsolete 
	 * procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateProcedureCommand</b>: 1 for each new procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateAreaProcedureCallSpecificationCommand</b>: 1 
	 * for each new area procedure call</li>
	 * </ul>
	 * The command's model change context will be set to: <b>CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION</b>.
	 * <br><br>
	 * @param area the area to which the procedure call specifications apply
	 * @param callStatementArguments the arguments for the area DSL's 'call' methods (i.e. a list 
	 *        containing items like 'IDMSCOND BEFORE FINISH')' or an empty list when no procedures 
	 *        are called
	 * @return a compound command
	 */
	Command createCommand(SchemaArea area, List<String> callStatementArguments) {
		
		ModelChangeCompoundCommand cc = 
			new ModelChangeCompoundCommand("Set procedure call specifications for area ${area.name}");
		cc.context = new ModelChangeContext(ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION)
		cc.context.putContextData(area)
		
		area.procedures.each { callSpec ->
			cc.add new RemoveAreaProcedureCallSpecificationCommand(callSpec)
		}
		
		obsoleteProcedures(area, callStatementArguments).each { obsoleteProcedure ->
			cc.add new DeleteProcedureCommand(obsoleteProcedure)
		}
		
		newProcedureNames(area.schema, callStatementArguments).each { procedureName ->			
			cc.add new CreateProcedureCommand(area.schema, procedureName)
		}
			
		callStatementArguments.each { callStatementArgument ->
			cc.add new CreateAreaProcedureCallSpecificationCommand(area, callStatementArgument)
		}
		
		assert cc.commands, 'nothing to do'
		
		cc
	}
	
	/**
	 * Creates a compound command that <b>changes a record's procedure call specifications</b>. The
	 * compound command will consist of:
	 * <ul>
	 * <li><b>zero or more</b> instances of <b>RemoveRecordProcedureCallSpecificationCommand</b>: 1 
	 * for each existing record procedure call</li>
	 * <li><b>zero or more</b> instances of <b>DeleteProcedureCommand</b>: 1 for each obsolete 
	 * procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateProcedureCommand</b>: 1 for each new procedure</li>
	 * <li><b>zero or more</b> instances of <b>CreateRecordProcedureCallSpecificationCommand</b>: 1 
	 * for each new record procedure call</li>
	 * </ul>
	 * The command's model change context will be set to: <b>CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION</b>.
	 * <br><br>
	 * @param record the record to which the procedure call specifications apply
	 * @param callStatementArguments the arguments for the record DSL's 'call' methods (i.e. a list 
	 *        containing items like 'IDMSCOND BEFORE STORE')' or an empty list when no procedures 
	 *        are called
	 * @return a compound command
	 */
	Command createCommand(SchemaRecord record, List<String> callStatementArguments) {
		
		ModelChangeCompoundCommand cc = 
			new ModelChangeCompoundCommand("Set procedure call specifications for record ${record.name}");
		cc.context = new ModelChangeContext(ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION)
		cc.context.putContextData(record)
		
		record.procedures.each { callSpec ->
			cc.add new RemoveRecordProcedureCallSpecificationCommand(callSpec)
		}
		
		obsoleteProcedures(record, callStatementArguments).each { obsoleteProcedure ->
			cc.add new DeleteProcedureCommand(obsoleteProcedure)
		}
		
		newProcedureNames(record.schema, callStatementArguments).each { procedureName ->			
			cc.add new CreateProcedureCommand(record.schema, procedureName)
		}
			
		callStatementArguments.each { callStatementArgument ->
			cc.add new CreateRecordProcedureCallSpecificationCommand(record, callStatementArgument)
		}
		
		assert cc.commands, 'nothing to do'
		
		cc
	}

}
