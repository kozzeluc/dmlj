package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.DeleteConnectorsCommand;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

public class ConnectorEditPart extends AbstractDiagramNodeEditPart<Connector> {

	private MemberRole memberRole;
	
	private ConnectorEditPart() {
		super(null); // disabled constructor
	}
	
	public ConnectorEditPart(Connector connector) {
		super(connector);
		// keep track of the MemberRole because if connectors are deleted, the 
		// reference to that MemberRole will be nullified in the ConnectionPart
		this.memberRole = connector.getConnectionPart().getMemberRole();
	}	

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
			new ComponentEditPolicy() {
			
			@Override
			protected Command createDeleteCommand(GroupRequest deleteRequest) {
				List<?> editParts = deleteRequest.getEditParts(); 
				if (editParts.size() != 1 ||
					!(editParts.get(0) instanceof ConnectorEditPart)) {
					
					return null;
				}
				Connector connector = 
					((ConnectorEditPart)editParts.get(0)).getModel();
				MemberRole memberRole = 
					connector.getConnectionPart().getMemberRole();
				return new DeleteConnectorsCommand(memberRole);				
			}
		});
	}
	
	@Override
	protected IFigure createFigure() {
		return new ConnectorFigure();
	}		

	public MemberRole getMemberRole() {
		return memberRole;
	}
	
	@Override
	protected EObject[] getModelObjects() {
		return new EObject[] {getModel(), 
							  getModel().getDiagramLocation()};
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<ConnectionPart> getModelSourceConnections() {
		MemberRole memberRole = getModel().getConnectionPart().getMemberRole();
		if (getModel().getConnectionPart() == memberRole.getConnectionParts().get(1)) {
			List<ConnectionPart> connectionParts = new ArrayList<>();
			connectionParts.add(getModel().getConnectionPart());
			return connectionParts;
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<ConnectionPart> getModelTargetConnections() {
		MemberRole memberRole = getModel().getConnectionPart().getMemberRole();
		if (getModel().getConnectionPart() == memberRole.getConnectionParts().get(0)) {
			List<ConnectionPart> connectionParts = new ArrayList<>();
			connectionParts.add(getModel().getConnectionPart());
			return connectionParts;
		} else {
			return Collections.EMPTY_LIST;
		}		
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new EllipseAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new EllipseAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new EllipseAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new EllipseAnchor(getFigure());
	}

	@Override
	protected void setFigureData() {
		Connector connector = getModel();
		ConnectorFigure figure = (ConnectorFigure) getFigure();
		figure.setLabel(connector.getLabel());
	}
	
}