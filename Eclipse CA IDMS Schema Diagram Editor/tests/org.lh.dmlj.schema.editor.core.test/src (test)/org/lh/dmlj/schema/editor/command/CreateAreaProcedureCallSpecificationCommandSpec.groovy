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
package org.lh.dmlj.schema.editor.command;

import static org.lh.dmlj.schema.ProcedureCallTime.AFTER
import static org.lh.dmlj.schema.ProcedureCallTime.BEFORE
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.area

import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaFactory

import spock.lang.Specification
import spock.lang.Unroll

public class CreateAreaProcedureCallSpecificationCommandSpec extends Specification {
	
	static def REPLACE_UNDERSCORES_BY_SPACES = { " ${it.toString().replace('_', ' ')}" }
	
	static def FUNCTIONS = 
		[ AreaProcedureCallFunction.EVERY_DML_FUNCTION ] + AreaProcedureCallFunction.values().collect()
	static def FUNCTIONS_AS_STRINGS = 
		[ '' ] + AreaProcedureCallFunction.values().collect(REPLACE_UNDERSCORES_BY_SPACES)
	static def FUNCTION_COUNT = FUNCTIONS.size()

	@Unroll("#callStatement")
	def "Executing the command adds the procedure call spec to both procedure and area"() {
	
		given: "a command to add a 2nd procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'\ncall 'TESTPROC BEFORE'")
		Procedure procedure = area.schema.getProcedure('TESTPROC')
		CreateAreaProcedureCallSpecificationCommand command = 
			new CreateAreaProcedureCallSpecificationCommand(area, callStatement)
			
		expect: "the procedure must already exist in the schema"
		procedure
		area.schema.procedures.size() == 1
		area.schema.procedures[-1].name == 'TESTPROC'
		area.schema.procedures[-1].callSpecifications.size() == 1
		area.procedures.size() == 1
		
		when: "executing the command"
		command.execute()
		
		then: "a procedure call spec is added to the procedure"
		area.schema.procedures.size() == 1
		def proceduresCallSpecifications = procedure.callSpecifications // this is an operation, not a property !
		proceduresCallSpecifications.size() == 2 
		area.is(proceduresCallSpecifications[-1].area)
		procedure.is(proceduresCallSpecifications[-1].procedure)
		callTime.is(proceduresCallSpecifications[-1].callTime)
		function.is(proceduresCallSpecifications[-1].function)
		
		and: "the same procedure call spec is added to the area"
		def areasCallSpecifications = area.procedures
		areasCallSpecifications.size() == 2
		areasCallSpecifications[-1].is(proceduresCallSpecifications[-1])
		
