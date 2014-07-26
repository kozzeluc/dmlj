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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPolicy;
import org.lh.dmlj.schema.INodeTextProvider;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.infrastructure.IModelChangeProvider;
import org.lh.dmlj.schema.editor.policy.IndexComponentEditPolicy;

public class IndexTreeEditPart extends AbstractSchemaTreeEditPart<SystemOwner> {

	public IndexTreeEditPart(SystemOwner systemOwner, IModelChangeProvider modelChangeProvider) {
		super(systemOwner, modelChangeProvider);
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
	protected void createEditPolicies() {			
		// the next edit policy allows for the deletion of an index
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new IndexComponentEditPolicy());		
	}
	
	@Override
	protected Class<?>[] getChildNodeTextProviderOrder() {
		return new Class<?>[] {SchemaRecord.class};
	}
	
	@Override
	protected String getImagePath() {
		if (getParentModelObject() instanceof SchemaRecord) {
			// we want the parent record's (member) role to be visible in the set image
			return "icons/index_member.gif";
		} else {
			return "icons/index.gif";
		}
	}
	
	@Override
	public List<?> getModelChildren() {
		
		List<SchemaRecord> children = new ArrayList<>();
		
		// add the member record
		children.add(getModel().getSet().getMembers().get(0).getRecord());
		
		return children;
		
	}

	@Override
	protected INodeTextProvider<Set> getNodeTextProvider() {
		return getModel().getSet();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void registerModel() {
		// different edit parts exist for the same index; make sure that selecting an index in the
		// SchemaEditor yields the outline view's top level index to become the current selection
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// the model object is the key in the edit part registry; this is what we want so that
			// selecting an index in the SchemaEditor selects the top level index edit part in the
			// outline view
			super.registerModel(); 
		} else {
			// assure that index edit parts that are not at the top level will never be found
			// by their model object; create an artificial key to make this happen
			EditPartRegistryKey<SystemOwner> key = new EditPartRegistryKey<>(getModel());
			getViewer().getEditPartRegistry().put(key, this);
		}		
		
		
	}	

}
