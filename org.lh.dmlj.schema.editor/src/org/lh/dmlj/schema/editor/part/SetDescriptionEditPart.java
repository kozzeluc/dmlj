package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.editor.Plugin;

public class SetDescriptionEditPart 
	extends AbstractDiagramElementEditPart<DiagramLabel>  {

	private SetDescriptionEditPart() {
		super(null); // disabled constructor
	}
	
	public SetDescriptionEditPart(DiagramLabel setDescription) {
		super(setDescription);		
	}	

	@Override
	protected IFigure createFigure() {
		Label label = new Label();
		label.setFont(Plugin.getDefault().getFont());
		return label;
	}
	
	@Override
	public void showSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			SetEditPart setEditPart = 
				(SetEditPart) getViewer().getEditPartRegistry().get(getModel().getMemberRole());
			PolylineConnection connection = (PolylineConnection) setEditPart.getFigure();
			connection.setLineWidth(2);
		}
		super.showSourceFeedback(request);
	}
	
	@Override
	public void eraseSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			SetEditPart setEditPart = 
				(SetEditPart) getViewer().getEditPartRegistry().get(getModel().getMemberRole());
			PolylineConnection connection = (PolylineConnection) setEditPart.getFigure();
			connection.setLineWidth(1);
		}
		super.eraseSourceFeedback(request);
	}

	@Override
	protected void setFigureData() {		
		Label label = (Label) getFigure();		
		label.setText(getModel().getValue());		
	}

}