		where: "we try all valid area procedure call combinations"
		callTime << [ BEFORE ] * FUNCTION_COUNT + [ AFTER ] * FUNCTION_COUNT
		functionAsString << FUNCTIONS_AS_STRINGS * 2
		function << FUNCTIONS * 2
		callStatement = "TESTPROC $callTime$functionAsString"
	}
	
	def "Undoing the command removes the procedure call spec from both procedure and area"() {

		given: "a command to add a 2nd procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'\ncall 'TESTPROC BEFORE'")
		CreateAreaProcedureCallSpecificationCommand command =
			new CreateAreaProcedureCallSpecificationCommand(area, 'TESTPROC AFTER')

		and: "the command has been executed"
		command.execute()
		
		expect: "the procedure call is part of both procedure and area"
		area.schema.procedures[-1].callSpecifications.size() == 2
		area.procedures.size() == 2
		AreaProcedureCallSpecification callSpec = area.procedures[-1]
		callSpec.callTime == ProcedureCallTime.AFTER
			
		when: "undoing the command"
		command.undo()
		
		then: "the procedure call spec is removed from the procedure"
		area.schema.procedures[-1].callSpecifications.size() == 1
		area.schema.procedures[0].callSpecifications[0].callTime == ProcedureCallTime.BEFORE
		
		and: "the procedure call spec is removed from the area"
		area.procedures.size() == 1
		area.procedures[0] == area.schema.procedures[0].callSpecifications[0]
	}
	
	def "Redoing the command adds the procedure call spec to both procedure and area again"() {
		
		given: "a command to add a 2nd procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'\ncall 'TESTPROC BEFORE'")
		CreateAreaProcedureCallSpecificationCommand command =
			new CreateAreaProcedureCallSpecificationCommand(area, 'TESTPROC AFTER')

		and: "the command has been executed and subsequently undone"
		command.execute()
		AreaProcedureCallSpecification ourCallSpec = area.procedures[-1]
		command.undo()
		
		expect: "the procedure call is no longer part of both procedure and area"
		area.schema.procedures[-1].callSpecifications.size() == 1
		area.procedures.size() == 1
		AreaProcedureCallSpecification callSpec = area.procedures[0]
		callSpec.callTime == ProcedureCallTime.BEFORE
		callSpec.is(area.schema.procedures[-1].callSpecifications[0])
			
		when: "redoing the command"
		command.redo()
		
		then: "the same procedure call spec is added to the procedure again"
		area.schema.procedures.size() == 1
		Procedure procedure = area.schema.getProcedure('TESTPROC')
		def proceduresCallSpecifications = procedure.callSpecifications // this is an operation, not a property !
		proceduresCallSpecifications.size() == 2
		ourCallSpec.is(proceduresCallSpecifications[-1])
		ourCallSpec.area.is(area)
		ourCallSpec.procedure.is(procedure)
		ourCallSpec.callTime == ProcedureCallTime.AFTER
		ourCallSpec.function == AreaProcedureCallFunction.EVERY_DML_FUNCTION
		
		and: "the same procedure call spec is added to the area again"
		def areasCallSpecifications = area.procedures
		areasCallSpecifications.size() == 2
		ourCallSpec.is(areasCallSpecifications[-1])		
	}
	
	def "The procedure specified must exist when executing the command"() {
	
		given: "a command to add a procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'")
		CreateAreaProcedureCallSpecificationCommand command =
			new CreateAreaProcedureCallSpecificationCommand(area, 'TESTPROC BEFORE')
			
		when: "executing the command"
		command.execute()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'procedure not found: TESTPROC'
	}
	
	def "The procedure specified must exist when redoing the command"() {
		
		given: "a command to add a procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'\ncall 'TESTPROC BEFORE'")
		CreateAreaProcedureCallSpecificationCommand command =
			new CreateAreaProcedureCallSpecificationCommand(area, 'TESTPROC BEFORE')
			
		and: "the command has been executed and subsequently undone"
		command.execute()
		command.undo()
		
		and: "for some reason, a procedure with the same name as the one upon command execution"
			 "does no longer exist (e.g. because of a logic error)"
		area.schema.procedures[-1].name = 'RENAMED'
		
		when: "redoing the command"
		command.redo()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'procedure not found: TESTPROC'
	}
	
	def "The procedure call spec created by the command must be the last one for the procedure when undoing the command"() {
		
		given: "a command to add a procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'\ncall 'TESTPROC BEFORE'")
		Procedure procedure = area.schema.procedures[-1]
		CreateAreaProcedureCallSpecificationCommand command =
			new CreateAreaProcedureCallSpecificationCommand(area, 'TESTPROC BEFORE')
			
		and: "the command has been executed"
		command.execute()		
		
		and: "for some reason, another procedure call spec is the last one for the procedure (e.g."
			 "because of a logic error)"
		AreaProcedureCallSpecification callSpec = 
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification()
		callSpec.procedure = procedure
		callSpec.area = area
		
		when: "undoing the command"
		command.undo()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'callSpec not the last for procedure'
	}
	
	def "The procedure call spec created by the command must be the last one for the area when undoing the command"() {
		
		given: "a command to add a procedure call to an area"
		SchemaArea area = area("name 'TESTAREA'\ncall 'TESTPROC BEFORE'")
		CreateAreaProcedureCallSpecificationCommand command =
			new CreateAreaProcedureCallSpecificationCommand(area, 'TESTPROC BEFORE')
			
		and: "the command has been executed"
		command.execute()
		
		and: "for some reason, another procedure call spec is the last one for the area (e.g."
			 "because of a logic error)"
		AreaProcedureCallSpecification callSpec =
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification()
		callSpec.procedure = null // avoid an AssertionError for the procedure
		callSpec.area = area
		
		when: "undoing the command"
		command.undo()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'callSpec not the last for area'
	}
	
}
