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

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail
import static org.lh.dmlj.schema.editor.testtool.TestTools.asObjectGraph
import static org.lh.dmlj.schema.editor.testtool.TestTools.asSyntax
import static org.lh.dmlj.schema.editor.testtool.TestTools.asXmi
import static org.lh.dmlj.schema.editor.testtool.TestTools.compare
import static org.lh.dmlj.schema.editor.testtool.TestTools.snapshot

import org.eclipse.core.runtime.AssertionFailedException
import org.eclipse.gef.commands.Command
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set
import org.lh.dmlj.schema.ViaSpecification
import org.lh.dmlj.schema.VsamLengthType
import org.lh.dmlj.schema.editor.dsl.builder.model.HasModelBuilderShortcuts;

import spock.lang.Specification

class MakeRecordDirectCommandSpec extends Specification implements HasModelBuilderShortcuts {
	
	private SchemaRecord createCalcRecord() {
		SchemaRecord record = record {
			calc {
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
			"""
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
	
	private SchemaRecord createStandaloneVsamCalcRecord() {
		SchemaRecord record = record {
			name 'RECORD1'
			vsamCalc {
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
			}
			elements """
                02 DEPT-ID-0410 picture 9(4)
			"""
		}
		assert record
		assert record.locationMode == LocationMode.VSAM_CALC
		assert record.calcKey
		assert record.keys.size() == 1
		assert record.keys[0] == record.calcKey
		assert record.elements.size() == 1
		assert record.elements[0].keyElements.size() == 1
		assert record.elements[0].keyElements[0].key == record.calcKey
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
		record
	}

	private SchemaRecord createStandaloneVsamRecord() {
		SchemaRecord record = record {
			name 'RECORD1'
			vsam
		}
		assert record
		assert record.locationMode == LocationMode.VSAM
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
		assert !record.memberRoles
		record
	}
	
	private SchemaRecord createVsamCalcRecordThatIsMemberOfVsamIndex() {
		Schema schema = schema { 
			record 'RECORD1' {
				vsamCalc {
					element 'DEPT-ID-0410'
					duplicates 'NOT ALLOWED'
				}
				elements """
                    02 DEPT-ID-0410 picture 9(4)
                """
			}
			set 'SET1' {
				vsamIndex
				member 'RECORD1'
			}
		}
		SchemaRecord record = schema.getRecord('RECORD1')
		Set set = schema.getSet('SET1')
		assert record
		assert set
		assert record.locationMode == LocationMode.VSAM_CALC
		assert record.calcKey
		assert record.keys.size() == 2
		assert record.keys[0] == record.calcKey
		assert record.keys[1] == set.members[0].sortKey
		assert record.elements.size() == 1
		assert record.elements[0].keyElements.size() == 2
		assert record.elements[0].keyElements[0].key == record.calcKey
		assert record.elements[0].keyElements[1].key == set.members[0].sortKey
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
		assert record.memberRoles.size() == 1
		assert record.memberRoles[0].set.name == 'SET1'
		record
	}
		
	private SchemaRecord createVsamRecordThatIsMemberOfVsamIndex() {
		Schema schema = schema {
			record 'RECORD1' {
				vsam
			}			
			set 'SET1' {
				vsamIndex
				member 'RECORD1' 
			}
		}
		SchemaRecord record = schema.getRecord('RECORD1')
		assert record
		assert record.locationMode == LocationMode.VSAM
		assert record.vsamType
		assert record.vsamType.lengthType == VsamLengthType.FIXED
		assert !record.vsamType.spanned
		assert record.memberRoles.size() == 1
		assert record.memberRoles[0].set.name == 'SET1'
		record
	}
	
	def "CALC record (execute)"() {
		
		given: "a CALC record and the appropriate command"
		SchemaRecord record = createCalcRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made DIRECT"
		LocationMode.DIRECT == record.locationMode
		!record.calcKey
		!record.keys
		record.elements.size() == 1
		!record.elements[0].keyElements
	}
	
	def "CALC record (undo)"() {
		
		given: "a CALC record and the appropriate command"
		SchemaRecord record = createCalcRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)		
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))		
	}
	
	def "CALC record (redo)"() {
		
		given: "a CALC record and the appropriate command"
		SchemaRecord record = createCalcRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))	
	}
	
	def "VIA record (execute)"() {
		
		given: "a VIA record and the appropriate command"
		SchemaRecord record = createViaRecord()
		ViaSpecification viaSpecification = record.viaSpecification
		Set set = viaSpecification.set
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made DIRECT"
		LocationMode.DIRECT == record.locationMode
		!record.viaSpecification
		record.schema.getSet(set.name) == set
		set.viaMembers.indexOf(viaSpecification) == -1
	}
	
	def "VIA record (undo)"() {
		
		given: "a VIA record and the appropriate command"
		SchemaRecord record = createViaRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)		
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VIA record (redo)"() {
		
		given: "a VIA record and the appropriate command"
		SchemaRecord record = createViaRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM record (execute; record is member of VSAM index so the command fails)"() {
		
		given: "a VSAM record that is a member of a VSAM index, and the appropriate command"
		SchemaRecord record = createVsamRecordThatIsMemberOfVsamIndex()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: cannot make record DIRECT because it is a member of a VSAM index')
	}
	
	def "VSAM record (execute)"() {
		
		given: "a standalone VSAM record and the appropriate command"
		SchemaRecord record = createStandaloneVsamRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made DIRECT"
		LocationMode.DIRECT == record.locationMode
		!record.vsamType
	}
	
	def "VSAM record (undo)"() {
		
		given: "a standalone VSAM record and the appropriate command"
		SchemaRecord record = createStandaloneVsamRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)		
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM record (redo)"() {
		
		given: "a standalone VSAM record and the appropriate command"
		SchemaRecord record = createStandaloneVsamRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM CALC record (execute; record is member of VSAM index so the command fails)"() {
		
		given: "a VSAM record that is a member of a VSAM index, and the appropriate command"
		SchemaRecord record = createVsamCalcRecordThatIsMemberOfVsamIndex()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: cannot make record DIRECT because it is a member of a VSAM index')
	}
	
	def "VSAM CALC record (execute)"() {
		
		given: "a standalone VSAM CALC record and the appropriate command"
		SchemaRecord record = createStandaloneVsamCalcRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made DIRECT"
		LocationMode.DIRECT == record.locationMode
		!record.calcKey
		!record.keys
		record.elements.size() == 1
		!record.elements[0].keyElements
		!record.vsamType
	}
	
	def "VSAM CALC record (undo)"() {
		
		given: "a standalone VSAM CALC record and the appropriate command"
		SchemaRecord record = createStandaloneVsamCalcRecord()
		Command command = new MakeRecordDirectCommand(record)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)		
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM CALC record (redo)"() {
		
		given: "a standalone VSAM CALC record and the appropriate command"
		SchemaRecord record = createStandaloneVsamCalcRecord()
		Command command = new MakeRecordDirectCommand(record)
		
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
