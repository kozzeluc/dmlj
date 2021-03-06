/**
 * Copyright (C) 2019  Luc Hermans
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
import org.lh.dmlj.schema.DiagramLabel
import org.lh.dmlj.schema.Guide
import org.lh.dmlj.schema.Ruler
import org.lh.dmlj.schema.RulerType
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set

class SchemaModelBuilder extends AbstractModelBuilder<Schema> {
	
	private static final String BODY_LABEL = "label"
	
	private static void createGuides(String positions, Ruler ruler) {		
		positions.split(',').each {
			Guide guide = SchemaFactory.eINSTANCE.createGuide()
			guide.ruler = ruler
			guide.position = Integer.valueOf(it)
		}		
	}
		
	def propertyMissing(String name) {
		if (name == 'showRulersAndGuides') {
			showRulersAndGuides()	
		} else if (name == 'showGrid') {
			showGrid()	
		} else if (name == 'snapToGuides') {
			snapToGuides()	
		} else if (name == 'snapToGrid') {
			snapToGrid()	
		} else if (name == 'snapToGeometry') {
			snapToGeometry()	
		} else if (name == 'record') {
			record()
		} else if (name == 'set') {
			set()
		} else {
			throw new MissingPropertyException('no such property: ' + name + ' for class: ' +
											   getClass().getName())
		}
	}

	SchemaArea area(Closure definition) {
		assert !bodies
		AreaModelBuilder areaBuilder = new AreaModelBuilder( [ schema : schema ] )
		if (bufferedName) {
			SchemaArea area	= areaBuilder.build(bufferedName, definition)
			bufferedName = null
			area
		} else {
			areaBuilder.build(definition)
		}
	}
	
	SchemaArea area(String name) {
		assert !bodies
		assert !bufferedName
		AreaModelBuilder areaBuilder = new AreaModelBuilder( [ schema : schema ] )		
		areaBuilder.build(name)
	}
	
	Schema build() {
		
		assert !bodies
		
		schema = SchemaFactory.eINSTANCE.createSchema()
		schema.name = 'TMPSCHM'
		schema.version = 1
		
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData()
		schema.setDiagramData(diagramData)
		diagramData.verticalRuler = SchemaFactory.eINSTANCE.createRuler()
		diagramData.verticalRuler.type = RulerType.VERTICAL
		diagramData.rulers << diagramData.verticalRuler
		diagramData.horizontalRuler = SchemaFactory.eINSTANCE.createRuler()
		diagramData.horizontalRuler.type = RulerType.HORIZONTAL
		diagramData.rulers << diagramData.horizontalRuler
		
		schema
	}
	
	Schema build(Closure definition) {
		
		assert !bodies
		
		schema = SchemaFactory.eINSTANCE.createSchema()
		
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData()
		schema.setDiagramData(diagramData)
		diagramData.verticalRuler = SchemaFactory.eINSTANCE.createRuler()
		diagramData.verticalRuler.type = RulerType.VERTICAL
		diagramData.rulers << diagramData.verticalRuler
		diagramData.horizontalRuler = SchemaFactory.eINSTANCE.createRuler()
		diagramData.horizontalRuler.type = RulerType.HORIZONTAL
		diagramData.rulers << diagramData.horizontalRuler
		
		runClosure definition
		
		assert !bodies
		
		if (!schema.name) {
			schema.name = 'TMPSCHM'
		}
		if (!schema.version) {
			schema.version = 1
		}
		
		schema
	}
	
	Schema buildFromFile(File file) {
		buildFromString(file.text)
	}
	
	void comments(String comments) {
		assert !bodies
		schema.comments << comments
	}
	
	void description(String description) {
		assert !bodies || bodies == [ BODY_DIAGRAM, BODY_LABEL ]
		if (!bodies) {
			schema.description = description
		} else if (description != 'null') { // clear label descriptions that were erroneously saved when null
			schema.diagramData.label.description = description
		}
	}
	
	DiagramData diagram(Closure definition) {
		assert !bodies
		bodies << BODY_DIAGRAM
		runClosure(definition)
		assert bodies == [ BODY_DIAGRAM ]
		bodies -= BODY_DIAGRAM
		schema.diagramData
	}
	
	void height(int height) {
		assert bodies == [ BODY_DIAGRAM, BODY_LABEL ]
		schema.diagramData.label.height = (short) height
	}
	
	void horizontalGuides(String positions) {
		assert bodies == [ BODY_DIAGRAM ]
		// mind that the HORIZONTAL guides are owned by the VERTICAL ruler		
		createGuides(positions, schema.diagramData.verticalRuler)
	}
	
	DiagramLabel label(Closure definition) {
		assert bodies == [ BODY_DIAGRAM ]
		bodies << BODY_LABEL
		schema.diagramData.label = SchemaFactory.eINSTANCE.createDiagramLabel()		
		schema.diagramData.label.diagramLocation = buildAndRegisterDiagramLocation('diagram label')
		runClosure(definition)
		assert bodies == [ BODY_DIAGRAM, BODY_LABEL ]
		bodies -= BODY_LABEL
		schema.diagramData.label
	}
	
	void memoDate(String memoDate) {
		assert !bodies
		schema.memoDate = memoDate		
	}
	
	void name(String name) {
		assert !bodies
		schema.name = name
	}
	
	SchemaRecord record() {
		def definition = { }
		record(definition)
	}
	
	SchemaRecord record(Closure definition) {
		assert !bodies
		RecordModelBuilder recordBuilder = new RecordModelBuilder( [ schema : schema ] )
		if (bufferedName) {
			recordBuilder.build(bufferedName, definition)
			bufferedName = null
		} else {
			recordBuilder.build(definition)
		}
	}
	
	SchemaRecord record(String name) {
		assert !bodies
		assert !bufferedName
		RecordModelBuilder recordBuilder = new RecordModelBuilder( [ schema : schema ] )
		recordBuilder.build(name)
	}
	
	Set set() {
		def definition = { }
		set(definition)
	}
	
	Set set(Closure definition) {
		assert !bodies
		SetModelBuilder setBuilder = new SetModelBuilder( [ schema : schema ] )
		if (bufferedName) {
			setBuilder.build(bufferedName, definition)
			bufferedName = null
		} else {
			setBuilder.build(definition)
		}
	}
	
	Set set(String name) {
		assert !bodies
		assert !bufferedName
		SetModelBuilder setBuilder = new SetModelBuilder( [ schema : schema ] )
		setBuilder.build(name)
	}
	
	private void showGrid() {
		assert bodies == [ BODY_DIAGRAM ]
		schema.diagramData.showGrid = true
	}
	
	private void showRulersAndGuides() {
		assert bodies == [ BODY_DIAGRAM ]
		schema.diagramData.showRulers = true
	}
	
	private void snapToGeometry() {
		assert bodies == [ BODY_DIAGRAM ]
		schema.diagramData.snapToGeometry = true
	}
	
	private void snapToGrid() {
		assert bodies == [ BODY_DIAGRAM ]
		schema.diagramData.snapToGrid = true
	}
	
	private void snapToGuides() {
		assert bodies == [ BODY_DIAGRAM ]
		schema.diagramData.snapToGuides = true
	}
	
	void version(int version) {
		assert !bodies
		schema.version = (short) version
	}
	
	void verticalGuides(String positions) {
		assert bodies == [ BODY_DIAGRAM ]
		// mind that the VERTICAL guides are owned by the HORIZONTAL ruler
		createGuides(positions, schema.diagramData.horizontalRuler)
	}
	
	void width(int width) {
		assert bodies == [ BODY_DIAGRAM, BODY_LABEL ]
		schema.diagramData.label.width = (short) width
	}
	
	void x(int x) {
		assert bodies == [ BODY_DIAGRAM, BODY_LABEL ]
		schema.diagramData.label.diagramLocation.x = x
	}
	
	void y(int y) {
		assert bodies == [ BODY_DIAGRAM, BODY_LABEL ]
		schema.diagramData.label.diagramLocation.y = y
	}
	
	void zoom(int zoom) {
		assert bodies == [ BODY_DIAGRAM ]
		schema.diagramData.zoomLevel = zoom / 100.0
	}
}
