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
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;

public class RecordTreeEditPart extends AbstractSchemaTreeEditPart<SchemaRecord> {

	public RecordTreeEditPart(SchemaRecord record, IModelChangeProvider modelChangeProvider) {
		super(record, modelChangeProvider);
	}
	
	@Override
	public void afterAddItem(EObject owner, EReference reference, Object item) {
		
		EObject parentModelObject = getParentModelObject();		
		if (parentModelObject instanceof Schema && owner == getModel().getSchema() && 
			reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {
			
			// a set was added; if the model record is the owner or member of that set, we need to
			// add a child for the new set but only if the record is at the top level
			Set set = (Set) item;
			boolean addChild = false;
			if (set.getOwner() != null && set.getOwner().getRecord() == getModel()) {
				// the model record is the owner of the new set
				addChild = true;
			} else {
				// traverse all set members and see if the model record is one of them
				for (MemberRole memberRole : set.getMembers()) {
					if (memberRole.getRecord() == getModel()) {
						addChild = true;
						break;
					}					
				}
			}
			if (addChild) {
				
				// create an edit part for the new set; the model object passed to the edit part
				// factory is always the first connection part for the first member in the case of
				// a user owned set, and the system owner in the case of a system owned indexed set
				EObject model = set.getSystemOwner() != null ? set.getSystemOwner() :
								set.getMembers().get(0).getConnectionParts().get(0);
				EditPart child = 
					SchemaTreeEditPartFactory.createEditPart(model, modelChangeProvider);
				
				// calculate the insertion index
				int index = getInsertionIndex(getChildren(), set, getChildNodeTextProviderOrder());
				
				// add the child at the appropriate position
				addChild(child, index);
				
			}
			
		} else if (reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a member record type was added to (what is now) a multiple-member set (if it wasn't 
			// one already); if the member role refers to the model record, we need to make sure 
			// that the set becomes visible as a child
			MemberRole memberRole = (MemberRole) item;
			if (memberRole.getRecord() == getModel()) {
				
				// create an edit part for the set	
				EditPart child = 
					SchemaTreeEditPartFactory.createEditPart(memberRole.getConnectionParts().get(0), 
															 modelChangeProvider);
					
				// calculate the insertion index
				int index = getInsertionIndex(getChildren(), memberRole.getSet(),
											  getChildNodeTextProviderOrder());
					
				// add the child at the appropriate position
				addChild(child, index);			
				
			}
			
		}
		
	}
	
	@Override
	public void afterRemoveItem(EObject owner, EReference reference, Object item) {
		
		EObject parentModelObject = getParentModelObject();		
		if (parentModelObject instanceof Schema && owner == getModel().getSchema() && 
			reference == SchemaPackage.eINSTANCE.getSchema_Sets()) {
			
			// a set was removed; if the model record is the owner or member of that set, we need to
			// remove the child edit part belonging to the removed set; we cannot use the item but
			// will need to traverse the child edit parts for this matter - we only need to do this
			// when we're at the top level
			String setName = ((Set) item).getName();
			
			// traverse the child edit parts and see if the removed set is one of them; if so,
			// remove the child edit part for the set - mind that removing a set in the DDLCATLOD
			// area will fail, but it is unlikely that anyone would like to delete a set there
			for (Object child : getChildren()) {
				AbstractSchemaTreeEditPart<?> cChild = (AbstractSchemaTreeEditPart<?>) child;
				if (cChild.getModel() instanceof ConnectionPart) {
					// user owned set
					ConnectionPart model = (ConnectionPart) cChild.getModel();
					if (model.getMemberRole().getSet().getName().equals(setName)) {
						removeChild(cChild);
						break;
					}
				} else if (cChild.getModel() instanceof SystemOwner) {
					// system owned set
					SystemOwner model = (SystemOwner) cChild.getModel();
					if (model.getSet().getName().equals(setName)) {
						removeChild(cChild);
						break;
					}
				}
			}
			
		} else if (reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a member record type was removed from the set; because the member role still refers
			// to its connection part(s), it's easy to remove the child edit part for the (first)
			// connection part
			MemberRole memberRole = (MemberRole) item;
			try {
				EditPart child = childFor(memberRole.getConnectionParts().get(0));
				// the model record was a member of the set; remove the connection child edit part
				removeChild(child);				
			} catch (IllegalArgumentException e) {
				// the model record wasn't a member of the set; ignore this condition				
			}
			
		}
			
		
	}
	
	@Override
	public void afterSetFeatures(EObject owner, EStructuralFeature[] features) {
		if (owner == getNodeTextProvider() && 
			isFeatureSet(features, SchemaPackage.eINSTANCE.getSchemaRecord_Name())) {
			
			// the record name has changed... the order of the parent edit part might become 
			// disrupted, so we have to inform that edit part of this fact
			nodeTextChanged();						
		}
	}
	
	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] {Set.class};
	}
	
	@Override
	protected String getImagePath() {
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof ConnectionPart) {
			Set set = ((ConnectionPart) parentModelObject).getMemberRole().getSet();
			if (set.getOwner() != null && set.getOwner().getRecord() == getModel()) {
				return "icons/owner_record.gif";
			} else {
				return "icons/member_record.gif";
			}
		} else if (parentModelObject instanceof SystemOwner) {
			return "icons/member_record.gif";
		} else {
			return "icons/record.gif";
		}
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
				} else if (set.getOwner().getRecord() == getModel()) {
					// the record is the owner of the set; add the first connection part of the
					// first member
					children.add(set.getMembers().get(0).getConnectionParts().get(0));
				} else {
					// the record is a member of the set; add the first connection part of the
					// member role
					for (MemberRole memberRole : set.getMembers()) {
						if (memberRole.getRecord() == getModel()) {
							children.add(memberRole.getConnectionParts().get(0));
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
