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

import org.eclipse.core.runtime.AssertionFailedException
import org.eclipse.gef.commands.Command
import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.VsamLengthType
import org.lh.dmlj.schema.editor.dsl.builder.model.HasModelBuilderShortcuts;

import spock.lang.Specification
import spock.lang.Unroll

class MakeRecordVsamCalcCommandSpec extends Specification implements HasModelBuilderShortcuts {

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
	
	private SchemaRecord createRecordWithMultipleElements() {
		SchemaRecord record = record {
			name 'RECORD1'
			elements """
				02 ELEMENT1 picture X
				02 ELEMENT2 picture X
				02 ELEMENT3 picture X
				02 ELEMENT4 picture X
				02 ELEMENT5 picture X
			"""
		}
		assert record
		assert record.locationMode == LocationMode.DIRECT
		assert record.elements.size() == 5
		record
	}
	
	private SchemaRecord createStandaloneDirectRecord() {
		SchemaRecord record = record { }
		assert record
		assert record.locationMode == LocationMode.DIRECT
		record
	}
	
	private SchemaRecord createStandaloneVsamRecord() {
		SchemaRecord record = record {
			vsam
		}
		assert record
		assert record.locationMode == LocationMode.VSAM
		assert record.vsamType
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
			elements """
                02 DEPT-ID-0410 picture 9(4)
			"""
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
	
	private SchemaRecord createVsamRecordThatIsMemberOfVsamIndex() {
		Schema schema = schema {
			record {
				name 'RECORD1'
				vsam
			}
			set {
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
		assert record.memberRoles[0].set.vsamIndex
		assert record.memberRoles[0].set.vsamIndex.record == record
		record
	}
	
	@Unroll
	def "the only valid duplicates options are NOT ALLOWED and UNORDERED"() {
		
		given: "a record and the appropriate command"
		SchemaRecord record = createStandaloneDirectRecord()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ], 
														duplicatesOption)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith("assertion failed: $message")
		
		where: ""
		duplicatesOption			| message
		DuplicatesOption.FIRST		| "unsupported duplicates option: FIRST"
		DuplicatesOption.LAST		| "unsupported duplicates option: LAST"
		DuplicatesOption.BY_DBKEY 	| "unsupported duplicates option: BY_DBKEY"
	}
	
	def "CALC record (not supported)"() {
		
		given: "a CALC record and the appropriate command"
		SchemaRecord record = createCalcRecord()
		Command command = new MakeRecordVsamCalcCommand(record, new ArrayList<Element>(), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: record not DIRECT or VSAM')
	}
	
	def "VIA record (not supported)"() {
		
		given: "a VIA record and the appropriate command"
		SchemaRecord record = createViaRecord()
		Command command = new MakeRecordVsamCalcCommand(record, new ArrayList<Element>(), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: record not DIRECT or VSAM')
	}
	
	def "VSAM CALC record (not supported)"() {
		
		given: "a VIA record and the appropriate command"
		SchemaRecord record = createVsamCalcRecord()
		Command command = new MakeRecordVsamCalcCommand(record, new ArrayList<Element>(), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: record not DIRECT or VSAM')
	}
	
	def "DIRECT record participating in a set as the owner (not supported)"() {
		
		given: "a DIRECT record that participates in a set and the appropriate command"
		SchemaRecord record = createDirectRecordThatIsOwnerOfaSet()
		Command command = new MakeRecordVsamCalcCommand(record, new ArrayList<Element>(), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: cannot make record VSAM CALC because it participates in 1 or more non-VSAM sets')
	}
	
	def "DIRECT record participating in a set as a member (not supported)"() {
		
		given: "a DIRECT record that participates in a set and the appropriate command"
		SchemaRecord record = createDirectRecordThatIsMemberOfaSet()
		Command command = new MakeRecordVsamCalcCommand(record, new ArrayList<Element>(), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error is thrown"
		def error = thrown(AssertionFailedException)
		error.message.startsWith('assertion failed: cannot make record VSAM CALC because it participates in 1 or more non-VSAM sets')
	}
	
	def "DIRECT record (execute)"() {
		
		given: "a DIRECT record and the appropriate command"
		SchemaRecord record = createStandaloneDirectRecord()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ], 
														DuplicatesOption.NOT_ALLOWED)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made VSAM CALC"
		record.locationMode == LocationMode.VSAM_CALC
		record.calcKey
		record.calcKey.record == record
		record.calcKey.elements.size() == 1
		record.calcKey.elements[0].element == record.elements[0]
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.memberRole
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
	}
	
	def "DIRECT record (undo)"() {
		
		given: "a DIRECT record and the appropriate command"
		SchemaRecord record = createStandaloneDirectRecord()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ], 
														DuplicatesOption.NOT_ALLOWED)
		
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
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ], 
														DuplicatesOption.NOT_ALLOWED)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "Standalone VSAM record (execute)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createStandaloneVsamRecord()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ],
														DuplicatesOption.UNORDERED)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made VSAM CALC"
		record.locationMode == LocationMode.VSAM_CALC
		record.calcKey
		record.calcKey.record == record
		record.calcKey.elements.size() == 1
		record.calcKey.elements[0].element == record.elements[0]
		record.calcKey.duplicatesOption == DuplicatesOption.UNORDERED
		!record.calcKey.memberRole
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
	}
	
	def "Standalone VSAM record (undo)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createStandaloneVsamRecord()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ],
														DuplicatesOption.UNORDERED)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "Standalone VSAM record (redo)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createStandaloneVsamRecord()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ],
														DuplicatesOption.NOT_ALLOWED)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM record that is a member of a VSAM index (execute)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createVsamRecordThatIsMemberOfVsamIndex()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ],
														DuplicatesOption.UNORDERED)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made VSAM CALC"
		record.locationMode == LocationMode.VSAM_CALC
		record.calcKey
		record.calcKey.record == record
		record.calcKey.elements.size() == 1
		record.calcKey.elements[0].element == record.elements[0]
		record.calcKey.duplicatesOption == DuplicatesOption.UNORDERED
		!record.calcKey.memberRole
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
		assert record.memberRoles.size() == 1
		assert record.memberRoles[0].set.vsamIndex
		assert record.memberRoles[0].set.vsamIndex.record == record
	}
	
	def "VSAM record that is a member of a VSAM index (undo)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createVsamRecordThatIsMemberOfVsamIndex()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ],
														DuplicatesOption.UNORDERED)
		
		when: "executing the command and then undoing it"
		def expected = snapshot(record.schema)
		command.execute()
		command.undo()
		
		then: "any modification is undone and the schema is exactly as it was before executing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "VSAM record that is a member of a VSAM index (redo)"() {
		
		given: "a VSAM record and the appropriate command"
		SchemaRecord record = createVsamRecordThatIsMemberOfVsamIndex()
		Command command = new MakeRecordVsamCalcCommand(record, [ record.elements[0] ],
														DuplicatesOption.NOT_ALLOWED)
		
		when: "executing the command, undoing it and then redoing it"
		command.execute()
		def expected = snapshot(record.schema)
		command.undo()
		command.redo()
		
		then: "the record (and schema) will be exactly as it (they) was (were) before undoing the"
			  "command"
		compare(expected, snapshot(record.schema))
	}
	
	def "multiple VSAM CALC key elements (contrasts with CA IDMS documentation)"() {
		
		given: "a record and the appropriate command"
		SchemaRecord record = createRecordWithMultipleElements()
		Command command = 
			new MakeRecordVsamCalcCommand(record, 
										  [ record.elements[0], record.elements[1], record.elements[2] ],
										  DuplicatesOption.NOT_ALLOWED)
		
		when: "executing the command"
		command.execute()
		
		then: "the record is made VSAM CALC and the VSAM CALC key contains more than 1 element"
		record.locationMode == LocationMode.VSAM_CALC
		record.calcKey
		record.calcKey.record == record
		record.calcKey.elements.size() == 3
		record.calcKey.elements[0].element == record.elements[0]
		record.calcKey.elements[1].element == record.elements[1]
		record.calcKey.elements[2].element == record.elements[2]
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.memberRole
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
	}
	
}
