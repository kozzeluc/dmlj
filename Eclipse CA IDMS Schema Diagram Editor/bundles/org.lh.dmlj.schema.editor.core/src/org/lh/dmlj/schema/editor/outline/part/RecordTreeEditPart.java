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
package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.policy.RecordComponentEditPolicy;
import org.lh.dmlj.schema.editor.policy.RemoveMemberFromSetEditPolicy;

public class RecordTreeEditPart extends AbstractSchemaTreeEditPart<SchemaRecord> {

	public RecordTreeEditPart(SchemaRecord record, IModelChangeProvider modelChangeProvider) {
		super(record, modelChangeProvider);
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {		
		boolean atTopLevel = getParentModelObject() instanceof Schema;		
		if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET && atTopLevel && 
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
			
			// a member record type was added to (what is now definitely) a multiple-member set
			// (execute/redo)
			SchemaRecord newMemberRecord = findRecord(context);
			if (newMemberRecord == getModel()) {
				Set set = findSet(context);
				createAndAddChild(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET && 
				   atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof Set) {
		
			// an add member to set operation was undone
			Set set = (Set) context.getListenerData();
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
					
			// a system owned indexed set was added (execute/redo)
			Set set = getLastSet();
			if (isMemberOf(set)) {
				createAndAddChild(set.getSystemOwner(), set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET && 
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof SystemOwner) {
					
			// an add system owned indexed set to the model record operation was undone
			SystemOwner systemOwner = (SystemOwner) context.getListenerData();
			findAndRemoveChild(systemOwner, false);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET && 
				  atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
			
			// a user owned set was added (execute/redo)
			Set set = getLastSet();
			if (isOwnerOf(set) || isMemberOf(set)) {				
				createAndAddChild(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET && 
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof Set) {
					
			// an add user owned set operation was undone
			Set set = (Set) context.getListenerData();
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
					
			// a VSAM index was added (execute/redo)
			Set set = getLastSet();
			if (isMemberOf(set)) {
				createAndAddChild(set.getVsamIndex(), set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX && 
				   context.getCommandExecutionMode() == CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof VsamIndex) {
					
			// an add VSAM index to the model record operation was undone
			VsamIndex vsamIndex = (VsamIndex) context.getListenerData();
			findAndRemoveChild(vsamIndex, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof SystemOwner) {
			
			// a system owned indexed set was deleted (execute/redo)
			SystemOwner systemOwner = (SystemOwner) context.getListenerData();
			findAndRemoveChild(systemOwner, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
		
			// a delete system owned indexed set operation was undone
			Set set = findSet(context);
			if (isMemberOf(set)) {
				createAndAddSystemOwnerAsChild(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof Set) {
			
			// a user owned set was deleted (execute/redo)
			Set set = (Set) context.getListenerData();
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
		
			// a delete user owned set operation was undone
			Set set = findSet(context);
			if (isOwnerOf(set) || isMemberOf(set)) {
				createAndAddSetAsChild(context);
			}			 
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof VsamIndex) {
			
			// a VSAM index was deleted (execute/redo)
			VsamIndex vsamIndex = (VsamIndex) context.getListenerData();
			findAndRemoveChild(vsamIndex, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX && 
				   atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
		
			// a delete VSAM index operation was undone
			Set set = findSet(context);
			if (isMemberOf(set)) {
				createAndAddVsamIndexAsChild(context);
			}
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
				   context.getListenerData() instanceof Set) {
		
			// a member was removed from the model set
			Set set = (Set) context.getListenerData();
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET && 
				   atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
		
			// a remove member from set operation was undone
			SchemaRecord newMemberRecord = findRecord(context);
			if (newMemberRecord == getModel()) {
				Set set = findSet(context);
				createAndAddChild(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && 
				   context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()) &&
				   appliesToModelRecord(context)) {
			
			// the record name has changed (execute/undo/redo)... the order of the parent edit part 
			// might become disrupted, so we have to inform that edit part of this fact
			nodeTextChanged();						
		}
	}
	
	private boolean appliesToModelRecord(ModelChangeContext context) {
		if (Boolean.TRUE.equals(context.getListenerData())) {
			return true;
		} else {
			String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
			return getModel().getName().equals(recordName);
		}
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		boolean atTopLevel = getParentModelObject() instanceof Schema;		
		if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET && 
			atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
	
			// an add member to set operation is being undone
			SchemaRecord newMemberRecord = findRecord(context);
			if (newMemberRecord == getModel()) {			
				Set set = findSet(context);			
				context.setListenerData(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET && atTopLevel &&
			context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
			
			// an add system owned indexed set operation is being undone
			Set set = getLastSet();
			if (isMemberOf(set)) {
				context.setListenerData(set.getSystemOwner());
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
					
			// an add user owned set operation is being undone
			Set set = getLastSet();
			if (isOwnerOf(set) || isMemberOf(set)) {				
				context.setListenerData(set);
			}		
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX && atTopLevel &&
				context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				
				// an add VSAM index operation is being undone
				Set set = getLastSet();
				if (isMemberOf(set)) {
					context.setListenerData(set.getVsamIndex());
				}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
		
			// a system owned indexed set is being deleted
			Set set = findSet(context);
			if (isMemberOf(set)) {
				context.setListenerData(set.getSystemOwner());
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
		
			// a user owned set is being deleted
			Set set = findSet(context);
			if (isOwnerOf(set) || isMemberOf(set)) {
				context.setListenerData(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {			
		
			// a VSAM index is being deleted
			Set set = findSet(context);
			if (isMemberOf(set)) {
				context.setListenerData(set.getVsamIndex());
			}
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET && 
				   atTopLevel && context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
			
			// a member is being removed from a set
			SchemaRecord newMemberRecord = findRecord(context);
			if (newMemberRecord == getModel()) {			
				Set set = findSet(context);			
				context.setListenerData(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && 
			context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO &&
			context.appliesTo(getModel())) {
					
			// the model record's name is changing (execute/redo); put Boolean.TRUE in the context's 
			// listener's data so that we can respond to this when processing the after model change 
			// event
			context.setListenerData(Boolean.TRUE);
		}
	}
	
	private void createAndAddSetAsChild(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		Set set = getModel().getSchema().getSet(setName);
		createAndAddChild(set);
	}
	
	private void createAndAddSystemOwnerAsChild(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		Set set = getModel().getSchema().getSet(setName);
		createAndAddChild(set.getSystemOwner(), set);
	}
	
	private void createAndAddVsamIndexAsChild(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		Set set = getModel().getSchema().getSet(setName);
		createAndAddChild(set.getVsamIndex(), set);
	}
	
	@Override
	protected void createEditPolicies() {		
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Set) {
			Set set = (Set) parentModelObject;
			if (set.isVsam() || set.getOwner().getRecord() != getModel()) {
				// the model record is a member of the parent edit part's model set; the next 
				// edit policy allows for the removal of the record as a set member, without the
				// ability to remove the set when the record is the last remaining member in the set 
				MemberRole memberRole = null;
				for (MemberRole aMemberRole : getModel().getMemberRoles()) {
					if (aMemberRole.getSet() == set) {
						memberRole = aMemberRole;
						break;
					}
				}
				Assert.isNotNull(memberRole, "internal error: no member role set (" + 
								 getModel().getName() + ")");
				installEditPolicy(EditPolicy.COMPONENT_ROLE, 
								  new RemoveMemberFromSetEditPolicy(memberRole, false));
			}
			// a record cannot be deleted when pressing the delete key under a set's owner record
		} else {
			// the next edit policy allows for the deletion of a record 
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new RecordComponentEditPolicy());
		}
	}
	
	private SchemaRecord findRecord(ModelChangeContext context) {
		String recordName = context.getContextData().get(IContextDataKeys.RECORD_NAME);
		return getModel().getSchema().getRecord(recordName);
	}
	
	private Set findSet(ModelChangeContext context) {
		String setName = context.getContextData().get(IContextDataKeys.SET_NAME);
		return getModel().getSchema().getSet(setName);
	}
	
	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] {Set.class};
	}
	
	@Override
	protected String getImagePath() {
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Set) {
			Set set = (Set) parentModelObject;
			if (set.getOwner() != null && set.getOwner().getRecord() == getModel()) {
				return "icons/owner_record.gif";
			} else {
				return "icons/member_record.gif";
			}
		} else if (parentModelObject instanceof SystemOwner ||
				   parentModelObject instanceof VsamIndex) {
			
			return "icons/member_record.gif";
		} else {
			return "icons/record.gif";
		}
	}
	
	private Set getLastSet() {
		return getModel().getSchema().getSets().get(getModel().getSchema().getSets().size() - 1);
	}

	@Override
	protected List<?> getModelChildren() {
		
		List<Object> children = new ArrayList<>();
		
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			
			// only when the record is at the top level, add the sets in which the record 
			// participates, in alphabetical order
			
			// gather the list of sets and sort it
			List<Set> sets = new ArrayList<>();
			for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
				sets.add(ownerRole.getSet());
			}
			for (MemberRole memberRole : getModel().getMemberRoles()) {
				sets.add(memberRole.getSet());
			}			
			Collections.sort(sets);
			
			for (Set set : sets) {
				if (set.getSystemOwner() != null) {
					// the record is the member of a system owned indexed set; add the system owner
					children.add(set.getSystemOwner());
				} else if (set.getOwner() != null && set.getOwner().getRecord() == getModel()) {
					// the record is the owner of the set; add the set
					children.add(set);
				} else {
					// the record is a member of the set
					if (set.isVsam()) {
						// add the VSAM index
						children.add(set.getVsamIndex());
					} else {
						// add the set (why are we traversing the list of members her ?)
						for (MemberRole memberRole : set.getMembers()) {
							if (memberRole.getRecord() == getModel()) {
								children.add(memberRole.getSet());
							}
						}
					}
				}
			}
		}
		
		return children;
		
	}

	@Override
	protected INodeTextProvider<SchemaRecord> getNodeTextProvider() {
		return getModel();
	}
	
	private boolean isMemberOf(Set set) {
		for (MemberRole memberRole : set.getMembers()) {
			if (memberRole.getRecord() == getModel()) {
				return true;
			}
		}
		return false;
	}	
	
	private boolean isOwnerOf(Set set) {
		return set.getOwner().getRecord() == getModel();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void registerModel() {
		// different edit parts exist for the same record; make sure that selecting a record in the
		// SchemaEditor yields the outline view's top level record to become the current selection
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// the model object is the key in the edit part registry; this is what we want so that
			// selecting a record in the SchemaEditor selects the top level record edit part in the
			// outline view
			super.registerModel(); 
		} else {
			// assure that record edit parts that are not at the top level will never be found
			// by their model object; create an artificial key to make this happen
			EditPartRegistryKey<SchemaRecord> key = new EditPartRegistryKey<>(getModel());
			getViewer().getEditPartRegistry().put(key, this);
		}		
		
		
	}

}
