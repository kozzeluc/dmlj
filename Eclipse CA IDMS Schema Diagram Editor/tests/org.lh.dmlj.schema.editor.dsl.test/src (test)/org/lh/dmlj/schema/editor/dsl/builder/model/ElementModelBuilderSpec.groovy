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
package org.lh.dmlj.schema.editor.dsl.builder.model

import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Usage

import spock.lang.Unroll

class ElementModelBuilderSpec extends AbstractModelBuilderSpec {
	
	def "build the simpliest element, NOT referencing a given record, from a closure only"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element"
		def definition = '02 ELEMENT-1 picture X(8)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with its level, name and picture properties set to the"
			  "given values, with a usage of DISPLAY, that does NOT reference a record"		
		element
		element.level == 2
		element.name == 'ELEMENT-1'
		element.picture == 'X(8)'
		element.usage == Usage.DISPLAY
		!element.record
		element.baseName == element.name
		!element.children
		!element.keyElements
		!element.nullable
		!element.occursSpecification
		!element.parent
		!element.redefines
		!element.value
	}
	
	def "build the simpliest element, referencing a given record, from a closure only"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element"
		def definition = '02 ELEMENT2 picture X(8)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with its level, name and picture properties set to the"
			  "given values, with a usage of DISPLAY, that reference the given record"
		element
		element.level == 2
		element.name == 'ELEMENT2'
		element.picture == 'X(8)'
		element.usage == Usage.DISPLAY
		element.record == record
		element.baseName == element.name
		!element.children
		!element.keyElements
		!element.nullable
		!element.occursSpecification
		!element.parent
		!element.redefines
		!element.value
		
		record.rootElements.size() == 2
		record.rootElements[0] == defaultElement
		record.rootElements[1] == element
		record.elements.size() == 2
		record.elements[0] == defaultElement
		record.elements[1] == element
	}
	
	def "build a group element containing 1 subordinate element, NOT referencing a given record"() {
		
		given: "an element model builder with a parent but without a record"
		Element parent = new ElementModelBuilder().build('02 PARENT1')
		assert parent
		assert parent.name == 'PARENT1'
		!parent.record
		!parent.picture
		!parent.children
		def ElementModelBuilder builder = new ElementModelBuilder( parent : parent )
		
		when: "building the element"
		def definition = '03 SUBO1 picture X(8)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with its level, name and picture properties set to the"
			  "given values, with a usage of DISPLAY, that references the parent but no record"
		element
		element.level == 3
		element.name == 'SUBO1'
		element.picture == 'X(8)'
		element.usage == Usage.DISPLAY
		!element.record
		element.baseName == element.name
		!element.children
		!element.keyElements
		!element.nullable
		!element.occursSpecification
		element.parent == parent
		!element.redefines
		!element.value
		
		parent.children.size() == 1
		parent.children[0] == element
	}
	
	def "build a group element containing 1 subordinate element, while also referencing a given record"() {
		
		given: "an element model builder with a parent and a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		Element parent = new ElementModelBuilder( record : record ).build('02 PARENT1')
		assert parent
		assert parent.name == 'PARENT1'
		parent.record == record
		record.rootElements.size() == 1
		record.rootElements[0] == parent
		record.elements.size() == 1
		record.elements[0] == parent
		!parent.picture
		!parent.children
		def ElementModelBuilder builder = new ElementModelBuilder( record : record, parent : parent )
		
		when: "building the element"
		def definition = '03 SUBO1 picture X(8)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with its level, name and picture properties set to the"
			  "given values, with a usage of DISPLAY, that references the parent and record"
		element
		element.level == 3
		element.name == 'SUBO1'
		element.picture == 'X(8)'
		element.usage == Usage.DISPLAY
		element.record == record
		element.baseName == element.name
		!element.children
		!element.keyElements
		!element.nullable
		!element.occursSpecification
		element.parent == parent
		!element.redefines
		!element.value
		
		parent.children.size() == 1
		parent.children[0] == element
		
		record.rootElements.size() == 2
		record.rootElements[0] == defaultElement
		record.rootElements[1] == parent
		record.elements.size() == 3
		record.elements[0] == defaultElement
		record.elements[1] == parent
		record.elements[2] == element
	}
	
	def "element with its baseName set"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element"
		def definition = '02 ELEMENT-1 (ELEMENT-1-BASE) picture X(8)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with its baseName set"
		element
		element.level == 2
		element.name == 'ELEMENT-1'
		element.picture == 'X(8)'
		element.usage == Usage.DISPLAY
		!element.record
		element.baseName == 'ELEMENT-1-BASE'
		!element.children
		!element.keyElements
		!element.nullable
		!element.occursSpecification
		!element.parent
		!element.redefines
		!element.value
	}
	
	def "default usage modes (non level 88)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element"
		def definition = '02 ELEMENT-1 picture X(8)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with usage mode DISPLAY"
		element
		element.usage == Usage.DISPLAY
	}
	
	def "default usage modes (level 88)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element (note that we don't specify a value, under normal circumstances,"
			  "a value is always specified for condition names)"
		def definition = '88 ELEMENT-1'
		Element element = builder.build(definition)
		
		then: "the result will be an element with usage mode CONDITION NAME"
		element
		element.usage == Usage.CONDITION_NAME
	}
	
	@Unroll
	def "usage modes (non level 88)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element (note that we don't specify a picture)"
		def definition = "02 ELEMENT-1 usage $_usage"
		Element element = builder.build(definition)
		
		then: "the result will be an element with usage mode CONDITION NAME; single quotes "
			  "surrounding the usage are only required when the usage contains a blank"
		element
		element.usage == expected
		
		where:
		_usage 	  		  | expected
		'BIT' 	  		  | Usage.BIT
		"'BIT'"   		  | Usage.BIT
		'COMPUTATIONAL'   | Usage.COMPUTATIONAL
		"'COMPUTATIONAL'" | Usage.COMPUTATIONAL
		'COMPUTATIONAL 1' | Usage.COMPUTATIONAL_1
		'COMPUTATIONAL 2' | Usage.COMPUTATIONAL_2
		'COMPUTATIONAL 3' | Usage.COMPUTATIONAL_3
		'DISPLAY' 		  | Usage.DISPLAY
		"'DISPLAY'"		  | Usage.DISPLAY
		'DISPLAY 1' 	  | Usage.DISPLAY_1
		'POINTER' 	  	  | Usage.POINTER
		"'POINTER'"	  	  | Usage.POINTER
	}
	
	def "usage modes (level 88)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element (note that we don't specify a value for the condition name)"
		def definition = "02 ELEMENT-1 usage 'CONDITION NAME'"
		Element element = builder.build(definition)
		
		then: "the result will be an element with usage mode CONDITION NAME"
		element
		element.usage == Usage.CONDITION_NAME
	}
	
	def "nullable element"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element"
		def definition = '02 ELEMENT-1 picture X(8) nullable'
		Element element = builder.build(definition)
		
		then: "the result will be an element with usage mode CONDITION NAME"
		element
		element.nullable
	}
	
	def "element redefining another element (root element)"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]		
		assert defaultElement.level == 2
		assert defaultElement.name == 'ELEMENT-1'
		assert defaultElement.picture == 'X(8)'
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element and specifying an element to redefine"
		def definition = '02 ELEMENT-2 picture 9(8) redefines ELEMENT-1'
		Element element = builder.build(definition)
		
		then: "the result will be an element that redefines the given element"
		element
		element.redefines == defaultElement
	}
	
	def "element redefining another element (non root element)"() {
		
		given: "an element model builder with a parent"
		Element group = new ElementModelBuilder().build '02 GROUP1'
		Element subo1 = new ElementModelBuilder( parent : group).build '03 SUBO1 PIC X(8)'
		def ElementModelBuilder builder = new ElementModelBuilder( parent : group )
		
		when: "building an element that redefines an element that is a subordinate of the same parent"
		def definition = '03 SUBO2 PIC 9(4) redefines SUBO1'	
		Element element = builder.build(definition)
		
		then: "the result will be an element that redefines the given element"
		element
		element.redefines == subo1
	}
	
	def "redefines invalid for condition names (assertion exception)"() {
		
		given: "an element model builder with a parent"
		Element element1 = new ElementModelBuilder().build '02 ELEMENT-1 PIC 9'
		Element cond1 = new ElementModelBuilder( parent : element1).build '88 COND-1 value 1'
		def ElementModelBuilder builder = new ElementModelBuilder( parent : element1 )
		
		when: "building the element and specifying an element to redefine (mind that we do not"
		      "specify a value; we should do this in a real world situation)"
		def definition = '88 COND-2 redefines COND-1'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('redefines invalid for condition names')
	}
	
	def "element cannot redefine itself (assertion exception)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying an element to redefine"
		def definition = '02 ELEMENT2 picture 9(8) redefines ELEMENT2'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('element cannot redefine itself')
	}
	
	def "redefined element is NOT defined because of no record nor parent (assertion exception)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying an element to redefine"
		def definition = '03 ELEMENT2 picture 9(8) redefines ELEMENT-1'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('redefined element is NOT defined in record or (same) parent: ELEMENT-1')
	}
	
	def "redefined element is NOT defined because not in record (assertion exception)"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.elements[0].name == 'ELEMENT-1'
		def ElementModelBuilder builder = new ElementModelBuilder( record : record)
		
		when: "building the element and specifying an element to redefine"
		def definition = '02 ELEMENT2 picture 9(8) redefines ELEMENT-1X'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('redefined element is NOT defined in record or (same) parent: ELEMENT-1X')
	}
	
	def "redefined element is NOT defined because not in same group (assertion exception)"() {
		
		given: "an element model builder with a parent"
		Element group1 = new ElementModelBuilder().build('02 GROUP1')
		Element group2 = new ElementModelBuilder( parent : group1 ).build('03 GROUP2')
		new ElementModelBuilder( parent : group2 ).build('04 SUBO1 PIC X(8)')
		Element group3 = new ElementModelBuilder( parent : group1 ).build('03 GROUP3')
		def ElementModelBuilder builder = new ElementModelBuilder( parent : group3 )
		
		when: "building an element containing some malicious redefines construction"
		def definition = '04 SUBO2 picture X(8) redefines SUBO1'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('redefined element is NOT defined in record or (same) parent: SUBO1')
	}
	
	def "level of redefined element mismatch (assertion exception)"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		assert defaultElement.name == 'ELEMENT-1'
		assert defaultElement.picture == 'X(8)'
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element and specifying an element to redefine"
		def definition = '03 ELEMENT-2 picture 9(8) redefines ELEMENT-1'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('level of redefined element mismatch')
	}
	
	@Unroll
	def "element with a value"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying a value"
		def definition = "02 ELEMENT-1 picture X(8) value $_value"
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given value"
		element
		element.value == expected
		
		where:
		_value  	| expected
		'123'   	| '123'
		"'01'"  	| "'01'"
		'"abc"' 	| '"abc"'
		'''a"b'c''' | '''a"b'c'''
		$/a\b\c/$ 	| $/a\b\c/$
	}
	
	def "simple occurs specification"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying a value"
		def definition = '02 ELEMENT-1 picture X(8) occurs 5'
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given occurs specification"
		element
		element.occursSpecification
		element.occursSpecification.count == 5
		!element.occursSpecification.dependingOn
		!element.occursSpecification.indexElements
	}
	
	def "occurs depending on specification"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element and specifying an occurs depending on"
		def definition = '02 ELEMENT-2 picture X(8) occurs 3 dependingOn ELEMENT-1'
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given occurs specification"
		element
		element.occursSpecification
		element.occursSpecification.count == 3
		element.occursSpecification.dependingOn == defaultElement
		!element.occursSpecification.indexElements
	}
	
	def "occurs cannot depend on same element (assertion exception)"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element and specifying an occurs depending on"
		def definition = '02 ELEMENT2 picture X(8) occurs 3 dependingOn ELEMENT2'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('occurs cannot depend on same element')
	}
	
	def "occurs depending on element is NOT defined in record (assertion exception)"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element and specifying an occurs depending on"
		def definition = '02 ELEMENT2 picture X(8) occurs 3 dependingOn ELEMENT3'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('occurs depending on element is NOT defined in record or parent: ELEMENT3')
	}
	
	def "occurs depending on element is NOT defined in parent (assertion exception)"() {
		
		given: "an element model builder with a parent"
		
		Element group1 = new ElementModelBuilder().build('02 GROUP1')
		new ElementModelBuilder( parent : group1 ).build('03 ELEMENT-1 picture X(8)')
		def ElementModelBuilder builder = new ElementModelBuilder( parent : group1 )
		
		when: "building the element and specifying an occurs depending on"
		def definition = '03 ELEMENT-2 picture X(8) occurs 3 dependingOn ELEMENT3'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('occurs depending on element is NOT defined in record or parent: ELEMENT3')
	}
	
	def "occurs depending on element is NOT defined (assertion exception; no record no parent)"() {
		
		given: "an element model builder with a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying an occurs depending on"
		def definition = '02 ELEMENT-1 picture X(8) occurs 3 dependingOn ELEMENT3'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('occurs depending on element is NOT defined in record or parent: ELEMENT3')
	}
	
	def "depending on element cannot be a condition name (assertion exception)"() {
		
		given: "an element model builder with a parent"
		Element group1 = new ElementModelBuilder().build('02 GROUP1')
		Element element1 = new ElementModelBuilder( parent : group1 ).build('03 ELEMENT-1 picture X(8)')
		new ElementModelBuilder( parent : element1 ).build("88 ELEMENT-2 value 'XYZ'")
		def ElementModelBuilder builder = new ElementModelBuilder( parent : group1 )
		
		when: "building the element and specifying an occurs depending on"
		def definition = '03 ELEMENT3 picture X(8) occurs 3 dependingOn ELEMENT-2'
		builder.build(definition)
		
		then: "building the element will fail"
		def error = thrown(AssertionError)
		error.message.startsWith('depending on element cannot be a condition name')
	}
	
	def "occurs specification with a single index element (no base name specified)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying occurs index elements"
		def definition =
			'02 ELEMENT-1 picture X(8) occurs 5 indexedBy ELEMENT-WITHOUT-BASE-NAME'
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given occurs specification"
		element
		element.occursSpecification
		element.occursSpecification.count == 5
		!element.occursSpecification.dependingOn
		element.occursSpecification.indexElements.size() == 1
		
		element.occursSpecification.indexElements[0].name == 'ELEMENT-WITHOUT-BASE-NAME'
		!element.occursSpecification.indexElements[0].baseName
	}
	
	def "occurs specification with a single index element (base name specified)"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying occurs index elements"
		def definition =
			'02 ELEMENT-1 picture X(8) occurs 5 indexedBy ELEMENT-WITH-BASE-NAME-1 (INDEX-ELEMENT-BASE-NAME-1)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given occurs specification"
		element
		element.occursSpecification
		element.occursSpecification.count == 5
		!element.occursSpecification.dependingOn
		element.occursSpecification.indexElements.size() == 1
		
		element.occursSpecification.indexElements[0].name == 'ELEMENT-WITH-BASE-NAME-1'
		element.occursSpecification.indexElements[0].baseName == 'INDEX-ELEMENT-BASE-NAME-1'
	}
	
	def "occurs specification with multiple index elements"() {
		
		given: "an element model builder without a record"
		def ElementModelBuilder builder = new ElementModelBuilder()
		
		when: "building the element and specifying occurs index elements"
		def definition = 
			'02 ELEMENT-1 picture X(8) occurs 5 ' + 
			'indexedBy ELEMENT-WITHOUT-BASE-NAME, ' + 
			'ELEMENT-WITH-BASE-NAME-1 (INDEX-ELEMENT-BASE-NAME-1), ' + 
			'ELEMENT-WITH-BASE-NAME-2 (INDEX-ELEMENT-BASE-NAME-2)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given occurs specification"
		element
		element.occursSpecification
		element.occursSpecification.count == 5
		!element.occursSpecification.dependingOn
		element.occursSpecification.indexElements.size() == 3
		
		element.occursSpecification.indexElements[0].name == 'ELEMENT-WITHOUT-BASE-NAME'
		!element.occursSpecification.indexElements[0].baseName
		
		element.occursSpecification.indexElements[1].name == 'ELEMENT-WITH-BASE-NAME-1'
		element.occursSpecification.indexElements[1].baseName == 'INDEX-ELEMENT-BASE-NAME-1'
		
		element.occursSpecification.indexElements[2].name == 'ELEMENT-WITH-BASE-NAME-2'
		element.occursSpecification.indexElements[2].baseName == 'INDEX-ELEMENT-BASE-NAME-2'
	}
	
	def "occurs depending on specification with index elements"() {
		
		given: "an element model builder with a record"
		SchemaRecord record = new RecordModelBuilder().build() // the record will contain 1 element
		assert record.rootElements.size() == 1
		assert record.elements.size() == 1
		Element defaultElement = record.elements[0]
		assert defaultElement.level == 2
		def ElementModelBuilder builder = new ElementModelBuilder( record : record )
		
		when: "building the element and specifying occurs index elements"
		def definition = 
			'02 ELEMENT2 picture X(8) occurs 5 dependingOn ELEMENT-1 ' + 
			'indexedBy ELEMENT-WITHOUT-BASE-NAME, ' +
			'ELEMENT-WITH-BASE-NAME-1 (INDEX-ELEMENT-BASE-NAME-1), ' +
			'ELEMENT-WITH-BASE-NAME-2 (INDEX-ELEMENT-BASE-NAME-2)'
		Element element = builder.build(definition)
		
		then: "the result will be an element with the given occurs specification"
		element
		element.occursSpecification
		element.occursSpecification.count == 5
		element.occursSpecification.dependingOn == defaultElement
		
		element.occursSpecification.indexElements.size() == 3
		
		element.occursSpecification.indexElements[0].name == 'ELEMENT-WITHOUT-BASE-NAME'
		!element.occursSpecification.indexElements[0].baseName
		
		element.occursSpecification.indexElements[1].name == 'ELEMENT-WITH-BASE-NAME-1'
		element.occursSpecification.indexElements[1].baseName == 'INDEX-ELEMENT-BASE-NAME-1'
		
		element.occursSpecification.indexElements[2].name == 'ELEMENT-WITH-BASE-NAME-2'
		element.occursSpecification.indexElements[2].baseName == 'INDEX-ELEMENT-BASE-NAME-2'
	}
	
	def "Problem case: SRHUDATA-139 (record SROOT-DCS-139)"() {
		
		given: "an element model builder with a parent"
		Element reducedGroup = new ElementModelBuilder().build('02 SRHKEY-139')
		new ElementModelBuilder( parent : reducedGroup ).build('03 SRHVSIZE-139 picture S9(4) usage COMPUTATIONAL')
		def builder = new ElementModelBuilder( parent : reducedGroup )
		
		when: "building the element"
		def definition = '02 SRHUDATA-139 picture X(01) occurs 500 dependingOn SRHVSIZE-139'
		Element element = builder.build(definition)
		
		then: "the SRHUDATA-139 element is built"
		element
		element.picture == 'X(01)'		
	}
	
}
