package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.model.SetDescription;

public class SystemOwnedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof MemberRole) {
        	MemberRole memberRole = (MemberRole) modelObject;
        	return memberRole.getSet().getSystemOwner() != null;
        } else if (modelObject instanceof SetDescription) {
        	MemberRole memberRole = 
        		((SetDescription)modelObject).getMemberRole();
        	return memberRole.getSet().getSystemOwner() != null;
        } else if (modelObject instanceof SystemOwner) {
        	return true;
        } else {
        	return false;
        }        
	}

}