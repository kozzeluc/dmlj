package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.figure.DiagramLabelFigure;
import org.lh.dmlj.schema.editor.policy.DiagramLabelComponentEditPolicy;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class DiagramLabelEditPart 
	extends AbstractResizableDiagramNodeEditPart<DiagramLabel> implements IPropertyChangeListener {

	private IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
	
	private DiagramLabelEditPart() {
		super(null); // disabled constructor
	}
	
	public DiagramLabelEditPart(DiagramLabel diagramLabel) {
		super(diagramLabel);		
	}
	
	@Override
	public void activate() {
		super.activate();
		store.addPropertyChangeListener(this);
	}
	
	@Override
	protected void createEditPolicies() {		
	
		// the next edit policy allows for the deletion of a (the) diagram label
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DiagramLabelComponentEditPolicy());
		
	}
	
	@Override
	protected IFigure createFigure() {
		IFigure figure = new DiagramLabelFigure();
		Label tooltip = new Label("Diagram label");
        figure.setToolTip(tooltip);
        return figure;
	}
	
	@Override
	public void deactivate() {
		store.removePropertyChangeListener(this);
		super.deactivate();
	}

	@Override
	protected EObject[] getModelObjects() {
		return new EObject[] {getModel(), getModel().getDiagramLocation()};
	}

	@Override
	public double getZoomLevel() {
		return getModel().getDiagramData().getZoomLevel();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.ORGANISATION)) {
			refresh();
		}
	}

	@Override
	protected void setFigureData() {
		DiagramLabel diagramLabel = getModel();
		Schema schema = diagramLabel.getDiagramData().getSchema();
		DiagramLabelFigure figure = (DiagramLabelFigure) getFigure();
		figure.setOrganisation(store.getString(PreferenceConstants.ORGANISATION));
		figure.setSchemaIdentification(schema.getName(), schema.getVersion());
		figure.setDescription(diagramLabel.getDescription());
		figure.setPreferredSize(diagramLabel.getWidth(), diagramLabel.getHeight());
	}

}