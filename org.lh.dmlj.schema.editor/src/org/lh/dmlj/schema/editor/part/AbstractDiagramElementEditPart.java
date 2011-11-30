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
import org.lh.dmlj.schema.DiagramElement;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;

public abstract class AbstractDiagramElementEditPart<T extends DiagramElement> 
	extends AbstractGraphicalEditPart implements Adapter, NodeEditPart {

	protected AbstractDiagramElementEditPart(T diagramElement) {
		super();
		setModel(diagramElement);
	}
	
	@Override
	public final void activate() {
		super.activate();
		getModel().eAdapters().add(this);
	}
	
	@Override
	protected void createEditPolicies() {		
	}

	@Override
	public final void deactivate() {
		getModel().eAdapters().remove(this);
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
		if (featureID == SchemaPackage.DIAGRAM_ELEMENT__X ||
			featureID == SchemaPackage.DIAGRAM_ELEMENT__Y) {
			
			refreshVisuals();
		}
	}

	@Override
	protected final void refreshVisuals() {
		setFigureData();
		DiagramElement m = (DiagramElement) getModel();		
		Dimension size = getFigure().getPreferredSize();
		Rectangle bounds = 
			new Rectangle(m.getX(), m.getY(), size.width, size.height);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
															  getFigure(), 
															  bounds);
	}

	protected abstract void setFigureData();

	@Override
	public final void setTarget(Notifier newTarget) {				
	}
	
}