/**
 * Copyright (C) 2013  Luc Hermans
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaPackage;


/**
 * A superclass for all diagram nodes, whether resizable or not.
 * @author Luc Hermans
 *
 * @param <T> the model type
 */
public abstract class AbstractDiagramNodeEditPart<T extends DiagramNode> 
	extends AbstractGraphicalEditPart implements Adapter, NodeEditPart {
	
	private List<EObject> modelObjects = new ArrayList<>();

	protected AbstractDiagramNodeEditPart(T diagramElement) {
		super();
		setModel(diagramElement);
	}
	
	@Override
	public void activate() {
		super.activate();
		modelObjects.clear();
		for (EObject modelObject : getModelObjects()) {
			modelObject.eAdapters().add(this);
			modelObjects.add(modelObject);
		}
	}
	
	protected void addModelObject(EObject modelObject) {
		if (!modelObjects.contains(modelObject)) {
			modelObject.eAdapters().add(this);
			modelObjects.add(modelObject);
		}
	}
	
	protected void adjustModelObjects(Notification notification,
									  List<EObject> modelObjects) {				
	}

	/**
	 * To be implemented by subclasses
	 * @return a fixed list with the model objects upon which the figure data
	 *         are dependent
	 */
	protected abstract EObject[] getModelObjects();

	@Override
	protected void createEditPolicies() {		
	}	

	@Override
	public void deactivate() {
		for (EObject modelObject : modelObjects) {
			modelObject.eAdapters().remove(this);
		}
		super.deactivate();
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
	public final Notifier getTarget() {
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}
	
	@Override
	public final boolean isAdapterForType(Object type) {
		return false;
	}

	@Override
	public final void notifyChanged(Notification notification) {
		
		adjustModelObjects(notification, new ArrayList<>(modelObjects));
		
		int featureID = notification.getFeatureID(DiagramNode.class);
		
		if (featureID != Notification.NO_FEATURE_ID) {
			// we only refresh when a feature has been changed (to avoid NPEs)
			refreshVisuals();
		}
		
		if (featureID == SchemaPackage.DIAGRAM_LOCATION__X ||
			featureID == SchemaPackage.DIAGRAM_LOCATION__Y) {
			
			// allow connections to be refreshed as well since the bendpoint
			// coordinates are stored as relative to the owner figure and moving
			// the owner figure does not automatically trigger a refresh of any
			// involved connection's visuals...
			refreshConnections();
		}
		
	}	
	
	protected void refreshConnections() {
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
	
	protected void removeModelObject(EObject modelObject) {
		if (modelObjects.contains(modelObject)) {
			modelObject.eAdapters().remove(this);
			modelObjects.remove(modelObject);
		}
	}

	protected abstract void setFigureData();

	@Override
	public final void setTarget(Notifier newTarget) {				
	}
	
}
