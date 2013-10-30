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
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.figure.SetDescriptionFigure;

public class SetDescriptionEditPart 
    extends AbstractNonResizableDiagramNodeEditPart<ConnectionLabel>  {

	private SetDescriptionEditPart() {
		super(null); // disabled constructor
	}
	
	public SetDescriptionEditPart(ConnectionLabel connectionLabel) {
		super(connectionLabel);		
	}	

	@Override
	protected void adjustModelObjects(Notification notification,
									  List<EObject> modelObjects) {
	
		if (notification.getFeature() == SchemaPackage.eINSTANCE
					 								  .getSystemOwner_AreaSpecification()) {
	
			Assert.isTrue(notification.getOldValue() != null &&
						  notification.getNewValue() != null, 
						  "logic error: notification.getOldValue() == null || " +
						  "notification.getNewValue() == null");
			AreaSpecification oldValue = 
				(AreaSpecification) notification.getOldValue();
			AreaSpecification newValue = 
				(AreaSpecification) notification.getNewValue();
			removeModelObject(oldValue.getArea());
			addModelObject(newValue.getArea());
		}
		
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new SetDescriptionFigure();	
		
		// add a tooltip containing the set's name...
        String adjustedSetName;
        Set set = getModel().getMemberRole().getSet();
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
		
		List<EObject> modelObjects = new ArrayList<>();		

		modelObjects.add(getModel());
		modelObjects.add(getModel().getMemberRole());
		modelObjects.add(getModel().getMemberRole().getSet());
		
		SystemOwner systemOwner =
			getModel().getMemberRole().getSet().getSystemOwner();
		if (systemOwner != null) {
			// in the case of a system owned indexed set, make sure we get 
			// notifications when the area name changes
			modelObjects.add(systemOwner);
			SchemaArea area = getModel().getMemberRole()
										.getSet()
										.getSystemOwner()
										.getAreaSpecification()
										.getArea(); 
			modelObjects.add(area);
		}
		
		modelObjects.add(getModel().getDiagramLocation());		
		
		return modelObjects.toArray(new EObject[] {});
	}

	@Override
	public void showSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			// Change the line color of the connection parts and connectors to 
			// which this label belongs to red so that the user can see to which 
			// connection parts the label belongs.
			for (ConnectionPart connectionPart : 
				 getModel().getMemberRole().getConnectionParts()) {
				
				SetEditPart setEditPart = 
					(SetEditPart) getViewer().getEditPartRegistry()
											 .get(connectionPart);
				PolylineConnection connection = 
					(PolylineConnection) setEditPart.getFigure();
				connection.setLineWidth(2);
				connection.setForegroundColor(ColorConstants.red);
				
				Connector connector = connectionPart.getConnector();
				if (connector != null) {
					ConnectorEditPart connectorEditPart = 
						(ConnectorEditPart) getViewer().getEditPartRegistry()
													   .get(connector);
					((ConnectorFigure)connectorEditPart.getFigure()).setLineWidth(2);
					connectorEditPart.getFigure()
								     .setForegroundColor(ColorConstants.red);
				}
			}
		}
		super.showSourceFeedback(request);
	}
	
	@Override
	public void eraseSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			// Change the line color of the connection parts and connectors to 
			// which this label belongs back to black.
			for (ConnectionPart connectionPart : 
				 getModel().getMemberRole().getConnectionParts()) {
				
				SetEditPart setEditPart = 
					(SetEditPart) getViewer().getEditPartRegistry()
											 .get(connectionPart);
				PolylineConnection connection = 
					(PolylineConnection) setEditPart.getFigure();
				connection.setLineWidth(1);
				connection.setForegroundColor(ColorConstants.black);
				
				Connector connector = connectionPart.getConnector();
				if (connector != null) {
					ConnectorEditPart connectorEditPart = 
						(ConnectorEditPart) getViewer().getEditPartRegistry()
													   .get(connector);
					((ConnectorFigure)connectorEditPart.getFigure()).setLineWidth(1);
					connectorEditPart.getFigure()
								     .setForegroundColor(ColorConstants.black);
				}
			}
		}
		super.eraseSourceFeedback(request);
	}

	@Override
	protected void setFigureData() {
		
		MemberRole memberRole = getModel().getMemberRole();
		
		SetDescriptionFigure figure = (SetDescriptionFigure) getFigure();
		
		// we need to manipulate the set name in the case of some dictionary
		// sets (DDLCATLOD area, which has the same structure as DDLDCLOD)...
		StringBuilder setName = 
			new StringBuilder(memberRole.getSet().getName());
		if (setName.toString().endsWith("_")) {
			setName.setLength(setName.length() - 1);
		}
		figure.setName(setName.toString());
		
		figure.setPointers(Tools.getPointers(memberRole));
		figure.setMembershipOption(Tools.getMembershipOption(memberRole));
		figure.setOrder(memberRole.getSet().getOrder().toString());
		
		figure.setSortKeys(Tools.getSortKeys(memberRole));		
		
		figure.setSystemOwnerArea(Tools.getSystemOwnerArea(memberRole));
		
	}

}
