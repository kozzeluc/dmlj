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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import org.lh.dmlj.schema.DiagramData
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set

import groovy.transform.CompileStatic;

@CompileStatic
class SchemaSyntaxBuilder extends AbstractSyntaxBuilder<Schema> {
	
	private Schema schema
	
	@Override
	protected String doBuild(Schema model) {	
		schema = model	
		nameAndVersion()
		description()
		memoDate()
		comments()		
		diagramData()
		areas()
		records()
		sets()
	}
	
	private void nameAndVersion() {
		without_tab "name '${schema.name}'"
		without_tab "version ${schema.version}"
	}
	
	private void description() {
		if (schema.description) {
			without_tab "description ${withQuotes(schema.description)}"
		}
	}
	
	private void memoDate() {
		if (schema.memoDate) {
			without_tab "memoDate ${withQuotes(schema.memoDate)}"
		}
	}
	
	private void comments() {
		if (schema.comments) {
			schema.comments.each { comment ->
				without_tab "comments ${withQuotes(comment)}"
			}
		}
	}
	
	private void diagramData() {
		beginDiagram()
		label()
		zoom()
		showRulersAndGuides()
		showGrid()
		snapToGuides()
		snapToGrid()
		snapToGeometry()
		endDiagram()	
	}
	
	private void beginDiagram() {
		blank_line()
		without_tab 'diagram {'
	}
	
	private void label() {
		DiagramData diagramData = schema.diagramData
		if (diagramData.label) {
			with_1_tab 'label {'
			with_2_tabs "description \"${diagramData.label.description}\""
			with_2_tabs "x ${xOrY(diagramData.label.diagramLocation.x)}"
			with_2_tabs "y ${xOrY(diagramData.label.diagramLocation.y)}"
			with_2_tabs "width ${diagramData.label.width}"
			with_2_tabs "height ${diagramData.label.height}"
			with_1_tab '}'			
		}	
	}
	
	private void zoom() {
		if (schema.diagramData.zoomLevel != 1.0) {
			with_1_tab "zoom ${(int) schema.diagramData.zoomLevel * 100}"
		}
	}
	
	private void showRulersAndGuides() {
		if (schema.diagramData.showRulers) {
			with_1_tab 'showRulersAndGuides'			
		}
	}
	
	private void showGrid() {
		if (schema.diagramData.showGrid) {
			with_1_tab 'showGrid'
		}
	}
	
	private void snapToGuides() {
		if (schema.diagramData.snapToGuides) {
			with_1_tab 'snapToGuides'
		}
	}
	
	private void snapToGrid() {
		if (schema.diagramData.snapToGrid) {
			with_1_tab 'snapToGrid'
		}
	}
	
	private void snapToGeometry() {
		if (schema.diagramData.snapToGeometry) {
			with_1_tab 'snapToGeometry'
		}
	}
	
	private void endDiagram() {
		without_tab '}'
	}
	
	private void areas() {
		schema.areas.each { area ->
			if (area.procedures || !area.areaSpecifications) {				
				AreaSyntaxBuilder areaSyntaxBuilder = 
					new AreaSyntaxBuilder([ output : output , initialTabs : 1 , generateName : false ])								
				blank_line()
				without_tab "area '${area.name}' {"
				areaSyntaxBuilder.build(area)
				without_tab '}'
			}
		}
	}
	
	private void records() {
		schema.records.each { record ->
			
			RecordSyntaxBuilder recordSyntaxBuilder =
				new RecordSyntaxBuilder([ output : output , initialTabs : 1 , generateName : false ])
			
			blank_line()
			without_tab "record '${record.name}' {"
			recordSyntaxBuilder.build(record)
			without_tab '}'
		}
	}
	
	private void sets() {
		schema.sets.each { set ->
			
			SetSyntaxBuilder setSyntaxBuilder =
				new SetSyntaxBuilder([ output : output , initialTabs : 1 , generateName : false ])
			
			blank_line()
			without_tab "set '${set.name}' {"
			setSyntaxBuilder.build(set)
			without_tab '}'
		}
	}	

}
