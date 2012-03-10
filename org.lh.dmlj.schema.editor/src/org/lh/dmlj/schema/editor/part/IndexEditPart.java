package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.anchor.IndexSourceAnchor;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

public class IndexEditPart extends AbstractDiagramNodeEditPart<SystemOwner> {

	private IndexEditPart() {
		super(null); // disabled constructor
	}
	
	public IndexEditPart(SystemOwner systemOwner) {
		super(systemOwner);
	}	

	@Override
	protected IFigure createFigure() {
		return new IndexFigure();
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
		connectionParts.addAll(memberRole.getConnectionParts());
		return connectionParts;
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