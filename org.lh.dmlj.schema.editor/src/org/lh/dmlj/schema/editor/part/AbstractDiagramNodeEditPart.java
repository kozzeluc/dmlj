package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.SchemaPackage;

public abstract class AbstractDiagramNodeEditPart<T extends DiagramNode> 
	extends AbstractGraphicalEditPart implements Adapter, NodeEditPart {
	
	private EObject[] modelObjects;

	protected AbstractDiagramNodeEditPart(T diagramElement) {
		super();
		setModel(diagramElement);
	}
	
	@Override
	public final void activate() {
		super.activate();
		EObject[] tmpModelObjects = getModelObjects();
		modelObjects = new EObject[tmpModelObjects.length];
		System.arraycopy(tmpModelObjects, 0, modelObjects, 0, 
						 modelObjects.length);
		for (int i = 0; i < modelObjects.length; i++) {
			modelObjects[i].eAdapters().add(this);
		}
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
	public final void deactivate() {
		for (int i = modelObjects.length - 1; i >= 0; i--) {
			modelObjects[i].eAdapters().remove(this);
		}
		super.deactivate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T getModel() {
		return (T) super.getModel();
	}
	
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
		refreshVisuals();
		int featureID = notification.getFeatureID(DiagramNode.class);
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

	protected abstract void setFigureData();

	@Override
	public final void setTarget(Notifier newTarget) {				
	}
	
}