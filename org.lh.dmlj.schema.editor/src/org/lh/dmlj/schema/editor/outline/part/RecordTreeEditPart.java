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
package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.policy.RemoveMemberFromSetEditPolicy;

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
				EObject model = set.getSystemOwner() != null ? set.getSystemOwner() : set;
				EditPart child = 
					SchemaTreeEditPartFactory.createEditPart(model, modelChangeProvider);
				
				// calculate the insertion index
				int index = getInsertionIndex(getChildren(), set, getChildNodeTextProviderOrder());
				
				// add the child at the appropriate position
				addChild(child, index);
				
			}
			
		} else if (parentModelObject instanceof Schema &&  
				   reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a member record type was added to (what is now definitely) a multiple-member set; if 
			// the member role refers to the model record, we need to make sure that the set becomes 
			// visible as a child, i.e. if that's not already the case
			MemberRole memberRole = (MemberRole) item;
			if (memberRole.getRecord() == getModel()) {				
				try {
					// see if a child already exists
					childFor(memberRole.getSet());
				} catch (IllegalArgumentException e) {
					// no: create an edit part for the set	
					EditPart child = SchemaTreeEditPartFactory.createEditPart(memberRole.getSet(), 
																 			  modelChangeProvider);						
					// calculate the insertion index
					int index = getInsertionIndex(getChildren(), memberRole.getSet(),
												  getChildNodeTextProviderOrder());						
					// add the child at the appropriate position
					addChild(child, index);								
				}
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
			Set set = (Set) item;			
			
			// traverse the child edit parts and see if the removed set is one of them; if so,
			// remove the child edit part for the set
			if (set.getSystemOwner() != null) {
				// SYSTEM OWNED
				try {
					// see if a child edit part exists for the system owner
					EditPart child = childFor(set.getSystemOwner());
					// yes: remove the child edit part
					removeChild(child);
				} catch (IllegalArgumentException e) {
					// no: the record is not a member of the system owned set
				}
			} else {
				// USER OWNED
				try {
					// see if a child edit part exists for the set
					EditPart child = childFor(set);
					// yes: remove the child edit part
					removeChild(child);
				} catch (IllegalArgumentException e) {
					// no: the record is not a member of the set
				}
			}
			
		} else if (reference == SchemaPackage.eINSTANCE.getSet_Members()) {
			
			// a member record type was removed from a multiple-member set;only do something when 
			// the set is NOT removed altogether AND THIS record type was a MEMBER record type of 
			// the given set
			Set set = (Set) owner;
			try {
				EditPart child = childFor(set);
				if (set.getSchema()!= null &&
					((Set) child.getModel()).getOwner().getRecord() != getModel() &&
					getModel().getRole(set.getName()) == null) {
					
					removeChild(child);	
				}
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
	protected void createEditPolicies() {		
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Set) {
			Set set = (Set) parentModelObject;
			if (set.getOwner().getRecord() != getModel()) {
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
		}
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
					// the record is the owner of the set; add the set
					children.add(set);
				} else {
					// the record is a member of the set; add the set
					for (MemberRole memberRole : set.getMembers()) {
						if (memberRole.getRecord() == getModel()) {
							children.add(memberRole.getSet());
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
