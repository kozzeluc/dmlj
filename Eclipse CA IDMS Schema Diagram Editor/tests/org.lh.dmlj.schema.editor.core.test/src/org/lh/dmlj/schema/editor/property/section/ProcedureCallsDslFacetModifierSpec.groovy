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
package org.lh.dmlj.schema.editor.property.section

import static org.lh.dmlj.schema.ProcedureCallTime.AFTER
import static org.lh.dmlj.schema.ProcedureCallTime.BEFORE
import static org.lh.dmlj.schema.RecordProcedureCallVerb.EVERY_DML_FUNCTION
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.area
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.record

import org.codehaus.groovy.control.MultipleCompilationErrorsException
import org.eclipse.gef.commands.Command
import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.editor.command.ProcedureCallCommandFactory
import org.lh.dmlj.schema.editor.property.exception.DSLFacetValidationException

import spock.lang.Specification
import spock.lang.Unroll

class ProcedureCallsDslFacetModifierSpec extends Specification {
	
	static def REPLACE_UNDERSCORES_BY_SPACES = { " ${it.toString().replace('_', ' ')}" } 
	
	static def FUNCTIONS = [ '' ] + AreaProcedureCallFunction.values().collect(REPLACE_UNDERSCORES_BY_SPACES)
	static def FUNCTION_COUNT = FUNCTIONS.size()
	
	static def VERBS = [ '' ] + RecordProcedureCallVerb.values().collect(REPLACE_UNDERSCORES_BY_SPACES) 
	static def VERB_COUNT = VERBS.size()
	
	@Unroll("model name: #expectedValue")
	def "The model name equals the model's 'name' attribute"() {
		
		given: "a ProcedureCallsDslFacetModifier for a model"
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "asking for the model name"
		def modelName = dslFacetModifier.getModelName()
		
		then: "the model's name attribute is read"
		modelName == expectedValue
		
		where:
		model  						|| expectedValue
		area("name 'TESTAREA'")   	|| 'TESTAREA'
		record("name 'TESTRECORD'") || 'TESTRECORD'
	}
	
	@Unroll("model type: #expectedValue")
	def "The model type reflects the model's type (area/record)"() {
		
		given: "a ProcedureCallsDslFacetModifier for a model"
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "asking for the model name"
		def modelType = dslFacetModifier.getModelType()
		
		then: "the model's name attribute is read"
		modelType == expectedValue
		
		where:
		model  						|| expectedValue
		area("name 'TESTAREA'")   	|| 'area'
		record("name 'TESTRECORD'") || 'record'
	}
	
	@Unroll("original DSL facet (#type): #expectedValue")
	def "The original procedure call syntax is derived from the model"() {
		
		given: "a ProcedureCallsDslFacetModifier for a model"
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "asking for the original DSL facet"
		def facet = dslFacetModifier.originalFacetDefinition
		
		then: "the original DSL facet is returned"
		facet == expectedValue
		
		where:
		type 	 | model 											 					|| expectedValue
		'area' 	 | area('')		   								 						|| ''
		'area'	 | area("\ncallProcedure 'IDMSCOMP BEFORE READY EXCLUSIVE'") 			|| "callProcedure 'IDMSCOMP BEFORE READY EXCLUSIVE'"
		'area' 	 | area("\ncallProcedure 'ABC AFTER'\ncallProcedure 'DEF BEFORE'") 	 	|| "callProcedure 'ABC AFTER'\ncallProcedure 'DEF BEFORE'"
		'record' | record('')		   								 					|| ''
		'record' | record("\ncallProcedure 'IDMSCOMP BEFORE STORE'\nrecordId 440" )		|| "callProcedure 'IDMSCOMP BEFORE STORE'"
		'record' | record("\ncallProcedure 'ABC AFTER'\ncallProcedure 'DEF BEFORE'")  	|| "callProcedure 'ABC AFTER'\ncallProcedure 'DEF BEFORE'"
	}
	
	@Unroll("Empty/blank line: #modifiedSyntax")
	def "Empty and blank procedure call syntax lines are ignored and all lines are trimmed"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing valid modified facet syntax"
		dslFacetModifier.setModifiedFacetDefinition(modifiedSyntax)
		
		then: "the modified facet syntax is accepted and blank lines are removed"
		dslFacetModifier.modifiedFacetDefinition == expectedValue
		
