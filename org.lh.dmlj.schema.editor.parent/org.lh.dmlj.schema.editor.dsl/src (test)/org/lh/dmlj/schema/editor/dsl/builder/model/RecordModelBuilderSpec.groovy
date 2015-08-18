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

import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallSpecification
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.Usage
import org.lh.dmlj.schema.editor.dsl.builder.model.RecordBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.model.SchemaBuilder;

import spock.lang.Unroll

class RecordModelBuilderSpec extends AbstractModelBuilderSpec {
	
	private void assertSimpleAreaSpecification(SchemaRecord record, String expectedAreaName) {
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName
		assert !record.areaSpecification.offsetExpression
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area		
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == expectedAreaName		
	}
	
	private void assertAreaSpecificationWithSymbolicSubareaName(SchemaRecord record, 
																String expectedAreaName,
																String expectedSymbolicSubareaName) {
											   
		assert record
		assert record.schema
		assert record.areaSpecification
		assert record.areaSpecification.symbolicSubareaName == expectedSymbolicSubareaName
		assert !record.areaSpecification.offsetExpression
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == expectedAreaName
	}
																
	private void assertAreaSpecificationWithOffsetPageCount(SchemaRecord record,
															String expectedAreaName,
															int expectedOffsetPageCount) {
											   
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName		
		assert record.areaSpecification.offsetExpression
		assert record.areaSpecification.offsetExpression.offsetPageCount == expectedOffsetPageCount
		assert !record.areaSpecification.offsetExpression.offsetPercent
		assert !record.areaSpecification.offsetExpression.pageCount
		assert !record.areaSpecification.offsetExpression.percent
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == expectedAreaName
	}
															
	private void assertAreaSpecificationWithOffsetPercent(SchemaRecord record, 
														  String expectedAreaName,
														  int expectedOffsetPercent) {
												   
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName
		assert record.areaSpecification.offsetExpression
		assert !record.areaSpecification.offsetExpression.offsetPageCount
		assert record.areaSpecification.offsetExpression.offsetPercent == (short) expectedOffsetPercent
		assert !record.areaSpecification.offsetExpression.pageCount
		assert !record.areaSpecification.offsetExpression.percent
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == expectedAreaName
	}
														  
	private void assertAreaSpecificationWithPageCount(SchemaRecord record, String expectedAreaName,
													  int expectedPageCount) {
												 
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName
		assert record.areaSpecification.offsetExpression
		assert !record.areaSpecification.offsetExpression.offsetPageCount
		assert !record.areaSpecification.offsetExpression.offsetPercent
		assert record.areaSpecification.offsetExpression.pageCount == expectedPageCount
		assert !record.areaSpecification.offsetExpression.percent
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == expectedAreaName
	}
															  
	private void assertAreaSpecificationWithPercent(SchemaRecord record, String expectedAreaName,
													int expectedPercent) {
													 
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName
		assert record.areaSpecification.offsetExpression
		assert !record.areaSpecification.offsetExpression.offsetPageCount
		assert !record.areaSpecification.offsetExpression.offsetPercent
		assert !record.areaSpecification.offsetExpression.pageCount
		assert record.areaSpecification.offsetExpression.percent == expectedPercent
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == expectedAreaName
	}
	
	private void assertOneSimpleElement(SchemaRecord record, String expectedName, 
										String expectedPicture, Usage expectedUsage) {
	
		record.elements.size == 1
		record.rootElements.size == 1
		record.rootElements[0] == record.elements[0]
		record.elements[0].level == 2
		record.elements[0].name == expectedName
		record.elements[0].baseName == expectedName
		record.elements[0].picture == expectedPicture
		record.elements[0].usage == expectedUsage
		
		record.elements[0].children.empty
		record.elements[0].keyElements.empty
		!record.elements[0].nullable
		!record.elements[0].occursSpecification
		!record.elements[0].parent
		!record.elements[0].redefines
		!record.elements[0].value
	}
	
