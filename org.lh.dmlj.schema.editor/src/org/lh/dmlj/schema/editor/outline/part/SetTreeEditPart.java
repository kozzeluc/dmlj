/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;

public class SetTreeEditPart extends AbstractSchemaTreeEditPart<ConnectionPart> {

	protected SetTreeEditPart(ConnectionPart connectionPart, 
							  IModelChangeProvider modelChangeProvider) {
		
		super(connectionPart, modelChangeProvider);
	}
	
	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
		if (owner == getNodeTextProvider() && 
			reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a member record type was added to the set
			SchemaRecord memberRecord = ((MemberRole) item).getRecord();
			EditPart child = 
				SchemaTreeEditPartFactory.createEditPart(memberRecord, modelChangeProvider);					
			int index = 
				getInsertionIndex(getChildren(), memberRecord, getChildNodeTextProviderOrder());					
			addChild(child, index);			
		}
	}
	
	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
		if (owner == getNodeTextProvider() && 
			reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a member record type was removed from the set; although the item is set to the member
			// role involved, the reference to the record itself is lost, so we need to figure out
			// ourselves which member record was removed... or just refresh the children:
			refreshChildren();
		}
	}
	
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
		if (owner == getNodeTextProvider() && 
			isFeatureSet(features, SchemaPackage.eINSTANCE.getSet_Name())) {
			
			// the set name has changed... the order of the parent edit part might become disrupted, 
			// so we have to inform that edit part of this fact
			nodeTextChanged();						
		}
	}	
	
	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] {SchemaRecord.class};
	}
	
	@Override
	protected String getImagePath() {
		String setName = getModel().getMemberRole().getSet().getName();
		if (getModel().getMemberRole().getSet().getMode() == SetMode.CHAINED) {			
			// chained set
			if (getParentModelObject() instanceof SchemaRecord) {
				// we want the parent record's role to be visible in the set image
				SchemaRecord record = (SchemaRecord) getParentModelObject();
				if (getModel().getMemberRole().getSet().getMembers().size() > 1) {
					// multiple member set
					if (record.getRole(setName) instanceof OwnerRole) {		
						// owner
						return "icons/multiple_member_set_owner.gif";
					} else {
						// member
						return "icons/multiple_member_set_member.gif";
					}
				} else {
					// single member set
					if (record.getRole(setName) instanceof OwnerRole) {		
						// owner
						return "icons/chained_set_owner.gif";
					} else {
						// member
						return "icons/chained_set_member.gif";
					}
				}
			} else {
				if (getModel().getMemberRole().getSet().getMembers().size() > 1) {
					// multiple member set
					return "icons/multiple_member_set.gif";
				} else {
					// single member set
					return "icons/chained_set.gif";
				}
			}
		} else {
			// indexed set
			if (getParentModelObject() instanceof SchemaRecord) {
				// we want the parent record's role to be visible in the set image
				SchemaRecord record = (SchemaRecord) getParentModelObject();
				if (record.getRole(setName) instanceof OwnerRole) {
					// owner
					return "icons/indexed_set_owner.gif";
				} else {
					// member
					return "icons/indexed_set_member.gif";
				}
			} else {			
				return "icons/indexed_set.gif";
			}
		}
	}

	@Override
	public List<?> getModelChildren() {
		
		List<SchemaRecord> children = new ArrayList<>();
		
		// add the owner record
		children.add(getModel().getMemberRole().getSet().getOwner().getRecord());
		
		// add the member records
		List<SchemaRecord> memberRecords = new ArrayList<>();
		for (MemberRole memberRole : getModel().getMemberRole().getSet().getMembers()) {
			memberRecords.add(memberRole.getRecord());
		}
		children.addAll(memberRecords);
		
		// sort the list of children
		Collections.sort(children);
		
		return children;
		
	}

	@Override
	protected INodeTextProvider<Set> getNodeTextProvider() {
		return getModel().getMemberRole().getSet();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void registerModel() {
		// different edit parts exist for the same set; make sure that selecting a set in the
		// SchemaEditor yields the outline view's top level set to become the current selection
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// the model object is the key in the edit part registry; this is what we want so that
			// selecting a set in the SchemaEditor selects the top level set edit part in the
			// outline view
			super.registerModel(); 
		} else {
			// assure that set edit parts that are not at the top level will never be found
			// by their model object; create an artificial key to make this happen
			EditPartRegistryKey<ConnectionPart> key = new EditPartRegistryKey<>(getModel());
			getViewer().getEditPartRegistry().put(key, this);
		}		
		
		
	}

}