		where:
		modifiedSyntax 						   							|| expectedValue
		"" 									   							|| ""
		" " 								   							|| ""
		"\n" 								   							|| ""
		" \n " 								   							|| ""
		"callProcedure 'XYZ BEFORE'\n" 				   					|| "callProcedure 'XYZ BEFORE'"
		" callProcedure 'XYZ BEFORE'\n\ncallProcedure 'ABC AFTER'"		|| "callProcedure 'XYZ BEFORE'\ncallProcedure 'ABC AFTER'"
		"callProcedure 'XYZ BEFORE'\n \n callProcedure 'ABC AFTER' "	|| "callProcedure 'XYZ BEFORE'\ncallProcedure 'ABC AFTER'"
	}
	
	@Unroll("valid area facet syntax: #modifiedSyntax")
	def "Non-empty area procedure call syntax contains 1 or more valid call statements and nothing else"() {
		
		given: "an area and a ProcedureCallsDslFacetModifier for that area"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing valid modified facet syntax"
		dslFacetModifier.setModifiedFacetDefinition(modifiedSyntax)
		
		then: "the modified facet syntax is accepted"
		notThrown(DSLFacetValidationException)
		
		where: "the argument for the call can be enclosed in single or double quotes"
		quote << [ "'" ] * FUNCTION_COUNT * 2 + [ '"' ] * FUNCTION_COUNT * 2
		callTime << [ BEFORE ] * FUNCTION_COUNT + [ AFTER ] * FUNCTION_COUNT + [ BEFORE ] * FUNCTION_COUNT + [ AFTER ] * FUNCTION_COUNT
		function << FUNCTIONS * 2 * 2
		modifiedSyntax = "callProcedure ${quote}XYZ $callTime$function$quote"
	}
	
	@Unroll("valid record facet syntax: #modifiedSyntax")
	def "Non-empty record procedure call syntax contains 1 or more valid call statements and nothing else"() {
		
		given: "a record and a ProcedureCallsDslFacetModifier for that record"
		def model = record("name 'TESTRECORD'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing valid modified facet syntax"
		dslFacetModifier.setModifiedFacetDefinition(modifiedSyntax)
		
		then: "the modified facet syntax is accepted"
		notThrown(DSLFacetValidationException)
		
		where: "the argument for the call can be enclosed in single or double quotes"
		quote << [ "'" ] * VERB_COUNT * 2 + [ '"' ] * VERB_COUNT * 2  
		callTime << [ BEFORE ] * VERB_COUNT + [ AFTER ] * VERB_COUNT + [ BEFORE ] * VERB_COUNT + [ AFTER ] * VERB_COUNT
		verb << VERBS * 2 * 2
		modifiedSyntax = "callProcedure ${quote}XYZ $callTime$verb$quote"
	}
	
	def "Procedure names are translated to uppercase"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing a lowercase procedure name"
		dslFacetModifier.setModifiedFacetDefinition("callProcedure 'xyz BEFORE'")
		
		then: "the procedure name is translated to uppercase"
		dslFacetModifier.modifiedFacetDefinition == "callProcedure 'XYZ BEFORE'"
	}
	
	@Unroll("invalid line: #invalidLine")
	def "Each relevant line in the modified procedure call syntax must start with \"call '\" or 'call \"' and end with a quote"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing an invalid line"
		dslFacetModifier.setModifiedFacetDefinition(invalidLine)
		
		then: "an exception is thrown"
		DSLFacetValidationException e = thrown(DSLFacetValidationException)
		e.message == "line 1 does not start with \"callProcedure '\" (or 'callProcedure \"') or end with a single quote:\n$invalidLine"
		!e.cause
		
		where:
		invalidLine 		|| _
		'x' 				|| _
		"callProcedure 'x" 	|| _
		'callProcedure "x' 	|| _
	}
	
	def "Each relevant line in the modified procedure call syntax is compiled by the Groovy compiler"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing an invalid line"
		dslFacetModifier.setModifiedFacetDefinition("callProcedure '''")
		
		then: "an exception is thrown"
		DSLFacetValidationException e = thrown(DSLFacetValidationException)
		e.message.startsWith('startup failed:')
		e.cause instanceof MultipleCompilationErrorsException
	}
	
	@Unroll("hasChanges(): #modifiedSyntax")
	def "hasChanges() tells whether modified DSL has been set and is different from the original"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = record("name 'TESTRECORD'\ncallProcedure 'XYZ BEFORE'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "passing valid modified facet syntax"
		dslFacetModifier.setModifiedFacetDefinition(modifiedSyntax)
		
		then: "the modified facet syntax is accepted and hasChanges() indicates if changes have been made"
		dslFacetModifier.hasChanges() == expectedValue
		
		where:
		modifiedSyntax 												|| expectedValue
		'' 															|| true
		"callProcedure 'XYZ BEFORE'" 								|| false
		"callProcedure 'xyz BEFORE'" 								|| false
		"callProcedure 'XYZ AFTER'"  								|| true
		"callProcedure 'XYZ BEFORE'\ncallProcedure 'ABC BEFORE'" 	|| true
	}
	
	def "hasChanges() returns false if no modified DSL facet has been set"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = record("name 'TESTRECORD'\ncallProcedure 'XYZ BEFORE'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "modified facet syntax is NOT set"
		boolean hasChanges = dslFacetModifier.hasChanges()
		
		then: "hasChanges() returns false"
		!hasChanges
	}
	
	def "hasChanges() returns false if only invalid modified DSL facet was set"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = record("name 'TESTRECORD'\ncallProcedure 'XYZ BEFORE'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "invalid facet syntax is set"
		dslFacetModifier.setModifiedFacetDefinition("invalid syntax")
		
		then: "a validation exception is thrown"
		thrown(DSLFacetValidationException)
		
		and: "hasChanges() returns false"
		!dslFacetModifier.hasChanges()
	}
	
	def "hasChanges() returns true if valid modified DSL was set before"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = record("name 'TESTRECORD'\ncallProcedure 'XYZ BEFORE'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "valid facet syntax is first set"
		dslFacetModifier.setModifiedFacetDefinition("callProcedure 'ABC BEFORE'")
		
		and: " invalid facet syntax is then set"
		dslFacetModifier.setModifiedFacetDefinition("invalid syntax")
		
		then: "a validation exception is thrown"
		thrown(DSLFacetValidationException)
		
		and: "hasChanges() returns true"
		dslFacetModifier.hasChanges()
	}
	
	def "hasChanges() returns false if the same valid DSL was last set successfully"() {
		
		given: "a model and a ProcedureCallsDslFacetModifier for that model"
		def model = record("name 'TESTRECORD'\ncallProcedure 'XYZ BEFORE'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "the same valid facet syntax is set"
		dslFacetModifier.setModifiedFacetDefinition("callProcedure 'XYZ BEFORE'")
		
		then: "hasChanges() returns false"
		!dslFacetModifier.hasChanges()
	}
	
	def "The area procedure calls command is created by a command factory"() {
		
		given: "a ProcedureCallsDslFacetModifier with modified facet syntax set"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		dslFacetModifier.setModifiedFacetDefinition("callProcedure 'XYZ BEFORE'")
		
		and: "a mock command factory that returns a mock command"
		Command command = Mock(Command)
		ProcedureCallCommandFactory commandFactory = Mock(ProcedureCallCommandFactory)
		dslFacetModifier.commandFactory = commandFactory
		
		when: "requesting for the command"
		Command returnedCommand = dslFacetModifier.getCommand()
		
		then: "(only) the right command factory's createCommand is invoked with the right arguments"
		      "(note that the 'callProcedure' keyword, as well as the quotes are stripped)"
		1 * commandFactory.createCommand(model, [ 'XYZ BEFORE' ]) >> command
		0 * commandFactory._
		
		and: "the command created by the command factory is returned"
		command.is(returnedCommand)
	}
	
	def "The area procedure calls command can only be created when the DSL facet has been modified"() {
		
		given: "a ProcedureCallsDslFacetModifier with NO modified facet syntax set"
		def model = area("name 'TESTAREA'")
		def dslFacetModifier = ProcedureCallsDslFacetModifier.forModel(model)
		
		when: "requesting for the command"
		Command returnedCommand = dslFacetModifier.getCommand()
		
		then: "an AssertionError is thrown"
		AssertionError e = thrown(AssertionError)
		e.message == 'command cannot be created: no changes. Expression: this.hasChanges()'
	}
	
}
