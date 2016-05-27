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

import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.KeyElement
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.ViaSpecification
import org.lh.dmlj.schema.VsamLengthType
import org.lh.dmlj.schema.VsamType

import spock.lang.Unroll

class RecordSyntaxBuilderSpec extends AbstractSyntaxBuilderSpec {

	def "EMPLOYEE"() {
		
		given: "a record syntax builder and the EMPSCHM version 100 schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(schema.getRecord('EMPLOYEE'))
		
		then: "the builder creates the syntax that describes the record"
		syntax == expected(
'''
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
recordId 415

calc {
    element 'EMP-ID-0415'
    duplicates 'NOT ALLOWED'
}

area 'EMP-DEMO-REGION' {
    offsetPages 5
    pages 95
}

elements """
    02 EMP-ID-0415 picture 9(4)
    02 EMP-NAME-0415
       03 EMP-FIRST-NAME-0415 picture X(10)
       03 EMP-LAST-NAME-0415 picture X(15)
    02 EMP-ADDRESS-0415
       03 EMP-STREET-0415 picture X(20)
       03 EMP-CITY-0415 picture X(15)
       03 EMP-STATE-0415 picture X(2)
       03 EMP-ZIP-0415
          04 EMP-ZIP-FIRST-FIVE-0415 picture X(5)
          04 EMP-ZIP-LAST-FOUR-0415 picture X(4)
    02 EMP-PHONE-0415 picture 9(10)
    02 STATUS-0415 picture X(2)
       88 ACTIVE-0415 value '01'
       88 ST-DISABIL-0415 value '02'
       88 LT-DISABIL-0415 value '03'
       88 LEAVE-OF-ABSENCE-0415 value '04'
       88 TERMINATED-0415 value '05'
    02 SS-NUMBER-0415 picture 9(9)
    02 START-DATE-0415
       03 START-YEAR-0415 picture 9(4)
       03 START-MONTH-0415 picture 9(2)
       03 START-DAY-0415 picture 9(2)
    02 TERMINATION-DATE-0415
       03 TERMINATION-YEAR-0415 picture 9(4)
       03 TERMINATION-MONTH-0415 picture 9(2)
       03 TERMINATION-DAY-0415 picture 9(2)
    02 BIRTH-DATE-0415
       03 BIRTH-YEAR-0415 picture 9(4)
       03 BIRTH-MONTH-0415 picture 9(2)
       03 BIRTH-DAY-0415 picture 9(2)
"""

diagram {
    x 285
    y 247
}
''')
	}
	
	def "Record with a baseName and baseVersion property (different name and version)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		record.baseName = 'EMPLOYEE-BASE'
		record.baseVersion = 101
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record"
		syntax.startsWith expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
primaryRecord 'EMPLOYEE-BASE version 101'
recordId 415

calc {
""")
	}
	
	def "Record with a baseName and baseVersion property (different name only)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		record.baseName = 'EMPLOYEE-BASE'
		record.baseVersion = 100
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record"
		syntax.startsWith expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
primaryRecord 'EMPLOYEE-BASE version 100'
recordId 415

calc {
""")
	}
	
	def "Record with a baseName and baseVersion property (different version only)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		record.baseName = 'EMPLOYEE'
		record.baseVersion = 101
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record"
		syntax.startsWith expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
primaryRecord 'EMPLOYEE version 101'
recordId 415

