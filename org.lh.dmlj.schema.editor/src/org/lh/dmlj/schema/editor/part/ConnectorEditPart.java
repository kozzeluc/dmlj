package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.anchor.ConnectorAnchor;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;
import org.lh.dmlj.schema.editor.policy.ConnectorComponentEditPolicy;

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
		// make sure we can delete connectors:
		installEditPolicy(EditPolicy.COMPONENT_ROLE, 
						  new ConnectorComponentEditPolicy());
	}
	
	@Override
	protected IFigure createFigure() {
		Figure figure = new ConnectorFigure();
		
		// add a tooltip containing the set's name...
        String adjustedSetName;
        Set set = getModel().getConnectionPart().getMemberRole().getSet();
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
	protected void setFigureData() {
		Connector connector = getModel();
		ConnectorFigure figure = (ConnectorFigure) getFigure();
		figure.setLabel(connector.getLabel());
	}
	
}