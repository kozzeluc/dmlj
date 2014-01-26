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
package org.lh.dmlj.schema.editor.outline.part;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;

public class SchemaTreeEditPartFactory implements EditPartFactory {
	
	private IModelChangeProvider modelChangeProvider;
	
	public static EditPart createEditPart(Object model, IModelChangeProvider modelChangeProvider) {
		if (model instanceof Schema) {
			return new SchemaTreeEditPart((Schema) model, modelChangeProvider);
		} else if (model instanceof DiagramLabel) {
			return new DiagramLabelTreeEditPart((DiagramLabel) model, modelChangeProvider);
		} else if (model instanceof SchemaArea) {
			return new AreaTreeEditPart((SchemaArea) model, modelChangeProvider);
		} else if (model instanceof SchemaRecord) {
			return new RecordTreeEditPart((SchemaRecord) model, modelChangeProvider);
		} else if (model instanceof ConnectionPart) {
			return new SetTreeEditPart((ConnectionPart) model, modelChangeProvider);
		} else if (model instanceof SystemOwner) {
			return new IndexTreeEditPart((SystemOwner) model, modelChangeProvider);
		}
		throw new IllegalArgumentException("Unexpected model type: " + model.getClass().getName());
	}
	
	public SchemaTreeEditPartFactory(SchemaEditor editor) {
		super();
		modelChangeProvider = (IModelChangeProvider) editor.getAdapter(IModelChangeProvider.class);
		Assert.isNotNull(modelChangeProvider, "modelChangeProvider == null");
	}

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// just delegate to our public static model
		return createEditPart(model, modelChangeProvider);
	}

}
