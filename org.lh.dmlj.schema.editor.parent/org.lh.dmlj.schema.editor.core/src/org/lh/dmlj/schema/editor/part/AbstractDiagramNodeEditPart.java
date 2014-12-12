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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.ResizableDiagramNode;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;


/**
 * A superclass for all diagram nodes, whether resizable or not.
 * @author Luc Hermans
 *
 * @param <T> the model type
 */
public abstract class AbstractDiagramNodeEditPart<T extends DiagramNode> 
	extends AbstractGraphicalEditPart implements IModelChangeListener, NodeEditPart {
	
	private IModelChangeProvider modelChangeProvider;
	
	/**
	 * Checks whether the feature of interest is set in a grouped model change.
	 * @param setFeatures the features set in the model change
	 * @param featureOfInterest the feature of interest
	 * @return true if the feature of interest is contained in the list of set features, false if 
	 * 		   not
	 */
	protected static final boolean isFeatureSet(EStructuralFeature[] setFeatures, 
								   		  		EStructuralFeature featureOfInterest) {
		
		for (EStructuralFeature feature : setFeatures) {
			if (feature == featureOfInterest) {
				return true;
			}
		}
		return false;
	}	
	
	protected AbstractDiagramNodeEditPart(T diagramNode, IModelChangeProvider modelChangeProvider) {		
		super();
		setModel(diagramNode);
		this.modelChangeProvider = modelChangeProvider;
		modelChangeProvider.addModelChangeListener(this);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		modelChangeProvider.addModelChangeListener(this);		
	}

	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
	}

	@Override
	public void afterModelChange(ModelChangeContext context) {		
	}
	
	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {		
	}

	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {		
	}

	/**
	 * Subclasses that override this method should make sure they call this implementation as well
	 * since it takes care of the diagram location and dimension changes.
	 * @see org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener#afterSetFeatures(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature[])
	 */
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
		if (owner == getModel().getDiagramLocation() &&
			(isFeatureSet(features, SchemaPackage.eINSTANCE.getDiagramLocation_X()) ||
			 isFeatureSet(features, SchemaPackage.eINSTANCE.getDiagramLocation_Y()))) {				
				
			// the diagram location of the model has changed; refresh the visuals so that the 
			// diagram node appears at its new location in the diagram
			refreshVisuals();
			
			// allow connections to be refreshed as well since the bendpoint coordinates are stored  
			// as as relative to the owner figure and moving the owner figure does not automatically 
			// trigger a refresh of any involved connection's visuals...
			refreshConnections();
		
		} else if (owner == getModel() && owner instanceof ResizableDiagramNode &&
				   (isFeatureSet(features, SchemaPackage.eINSTANCE.getResizableDiagramNode_Width()) ||
					isFeatureSet(features, SchemaPackage.eINSTANCE.getResizableDiagramNode_Height()))) {
		
			// the dimensions (size) of the model has changed; refresh the visuals so that the 
			// diagram node appears with its new dimensions in the diagram
			refreshVisuals();
			
			// allow connections to be refreshed as well since the bendpoint coordinates are stored  
			// as as relative to the owner figure and resizing the owner figure does not  
			// automatically trigger a refresh of any involved connection's visuals...
			refreshConnections();			
			
		}		
	}	
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {		
	}

	@Override
	protected void createEditPolicies() {		
	}	

	@Override
	@SuppressWarnings("unchecked")
	public final T getModel() {
		return (T) super.getModel();
	}
	
	/**
	 * @return an EditPolicy to be installed as the EditPolicy.PRIMARY_DRAG_ROLE, probably a
	 * ModifiedNonResizableEditPolicy or a ModifiedResizableEditPolicy
	 */
	public abstract EditPolicy getResizeEditPolicy();

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}	
	
	protected void refreshConnections() {
	}

	@Override
	public void removeNotify() {
		// note: this method is NOT invoked when the editor is closed (i.e. when the viewer is 
		//disposed)
		modelChangeProvider.removeModelChangeListener(this);
		super.removeNotify();
	}

	@Override
	protected final void refreshVisuals() {
		setFigureData();
		Dimension size = getFigure().getPreferredSize();
		Rectangle bounds = new Rectangle(getModel().getDiagramLocation().getX(), 
										 getModel().getDiagramLocation().getY(), 
						  				 size.width, size.height);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
															  getFigure(), 
															  bounds);
	}	

	protected abstract void setFigureData();
	
}
