package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.anchor.IndexSourceAnchor;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

public class IndexEditPart 
	extends AbstractDiagramElementEditPart implements NodeEditPart {

	@SuppressWarnings("unused")
	private IndexEditPart() {
		super(); // disabled constructor
	}
	
	public IndexEditPart(SystemOwner systemOwner) {
		super();
		setModel(systemOwner);
	}
	
	@Override
	protected void createEditPolicies() {
	}

	@Override
	protected IFigure createFigure() {
		return new IndexFigure();
	}
	
	public SystemOwner getModel() {
		return (SystemOwner) super.getModel();
	}
	
	@Override
	protected List<Set> getModelSourceConnections() {
		
		List<Set> sets = new ArrayList<>();
		
		sets.add(getModel().getSet());
		
		return sets;
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
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}
	
}