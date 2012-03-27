package org.lh.dmlj.schema.editor.policy;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.anchor.ReconnectEndpointAnchor;
import org.lh.dmlj.schema.editor.command.MoveEndpointCommand;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.part.SetEditPart;

/**
 * An edit policy that enables moving both connection end points.
 */
public class RecordGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	private RecordFigure   figure;
	private SchemaRecord   record;
	private EditPartViewer viewer;	
	
	public RecordGraphicalNodeEditPolicy(SchemaRecord record,
										 RecordFigure figure,
										 EditPartViewer viewer) {	
		
		super();		
		this.record = record;
		this.figure = figure;
		this.viewer = viewer;
	}
	
	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		return null;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		// do not allow to change the owner of the set; only the start
		// location can be changed; we currently don't support split
		// set connections (i.e. a set with 2 connection parts each with 
		// a connector attached)...
		if (!(request.getConnectionEditPart() instanceof SetEditPart)) {
			return null;
		}
		ConnectionPart connectionPart = 
			(ConnectionPart) request.getConnectionEditPart().getModel();
		List<ConnectionPart> connectionParts = 
			connectionPart.getMemberRole().getConnectionParts();
		if (connectionParts.size() > 1 && 
			connectionPart == connectionParts.get(1)) {
				
			// cannot set the source endpoint location on a connector 
			return null;
		}
		OwnerRole ownerRole = 
			connectionPart.getMemberRole().getSet().getOwner();
		if (ownerRole != null && ownerRole.getRecord() == record) {	
			Point reference;
			if (connectionPart.getBendpointLocations().isEmpty()) {
				DiagramLocation targetConnectionPoint = 
					connectionPart.getTargetEndpointLocation();
				if (targetConnectionPoint != null) {							
					reference = 
						new PrecisionPoint(targetConnectionPoint.getX(), 
										   targetConnectionPoint.getY());
				} else {
					SchemaRecord record =
						connectionPart.getMemberRole().getRecord();
					GraphicalEditPart editPart = 
						(GraphicalEditPart) viewer.getEditPartRegistry()
												  .get(record);
					reference = 
						editPart.getFigure().getBounds().getCenter();
					editPart.getFigure().translateToAbsolute(reference);
				}
			} else {
				int i = 
					connectionPart.getBendpointLocations().size() - 1;
				DiagramLocation lastBendpoint = 
					connectionPart.getBendpointLocations().get(i);
				reference = new PrecisionPoint(lastBendpoint.getX(), 
											   lastBendpoint.getY());
			}
			double zoomLevel = connectionPart.getMemberRole()
											 .getSet()
											 .getSchema()
											 .getDiagramData()
											 .getZoomLevel();
			Point location = 
				ReconnectEndpointAnchor.getRelativeLocation((RecordFigure)figure, 
														    request.getLocation(), 
														    zoomLevel);
			return new MoveEndpointCommand(connectionPart, location.x, 
										   location.y, true);					
		} else {
			return null;
		}
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		// do not allow to change the member of the set; only the end
		// location can be changed
		if (!(request.getConnectionEditPart() instanceof SetEditPart)) {
			return null;
		}
		ConnectionPart connectionPart = 
			(ConnectionPart) request.getConnectionEditPart().getModel();
		List<ConnectionPart> connectionParts = 
			connectionPart.getMemberRole().getConnectionParts();
		if (connectionParts.size() > 1 && 
			connectionPart == connectionParts.get(0)) {
			
			// cannot set the target endpoint location on a connector 
			return null;
		}
		if (record == connectionPart.getMemberRole().getRecord()) {					
			double zoomLevel = connectionPart.getMemberRole()
											 .getSet()
											 .getSchema()
											 .getDiagramData()
											 .getZoomLevel();
			Point location = 
				ReconnectEndpointAnchor.getRelativeLocation((RecordFigure)figure, 
															request.getLocation(), 
															zoomLevel);
			return new MoveEndpointCommand(connectionPart, location.x, 
										   location.y, false);					
		} else {
			return null;
		}
	}	
	
}