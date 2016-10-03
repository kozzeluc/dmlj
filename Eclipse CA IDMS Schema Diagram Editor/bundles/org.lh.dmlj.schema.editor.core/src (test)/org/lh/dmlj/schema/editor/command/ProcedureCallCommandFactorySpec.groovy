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

import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.area
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.record

import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType
import org.lh.dmlj.schema.editor.dsl.builder.model.AreaModelBuilder
import org.lh.dmlj.schema.editor.dsl.builder.model.RecordModelBuilder

import spock.lang.Specification

public class ProcedureCallCommandFactorySpec extends Specification {
	
	def "Change the only area procedure call function"() {
	
		given: "an area with 1 procedure call specification and a procedure call command factory"
		SchemaArea area = area("name 'TESTAREA'\ncall 'IDMSCOMP BEFORE'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		area.procedures.size() == 1
		AreaProcedureCallSpecification callSpec = area.procedures[0]
		
		when: "creating the command for changing the procedure call function"
		ModelChangeCompoundCommand cc = factory.createCommand(area, [ 'IDMSCOMP AFTER' ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for area TESTAREA'
		cc.commands.size() == 2
		
		and: "the first command removes the old procedure call specification"
		RemoveAreaProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command creates the new procedure call specification"
		CreateAreaProcedureCallSpecificationCommand command2 = cc.commands[1]
		command2.area == area
		command2.callStatementArgument == 'IDMSCOMP AFTER'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.AREA_NAME] == 'TESTAREA'
	}

	def "Create first area procedure call specification and a new procedure"() {
		
		given: "an area without procedure calls and a procedure call command factory"
		SchemaArea area = area("name 'TESTAREA'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		when: "creating the command for 1 call to a non-existing procedure"
		ModelChangeCompoundCommand cc = factory.createCommand(area, [ 'TESTPROC BEFORE' ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for area TESTAREA'
		cc.commands.size() == 2
		
		and: "the first command creates the procedure"
		CreateProcedureCommand command1 = cc.commands[0]
		command1.schema == area.schema
		command1.procedureName == 'TESTPROC'
		
		and: "the second command creates the new procedure call specification"
		CreateAreaProcedureCallSpecificationCommand command2 = cc.commands[1]
		command2.area == area
		command2.callStatementArgument == 'TESTPROC BEFORE'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.AREA_NAME] == 'TESTAREA'
	}
	
	def "Remove the only area procedure call specification and an obsolete procedure"() {
		
		given: "an area with 1 procedure call and a procedure call command factory"
		SchemaArea area = area("name 'TESTAREA'\n call 'TESTPROC BEFORE'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		area.procedures.size() == 1
		AreaProcedureCallSpecification callSpec = area.procedures[0]
		Procedure procedure = callSpec.procedure
		
		when: "creating the command which removes the procedure call"
		ModelChangeCompoundCommand cc = factory.createCommand(area, [ ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for area TESTAREA'
		cc.commands.size() == 2
		
		and: "the first command removes the old procedure call specification"
		RemoveAreaProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command removes the obsolete procedure"
		DeleteProcedureCommand command2 = cc.commands[1]
		command2.procedure == procedure
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.AREA_NAME] == 'TESTAREA'
	}
	
	def "Replace an area procedure call specification, remove an obsolete procedure and create a new one"() {
		
		given: "an area with 1 procedure call and a procedure call command factory"
		SchemaArea area = area("name 'TESTAREA'\ncall 'IDMSCOMP BEFORE'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		area.procedures.size() == 1
		AreaProcedureCallSpecification callSpec = area.procedures[0]
		Procedure procedure = callSpec.procedure
		
		when: "creating the command for only 1 call to a non-existing procedure"
		ModelChangeCompoundCommand cc = factory.createCommand(area, [ 'IDMSDCOM AFTER' ] )
		
		then: "a compound command composed of 4 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for area TESTAREA'
		cc.commands.size() == 4
		
		and: "the first command removes the old procedure call specification"
		RemoveAreaProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command removes the obsolete procedure"
		DeleteProcedureCommand command2 = cc.commands[1]
		command2.procedure == procedure
		
		and: "the third command creates the procedure"
		CreateProcedureCommand command3 = cc.commands[2]
		command3.schema == area.schema
		command3.procedureName == 'IDMSDCOM'
		
		and: "the fourth command creates the new procedure call specification"
		CreateAreaProcedureCallSpecificationCommand command4 = cc.commands[3]
		command4.area == area
		command4.callStatementArgument == 'IDMSDCOM AFTER'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.AREA_NAME] == 'TESTAREA'
	}
	
	def "Remove an area procedure call specification while retaining the procedure"() {
		
		given: "a schema with 2 areas, each with a procedure call specification referring to"		
			   "the same procedure, and a procedure call command factory"
		SchemaArea area1 = new AreaModelBuilder().build { 
			name 'TESTAREA1'
			call 'IDMSCOMP BEFORE' 
		}
		SchemaArea area2 = new AreaModelBuilder( schema : area1.schema ).build {
			name 'TESTAREA2'
			call 'IDMSCOMP BEFORE'
			call 'IDMSDCOM BEFORE'
		}
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		area1.procedures.size() == 1
		AreaProcedureCallSpecification callSpec = area1.procedures[0]
		
		when: "creating the command for changing the procedure call function for the 1st area"
		ModelChangeCompoundCommand cc = factory.createCommand(area1, [ 'IDMSDCOM BEFORE' ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for area TESTAREA1'
		cc.commands.size() == 2
		
		and: "the first command removes the old procedure call specification"
		RemoveAreaProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command creates the new procedure call specification"
		CreateAreaProcedureCallSpecificationCommand command2 = cc.commands[1]
		command2.area == area1
		command2.callStatementArgument == 'IDMSDCOM BEFORE'
			
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.AREA_NAME] == 'TESTAREA1'
	}
	
	def "An assertion error is thrown when no area procedure calls exist and none are added"() {
		
		given: "an area with no procedure call specifications and a procedure call command factory"
		SchemaArea area = area("name 'TESTAREA'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		when: "creating the command for changing the procedure call function"
		factory.createCommand(area, [ ] )
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'nothing to do'
	}
	
	def "Full mix for an area"() {
		
		given: "an area with several procedure call specifications"
		SchemaArea area = area("""name 'TESTAREA'
			call 'KEEPREF1 BEFORE'
			call 'KEEPREF1 AFTER'
			call 'KEEPNOR1 BEFORE'
			call 'KEEPNOR1 AFTER'
			call 'OBSOLET1 BEFORE'
			call 'OBSOLET1 AFTER'
			call 'KEEPREF2 BEFORE'
			call 'OBSOLET2 AFTER'
			call 'KEEPNOR2 BEFORE'
		""")
		
		and: "another area to reference procedures that are to be kept but will no longer referenced"
		     "by the area"
		SchemaArea anotherArea = new AreaModelBuilder( schema : area.schema ).build {
			name 'ANOTHER'
			call 'KEEPNOR1 BEFORE'
			call 'KEEPNOR2 BEFORE'
		}
		
		and: "a new set of procedure call specifications"
		def callStatementArguments = [ 
			'KEEPREF1 BEFORE',
			'KEEPREF2 AFTER',
			'NEW1 BEFORE',
			'NEW1 AFTER',
			'NEW2 BEFORE' ]
		
		expect:
		area.schema.procedures.size() == 6
		def oldProcedureCallSpecifications = new ArrayList<>(area.procedures)
		oldProcedureCallSpecifications.size() == 9
		anotherArea.procedures.size() == 2
		
		and: "a procedure call command factory"
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		when: "creating the command"
		ModelChangeCompoundCommand cc = factory.createCommand(area, callStatementArguments)
		
		then: "a compound command composed of 18 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for area TESTAREA'
		cc.commands.size() == 18
		
		and: "the first 9 commands remove the old procedure call specifications from the area"
		cc.commands[0] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[1] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[2] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[3] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[4] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[5] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[6] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[7] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[8] instanceof RemoveAreaProcedureCallSpecificationCommand
		cc.commands[0].callSpec.is(oldProcedureCallSpecifications[0])
		cc.commands[1].callSpec.is(oldProcedureCallSpecifications[1])
		cc.commands[2].callSpec.is(oldProcedureCallSpecifications[2])
		cc.commands[3].callSpec.is(oldProcedureCallSpecifications[3])
		cc.commands[4].callSpec.is(oldProcedureCallSpecifications[4])
		cc.commands[5].callSpec.is(oldProcedureCallSpecifications[5])
		cc.commands[6].callSpec.is(oldProcedureCallSpecifications[6])
		cc.commands[7].callSpec.is(oldProcedureCallSpecifications[7])
		cc.commands[8].callSpec.is(oldProcedureCallSpecifications[8])
		
		and: "the 10th and 11th command remove the 2 obsolete procedures"
		cc.commands[9] instanceof DeleteProcedureCommand
		cc.commands[10] instanceof DeleteProcedureCommand		
		cc.commands[9].procedure.name == 'OBSOLET1'
		cc.commands[10].procedure.name == 'OBSOLET2'
		
		and: "the 12th and 13th command create the 2 new procedures"
		cc.commands[11] instanceof CreateProcedureCommand
		cc.commands[12] instanceof CreateProcedureCommand
		cc.commands[11].schema == area.schema
		cc.commands[11].procedureName == 'NEW1'
		cc.commands[12].schema == area.schema
		cc.commands[12].procedureName == 'NEW2'
				
		and: "the last 5 commands create the new procedure call specifications"
		cc.commands[13] instanceof CreateAreaProcedureCallSpecificationCommand
		cc.commands[14] instanceof CreateAreaProcedureCallSpecificationCommand
		cc.commands[15] instanceof CreateAreaProcedureCallSpecificationCommand
		cc.commands[16] instanceof CreateAreaProcedureCallSpecificationCommand
		cc.commands[17] instanceof CreateAreaProcedureCallSpecificationCommand
		cc.commands[13].area == area
		cc.commands[13].callStatementArgument == 'KEEPREF1 BEFORE'
		cc.commands[14].area == area
		cc.commands[14].callStatementArgument == 'KEEPREF2 AFTER'
		cc.commands[15].area == area
		cc.commands[15].callStatementArgument == 'NEW1 BEFORE'
		cc.commands[16].area == area
		cc.commands[16].callStatementArgument == 'NEW1 AFTER'
		cc.commands[17].area == area
		cc.commands[17].callStatementArgument == 'NEW2 BEFORE'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_AREA_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.AREA_NAME] == 'TESTAREA'
	}
	
	def "Change the only record procedure call verb"() {
		
		given: "a record with 1 procedure call specification and a procedure call command factory"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'IDMSCOMP BEFORE'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		record.procedures.size() == 1
		RecordProcedureCallSpecification callSpec = record.procedures[0]
		
		when: "creating the command for changing the procedure call function"
		ModelChangeCompoundCommand cc = factory.createCommand(record, [ 'IDMSCOMP AFTER' ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for record TESTRECORD'
		cc.commands.size() == 2
		
		and: "the first command removes the old procedure call specification"
		RemoveRecordProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command creates the new procedure call specification"
		CreateRecordProcedureCallSpecificationCommand command2 = cc.commands[1]
		command2.record == record
		command2.callStatementArgument == 'IDMSCOMP AFTER'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.RECORD_NAME] == 'TESTRECORD'
	}
	
	def "Create first record procedure call specification and a new procedure"() {
		
		given: "a record without procedure calls and a procedure call command factory"
		SchemaRecord record = record("name 'TESTRECORD'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		when: "creating the command for 1 call to a non-existing procedure"
		ModelChangeCompoundCommand cc = factory.createCommand(record, [ 'TESTPROC BEFORE' ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for record TESTRECORD'
		cc.commands.size() == 2
		
		and: "the first command creates the procedure"
		CreateProcedureCommand command1 = cc.commands[0]
		command1.schema == record.schema
		command1.procedureName == 'TESTPROC'
		
		and: "the second command creates the new procedure call specification"
		CreateRecordProcedureCallSpecificationCommand command2 = cc.commands[1]
		command2.record == record
		command2.callStatementArgument == 'TESTPROC BEFORE'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.RECORD_NAME] == 'TESTRECORD'
	}
	
	def "Remove the only record procedure call specification and an obsolete procedure"() {
		
		given: "a record with 1 procedure call and a procedure call command factory"
		SchemaRecord record = record("name 'TESTRECORD'\n call 'TESTPROC BEFORE'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		record.procedures.size() == 1
		RecordProcedureCallSpecification callSpec = record.procedures[0]
		Procedure procedure = callSpec.procedure
		
		when: "creating the command which removes the procedure call"
		ModelChangeCompoundCommand cc = factory.createCommand(record, [ ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for record TESTRECORD'
		cc.commands.size() == 2
		
		and: "the first command removes the old procedure call specification"
		RemoveRecordProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command removes the obsolete procedure"
		DeleteProcedureCommand command2 = cc.commands[1]
		command2.procedure == procedure
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.RECORD_NAME] == 'TESTRECORD'
	}
	
	def "Replace a record procedure call specification, remove an obsolete procedure and create a new one"() {
		
		given: "a record with 1 procedure call and a procedure call command factory"
		SchemaRecord record = record("name 'TESTRECORD'\ncall 'IDMSCOMP BEFORE'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		record.procedures.size() == 1
		RecordProcedureCallSpecification callSpec = record.procedures[0]
		Procedure procedure = callSpec.procedure
		
		when: "creating the command for only 1 call to a non-existing procedure"
		ModelChangeCompoundCommand cc = factory.createCommand(record, [ 'IDMSDCOM AFTER' ] )
		
		then: "a compound command composed of 4 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for record TESTRECORD'
		cc.commands.size() == 4
		
		and: "the first command removes the old procedure call specification"
		RemoveRecordProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command removes the obsolete procedure"
		DeleteProcedureCommand command2 = cc.commands[1]
		command2.procedure == procedure
		
		and: "the third command creates the procedure"
		CreateProcedureCommand command3 = cc.commands[2]
		command3.schema == record.schema
		command3.procedureName == 'IDMSDCOM'
		
		and: "the fourth command creates the new procedure call specification"
		CreateRecordProcedureCallSpecificationCommand command4 = cc.commands[3]
		command4.record == record
		command4.callStatementArgument == 'IDMSDCOM AFTER'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.RECORD_NAME] == 'TESTRECORD'
	}
	
	def "Remove a record procedure call specification while retaining the procedure"() {
		
		given: "a schema with 2 records, each with a procedure call specification referring to"
			   "the same procedure, and a procedure call command factory"
		SchemaRecord record1 = new RecordModelBuilder().build {
			name 'TESTRECORD1'
			call 'IDMSCOMP BEFORE'
		}
		SchemaRecord record2 = new RecordModelBuilder( schema : record1.schema ).build {
			name 'TESTRECORD2'
			call 'IDMSCOMP BEFORE'
			call 'IDMSDCOM BEFORE'
		}
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		expect: "the procedure call exists"
		record1.procedures.size() == 1
		RecordProcedureCallSpecification callSpec = record1.procedures[0]
		
		when: "creating the command for changing the procedure call function for the 1st record"
		ModelChangeCompoundCommand cc = factory.createCommand(record1, [ 'IDMSDCOM BEFORE' ] )
		
		then: "a compound command composed of 2 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for record TESTRECORD1'
		cc.commands.size() == 2
		
		and: "the first command removes the old procedure call specification"
		RemoveRecordProcedureCallSpecificationCommand command1 = cc.commands[0]
		command1.callSpec == callSpec
		
		and: "the second command creates the new procedure call specification"
		CreateRecordProcedureCallSpecificationCommand command2 = cc.commands[1]
		command2.record == record1
		command2.callStatementArgument == 'IDMSDCOM BEFORE'
			
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.RECORD_NAME] == 'TESTRECORD1'
	}
	
	def "An assertion error is thrown when no record procedure calls exist and none are added"() {
		
		given: "a record with no procedure call specifications and a procedure call command factory"
		SchemaRecord record = record("name 'TESTRECORD'")
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		when: "creating the command for changing the procedure call function"
		factory.createCommand(record, [ ] )
		
		then: "an AssertionError is thrown"
		def e = thrown(AssertionError)
		e.message.startsWith 'nothing to do'
	}
	
	def "Full mix for a record"() {
		
		given: "a record with several procedure call specifications"
		SchemaRecord record = record("""name 'TESTRECORD'
			call 'KEEPREF1 BEFORE'
			call 'KEEPREF1 AFTER'
			call 'KEEPNOR1 BEFORE'
			call 'KEEPNOR1 AFTER'
			call 'OBSOLET1 BEFORE'
			call 'OBSOLET1 AFTER'
			call 'KEEPREF2 BEFORE'
			call 'OBSOLET2 AFTER'
			call 'KEEPNOR2 BEFORE'
		""")
		
		and: "an area to reference procedures that are to be kept but will no longer referenced"
			 "by the record"
		SchemaArea anArea = new AreaModelBuilder( schema : record.schema ).build {
			name 'ANOTHER'
			call 'KEEPNOR1 BEFORE'
			call 'KEEPNOR2 BEFORE'
		}
		
		and: "a new set of procedure call specifications"
		def callStatementArguments = [
			'KEEPREF1 BEFORE',
			'KEEPREF2 AFTER',
			'NEW1 BEFORE',
			'NEW1 AFTER',
			'NEW2 BEFORE' ]
		
		expect:
		record.schema.procedures.size() == 6
		def oldProcedureCallSpecifications = new ArrayList<>(record.procedures)
		oldProcedureCallSpecifications.size() == 9
		anArea.procedures.size() == 2
		
		and: "a procedure call command factory"
		ProcedureCallCommandFactory factory = new ProcedureCallCommandFactory()
		
		when: "creating the command"
		ModelChangeCompoundCommand cc = factory.createCommand(record, callStatementArguments)
		
		then: "a compound command composed of 18 commands is returned"
		cc
		cc.label == 'Set procedure call specifications for record TESTRECORD'
		cc.commands.size() == 18
		
		and: "the first 9 commands remove the old procedure call specifications from the record"
		cc.commands[0] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[1] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[2] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[3] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[4] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[5] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[6] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[7] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[8] instanceof RemoveRecordProcedureCallSpecificationCommand
		cc.commands[0].callSpec.is(oldProcedureCallSpecifications[0])
		cc.commands[1].callSpec.is(oldProcedureCallSpecifications[1])
		cc.commands[2].callSpec.is(oldProcedureCallSpecifications[2])
		cc.commands[3].callSpec.is(oldProcedureCallSpecifications[3])
		cc.commands[4].callSpec.is(oldProcedureCallSpecifications[4])
		cc.commands[5].callSpec.is(oldProcedureCallSpecifications[5])
		cc.commands[6].callSpec.is(oldProcedureCallSpecifications[6])
		cc.commands[7].callSpec.is(oldProcedureCallSpecifications[7])
		cc.commands[8].callSpec.is(oldProcedureCallSpecifications[8])
		
		and: "the 10th and 11th command remove the 2 obsolete procedures"
		cc.commands[9] instanceof DeleteProcedureCommand
		cc.commands[10] instanceof DeleteProcedureCommand
		cc.commands[9].procedure.name == 'OBSOLET1'
		cc.commands[10].procedure.name == 'OBSOLET2'
		
		and: "the 12th and 13th command create the 2 new procedures"
		cc.commands[11] instanceof CreateProcedureCommand
		cc.commands[12] instanceof CreateProcedureCommand
		cc.commands[11].schema == record.schema
		cc.commands[11].procedureName == 'NEW1'
		cc.commands[12].schema == record.schema
		cc.commands[12].procedureName == 'NEW2'
				
		and: "the last 5 commands create the new procedure call specifications"
		cc.commands[13] instanceof CreateRecordProcedureCallSpecificationCommand
		cc.commands[14] instanceof CreateRecordProcedureCallSpecificationCommand
		cc.commands[15] instanceof CreateRecordProcedureCallSpecificationCommand
		cc.commands[16] instanceof CreateRecordProcedureCallSpecificationCommand
		cc.commands[17] instanceof CreateRecordProcedureCallSpecificationCommand
		cc.commands[13].record == record
		cc.commands[13].callStatementArgument == 'KEEPREF1 BEFORE'
		cc.commands[14].record == record
		cc.commands[14].callStatementArgument == 'KEEPREF2 AFTER'
		cc.commands[15].record == record
		cc.commands[15].callStatementArgument == 'NEW1 BEFORE'
		cc.commands[16].record == record
		cc.commands[16].callStatementArgument == 'NEW1 AFTER'
		cc.commands[17].record == record
		cc.commands[17].callStatementArgument == 'NEW2 BEFORE'
		
		and: "the compound command's context is set up correctly"
		cc.context
		cc.context.modelChangeType == ModelChangeType.CHANGE_RECORD_PROCEDURE_CALL_SPECIFICATION
		cc.context.contextData[IContextDataKeys.RECORD_NAME] == 'TESTRECORD'
	}
	
}
