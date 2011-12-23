package org.lh.dmlj.schema.editor.anchor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.lh.dmlj.schema.MemberRole;

public class LockedRecordSourceAnchor extends AbstractLockedRecordAnchor {

	public LockedRecordSourceAnchor(IFigure figure, MemberRole memberRole) {
		super(figure, memberRole, true);
	}
	
	@Override
	protected Point getDefaultLocation(Point reference) {
		return super.getChopboxLocation(reference);
	}
	
}