package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.figure.SetDescriptionFigure;

public class SetDescriptionEditPart 
    extends AbstractDiagramNodeEditPart<ConnectionLabel>  {

	private SetDescriptionEditPart() {
		super(null); // disabled constructor
	}
	
	public SetDescriptionEditPart(ConnectionLabel connectionLabel) {
		super(connectionLabel);		
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
		return new EObject[] {getModel(),
							  getModel().getMemberRole(),
						      getModel().getMemberRole().getSet(),
							  getModel().getDiagramLocation()};
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