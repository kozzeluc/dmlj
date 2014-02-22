/**
 * Copyright (C) 2014  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.figure.DiagramLabelFigure;
import org.lh.dmlj.schema.editor.policy.DiagramLabelComponentEditPolicy;
import org.lh.dmlj.schema.editor.preference.PreferenceConstants;

public class DiagramLabelEditPart 
	extends AbstractResizableDiagramNodeEditPart<DiagramLabel> implements IPropertyChangeListener {

	private IPreferenceStore store = Plugin.getDefault().getPreferenceStore();
	
	private DiagramLabelEditPart() {
		super(null, null); // disabled constructor
	}
	
	public DiagramLabelEditPart(DiagramLabel diagramLabel,
								IModelChangeProvider modelChangeProvider) {
		
		super(diagramLabel, modelChangeProvider);		
	}
	
	@Override
	public void activate() {
		super.activate();
		store.addPropertyChangeListener(this);
	}
	
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
		
		super.afterSetFeatures(owner, features);
		
		if (owner == getModel() &&
			isFeatureSet(features, SchemaPackage.eINSTANCE.getDiagramLabel_Description())) {
			
			// the diagram label's description has been set; refresh the edit part's visuals
			refreshVisuals();		
		}				
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
	public double getZoomLevel() {
		return getModel().getDiagramData().getZoomLevel();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.ORGANISATION)) {
			refreshVisuals();
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
