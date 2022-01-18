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

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl
import org.eclipse.emf.ecore.xmi.util.XMLProcessor
import org.lh.dmlj.schema.AreaProcedureCallFunction
import org.lh.dmlj.schema.DiagramData
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RulerType
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SetOrder

class SchemaModelBuilderSpec extends AbstractModelBuilderSpec {

	def "build the temporary schema"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema without any argument"
		Schema schema = builder.build()
		
		then: "the result will be a schema, containing only some initial diagram data"
		assertTemporarySchema(schema)
	}
	
	def "schema without a name and version"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema from an empty closure"
		def definition = { }
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema, containing only some initial diagram data"
		assertTemporarySchema(schema)
	}

	def "build an initial schema given (only) a name and version"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
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
	
	def "build an initial schema given a name, version and description"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name and version"
		def definition = {
			name 'EMPSCHM'
			version 100
			description 'EMPLOYEE DEMO DATABASE'
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version and some initial"
			  "diagram data"
		schema
		schema.description == 'EMPLOYEE DEMO DATABASE'
	}
	
	def "build an initial schema given a name, version and comments"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name and version"
		def definition = {
			name 'EMPSCHM'
			version 100
			comments 'INSTALLATION: COMMONWEATHER CORPORATION'
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version and some initial"
			  "diagram data"
		schema
		schema.comments
		schema.comments.size() == 1
		schema.comments[0] == 'INSTALLATION: COMMONWEATHER CORPORATION'
	}
	
	def "build a schema with a memoDate"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a memoDate"
		def definition = {
			memoDate '01/02/03'
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given memoDate"
		schema
		schema.memoDate == '01/02/03'
	}
	
	def "build a schema with (only) a diagram label"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
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
		diagramData.rulers.size() == 2
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
		
		diagramData.locations.size() == 1
		diagramData.locations[0] == diagramData.label.diagramLocation
	}
	
	def "build a schema with (only) non-standard diagram settings"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
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
	
	def "define a schema containing guides"() {
		
		given: "a Schema builder and "
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		and: "schema DSL containing 2 horizontal and 3 vertical guides"
		def syntax = {
			name 'TESTSCHM'
			version 1
			diagram {
				horizontalGuides '1,2'
				verticalGuides '3,4,5'
			}
		}
		
		when: "building the schema"
		Schema schema = builder.build(syntax)
		
		then: "the verical ruler owns the 2 horizontal guides"
		schema.diagramData.verticalRuler.guides.size() == 2
		schema.diagramData.verticalRuler.guides[0].position == 1
		schema.diagramData.verticalRuler.guides[1].position == 2
		
		and : "the horizontal ruler owns the 2 verical guides"
		schema.diagramData.horizontalRuler.guides.size() == 3
		schema.diagramData.horizontalRuler.guides[0].position == 3
		schema.diagramData.horizontalRuler.guides[1].position == 4
		schema.diagramData.horizontalRuler.guides[2].position == 5
	}
	
	def "define an area without procedure calls: area name passed as string argument, no area closure"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and an area without any procedures called"
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
		schema.areas.size() == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.empty
		schema.records.empty
		schema.sets.empty
	}
	
	def "define an area without procedure calls: area name is part of the area closure"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and an area without any procedures called"
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
		schema.areas.size() == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.empty
		schema.records.empty
		schema.sets.empty
	}
	
	def "define an area with procedure calls: area name is preceding the area closure"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and an area with a procedure called"
		def definition = {
			name 'EMPSCHM'
			version 100
			area 'EMP-DEMO-REGION' {
				callProcedure 'IDMSCOMP BEFORE FINISH'	
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing the area"
			  "specified"
		schema
		assertSchemaWithStandardDiagramData(schema, 'EMPSCHM', 100)
		schema.procedures.size== 1
		schema.procedures[0].name == 'IDMSCOMP'
		schema.areas.size() == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.size() == 1
		schema.areas[0].procedures[0].procedure == schema.procedures[0]
		schema.areas[0].procedures[0].callTime == ProcedureCallTime.BEFORE
		schema.areas[0].procedures[0].function == AreaProcedureCallFunction.FINISH
		schema.records.empty
		schema.sets.empty
	}
	
	def "define an area with procedure calls: area name is part of the area closure"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and an area with a procedure called"
		def definition = {
			name 'EMPSCHM'
			version 100
			area {
				name 'EMP-DEMO-REGION'
				callProcedure 'IDMSCOMP BEFORE FINISH'
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing the area"
			  "specified"
		schema
		assertSchemaWithStandardDiagramData(schema, 'EMPSCHM', 100) 
		schema.procedures.size() == 1
		schema.procedures[0].name == 'IDMSCOMP'
		schema.areas.size() == 1
		schema.areas[0].name == 'EMP-DEMO-REGION'
		schema.areas[0].procedures.size() == 1
		schema.areas[0].procedures[0].procedure == schema.procedures[0]
		schema.areas[0].procedures[0].callTime == ProcedureCallTime.BEFORE
		schema.areas[0].procedures[0].function == AreaProcedureCallFunction.FINISH
		schema.records.empty
		schema.sets.empty
	}
	
	def "define a couple of records without specifying anything at all (schema name and version not specified)"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 records, for which we don't specify"
			  "anything"
		def definition = {
			record
			record
			record
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 records"
			  "with an automatically generated name, referring to 3 automatically generated areas"
		schema
		assertBasicSchema(schema, 'TMPSCHM', 1)
		assertStandardDiagramData(schema)
		assert schema.diagramData.connectionLabels.empty
		assert schema.diagramData.connectionParts.empty
		assert schema.diagramData.connectors.empty
		assert schema.diagramData.locations.size() == 3
		schema.procedures.empty
		schema.areas.size() == 3
		schema.areas[0].name == 'SR0010-AREA'
		schema.areas[1].name == 'SR0011-AREA'
		schema.areas[2].name == 'SR0012-AREA'
		schema.records.size() == 3
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
	
	def "define a couple of records without specifying anything at all (schema name and version specified)"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 records, for which we don't specify"
		      "anything"
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
		assert schema.diagramData.locations.size() == 3
		schema.procedures.empty
		schema.areas.size() == 3
		schema.areas[0].name == 'SR0010-AREA'
		schema.areas[1].name == 'SR0011-AREA'
		schema.areas[2].name == 'SR0012-AREA'
		schema.records.size() == 3
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
	
	def "define a couple of records by specifying a name (no closure)"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 records, for which we specify a name"
		def definition = {
			name 'EMPSCHM'
			version 100
			record 'RECORD1'
			record 'RECORD2'
			record 'RECORD3'
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 records"
			  "with the given names, referring to 3 automatically generated areas"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		assert schema.diagramData.connectionLabels.empty
		assert schema.diagramData.connectionParts.empty
		assert schema.diagramData.connectors.empty
		assert schema.diagramData.locations.size() == 3
		schema.procedures.empty
		schema.areas.size() == 3
		schema.areas[0].name == 'RECORD1-AREA'
		schema.areas[1].name == 'RECORD2-AREA'
		schema.areas[2].name == 'RECORD3-AREA'
		schema.records.size() == 3
		schema.records[0].name == 'RECORD1'
		schema.records[0].id == 10
		schema.records[0].areaSpecification.area == schema.areas[0]
		schema.records[1].name == 'RECORD2'
		schema.records[1].id == 11
		schema.records[1].areaSpecification.area == schema.areas[1]
		schema.records[2].name == 'RECORD3'
		schema.records[2].id == 12
		schema.records[2].areaSpecification.area == schema.areas[2]
		schema.sets.empty
	}
	
	def "define a couple of records by specifying a name (with closure)"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 records, for which we specify a name"
		def definition = {
			name 'EMPSCHM'
			version 100
			record { 
				name 'RECORD1' 
			}
			record { 
				name 'RECORD2' 
			}
			record { 
				name 'RECORD3' 
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 records"
			  "with the given names, referring to 3 automatically generated areas"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		assert schema.diagramData.connectionLabels.empty
		assert schema.diagramData.connectionParts.empty
		assert schema.diagramData.connectors.empty
		assert schema.diagramData.locations.size() == 3
		schema.procedures.empty
		schema.areas.size() == 3
		schema.areas[0].name == 'RECORD1-AREA'
		schema.areas[1].name == 'RECORD2-AREA'
		schema.areas[2].name == 'RECORD3-AREA'
		schema.records.size() == 3
		schema.records[0].name == 'RECORD1'
		schema.records[0].id == 10
		schema.records[0].areaSpecification.area == schema.areas[0]
		schema.records[1].name == 'RECORD2'
		schema.records[1].id == 11
		schema.records[1].areaSpecification.area == schema.areas[1]
		schema.records[2].name == 'RECORD3'
		schema.records[2].id == 12
		schema.records[2].areaSpecification.area == schema.areas[2]
		schema.sets.empty
	}
	
	def "define a couple of records by specifying a name and record id"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 records, for which we specify a name"
		def definition = {
			name 'EMPSCHM'
			version 100
			record 'RECORD1' { 
				recordId 100
			}
			record 'RECORD2' { 
				recordId 200
			}
			record 'RECORD3' { 
				recordId 300
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 records"
			  "with the given names, referring to 3 automatically generated areas"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		assert schema.diagramData.connectionLabels.empty
		assert schema.diagramData.connectionParts.empty
		assert schema.diagramData.connectors.empty
		assert schema.diagramData.locations.size() == 3
		schema.procedures.empty
		schema.areas.size() == 3
		schema.areas[0].name == 'RECORD1-AREA'
		schema.areas[1].name == 'RECORD2-AREA'
		schema.areas[2].name == 'RECORD3-AREA'
		schema.records.size() == 3
		schema.records[0].name == 'RECORD1'
		schema.records[0].id == 100
		schema.records[0].areaSpecification.area == schema.areas[0]
		schema.records[1].name == 'RECORD2'
		schema.records[1].id == 200
		schema.records[1].areaSpecification.area == schema.areas[1]
		schema.records[2].name == 'RECORD3'
		schema.records[2].id == 300
		schema.records[2].areaSpecification.area == schema.areas[2]
		schema.sets.empty
	}
	
	def "assign a record to an existing area"() {
		
	   given: "a schema builder"
	   def schemaBuilder = new SchemaModelBuilder()
		
	   when: "building the schema"
	   def definition = {
		   name 'EMPSCHM'
		   version 100
		   
		   area 'EMP-DEMO-REGION'
		   
		   record 'EMPLOYEE' {
			   area 'EMP-DEMO-REGION'
		   }
	   }
	   Schema schema = schemaBuilder.build(definition)
		
	   then: "the area will be created only once and is referenced by the record"
	   schema
	   schema.areas.size() == 1
	   schema.records.size() == 1
	   schema.records[0].name == 'EMPLOYEE'
	   schema.records[0].areaSpecification.area
	   schema.records[0].areaSpecification.area.name == 'EMP-DEMO-REGION'
	   schema.records[0].areaSpecification.area.is(schema.areas[0])
   }
	
	def "assign a record to a non-existing area"() {
		
	   given: "a schema builder"
	   def schemaBuilder = new SchemaModelBuilder()
		
	   when: "building the schema"
	   def definition = {
		   name 'EMPSCHM'
		   version 100
		   
		   record 'EMPLOYEE' {
			   area 'EMP-DEMO-REGION'
		   }
	   }
	   Schema schema = schemaBuilder.build(definition)
		
	   then: "the area will be created automatically and is referenced by the record"
	   schema
	   schema.areas.size() == 1
	   schema.records.size() == 1
	   schema.records[0].name == 'EMPLOYEE'
	   schema.records[0].areaSpecification.area
	   schema.records[0].areaSpecification.area.name == 'EMP-DEMO-REGION'
	   schema.records[0].areaSpecification.area.is(schema.areas[0])
   }
	
	def "define a VSAM record"() {
	}
	
	def "define a couple of sets without specifying anything at all"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 sets, for which we don't specify"
			  "anything"
		def definition = {
			name 'EMPSCHM'
			version 100
			set
			set
			set
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 sets"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		schema.sets.size() == 3
		schema.sets[0].name == 'SET1'
		schema.sets[0].order == SetOrder.NEXT
		schema.sets[0].owner
		schema.sets[0].owner.record.schema.is(schema)
		schema.sets[0].members.size() == 1
		schema.sets[0].members[0].record.schema.is(schema)
		schema.sets[1].name == 'SET2'
		schema.sets[1].order == SetOrder.NEXT
		schema.sets[1].owner
		schema.sets[1].owner.record.schema.is(schema)
		schema.sets[1].members.size() == 1
		schema.sets[1].members[0].record.schema.is(schema)
		schema.sets[2].name == 'SET3'
		schema.sets[2].order == SetOrder.NEXT
		schema.sets[2].owner
		schema.sets[2].owner.record.schema.is(schema)
		schema.sets[2].members.size() == 1
		schema.sets[2].members[0].record.schema.is(schema)
	}
	
	def "define a couple of sets by specifying a name (no closure)"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 sets, for which we don't specify"
			  "anything"
		def definition = {
			name 'EMPSCHM'
			version 100
			set 'SET01'
			set 'SET02'
			set 'SET03'
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 sets"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		schema.sets.size() == 3
		schema.sets[0].name == 'SET01'
		schema.sets[0].order == SetOrder.NEXT
		schema.sets[0].owner
		schema.sets[0].owner.record.schema.is(schema)
		schema.sets[0].members.size() == 1
		schema.sets[0].members[0].record.schema.is(schema)
		schema.sets[1].name == 'SET02'
		schema.sets[1].order == SetOrder.NEXT
		schema.sets[1].owner
		schema.sets[1].owner.record.schema.is(schema)
		schema.sets[1].members.size() == 1
		schema.sets[1].members[0].record.schema.is(schema)
		schema.sets[2].name == 'SET03'
		schema.sets[2].order == SetOrder.NEXT
		schema.sets[2].owner
		schema.sets[2].owner.record.schema.is(schema)
		schema.sets[2].members.size() == 1
		schema.sets[2].members[0].record.schema.is(schema)
	}
	
	def "define a couple of sets by specifying a name (with closure)"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 sets, for which we don't specify"
			  "anything"
		def definition = {
			name 'EMPSCHM'
			version 100
			set { 
				name 'SET01' 
			}
			set { 
				name 'SET02' 
			}
			set { 
				name 'SET03' 
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 sets"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		schema.sets.size() == 3
		schema.sets[0].name == 'SET01'
		schema.sets[0].order == SetOrder.NEXT
		schema.sets[0].owner
		schema.sets[0].owner.record.schema.is(schema)
		schema.sets[0].members.size() == 1
		schema.sets[0].members[0].record.schema.is(schema)
		schema.sets[1].name == 'SET02'
		schema.sets[1].order == SetOrder.NEXT
		schema.sets[1].owner
		schema.sets[1].owner.record.schema.is(schema)
		schema.sets[1].members.size() == 1
		schema.sets[1].members[0].record.schema.is(schema)
		schema.sets[2].name == 'SET03'
		schema.sets[2].order == SetOrder.NEXT
		schema.sets[2].owner
		schema.sets[2].owner.record.schema.is(schema)
		schema.sets[2].members.size() == 1
		schema.sets[2].members[0].record.schema.is(schema)
	}
	
	def "define a couple of sets by specifying a name and set order"() {
		
		given: "a Schema builder"
		def SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema with a name, version and 3 sets, for which we don't specify"
			  "anything"
		def definition = {
			name 'EMPSCHM'
			version 100
			set 'SET01' { 
				order 'FIRST'
			}
			set 'SET02' { 
				order 'LAST' 
			}
			set 'SET03' { 
				order 'PRIOR' 
			}
		}
		Schema schema = builder.build(definition)
		
		then: "the result will be a schema with the given name and version containing 3 sets"
		schema
		assertBasicSchema(schema, 'EMPSCHM', 100)
		assertStandardDiagramData(schema)
		schema.sets.size() == 3
		schema.sets[0].name == 'SET01'
		schema.sets[0].order == SetOrder.FIRST
		schema.sets[0].owner
		schema.sets[0].owner.record.schema.is(schema)
		schema.sets[0].members.size() == 1
		schema.sets[0].members[0].record.schema.is(schema)
		schema.sets[1].name == 'SET02'
		schema.sets[1].order == SetOrder.LAST
		schema.sets[1].owner
		schema.sets[1].owner.record.schema.is(schema)
		schema.sets[1].members.size() == 1
		schema.sets[1].members[0].record.schema.is(schema)
		schema.sets[2].name == 'SET03'
		schema.sets[2].order == SetOrder.PRIOR
		schema.sets[2].owner
		schema.sets[2].owner.record.schema.is(schema)
		schema.sets[2].members.size() == 1
		schema.sets[2].members[0].record.schema.is(schema)
	}
	
	def "EMPSCHM version 100"() {
		
		given: "the schema loaded from xmi, as well as a Schema builder"
		Schema original = loadSchema('testdata/EMPSCHM version 100.schema')
		SchemaModelBuilder builder = new SchemaModelBuilder()		
		
		when: "building the schema from a file containing the schema syntax"
		Schema schema = builder.buildFromFile(new File('testdata/EMPSCHM version 100.schemadsl'))
		
		then: "the result will be a schema conforming to the syntax in the file"
		schema
		assertEquals(toCompareFriendlyXmi(original), toCompareFriendlyXmi(schema)) 
	}
	
	def "IDMSNTWK version 1 (Release 18.5)"() {
		
		given: "the schema loaded from xmi, as well as a Schema builder"
		Schema original = loadSchema('testdata/IDMSNTWK version 1 (Release 18.5).schema')
		SchemaModelBuilder builder = new SchemaModelBuilder()
		
		when: "building the schema from a file containing the schema syntax"
		Schema schema = 
			builder.buildFromFile(new File('testdata/IDMSNTWK version 1 (Release 18.5).schemadsl'))
		
		then: "the result will be a schema conforming to the syntax in the file"
		schema
		assertEquals(toCompareFriendlyXmi(original), toCompareFriendlyXmi(schema)) 
	}
}
