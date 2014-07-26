/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard.helper;

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;

public class NewSchemaWizardHelper {

	private IDiagramDataAttributeProvider diagramDataAttributeProvider;
	
	@SuppressWarnings("unused")
	private NewSchemaWizardHelper() {
		super(); // disabled constructor
	}
	
	public NewSchemaWizardHelper(IDiagramDataAttributeProvider diagramDataAttributeProvider) {
		super();
		this.diagramDataAttributeProvider = diagramDataAttributeProvider;
	}
	
	public Schema createSchema() {
		
		// create the schema and set its name and version
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		schema.setName("NEWSCHEM");
		schema.setVersion((short) 1);
		
		// diagram data with rulers (todo: set these attributes 
		// according to the editor's preferences)...
		DiagramData diagramData = 
			SchemaFactory.eINSTANCE.createDiagramData();
		schema.setDiagramData(diagramData);
		Ruler verticalRuler = 
			SchemaFactory.eINSTANCE.createRuler();
		verticalRuler.setType(RulerType.VERTICAL);
		diagramData.setVerticalRuler(verticalRuler);
		diagramData.getRulers()
				   .add(verticalRuler); // ruler container
		Ruler horizontalRuler = 
			SchemaFactory.eINSTANCE.createRuler();
		horizontalRuler.setType(RulerType.HORIZONTAL);
		diagramData.setHorizontalRuler(horizontalRuler);
		diagramData.getRulers()
				   .add(horizontalRuler); // ruler container
		
		// set the diagram data properties:
		schema.getDiagramData().setShowRulers(diagramDataAttributeProvider.isShowRulers());
		schema.getDiagramData().setShowGrid(diagramDataAttributeProvider.isShowGrid());
		schema.getDiagramData().setSnapToGuides(diagramDataAttributeProvider.isSnapToGuides());
		schema.getDiagramData().setSnapToGrid(diagramDataAttributeProvider.isSnapToGrid());
		schema.getDiagramData().setSnapToGeometry(diagramDataAttributeProvider.isSnapToGeometry());		
		
		return schema;
	}	
	
}
