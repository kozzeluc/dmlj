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
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeListener;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.policy.SchemaXYLayoutEditPolicy;

public class SchemaEditPart 
	extends AbstractGraphicalEditPart implements IModelChangeListener {
	
	private IModelChangeProvider modelChangeProvider;
	private SchemaEditor schemaEditor;

	@SuppressWarnings("unused")
	private SchemaEditPart() {
		super(); // disabled constructor
	}
	
	public SchemaEditPart(Schema schema, SchemaEditor schemaEditor) {
		super();
		setModel(schema);
		this.schemaEditor = schemaEditor;
		modelChangeProvider = 
			(IModelChangeProvider) schemaEditor.getAdapter(IModelChangeProvider.class);
	}
	
	@Override
	public final void addNotify() {
		super.addNotify();
		modelChangeProvider.addModelChangeListener(this);
	}

	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
		
		/*if (owner == getModel().getDiagramData() && 
			reference == SchemaPackage.eINSTANCE.getDiagramData_Label()) {
			
			// a DIAGRAM LABEL was added
			EditPart child = 
				SchemaDiagramEditPartFactory.createEditPart(item, modelChangeProvider, schemaEditor);
			addChild(child, getChildren().size());			
		
		} else if (owner == getModel() && 
				   reference == SchemaPackage.eINSTANCE.getSchema_Records()) {
			
			// a RECORD was added --> MIGRATED
		
		} else if (owner == getModel() && reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {			
			
			// a SET was added
			Set set = (Set) item;
			
			// create an edit part for the SYSTEM OWNER or refresh the owner record edit part
			if (set.getSystemOwner() != null) {	
				// create an edit part for the SYSTEM OWNER and add it as a child
				EObject model = set.getSystemOwner();
				EditPart child = 
					SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider, 
																schemaEditor);			
				addChild(child, getChildren().size());
			} else {					
				// refresh the owner record edit part
				SchemaRecord ownerRecord = set.getOwner().getRecord();
				GraphicalEditPart ownerRecordEditPart = 
					(GraphicalEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
				ownerRecordEditPart.refresh();
			}
			
			// refresh the member record edit part (take the last member because we might be undoing
			// the deletion of a multiple-member set)
			SchemaRecord memberRecord = 
				set.getMembers().get(set.getMembers().size() - 1).getRecord();
			GraphicalEditPart memberRecordEditPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry().get(memberRecord);
			memberRecordEditPart.refresh();
			
			// create the set label edit part and add it as a child (take the last member because we 
			// might be undoing the deletion of a multiple-member set)
			ConnectionLabel connectionLabel = 
				set.getMembers().get(set.getMembers().size() - 1).getConnectionLabel();
			EditPart setDescriptionEditPart = 
				SchemaDiagramEditPartFactory.createEditPart(connectionLabel, modelChangeProvider,
															schemaEditor);
			addChild(setDescriptionEditPart, getChildren().size());
			
		} else if (reference == SchemaPackage.eINSTANCE.getSet_Members()) {			
			
			// a member record type was added to an existing (user-owned chained) set
			Set set = (Set) owner;
			MemberRole memberRole = (MemberRole) item;
											
			// refresh the owner record edit part
			SchemaRecord ownerRecord = set.getOwner().getRecord();
			GraphicalEditPart ownerRecordEditPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
			ownerRecordEditPart.refresh();			
			
			// refresh the member record edit part
			SchemaRecord memberRecord = memberRole.getRecord();
			GraphicalEditPart memberRecordEditPart = 
				(GraphicalEditPart) getViewer().getEditPartRegistry().get(memberRecord);
			memberRecordEditPart.refresh();
			
			// create the set label edit part and add it as a child
			ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
			EditPart setDescriptionEditPart = 
				SchemaDiagramEditPartFactory.createEditPart(connectionLabel, modelChangeProvider,
															schemaEditor);
			addChild(setDescriptionEditPart, getChildren().size());			
			
		} else if (owner instanceof MemberRole && 
				   reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts()) {			
		
			// connectors created
			
			MemberRole memberRole = (MemberRole) owner;
			
			// create and add the 2 connector edit parts
			for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
				Connector model = connectionPart.getConnector();
				EditPart connectorEditPart =
					SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider,
																schemaEditor);
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
			
		}*/		
		
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddRecord(context);
			} else {
				handleAddRecordUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleSwapRecordElements(context);
			} else {
				handleSwapRecordElementsUndo(context);
			}
		}
	}

	@Override
	public void afterMoveItem(EObject oldOwner, EReference reference, Object item, 
							  EObject newOwner) {		
	}

	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
		
		/*if (owner == getModel().getDiagramData() && 
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
			
			// a set or index was removed; we don't expect any connectors to be present (i.e. 
			// connectors should be removed before a set is removed; if connectors were present, 
			// this will have impact on what we have to do here though)
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
					if (connectionLabel.getMemberRole().getSet() == null ||
						connectionLabel.getMemberRole().getSet() == set) {
						
						// ... that is involved
						setDescriptionEditParts.add((EditPart) entry.getValue());
					}
				} else if (entry.getKey() instanceof ConnectionPart) {
					// set connection edit part...					
					ConnectionPart connectionPart = (ConnectionPart) entry.getKey();
					if (connectionPart.getMemberRole().getSet() == null ||
						connectionPart.getMemberRole().getSet() == set) {
						
						// ... that is involved
						SetEditPart setEditPart = (SetEditPart) entry.getValue();
						MemberRole memberRole = setEditPart.getMemberRole();
						Assert.isTrue(memberRole.getConnectionParts().size() == 1, 
								  	  "expected exactly 1 connection part: " + 
								  	  memberRole.getConnectionParts().size());						
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
			
			// refresh the record edit part(s) involved; our list might be empty, e.g. when 
			// connectors were present
			for (EditPart editPart : recordEditParts) {
				editPart.refresh();				
			}
		
		} else if (reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a record was removed as a member record type from a set; remove the set description
			// edit part first - we don't expect any connectors to be present (i.e. connectors
			// should be removed before a member record type is removed from a set) 
			MemberRole memberRole = (MemberRole) item;
			Assert.isTrue(memberRole.getConnectionParts().size() == 1, 
					  	  "expected exactly 1 connection part: " + 
					  	  memberRole.getConnectionParts().size());
			EditPart child = 
				(EditPart) getViewer().getEditPartRegistry().get(memberRole.getConnectionLabel());
			removeChild(child);
			
			// identify the owner and member record edit parts (we might not always find these
			// because the model update might be done in a composite command)
			SetEditPart setEditPart =
				(SetEditPart) getViewer().getEditPartRegistry()
				  	   					 .get(memberRole.getConnectionParts().get(0));
			List<EditPart> recordEditParts = new ArrayList<>();
			if (setEditPart.getSource() != null) {
				recordEditParts.add(setEditPart.getSource()); // owner record edit part
			}
			if (setEditPart.getTarget() != null) {
				recordEditParts.add(setEditPart.getTarget()); // member record edit part
			}
			
			// remove the connection edit part
			removeChild(setEditPart);				
			
			// refresh the owner and member record edit parts if we were able to find them
			for (EditPart editPart : recordEditParts) {
				editPart.refresh();				
			}						
						
		} else if (owner instanceof MemberRole && 
				   reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts()) {			
		
			// connectors removed (possibly because the set is being removed too)
			
			MemberRole memberRole = (MemberRole) owner;
			Assert.isTrue(memberRole.getConnectionParts().size() == 1);
			
			// find and remove both connector edit parts
			List<ConnectionPart> connectionParts = new ArrayList<>();
			connectionParts.add(memberRole.getConnectionParts().get(0));
			connectionParts.add((ConnectionPart) item); // the removed connection part
			List<ConnectorEditPart> connectorEditParts = new ArrayList<>();
			GraphicalEditPart memberRecordEditPart = null;
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
							memberRecordEditPart = (RecordEditPart) setEditPart.getTarget();
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
			
			// refresh the member record edit part (the only remaining connection edit part will be 
			// created again, provided the set isn't removed together with the connectors)
			Assert.isNotNull(memberRecordEditPart);			
			memberRecordEditPart.refresh();			
		
		}*/		
		
	}

	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {		
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				prepareForAddRecordUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				prepareForSwapRecordElements(context);
			} else {
				prepareForSwapRecordElementsUndo(context);
			}
		}		
	}
	
	private void createAndAddChild(EObject model) {
		EditPart newChild = 
			SchemaDiagramEditPartFactory.createEditPart(model, modelChangeProvider, schemaEditor);
		addChild(newChild, getChildren().size());		
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
	
	private void findAndRemoveChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		EditPart obsoleteChild = (EditPart) getViewer().getEditPartRegistry().get(model);
		Assert.isNotNull(obsoleteChild, "missing obsolete child edit part: " + model);
		removeChild(obsoleteChild);
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

	private void handleAddRecord(ModelChangeContext context) {
		SchemaRecord record = getModel().getRecords().get(getModel().getRecords().size() - 1);			
		createAndAddChild(record);
	}

	private void handleAddRecordUndo(ModelChangeContext context) {
		SchemaRecord record = (SchemaRecord) context.getListenerData();
		findAndRemoveChild(record);
	}

	private void handleSwapRecordElements(ModelChangeContext context) {
		// when swapping record elements, the record is ALWAYS removed from all SORTED multiple-
		// member sets in which it participates as a member; it is only added as a member again if
		// at least 1 element of the sort key can be retained; make sure to remove the edit parts
		// for model objects that have become obsolete after the model change and create new edit
		// parts for their replacements
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(recordName);		
		@SuppressWarnings("unchecked")
		List<MultipleMemberSetObsoleteObjectCollection> listenerData = 
			(List<MultipleMemberSetObsoleteObjectCollection>) context.getListenerData();
		for (MultipleMemberSetObsoleteObjectCollection obsoleteObjectCollection : listenerData) {
			
			// remove the obsolete connection label, set (connection part) and connector edit parts
			findAndRemoveChild(obsoleteObjectCollection.connectionLabel);
			for (ConnectionPart connectionPart : obsoleteObjectCollection.connectionParts) {
				findAndRemoveChild(connectionPart);
			}
			for (Connector connector : obsoleteObjectCollection.connectors) {
				findAndRemoveChild(connector);
			}
			
			// add a new connection label edit part and, if applicable, new connector edit parts, if 
			// and only if the record is still a member of the given multiple-member set - the edit
			// part for the connection part will be created automatically by refreshing both owner
			// and member record edit parts
			MemberRole memberRole = (MemberRole) memberRecord.getRole(obsoleteObjectCollection.setName);
			if (memberRole != null) {
				
				// create a new connection label edit part
				createAndAddChild(memberRole.getConnectionLabel());
				
				// if applicable, create the new connector edit parts
				if (memberRole.getConnectionParts().size() > 1) {
					for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {					
						createAndAddChild(connectionPart.getConnector());
					}
				}
			}
				
			// refresh the owner record edit part			
			Set set = getModel().getSet(obsoleteObjectCollection.setName);
			SchemaRecord ownerRecord = set.getOwner().getRecord();
			RecordEditPart ownerRecordEditPart = 
				(RecordEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
			ownerRecordEditPart.refresh();			
		}		
		// refresh the member record edit part
		RecordEditPart memberRecordEditPart = 
			(RecordEditPart) getViewer().getEditPartRegistry().get(memberRecord);
		memberRecordEditPart.refresh();		
	}
	
	private void handleSwapRecordElementsUndo(ModelChangeContext context) {
		// when swapping record elements, the record is ALWAYS removed from all SORTED multiple-
		// member sets in which it participates as a member; it is only added as a member again if
		// at least 1 element of the sort key can be retained
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(recordName);		
		@SuppressWarnings("unchecked")
		List<Set> listenerData = (List<Set>) context.getListenerData();
		for (MemberRole memberRole : memberRecord.getMemberRoles()) {
			Set set = memberRole.getSet();
			if (!listenerData.contains(set)) {
				
				// create the set label edit part and add it as a child
				createAndAddChild(memberRole.getConnectionLabel());
				
				// create and add the 2 connector edit parts if necessary
				if (memberRole.getConnectionParts().size() > 1) {
					for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
						createAndAddChild(connectionPart.getConnector());
					}
				}
				
				// refresh the owner record edit part			
				SchemaRecord ownerRecord = set.getOwner().getRecord();
				RecordEditPart ownerRecordEditPart = 
						(RecordEditPart) getViewer().getEditPartRegistry().get(ownerRecord);
				ownerRecordEditPart.refresh();
			}
		}
		// refresh the member record edit part
		RecordEditPart memberRecordEditPart = 
			(RecordEditPart) getViewer().getEditPartRegistry().get(memberRecord);
		memberRecordEditPart.refresh();
	}

	private void prepareForAddRecordUndo(ModelChangeContext context) {
		SchemaRecord record = getModel().getRecords().get(getModel().getRecords().size() - 1);
		context.setListenerData(record);
	}

	private void prepareForSwapRecordElements(ModelChangeContext context) {
		// when swapping record elements, the record is ALWAYS removed from all SORTED multiple-
		// member sets in which it participates as a member; it is only added as a member again if
		// at least 1 element of the sort key can be retained; build a list with the model objects
		// that will have become obsolete after the model change
		List<MultipleMemberSetObsoleteObjectCollection> listenerData = new ArrayList<>();
		context.setListenerData(listenerData);
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord record = getModel().getRecord(recordName);
		for (MemberRole memberRole : record.getMemberRoles()) {
			Set set = memberRole.getSet();
			if (set.isMultipleMember() && set.isSorted()) {
				// THE thing to watch for in case a record's elements are swapped, are the SORTED 
				// multiple-member sets of which the record is a member, there will ALWAYS be 
				// something to do so let's just prepare by collecting all model objects that will
				// be removed and possibly replaced after the model change
				MultipleMemberSetObsoleteObjectCollection obsoleteObjectCollection = 
					new MultipleMemberSetObsoleteObjectCollection(set.getName(), 
																  memberRole.getConnectionLabel());
				listenerData.add(obsoleteObjectCollection);
				for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
					obsoleteObjectCollection.connectionParts.add(connectionPart);
					Connector connector = connectionPart.getConnector();
					if (connector != null) {
						obsoleteObjectCollection.connectors.add(connector);
					}
				}
			}
		}
	}
	
	private void prepareForSwapRecordElementsUndo(ModelChangeContext context) {
		// when swapping record elements, the record is ALWAYS removed from all SORTED multiple-
		// member sets in which it participates as a member; it is only added as a member again if
		// at least 1 element of the sort key can be retained; build a list with the names of the
		// sets in which the record participates as a member immediately before the swap elements
		// operation is undone
		List<Set> listenerData = new ArrayList<>();
		context.setListenerData(listenerData);
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord record = getModel().getRecord(recordName);
		for (MemberRole memberRole : record.getMemberRoles()) {
			Set set = memberRole.getSet();
			listenerData.add(set);
		}
	}

	@Override
	public final void removeNotify() {		
		// note: this method doesn't seem to be called at all
		modelChangeProvider.removeModelChangeListener(this);
		super.removeNotify();
	}
	
	public static class MultipleMemberSetObsoleteObjectCollection {
		
		private String setName;
		private ConnectionLabel connectionLabel;
		private List<ConnectionPart> connectionParts = new ArrayList<>();
		private List<Connector> connectors = new ArrayList<>();
		
		public MultipleMemberSetObsoleteObjectCollection(String setName, 
														 ConnectionLabel connectionLabel) {
			super();
			this.setName = setName;
			this.connectionLabel = connectionLabel;
		}
		
	}
	
}
