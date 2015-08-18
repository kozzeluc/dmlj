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

import org.lh.dmlj.schema.DiagramData
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.RulerType
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode

import spock.lang.Specification

class AbstractModelBuilderSpec extends Specification {

	protected String TEMP_SCHEMA_NAME = 'TMPSCHM'
	protected int TEMP_SCHEMA_VERSION = 1
	
	protected static runClosure(Object delegate, Closure closure) {
		Closure clonedClosure = closure.clone()
		clonedClosure.delegate = delegate
		clonedClosure.resolveStrategy = Closure.DELEGATE_ONLY
		clonedClosure()
	}
	
	protected void assertBasicSchema(Schema schema, String expectedName, int expectedVersion) {		
		assert schema
		assert schema.name == expectedName
		assert schema.version == expectedVersion
		assert !schema.comments
		assert !schema.description
		assert !schema.memoDate
	}
	
	protected void assertDiagramDataCollectionsEmpty(Schema schema) {
		assert schema.diagramData.connectionLabels.empty
		assert schema.diagramData.connectionParts.empty
		assert schema.diagramData.connectors.empty
		assert schema.diagramData.locations.empty
	}

	protected void assertEmptySchema(Schema schema, String expectedName, int expectedVersion) {
		assertBasicSchema(schema, expectedName, expectedVersion) 
		assertEmptySchemaCollections(schema) 
		assertStandardDiagramData(schema)
		assertDiagramDataCollectionsEmpty(schema)
	}
	
	protected void assertEmptySchemaCollections(Schema schema) {
		assert schema.procedures.empty
		assert schema.areas.empty
		assert schema.records.empty
		assert schema.sets.empty
	}
	
	protected void assertSchemaWithStandardDiagramData(Schema schema, String expectedName, 
													   int expectedVersion) {		
		assertBasicSchema(schema, expectedName, expectedVersion)
		assertStandardDiagramData(schema)
		assertDiagramDataCollectionsEmpty(schema)
	}
	
	protected void assertStandardDiagramData(Schema schema) {
		assert schema
		DiagramData diagramData = schema.diagramData
		assert diagramData
		assert !diagramData.label
		assert diagramData.verticalRuler
		assert diagramData.verticalRuler.type == RulerType.VERTICAL
		assert diagramData.horizontalRuler
		assert diagramData.horizontalRuler.type == RulerType.HORIZONTAL
		assert diagramData.rulers.size == 2
		assert diagramData.rulers[0] == diagramData.verticalRuler
		assert diagramData.rulers[1] == diagramData.horizontalRuler
		
		assert !diagramData.showRulers
		assert !diagramData.showGrid
		assert !diagramData.snapToGuides
		assert !diagramData.snapToGrid
		assert !diagramData.snapToGeometry
		
		assert diagramData.zoomLevel == 1.0d
	}
	
	protected void assertTemporarySchema(Schema schema) {
		assertEmptySchema(schema, TEMP_SCHEMA_NAME, TEMP_SCHEMA_VERSION)
	}
	
}
