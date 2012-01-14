package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.model.SetLabel;

public class SetLabelEditPart 
    extends AbstractDiagramNodeEditPart<SetLabel>  {

	private SetLabelEditPart() {
		super(null); // disabled constructor
	}
	
	public SetLabelEditPart(SetLabel setLabel) {
		super(setLabel);		
	}	

	@Override
	protected IFigure createFigure() {
		Label label = new Label();
		label.setFont(Plugin.getDefault().getFont());
		return label;
	}
	
	@Override
	protected EObject[] getModelObjects() {
		return new EObject[] {getModel().getMemberRole(), 
							  getModel().getDiagramLocation()};
	}

	@Override
	public void showSourceFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			SetEditPart setEditPart = 
				(SetEditPart) getViewer().getEditPartRegistry().get(getModel().getMemberRole());
			PolylineConnection connection = (PolylineConnection) setEditPart.getFigure();
			connection.setLineWidth(2);
			connection.setForegroundColor(ColorConstants.red);
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
			connection.setForegroundColor(ColorConstants.black);
		}
		super.eraseSourceFeedback(request);
	}

	@Override
	protected void setFigureData() {		
		Label label = (Label) getFigure();		
		label.setText(getModel().getValue());		
	}

}