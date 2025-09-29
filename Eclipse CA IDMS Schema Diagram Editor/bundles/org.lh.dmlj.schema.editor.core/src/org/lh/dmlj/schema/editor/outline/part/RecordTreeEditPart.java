/**
 * Copyright (C) 2025  Luc Hermans
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

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.policy.RecordComponentEditPolicy;
import org.lh.dmlj.schema.editor.policy.RemoveMemberFromSetEditPolicy;

public class RecordTreeEditPart extends AbstractSchemaTreeEditPart<SchemaRecord> {

	public RecordTreeEditPart(SchemaRecord schemaRecord, IModelChangeProvider modelChangeProvider) {
		super(schemaRecord, modelChangeProvider);
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()) && appliesToModelRecord(context)) {
			// the record name has changed (execute/undo/redo)... the order of the parent edit part might become
			// disrupted, so we have to inform that edit part of this fact
			nodeTextChanged();
		} else if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
			afterUndoModelChange(context);
		} else if (getParentModelObject() instanceof Schema) {
			afterExecuteOrRedoModelChangeForParentModelObjectOfTypeSchema(context);
		}
	}
	
	private void afterExecuteOrRedoModelChangeForParentModelObjectOfTypeSchema(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			addMemberToSet(context);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			afterModelChangeOfTypeAddSystemOwnedSet();
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			afterModelChangeOfTypeAddUserOwnedSet();
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX) {
			afterModelChangeOfTypeAddVsamIndex();
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET && context.getListenerData() instanceof SystemOwner systemOwner) {				
			findAndRemoveChild(systemOwner, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET && context.getListenerData() instanceof Set set) {
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX && context.getListenerData() instanceof VsamIndex vsamIndex) {
			findAndRemoveChild(vsamIndex, false);
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET && context.getListenerData() instanceof Set set) {
			findAndRemoveChild(set, false);
		}
	}
	
	private void afterModelChangeOfTypeAddSystemOwnedSet() {
		var set = getLastSet();
		if (isMemberOf(set)) {
			createAndAddChild(set.getSystemOwner(), set);
		}
	}
	
	private void afterModelChangeOfTypeAddUserOwnedSet() {
		var set = getLastSet();
		if (isOwnerOf(set) || isMemberOf(set)) {				
			createAndAddChild(set);
		}
	}
	
	private void afterModelChangeOfTypeAddVsamIndex() {
		var set = getLastSet();
		if (isMemberOf(set)) {
			createAndAddChild(set.getVsamIndex(), set);
		}		
	}
	
	private void afterUndoModelChange(ModelChangeContext context) {
		if (getParentModelObject() instanceof Schema) {
			afterUndoModelChangeForParentModelObjectOfTypeSchema(context);
		} else {
			afterUndoModelChangeForParentModelObjectOfTypeOtherThanSchema(context);
		}
	}
	
	private void afterUndoModelChangeForParentModelObjectOfTypeSchema(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET && context.getListenerData() instanceof Set set) {
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			afterUndoModelChangeOfTypeDeleteSystemOwnedSet(context);
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			afterUndoModelChangeOfTypeDeleteUserOwnedSet(context);			
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX) {
			afterUndoModelChangeOfTypeDeleteVsamIndex(context);
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			addMemberToSet(context);
		}
	}
	
	private void afterUndoModelChangeOfTypeDeleteSystemOwnedSet(ModelChangeContext context) {
		var set = findSet(context);
		if (isMemberOf(set)) {
			createAndAddSystemOwnerAsChild(context);
		}
	}
	
	private void afterUndoModelChangeOfTypeDeleteUserOwnedSet(ModelChangeContext context) {
		var set = findSet(context);
		if (isOwnerOf(set) || isMemberOf(set)) {
			createAndAddSetAsChild(context);
		}		
	}
	
	private void afterUndoModelChangeOfTypeDeleteVsamIndex(ModelChangeContext context) {
		var set = findSet(context);
		if (isMemberOf(set)) {
			createAndAddVsamIndexAsChild(context);
		}
	}
	
	private void addMemberToSet(ModelChangeContext context) {
		var newMemberRecord = findRecord(context);
		if (newMemberRecord == getModel()) {
			var set = findSet(context);
			createAndAddChild(set);
		}
	}
	
	private void afterUndoModelChangeForParentModelObjectOfTypeOtherThanSchema(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET && context.getListenerData() instanceof SystemOwner systemOwner) {
			findAndRemoveChild(systemOwner, false);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET && context.getListenerData() instanceof Set set) {
			findAndRemoveChild(set, false);
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX && context.getListenerData() instanceof VsamIndex vsamIndex) {
			findAndRemoveChild(vsamIndex, false);
		}
	}
	
	private boolean appliesToModelRecord(ModelChangeContext context) {
		if (Boolean.TRUE.equals(context.getListenerData())) {
			return true;
		} else {
			var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
			return getModel().getName().equals(recordName);
		}
	}
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()) &&
		    context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(getModel())) {
			
			// the model record's name is changing (execute/redo); put Boolean.TRUE in the context's listener's
			// data so that we can respond to this when processing the after model change event
			context.setListenerData(Boolean.TRUE);
		} else if (getParentModelObject() instanceof Schema) {
			if (context.getCommandExecutionMode() == CommandExecutionMode.UNDO) {
				beforeUndoModelChangeForParentModelObjectOfTypeSchema(context);
			} else if (getParentModelObject() instanceof Schema) {
				beforeExecuteOrRedoModelChangeForParentModelObjectOfTypeSchema(context);
			}
		}
	}
	
	private void beforeExecuteOrRedoModelChangeForParentModelObjectOfTypeSchema(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.DELETE_SYSTEM_OWNED_SET) {
			var set = findSet(context);
			if (isMemberOf(set)) {
				context.setListenerData(set.getSystemOwner());
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_USER_OWNED_SET) {
			var set = findSet(context);
			if (isOwnerOf(set) || isMemberOf(set)) {
				context.setListenerData(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.DELETE_VSAM_INDEX) {
			var set = findSet(context);
			if (isMemberOf(set)) {
				context.setListenerData(set.getVsamIndex());
			}
		} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
			var newMemberRecord = findRecord(context);
			if (newMemberRecord == getModel()) {			
				var set = findSet(context);			
				context.setListenerData(set);
			}
		}
	}

	private void beforeUndoModelChangeForParentModelObjectOfTypeSchema(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {
			var newMemberRecord = findRecord(context);
			if (newMemberRecord == getModel()) {			
				var set = findSet(context);			
				context.setListenerData(set);
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_SYSTEM_OWNED_SET) {
			var set = getLastSet();
			if (isMemberOf(set)) {
				context.setListenerData(set.getSystemOwner());
			}
		} else if (context.getModelChangeType() == ModelChangeType.ADD_USER_OWNED_SET) {
			var set = getLastSet();
			if (isOwnerOf(set) || isMemberOf(set)) {				
				context.setListenerData(set);
			}		
		} else if (context.getModelChangeType() == ModelChangeType.ADD_VSAM_INDEX) {
			var set = getLastSet();
			if (isMemberOf(set)) {
				context.setListenerData(set.getVsamIndex());
			}
		}		
	}

	private void createAndAddSetAsChild(ModelChangeContext context) {
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		var set = getModel().getSchema().getSet(setName);
		createAndAddChild(set);
	}
	
	private void createAndAddSystemOwnerAsChild(ModelChangeContext context) {
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		var set = getModel().getSchema().getSet(setName);
		createAndAddChild(set.getSystemOwner(), set);
	}
	
	private void createAndAddVsamIndexAsChild(ModelChangeContext context) {
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		var set = getModel().getSchema().getSet(setName);
		createAndAddChild(set.getVsamIndex(), set);
	}
	
	@Override
	protected void createEditPolicies() {
		if (isReadOnlyMode()) {
			return;
		}
		var parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Set set) {
			if (set.isVsam() || set.getOwner().getRecord() != getModel()) {
				// the model record is a member of the parent edit part's model set; the next edit policy allows
				// for the removal of the record as a set member, without the ability to remove the set when the
				// record is the last remaining member in the set
				var memberRole = getModel().getMemberRoles().stream()
						.filter(aMemberRole -> aMemberRole.getSet() == set)
						.findFirst()
						.orElseThrow(() -> new IllegalStateException(String.format("internal error: no member role set (%s)", getModel().getName())));
				installEditPolicy(EditPolicy.COMPONENT_ROLE, new RemoveMemberFromSetEditPolicy(memberRole, false));
			}
			// a record cannot be deleted when pressing the delete key under a set's owner record
		} else {
			// the next edit policy allows for the deletion of a record
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new RecordComponentEditPolicy());
		}
	}
	
	private SchemaRecord findRecord(ModelChangeContext context) {
		var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
		return getModel().getSchema().getRecord(recordName);
	}
	
	private Set findSet(ModelChangeContext context) {
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		return getModel().getSchema().getSet(setName);
	}
	
	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] { Set.class };
	}
	
	@Override
	protected String getImagePath() {
		var parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Set set) {
			if (set.getOwner() != null && set.getOwner().getRecord() == getModel()) {
				return "icons/owner_record.gif";
			} else {
				return "icons/member_record.gif";
			}
		} else if (parentModelObject instanceof SystemOwner || parentModelObject instanceof VsamIndex) {
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
		var children = new ArrayList<Object>();
		
		var parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// only when the record is at the top level, add the sets in which the record participates, in alphabetical order
			
			// gather the list of sets and sort it
			var sets = Stream.concat(getModel().getOwnerRoles().stream().map(OwnerRole::getSet), getModel().getMemberRoles().stream().map(MemberRole::getSet))
					.sorted(comparing(Set::getName, CASE_INSENSITIVE_ORDER))
					.toList();
			for (var set : sets) {
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
						// add the set
						set.getMembers().stream()
								.filter(memberRole -> memberRole.getRecord() == getModel())
								.map(MemberRole::getSet)
								.forEach(children::add);
					}
				}
			}
		}
		return children;
	}

	@Override
	protected WrappedNodeTextProvider getNodeTextProvider() {
		return new WrappedNodeTextProvider(getModel());
	}
	
	private boolean isMemberOf(Set set) {
		return set.getMembers().stream()
				.anyMatch(memberRole -> memberRole.getRecord() == getModel());
	}	
	
	private boolean isOwnerOf(Set set) {
		return set.getOwner().getRecord() == getModel();
	}
	
	@Override
	protected void registerModel() {
		// different edit parts exist for the same record; make sure that selecting a record in the SchemaEditor
		// yields the outline view's top level record to become the current selection
		var parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// the model object is the key in the edit part registry; this is what we want so that selecting a
			// record in the SchemaEditor selects the top level record edit part in the outline view
			super.registerModel();
		} else {
			// assure that record edit parts that are not at the top level will never be found by their model
			// object; create an artificial key to make this happen
			var key = new EditPartRegistryKey<>(getModel());
			getViewer().getEditPartRegistry().put(key, this);
		}
	}

}