calc {
""")
	}
	
	def "Record with a suppressed baseName and baseVersion property (same name and version)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		record.baseName = 'EMPLOYEE'
		record.baseVersion = 100
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record"
		syntax.startsWith expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
recordId 415

calc {
""")
	}
	
	def "Record with location mode DIRECT"() {
		
		given: "a record syntax builder and the IDMSNTWK version 1 (Release 18.5) schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(schema.getRecord('LOGREC-143'))
		
		then: "the builder creates the syntax that describes the record: direct is the default and"
		      "as such, no direct specification is generated"
		syntax.startsWith(expected(
'''
name 'LOGREC-143'
shareStructure 'LOGREC-143 version 1'
recordId 143
 
area 'DDLDCLOG'

minimumRootLength 0
minimumFragmentLength 4
 
elements """
'''))
	}
		
	@Unroll
	def "Record with location mode CALC"() {
			
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		KeyElement extraCalcKeyElement = SchemaFactory.eINSTANCE.createKeyElement() 
		extraCalcKeyElement.element = record.getElement('EMP-NAME-0415')
		record.calcKey.elements << extraCalcKeyElement
		record.calcKey.duplicatesOption = duplicatesOption
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: a 'calc' specification is"
		      "generated"
		syntax.startsWith(expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
recordId 415
 
calc {
    element 'EMP-ID-0415'
    element 'EMP-NAME-0415'
    duplicates '$duplicatesOptionInSyntax'
}
 
area 'EMP-DEMO-REGION' {
    offsetPages 5
    pages 95
}
 
elements \"\"\"
"""))
		
		where:
		duplicatesOption 			 | duplicatesOptionInSyntax		
		DuplicatesOption.FIRST 		 | 'FIRST'
		DuplicatesOption.LAST 		 | 'LAST'
		DuplicatesOption.BY_DBKEY 	 | 'BY DBKEY'
		DuplicatesOption.NOT_ALLOWED | 'NOT ALLOWED'
			
	}
	
	@Unroll
	def "Record with location mode VIA"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
				"schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPOSITION')
		ViaSpecification viaSpecification = SchemaFactory.eINSTANCE.createViaSpecification()
		viaSpecification.set = schema.getSet('JOB-EMPOSITION')
		viaSpecification.symbolicDisplacementName = symbolicDisplacementName
		viaSpecification.displacementPageCount = displacementPageCount
		record.viaSpecification = viaSpecification
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: a 'via' specification is"
			  "generated"
		syntax.startsWith(expected(
"""
name 'EMPOSITION'
shareStructure 'EMPOSITION version 100'
recordId 420
 
via {
    set 'JOB-EMPOSITION'$displacementSyntax
}
 
area 'EMP-DEMO-REGION' {
    offsetPages 5
    pages 95
}
 
elements \"\"\"
"""))
		
		where: "the displacement is specified as follows"
		symbolicDisplacementName | displacementPageCount | displacementSyntax
		null					 | null					 | ''
		'SYMBOL1'				 | null					 | "\n    displacement 'SYMBOL1'"
		null					 | 10					 | '\n    displacement 10'
		
	}
	
	def "Record with location mode VSAM (fixed nonspanned)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
				"schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('INSURANCE-PLAN')
		record.locationMode = LocationMode.VSAM
		record.calcKey = null
		VsamType vsamType = SchemaFactory.eINSTANCE.createVsamType()
		vsamType.lengthType = VsamLengthType.FIXED
		vsamType.spanned = false
		record.vsamType = vsamType
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: a 'vsam' specification is"
			  "generated"
		syntax.startsWith(expected(
'''
name 'INSURANCE-PLAN'
shareStructure 'INSURANCE-PLAN version 100'
recordId 435
 
vsam
 
area 'INS-DEMO-REGION' {
    offsetPages 1
    pages 4
}
 
elements """
'''))
	}
	
	@Unroll
	def "Record with location mode VSAM (other than fixed nonspanned)"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
				"schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('INSURANCE-PLAN')
		record.locationMode = LocationMode.VSAM
		record.calcKey = null
		VsamType vsamType = SchemaFactory.eINSTANCE.createVsamType()
		vsamType.lengthType = lengthType
		vsamType.spanned = spanned
		record.vsamType = vsamType
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: a 'vsam' specification is"
			  "generated"
		syntax.startsWith(expected(
"""
name 'INSURANCE-PLAN'
shareStructure 'INSURANCE-PLAN version 100'
recordId 435
 
vsam {
    $expectedVsamBody
}
 
area 'INS-DEMO-REGION' {
    offsetPages 1
    pages 4
}
 
elements \"\"\"
"""))
		
		where: "the VSAM type is specified as follows"
		lengthType				| spanned | expectedVsamBody 
		VsamLengthType.FIXED	| true     | "spanned"
		VsamLengthType.VARIABLE	| false    | "type 'VARIABLE'"
		VsamLengthType.VARIABLE	| true     | "type 'VARIABLE'\n    spanned"
	}
	
	@Unroll
	def "Record with location mode VSAM CALC (fixed nonspanned)"() {
		
	given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
		   "schema"
	RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
	Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
	SchemaRecord record = schema.getRecord('INSURANCE-PLAN')
	record.locationMode = LocationMode.VSAM_CALC
	record.calcKey.duplicatesOption = duplicatesOption
	VsamType vsamType = SchemaFactory.eINSTANCE.createVsamType()
	vsamType.lengthType = VsamLengthType.FIXED
	vsamType.spanned = false
	record.vsamType = vsamType
	
	when: "passing the record to the builder's build method"
	String syntax = builder.build(record)
	
	then: "the builder creates the syntax that describes the record: a 'vsamCalc' specification"
		  "is generated"
	syntax.startsWith(expected(
"""
name 'INSURANCE-PLAN'
shareStructure 'INSURANCE-PLAN version 100'
recordId 435
 
vsamCalc {
    element 'INS-PLAN-CODE-0435'
    duplicates '$duplicatesOptionInSyntax'
}
 
area 'INS-DEMO-REGION' {
    offsetPages 1
    pages 4
}
 
elements \"\"\"
"""))
	
	where: "the duplicates option is specified as follows:"
	duplicatesOption 			 | duplicatesOptionInSyntax
	DuplicatesOption.UNORDERED 	 | 'UNORDERED'				 
	DuplicatesOption.NOT_ALLOWED | 'NOT ALLOWED'			 			 
}
	
	@Unroll
	def "Record with location mode VSAM CALC (other than fixed nonspanned)"() {
			
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('INSURANCE-PLAN')
		record.locationMode = LocationMode.VSAM_CALC
		record.calcKey.duplicatesOption = duplicatesOption
		VsamType vsamType = SchemaFactory.eINSTANCE.createVsamType()
		vsamType.lengthType = lengthType
		vsamType.spanned = spanned
		record.vsamType = vsamType
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: a 'vsamCalc' specification"
			  "is generated"
		syntax.startsWith(expected(
"""
name 'INSURANCE-PLAN'
shareStructure 'INSURANCE-PLAN version 100'
recordId 435
 
vsamCalc {
    element 'INS-PLAN-CODE-0435'
    duplicates '$duplicatesOptionInSyntax'
    $expectedVsamBodyRemainder
}
 
area 'INS-DEMO-REGION' {
    offsetPages 1
    pages 4
}
 
elements \"\"\"
"""))
		
		where: "the duplicates option and VSAM type are specified as follows:"
		duplicatesOption 			 | lengthType 			   | spanned | duplicatesOptionInSyntax | expectedVsamBodyRemainder
		DuplicatesOption.UNORDERED 	 | VsamLengthType.FIXED    | true    | 'UNORDERED'				 | "spanned"
		DuplicatesOption.UNORDERED 	 | VsamLengthType.VARIABLE | false   | 'UNORDERED'				 | "type 'VARIABLE'"
		DuplicatesOption.UNORDERED 	 | VsamLengthType.VARIABLE | true    | 'UNORDERED'				 | "type 'VARIABLE'\n    spanned"
		DuplicatesOption.NOT_ALLOWED | VsamLengthType.FIXED    | true    | 'NOT ALLOWED'			 | "spanned"
		DuplicatesOption.NOT_ALLOWED | VsamLengthType.VARIABLE | false   | 'NOT ALLOWED'			 | "type 'VARIABLE'"
		DuplicatesOption.NOT_ALLOWED | VsamLengthType.VARIABLE | true    | 'NOT ALLOWED'			 | "type 'VARIABLE'\n    spanned"
	}
	
	@Unroll
	def "Area specification"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		if (symbolicSubareaName) {
			record.areaSpecification.symbolicSubareaName = symbolicSubareaName	
		} else if (offsetPageCount || offsetPercent || pageCount || percent) {
			record.areaSpecification.offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression()
			record.areaSpecification.offsetExpression.offsetPageCount = offsetPageCount
			record.areaSpecification.offsetExpression.offsetPercent = offsetPercent
			record.areaSpecification.offsetExpression.pageCount = pageCount
			record.areaSpecification.offsetExpression.percent = percent
		} else {
			record.areaSpecification.symbolicSubareaName = null
			record.areaSpecification.offsetExpression = null
		}
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: an 'area' specification is"
			  "generated"
		syntax.startsWith(expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
recordId 415
 
calc {
    element 'EMP-ID-0415'
    duplicates 'NOT ALLOWED'
}
 
area 'EMP-DEMO-REGION'$areaBody
 
elements \"\"\"
"""))
		
		where: "the symbolic offset or offset expression are specified as follows:"
		symbolicSubareaName | offsetPageCount | offsetPercent | pageCount | percent | areaBody
		null 				| null			  |	null		  | null	  |	null	| ''
		'SYMBOL1'			| null			  |	null		  | null	  |	null	| " {\n    subarea 'SYMBOL1'\n}"
		null				| 10			  |	null		  | null	  |	null	| ' {\n    offsetPages 10\n}'
		null				| null			  |	20		  	  | null	  |	null	| ' {\n    offsetPercent 20\n}'
		null				| null			  |	null	  	  | 30	  	  |	null	| ' {\n    pages 30\n}'
		null				| null			  |	null	  	  | null	  |	40		| ' {\n    percent 40\n}'
		null				| 11			  |	null	  	  | 12	  	  |	null	| ' {\n    offsetPages 11\n    pages 12\n}'
		null				| 11			  |	null	  	  | null  	  |	12		| ' {\n    offsetPages 11\n    percent 12\n}'
		null				| null			  |	21	  	  	  | null	  |	22		| ' {\n    offsetPercent 21\n    percent 22\n}'
		null				| null			  |	21	  	  	  | 22		  |	null	| ' {\n    offsetPercent 21\n    pages 22\n}'
	}
	
	@Unroll
	def "Minimum Root Length and/or Minimum Fragment Length"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		record.minimumRootLength = minimumRootLength
		record.minimumFragmentLength = minimumFragmentLength
			
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: an 'area' specification is"
			  "generated"
		syntax.startsWith(expected(
"""
name 'EMPLOYEE'
shareStructure 'EMPLOYEE version 100'
recordId 415
 
calc {
    element 'EMP-ID-0415'
    duplicates 'NOT ALLOWED'
}
 
area 'EMP-DEMO-REGION' {
    offsetPages 5
    pages 95
}
$expectedSyntax 
elements \"\"\"
"""))
		
		where: "the minimum root- and fragment lengths are specified as follows"
		minimumRootLength | minimumFragmentLength | expectedSyntax
		null			  | null				  | ''
		20			  	  | null				  | '\nminimumRootLength 20\n'
		null		  	  | 30				  	  | '\nminimumFragmentLength 30\n'
		20		  	  	  | 30				  	  | '\nminimumRootLength 20\nminimumFragmentLength 30\n'
	}
	
	@Unroll
	def "Record with procedures"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('JOB')
		for (callSpec in record.procedures) {
			callSpec.callTime = callTime
			callSpec.verb = verb
		}
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: an 'area' specification is"
			  "generated"
		syntax.startsWith(expected(
"""
name 'JOB'
shareStructure 'JOB version 100'
recordId 440
 
calc {
    element 'JOB-ID-0440'
    duplicates 'NOT ALLOWED'
}
 
area 'ORG-DEMO-REGION' {
    offsetPages 5
    pages 45
}
 
minimumRootLength 24
minimumFragmentLength 296
 
procedure 'IDMSCOMP $syntaxPart'
procedure 'IDMSCOMP $syntaxPart'
procedure 'IDMSDCOM $syntaxPart'
 
elements \"\"\"
"""))
		
		where:
		callTime 				 | verb 						   	  		  | syntaxPart
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.CONNECT 	  		  | 'BEFORE CONNECT'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.DISCONNECT 		  | 'BEFORE DISCONNECT'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.ERASE 	  		  | 'BEFORE ERASE'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.EVERY_DML_FUNCTION | 'BEFORE EVERY DML FUNCTION'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.FIND 			  | 'BEFORE FIND'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.GET 				  | 'BEFORE GET'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.MODIFY 			  | 'BEFORE MODIFY'
		ProcedureCallTime.BEFORE | RecordProcedureCallVerb.STORE 			  | 'BEFORE STORE'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.CONNECT 			  | 'AFTER CONNECT'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.DISCONNECT 		  | 'AFTER DISCONNECT'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.ERASE 			  | 'AFTER ERASE'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.EVERY_DML_FUNCTION | 'AFTER EVERY DML FUNCTION'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.FIND 			  | 'AFTER FIND'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.GET 				  | 'AFTER GET'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.MODIFY 			  | 'AFTER MODIFY'
		ProcedureCallTime.AFTER  | RecordProcedureCallVerb.STORE 			  | 'AFTER STORE'
		
	}
	
	@Unroll
	def "Diagram data"() {
		
		given: "a record syntax builder and a slightly tweaked version of the EMPSCHM version 100"
			   "schema"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder()
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaRecord record = schema.getRecord('EMPLOYEE')
		record.storageMode = storageMode
		
		when: "passing the record to the builder's build method"
		String syntax = builder.build(record)
		
		then: "the builder creates the syntax that describes the record: an 'area' specification is"
			  "generated"
		syntax.endsWith(
"""
diagram {$syntaxPart
    x 285
    y 247
}
""")
		
		where:
		storageMode		  			 	| syntaxPart
		StorageMode.FIXED 			 	| ''
		StorageMode.FIXED_COMPRESSED 	| "\n    storageMode 'FIXED COMPRESSED'"
		StorageMode.VARIABLE 		 	| "\n    storageMode 'VARIABLE'"
		StorageMode.VARIABLE_COMPRESSED | "\n    storageMode 'VARIABLE COMPRESSED'"
	}
	
	def "Syntax for inclusion in a schema"() {
		
		given: "a record syntax builder for which we indicate that we want to suppress the 'name"
			   "property and set an initial indent"
		RecordSyntaxBuilder builder = new RecordSyntaxBuilder( generateName : false, initialTabs : 1)
		Schema schema = loadSchema('testdata/EMPSCHM version 100.schema')
		
		when: "passing an area to the builder's build method"
		String syntax = builder.build(schema.getRecord("EMPLOYEE"))
		
		then: "the builder creates the syntax that describes the record and will omit the name"
			  "property and indent the syntax with 1 'tab' (being actually spaces)"
		syntax.startsWith(expected(
"""
    shareStructure 'EMPLOYEE version 100'
    recordId 415
 
    calc {
"""))
		syntax.endsWith(
			"""
    diagram {
        x 285
        y 247
    }
""")
	}
	
}
