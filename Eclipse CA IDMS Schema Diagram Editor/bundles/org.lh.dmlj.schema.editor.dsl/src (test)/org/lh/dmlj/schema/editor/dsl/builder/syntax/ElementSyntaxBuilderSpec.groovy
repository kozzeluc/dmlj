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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.IndexElement
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.Usage

import spock.lang.Unroll

class ElementSyntaxBuilderSpec extends AbstractSyntaxBuilderSpec {

	def "Element with only a level, name and picture property"() {
		
		given: "an element syntax builder and the EMPSCHM version 100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(schema.getRecord('EMPLOYEE').getElement('EMP-ID-0415'))
		
		then: "the builder creates the syntax that describes the element: only a level, name and "
			  "picture property are generated; a usage property is NOT generated because DISPLAY is"
			  "considered the default"
		syntax == '02 EMP-ID-0415 picture 9(4)'
	}
	
	def "Element with a baseName different from the element name"() {
		
		given: "an element syntax builder and the EMPSCHM version 100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('EMP-ID-0415')
		element.setBaseName("EMP-ID")
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a baseName property is "
			  "generated"
		syntax == '02 EMP-ID-0415 (EMP-ID) picture 9(4)'
	}
	
	def "Element with a baseName equal to the element name"() {
		
		given: "an element syntax builder and the EMPSCHM version 100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('EMP-ID-0415')
		element.setBaseName("EMP-ID-0415")
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a baseName property is "
			  "generated"
		syntax == '02 EMP-ID-0415 picture 9(4)'
	}
	
	def "Element with a redefines property"() {
		
		given: "an element syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		Element element = schema.getRecord('MAP-098').getElement('MAP-EXT-NAME-098')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a redefines property is "
			  "generated"
		syntax == '02 MAP-EXT-NAME-098 redefines MAP-CURSOR-098 picture X(32)'
	}
	
	def "Element with children"() {
		
		given: "an element syntax builder and the EMPSCHM version 100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('EMP-ADDRESS-0415')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element and its children"
		syntax == stripLeadingAndTrailingNewLine("""
02 EMP-ADDRESS-0415
   03 EMP-STREET-0415 picture X(20)
   03 EMP-CITY-0415 picture X(15)
   03 EMP-STATE-0415 picture X(2)
   03 EMP-ZIP-0415
      04 EMP-ZIP-FIRST-FIVE-0415 picture X(5)
      04 EMP-ZIP-LAST-FOUR-0415 picture X(4)
""")
	}
	
	@Unroll
	def "Usage property for non-level 88 elements"() {
		
		given: "an element syntax builder"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		
		when: "passing an element to the builder's build method"
		Element element = [ getLevel : { (short) 2 }, getName : { "EL1" }, getBaseName : { null },
							getRedefines : { null }, getChildren : { null }, getPicture : { null },
							getValue : { null }, getOccursSpecification : { null },
							isNullable : { false },
							getUsage : { usage } ] as Element
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: an appropriate usage"
			  "property is generated, or omitted in the case of non-level 88 elements with a usage"
			  "other than DISPLAY"
		syntax == expected
		
		where:
		usage				  | expected
		Usage.BIT 			  | "02 EL1 usage 'BIT'"
		Usage.COMPUTATIONAL   | "02 EL1 usage 'COMPUTATIONAL'"
		Usage.COMPUTATIONAL_1 | "02 EL1 usage 'COMPUTATIONAL 1'"
		Usage.COMPUTATIONAL_2 | "02 EL1 usage 'COMPUTATIONAL 2'"
		Usage.COMPUTATIONAL_3 | "02 EL1 usage 'COMPUTATIONAL 3'"
		Usage.DISPLAY 		  | "02 EL1"								// default for non-level 88
		Usage.DISPLAY_1 	  | "02 EL1 usage 'DISPLAY 1'"
		Usage.POINTER 	  	  | "02 EL1 usage 'POINTER'"
	}
	
	def "Usage property for level 88 elements"() {
		
		given: "an element syntax builder"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder( [ generateName : false ] )
		
		when: "passing an element to the builder's build method"
		Element element = [ getLevel : { (short) 88 }, getName : { "EL1" }, getBaseName : { null },
							getRedefines : { null }, getChildren : { null }, getPicture : { null },
							getValue : { null }, getOccursSpecification : { null },
							isNullable : { false },
							getUsage : { Usage.CONDITION_NAME } ] as Element
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: an appropriate usage"
			  "property is omitted in the case of level 88 element"
		syntax == "88 EL1"												// default for level 88
	}
	
	def "Element with a value property (nothing special involved)"() {
		
		given: "an element syntax builder and a slightly tweaked version of the EMPSCHM version"
			   "100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('ACTIVE-0415')
		element.setValue('123')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a value property is "
			  "generated"
		syntax == "88 ACTIVE-0415 value 123"
	}
	
