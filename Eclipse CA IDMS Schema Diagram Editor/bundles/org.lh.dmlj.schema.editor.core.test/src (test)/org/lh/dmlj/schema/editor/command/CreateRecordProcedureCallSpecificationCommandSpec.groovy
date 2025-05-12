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
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.record

import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord

import spock.lang.Specification
import spock.lang.Unroll

public class CreateRecordProcedureCallSpecificationCommandSpec extends Specification {
	
	static def REPLACE_UNDERSCORES_BY_SPACES = { " ${it.toString().replace('_', ' ')}" }
	
	static def VERBS = 
		[ RecordProcedureCallVerb.EVERY_DML_FUNCTION ] + RecordProcedureCallVerb.values().collect()
	static def VERBS_AS_STRINGS = [ '' ] + RecordProcedureCallVerb.values().collect(REPLACE_UNDERSCORES_BY_SPACES)
	static def VERB_COUNT = VERBS.size()

	@Unroll("#callStatement")
	def "Executing the command adds the procedure call spec to both procedure and record"() {
	
		given: "a command to add a 2nd procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'TESTPROC BEFORE'")
		Procedure procedure = record.schema.getProcedure('TESTPROC')
		CreateRecordProcedureCallSpecificationCommand command = 
			new CreateRecordProcedureCallSpecificationCommand(record, callStatement)
			
		expect: "the procedure must already exist in the schema"
		procedure
		record.schema.procedures.size() == 1
		record.schema.procedures[-1].name == 'TESTPROC'
		record.schema.procedures[-1].callSpecifications.size() == 1
		record.procedures.size() == 1
		
		when: "executing the command"
		command.execute()
		
		then: "a procedure call spec is added to the procedure"
		record.schema.procedures.size() == 1
		def proceduresCallSpecifications = procedure.callSpecifications // this is an operation, not a property !
		proceduresCallSpecifications.size() == 2 
		record.is(proceduresCallSpecifications[-1].record)
		procedure.is(proceduresCallSpecifications[-1].procedure)
		callTime.is(proceduresCallSpecifications[-1].callTime)
		verb.is(proceduresCallSpecifications[-1].verb)
		
		and: "the same procedure call spec is added to the record"
		def recordsCallSpecifications = record.procedures
		recordsCallSpecifications.size() == 2
		recordsCallSpecifications[-1].is(proceduresCallSpecifications[-1])
		
