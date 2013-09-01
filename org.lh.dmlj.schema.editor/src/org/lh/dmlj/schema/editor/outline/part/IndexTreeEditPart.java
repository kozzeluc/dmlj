package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

public class IndexTreeEditPart extends AbstractSchemaTreeEditPart<SystemOwner> {

	public IndexTreeEditPart(SystemOwner systemOwner, CommandStack commandStack) {
		super(systemOwner, commandStack);
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
	protected String getNodeText() {
		return getModel().getSet().getName();
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