	private void assertOtherDiagramDataAndLocation(SchemaRecord record, 
												   StorageMode expectedStorageMode) {		
		
		assert record
		record.storageMode == expectedStorageMode
		
		assert record.schema.diagramData.connectionLabels.empty
		assert record.schema.diagramData.connectionParts.empty
		assert record.schema.diagramData.connectors.empty
		
		assert record.diagramLocation
		assert record.schema.diagramData.locations.size == 1
		assert record.schema.diagramData.locations[0] == record.diagramLocation				
		assert record.diagramLocation.x == 0
		assert record.diagramLocation.y == 0
		assert record.diagramLocation.eyecatcher == 'record ' + record.name
	}
	
	private void assertRecordNamesAndVersions(SchemaRecord record, String expectedName, 
											  int expectedVersion) {
											  
		assert record.name == expectedName
		assert record.baseName == expectedName
		assert record.baseVersion == expectedVersion
		assert record.synonymName == expectedName
		assert record.synonymVersion == expectedVersion
	}
	
	private void assertSimpleDirectRecord(SchemaRecord record, int expectedRecordId) {
		assert record.id == expectedRecordId
		assert record.locationMode == LocationMode.DIRECT
		assert !record.minimumFragmentLength
		assert !record.minimumRootLength
		assert !record.calcKey
		assert !record.viaSpecification
		assert !record.vsamType
		assert record.roles.empty
		assert record.keys.empty
		assert record.procedures.empty
	}
	
	def "build the simpliest record, NOT referencing a given schema, from only a name"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the record name"
		SchemaRecord record = builder.build('RECORD1')
		
		then: "the result will be a record with its name property set to the given record name"
			  "and with a location mode of DIRECT; it will refer to an automatically generated"
			  "(temporary) schema and area and have basic diagram data"
		
		record	
		assertBasicSchema(record.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assertStandardDiagramData(record.schema)
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'RECORD1', 1)		
		assertSimpleAreaSpecification(record, 'RECORD1-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)					
	}
	
	def "build the simpliest record, referencing a given schema, from only a name"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordBuilder builder = new RecordBuilder( [ schema : schema] )
		
		when: "building the record passing the record name"
		SchemaRecord record = builder.build('RECORD1')
		
		then: "the result will be a record with its name property set to the given record name"
			  "and with a location mode of DIRECT; it will refer to the given schema and to an"
			  "automatically generated area and have basic diagram data"
		
