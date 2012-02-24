package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.anchor.LockedRecordSourceAnchor;
import org.lh.dmlj.schema.editor.anchor.LockedRecordTargetAnchor;
import org.lh.dmlj.schema.editor.anchor.ReconnectEndpointAnchor;
import org.lh.dmlj.schema.editor.command.MoveEndpointCommand;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class RecordEditPart 
	extends AbstractDiagramNodeEditPart<SchemaRecord>  {

	private RecordEditPart() {
		super(null); // disabled constructor
	}
	
	public RecordEditPart(SchemaRecord record) {
		super(record);		
	}	

	@Override
	protected void createEditPolicies() {
		final SchemaRecord record = getModel();
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
						  new GraphicalNodeEditPolicy() {

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
				// location can be changed...
				if (!(request.getConnectionEditPart() instanceof SetEditPart)) {
					return null;
				}
				MemberRole memberRole = 
					(MemberRole) request.getConnectionEditPart().getModel();
				OwnerRole ownerRole = memberRole.getSet().getOwner();
				if (ownerRole != null && ownerRole.getRecord() == record) {	
					Point reference;
					if (memberRole.getDiagramBendpoints().isEmpty()) {
						DiagramLocation targetConnectionPoint = 
								memberRole.getDiagramTargetAnchor();
						if (targetConnectionPoint != null) {							
							reference = 
								new PrecisionPoint(targetConnectionPoint.getX(), 
												   targetConnectionPoint.getY());
						} else {
							GraphicalEditPart editPart = 
								(GraphicalEditPart) getViewer().getEditPartRegistry()
													  		   .get(memberRole.getRecord());
							reference = 
								editPart.getFigure().getBounds().getCenter();
							editPart.getFigure().translateToAbsolute(reference);
						}
					} else {
						int i = memberRole.getDiagramBendpoints().size() - 1;
						DiagramLocation lastBendpoint = 
							memberRole.getDiagramBendpoints().get(i);
						reference = new PrecisionPoint(lastBendpoint.getX(), 
													   lastBendpoint.getY());
					}
					double zoomLevel = 
						memberRole.getSet().getSchema().getDiagramData().getZoomLevel();
					Point location = 
						ReconnectEndpointAnchor.getRelativeLocation((RecordFigure)figure, 
																    request.getLocation(), 
																    reference,
																    zoomLevel);
					return new MoveEndpointCommand(memberRole, location.x, 
												   location.y, true);					
				} else {
					return null;
				}
			}

			@Override
			protected Command getReconnectTargetCommand(ReconnectRequest request) {
				// do not allow to change the member of the set; only the end
				// location can be changed...
				if (!(request.getConnectionEditPart() instanceof SetEditPart)) {
					return null;
				}
				MemberRole memberRole = 
					(MemberRole) request.getConnectionEditPart().getModel();
				if (record == memberRole.getRecord()) {					
					Point reference;
					if (memberRole.getDiagramBendpoints().isEmpty()) {
						DiagramLocation sourceConnectionPoint = 
							memberRole.getDiagramSourceAnchor();
						if (sourceConnectionPoint != null) {
							reference = 
								new PrecisionPoint(sourceConnectionPoint.getX(), 
										   		   sourceConnectionPoint.getY());
						} else if (memberRole.getSet().getOwner() != null) {
							// user owned set
							SchemaRecord owner = 
								memberRole.getSet().getOwner().getRecord();
							GraphicalEditPart editPart = 
								(GraphicalEditPart) getViewer().getEditPartRegistry()
														  	   .get(owner);							
							reference = 
								editPart.getFigure().getBounds().getCenter();
							
							
							
						} else {
							// system owned set
							GraphicalEditPart editPart = 
								(GraphicalEditPart) getViewer().getEditPartRegistry()
														  	   .get(memberRole.getSet().getSystemOwner());
							reference = 
								editPart.getFigure().getBounds().getBottom();							
						}
					} else {
						DiagramLocation firstBendpoint = 
							memberRole.getDiagramBendpoints().get(0);
						reference = new PrecisionPoint(firstBendpoint.getX(), 
													   firstBendpoint.getY());
					}
					double zoomLevel = 
						memberRole.getSet().getSchema().getDiagramData().getZoomLevel();
					Point location = 
						ReconnectEndpointAnchor.getRelativeLocation((RecordFigure)figure, 
																	request.getLocation(), 
																	reference,
																	zoomLevel);
					return new MoveEndpointCommand(memberRole, location.x, 
												   location.y, false);					
				} else {
					return null;
				}
			}
			
		});
	}
	
	@Override
	protected IFigure createFigure() {
		return new RecordFigure();				
	}
	
	@Override
	protected EObject[] getModelObjects() {
		return new EObject[] {getModel(), 
							  getModel().getDiagramLocation()};
	}	

	@Override
	protected List<MemberRole> getModelSourceConnections() {
		List<MemberRole> memberRoles = new ArrayList<>();
		for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
			memberRoles.addAll(ownerRole.getSet().getMembers());
		}
		return memberRoles;
	}
	
	@Override
	protected List<MemberRole> getModelTargetConnections() {
		List<MemberRole> memberRoles = new ArrayList<>();
		for (MemberRole memberRole : getModel().getMemberRoles()) {
			memberRoles.add(memberRole);
		}
		return memberRoles;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new LockedRecordSourceAnchor((RecordFigure) getFigure(), 
									   		(MemberRole) connection.getModel());
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {		
		if (!(request instanceof ReconnectRequest)) {
			return super.getSourceConnectionAnchor(request);
		}
		ReconnectRequest rRequest = (ReconnectRequest)request;						
		MemberRole memberRole = 
			(MemberRole)rRequest.getConnectionEditPart().getModel();
		return new ReconnectEndpointAnchor((RecordFigure)getFigure(), 
										   rRequest.getLocation(), memberRole);	
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new LockedRecordTargetAnchor((RecordFigure) getFigure(), 
										   (MemberRole) connection.getModel());
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		if (!(request instanceof ReconnectRequest)) {
			return super.getSourceConnectionAnchor(request);
		}
		ReconnectRequest rRequest = (ReconnectRequest)request;		
		MemberRole memberRole = 
			(MemberRole)rRequest.getConnectionEditPart().getModel();
		return new ReconnectEndpointAnchor((RecordFigure)getFigure(), 
										   rRequest.getLocation(), memberRole);	
	}
	
	@Override
	protected void refreshConnections() {
		for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
			for (MemberRole memberRole : ownerRole.getSet().getMembers()) {
				GraphicalEditPart editPart = 
					(GraphicalEditPart) getViewer().getEditPartRegistry()
											  	   .get(memberRole);
				editPart.refresh();
			}
		}		
	}

	@Override
	protected void setFigureData() {
		SchemaRecord record = getModel();
		RecordFigure figure = (RecordFigure) getFigure();
		// we need to manipulate the record name in the case of some dictionary
		// records (DDLCATLOD area, which has the same structure as DDLDCLOD)...
		StringBuilder recordName = new StringBuilder(record.getName());
		if (record.getName().endsWith("_")) {
			recordName.setLength(recordName.length() - 1);
		}
		figure.setRecordName(recordName.toString());
		figure.setRecordId(record.getId());
		figure.setStorageMode(Tools.getStorageMode(record.getStorageMode()));
		figure.setRecordLength(record.getDataLength());
		figure.setLocationMode(record.getLocationMode().toString());
		if (record.getLocationMode() == LocationMode.CALC) {
			String calcKey = Tools.getCalcKey(record.getCalcKey());
			figure.setLocationModeDetails(calcKey);
			String duplicatesOption = 
				Tools.getDuplicatesOption(record.getCalcKey()
												.getDuplicatesOption());
			figure.setDuplicatesOption(duplicatesOption);
		} else if (record.getLocationMode() == LocationMode.VIA) {
			String setName;
			if (record.getViaSpecification().getSetName().endsWith("_")) {
				// we need to manipulate the via set name in the case of some 
				// dictionary records (DDLCATLOD area, which has the same 
				// structure as DDLDCLOD)...
				setName = record.getViaSpecification()
								.getSetName()
								.substring(0, record.getViaSpecification()
													.getSetName()
													.length() - 1);
			} else {
				setName = record.getViaSpecification().getSetName();
			}
			figure.setLocationModeDetails(setName);
		} else {
			figure.setLocationModeDetails("");
		}
		figure.setAreaName(record.getAreaSpecification().getArea().getName());
	}	
	
}