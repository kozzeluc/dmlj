/**
 * Copyright (C) 2015  Luc Hermans
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

import static org.lh.dmlj.schema.editor.testtool.TestTools.compare
import static org.lh.dmlj.schema.editor.testtool.TestTools.snapshot
import static org.junit.Assert.fail

import org.eclipse.core.runtime.AssertionFailedException
import org.eclipse.gef.commands.Command
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.VsamLengthType
import org.lh.dmlj.schema.editor.testtool.SchemaEntityBuilder

import spock.lang.Specification

class MakeRecordVsamCommandSpec extends Specification implements SchemaEntityBuilder {
	
	private SchemaRecord createCalcRecord() {
		SchemaRecord record = record {
			calc {
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
			}
			elements {
				element 'DEPT-ID-0410' {
					level 2
					picture '9(4)'
				}
			}
		}
		assert record
		assert record.locationMode == LocationMode.CALC
		assert record.calcKey
		assert record.keys.size() == 1
		assert record.elements.size() == 1
		assert record.elements[0].keyElements.size() == 1
		assert record.elements[0].keyElements[0].key == record.calcKey
		record
	}
	
	private SchemaRecord createDirectRecordThatIsMemberOfaSet() {
		Schema schema = schema {
			set {
			}
		}
		assert schema
		assert schema.sets.size() == 1
		assert schema.sets[0].members.size() == 1
		SchemaRecord record = schema.sets[0].members[0].record
		assert record
		assert record.locationMode == LocationMode.DIRECT
		assert record.memberRoles.size() == 1
		record
	}
	
	private SchemaRecord createDirectRecordThatIsOwnerOfaSet() {
		Schema schema = schema {
			set {
			}		
		}
		assert schema
		assert schema.sets.size() == 1
		SchemaRecord record = schema.sets[0].owner.record
		assert record
		assert record.locationMode == LocationMode.DIRECT
		assert record.ownerRoles.size() == 1
		record
	}
	
	private SchemaRecord createStandaloneDirectRecord() {
		SchemaRecord record = record { }
		assert record
		assert record.locationMode == LocationMode.DIRECT
		record
	}
	
	private SchemaRecord createViaRecord() {
		SchemaRecord record = record {
			via {
				set 'SET1'
			}
		}
		assert record
		assert record.locationMode == LocationMode.VIA
		assert record.viaSpecification
		assert record.viaSpecification.set
		assert record.viaSpecification.set.name == 'SET1'
		record
	}
	
	private SchemaRecord createVsamCalcRecord() {
		SchemaRecord record = record {
			vsamCalc {
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
			}
			elements {
				element 'DEPT-ID-0410' {
					level 2
					picture '9(4)'
				}
			}
		}
		assert record
		assert record.locationMode == LocationMode.VSAM_CALC
		assert record.calcKey
		assert record.keys.size() == 1
		assert record.elements.size() == 1
		assert record.elements[0].keyElements.size() == 1
		assert record.elements[0].keyElements[0].key == record.calcKey
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
		record
	}

	private SchemaRecord createVsamRecord() {
		SchemaRecord record = record {
			vsam
		}
		assert record
		assert record.locationMode == LocationMode.VSAM
		assert record.vsamType
		record
	}

	def "CALC record (not supported)"() {
		
		given: "a CALC record and the appropriate command"
		SchemaRecord record = createCalcRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: record should be DIRECT or VSAM CALC')
	}
	
	def "VIA record (not supported)"() {
		
		given: "a VIA record and the appropriate command"
		SchemaRecord record = createViaRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: record should be DIRECT or VSAM CALC')
	}
	
	def "VSAM record (not supported)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createVsamRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: record should be DIRECT or VSAM CALC')
	}
	
	def "DIRECT record participating in a set as the owner (not supported)"() {
		
		given: "a DIRECT record that participates in a set and the appropriate command"
		SchemaRecord record = createDirectRecordThatIsOwnerOfaSet()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: cannot make record VSAM because it participates in 1 or more non-VSAM sets')
	}
	
	def "DIRECT record participating in a set as a member (not supported)"() {
		
		given: "a DIRECT record that participates in a set and the appropriate command"
		SchemaRecord record = createDirectRecordThatIsMemberOfaSet()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: cannot make record VSAM because it participates in 1 or more non-VSAM sets')
	}
	
	def "DIRECT record (execute)"() {
		
		given: "a DIRECT record and the appropriate command"
		SchemaRecord record = createStandaloneDirectRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made VSAM"
		record.locationMode == LocationMode.VSAM
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
	}
	
	def "DIRECT record (undo)"() {
		
		given: "a DIRECT record and the appropriate command"
		SchemaRecord record = createStandaloneDirectRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)		
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "DIRECT record (redo)"() {
		
		given: "a DIRECT record and the appropriate command"
		SchemaRecord record = createStandaloneDirectRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM CALC record (execute)"() {
		
		given: "a VSAM CALC record and the appropriate command"
		SchemaRecord record = createVsamCalcRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made VSAM"
		record.locationMode == LocationMode.VSAM
		assert !record.calcKey
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
	}
	
	def "VSAM CALC record (undo)"() {
		
		given: "a VSAM CALC record and the appropriate command"
		SchemaRecord record = createVsamCalcRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)		
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM CALC record (redo)"() {
		
		given: "a VSAM CALC record and the appropriate command"
		SchemaRecord record = createVsamCalcRecord()
		Command command = new MakeRecordVsamCommand(record)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
}
