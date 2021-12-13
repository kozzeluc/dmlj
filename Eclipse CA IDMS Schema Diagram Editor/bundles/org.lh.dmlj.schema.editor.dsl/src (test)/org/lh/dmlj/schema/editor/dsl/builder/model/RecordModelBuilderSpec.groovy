/**
 * Copyright (C) 2021  Luc Hermans
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

import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.Key
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.Usage
import org.lh.dmlj.schema.VsamLengthType

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
	
		assert record.elements.size() == 1
		assert record.rootElements.size() == 1
		assert record.rootElements[0] == record.elements[0]
		assert record.elements[0].level == 2
		assert record.elements[0].name == expectedName
		assert record.elements[0].baseName == expectedName
		assert record.elements[0].picture == expectedPicture
		assert record.elements[0].usage == expectedUsage
		
		assert record.elements[0].children.empty
		assert record.elements[0].keyElements.empty
		assert !record.elements[0].nullable
		assert !record.elements[0].occursSpecification
		assert !record.elements[0].parent
		assert !record.elements[0].redefines
		assert !record.elements[0].value
	}
	
	private void assertOtherDiagramDataAndLocation(SchemaRecord record, 
												   StorageMode expectedStorageMode) {		
		
		assert record
		assert record.storageMode == expectedStorageMode
		
		assert record.schema.diagramData.connectionLabels.empty
		assert record.schema.diagramData.connectionParts.empty
		assert record.schema.diagramData.connectors.empty
		
		assert record.diagramLocation
		assert record.schema.diagramData.locations.size() == 1
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)					
	}
	
	def "build the simpliest record, referencing a given schema, from only a name"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema] )
		
		when: "building the record passing the record name"
		SchemaRecord record = builder.build('RECORD1')
		
		then: "the result will be a record with its name property set to the given record name"
			  "and with a location mode of DIRECT; it will refer to the given schema and to an"
			  "automatically generated area and have basic diagram data"
		
		record
		record.schema == schema		
		record.schema.records.size() == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'RECORD1', 1)
		assert schema.areas.size() == 1
		assertSimpleAreaSpecification(record, 'RECORD1-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, from an empty closure"() {
		
		given: "a SchemaRecord builder without a Schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, from an empty closure"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema		
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
		when: "building the record passing an empty closure"
		def definition = {}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record with its name property set to SR0010 and with a"
			  "location mode of DIRECT; it will refer to the given schema and to an automatically"
			  "generated area and have basic diagram data"
		
		record				
		record.schema == schema		
		record.schema.records.size() == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'SR0010', 1)
		assert schema.areas.size() == 1
		assertSimpleAreaSpecification(record, 'SR0010-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, passing nothing at all"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, passing nothing at all"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
		when: "building the record passing no arguments at all"
		SchemaRecord record = builder.build()
		
		then: "the result will be a record with its name property set to SR0010 and with a"
			  "location mode of DIRECT; it will refer to the given schema and to an automatically"
			  "generated area and have basic diagram data"
		
		record
		record.schema == schema
		record.schema.records.size() == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'SR0010', 1)
		assert schema.areas.size() == 1
		assertSimpleAreaSpecification(record, 'SR0010-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, from a closure specifying only a name"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, from a closure specifying only a name"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
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
		record.schema.records.size() == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'EMPLOYEE', 1)
		assert schema.areas.size() == 1
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, NOT referencing a given schema, from a name AND a closure"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build the simpliest record, referencing a given schema, from a name AND a closure"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
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
		record.schema.records.size() == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assertRecordNamesAndVersions(record, 'EMPLOYEE', 1)
		assert schema.areas.size() == 1
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 415)
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build a record, NOT referencing a given schema, from a closure only, controlling ALL names and versions"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	def "build a record, referencing a given schema, from a closure only, controlling ALL names and versions"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
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
		record.schema.records.size() == 1
		record.schema.records[0] == record
		assertOtherDiagramDataAndLocation(record, StorageMode.FIXED)
		assert record.name == 'EMPLOYEE'
		assert record.baseName == 'EMPLOYEE-BASE'
		assert record.baseVersion == 10
		assert record.synonymName == 'EMPLOYEE-SYNONYM'
		assert record.synonymVersion == 100
		assert schema.areas.size() == 1
		assertSimpleAreaSpecification(record, 'EMPLOYEE-AREA')
		assertSimpleDirectRecord(record, 10)
		assertOneSimpleElement(record, 'ELEMENT-1', 'X(8)', Usage.DISPLAY)
	}
	
	@Unroll
	def "specify a storage mode"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record with a closure containing 4 procedure calls,"
			  "referring to 2 distinct procedures"
		def definition = {
			call 'PROC2 BEFORE STORE'
			call 'PROC1 AFTER GET'
			call 'PROC1 BEFORE FIND'
			call 'PROC2 AFTER ERASE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record that references the procedures specified; its schema"
			  "property will refer to an automatically generated (temporary) schema, which will"
			  "contain the definition for the 2 procedures"
		record
		record.procedures.size() == 4
		record.schema.procedures.size() == 2
		record.schema.procedures.get(0).name == 'PROC2'
		record.schema.procedures.get(1).name == 'PROC1'
		record.schema.procedures.each { Procedure procedure ->
			assert procedure.callSpecifications.size() == 2
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
				call 'PROC1 BEFORE FINISH'
			}
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		assert schema.procedures
		assert schema.procedures.size() == 1
		assert schema.procedures[0].name == 'PROC1'
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )							
																										
		when: "building the record with a closure containing 4 procedure calls, referring to 2"			
			  "distinct procedures, of which 1 is already defined in the schema"						
		def definition = {																				
			area 'EMP-DEMO-REGION'
			call 'PROC2 BEFORE STORE'
			call 'PROC1 AFTER GET'
			call 'PROC1 BEFORE FIND'
			call 'PROC2 AFTER ERASE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the result will be a record that references the procedures specified"
		record
		record.schema == schema
		record.schema.records.size() == 1
		record.schema.records[0] == record
		record.procedures.size() == 4
		record.schema.procedures.size() == 2
		record.schema.procedures[0].name == 'PROC1'
		record.schema.procedures[0].callSpecifications.size() == 3
		record.schema.procedures[0].callSpecifications[0].area == schema.areas[0]
		record.schema.procedures[0].callSpecifications[1].record == record
		record.schema.procedures[0].callSpecifications[2].record == record
		record.schema.procedures.get(1).name == 'PROC2'
		record.schema.procedures[1].callSpecifications.size() == 2
		record.schema.procedures[1].callSpecifications[0].record == record
		record.schema.procedures[1].callSpecifications[1].record == record
	}
	
	@Unroll
	def "the callTime should be derived from what's in the closure's procedure value"() {
		
		given: "a record builder"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record with a closure containing a procedure call"
		def definition = {
			call 'PROC1 ' + closureCallTime + ' STORE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the callTime should the set to the value specified"
		ProcedureCallTime callTime = record.procedures.get(0).callTime
		callTime == expected
		
		where:
		closureCallTime	|| expected
		'BEFORE'		|| ProcedureCallTime.BEFORE
		'AFTER'			|| ProcedureCallTime.AFTER
	}
	
	@Unroll("verb:#closureVerb")
	def "the verb should be derived from what's in the closure's procedure value"() {
		
		given: "a record builder"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record with a closure containing a procedure call"
		def definition = {
			call "PROC1 BEFORE$closureVerb"
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the verb should the set to the value specified"
		RecordProcedureCallVerb verb = record.procedures.get(0).verb
		verb == expected
		
		where:
		closureVerb			 || expected
		''					 || RecordProcedureCallVerb.EVERY_DML_FUNCTION
		' CONNECT'			 || RecordProcedureCallVerb.CONNECT
		' DISCONNECT'		 || RecordProcedureCallVerb.DISCONNECT
		' ERASE'			 || RecordProcedureCallVerb.ERASE
		' FIND'				 || RecordProcedureCallVerb.FIND
		' GET'				 || RecordProcedureCallVerb.GET
		' MODIFY'			 || RecordProcedureCallVerb.MODIFY
		' STORE'			 || RecordProcedureCallVerb.STORE		
	}
	
	def "the minimum root en fragment lengths should be taken from what's in the closure"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
	
	def "assign an existing area"() {
		 
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
			version 100
			area 'EMP-DEMO-REGION'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		SchemaArea empDemoRegion = schema.getArea('EMP-DEMO-REGION')
		assert empDemoRegion
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		 
		when: ""
		def definition = {
			area 'EMP-DEMO-REGION'
		}
		SchemaRecord record = builder.build(definition)
		 
		then: ""
		record
		record.areaSpecification
		record.areaSpecification.area.is(empDemoRegion)
		!record.areaSpecification.offsetExpression
	    record.schema
		record.schema.is(schema)
		schema.areas.size() == 1
		schema.areas[0].is(empDemoRegion)
	}
	
	def "assign an existing area (with offset expression)"() {
		
		given: "a record builder with a given schema"
		def schemaDefinition = {
			name 'EMPSCHM'
			version 100
			area 'EMP-DEMO-REGION'
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema
		SchemaArea empDemoRegion = schema.getArea('EMP-DEMO-REGION')
		assert empDemoRegion
		def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
		when: ""
		def definition = {
			area 'EMP-DEMO-REGION' {
				offsetPages 5
				pages 10
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: ""
		record
		record.areaSpecification
	   	record.areaSpecification.area.is(empDemoRegion)
		record.areaSpecification.offsetExpression
		record.areaSpecification.offsetExpression.offsetPageCount == 5
		!record.areaSpecification.offsetExpression.offsetPercent
		record.areaSpecification.offsetExpression.pageCount == 10
		!record.areaSpecification.offsetExpression.percent
		record.schema
		record.schema.is(schema)
		schema.areas.size() == 1
		schema.areas[0].is(empDemoRegion)
    }
	
	def "assign an non-existing area"() {
		
	   given: "a record builder with a given schema"
	   def schemaDefinition = {
		   name 'EMPSCHM'
		   version 100
	   }
	   def schemaBuilder = new SchemaModelBuilder()
	   Schema schema = schemaBuilder.build(schemaDefinition)
	   assert schema
	   assert !schema.getArea('EMP-DEMO-REGION')
	   def RecordModelBuilder builder = new RecordModelBuilder( [ schema : schema ] )
		
	   when: ""
	   def definition = {
		   area 'EMP-DEMO-REGION'
	   }
	   SchemaRecord record = builder.build(definition)
		
	   then: ""
	   record
	   record.areaSpecification
	   record.areaSpecification.area.name == 'EMP-DEMO-REGION'
	   !record.areaSpecification.offsetExpression
	   record.schema
	   record.schema.is(schema)
	   schema.areas.size() == 1
	   schema.areas[0] == record.areaSpecification.area
   }
	
	def "offsetPages and offsetPercent are mutually exclusive"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
		def RecordModelBuilder builder = new RecordModelBuilder()
		
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
	
	def "single simple root element"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record from te definition"
		def definition = { 
			elements """
				02 DEPT-ID-0410 picture 9(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's element list will contain only the given element"
		record
		record.elements.size() == 1
		record.rootElements.size() == 1
		
		record.elements[0] == record.rootElements[0]
		record.elements[0].name == 'DEPT-ID-0410'
		record.elements[0].level == 2
		record.elements[0].picture == '9(4)'
		assert !record.elements[0].children
		!record.elements[0].parent
	}

	def "2 simple root elements"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record from te definition"
		def definition = {
			elements """
				02 DEPT-ID-0410 picture 9(4)
				02 DEPT-NAME-0410 picture X(45)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's element list will contain both elements"
		record
		record.elements.size() == 2
		record.rootElements.size() == 2
		
		record.elements[0] == record.rootElements[0]
		record.elements[0].name == 'DEPT-ID-0410'
		record.elements[0].level == 2
		record.elements[0].picture == '9(4)'
		assert !record.elements[0].children
		!record.elements[0].parent
		
		record.elements[1] == record.rootElements[1]
		record.elements[1].name == 'DEPT-NAME-0410'
		record.elements[1].level == 2
		record.elements[1].picture == 'X(45)'
		assert !record.elements[1].children
		!record.elements[1].parent
	}

	def "single group root element"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record from te definition"
		def definition = {
			elements """
				02 EMP-NAME-0415
				   03 EMP-FIRST-NAME-0415 picture X(10)
				   03 EMP-LAST-NAME-0415 picture X(15)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's element list will contain the given elements"
		record
		record.elements.size() == 3
		record.rootElements.size() == 1
		
		record.elements[0] == record.rootElements[0]
		record.elements[0].name == 'EMP-NAME-0415'
		record.elements[0].level == 2
		!record.elements[0].picture
		record.elements[0].children.size() == 2
		!record.elements[0].parent
		
		record.elements[1].name == 'EMP-FIRST-NAME-0415'
		record.elements[1].level == 3
		record.elements[1].picture == 'X(10)'
		!record.elements[1].children
		record.elements[1].parent == record.elements[0]
		
		record.elements[2].name == 'EMP-LAST-NAME-0415'
		record.elements[2].level == 3
		record.elements[2].picture == 'X(15)'
		!record.elements[2].children
		record.elements[2].parent == record.elements[0]
	}

	def "element structure of record COVERAGE"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
			
		when: "building the record passing a description of the COVERAGE record"
		def definition = {
			name 'COVERAGE'
			shareStructure 'COVERAGE version 100'
			recordId 400
		
			via {
				set 'EMP-COVERAGE'
			}
		
			area 'INS-DEMO-REGION' {
				offsetPages 5
				pages 45
			}
		
			elements """
	            02 SELECTION-DATE-0400
				   03 SELECTION-YEAR-0400 picture 9(4)
				   03 SELECTION-MONTH-0400 picture 9(2)
				   03 SELECTION-DAY-0400 picture 9(2)
				02 TERMINATION-DATE-0400
				   03 TERMINATION-YEAR-0400 picture 9(4)
				   03 TERMINATION-MONTH-0400 picture 9(2)
				   03 TERMINATION-DAY-0400 picture 9(2)
				02 TYPE-0400 picture X
				   88 MASTER-0400 value 'M'
				   88 FAMILY-0400 value 'F'
				   88 DEPENDENT-0400 value 'D'
				02 INS-PLAN-CODE-0400 picture X(3)
				   88 GROUP-LIFE-0400 value '001'
				   88 HMO-0400 value '002'
				   88 GROUP-HEALTH-0400 value '003'
				   88 GROUP-DENTAL-0400 value '004'
			"""
		
			diagram {
				x 285
				y 361
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the COVERAGE record will be built"
		record
		
		record.rootElements.size() == 4
		
		Element selectionDate = record.rootElements[0]
		selectionDate
		selectionDate.name == 'SELECTION-DATE-0400'
		selectionDate.record.is(record)
		!selectionDate.parent
		selectionDate.children.size() == 3
		selectionDate.children[0].name == 'SELECTION-YEAR-0400'
		selectionDate.children[1].name == 'SELECTION-MONTH-0400'
		selectionDate.children[2].name == 'SELECTION-DAY-0400'
		selectionDate.children.each {
			assert it.record.is(record)
			assert it.parent.is(selectionDate)
		}
		
		Element terminationDate = record.rootElements[1]
		terminationDate
		terminationDate.name == 'TERMINATION-DATE-0400'
		terminationDate.record.is(record)
		!terminationDate.parent
		terminationDate.children.size() == 3
		terminationDate.children[0].name == 'TERMINATION-YEAR-0400'
		terminationDate.children[1].name == 'TERMINATION-MONTH-0400'
		terminationDate.children[2].name == 'TERMINATION-DAY-0400'
		terminationDate.children.each {
			assert it.record.is(record)
			assert it.parent.is(terminationDate)
		}
		
		Element type = record.rootElements[2]
		type
		type.name == 'TYPE-0400'
		type.record.is(record)
		!type.parent
		type.children.size() == 3
		type.children[0].name == 'MASTER-0400'
		type.children[1].name == 'FAMILY-0400'
		type.children[2].name == 'DEPENDENT-0400'
		type.children.each {
			assert it.record.is(record)
			assert it.parent.is(type)
		}
		
		Element insPlanCode = record.rootElements[3]
		insPlanCode
		insPlanCode.name == 'INS-PLAN-CODE-0400'
		insPlanCode.record.is(record)
		!insPlanCode.parent
		insPlanCode.children.size() == 4
		insPlanCode.children[0].name == 'GROUP-LIFE-0400'
		insPlanCode.children[1].name == 'HMO-0400'
		insPlanCode.children[2].name == 'GROUP-HEALTH-0400'
		insPlanCode.children[3].name == 'GROUP-DENTAL-0400'
		insPlanCode.children.each {
			assert it.record.is(record)
			assert it.parent.is(insPlanCode)
		}
	}

	def "DIRECT record (default)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing no location mode"
		def definition = { }
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to DIRECT"
		record
		record.locationMode == LocationMode.DIRECT
	}
	
	def "DIRECT record (explicitly specified)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing no location mode"
		def definition = { 
			direct
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to DIRECT"
		record
		record.locationMode == LocationMode.DIRECT
	}
	
	def "VSAM record (default length type, nonspanned; no closure)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VSAM without a closure"
		def definition = {
			vsam
		}
		SchemaRecord record = builder.build(definition)
			
		then: "the record's location mode will be set to VSAM and the VSAM type to FIXED length"
			  "NONSPANNED"
		record
		record.locationMode == LocationMode.VSAM
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
	}
	
	def "VSAM record (default length type, nonspanned; empty closure)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VSAM without a closure"
		def definition = {
			vsam {				
			}
		}
		SchemaRecord record = builder.build(definition)
			
		then: "the record's location mode will be set to VSAM and the VSAM type to FIXED length"
			  "NONSPANNED"
		record
		record.locationMode == LocationMode.VSAM
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
	}
	
	@Unroll
	def "VSAM record (fixed/variable length nonspanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VSAM"
		def definition = {
			vsam {
				type _type	
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM and the VSAM type to FIXED length"
			  "NONSPANNED"
		record
		record.locationMode == LocationMode.VSAM
		record.vsamType
		record.vsamType.lengthType == expectedLengthType
		!record.vsamType.spanned
		
		where:
		_type 	   | expectedLengthType
		'FIXED'    | VsamLengthType.FIXED
		'VARIABLE' | VsamLengthType.VARIABLE
	}
	
	@Unroll
	def "VSAM record (fixed/variable length spanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VSAM"
		def definition = {
			vsam {
				type _type
				spanned
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM and the VSAM type to FIXED length"
			  "NONSPANNED"
		record
		record.locationMode == LocationMode.VSAM
		record.vsamType
		record.vsamType.lengthType == expectedLengthType
		record.vsamType.spanned
		
		where:
		_type 	   | expectedLengthType
		'FIXED'    | VsamLengthType.FIXED
		'VARIABLE' | VsamLengthType.VARIABLE
	}
	
	def "VIA record (no displacement specification; VIA set is a placeholder, existing schema)"() {
		
		given: "a record builder with a schema"
		def schemaDefinition = {
			name 'EMPSCHM'
			version 100
		}
		def schemaBuilder = new SchemaModelBuilder()
		Schema schema = schemaBuilder.build(schemaDefinition)
		assert schema 
		def RecordModelBuilder builder = new RecordModelBuilder( schema : schema )
		
		when: "building the record passing a location mode of VIA"
		def definition = {
			via {
				set 'XYZ'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VIA and it will refer to a VIA"
			  "specification that refers to a placeholder set - the VIA set is to be resolved when"
			  "the set is buing built (which is beyond the scope of this test case)"
		record
		record.locationMode == LocationMode.VIA
		record.viaSpecification
		record.viaSpecification.set
		record.viaSpecification.set.name == 'XYZ'
		!record.viaSpecification.set.schema		// placeholder set is NOT attached to the schema
		!record.viaSpecification.set.members	// placeholder set has no members
		!record.viaSpecification.symbolicDisplacementName
		!record.viaSpecification.displacementPageCount
	}
	
	def "VIA record (no displacement specification; VIA set is real and not a placeholder, no schema)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VIA"
		def definition = {
			via {
				set 'XYZ'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VIA and it will refer to a VIA"
			  "specification that refers to an automatically created set that is added to the"
			  "temporary schema to which the record belongs"
		record
		record.locationMode == LocationMode.VIA
		record.viaSpecification
		record.viaSpecification.set
		record.viaSpecification.set.name == 'XYZ'
		record.viaSpecification.set.schema
		record.viaSpecification.set.schema.is(record.schema)
		record.viaSpecification.set.members.size() == 1
		record.viaSpecification.set.members[0].record.is(record)
		!record.viaSpecification.symbolicDisplacementName
		!record.viaSpecification.displacementPageCount
	}
	
	def "VIA record (symbolic displacement name)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VIA"
		def definition = {
			via {
				set 'XYZ'
				displacement 'SYMBOL1'
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VIA and it will refer to a VIA"
			  "specification that refers to a placeholder set"
		record
		record.locationMode == LocationMode.VIA
		record.viaSpecification
		record.viaSpecification.set
		record.viaSpecification.set.name == 'XYZ'
		record.viaSpecification.set.schema
		record.viaSpecification.set.schema.is(record.schema)
		record.viaSpecification.set.members.size() == 1
		record.viaSpecification.set.members[0].record.is(record)
		record.viaSpecification.symbolicDisplacementName == 'SYMBOL1'
		!record.viaSpecification.displacementPageCount
	}
	
	def "VIA record (displacement page count)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VIA"
		def definition = {
			via {
				set 'XYZ'
				displacement 13
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VIA and it will refer to a VIA"
			  "specification that refers to a placeholder set"
		record
		record.locationMode == LocationMode.VIA
		record.viaSpecification
		record.viaSpecification.set
		record.viaSpecification.set.name == 'XYZ'
		record.viaSpecification.set.schema
		record.viaSpecification.set.schema.is(record.schema)
		record.viaSpecification.set.members.size() == 1
		record.viaSpecification.set.members[0].record.is(record)
		!record.viaSpecification.symbolicDisplacementName
		record.viaSpecification.displacementPageCount == 13
	}
	
	def "VIA record (displacement page count is mutually exclusive with symbolic displacement name)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VIA"
		def definition = {
			via {
				set 'XYZ'
				displacement 'SYMBOL1'
				displacement 13
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "an error will occur"
		def error = thrown(AssertionError)
		error.message.startsWith('displacement page count and symbolic displacement name are mutually exclusive')
	}
	
	def "VIA record (symbolic displacement name is mutually exclusive with displacement page count)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VIA"
		def definition = {
			via {
				set 'XYZ'
				displacement 13
				displacement 'SYMBOL1'				
			}
		}
		SchemaRecord record = builder.build(definition)
		
		then: "an error will occur"
		def error = thrown(AssertionError)
		error.message.startsWith('symbolic displacement name and displacement page count are mutually exclusive')
	}
	
	def "CALC record (1 CALC key element)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of CALC"
		def definition = {
			calc {
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to CALC"
		record
		record.locationMode == LocationMode.CALC
		record.calcKey
		record.keys.find { Key key -> key.is(record.calcKey) }
		record.calcKey.elements.size() == 1
				
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
	}
	
	def "CALC record (2 CALC key elements)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of CALC"
		def definition = {
			calc {
				element 'DEPT-ID-0410'
				element 'DEPT-ID2-0410'
				duplicates 'NOT ALLOWED'
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
				02 DEPT-ID2-0410 picture X(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to CALC"
		record
		record.locationMode == LocationMode.CALC
		record.calcKey
		record.keys.find { Key key -> key.is(record.calcKey) }
		record.calcKey.elements.size() == 2
		
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]				
		record.calcKey.elements[1].element == record.elements[1]
	}
	
	@Unroll
	def "CALC record (verify all possible duplicates option values)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of CALC"
		def definition = {
			calc {
				element 'DEPT-ID-0410'
				duplicates _duplicates
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to CALC"
		record
		record.locationMode == LocationMode.CALC
		record.calcKey
		record.keys.find { Key key -> key.is(record.calcKey) }
		record.calcKey.elements.size() == 1
				
		record.calcKey.duplicatesOption == expectedDuplicatesOption
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
		
		where:
		_duplicates   | expectedDuplicatesOption
		'FIRST' 	  | DuplicatesOption.FIRST
		'LAST' 		  | DuplicatesOption.LAST
		'BY DBKEY'    | DuplicatesOption.BY_DBKEY
		'NOT ALLOWED' | DuplicatesOption.NOT_ALLOWED
	}
	
	def "VSAM CALC record (1 CALC key element, default length type, nonspanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of CALC"
		def definition = {
			vsamCalc {
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
				"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM CALC"
		record
		record.locationMode == LocationMode.VSAM_CALC
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
		record.calcKey
		record.calcKey.elements.size() == 1
				
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
		
		record.keys.size() == 1
		record.keys[0] == record.calcKey
	}
	
	def "VSAM CALC record (1 CALC key element, variable length type, nonspanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of CALC"
		def definition = {
			vsamCalc {				 
				element 'DEPT-ID-0410'
				duplicates 'NOT ALLOWED'
				type 'VARIABLE'
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM CALC"
		record
		record.locationMode == LocationMode.VSAM_CALC
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.VARIABLE
		!record.vsamType.spanned
		record.calcKey
		record.calcKey.elements.size() == 1
				
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
		
		record.keys.size() == 1
		record.keys[0] == record.calcKey
	}
	
	def "VSAM CALC record (2 CALC key elements, default length type, nonspanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VSAM CALC"
		def definition = {
			vsamCalc {
				element 'DEPT-ID-0410'
				element 'DEPT-ID2-0410'
				duplicates 'NOT ALLOWED'
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
				02 DEPT-ID2-0410 picture X(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM CALC"
		record
		record.locationMode == LocationMode.VSAM_CALC
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		!record.vsamType.spanned
		record.calcKey
		record.calcKey.elements.size() == 2
		
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
		record.calcKey.elements[1].element == record.elements[1]
		
		record.keys.size() == 1
		record.keys[0] == record.calcKey
	}
	
	def "VSAM CALC record (2 CALC key elements, default length type, spanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of VSAM CALC"
		def definition = {
			vsamCalc {
				element 'DEPT-ID-0410'
				element 'DEPT-ID2-0410'
				duplicates 'NOT ALLOWED'
				spanned
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
				02 DEPT-ID2-0410 picture X(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM CALC"
		record
		record.locationMode == LocationMode.VSAM_CALC
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.FIXED
		record.vsamType.spanned
		record.calcKey
		record.calcKey.elements.size() == 2
		
		record.calcKey.duplicatesOption == DuplicatesOption.NOT_ALLOWED
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
		record.calcKey.elements[1].element == record.elements[1]
		
		record.keys.size() == 1
		record.keys[0] == record.calcKey
	}
	
	@Unroll
	def "VSAM CALC record (verify all possible duplicates option values; variable length type, spanned)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a location mode of CALC"
		def definition = {
			vsamCalc {
				element 'DEPT-ID-0410'
				duplicates _duplicates
				type 'VARIABLE'
				spanned
			}
			elements """
				02 DEPT-ID-0410 picture 9(4)
			"""
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record's location mode will be set to VSAM CALC"
		record
		record.locationMode == LocationMode.VSAM_CALC
		record.vsamType
		record.vsamType.lengthType == VsamLengthType.VARIABLE
		record.vsamType.spanned
		record.calcKey
		record.calcKey.elements.size() == 1
				
		record.calcKey.duplicatesOption == expectedDuplicatesOption
		!record.calcKey.compressed
		!record.calcKey.naturalSequence
		!record.calcKey.memberRole
		
		record.calcKey.elements[0].element == record.elements[0]
		
		record.keys.size() == 1
		record.keys[0] == record.calcKey
		
		where:
		_duplicates   | expectedDuplicatesOption
		'NOT ALLOWED' | DuplicatesOption.NOT_ALLOWED
		'UNORDERED'   | DuplicatesOption.UNORDERED
	}
	
	def "default synonym name and version"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing no synonym name and version"
		def definition = {
			name 'EMPLOYEE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will have its synonym name and version set based on the record name"
		record
		record.name == 'EMPLOYEE'
		record.synonymName == 'EMPLOYEE'
		record.synonymVersion == 1
		record.baseName == record.synonymName
		record.baseVersion == record.synonymVersion
	}
	
	def "synonym name and version"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a synonym name and version"
		def definition = {
			name 'EMPLOYEE'
			shareStructure 'EMPSYN version 123'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will have its synonym name and version set based on the record name"
		record
		record.name == 'EMPLOYEE'
		record.synonymName == 'EMPSYN'
		record.synonymVersion == 123
		record.baseName == record.synonymName
		record.baseVersion == record.synonymVersion
	}
	
	def "default base name and version (no record synonym defined)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing no base name and version"
		def definition = {
			name 'EMPLOYEE'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will have its base name and version equal to the synonym name and version"
		record
		record.name == 'EMPLOYEE'
		record.synonymName == 'EMPLOYEE'
		record.synonymVersion == 1
		record.baseName == record.synonymName
		record.baseVersion == record.synonymVersion		
	}
	
	def "default base name and version (record synonym defined)"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing no base name and version"
		def definition = {
			name 'EMPLOYEE'
			shareStructure 'EMPSYN version 123'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will have its base name and version equal to the synonym name and version"
		record
		record.name == 'EMPLOYEE'
		record.synonymName == 'EMPSYN'
		record.synonymVersion == 123
		record.baseName == record.synonymName
		record.baseVersion == record.synonymVersion
	}
	
	def "base name and version"() {
		
		given: "a record builder without a schema"
		def RecordModelBuilder builder = new RecordModelBuilder()
		
		when: "building the record passing a base name and version"
		def definition = {
			name 'EMPLOYEE'
			primaryRecord 'EMPBASE version 456'
		}
		SchemaRecord record = builder.build(definition)
		
		then: "the record will have its base record name and version set to the values specified"
		record
		record.name == 'EMPLOYEE'
		record.synonymName == 'EMPLOYEE'
		record.synonymVersion == 1
		record.baseName == 'EMPBASE'
		record.baseVersion == 456
	}
	
}
