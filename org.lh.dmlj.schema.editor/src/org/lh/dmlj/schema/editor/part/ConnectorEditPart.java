package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.figure.ConnectorFigure;

public class ConnectorEditPart extends AbstractDiagramNodeEditPart<Connector> {

	private ConnectorEditPart() {
		super(null); // disabled constructor
	}
	
	public ConnectorEditPart(Connector connector) {
		super(connector);
	}	

	@Override
	protected IFigure createFigure() {
		return new ConnectorFigure();
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

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return super.getSourceConnectionAnchor(connection);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return super.getSourceConnectionAnchor(request);
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
		return super.getSourceConnectionAnchor(connection);
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return super.getSourceConnectionAnchor(request);
	}

	@Override
	protected void setFigureData() {
		// no data to set ???
	}
	
}