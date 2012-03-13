package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.policy.MoveDiagramNodeEditPolicy;

public class SchemaEditPart 
	extends AbstractGraphicalEditPart implements Adapter {

	@SuppressWarnings("unused")
	private SchemaEditPart() {
		super(); // disabled constructor
	}
	
	public SchemaEditPart(Schema schema) {
		super();
		setModel(schema);
	}
	
	@Override
	public final void activate() {
		super.activate();
		getModel().getDiagramData().eAdapters().add(this);
	}
	
	@Override
	protected void createEditPolicies() {
		// install the edit policy for moving diagram nodes...
		installEditPolicy(EditPolicy.LAYOUT_ROLE, 
						  new MoveDiagramNodeEditPolicy());		
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}
	
	@Override
	public final void deactivate() {		
		getModel().getDiagramData().eAdapters().remove(this);
		super.deactivate();
	}
	
	public Schema getModel() {
		return (Schema) super.getModel();
	}
	
	@Override
	protected List<?> getModelChildren() {
		List<Object> allObjects = new ArrayList<>();
		
		// records
		allObjects.addAll(getModel().getRecords());
		
		// set connection labels
		for (Set set : getModel().getSets()) {
			for (MemberRole memberRole : set.getMembers()) {
				allObjects.add(memberRole.getConnectionLabel());
			}
		}
		
		// set connectors
		for (Set set : getModel().getSets()) {
			for (MemberRole memberRole : set.getMembers()) {
				if (memberRole.getConnectionParts().size() > 1) {
					for (ConnectionPart connectionPart :
						  memberRole.getConnectionParts()) {
						
						allObjects.add(connectionPart.getConnector());
					}
				}
			}
		}
		
		// indexes
		for (Set set : getModel().getSets()) {
			if (set.getMode() == SetMode.INDEXED && 
				set.getSystemOwner() != null) {
				
				allObjects.add(set.getSystemOwner());
			}
		}
		
		return allObjects;
	}

	@Override
	public Notifier getTarget() {
		return null;
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return false;
	}

	@Override
	public void notifyChanged(Notification notification) {
		int featureID = notification.getFeatureID(SchemaRecord.class);
		if (featureID == SchemaPackage.DIAGRAM_DATA__CONNECTORS) {
			// We only act when the second connector is added or removed; the
			// commands responsible for connector creation and deletion should
			// therefore add the connector to or delete it from the 
			// DiagramData's connectors list attribute as their very last
			// activity, thus triggering the adjustment of the diagram 
			// hereunder.
			if (notification.getNewValue() != null &&
				notification.getNewValue() instanceof Connector &&
				getModel().getDiagramData().getConnectors().size() % 2 == 0) {				
				
				// connectors created; the second connector is in the 
				// notification's new value field
				
				MemberRole memberRole = 
					((Connector) notification.getNewValue())
											 .getConnectionPart()
											 .getMemberRole();
				
				// create the 2 connector edit parts
				for (ConnectionPart connectionPart : 
					 memberRole.getConnectionParts()) {
					
					ConnectorEditPart connectorEditPart =
						new ConnectorEditPart(connectionPart.getConnector());
					addChild(connectorEditPart, getChildren().size());					
				}
				
				// refresh the owner edit part
				GraphicalEditPart ownerEditPart;
				if (memberRole.getSet().getOwner() != null) {
					// user owned set
					SchemaRecord ownerRecord = 
						memberRole.getSet().getOwner().getRecord();
					ownerEditPart = 
						(GraphicalEditPart) getViewer().getEditPartRegistry()
					  		   						   .get(ownerRecord);
				} else {
					// system owned set
					SystemOwner systemOwner = 
						memberRole.getSet().getSystemOwner();
					ownerEditPart = 
						(GraphicalEditPart) getViewer().getEditPartRegistry()
							   						   .get(systemOwner);
				}
				ownerEditPart.refresh();
				
				// refresh the member record edit part
				SchemaRecord memberRecord = memberRole.getRecord();
				GraphicalEditPart memberRecordEditPart = 
					(GraphicalEditPart) getViewer().getEditPartRegistry()
										  		   .get(memberRecord);
				memberRecordEditPart.refresh();
			} else if (notification.getOldValue() != null &&
					   notification.getOldValue() instanceof Connector &&
					   getModel().getDiagramData().getConnectors().size() % 2 == 0) {				
					
				// Connectors deleted; the second connector is in the 
				// notification's old value field, still refers to the set's
				// second ConnectionPart, but the reference to the MemberRole in
				// that ConnectionPart is nullified (because the ConnectionPart
				// is no longer part of the model).  We can use the memberRole
				// field in the connector and set edit parts to remove 
				// everything that needs to be removed (both connectors and the
				// connection from the second connector to the member record).
				
				Connector connector = (Connector) notification.getOldValue();				
				GraphicalEditPart editPart = 
					(GraphicalEditPart) getViewer().getEditPartRegistry()
										  		   .get(connector);
				ConnectorEditPart connectorEditPart = 
					(ConnectorEditPart) editPart; // second connector edit part
				MemberRole memberRole = connectorEditPart.getMemberRole();
				List<?> connections = connectorEditPart.getSourceConnections();
				for (Object connection : connections) {
					// only 1 connection is expected to be present
					removeChild((SetEditPart)connection); // second connection part
				}
				removeChild(connectorEditPart);
				for (Object child : getChildren()) {
					if (child instanceof ConnectorEditPart) {
						connectorEditPart = (ConnectorEditPart) child;
						if (connectorEditPart.getMemberRole() == memberRole) {
							removeChild(connectorEditPart); // first connector 
							break;						    // edit part
						}
					}
				}

				// refresh the owner edit part
				GraphicalEditPart ownerEditPart;
				if (memberRole.getSet().getOwner() != null) {
					// user owned set
					SchemaRecord ownerRecord = 
						memberRole.getSet().getOwner().getRecord();
					ownerEditPart = 
						(GraphicalEditPart) getViewer().getEditPartRegistry()
					  		   						   .get(ownerRecord);
				} else {
					// system owned set
					SystemOwner systemOwner = 
						memberRole.getSet().getSystemOwner();
					ownerEditPart = 
						(GraphicalEditPart) getViewer().getEditPartRegistry()
							   						   .get(systemOwner);
				}
				ownerEditPart.refresh();
				
				// refresh the member record edit part
				SchemaRecord memberRecord = memberRole.getRecord();
				GraphicalEditPart memberRecordEditPart = 
					(GraphicalEditPart) getViewer().getEditPartRegistry()
										  		   .get(memberRecord);
				memberRecordEditPart.refresh();				
			}
		}
	}

	@Override
	public void setTarget(Notifier newTarget) {				
	}
	
}