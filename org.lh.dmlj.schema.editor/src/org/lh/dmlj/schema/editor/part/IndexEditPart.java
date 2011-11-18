package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

public class IndexEditPart extends AbstractDiagramElementEditPart {

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
	
}