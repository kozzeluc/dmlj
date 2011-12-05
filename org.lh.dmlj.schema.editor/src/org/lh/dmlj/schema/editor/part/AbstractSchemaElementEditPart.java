package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.DiagramLocationProvider;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;

public abstract class AbstractSchemaElementEditPart<T extends DiagramLocationProvider> 
	extends AbstractGraphicalEditPart implements Adapter, NodeEditPart {

	protected AbstractSchemaElementEditPart(T diagramElement) {
		super();
		setModel(diagramElement);
	}
	
	@Override
	public final void activate() {
		super.activate();
		attachModelChangeListener();
	}
	
	@Override
	protected void createEditPolicies() {		
	}
	
	protected abstract void attachModelChangeListener();
	protected abstract void detachModelChangeListener();

	@Override
	public final void deactivate() {
		detachModelChangeListener();
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
		int featureID = notification.getFeatureID(SchemaRecord.class);
		if (featureID == SchemaPackage.DIAGRAM_LOCATION__X ||
			featureID == SchemaPackage.DIAGRAM_LOCATION__Y) {
			
			refreshVisuals();
		}
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