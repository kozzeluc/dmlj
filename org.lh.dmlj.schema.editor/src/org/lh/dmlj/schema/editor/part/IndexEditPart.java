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
package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.anchor.IndexSourceAnchor;
import org.lh.dmlj.schema.editor.figure.IndexFigure;
import org.lh.dmlj.schema.editor.policy.IndexComponentEditPolicy;

public class IndexEditPart extends AbstractNonResizableDiagramNodeEditPart<SystemOwner> {

	private IndexEditPart() {
		super(null); // disabled constructor
	}
	
	public IndexEditPart(SystemOwner systemOwner) {
		super(systemOwner);
	}	

	@Override
	protected void createEditPolicies() {			
		// the next edit policy allows for the deletion of an index
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new IndexComponentEditPolicy());		
	}
	
	@Override
	protected IFigure createFigure() {
		Figure figure = new IndexFigure();
		
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
	protected EObject[] getModelObjects() {
		return new EObject[] {getModel(), 
							  getModel().getDiagramLocation()};
	}

	@Override
	protected List<ConnectionPart> getModelSourceConnections() {
		List<ConnectionPart> connectionParts = new ArrayList<>();
		MemberRole memberRole = getModel().getSet().getMembers().get(0);
		ConnectionPart connectionPart = memberRole.getConnectionParts().get(0);					
		connectionParts.add(connectionPart);
		return connectionParts;
	}

	/**
	 * Finds the index's member record edit part by following the source connection to it.  This 
	 * can be useful when an index was deleted and its member record edit part needs to be 
	 * refreshed.  This method will only work if no connectors are put between index and record, so
	 * first remove the connectors before ever removing an index.
	 * @return the index's member record edit part
	 */
	public RecordEditPart getRecordEditPart() {
		List<?> sourceConnections = getSourceConnections();
		Assert.isTrue(sourceConnections.size() == 1, 
					  "expected only 1 source target connection: " + sourceConnections.size());
		Assert.isTrue(sourceConnections.get(0) instanceof SetEditPart,
					  "expected an org.lh.dmlj.schema.editor.part.SetEditPart: " +
					  sourceConnections.get(0).getClass().getName());
		SetEditPart setEditPart = (SetEditPart) sourceConnections.get(0);
		Assert.isTrue(setEditPart.getTarget() instanceof RecordEditPart,
				  	  "expected an org.lh.dmlj.schema.editor.part.RecordEditPart: " +
					  setEditPart.getTarget().getClass().getName());
		return (RecordEditPart) setEditPart.getTarget();
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new IndexSourceAnchor((IndexFigure) getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new IndexSourceAnchor((IndexFigure) getFigure());
	}
	
	@Override
	protected void refreshConnections() {
		MemberRole memberRole = getModel().getSet().getMembers().get(0);
		for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
			GraphicalEditPart editPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry()
										  	   .get(connectionPart);
			editPart.refresh();		
		}
	}

	@Override
	protected void setFigureData() {
		//no data to set
	}
	
}
