/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;

public class SchemaDiagramEditPartFactory implements EditPartFactory {
	
	private IModelChangeProvider modelChangeProvider;	
	
	public static EditPart createEditPart(Object model, IModelChangeProvider modelChangeProvider) {
		if (model instanceof Schema) {
			return new SchemaEditPart((Schema) model, modelChangeProvider);
		} else if (model instanceof SchemaRecord) {
			return new RecordEditPart((SchemaRecord) model, modelChangeProvider);
		} else if (model instanceof SystemOwner) {
			return new IndexEditPart((SystemOwner) model, modelChangeProvider);
		} else if (model instanceof ConnectionPart) {
			return new SetEditPart((ConnectionPart) model, modelChangeProvider);
		} else if (model instanceof ConnectionLabel) {
			return new SetDescriptionEditPart((ConnectionLabel) model, modelChangeProvider);
		} else if (model instanceof Connector) {
			return new ConnectorEditPart((Connector) model, modelChangeProvider);
		} else if (model instanceof DiagramLabel) {
			return new DiagramLabelEditPart((DiagramLabel) model, modelChangeProvider);
		}
		throw new IllegalStateException("No EditPart for " + model.getClass());		
	}

	public SchemaDiagramEditPartFactory(IModelChangeProvider modelChangeProvider) {
		super();
		this.modelChangeProvider = modelChangeProvider;
	}
	
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// just delegate to our public static method
		return createEditPart(model, modelChangeProvider);
	}

}
