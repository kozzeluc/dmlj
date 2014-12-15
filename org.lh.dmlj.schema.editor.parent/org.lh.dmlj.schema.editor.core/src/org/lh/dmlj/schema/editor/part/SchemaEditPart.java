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
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SystemOwner;
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
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_CONNECTORS) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddConnectors(context);
			} else {
				handleAddConnectorsUndo(context);
			}
		} if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddDiagramLabel(context);
			} else {
				handleAddDiagramLabelUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddMemberToSet(context);
			} else {
				handleAddMemberToSetUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddRecord(context);
			} else {
				handleAddRecordUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddSystemOwnedSet(context);
			} else {
				handleAddSystemOwnedSetUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleAddUserOwnedSet(context);
			} else {
				handleAddUserOwnedSetUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_CONNECTORS) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleDeleteConnectors(context);
			} else {
				handleDeleteConnectorsUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleDeleteDiagramLabel(context);
			} else {
				handleDeleteDiagramLabelUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleDeleteRecord(context);
			} else {
				handleDeleteRecordUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				handleRemoveMemberFromSet(context);
			} else {
				handleRemoveMemberFromSetUndo(context);
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
			
			// the diagram label was removed --> MIGRATED
		
		} else if (owner == getModel() && 
				   reference == SchemaPackage.eINSTANCE.getSchema_Records()) {
			
			// a record was removed --> MIGRATED			
			
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
			
			// a record was removed as a member record type from a set --> MIGRATED						
						
		} else if (owner instanceof MemberRole && 
				   reference == SchemaPackage.eINSTANCE.getMemberRole_ConnectionParts()) {			
		
			// connectors removed --> MIGRATED			
		
		}*/		
		
	}

	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {		
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_CONNECTORS) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {			
				prepareForAddConnectorsUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				prepareForAddDiagramLabelUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				prepareForAddMemberToSetUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				prepareForAddRecordUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				prepareForAddSystemOwnedSetUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				prepareForAddUserOwnedSetUndo(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_CONNECTORS) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				prepareForDeleteConnectors(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				prepareForDeleteDiagramLabel(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				prepareForDeleteRecord(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
				prepareForRemoveMemberFromSet(context);
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
	
	private void findAndRefreshChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		EditPart child = (EditPart) getViewer().getEditPartRegistry().get(model);
		Assert.isNotNull(child, "missing child edit part: " + model);
		child.refresh();
	}

	private void findAndRemoveChild(EObject model) {
		Assert.isNotNull(model, "model is null");
		EditPart obsoleteChild = (EditPart) getViewer().getEditPartRegistry().get(model);
		Assert.isNotNull(obsoleteChild, "missing obsolete child edit part: " + model);
		removeChild(obsoleteChild);
	}
	
	private void findAndRemoveChildren(List<EObject> models) {
		if (models == null || models.isEmpty()) {
			return;
		}
		for (EObject model : models) {
			findAndRemoveChild(model);
		}
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

	private void handleAddConnectors(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		Set set = memberRole.getSet();
		EObject owner = 
			set.getSystemOwner() != null ? set.getSystemOwner() : set.getOwner().getRecord();
		createAndAddChild(memberRole.getConnectionParts().get(0).getConnector());
		createAndAddChild(memberRole.getConnectionParts().get(1).getConnector());
		findAndRefreshChild(owner);
		findAndRefreshChild(memberRecord);
	}

	private void handleAddConnectorsUndo(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		Set set = memberRole.getSet();
		EObject owner = 
			set.getSystemOwner() != null ? set.getSystemOwner() : set.getOwner().getRecord();
		@SuppressWarnings("unchecked")
		List<EObject> obsoleteObjects = (List<EObject>) context.getListenerData();
		findAndRemoveChildren(obsoleteObjects);
		findAndRefreshChild(owner);
		findAndRefreshChild(memberRecord);
	}

	private void handleAddDiagramLabel(ModelChangeContext context) {
		DiagramLabel diagramLabel = getModel().getDiagramData().getLabel();
		createAndAddChild(diagramLabel);
	}

	private void handleAddDiagramLabelUndo(ModelChangeContext context) {
		DiagramLabel diagramLabel = (DiagramLabel) context.getListenerData();
		findAndRemoveChild(diagramLabel);
	}

	private void handleAddMemberToSet(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		Set set = memberRole.getSet();
		SchemaRecord ownerRecord = set.getOwner().getRecord();			
		findAndRefreshChild(ownerRecord);		
		findAndRefreshChild(memberRecord);		
		createAndAddChild(memberRole.getConnectionLabel());		
	}

	private void handleAddMemberToSetUndo(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		Set set = getModel().getSet(setName);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		SchemaRecord ownerRecord = set.getOwner().getRecord();			
		ConnectionLabel connectionLabel = (ConnectionLabel) context.getListenerData();
		findAndRefreshChild(ownerRecord);		
		findAndRefreshChild(memberRecord);
		findAndRemoveChild(connectionLabel);		
	}

	private void handleAddRecord(ModelChangeContext context) {
		SchemaRecord record = getModel().getRecords().get(getModel().getRecords().size() - 1);			
		createAndAddChild(record);
	}

	private void handleAddRecordUndo(ModelChangeContext context) {
		SchemaRecord record = (SchemaRecord) context.getListenerData();
		findAndRemoveChild(record);
	}

	private void handleAddSystemOwnedSet(ModelChangeContext context) {
		Set set = getModel().getSets().get(getModel().getSets().size() - 1);			
		SystemOwner systemOwner = set.getSystemOwner();
		MemberRole memberRole = set.getMembers().get(0);
		SchemaRecord memberRecord = memberRole.getRecord();
		createAndAddChild(memberRole.getConnectionLabel());	
		createAndAddChild(systemOwner);
		findAndRefreshChild(memberRecord);			
	}

	private void handleAddSystemOwnedSetUndo(ModelChangeContext context) {
		@SuppressWarnings("unchecked")
		List<EObject> obsoleteOrToRefreshObjects = (List<EObject>) context.getListenerData();
		for (EObject obsoleteOrToRefreshObject : obsoleteOrToRefreshObjects) {
			if (obsoleteOrToRefreshObject instanceof SchemaRecord) {
				findAndRefreshChild(obsoleteOrToRefreshObject); // member record
			} else if (obsoleteOrToRefreshObject instanceof SystemOwner ||
					   obsoleteOrToRefreshObject instanceof ConnectionLabel || 
					   obsoleteOrToRefreshObject instanceof ConnectionPart) {
				
				findAndRemoveChild(obsoleteOrToRefreshObject);
			}
		}
	}

	private void handleAddUserOwnedSet(ModelChangeContext context) {
		Set set = getModel().getSets().get(getModel().getSets().size() - 1);			
		SchemaRecord ownerRecord = set.getOwner().getRecord();
		MemberRole memberRole = set.getMembers().get(0);
		SchemaRecord memberRecord = memberRole.getRecord();
		createAndAddChild(memberRole.getConnectionLabel());
		findAndRefreshChild(ownerRecord);
		findAndRefreshChild(memberRecord);
	}

	private void handleAddUserOwnedSetUndo(ModelChangeContext context) {
		@SuppressWarnings("unchecked")
		List<EObject> obsoleteOrToRefreshObjects = (List<EObject>) context.getListenerData();
		for (EObject obsoleteOrToRefreshObject : obsoleteOrToRefreshObjects) {
			if (obsoleteOrToRefreshObject instanceof SchemaRecord) {
				findAndRefreshChild(obsoleteOrToRefreshObject); // owner or member record
			} else if (obsoleteOrToRefreshObject instanceof ConnectionLabel || 
					   obsoleteOrToRefreshObject instanceof ConnectionPart) {
				
				findAndRemoveChild(obsoleteOrToRefreshObject);
			}
		}
	}

	private void handleDeleteConnectors(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		Set set = memberRole.getSet();
		EObject owner = 
			set.getSystemOwner() != null ? set.getSystemOwner() : set.getOwner().getRecord();
		@SuppressWarnings("unchecked")
		List<EObject> obsoleteObjects = (List<EObject>) context.getListenerData();
		findAndRemoveChildren(obsoleteObjects);
		findAndRefreshChild(owner);
		findAndRefreshChild(memberRecord);
	}

	private void handleDeleteConnectorsUndo(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		Set set = memberRole.getSet();
		EObject owner = 
			set.getSystemOwner() != null ? set.getSystemOwner() : set.getOwner().getRecord();
		createAndAddChild(memberRole.getConnectionParts().get(0).getConnector());
		createAndAddChild(memberRole.getConnectionParts().get(1).getConnector());
		findAndRefreshChild(owner);
		findAndRefreshChild(memberRecord);
	}

	private void handleDeleteDiagramLabel(ModelChangeContext context) {
		DiagramLabel diagramLabel = (DiagramLabel) context.getListenerData();
		findAndRemoveChild(diagramLabel);
	}

	private void handleDeleteDiagramLabelUndo(ModelChangeContext context) {
		DiagramLabel diagramLabel = getModel().getDiagramData().getLabel();
		createAndAddChild(diagramLabel);
	}

	private void handleDeleteRecord(ModelChangeContext context) {
		// NOTE: it is assumed that the deleted record did NOT participate in any sets; this 
		// condition is probably about to change in the future.
		SchemaRecord record = (SchemaRecord) context.getListenerData();
		findAndRemoveChild(record);
	}

	private void handleDeleteRecordUndo(ModelChangeContext context) {
		// NOTE: it is assumed that the deleted record did NOT participate in any sets; this 
		// condition is probably about to change in the future.
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord record = getModel().getRecord(recordName);
		createAndAddChild(record);
	}

	private void handleRemoveMemberFromSet(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		Set set = getModel().getSet(setName);
		SchemaRecord ownerRecord = set.getOwner().getRecord();
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		@SuppressWarnings("unchecked")
		List<EObject> obsoleteObjects = (List<EObject>) context.getListenerData();
		findAndRemoveChildren(obsoleteObjects);
		findAndRefreshChild(ownerRecord);
		findAndRefreshChild(memberRecord);		
	}

	private void handleRemoveMemberFromSetUndo(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);		
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		SchemaRecord ownerRecord = memberRole.getSet().getOwner().getRecord();
		createAndAddChild(memberRole.getConnectionLabel());
		if (memberRole.getConnectionParts().size() > 1) {
			createAndAddChild(memberRole.getConnectionParts().get(0).getConnector());
			createAndAddChild(memberRole.getConnectionParts().get(1).getConnector());
		}
		findAndRefreshChild(ownerRecord);
		findAndRefreshChild(memberRecord);		
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
			findAndRefreshChild(ownerRecord);
		}		
		// refresh the member record edit part
		findAndRefreshChild(memberRecord);
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

	private void prepareForAddConnectorsUndo(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		List<EObject> objectsToBecomeObsolete = new ArrayList<>();
		context.setListenerData(objectsToBecomeObsolete);
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(0).getConnector());
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(1).getConnector());
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(1));
	}

	private void prepareForAddDiagramLabelUndo(ModelChangeContext context) {
		DiagramLabel diagramLabel = getModel().getDiagramData().getLabel();
		context.setListenerData(diagramLabel);
	}

	private void prepareForAddMemberToSetUndo(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		context.setListenerData(memberRole.getConnectionLabel());		
	}

	private void prepareForAddRecordUndo(ModelChangeContext context) {
		SchemaRecord record = getModel().getRecords().get(getModel().getRecords().size() - 1);
		context.setListenerData(record);
	}

	private void prepareForAddSystemOwnedSetUndo(ModelChangeContext context) {
		Set set = getModel().getSets().get(getModel().getSets().size() - 1);			
		SystemOwner systemOwner = set.getSystemOwner();
		MemberRole memberRole = set.getMembers().get(0);
		SchemaRecord memberRecord = memberRole.getRecord();
		List<EObject> obsoleteOrToRefreshObjects = new ArrayList<EObject>();
		context.setListenerData(obsoleteOrToRefreshObjects);
		obsoleteOrToRefreshObjects.add(memberRole.getConnectionParts().get(0)); // to become obsolete
		obsoleteOrToRefreshObjects.add(memberRole.getConnectionLabel());		// to become obsolete
		obsoleteOrToRefreshObjects.add(systemOwner);	// to become obsolete
		obsoleteOrToRefreshObjects.add(memberRecord);	// to refresh
	}

	private void prepareForAddUserOwnedSetUndo(ModelChangeContext context) {
		Set set = getModel().getSets().get(getModel().getSets().size() - 1);			
		SchemaRecord ownerRecord = set.getOwner().getRecord();
		MemberRole memberRole = set.getMembers().get(0);
		SchemaRecord memberRecord = memberRole.getRecord();
		List<EObject> obsoleteOrToRefreshObjects = new ArrayList<EObject>();
		context.setListenerData(obsoleteOrToRefreshObjects);
		obsoleteOrToRefreshObjects.add(memberRole.getConnectionParts().get(0)); // to become obsolete
		obsoleteOrToRefreshObjects.add(memberRole.getConnectionLabel());		// to become obsolete
		obsoleteOrToRefreshObjects.add(ownerRecord);	// to refresh
		obsoleteOrToRefreshObjects.add(memberRecord);	// to refresh
	}

	private void prepareForDeleteConnectors(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);
		List<EObject> objectsToBecomeObsolete = new ArrayList<>();
		context.setListenerData(objectsToBecomeObsolete);
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(0).getConnector());
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(1).getConnector());
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(1));		
	}

	private void prepareForDeleteDiagramLabel(ModelChangeContext context) {
		DiagramLabel diagramLabel = getModel().getDiagramData().getLabel();
		context.setListenerData(diagramLabel);
	}

	private void prepareForDeleteRecord(ModelChangeContext context) {
		// NOTE: it is assumed that the deleted record does NOT participate in any sets; this 
		// condition is probably about to change in the future.
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord record = getModel().getRecord(recordName);
		context.setListenerData(record);
	}

	private void prepareForRemoveMemberFromSet(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		String memberRecordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord memberRecord = getModel().getRecord(memberRecordName);
		MemberRole memberRole = (MemberRole) memberRecord.getRole(setName);		
		List<EObject> objectsToBecomeObsolete = new ArrayList<>();
		context.setListenerData(objectsToBecomeObsolete);
		objectsToBecomeObsolete.add(memberRole.getConnectionLabel());
		objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(0));
		if (memberRole.getConnectionParts().size() > 1) {			
			objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(0).getConnector());
			objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(1).getConnector());
			objectsToBecomeObsolete.add(memberRole.getConnectionParts().get(1));
		}	
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
