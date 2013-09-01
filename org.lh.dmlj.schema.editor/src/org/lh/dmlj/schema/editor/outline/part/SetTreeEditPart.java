package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.common.Tools;

public class SetTreeEditPart extends AbstractSchemaTreeEditPart<ConnectionPart> {

	protected SetTreeEditPart(ConnectionPart connectionPart, CommandStack commandStack) {
		super(connectionPart, commandStack);
	}
	
	@Override
	public List<?> getModelChildren() {
		
		List<SchemaRecord> children = new ArrayList<>();
		
		// add the owner record
		children.add(getModel().getMemberRole().getSet().getOwner().getRecord());
		
		// add the member
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
	protected String getNodeText() {
		return Tools.removeTrailingUnderscore(getModel().getMemberRole().getSet().getName());
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