/**
 * Copyright (C) 2016  Luc Hermans
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.anchor.VsamIndexSourceAnchor;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.figure.VsamIndexFigure;
import org.lh.dmlj.schema.editor.policy.VsamIndexComponentEditPolicy;

public class VsamIndexEditPart extends AbstractNonResizableDiagramNodeEditPart<VsamIndex> {

	private VsamIndexEditPart() { 
		super(null, null); // disabled constructor
	}
	
	public VsamIndexEditPart(VsamIndex vsamIndex, IModelChangeProvider modelChangeProvider) {
		super(vsamIndex, modelChangeProvider);
	}	
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if ((context.getModelChangeType() == ModelChangeType.MOVE_VSAM_INDEX ||
			 context.getModelChangeType() == ModelChangeType.MOVE_GROUP_OF_DIAGRAM_NODES) &&
			context.appliesTo(getModel().getSet())) {
			
			// the VSAM index was moved
			refreshVisuals();			
			refreshConnections();
		}
	}

	@Override
	protected void createEditPolicies() {			
		if (!isReadOnlyMode()) {
			// the next edit policy allows for the deletion of a VSAM index
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new VsamIndexComponentEditPolicy());
		}
	}
	
	@Override
	protected IFigure createFigure() {
		Figure figure = new VsamIndexFigure();
		
		// add a tooltip containing the set's name...
        String adjustedSetName;
        Set set = getModel().getSet();
        if (set.getName().endsWith("_")) {
            StringBuilder p = new StringBuilder(set.getName());
            p.setLength(p.length() - 1);
            adjustedSetName = p.toString();
        } else {
            adjustedSetName = set.getName();
        }
        Label tooltip = new Label(adjustedSetName);
        figure.setToolTip(tooltip);
		
		return figure;
	}		

	@Override
	protected List<ConnectionPart> getModelSourceConnections() {
		List<ConnectionPart> connectionParts = new ArrayList<>();
		MemberRole memberRole = getModel().getMemberRole();
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);					
		connectionParts.add(connectionPart);
		return connectionParts;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new VsamIndexSourceAnchor((VsamIndexFigure) getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new VsamIndexSourceAnchor((VsamIndexFigure) getFigure());
	}
	
	@Override
	protected void refreshConnections() {
		MemberRole memberRole = getModel().getMemberRole();
		for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
			GraphicalEditPart editPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry().get(connectionPart);
			editPart.refresh();		
		}
	}

	@Override
	protected void setFigureData() {
		//no data to set
	}
	
}