		where: "we try all valid record procedure call combinations"
		callTime << [ BEFORE ] * VERB_COUNT + [ AFTER ] * VERB_COUNT
		verbAsString << VERBS_AS_STRINGS * 2
		verb <<VERBS * 2
		callStatement = "TESTPROC $callTime$verbAsString"
	}
	
	def "Undoing the command removes the procedure call spec from both procedure and record"() {

		given: "a command to add a 2nd procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'TESTPROC BEFORE'")
		CreateRecordProcedureCallSpecificationCommand command =
			new CreateRecordProcedureCallSpecificationCommand(record, 'TESTPROC AFTER')

		and: "the command has been executed"
		command.execute()
		
		expect: "the procedure call is part of both procedure and record"
		record.schema.procedures[-1].callSpecifications.size() == 2
		record.procedures.size() == 2
		RecordProcedureCallSpecification callSpec = record.procedures[-1]
		callSpec.callTime == ProcedureCallTime.AFTER
			
		when: "undoing the command"
		command.undo()
		
		then: "the procedure call spec is removed from the procedure"
		record.schema.procedures[-1].callSpecifications.size() == 1
		record.schema.procedures[0].callSpecifications[0].callTime == ProcedureCallTime.BEFORE
		
		and: "the procedure call spec is removed from the record"
		record.procedures.size() == 1
		record.procedures[0] == record.schema.procedures[0].callSpecifications[0]
	}
	
	def "Redoing the command adds the procedure call spec to both procedure and record again"() {
		
		given: "a command to add a 2nd procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'TESTPROC BEFORE'")
		CreateRecordProcedureCallSpecificationCommand command =
			new CreateRecordProcedureCallSpecificationCommand(record, 'TESTPROC AFTER')

		and: "the command has been executed and subsequently undone"
		command.execute()
		RecordProcedureCallSpecification ourCallSpec = record.procedures[-1]
		command.undo()
		
		expect: "the procedure call is no longer part of both procedure and record"
		record.schema.procedures[-1].callSpecifications.size() == 1
		record.procedures.size() == 1
		RecordProcedureCallSpecification callSpec = record.procedures[0]
		callSpec.callTime == ProcedureCallTime.BEFORE
		callSpec.is(record.schema.procedures[-1].callSpecifications[0])
			
		when: "redoing the command"
		command.redo()
		
		then: "the same procedure call spec is added to the procedure again"
		record.schema.procedures.size() == 1
		Procedure procedure = record.schema.getProcedure('TESTPROC')
		def proceduresCallSpecifications = procedure.callSpecifications // this is an operation, not a property !
		proceduresCallSpecifications.size() == 2
		ourCallSpec.is(proceduresCallSpecifications[-1])
		ourCallSpec.record.is(record)
		ourCallSpec.procedure.is(procedure)
		ourCallSpec.callTime == ProcedureCallTime.AFTER
		ourCallSpec.verb == RecordProcedureCallVerb.EVERY_DML_FUNCTION
		
		and: "the same procedure call spec is added to the record again"
		def recordsCallSpecifications = record.procedures
		recordsCallSpecifications.size() == 2
		ourCallSpec.is(recordsCallSpecifications[-1])		
	}
	
	def "The procedure specified must exist when executing the command"() {
	
		given: "a command to add a procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'")
		CreateRecordProcedureCallSpecificationCommand command =
			new CreateRecordProcedureCallSpecificationCommand(record, 'TESTPROC BEFORE')
			
		when: "executing the command"
		command.execute()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'procedure not found: TESTPROC'
	}
	
	def "The procedure specified must exist when redoing the command"() {
		
		given: "a command to add a procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'TESTPROC BEFORE'")
		CreateRecordProcedureCallSpecificationCommand command =
			new CreateRecordProcedureCallSpecificationCommand(record, 'TESTPROC BEFORE')
			
		and: "the command has been executed and subsequently undone"
		command.execute()
		command.undo()
		
		and: "for some reason, a procedure with the same name as the one upon command execution"
			 "does no longer exist (e.g. because of a logic error)"
		record.schema.procedures[-1].name = 'RENAMED'
		
		when: "redoing the command"
		command.redo()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'procedure not found: TESTPROC'
	}
	
	def "The procedure call spec created by the command must be the last one for the procedure when undoing the command"() {
		
		given: "a command to add a procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'TESTPROC BEFORE'")
		Procedure procedure = record.schema.procedures[-1]
		CreateRecordProcedureCallSpecificationCommand command =
			new CreateRecordProcedureCallSpecificationCommand(record, 'TESTPROC BEFORE')
			
		and: "the command has been executed"
		command.execute()		
		
		and: "for some reason, another procedure call spec is the last one for the procedure (e.g."
			 "because of a logic error)"
		RecordProcedureCallSpecification callSpec = 
			SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification()
		callSpec.procedure = procedure
		callSpec.record = record
		
		when: "undoing the command"
		command.undo()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'callSpec not the last for procedure'
	}
	
	def "The procedure call spec created by the command must be the last one for the record when undoing the command"() {
		
		given: "a command to add a procedure call to a record"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'TESTPROC BEFORE'")
		CreateRecordProcedureCallSpecificationCommand command =
			new CreateRecordProcedureCallSpecificationCommand(record, 'TESTPROC BEFORE')
			
		and: "the command has been executed"
		command.execute()
		
		and: "for some reason, another procedure call spec is the last one for the record (e.g."
			 "because of a logic error)"
		RecordProcedureCallSpecification callSpec =
			SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification()
		callSpec.procedure = null // avoid an AssertionError for the procedure
		callSpec.record = record
		
		when: "undoing the command"
		command.undo()
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'callSpec not the last for record'
	}
	
}