		record
		record.schema == schema		
		record.schema.records.size == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'RECORD1', 1)
		assert schema.areas.size == 1
		assertSimpleAreaSpecification(record, 'RECORD1-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, from an empty closure"() {
		
		given: "a SchemaRecord builder without a Schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing an empty closure"
		def definition = {}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name property set to SR0010 and with a" 
			  "location mode of DIRECT; it will refer to an automatically generated (temporary)"
			  "schema and area and have basic diagram data"
		
		record				
		assertBasicSchema(record.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assertStandardDiagramData(record.schema)
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'SR0010', 1)
		assertSimpleAreaSpecification(record, 'SR0010-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, from an empty closure"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema		
		def RecordBuilder builder = new RecordBuilder( [ schema : schema ] )
		
		when: "building the record passing an empty closure"
		def definition = {}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name property set to SR0010 and with a"
			  "location mode of DIRECT; it will refer to the given schema and to an automatically"
			  "generated area and have basic diagram data"
		
		record				
		record.schema == schema		
		record.schema.records.size == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'SR0010', 1)
		assert schema.areas.size == 1
		assertSimpleAreaSpecification(record, 'SR0010-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, passing nothing at all"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing no arguments at all"
		SchemaRecord record = builder.build()
		
		then: "the result will be a record with its name property set to SR0010 and with a"
			  "location mode of DIRECT; it will refer to an automatically generated (temporary)"
			  "schema and area and have basic diagram data"
		
		record
		assertBasicSchema(record.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assertStandardDiagramData(record.schema)
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'SR0010', 1)
		assertSimpleAreaSpecification(record, 'SR0010-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, passing nothing at all"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordBuilder builder = new RecordBuilder( [ schema : schema ] )
		
		when: "building the record passing no arguments at all"
		SchemaRecord record = builder.build()
		
		then: "the result will be a record with its name property set to SR0010 and with a"
			  "location mode of DIRECT; it will refer to the given schema and to an automatically"
			  "generated area and have basic diagram data"
		
		record
		record.schema == schema
		record.schema.records.size == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'SR0010', 1)
		assert schema.areas.size == 1
		assertSimpleAreaSpecification(record, 'SR0010-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, from a closure specifying only a name"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a closure with the record name"
		def definition = {
			name 'EMPLOYEE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name property set to the given name and with a"
			  "location mode of DIRECT; it will refer to an automatically generated (temporary)"
			  "schema and area and have basic diagram data"
		
		record				
		assertBasicSchema(record.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assertStandardDiagramData(record.schema)
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'EMPLOYEE', 1)
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, from a closure specifying only a name"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordBuilder builder = new RecordBuilder( [ schema : schema ] )
		
		when: "building the record passing a closure with the record name"
		def definition = {
			name 'EMPLOYEE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name property set to the given name and with a"
			  "location mode of DIRECT; it will refer to the given schema and to an automatically"
			  "generated area and have basic diagram data"
		
		record				
		record.schema == schema		
		record.schema.records.size == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'EMPLOYEE', 1)
		assert schema.areas.size == 1
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, from a name AND a closure"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a name and a closure containing the record id"
		def definition = {
			recordId 415
		}
		SchemaRecord record = builder.build('EMPLOYEE', definition)
		
		then: "the result will be a record with its name and id properties set to the given name"
			  "and record id and with a location mode of DIRECT; it will refer to an" 
			  "automatically generated (temporary) schema and area and have basic diagram data"
		
		record				
		assertBasicSchema(record.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assertStandardDiagramData(record.schema)
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'EMPLOYEE', 1)
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 415)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, from a name AND a closure"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordBuilder builder = new RecordBuilder( [ schema : schema ] )
		
		when: "building the record passing a name and a closure containing the record id"
		def definition = {
			recordId 415
		}
		SchemaRecord record = builder.build('EMPLOYEE', definition)
		
		then: "the result will be a record with its name and id properties set to the given name"
			  "and record id and with a location mode of DIRECT; it will refer to the given schema"
			  "and to an automatically generated (temporary) schema and area and have basic diagram"
			  "data"
		
		record
		record.schema == schema
		record.schema.records.size == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'EMPLOYEE', 1)
		assert schema.areas.size == 1
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 415)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build a record, NOT referencing a given schema, from a closure only, controlling ALL names and versions"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a name and a closure containing the record id, record"
		      "synonym and base record"
		def definition = {
			name 'EMPLOYEE'
			shareStructure 'EMPLOYEE-SYNONYM version 100'
			primaryRecord 'EMPLOYEE-BASE version 10'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name, id, record synonym and base record"
		      "properties set to the values in the closure and with a location mode of DIRECT;"
			  "it will refer to an automatically generated (temporary) schema and area and have"
			  "basic diagram data"
		
		record		
		assertBasicSchema(record.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assertStandardDiagramData(record.schema)
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assert record.name == 'EMPLOYEE'
		assert record.baseName == 'EMPLOYEE-BASE'
		assert record.baseVersion == 10
		assert record.synonymName == 'EMPLOYEE-SYNONYM'
		assert record.synonymVersion == 100		
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build a record, referencing a given schema, from a closure only, controlling ALL names and versions"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordBuilder builder = new RecordBuilder( [ schema : schema ] )
		
		when: "building the record passing a name and a closure containing the record id, record"
		      "synonym and base record"
		def definition = {
			name 'EMPLOYEE'
			shareStructure 'EMPLOYEE-SYNONYM version 100'
			primaryRecord 'EMPLOYEE-BASE version 10'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name, id, record synonym and base record"
		      "properties set to the values in the closure and with a location mode of DIRECT;"
			  "it will refer to the given schema and to an automatically generated area and have"
			  "basic diagram data"		
		record
		record.schema == schema
		record.schema.records.size == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assert record.name == 'EMPLOYEE'
		assert record.baseName == 'EMPLOYEE-BASE'
		assert record.baseVersion == 10
		assert record.synonymName == 'EMPLOYEE-SYNONYM'
		assert record.synonymVersion == 100
		assert schema.areas.size == 1
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT1', 'X(8)', Usage.DISPLAY)
	}
	
	@Unroll
	def "specify a storage mode"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a closure containing the storage mode"
		def definition = {
			diagram {
				storageMode closureStorageMode
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's storageMode should be set to the value specified in the closure"		
		record
		record.storageMode == expected
		
		where:
		closureStorageMode	  | expected
		'FIXED'				  | StorageMode.FIXED
		'FIXED COMPRESSED'	  | StorageMode.FIXED_COMPRESSED
		'VARIABLE'			  | StorageMode.VARIABLE
		'VARIABLE COMPRESSED' | StorageMode.VARIABLE_COMPRESSED
	}
	
	def "build a record with procedures called, NOT referencing a given schema"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record with a closure containing 4 procedure calls,"
			  "referring to 2 distinct procedures"
		def definition = {
			procedure 'PROC2 BEFORE STORE'
			procedure 'PROC1 AFTER GET'
			procedure 'PROC1 BEFORE FIND'
			procedure 'PROC2 AFTER ERASE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record that references the procedures specified; its schema"
			  "property will refer to an automatically generated (temporary) schema, which will"
			  "contain the definition for the 2 procedures"
		record
		record.procedures.size == 4
		record.schema.procedures.size == 2
		record.schema.procedures.get(0).name == 'PROC2'
		record.schema.procedures.get(1).name == 'PROC1'
		record.schema.procedures.each { Procedure procedure ->
			assert procedure.callSpecifications.size == 2
			procedure.callSpecifications.each { RecordProcedureCallSpecification callSpec ->
				assert callSpec.record == record
				assert callSpec.callTime
				assert callSpec.verb
			}
		}
	}
	
	def "build a record with procedures called, referencing a given schema and area"() {
		
		given: "a record builder with a schema"
		def schemaDefinition = {
			name 'EMPSCHM'
			area 'EMP-DEMO-REGION' {
				procedure 'PROC1 BEFORE FINISH'
			}
		}
		def schemaBuilder = new SchemaBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		assert schema.procedures
		assert schema.procedures.size == 1
		assert schema.procedures[0].name == 'PROC1'
		def RecordBuilder builder = new RecordBuilder( [ schema : schema ] )							
																										
		when: "building the record with a closure containing 4 procedure calls, referring to 2"			
			  "distinct procedures, of which 1 is already defined in the schema"						
		def definition = {																				
			area 'EMP-DEMO-REGION'
			procedure 'PROC2 BEFORE STORE'
			procedure 'PROC1 AFTER GET'
			procedure 'PROC1 BEFORE FIND'
			procedure 'PROC2 AFTER ERASE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record that references the procedures specified"
		record
		record.schema == schema
		record.schema.records.size == 1
		record.schema.records[0] == record
		record.procedures.size == 4
		record.schema.procedures.size == 2
		record.schema.procedures[0].name == 'PROC1'
		record.schema.procedures[0].callSpecifications.size == 3
		record.schema.procedures[0].callSpecifications[0].area == schema.areas[0]
		record.schema.procedures[0].callSpecifications[1].record == record
		record.schema.procedures[0].callSpecifications[2].record == record
		record.schema.procedures.get(1).name == 'PROC2'
		record.schema.procedures[1].callSpecifications.size == 2
		record.schema.procedures[1].callSpecifications[0].record == record
		record.schema.procedures[1].callSpecifications[1].record == record
	}
	
	@Unroll
	def "the callTime should be derived from what's in the closure's procedure value"() {
		
		given: "a record builder"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record with a closure containing a procedure call"
		def definition = {
			procedure 'PROC1 ' + closureCallTime + ' STORE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the callTime should the set to the value specified"
		ProcedureCallTime callTime = record.procedures.get(0).callTime
		callTime == expected
		
		where:
		closureCallTime	| expected
		'BEFORE'		| ProcedureCallTime.BEFORE
		'AFTER'			| ProcedureCallTime.AFTER
	}
	
	@Unroll
	def "the function should be derived from what's in the closure's procedure value"() {
		
		given: "a record builder"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record with a closure containing a procedure call"
		def definition = {
			procedure 'PROC1 BEFORE ' + closureVerb
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the verb should the set to the value specified"
		RecordProcedureCallVerb verb = record.procedures.get(0).verb
		verb == expected
		
		where:
		closureVerb			 | expected
		'CONNECT'			 | RecordProcedureCallVerb.CONNECT
		'DISCONNECT'		 | RecordProcedureCallVerb.DISCONNECT
		'ERASE'				 | RecordProcedureCallVerb.ERASE
		'EVERY DML FUNCTION' | RecordProcedureCallVerb.EVERY_DML_FUNCTION
		'FIND'				 | RecordProcedureCallVerb.FIND
		'GET'				 | RecordProcedureCallVerb.GET
		'MODIFY'			 | RecordProcedureCallVerb.MODIFY
		'STORE'				 | RecordProcedureCallVerb.STORE		
	}
	
	def "the minimum root en fragment lengths should be taken from what's in the closure"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a closure containing a minimum root and fragment length"
		def definition = {
			minimumRootLength 5
			minimumFragmentLength 10
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's minimum root and fragment length should be set to the values"
			  "specified in the closure"
		record
		record.minimumRootLength == 5
		record.minimumFragmentLength == 10
	}
	
	def "the record figure's x and y diagram coordinates should be taken from what's in the closure"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a closure containing the figure's x and y coordinates"
		def definition = {
			diagram {
				x 13
				y 17
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record figure's x and y coordinates should be set to the values specified in"
			  "the closure"
		record
		record.diagramLocation.x == 13
		record.diagramLocation.y == 17
	}
	
	def "assign an area without symbolic subarea or offset expression (no schema specified, area name, no area body)"() {
	
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name without a closure"
		def definition = {
			area 'EMP-DEMO-REGION'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name"
		assertSimpleAreaSpecification(record, 'EMP-DEMO-REGION')			
	}
	
	def "assign an area without symbolic subarea or offset expression (no schema specified, name and empty closure)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and an empty closure"
		def definition = {
			area 'EMP-DEMO-REGION' { }
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name"
		assertSimpleAreaSpecification(record, 'EMP-DEMO-REGION')			
	}
	
	def "assign an area without symbolic subarea or offset expression (no schema specified, area body contains name)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing a closure containing an area name"
		def definition = {
			area { 
				name 'EMP-DEMO-REGION'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name"
		assertSimpleAreaSpecification(record, 'EMP-DEMO-REGION')			
	}
	
	def "assign an area with a symbolic subarea name (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the symbolic"
			  "subarea name"
		def definition = {
			area 'EMP-DEMO-REGION' { 
				subarea 'SUBAREA1'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given symbolic subarea name"
		assertAreaSpecificationWithSymbolicSubareaName(record, 'EMP-DEMO-REGION', 'SUBAREA1')			
	}
	
	def "assign an area with offset pages (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the offset page"
			  "count"
		def definition = {
			area 'EMP-DEMO-REGION' {
				offsetPages 13
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given offset page count"
		assertAreaSpecificationWithOffsetPageCount(record, 'EMP-DEMO-REGION', 13)
	}
	
	def "assign an area with offset percent (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the offset percent"
		def definition = {
			area 'EMP-DEMO-REGION' {
				offsetPercent 25
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given offset percent"
		assertAreaSpecificationWithOffsetPercent(record, 'EMP-DEMO-REGION', 25)
	}
	
	def "assign an area with pages in its offset expression (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the page count"
		def definition = {
			area 'EMP-DEMO-REGION' {
				pages 17
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given page count in the offset expression"
		assertAreaSpecificationWithPageCount(record, 'EMP-DEMO-REGION', 17)
	}
	
	def "assign an area with a percentage in its offset expression (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the percentage"
		def definition = {
			area 'ORG-DEMO-REGION' {
				percent 47
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given offset percent"
		assertAreaSpecificationWithPercent(record, 'ORG-DEMO-REGION', 47)
	}
	
	def "assign an area with offset pages and pages (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the offset page"
			  "count"
		def definition = {
			area 'INS-DEMO-REGION' {
				offsetPages 3
				pages 24
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given offset page count and page count"
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName
		assert record.areaSpecification.offsetExpression
		assert record.areaSpecification.offsetExpression.offsetPageCount == 3
		assert !record.areaSpecification.offsetExpression.offsetPercent
		assert record.areaSpecification.offsetExpression.pageCount == 24
		assert !record.areaSpecification.offsetExpression.percent
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == "INS-DEMO-REGION"
	}
	
	def "assign an area with offset percent and percent (no schema specified, area name and area body)"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the offset page"
			  "count"
		def definition = {
			area 'INS-DEMO-REGION' {
				offsetPercent 33
				percent 50
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will reference a newly created area with the given name, specifying the"
			  "given offset page count and page count"
		assert record
		assert record.schema
		assert record.areaSpecification
		assert !record.areaSpecification.symbolicSubareaName
		assert record.areaSpecification.offsetExpression
		assert !record.areaSpecification.offsetExpression.offsetPageCount
		assert record.areaSpecification.offsetExpression.offsetPercent == 33
		assert !record.areaSpecification.offsetExpression.pageCount
		assert record.areaSpecification.offsetExpression.percent == 50
		assert !record.areaSpecification.systemOwner
		assert record.areaSpecification.area
		assert record.areaSpecification.area in record.schema.areas
		assert record.areaSpecification.area.name == "INS-DEMO-REGION"
	}
	
	def "offsetPages and offsetPercent are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the offset page"
			  "count and offset percent (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				offsetPages 7
				offsetPercent 50
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because offsetPages and offsetPercent are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('offsetPercent and offsetPages are mutually exclusive')
	}
	
	def "offsetPercent and offsetPages are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the offset percent"
			  "and offset pages (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				offsetPercent 50
				offsetPages 7				
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because offsetPages and offsetPercent are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('offsetPages and offsetPercent are mutually exclusive')
	}
	
	def "pages and percent are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the page count"
			  "and percent (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				pages 5
				percent 35
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because pages and percent are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('percent and pages are mutually exclusive')
	}
	
	def "percent and pages are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the percent and"
			  "pages (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {				
				percent 35
				pages 5
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because pages and percent are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('pages and percent are mutually exclusive')
	}
	
	def "subarea and offsetPages are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the subarea and"
			  "offsetPages (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				subarea 'SUBAREA1'
				offsetPages 5
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because subarea and offsetPages are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('offsetPages and subarea are mutually exclusive')
	}
	
	def "offsetPages and subarea are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the"
			  "offsetPages and subarea (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				offsetPages 5
				subarea 'SUBAREA1'				
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because offsetPages and subarea are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('subarea and anything of [offsetPages, offsetPercent, pages, percent] area mutually exclusive')
	}
	
	def "subarea and offsetPercent are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the subarea and"
			  "offsetPercent (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				subarea 'SUBAREA1'
				offsetPercent 20
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because subarea and offsetPercent are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('offsetPercent and subarea are mutually exclusive')
	}
	
	def "offsetPercent and subarea are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the"
			  "offsetPercent and subarea (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				offsetPercent 5
				subarea 'SUBAREA1'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because offsetPercent and subarea are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('subarea and anything of [offsetPages, offsetPercent, pages, percent] area mutually exclusive')
	}
	
	def "subarea and pages are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the subarea and"
			  "pages (in that order)"
		def definition = {
			area 'ORG-DEMO-REGION' {
				subarea 'SUBAREA1'
				pages 100
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because subarea and pages are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('pages and subarea are mutually exclusive')
	}
	
	def "pages and subarea are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the pages and"
			  "subarea (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				pages 5
				subarea 'SUBAREA1'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because pages and subarea are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('subarea and anything of [offsetPages, offsetPercent, pages, percent] area mutually exclusive')
	}
	
	def "subarea and percent are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the subarea and"
			  "percent (in that order)"
		def definition = {
			area 'ORG-DEMO-REGION' {
				subarea 'SUBAREA1'
				percent 15
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because subarea and percent are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('percent and subarea are mutually exclusive')
	}
	
	def "percent and subarea are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordBuilder builder = new RecordBuilder()
		
		when: "building the record passing the area name and a closure containing the percent and"
			  "subarea (in that order)"
		def definition = {
			area 'INS-DEMO-REGION' {
				percent 20
				subarea 'SUBAREA1'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record build is rejected because percent and subarea are mutually exclusive"
		def error = thrown(AssertionError)
		error.message.startsWith('subarea and anything of [offsetPages, offsetPercent, pages, percent] area mutually exclusive')
	}
	
}
