/**
 * Copyright (C) 2026  Luc Hermans
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

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.anchor.ConnectorAnchor;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.palette.IMultipleMemberSetPlaceHolder;
import org.lh.dmlj.schema.editor.policy.ConnectorComponentEditPolicy;

public class ConnectorEditPart extends AbstractNonResizableDiagramNodeEditPart<Connector> {
	private MemberRole memberRole;
	
	public ConnectorEditPart(Connector connector, IModelChangeProvider modelChangeProvider) {
		super(connector, modelChangeProvider);
		// keep track of the MemberRole because if connectors are deleted, the reference to that MemberRole will
		// be nullified in the ConnectionPart
		this.memberRole = connector.getConnectionPart().getMemberRole();
	}	
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if ((context.getModelChangeType() == ModelChangeType.MOVE_CONNECTOR || context.getModelChangeType() == ModelChangeType.MOVE_GROUP_OF_DIAGRAM_NODES) &&
			context.appliesTo(getModel())) {
						
			refreshVisuals();			
			refreshConnections();			
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getConnector_Label()) &&
				   context.appliesTo((memberRole.getConnectionParts().get(0).getConnector()))) {
			
			// the connector label is set; refresh the edit part's visuals (note that the context data ALWAYS
			// refers to the FIRST connector in this situation, although both connectors are impacted)
			refreshVisuals();			
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
				   (context.getCommandExecutionMode() != CommandExecutionMode.UNDO && Boolean.TRUE.equals(context.getListenerData())) ||
				    context.getCommandExecutionMode() == CommandExecutionMode.UNDO && context.appliesTo(getModel().getConnectionPart().getMemberRole().getSet())) {
			
			// the set name has changed (execute/undo/redo)
			refreshVisuals();						
		}		
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(getModel().getConnectionPart().getMemberRole().getSet())) {
					
			// the model set's name is changing (execute/redo); put Boolean.TRUE in the context's listener's data
			// so that we can respond to this when processing the after model change event (so we can update the
			// figure's tooltip)
			context.setListenerData(Boolean.TRUE);
		}
	}
	
	@Override
	protected void createEditPolicies() {
		if (!isReadOnlyMode()) {
			// make sure we can delete connectors:
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new ConnectorComponentEditPolicy());
		}
	}
	
	@Override
	protected IFigure createFigure() {
		var figure = new ConnectorFigure();
		
		// add a tooltip containing the set's name...
        String adjustedSetName;
        var set = getModel().getConnectionPart().getMemberRole().getSet();
        if (set.getName().endsWith("_")) {
            var p = new StringBuilder(set.getName());
            p.setLength(p.length() - 1);
            adjustedSetName = p.toString();
        } else {
            adjustedSetName = set.getName();
        }
        var tooltip = new Label(adjustedSetName);
        figure.setToolTip(tooltip);
        
        return figure;
	}		

	public MemberRole getMemberRole() {
		return memberRole;
	}
	
	@Override	
	protected List<ConnectionPart> getModelSourceConnections() {
		if (getModel().getConnectionPart() == getModel().getConnectionPart().getMemberRole().getConnectionParts().get(1)) {
			return List.of(getModel().getConnectionPart());
		} else {
			return List.of();
		}
	}

	@Override
	protected List<ConnectionPart> getModelTargetConnections() {
		if (getModel().getConnectionPart() == getModel().getConnectionPart().getMemberRole().getConnectionParts().get(0)) {
			return List.of(getModel().getConnectionPart());
		} else {
			return List.of();
		}		
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ConnectorAnchor((ConnectorFigure) getFigure(), getModel());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ConnectorAnchor((ConnectorFigure) getFigure(), getModel());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ConnectorAnchor((ConnectorFigure) getFigure(), getModel());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ConnectorAnchor((ConnectorFigure) getFigure(), getModel());
	}
	
	@Override
	public EditPart getTargetEditPart(Request request) {
		// we need to do something special ONLY in the case when adding a member record type to a (possibly
		// already multiple-member) set...
		if (!(request instanceof CreateConnectionRequest createConnectionRequest) || createConnectionRequest.getNewObjectType() != IMultipleMemberSetPlaceHolder.class) {
			return super.getTargetEditPart(request);
		}
		
		// ...if the user has already selected a set, just have the default behaviour proceed
		var cRequest = (CreateConnectionRequest) request;
		if (cRequest.getSourceEditPart() != null) {			
			return super.getTargetEditPart(request);			
		}		
		
		// ...we want the line that is drawn, to start at the owner of the set, so 'redirect' to the connection
		// label edit part and have that take care of everything
		var connectionLabel = memberRole.getConnectionLabel();
		var setDescriptionEditPart = (SetDescriptionEditPart) getViewer().getEditPartRegistry().get(connectionLabel);
		Assert.isNotNull(setDescriptionEditPart, "no edit part for set description: " + memberRole.getSet().getName() +
				" (" + memberRole.getRecord(). getName() + ")");
		return setDescriptionEditPart;
		
	}	

	@Override
	protected void setFigureData() {
		var connector = getModel();
		var figure = (ConnectorFigure) getFigure();
		figure.setLabel(connector.getLabel());
		
		// we need to manipulate the set name in the case of some dictionary sets (DDLCATLOD area, which has the
		// same structure as DDLDCLOD)...
		var set = getModel().getConnectionPart().getMemberRole().getSet();
		var adjustedSetName = Tools.removeTrailingUnderscore(set.getName());
		figure.setName(adjustedSetName);
	}
	
	@Override
	protected void refreshConnections() {
		var connectionPart = getModel().getConnectionPart();
		var editPart = EditPart.class.cast(getViewer().getEditPartRegistry().get(connectionPart));
		editPart.refresh();
	}
	
}
