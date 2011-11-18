package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.DiagramElement;

public abstract class AbstractDiagramElementEditPart 
	extends AbstractGraphicalEditPart {

	public AbstractDiagramElementEditPart() {
		super();
	}

	@Override
	protected void refreshVisuals() {
		
		DiagramElement m = (DiagramElement) getModel();
		
		//System.out.println(m.getX() + " " + m.getY());
		
		Dimension size = getFigure().getPreferredSize();
		Rectangle bounds = 
			new Rectangle(m.getX(), m.getY(), size.width, size.height);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
															  getFigure(), 
															  bounds);
		super.refreshVisuals();
	}
	
}