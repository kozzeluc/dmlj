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

import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.schema

import spock.lang.Specification

class ChangeSchemaCommentsCommandSpec extends Specification {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator")
	
	private static final String LINE1 = 'line 1'
	private static final String LINE2 = 'line 2'
	private static final String LINE3 = 'line 3'

	def "A schema's comments property is a list of strings"() {
		
		given: "a schema with an empty comments list"
		def schema = schema("name 'TESTSCHM'")
		
		expect: "the schema's comments list is not null but empty"
		schema.comments.empty
		
		and: "a new comments value consisting of 3 lines"
		String newValue = LINE1 + LINE_SEPARATOR + LINE2 + LINE_SEPARATOR + LINE3
		
		and: "a command constructed with the schema and new value"
		ChangeSchemaCommentsCommand command = new ChangeSchemaCommentsCommand(schema, newValue)
		
		when: "executing the command"
		command.execute()
		
		then: "the schema's comments list will contain the 3 lines"
		schema.comments == [ LINE1, LINE2, LINE3 ]
			
		when: "undoing the command"
		command.undo()
		
		then: "the schema's comments list is emptied"
		schema.comments.empty
		
		when: "redoing the command"
		command.redo()
		
		then: "the schema's comments list will contain the same 3 lines again"
		schema.comments == [ LINE1, LINE2, LINE3 ]
	}
	
	def "To remove a schema's comments, an empty string is passed to the command"() {
		
		given: "a schema with 3 lines of comments"
		def schema = schema("name 'TESTSCHM'\ncomments 'line 1'\ncomments 'line 2'\ncomments 'line 3'")
		
		expect: "the schema's comments list contains the 3 lines"
		schema.comments == [ LINE1, LINE2, LINE3 ]
		
		and: "an empty new comments value"
		String newValue = ""
		
		and: "a command constructed with the schema and new value"
		ChangeSchemaCommentsCommand command = new ChangeSchemaCommentsCommand(schema, newValue)
		
		when: "executing the command"
		command.execute()
		
		then: "the schema's comments list is emptied"
		schema.comments == [ ]
			
		when: "undoing the command"
		command.undo()
		
		then: "the schema's comments list contains the 3 lines again"
		schema.comments == [ LINE1, LINE2, LINE3 ]
		
		when: "redoing the command"
		command.redo()
		
		then: "the schema's comments list is emptied again"
		schema.comments == [ ]
	}
	
	def "The command's 'schema' construction argument must not be null"() {
		
		given: "a command constructed with a null 'schema' argument"
		ChangeSchemaCommentsCommand command = new ChangeSchemaCommentsCommand(null, "")
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		AssertionError e = thrown(AssertionError)
		
		and: "the assertion error's message indicates what's wrong"
		e.message.startsWith 'schema is null'
	}
	
	def "The command's 'newValue' construction argument must not be null"() {
		
		given: "a schema"
		def schema = schema("name 'TESTSCHM'")
		
		and: "a command constructed with a null 'newValue' argument"
		ChangeSchemaCommentsCommand command = new ChangeSchemaCommentsCommand(schema, null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		AssertionError e = thrown(AssertionError)
		
		and: "the assertion error's message indicates what's wrong"
		e.message.startsWith 'newValue is null'
	}
	
}
