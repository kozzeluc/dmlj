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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.policy.SchemaXYLayoutEditPolicy;

public class SchemaEditPart 
	extends AbstractGraphicalEditPart implements IModelChangeListener {
	
	private IModelChangeProvider modelChangeProvider;

	@SuppressWarnings("unused")
	private SchemaEditPart() {
		super(); // disabled constructor
	}
	
	public SchemaEditPart(Schema schema, IModelChangeProvider modelChangeProvider) {
		super();
		setModel(schema);
		this.modelChangeProvider = modelChangeProvider;
	}
	
	@Override
	public final void activate() {
		super.activate();
		modelChangeProvider.addModelChangeListener(this);
	}
	
	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
		
		if (owner == getModel().getDiagramData() && 
			reference == SchemaPackage.eINSTANCE.getDiagramData_Label()) {
			
			// a DIAGRAM LABEL was added
			EditPart child = SchemaDiagramEditPartFactory.createEditPart(item, modelChangeProvider);
			addChild(child, getChildren().size());			
		
		} else if (owner == getModel() && 
				   reference == SchemaPackage.eINSTANCE.getSchema_Records()) {
			
			// a RECORD was added
			SchemaRecord record = (SchemaRecord) item;			
			EditPart child = SchemaDiagramEditPartFactory.createEditPart(record, modelChangeProvider);
			addChild(child, getChildren().size());
		
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {			
			
			// a SET was added
			Set set = (Set) item;
			
			// create an edit part for the SYSTEM OWNER or refresh the owner record edit part
			if (set.getSystemOwner() != null) {	
				// create an edit part for the SYSTEM OWNER and add it as a child
				EObject model = set.getSystemOwner();
				EditPart child = 
					SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider);			
				addChild(child, getChildren().size());
			} else {					
				// refresh the owner record edit part
				SchemaRecord ownerRecord = set.getOwner().getRecord();
				GraphicalEditPart memberRecordEditPart = 
					(GraphicalEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
				memberRecordEditPart.refresh();
			}
			
			// refresh the member record edit part
			SchemaRecord memberRecord = set.getMembers().get(0).getRecord();
			GraphicalEditPart memberRecordEditPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry().get(memberRecord);
			memberRecordEditPart.refresh();
			
			// create the set label edit part and add it as a child
			ConnectionLabel connectionLabel = set.getMembers().get(0).getConnectionLabel();
			EditPart setDescriptionEditPart = 
				SchemaDiagramEditPartFactory.createEditPart(connectionLabel, modelChangeProvider);
			addChild(setDescriptionEditPart, getChildren().size());
			
		} else if (owner instanceof MemberRole && 
				   reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts()) {			
		
			// connectors created
			
			MemberRole memberRole = (MemberRole) owner;
			
			// create and add the 2 connector edit parts
			for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
				Connector model = connectionPart.getConnector();
				EditPart connectorEditPart =
					SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider);
				addChild(connectorEditPart, getChildren().size());					
			}
			
			// find and refresh the owner edit part
			GraphicalEditPart ownerEditPart;
			if (memberRole.getSet().getOwner() != null) {
				// user owned set
				SchemaRecord ownerRecord = memberRole.getSet().getOwner().getRecord();
				ownerEditPart = (GraphicalEditPart) getViewer().getEditPartRegistry()
															   .get(ownerRecord);
			} else {
				// system owned set
				SystemOwner systemOwner = memberRole.getSet().getSystemOwner();
				ownerEditPart = (GraphicalEditPart) getViewer().getEditPartRegistry()
						   						   			   .get(systemOwner);
			}
			ownerEditPart.refresh();
			
			// find and refresh the member record edit part
			SchemaRecord memberRecord = memberRole.getRecord();
			GraphicalEditPart memberRecordEditPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry()
									  		   .get(memberRecord);
			memberRecordEditPart.refresh();			
			
		}		
		
	}

	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {		
	}

	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
		
		if (owner == getModel().getDiagramData() && 
			reference == SchemaPackage.eINSTANCE.getDiagramData_Label()) {
			
			// the diagram label was removed
			EditPart child = (EditPart) getViewer().getEditPartRegistry().get(item);
			removeChild(child);
		
		} else if (owner == getModel() && 
				   reference == SchemaPackage.eINSTANCE.getSchema_Records()) {
			
			// a record was removed
			EditPart child = (EditPart) getViewer().getEditPartRegistry().get(item);
			removeChild(child);				
			
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {			
			
			// a set was removed
			Set set = (Set) item;
			
			// remove the SYSTEM OWNER's edit part if applicable			
			if (set.getSystemOwner() != null) {				
				EObject model = set.getSystemOwner();
				EditPart child = (EditPart) getViewer().getEditPartRegistry().get(model);
				removeChild(child);
			}
			
			// gather the set label edit part(s) as well as all (owner and member) record edit parts
			// involved 
			List<EditPart> setDescriptionEditParts = new ArrayList<>();	
			List<EditPart> recordEditParts = new ArrayList<>();	
			for (Object object : getViewer().getEditPartRegistry().entrySet()) {
				Entry<?, ?> entry = (Entry<?, ?>) object;																
				if (entry.getKey() instanceof ConnectionLabel) {
					// set label edit part...
					ConnectionLabel connectionLabel = (ConnectionLabel) entry.getKey();
					if (connectionLabel.getMemberRole().getSet() == set) {
						// ... that is involved
						setDescriptionEditParts.add((EditPart) entry.getValue());
					}
				} else if (entry.getKey() instanceof ConnectionPart) {
					// set connection edit part...					
					ConnectionPart connectionPart = (ConnectionPart) entry.getKey();
					if (connectionPart.getMemberRole().getSet() == set) {
						// ... that is involved
						SetEditPart setEditPart = (SetEditPart) entry.getValue();
						if (setEditPart.getSource() != null) {
							// owner record
							Assert.isTrue(setEditPart.getSource() instanceof RecordEditPart);
							recordEditParts.add(setEditPart.getSource());
						}						
						// member record
						Assert.isTrue(setEditPart.getTarget() instanceof RecordEditPart);
						recordEditParts.add(setEditPart.getTarget());
					}
				}
			}
			
			// remove the set label edit part(s) involved
			Assert.isTrue(!setDescriptionEditParts.isEmpty(), 
						  "expected at least 1 SetDescriptionEditPart");			
			for (EditPart setDescriptionEditPart : setDescriptionEditParts) {
				removeChild(setDescriptionEditPart);
			}
			
			// refresh the record edit part(s) involved
			Assert.isTrue(!recordEditParts.isEmpty(), "expected at least 1 RecordEditPart");
			for (EditPart editPart : recordEditParts) {
				editPart.refresh();				
			}
		
		} else if (owner instanceof MemberRole && 
				   reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts()) {			
		
			// connectors removed
			
			MemberRole memberRole = (MemberRole) owner;
			Assert.isTrue(memberRole.getConnectionParts().size() == 1);
			
			// find and remove both connector edit parts
			List<ConnectionPart> connectionParts = new ArrayList<>();
			connectionParts.add(memberRole.getConnectionParts().get(0));
			connectionParts.add((ConnectionPart) item); // the removed connection part
			List<ConnectorEditPart> connectorEditParts = new ArrayList<>();
			for (Object object : getViewer().getEditPartRegistry().entrySet()) {
				Entry<?, ?> entry = (Entry<?, ?>) object;																
				if (entry.getKey() instanceof ConnectionPart) {
					// set connection edit part...					
					ConnectionPart connectionPart = (ConnectionPart) entry.getKey();
					if (connectionParts.indexOf(connectionPart) > -1) {
						// ... that is involved
						SetEditPart setEditPart = (SetEditPart) entry.getValue();
						Assert.isTrue(setEditPart.getSource() instanceof ConnectorEditPart ||
									  setEditPart.getTarget() instanceof ConnectorEditPart);
						if (setEditPart.getSource() instanceof ConnectorEditPart) {
							// second connector edit part
							connectorEditParts.add((ConnectorEditPart) setEditPart.getSource());
						} else {
							// first connector edit part
							connectorEditParts.add((ConnectorEditPart) setEditPart.getTarget());
						}						
					}
				}
			}
			Assert.isTrue(connectorEditParts.size() == 2);
			for (EditPart editPart : connectorEditParts) {
				removeChild(editPart);
			}

			// refresh the owner edit part
			GraphicalEditPart ownerEditPart;
			if (memberRole.getSet().getOwner() != null) {
				// user owned set
				SchemaRecord ownerRecord = memberRole.getSet().getOwner().getRecord();
				ownerEditPart = (GraphicalEditPart) getViewer().getEditPartRegistry()
				  		   						   			   .get(ownerRecord);
			} else {
				// system owned set
				SystemOwner systemOwner = memberRole.getSet().getSystemOwner();
				ownerEditPart = (GraphicalEditPart) getViewer().getEditPartRegistry()
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

	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {		
	}

	@Override
	protected void createEditPolicies() {
		
		// install the edit policy for creating, moving and resizing diagram nodes...
		installEditPolicy(EditPolicy.LAYOUT_ROLE, 
						  new SchemaXYLayoutEditPolicy(getModel()));
		
		// install the snap feedback policy...
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
		  
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
		modelChangeProvider.removeModelChangeListener(this);
		super.deactivate();
	}
	
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
	    if (adapter == SnapToHelper.class) {
	        // make sure we can snap figures to the grid, guides and geometry
	        List<Object> snapStrategies = new ArrayList<>();
	        if (getModel().getDiagramData().isShowRulers() &&
	        	getModel().getDiagramData().isSnapToGuides()) {
	            
	        	snapStrategies.add(new SnapToGuides(this));
	        }
	        if (getModel().getDiagramData().isSnapToGeometry()) {
	            snapStrategies.add(new SnapToGeometry(this));
	        }	        
	        if (getModel().getDiagramData().isShowGrid() &&
	        	getModel().getDiagramData().isSnapToGrid()) {
	        	
	            snapStrategies.add(new SnapToGrid(this));
	        }
	        if (snapStrategies.isEmpty()) {
	            return null;
	        }
	        if (snapStrategies.size() == 1) {
	            return snapStrategies.get(0);
	        }
	  			
	        SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
	        ss = snapStrategies.toArray(ss);			
	  			
	        return new CompoundSnapToHelper(ss);
	    }
	    return super.getAdapter(adapter);
	}	
	
	public Schema getModel() {
		return (Schema) super.getModel();
	}
	
	@Override
	protected List<?> getModelChildren() {
		List<Object> allObjects = new ArrayList<>();
		
		// diagram label (optional)
		if (getModel().getDiagramData().getLabel() != null) {
			allObjects.add(getModel().getDiagramData().getLabel());
		}
		
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
	
}
