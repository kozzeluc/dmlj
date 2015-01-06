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
package org.lh.dmlj.schema.editor.policy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.CreateDiagramLabelCommand;
import org.lh.dmlj.schema.editor.command.CreateRecordCommand;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.ModelChangeBasicCommand;
import org.lh.dmlj.schema.editor.command.MoveDiagramNodeCommand;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.figure.DiagramLabelFigure;
import org.lh.dmlj.schema.editor.part.AbstractDiagramNodeEditPart;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class SchemaXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private Schema schema;
	
	public SchemaXYLayoutEditPolicy(Schema schema) {
		super();
		this.schema = schema;
	}
	
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, 
													EditPart child,
													Object constraint) {
		
		if (child.getModel() instanceof DiagramNode) {
			// we're dealing with a DiagramNode, it can only be a
			// move request, so create the move command...
			Rectangle box = (Rectangle)constraint;
			DiagramNode diagramNode = (DiagramNode) child.getModel();
			MoveDiagramNodeData moveDiagramNodeData = new MoveDiagramNodeData(diagramNode);
			ModelChangeContext context = 
				new ModelChangeContext(moveDiagramNodeData.getModelChangeType());
			context.putContextData(diagramNode);
			IModelChangeCommand command = new MoveDiagramNodeCommand(diagramNode, box.x, box.y);
			command.setContext(context);
			return (Command) command;
		} else {
			// not a DiagramNode or user is trying to resize, make
			// sure he/she gets the right feedback
			return null;
		}
	}
	
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {		
		if (child instanceof AbstractDiagramNodeEditPart<?>) {
			// a diagram node edit part (resizable or not)
			return ((AbstractDiagramNodeEditPart<?>) child).getResizeEditPolicy();
		} else {
			// any other edit part
			return super.createChildEditPolicy(child);
		}
	}
	
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		
		if (request.getNewObjectType() != DiagramLabel.class && 
			request.getNewObjectType() != SchemaRecord.class ||
			request.getNewObjectType() == DiagramLabel.class && 
			schema.getDiagramData().getLabel() != null) {
			
			return null;
		}	
		
		if (request.getNewObjectType() == DiagramLabel.class) {
			String organisation = Plugin.getDefault()
										.getPreferenceStore()
										.getString(PreferenceConstants.DIAGRAMLABEL_ORGANISATION);
			String lastModified = null;
			if (Plugin.getDefault()
					  .getPreferenceStore()
					  .getBoolean(PreferenceConstants.DIAGRAMLABEL_SHOW_LAST_MODIFIED)) {
				
				String pattern = 
					Plugin.getDefault()
						  .getPreferenceStore()
						  .getString(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN);
				DateFormat format = new SimpleDateFormat(pattern);
				lastModified = 
					"Last modified: " + format.format(System.currentTimeMillis()) + " (not saved)";
			}
			Dimension size = 
				DiagramLabelFigure.getInitialSize(organisation, schema.getName(), schema.getVersion(), 
												  schema.getDescription(), lastModified); 
			PrecisionPoint p = new PrecisionPoint(request.getLocation().x, request.getLocation().y); 				
			getHostFigure().translateToRelative(p);
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_DIAGRAM_LABEL);
			ModelChangeBasicCommand command = new CreateDiagramLabelCommand(schema, p, size);
			command.setContext(context);
			return command;
		} else if (request.getNewObjectType() == SchemaRecord.class) {
			PrecisionPoint p = new PrecisionPoint(request.getLocation().x, request.getLocation().y); 				
			getHostFigure().translateToRelative(p);
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
			ModelChangeBasicCommand command = new CreateRecordCommand(schema, p);
			command.setContext(context);
			return command;
		}
		return null;
	}	
	
	public static class MoveDiagramNodeData {
		
		private DiagramNode diagramNode;
		
		public MoveDiagramNodeData(DiagramNode diagramNode) {
			super();
			this.diagramNode = diagramNode;
		}
		
		public ModelChangeType getModelChangeType() {
			if (diagramNode instanceof ConnectionLabel) {
				return ModelChangeType.MOVE_SET_OR_INDEX_LABEL;
			} else if (diagramNode instanceof Connector) {
				return ModelChangeType.MOVE_CONNECTOR;
			} else if (diagramNode instanceof DiagramLabel) {
				return ModelChangeType.MOVE_DIAGRAM_LABEL;
			} else if (diagramNode instanceof SchemaRecord) {
				return ModelChangeType.MOVE_RECORD;
			} else if (diagramNode instanceof SystemOwner) {
				return ModelChangeType.MOVE_INDEX;
			} else {
				throw new IllegalStateException("Unexpected diagram node: " + diagramNode);
			}
		}
		
	}
	
}
