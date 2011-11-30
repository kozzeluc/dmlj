package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.anchor.IndexSourceAnchor;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

public class IndexEditPart extends AbstractDiagramElementEditPart<SystemOwner> {

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
	protected List<MemberRole> getModelSourceConnections() {
		List<MemberRole> memberRoles = new ArrayList<>();
		memberRoles.add(getModel().getSet().getMembers().get(0));
		return memberRoles;
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
	protected void setFigureData() {
		//no data to set
	}
	
}