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

import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.DiagramData
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RulerType
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.editor.dsl.builder.model.SchemaBuilder;

class SchemaModelBuilderSpec extends AbstractModelBuilderSpec {

	def "build the temporary schema"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema without any argument"
		Schema schema = builder.build()
		
		then: "the result will be a schema, containing only some initial diagram data"
		assertTemporarySchema(schema)
	}
	
	def "build an initial schema given (only) a name and version"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name and version"
		def definition = {
			name 'EMPSCHM'
			version 100
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version and some initial"
			  "diagram data"
		assertEmptySchema(schema, 'EMPSCHM', 100)	
	}
	
	def "build a schema with (only) a diagram label"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and diagram label"
		def definition = {
			name 'EMPSCHM'
			version 100
			diagram {
				label {
					description 'test diagram label'
					x 50
					y 100
					width 169
					height 45
				}
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version and some initial"
			  "diagram data"
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertEmptySchemaCollections(schema)
		DiagramData diagramData = schema.diagramData
		
		diagramData
		diagramData.label
		diagramData.label.description
		diagramData.label.width == 169
		diagramData.label.height == 45
		diagramData.label.diagramLocation
		diagramData.label.diagramLocation.x == 50
		diagramData.label.diagramLocation.y == 100
		diagramData.label.diagramLocation.eyecatcher == 'diagram label'
		
		diagramData.verticalRuler
		diagramData.verticalRuler.type == RulerType.VERTICAL
		diagramData.horizontalRuler
		diagramData.horizontalRuler.type == RulerType.HORIZONTAL
		diagramData.rulers.size == 2
		diagramData.rulers[0] == diagramData.verticalRuler
		diagramData.rulers[1] == diagramData.horizontalRuler
		
		diagramData.zoomLevel == 1.0d
		
		!diagramData.showRulers
		!diagramData.showGrid
		!diagramData.snapToGuides
		!diagramData.snapToGrid
		!diagramData.snapToGeometry
		
		diagramData.connectionLabels.empty
		diagramData.connectionParts.empty
		diagramData.connectors.empty
		
		diagramData.locations.size == 1
		diagramData.locations[0] == diagramData.label.diagramLocation
	}
	
	def "build a schema with (only) non-standard diagram settings"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and non-standard diagram settings"
		def definition = {
			name 'EMPSCHM'
			version 100
			diagram {
				zoom 200
				showRulersAndGuides
				showGrid
				snapToGuides
				snapToGrid
				snapToGeometry
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version and its diagram data"
			  "match the ones specified"
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertEmptySchemaCollections(schema)
		DiagramData diagramData = schema.diagramData
		
		diagramData
		diagramData.zoomLevel == 2.0d
		diagramData.showRulers
		diagramData.showGrid
		diagramData.snapToGuides
		diagramData.snapToGrid
		diagramData.snapToGeometry
	}
	
	def "define an area without procedure calls: area name passed as string argument, no area closure"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and an area with a procedure called"
		def definition = {
			name 'EMPSCHM'
			version 100
			area 'EMP-DEMO-REGION'
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing the area"
			  "specified"
		schema
		assertSchemaWithStandardDiagramData(schema, 'EMPSCHM', 100)
		schema.procedures.empty
		schema.areas.size == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.empty
		schema.records.empty
		schema.sets.empty
	}
	
	def "define an area without procedure calls: area name is part of the area closure"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and an area with a procedure called"
		def definition = {
			name 'EMPSCHM'
			version 100
			area {
				name 'EMP-DEMO-REGION'
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing the area"
			  "specified"
		schema
		assertSchemaWithStandardDiagramData(schema, 'EMPSCHM', 100)
		schema.procedures.empty
		schema.areas.size == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.empty
		schema.records.empty
		schema.sets.empty
	}
	
	def "define an area with procedure calls: area name is preceding the area closure"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and an area with a procedure called"
		def definition = {
			name 'EMPSCHM'
			version 100
			area 'EMP-DEMO-REGION' {
				procedure 'IDMSCOMP BEFORE FINISH'	
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing the area"
			  "specified"
		schema
		assertSchemaWithStandardDiagramData(schema, 'EMPSCHM', 100)
		schema.procedures.size== 1
		schema.procedures[0].name == 'IDMSCOMP'
		schema.areas.size == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.size == 1
		schema.areas[0].procedures[0].procedure == schema.procedures[0]
		schema.areas[0].procedures[0].callTime == ProcedureCallTime.BEFORE
		schema.areas[0].procedures[0].function == AreaProcedureCallFunction.FINISH
		schema.records.empty
		schema.sets.empty
	}
	
	def "define an area with procedure calls: area name is part of the area closure"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and an area with a procedure called"
		def definition = {
			name 'EMPSCHM'
			version 100
			area {
				name 'EMP-DEMO-REGION'
				procedure 'IDMSCOMP BEFORE FINISH'
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing the area"
			  "specified"
		schema
		assertSchemaWithStandardDiagramData(schema, 'EMPSCHM', 100) 
		schema.procedures.size == 1
		schema.procedures[0].name == 'IDMSCOMP'
		schema.areas.size == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.size == 1
		schema.areas[0].procedures[0].procedure == schema.procedures[0]
		schema.areas[0].procedures[0].callTime == ProcedureCallTime.BEFORE
		schema.areas[0].procedures[0].function == AreaProcedureCallFunction.FINISH
		schema.records.empty
		schema.sets.empty
	}
	
	def "define a couple of records without specifying anything at all"() {
		
		given: "a Schema builder"
		def SchemaBuilder builder = new SchemaBuilder()
		
		when: "building the schema with a name, version and 3 records, for which we don't specify"
		      "nything"
		def definition = {
			name 'EMPSCHM'
			version 100
			record
			record
			record
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 records"
			  "with an automatically generated name, referring to 3 automatically generated areas"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		assert schema.diagramData.connectionLabels.empty
		assert schema.diagramData.connectionParts.empty
		assert schema.diagramData.connectors.empty
		assert schema.diagramData.locations.size == 3
		schema.procedures.empty
		schema.areas.size == 3
		schema.areas[0].name == 'SR0010-AREA'
		schema.areas[1].name == 'SR0011-AREA'
		schema.areas[2].name == 'SR0012-AREA'
		schema.records.size == 3
		schema.records[0].name == 'SR0010'
		schema.records[0].id == 10
		schema.records[0].areaSpecification.area == schema.areas[0]
		schema.records[1].name == 'SR0011'
		schema.records[1].id == 11
		schema.records[1].areaSpecification.area == schema.areas[1]
		schema.records[2].name == 'SR0012'
		schema.records[2].id == 12
		schema.records[2].areaSpecification.area == schema.areas[2]
		schema.sets.empty
	}
	
}
