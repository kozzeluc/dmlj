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

import static org.junit.Assert.fail

import org.eclipse.core.runtime.AssertionFailedException
import org.lh.dmlj.schema.IndexedSetModeSpecification
import org.lh.dmlj.schema.Set
import org.lh.dmlj.schema.editor.dsl.builder.model.HasModelBuilderShortcuts

import spock.lang.Specification

class ChangeIndexedSetModeSpecificationCommandSpec 
	extends Specification implements HasModelBuilderShortcuts {
	
	private IndexedSetModeSpecification createIndexedSetModeSpecificationWithSymbolicIndex() {
		Set set = set { 
			name 'SET1'
			index 'SYMBOL1'
		}
		assert set
		assert set.indexedSetModeSpecification
		assert set.indexedSetModeSpecification.symbolicIndexName == 'SYMBOL1'
		assert !set.indexedSetModeSpecification.keyCount
		assert !set.indexedSetModeSpecification.displacementPageCount
		set.indexedSetModeSpecification	
	}
	
	private IndexedSetModeSpecification createIndexedSetModeSpecificationWithKeyCount() {
		Set set = set {
			name 'SET1'
			index {
				keys 3
			}
		}
		assert set
		assert set.indexedSetModeSpecification
		assert !set.indexedSetModeSpecification.symbolicIndexName
		assert set.indexedSetModeSpecification.keyCount.shortValue() == 3
		assert !set.indexedSetModeSpecification.displacementPageCount
		set.indexedSetModeSpecification
	}
	
	private IndexedSetModeSpecification createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages() {
		Set set = set {
			name 'SET1'
			index {
				keys 3
				displacement 1
			}
		}
		assert set
		assert set.indexedSetModeSpecification
		assert !set.indexedSetModeSpecification.symbolicIndexName
		assert set.indexedSetModeSpecification.keyCount.shortValue() == 3
		assert set.indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		set.indexedSetModeSpecification
	}
	
	def "check assertions before command execution: null set"() {
		
		when: "creating a command with a null 'set' argument"
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(null, null, null, null)
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: set is null')
	}
	
	def "check assertions before command execution: set without indexed set mode specification"() {
		
		given: "a command constructed with a set not having an indexed set mode specification"
		Set set = set { name 'SET1' }
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, null, null, null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: not an indexed set') 	
	}
	
	def "check assertions before command execution: neither symbolic index name, key count nor displacement specified"() {
		
		given: "a command constructed with neither symbolic index name nor key count and displacement"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, null, null, null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: neither symbolic index name nor key count specified')
	}
	
	def "check assertions before command execution: neither symbolic index name nor key count specified"() {
		
		given: "a command constructed with neither symbolic index name nor key count"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, null, null, Short.valueOf((short) 1));
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: neither symbolic index name nor key count specified')
	}
	
	def "check assertions before command execution: symbolic index name, key count and displacement specified"() {
		
		given: "a command constructed with symbolic, key count and displacement"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, "SYMBOL1", Short.valueOf((short) 1), Short.valueOf((short) 1))
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: symbolic index name AND key count specified')
	}
	
	def "check assertions before command execution: symbolic index name and key count specified"() {
		
		given: "a command constructed with both symbolic index name and key count"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, "SYMBOL1", Short.valueOf((short) 1), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: symbolic index name AND key count specified')
	}
	
	def "check assertions before command execution: symbolic index and displacement specified"() {
		
		given: "a command constructed with a symbolic index and displacement"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, "SYMBOL1", null, Short.valueOf((short) 1))
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: key count NOT specified')
	}
	
	def "check assertions before command execution: invalid symbolic index specified"() {
		
		given: "a command constructed with an invalid symbolic index"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, "123", null, null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: symbolicIndexName is invalid (first character must be a letter, @, $, or #)')
	}
	
	def "check assertions before command execution: invalid keyCount (below lower limit)"() {
		
		given: "a command constructed with an invalid key count"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, null, Short.valueOf((short) 2), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: keyCount is invalid (must be >= 3 and <= 8180)')
	}
	
	def "check assertions before command execution: invalid keyCount (above upper limit)"() {
		
		given: "a command constructed with an invalid key count"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, null, Short.valueOf((short) 8181), null)
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: keyCount is invalid (must be >= 3 and <= 8180)')
	}
	
	def "check assertions before command execution: invalid displacementPages (below lower limit)"() {
		
		given: "a command constructed with an invalid displacement"
		Set set = createIndexedSetModeSpecificationWithSymbolicIndex().set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(set, null, Short.valueOf((short) 3), Short.valueOf((short) 0))
		
		when: "executing the command"
		command.execute()
		
		then: "an assertion error occurs"
		def error = thrown(AssertionFailedException)
		error.message.equals('assertion failed: displacementPages is invalid (must be >= 1 and <= 32767)')
	}
	
	def "set symbolic index (execute)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 'SYMBOL1', null, null)
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be set to the specified value and the key count removed"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL1'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set symbolic index (undo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 'SYMBOL1', null, null)
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set symbolic index (redo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 'SYMBOL1', null, null)
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL1'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}

	def "change symbolic index (execute)"() {
		
		given: "an indexed set mode specification specifying a symbolic index name and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification = 
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command = 
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set, 
														 'SYMBOL2', null, null)
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be changed to the new value"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL2'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change symbolic index (undo)"() {
		
		given: "a set with an indexed set mode specification specifying a symbolic index name and"
			   "the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 'SYMBOL2', null, null)
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL1'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change symbolic index (redo)"() {
		
		given: "a set with an indexed set mode specification specifying a symbolic index name and"
			   "the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 'SYMBOL2', null, null)
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL2'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set key count (execute)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), null)
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be nullified and the key count set to the specified value"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount
		indexedSetModeSpecification.keyCount.shortValue() == 3
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set key count (undo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), null)
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL1'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set key count (redo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), null)
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount
		indexedSetModeSpecification.keyCount.shortValue() == 3
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change key count (execute)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 4), null)
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be nullified and the key count set to the specified value"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount
		indexedSetModeSpecification.keyCount.shortValue() == 4
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change key count (undo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 4), null)
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change key count (redo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 4), null)
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount
		indexedSetModeSpecification.keyCount.shortValue() == 4
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set displacement pages (execute)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 1))
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be nullified and the key count set to the specified value"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set displacement pages (undo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 1))
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set displacement pages (redo)"() {
		
		given: "an indexed set mode specification specifying a key count and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCount()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 1))
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change displacement pages (execute)"() {
		
		given: "an indexed set mode specification specifying a key count with displacement and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 2))
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be nullified and the key count set to the specified value"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount.shortValue() == 2
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change displacement pages (undo)"() {
		
		given: "an indexed set mode specification specifying a key count with displacement and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 2))
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change displacement pages (redo)"() {
		
		given: "an indexed set mode specification specifying a key count with displacement and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 2))
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount.shortValue() == 2
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set key count AND displacement pages (execute)"() {
		
		given: "an indexed set mode specification specifying a symbolic index and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 1))
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be nullified and the key count set to the specified value"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set key count AND displacement pages (undo)"() {
		
		given: "an indexed set mode specification specifying a symbolic index and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 1))
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		indexedSetModeSpecification.symbolicIndexName == 'SYMBOL1'
		!indexedSetModeSpecification.keyCount
		!indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "set key count AND displacement pages (redo)"() {
		
		given: "an indexed set mode specification specifying a symbolic index and the appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithSymbolicIndex()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 3), Short.valueOf((short) 1))
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount
		indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change key count AND displacement pages (execute)"() {
		
		given: "an indexed set mode specification specifying a key count and displacement, and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 4), Short.valueOf((short) 2))
		
		when: "executing the command"
		command.execute()
		
		then: "the symbolic index will be nullified and the key count set to the specified value"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 4
		indexedSetModeSpecification.displacementPageCount.shortValue() == 2
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change key count AND displacement pages (undo)"() {
		
		given: "an indexed set mode specification specifying a key count and displacement, and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 4), Short.valueOf((short) 2))
		
		when: "executing the command and subsequently undoing it"
		command.execute()
		command.undo()
		
		then: "the indexed set mode specification changes are undone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 3
		indexedSetModeSpecification.displacementPageCount.shortValue() == 1
		indexedSetModeSpecification.set.is(originalSet)
	}
	
	def "change key count AND displacement pages (redo)"() {
		
		given: "an indexed set mode specification specifying a key count and displacement, and the"
			   "appropriate command"
		IndexedSetModeSpecification indexedSetModeSpecification =
			createIndexedSetModeSpecificationWithKeyCountAndDisplacementPages()
		Set originalSet = indexedSetModeSpecification.set
		ChangeIndexedSetModeSpecificationCommand command =
			new ChangeIndexedSetModeSpecificationCommand(indexedSetModeSpecification.set,
														 null, Short.valueOf((short) 4), Short.valueOf((short) 2))
		
		when: "executing the command, undoing it and subsequently redoing it"
		command.execute()
		command.undo()
		command.redo()
		
		then: "the indexed set mode specification changes are redone"
		!indexedSetModeSpecification.symbolicIndexName
		indexedSetModeSpecification.keyCount.shortValue() == 4
		indexedSetModeSpecification.displacementPageCount.shortValue() == 2
		indexedSetModeSpecification.set.is(originalSet)
	}
	
}
