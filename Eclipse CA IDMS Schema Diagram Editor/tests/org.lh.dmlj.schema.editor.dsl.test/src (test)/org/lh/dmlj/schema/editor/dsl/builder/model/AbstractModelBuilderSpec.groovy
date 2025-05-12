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

import org.lh.dmlj.schema.DiagramData
import org.lh.dmlj.schema.RulerType
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.editor.dsl.builder.AbstractBuilderSpec

class AbstractModelBuilderSpec extends AbstractBuilderSpec {

	protected String TEMP_SCHEMA_NAME = 'TMPSCHM'
	protected int TEMP_SCHEMA_VERSION = 1
	
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
	
	protected void assertEquals(List<String> expectedLines, List<String> actualLines) {
		
		String firstLineMessage
		int index
		for (int i = 0; i < expectedLines.size() && i < actualLines.size(); i++) {
			if (expectedLines[i] != actualLines[i]) {
				firstLineMessage = 
					"line $i - expected: <${expectedLines[i]}>, actual: <${actualLines[i]}>"
				index = i
				break
			}
		}
		if (!firstLineMessage) {
			if (expectedLines.size() > actualLines.size()) {
				index = expectedLines.size() - 1
				firstLineMessage = "line $index - expected: <${expectedLines[index]}>, actual: null"						
			} else if (expectedLines.size() != actualLines.size()) {
				index = actualLines.size() - 1
				firstLineMessage = "line $index - expected: null, actual: <${actualLines[index]}>"		
				
			}
		}
		
		if (firstLineMessage) {
			StringBuilder message = new StringBuilder(firstLineMessage)
			message << '\n\nexpected lines:'
			for (int i in (index - 5)..(index + 5)) {
				if (i >= 0 && i < expectedLines.size()) {
					message << "\n[$i] ${expectedLines[i]}"
				}	
			}
			message << '\n\nactual lines:'
			for (int i in (index - 5)..(index + 5)) {
				if (i >= 0 && i < expectedLines.size()) {
					message << "\n[$i] ${actualLines[i]}"
				}
			}
			throw new AssertionError(message)
		}
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
		assert diagramData.rulers.size() == 2
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
