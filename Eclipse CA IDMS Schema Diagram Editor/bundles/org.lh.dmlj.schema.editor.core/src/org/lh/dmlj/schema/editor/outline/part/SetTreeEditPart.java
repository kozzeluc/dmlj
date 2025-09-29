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

import static java.util.Comparator.comparing;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.command.infrastructure.CommandExecutionMode;
import org.lh.dmlj.schema.editor.command.infrastructure.ContextDataKeys;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeContext;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeType;
import org.lh.dmlj.schema.editor.policy.DeleteSetEditPolicy;
import org.lh.dmlj.schema.editor.policy.RemoveMemberFromSetEditPolicy;

public class SetTreeEditPart extends AbstractSchemaTreeEditPart<Set> {

	protected SetTreeEditPart(Set set, IModelChangeProvider modelChangeProvider) {		
		super(set, modelChangeProvider);
	}
	
	@Override
	public void afterModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) && appliesToModelSet(context)) {
			// the set name has changed (execute/undo/redo)... the order of the parent edit part might become
			// disrupted, so we have to inform that edit part of this fact
			nodeTextChanged();						
		} else if (appliesToMemberOfModelSet(context)) {
			if (context.getCommandExecutionMode() != CommandExecutionMode.UNDO) {
				if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {	
					createAndAddRecordAsChild(context);
				} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {	
					findAndRemoveRecordAsChild(context);
				}			
			} else {
				if (context.getModelChangeType() == ModelChangeType.ADD_MEMBER_TO_SET) {				
					findAndRemoveRecordAsChild(context);
				} else if (context.getModelChangeType() == ModelChangeType.REMOVE_MEMBER_FROM_SET) {
					createAndAddRecordAsChild(context);
				}
			}
		}
	}
	
	private boolean appliesToMemberOfModelSet(ModelChangeContext context) {
		var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
		return getModel().getName().equals(setName);
	}
	
	private boolean appliesToModelSet(ModelChangeContext context) {
		if (Boolean.TRUE.equals(context.getListenerData())) {
			return true;
		} else {
			var setName = context.getContextData().get(ContextDataKeys.SET_NAME);
			return getModel().getName().equals(setName);
		}
	}	
	
	@Override
	public void beforeModelChange(ModelChangeContext context) {
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && context.isPropertySet(SchemaPackage.eINSTANCE.getSet_Name()) &&
			context.getCommandExecutionMode() != CommandExecutionMode.UNDO && context.appliesTo(getModel())) {
					
			// the model set's name is changing (execute/redo); put Boolean.TRUE in the context's listener's data
			// so that we can respond to this when processing the after model change event
			context.setListenerData(Boolean.TRUE);
		}
	}
	
	private void createAndAddRecordAsChild(ModelChangeContext context) {
		var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
		var schemaRecord = getModel().getSchema().getRecord(recordName);
		createAndAddChild(schemaRecord);
	}

	@Override
	protected void createEditPolicies() {
		if (isReadOnlyMode()) {
			return;
		}
		if (getParentModelObject() instanceof Schema) {
			// the next edit policy allows for the deletion of a set
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteSetEditPolicy(getModel()));
		} else if (getParentModelObject() instanceof SchemaRecord schemaRecord && isMemberofModelSet(schemaRecord)) {
			// if the parent model object is a member in the model set, install an edit policy that allows for
			// the removal of that record as a set member, provided it is not the last member in that set (in
			// other words, we don't want the whole set to be deleted, just this 1 member record removed)
			var memberRole = getMemberRole((SchemaRecord) getParentModelObject());
			installEditPolicy(EditPolicy.COMPONENT_ROLE, new RemoveMemberFromSetEditPolicy(memberRole, false));
		}
	}
	
	private void findAndRemoveRecordAsChild(ModelChangeContext context) {
		var recordName = context.getContextData().get(ContextDataKeys.RECORD_NAME);
		var schemaRecord = getModel().getSchema().getRecord(recordName);
		findAndRemoveChild(schemaRecord, false);
	}
	
	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] { SchemaRecord.class };
	}
	
	@Override
	protected String getImagePath() {
		if (getModel().getMode() == SetMode.CHAINED) {
			if (getModel().getMembers().size() > 1) {
				return getImagePathForMultipleMemberSet();
			} else {
				return getImagePathForChainedSet();
			}
		} else {
			return getImagePathForIndexedSet();
		}
	}
	
	private String getImagePathForMultipleMemberSet() {
		if (getParentModelObject() instanceof SchemaRecord schemaRecord) {
			if (schemaRecord.getRole(getModel().getName()) instanceof OwnerRole) {						
				return "icons/multiple_member_set_owner.gif";
			} else {
				return "icons/multiple_member_set_member.gif";
			}
		} else {
			return "icons/multiple_member_set.gif";
		}
	}
	
	private String getImagePathForChainedSet() {
		if (getParentModelObject() instanceof SchemaRecord schemaRecord) {
			// we want the parent record's role to be visible in the set image
			if (schemaRecord.getRole(getModel().getName()) instanceof OwnerRole) {						
				return "icons/chained_set_owner.gif";
			} else {
				return "icons/chained_set_member.gif";
			}
		} else {
			return "icons/chained_set.gif";
		}
	}
	
	private String getImagePathForIndexedSet() {
		if (getParentModelObject() instanceof SchemaRecord schemaRecord) {
			// we want the parent record's role to be visible in the set image
			if (schemaRecord.getRole(getModel().getName()) instanceof OwnerRole) {
				return "icons/indexed_set_owner.gif";
			} else {
				return "icons/indexed_set_member.gif";
			}
		} else {			
			return "icons/indexed_set.gif";
		}
	}

	private MemberRole getMemberRole(SchemaRecord schemaRecord) {
		return getModel().getMembers().stream()
				.filter(memberRole -> memberRole.getRecord() == schemaRecord)
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<?> getModelChildren() {
		return Stream.concat(Stream.of(getModel().getOwner().getRecord()),
							 getModel().getMembers().stream().map(MemberRole::getRecord))
				.sorted(comparing(SchemaRecord::getName, String.CASE_INSENSITIVE_ORDER))
				.toList();
	}

	@Override
	protected WrappedNodeTextProvider getNodeTextProvider() {
		return new WrappedNodeTextProvider(getModel());
	}

	private boolean isMemberofModelSet(SchemaRecord schemaRecord) {
		return getMemberRole(schemaRecord) != null;
	}
	
	@Override
	protected void registerModel() {
		// different edit parts exist for the same set; make sure that selecting a set in the SchemaEditor yields
		// the outline view's top level set to become the current selection
		var parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// the model object is the key in the edit part registry; this is what we want so that selecting a
			// set in the SchemaEditor selects the top level set edit part in the outline view
			super.registerModel(); 
		} else {
			// assure that set edit parts that are not at the top level will never be found by their model
			// object; create an artificial key to make this happen
			var key = new EditPartRegistryKey<>(getModel());
			getViewer().getEditPartRegistry().put(key, this);
		}
	}

}
