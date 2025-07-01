/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.dsl.builder.model;

import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.AreaProcedureCallSpecification
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.editor.dsl.builder.model.AreaModelBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.model.SchemaModelBuilder;

import spock.lang.Unroll


public class AreaModelBuilderSpec extends AbstractModelBuilderSpec {
	
	private void assertTemporarySchema(SchemaArea area) {
		assertSchemaWithStandardDiagramData(area.schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
		assert area.schema.areas.size() == 1
		assert area.schema.areas[0] == area
		assert area.schema.records.empty
		assert area.schema.sets.empty
		if (area.procedures) {
			assert !area.schema.procedures.empty
		} else {
			assert area.schema.procedures.empty
		}
	}
	
	def "build area without procedures, NOT referencing a given schema, from a closure only"() {
		
		given: "an area model builder without a Schema"
		def AreaModelBuilder builder = new AreaModelBuilder()
		
		when: "building the area with a closure specifying only the area name"
		def definition = {
			name 'AREA1'
		}
		SchemaArea area = builder.build(definition)
		
		then: "the result will be a SchemaArea instance with its name property set to the given"
			  "area name; its schema property will refer to an automatically generated (temporary)"
			  "schema"
		area
		area.name == 'AREA1'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.isEmpty()
		area.records.isEmpty()
		assertTemporarySchema area		
	}
	
	def "build area without procedures, referencing a given schema, from a closure only"() {
		
		given: "an area model builder with a Schema"
		Schema schema = new SchemaModelBuilder().build()
		assert schema.areas.empty
		assert schema.procedures.empty
		def AreaModelBuilder builder = new AreaModelBuilder( [ schema : schema ] )
		
		when: "building the area with a closure specifying only the area name"
		def definition = {
			name 'AREA1'
		}
		SchemaArea area = builder.build(definition)
		
		then: "the result will be a SchemaArea instance with its name property set to the given"
			  "area name and its schema property referring to given schema"
		area
		area.name == 'AREA1'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.isEmpty()
		area.records.isEmpty()
		area.schema == schema
		schema.areas.size() == 1
		schema.procedures.empty
	}
	
	def "build area with procedures called, NOT referencing a given schema, completely from a closure"() {
		
		given: "an area model builder without a Schema"
		def AreaModelBuilder builder = new AreaModelBuilder()
		
		when: "building the area from a closure containing the area name and 4 procedure calls,"
			  "referring to 2 distinct procedures"
		def definition = {
			name 'AREA2'
			callProcedure 'PROC2 BEFORE EVERY DML FUNCTION'
			callProcedure 'PROC1 AFTER READY EXCLUSIVE'
			callProcedure 'PROC1 BEFORE READY EXCLUSIVE UPDATE'
			callProcedure 'PROC2 AFTER READY EXCLUSIVE RETRIEVAL'
		}
		SchemaArea area = builder.build(definition)
		
		then: "the result will be a SchemaArea instance with its name property set, that references"
			  "the procedures specified; its schema property will refer to an automatically" 
			  "generated (temporary) schema, which will contain the definition for the 2"
			  "procedures"
		area
		area.name == 'AREA2'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.size() == 4
		area.records.isEmpty()
		assertTemporarySchema area
		area.schema.procedures.size() == 2
		area.schema.procedures.get(0).name == 'PROC2'
		area.schema.procedures.get(1).name == 'PROC1'
		area.schema.procedures.each { Procedure procedure ->
			assert procedure.callSpecifications.size() == 2
			procedure.callSpecifications.each { AreaProcedureCallSpecification callSpec -> 
				assert callSpec.area == area 
				assert callSpec.callTime
				assert callSpec.function
			}
		}
	}
	
	def "build area with procedures called, referencing a given schema, completely from a closure"() {
		
		given: "a SchemaArea builder with a Schema"
		Schema schema = new SchemaModelBuilder().build()
		assert schema.areas.empty
		assert schema.procedures.empty
		def AreaModelBuilder builder = new AreaModelBuilder( [ schema : schema ] )
		
		when: "building the area from a closure containing the area name and 4 procedure calls,"
			  "referring to 2 distinct procedures"
		def definition = {
			name 'AREA2'
			callProcedure 'PROC2 BEFORE EVERY DML FUNCTION'
			callProcedure 'PROC1 AFTER READY EXCLUSIVE'
			callProcedure 'PROC1 BEFORE READY EXCLUSIVE UPDATE'
			callProcedure 'PROC2 AFTER READY EXCLUSIVE RETRIEVAL'
		}
		SchemaArea area = builder.build(definition)
		
		then: "the result will be a SchemaArea instance with its name property set, that references"
			  "the procedures specified; its schema property will refer to the given schema, which" 
			  "will contain the definition for the 2 procedures"
		area
		area.name == 'AREA2'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.size() == 4
		area.records.isEmpty()
		area.schema == schema
		schema.areas.size() == 1
		schema.procedures.size() == 2
		schema.procedures.get(0).name == 'PROC2'
		schema.procedures.get(1).name == 'PROC1'
		schema.procedures.each { Procedure procedure ->
			assert procedure.callSpecifications.size() == 2
			procedure.callSpecifications.each { AreaProcedureCallSpecification callSpec ->
				assert callSpec.area == area
				assert callSpec.callTime
				assert callSpec.function
			}
		}
	}
	
	def "build area with NO procedures called, NOT referencing a given schema, from only a name"() {
		
		given: "a SchemaArea builder without a Schema"
		def AreaModelBuilder builder = new AreaModelBuilder()
		
		when: "building the area passing the area name"
		SchemaArea area = builder.build('AREA1')
		
		then: "the result will be a SchemaArea instance with only its name property set; its " +
			  "schema property will refer to an automatically generated (temporary) schema"
		area
		area.name == 'AREA1'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.isEmpty()
		area.records.isEmpty()
		assertTemporarySchema area
	}
	
	def "build area with NO procedures called, referencing a given schema, from only a name"() {
		
		given: "a SchemaArea builder with a Schema"
		Schema schema = new SchemaModelBuilder().build()
		assert schema.areas.empty
		assert schema.procedures.empty
		def AreaModelBuilder builder = new AreaModelBuilder( [ schema : schema ] )
		
		when: "building the area passing the area name"
		SchemaArea area = builder.build('AREA1')
		
		then: "the result will be a SchemaArea instance with its name property set ro the given"
			  "area name; its schema property will refer to the given schema"
		area
		area.name == 'AREA1'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.isEmpty()
		area.records.isEmpty()
		area.schema == schema
		schema.areas.size() == 1
		schema.procedures.empty
	}

	def "build area with procedures called, NOT referencing a schema, from a name AND a closure"() {
		
		given: "a SchemaArea builder without a Schema"
		def AreaModelBuilder builder = new AreaModelBuilder()
		
		when: "building the area with the area name and a closure containing 4 procedure calls,"
			  " referring to 2 distinct procedures"
		def definition = {
			callProcedure 'PROC2 BEFORE EVERY DML FUNCTION'
			callProcedure 'PROC1 AFTER READY EXCLUSIVE'
			callProcedure 'PROC1 BEFORE READY EXCLUSIVE UPDATE'
			callProcedure 'PROC2 AFTER READY EXCLUSIVE RETRIEVAL'
		}
		SchemaArea area = builder.build('AREA2', definition)
		
		then: "the result will be a SchemaArea instance with its name property set, that references"
			  " the procedures specified; its schema property will refer to an automatically" +
			  " generated (temporary) schema, which will contain the definition for the 2"
			  " procedures"
		area
		area.name == 'AREA2'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.size() == 4
		area.records.isEmpty()
		assertTemporarySchema area
		area.schema.procedures.size() == 2
		area.schema.procedures.get(0).name == 'PROC2'
		area.schema.procedures.get(1).name == 'PROC1'
		area.schema.procedures.each { Procedure procedure ->
			assert procedure.callSpecifications.size() == 2
			procedure.callSpecifications.each { AreaProcedureCallSpecification callSpec ->
				assert callSpec.area == area
				assert callSpec.callTime
				assert callSpec.function
			}
		}
	}
	
	def "build area with procedures called, referencing a schema, from a name AND a closure"() {
		
		given: "a SchemaArea builder with a Schema"
		Schema schema = new SchemaModelBuilder().build()
		assert schema.areas.empty
		assert schema.procedures.empty
		def AreaModelBuilder builder = new AreaModelBuilder( [ schema : schema ] )
		
		when: "building the area with the area name and a closure containing 4 procedure calls,"
			  "referring to 2 distinct procedures"
		def definition = {
			callProcedure 'PROC2 BEFORE EVERY DML FUNCTION'
			callProcedure 'PROC1 AFTER READY EXCLUSIVE'
			callProcedure 'PROC1 BEFORE READY EXCLUSIVE UPDATE'
			callProcedure 'PROC2 AFTER READY EXCLUSIVE RETRIEVAL'
		}
		SchemaArea area = builder.build('AREA2', definition)
		
		then: "the result will be a SchemaArea instance with its name property set to the given"
			  "area name, that references the procedures specified; its schema property will" 
			  "refer to he given schema, which will contain the definition for the 2 procedures"
		area
		area.name == 'AREA2'
		area.areaSpecifications.isEmpty()
		area.indexes.isEmpty()
		area.procedures.size() == 4
		area.records.isEmpty()
		area.schema == schema
		schema.areas.size() == 1
		schema.procedures.size() == 2
		schema.procedures.get(0).name == 'PROC2'
		schema.procedures.get(1).name == 'PROC1'
		schema.procedures.each { Procedure procedure ->
			assert procedure.callSpecifications.size() == 2
			procedure.callSpecifications.each { AreaProcedureCallSpecification callSpec ->
				assert callSpec.area == area
				assert callSpec.callTime
				assert callSpec.function
			}
		}
	}

	@Unroll
	def "the callTime should be derived from what's in the closure's procedure value"() {
		
		given: "a SchemaArea builder"
		def AreaModelBuilder builder = new AreaModelBuilder()
		
		when: "building the area with a closure containing a procedure call"
		def definition = {
			name 'AREA1'
			callProcedure 'PROC1 ' + closureCallTime
		}
		SchemaArea area = builder.build(definition)
		
		then: "the callTime should be set to the value specified"
		ProcedureCallTime callTime = area.procedures.get(0).callTime
		callTime == expected
		
		where:
		closureCallTime	|| expected
		'BEFORE'		|| ProcedureCallTime.BEFORE
		'AFTER'			|| ProcedureCallTime.AFTER
	}
	
	@Unroll("verb:#closureFunction")
	def "the function should be derived from what's in the closure's procedure value"() {
		
		given: "a SchemaArea builder"
		def AreaModelBuilder builder = new AreaModelBuilder()
		
		when: "building the area with a closure containing a procedure call"
		def definition = {
			name 'AREA1'
			callProcedure "PROC1 BEFORE$closureFunction"
		}
		SchemaArea area = builder.build(definition)
		
		then: "the function should the set to the value specified"
		AreaProcedureCallFunction function = area.procedures.get(0).function
		function == expected
		
		where:
		closureFunction					|| expected
		''								|| AreaProcedureCallFunction.EVERY_DML_FUNCTION
		' READY EXCLUSIVE'				|| AreaProcedureCallFunction.READY_EXCLUSIVE
		' READY EXCLUSIVE UPDATE'		|| AreaProcedureCallFunction.READY_EXCLUSIVE_UPDATE
		' READY EXCLUSIVE RETRIEVAL'	|| AreaProcedureCallFunction.READY_EXCLUSIVE_RETRIEVAL
		' READY PROTECTED'				|| AreaProcedureCallFunction.READY_PROTECTED
		' READY PROTECTED UPDATE'		|| AreaProcedureCallFunction.READY_PROTECTED_UPDATE
		' READY PROTECTED RETRIEVAL'	|| AreaProcedureCallFunction.READY_PROTECTED_RETRIEVAL
		' READY SHARED'					|| AreaProcedureCallFunction.READY_SHARED
		' READY SHARED UPDATE'			|| AreaProcedureCallFunction.READY_SHARED_UPDATE
		' READY SHARED RETRIEVAL'		|| AreaProcedureCallFunction.READY_SHARED_RETRIEVAL
		' READY UPDATE'					|| AreaProcedureCallFunction.READY_UPDATE
		' READY RETRIEVAL'				|| AreaProcedureCallFunction.READY_RETRIEVAL
		' FINISH'						|| AreaProcedureCallFunction.FINISH
		' COMMIT'						|| AreaProcedureCallFunction.COMMIT
		' ROLLBACK'						|| AreaProcedureCallFunction.ROLLBACK
	}

}
