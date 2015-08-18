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

class SchemaSyntaxBuilder extends AbstractSyntaxBuilder<Schema> {
	
	@Override
	protected String build() {		
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
		without_tab "name '${model.name}'"
		without_tab "version ${model.version}"
	}
	
	private void description() {
		if (model.description) {
			without_tab "description ${withQuotes(model.description)}"
		}
	}
	
	private void memoDate() {
		if (model.memoDate) {
			without_tab "memoDate ${withQuotes(model.memoDate)}"
		}
	}
	
	private void comments() {
		if (model.comments) {
			for (comment in model.comments) {
				without_tab "comments ${withQuotes(comment)})"
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
		DiagramData diagramData = model.diagramData
		if (diagramData.label) {
			with_1_tab 'label {'
			with_2_tabs "description \"${diagramData.label.description}\""
			with_2_tabs "x ${diagramData.label.diagramLocation.x}"
			with_2_tabs "y ${diagramData.label.diagramLocation.y}"
			with_2_tabs "width ${diagramData.label.width}"
			with_2_tabs "height ${diagramData.label.height}"
			with_1_tab '}'			
		}	
	}
	
	private void zoom() {
		if (model.diagramData.zoomLevel != 1.0) {
			with_1_tab "zoom ${(int) model.diagramData.zoomLevel * 100}"
		}
	}
	
	private void showRulersAndGuides() {
		if (model.diagramData.showRulers) {
			with_1_tab 'showRulersAndGuides'			
		}
	}
	
	private void showGrid() {
		if (model.diagramData.showGrid) {
			with_1_tab 'showGrid'
		}
	}
	
	private void snapToGuides() {
		if (model.diagramData.snapToGuides) {
			with_1_tab 'snapToGuides'
		}
	}
	
	private void snapToGrid() {
		if (model.diagramData.snapToGrid) {
			with_1_tab 'snapToGrid'
		}
	}
	
	private void snapToGeometry() {
		if (model.diagramData.snapToGeometry) {
			with_1_tab 'snapToGeometry'
		}
	}
	
	private void endDiagram() {
		without_tab '}'
	}
	
	private void areas() {
		def areaDslBuilderProperties = [ output : output , initialTabs : 1 , generateName : false ];
		for (area in model.areas) {
			if (area.procedures) {				
				blank_line()
				without_tab "area '${area.name}' {"
				new AreaSyntaxBuilder(areaDslBuilderProperties).build(area)
				without_tab '}'
			}
		}
	}
	
	private void records() {
		def recordDslBuilderProperties = [ output : output , initialTabs : 1 , generateName : false ];
		for (record in model.records) {
			blank_line()
			without_tab "record '${record.name}' {"
			new RecordSyntaxBuilder(recordDslBuilderProperties).build(record)
			without_tab '}'
		}
	}
	
	private void sets() {
		def setDslBuilderProperties = [ output : output , initialTabs : 1 , generateName : false ];
		for (set in model.sets) {
			blank_line()
			without_tab "set '${set.name}' {"
			new SetSyntaxBuilder(setDslBuilderProperties).build(set)
			without_tab '}'
		}
	}	

}
