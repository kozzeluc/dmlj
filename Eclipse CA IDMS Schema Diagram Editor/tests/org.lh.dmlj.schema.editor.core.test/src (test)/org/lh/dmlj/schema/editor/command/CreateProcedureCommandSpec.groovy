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

import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.schema

import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.SchemaFactory

import spock.lang.Specification

public class CreateProcedureCommandSpec extends Specification {

	def "Create a first procedure"() {
		
		given: "a schema without procedures and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPROC')
		
		expect: "no procedures are defined in the schema before executing the command"
		!schema.procedures
		
		when: "executing the command"
		command.execute()
		
		then: "the procedure will be created and added to the schema"
		schema.procedures.size() == 1
		schema.procedures[-1] instanceof Procedure
		schema.procedures[-1].name == 'TESTPROC'
	}
	
	def "Undo the creation of a first procedure"() {
		
		given: "a schema without procedures and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPROC')
		
		and: "the command has been executed"
		command.execute()
		
		when: "undoing the command"
		command.undo()
		
		then: "the procedure will be removed from the schema"
		!schema.procedures
	}
	
	def "Redo the creation of a first procedure"() {
		
		given: "a schema without procedures and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPROC')
		
		and: "the command is executed and then undone"
		command.execute()
		Procedure procedure = schema.procedures[-1]
		command.undo()
		
		when: "redoing the command"
		command.redo()
		
		then: "the same procedure will be added to the schema again"
		schema.procedures.size() == 1
		schema.procedures[-1].is(procedure)
		schema.procedures[-1].name == 'TESTPROC'		
	}
	
	def "Create a second procedure"() {
		
		given: "a schema with exactly 1 procedure and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'\nrecord { call 'TESTPROC BEFORE' }")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPRO2')
		
		expect: "that only 1 procedure is defined in the schema"
		schema.procedures.size() == 1
		
		when: "executing the command"
		command.execute()
		
		then: "the procedure will be created and added to the schema"
		schema.procedures.size() == 2
		schema.procedures[0].name == 'TESTPROC'
		schema.procedures[-1] instanceof Procedure
		schema.procedures[-1].name == 'TESTPRO2'
	}
	
	def "Undo the creation of a second procedure"() {
		
		given: "a schema with exactly 1 procedure and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'\nrecord { call 'TESTPROC BEFORE' }")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPRO2')
		
		and: "the command has been executed"
		command.execute()
		
		when: "undoing the command"
		command.undo()
		
		then: "the procedure will be removed from the schema"
		schema.procedures.size() == 1
		schema.procedures[0].name == 'TESTPROC'
	}
	
	def "Redo the creation of a second procedure"() {
		
		given: "a schema with exactly 1 procedure and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'\nrecord { call 'TESTPROC BEFORE' }")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPRO2')
		
		and: "the command is executed and then undone"
		command.execute()
		Procedure procedure = schema.procedures[-1]		
		command.undo()
		
		when: "redoing the command"
		command.redo()
		
		then: "the same procedure will be added to the schema again"
		schema.procedures.size() == 2
		schema.procedures[0].name == 'TESTPROC'
		schema.procedures[-1].is(procedure)
		schema.procedures[-1].name == 'TESTPRO2'
	}
	
	def "Procedure uniqueness is enforced when executing the command"() {
		
		given: "a schema with exactly 1 procedure and a CreateProcedureCommand for a procedure with"
			   "the same name"
		def schema = schema("name 'TESTSCHM'\nrecord { call 'TESTPROC BEFORE' }")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPROC')
		
		expect: "that only 1 procedure is defined in the schema"
		schema.procedures.size() == 1
		schema.procedures[-1].name == 'TESTPROC'
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def e = thrown(AssertionError)
		e.message == 'duplicate procedure: TESTPROC (schema=TESTSCHM). Expression: schema.getProcedure(procedureName)'
	}
	
	def "Procedure uniqueness is enforced when redoing the command"() {
		
		given: "a schema with no procedures and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPROC')
		
		and: "the command has been executed and then undone"
		command.execute()
		command.undo()
		
		and: "for some reason after the undo, a procedure with the same name as the one in the command"
		     "now exists (e.g. because of a logic error)"
		Procedure procedure = SchemaFactory.eINSTANCE.createProcedure()
		procedure.name = 'TESTPROC'
		schema.procedures << procedure
		
		expect:
		schema.procedures.size() == 1
		schema.procedures[-1].name == 'TESTPROC'
		
		when: "redoing the command"
		command.redo()		
		
		then: "an assertion error is thrown"
		def e = thrown(AssertionError)
		e.message == 'duplicate procedure: TESTPROC (schema=TESTSCHM). Expression: schema.getProcedure(procedureName)'
	}
	
	def "The procedure created by the command must be the last one in the list when undoing the command"() {
		
		given: "a schema with no procedures and a CreateProcedureCommand for that schema"
		def schema = schema("name 'TESTSCHM'")
		CreateProcedureCommand command = new CreateProcedureCommand(schema, 'TESTPROC')
		
		and: "the command is executed"
		command.execute()
		
		and: "for some reason after command execution, another procedure has been added to the end"
			 "of the schema's procedure list (e.g. because of a logic error)"
		Procedure procedure = SchemaFactory.eINSTANCE.createProcedure()
		procedure.name = 'TESTPRO2'
		schema.procedures << procedure
		
		when: "undoing the command"
		command.undo()
		
		then: "an assertion error is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith('not the last procedure in schema: TESTPROC (TESTSCHM).')
	}
	
}
