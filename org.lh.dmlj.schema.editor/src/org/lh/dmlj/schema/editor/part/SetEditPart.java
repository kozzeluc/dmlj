package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.lh.dmlj.schema.Set;

public class SetEditPart extends AbstractConnectionEditPart {

	public SetEditPart(Set set) {
		super();
		setModel(set);
	}
	
	@Override
	protected void createEditPolicies() {
	}
	
	@Override
	protected IFigure createFigure() {
		
		PolylineConnection connection = new PolylineConnection();
		
		Set set = getModel();
		if (set.getOwner() != null) { 
			// user owned set (chained or indexed), make sure an arrow is drawn
			// at the target end...
			PolylineDecoration decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
			connection.setTargetDecoration(decoration);
		}
		
		connection.setLineWidth(1);				
		
		return connection;		
	}
	
	public Set getModel() {
		return (Set) super.getModel();
	}
	
}