	def "Element with a value property (single quotes involved)"() {
		
		given: "an element syntax builder and the EMPSCHM version 100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('ACTIVE-0415')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a value property is "
			  "generated"
		syntax == "88 ACTIVE-0415 value '01'"
	}
	
	def "Element with a value property (double quotes involved)"() {
		
		given: "an element syntax builder and a slightly tweaked version of the EMPSCHM version"
			   "100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('ACTIVE-0415')
		element.setValue('"abc"')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a value property is "
			  "generated"
		syntax == '88 ACTIVE-0415 value "abc"'
	}
	
	def "Element with a value property (single AND double quotes involved)"() {
		
		given: "an element syntax builder and a slightly tweaked version of the EMPSCHM version"
			   "100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('ACTIVE-0415')
		element.setValue("""'a"b"c'""")
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a value property is "
			  "generated"
		syntax == """88 ACTIVE-0415 value 'a"b"c'"""
	}
	
	def "Element with a value property (backslashes involved)"() {
		
		given: "an element syntax builder and a slightly tweaked version of the EMPSCHM version"
			   "100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('EMPLOYEE').getElement('ACTIVE-0415')
		element.setValue("'a\\b\\c'")
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a value property is "
			  "generated"
		syntax == "88 ACTIVE-0415 value 'a\\b\\c'"
	}
	
	def "Element with an occurs property containing only a count"() {
		
		given: "an element syntax builder and the EMPSCHM version 100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('HOSPITAL-CLAIM').getElement('DIAGNOSIS-0430')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: an occurs property is "
			  "generated, specifying (only) a count"
		syntax == '02 DIAGNOSIS-0430 picture X(60) occurs 2'
	}
	
	def "Element with an occurs property containing a dependingOn specification"() {
		
		given: "an element syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		Element element = schema.getRecord('CATEXT-163').getElement('CAT-EXTENSION-DATA-163')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: an occurs property is "
			  "generated, specifying a count and the depending on element name"
		syntax == '02 CAT-EXTENSION-DATA-163 picture X(01) occurs 510 dependingOn CAT-EXTENSION-LENGTH-163'
	}
	
	def "Element with an occurs property containing 2 index element specifications"() {
		
		given: "an element syntax builder and a slightyly tweaked version of the EMPSCHM version"
			   "100 schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		Element element = schema.getRecord('HOSPITAL-CLAIM').getElement('DIAGNOSIS-0430')
		IndexElement indexElement1 = SchemaFactory.eINSTANCE.createIndexElement()
		IndexElement indexElement2 = SchemaFactory.eINSTANCE.createIndexElement()
		indexElement1.name = "INDEX1"
		indexElement2.name = "INDEX2"
		indexElement1.baseName = "INDEX1-BASE"
		indexElement2.baseName = "INDEX2-BASE"
		element.occursSpecification.indexElements << indexElement1
		element.occursSpecification.indexElements << indexElement2
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: an occurs property is "
			  "generated, specifying a count and both index elements"
		syntax == '02 DIAGNOSIS-0430 picture X(60) occurs 2 indexedBy INDEX1 (INDEX1-BASE), INDEX2 (INDEX2-BASE)'
	}
	
	def "Element with an occurs property containing a dependingOn specification and 2 index element specifications"() {
		
		given: "an element syntax builder and a slightyly tweaked version of the IDMSNTWK version" +
			   "1 (Release 18.5) schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		Element element = schema.getRecord('CATEXT-163').getElement('CAT-EXTENSION-DATA-163')
		IndexElement indexElement1 = SchemaFactory.eINSTANCE.createIndexElement()
		IndexElement indexElement2 = SchemaFactory.eINSTANCE.createIndexElement()
		indexElement1.name = "INDEX1"
		indexElement2.name = "INDEX2"
		indexElement1.baseName = "INDEX1-BASE"
		indexElement2.baseName = "INDEX2-BASE"
		element.occursSpecification.indexElements << indexElement1
		element.occursSpecification.indexElements << indexElement2
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: an occurs property is "
			  "generated, specifying a count, the depending on element name and index elements"
		syntax == '02 CAT-EXTENSION-DATA-163 picture X(01) occurs 510 dependingOn CAT-EXTENSION-LENGTH-163 indexedBy INDEX1 (INDEX1-BASE), INDEX2 (INDEX2-BASE)'
	}
	
	def "Element with a nullable property"() {
		
		given: "an element syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		ElementSyntaxBuilder builder = new ElementSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		Element element = schema.getRecord('INDEX-1041').getElement('AREA-1041')
		
		when: "passing the element to the builder's build method"
		String syntax = builder.build(element)
		
		then: "the builder creates the syntax that describes the element: a nullable property is "
			  "generated"
		syntax == '02 AREA-1041 picture X(18) nullable'
	}
}
