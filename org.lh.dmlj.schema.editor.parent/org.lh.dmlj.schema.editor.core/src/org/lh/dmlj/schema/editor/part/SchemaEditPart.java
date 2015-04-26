/**
 * Copyright (C) 2015  Luc Hermans
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

import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
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
import org.lh.dmlj.schema.editor.SchemaEditor;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.policy.SchemaXYLayoutEditPolicy;

public class SchemaEditPart extends AbstractGraphicalContainerEditPart<Schema> {
	
	public SchemaEditPart(Schema schema, SchemaEditor schemaEditor) {
		super(schema, schemaEditor);
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {	
		if (context.getCommandExecutionMode() == CommandExecutionMode.EXECUTE ||
			context.getCommandExecutionMode() == CommandExecutionMode.REDO) {			
			
			afterModelChange_ExecuteOrRedo(context);
		} else {
			afterModelChange_Undo(context);
		}
	}
	
	private void afterModelChange_ExecuteOrRedo(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_CONNECTORS) {
			collectObjectsForMemberRole(context, Scope.CONNECTORS_ONLY);	
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			createAndAddChild(getModel().getDiagramData().getLabel());
		} else if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			collectObjectsForMemberRole(context, Scope.ALL);
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			createAndAddChild(getModel().getRecords().get(getModel().getRecords().size() - 1));
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			collectObjectsForSet(context, getModel().getSets().get(getModel().getSets().size() - 1));
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			collectObjectsForSet(context, getModel().getSets().get(getModel().getSets().size() - 1));
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_CONNECTORS) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			findAndRemoveChild((DiagramLabel) context.getListenerData());
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context); 
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS) {
			handleSwapRecordElements(context);
		}
	}	
	
	private void afterModelChange_Undo(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_CONNECTORS) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			findAndRemoveChild((DiagramLabel) context.getListenerData());
		} else if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			findAndRemoveChild((SchemaRecord) context.getListenerData());
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			findAndRemoveChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_CONNECTORS) {
			collectObjectsForMemberRole(context, Scope.CONNECTORS_ONLY);
			createAndAddChildren(context);		
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			createAndAddChild(getModel().getDiagramData().getLabel());
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			collectObjectsForRecord(context);
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			collectObjectsForSet(context);
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			collectObjectsForSet(context);
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			collectObjectsForMemberRole(context, Scope.ALL);
			createAndAddChildren(context);
			findAndRefreshChildren(context);
		} else if (context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS) {
			handleSwapRecordElementsUndo(context);
		}
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getCommandExecutionMode() == CommandExecutionMode.EXECUTE ||
			context.getCommandExecutionMode() == CommandExecutionMode.REDO) {
			
			beforeModelChange_ExecuteOrRedo(context);
		} else if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
			beforeModelChange_Undo(context);
		}
	}
	
	private void beforeModelChange_ExecuteOrRedo(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.DELETE_CONNECTORS) {
			collectObjectsForMemberRole(context, Scope.CONNECTORS_ONLY);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_DIAGRAM_LABEL) {
			context.setListenerData(getModel().getDiagramData().getLabel());
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_RECORD) {
			collectObjectsForRecord(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			collectObjectsForSet(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			collectObjectsForSet(context);
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			collectObjectsForMemberRole(context, Scope.ALL);
		} else if (context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS) {
			prepareForSwapRecordElements(context);
		}		
	}		
		
	private void beforeModelChange_Undo(ModelChangeContext context) {	
		if (context.getModelChangeType() == ModelChangeType.ADD_CONNECTORS) {
			collectObjectsForMemberRole(context, Scope.CONNECTORS_ONLY);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_DIAGRAM_LABEL) {
			context.setListenerData(getModel().getDiagramData().getLabel());
		} else if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			collectObjectsForMemberRole(context, Scope.ALL);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_RECORD) {
			context.setListenerData(getModel().getRecords().get(getModel().getRecords().size() - 1));
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			collectObjectsForSet(context, getModel().getSets().get(getModel().getSets().size() - 1));
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			collectObjectsForSet(context, getModel().getSets().get(getModel().getSets().size() - 1));
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_CONNECTORS) {
			collectObjectsForMemberRole(context, Scope.CONNECTORS_ONLY);
		} else if (context.getModelChangeType() == ModelChangeType.SWAP_RECORD_ELEMENTS) {
			prepareForSwapRecordElementsUndo(context);
		}		
	}	

	@Override
	protected void createEditPolicies() {
		
		// install the edit policy for creating, moving and resizing diagram nodes...
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new SchemaXYLayoutEditPolicy(getModel()));
		
		// install the snap feedback policy...
		installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());	  
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
		
		// VSAM indexes
		for (Set set : getModel().getSets()) {
			if (set.getMode() == SetMode.VSAM_INDEX) {
				allObjects.add(set.getVsamIndex());
			}
		}
		
		return allObjects;
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
		List<SwapRecordElementsObsoleteObjectCollection> listenerData = 
			(List<SwapRecordElementsObsoleteObjectCollection>) context.getListenerData();
		for (SwapRecordElementsObsoleteObjectCollection obsoleteObjectCollection : listenerData) {
			
			// remove the obsolete connection label and connector edit parts, if any
			findAndRemoveChild(obsoleteObjectCollection.connectionLabel);
			for (Connector connector : obsoleteObjectCollection.connectors) {
				findAndRemoveChild(connector);
			}
			
			// add a new connection label edit part and, if applicable, new connector edit parts, if 
			// and only if the record is still a member of the given multiple-member set - the edit
			// part for the connection part will be created automatically by refreshing both owner
			// and member record edit parts
			MemberRole memberRole = 
				(MemberRole) memberRecord.getRole(obsoleteObjectCollection.setName);
			if (memberRole != null) {
				
				// create a new connection label edit part
				createAndAddChild(memberRole.getConnectionLabel());
				
				// if applicable, create the new connector edit parts
				if (memberRole.getConnectionParts().size() > 1) {
					createAndAddChild(memberRole.getConnectionParts().get(0).getConnector());
					createAndAddChild(memberRole.getConnectionParts().get(1).getConnector());
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
					createAndAddChild(memberRole.getConnectionParts().get(0).getConnector());
					createAndAddChild(memberRole.getConnectionParts().get(1).getConnector());
				}
				
				// refresh the owner record edit part
				findAndRefreshChild(set.getOwner().getRecord());
			}
		}
		// refresh the member record edit part
		findAndRefreshChild(memberRecord);
	}
	
	private void prepareForSwapRecordElements(ModelChangeContext context) {
		// when swapping record elements, the record is ALWAYS removed from all SORTED multiple-
		// member sets in which it participates as a member; it is only added as a member again if
		// at least 1 element of the sort key can be retained; build a list with the model objects
		// that will have become obsolete after the model change
		List<SwapRecordElementsObsoleteObjectCollection> listenerData = new ArrayList<>();
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
				SwapRecordElementsObsoleteObjectCollection obsoleteObjectCollection = 
					new SwapRecordElementsObsoleteObjectCollection(set.getName(), 
																   memberRole.getConnectionLabel());
				listenerData.add(obsoleteObjectCollection);
				if (memberRole.getConnectionParts().size() > 1) {				
					obsoleteObjectCollection.connectors.add(memberRole.getConnectionParts().get(0).getConnector());	
					obsoleteObjectCollection.connectors.add(memberRole.getConnectionParts().get(1).getConnector());	
				}
			}
		}
	}
	
	private void prepareForSwapRecordElementsUndo(ModelChangeContext context) {
		// when swapping record elements, the record is ALWAYS removed from all SORTED multiple-
		// member sets in which it participates as a member; it is only added as a member again if
		// at least 1 element of the sort key can be retained; build a list with the sets in which 
		// the record participates as a member immediately before the swap elements operation is 
		// undone
		List<Set> listenerData = new ArrayList<>();
		context.setListenerData(listenerData);
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		SchemaRecord record = getModel().getRecord(recordName);
		for (MemberRole memberRole : record.getMemberRoles()) {
			Set set = memberRole.getSet();
			listenerData.add(set);
		}
	}
	
	/**
	 * A helper class to assist in swap record elements operations.
	 */
	public static class SwapRecordElementsObsoleteObjectCollection {
		
		private String setName;
		private ConnectionLabel connectionLabel;
		private List<Connector> connectors = new ArrayList<>();
		
		public SwapRecordElementsObsoleteObjectCollection(String setName, 
														  ConnectionLabel connectionLabel) {
			super();
			this.setName = setName;
			this.connectionLabel = connectionLabel;
		}
		
	}
	
}
