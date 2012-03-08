package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SystemOwner;

public class IndexedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof MemberRole) {
        	MemberRole memberRole = (MemberRole) modelObject;
        	return memberRole.getSet().getMode() == SetMode.INDEXED;
        } else if (modelObject instanceof ConnectionLabel) {
        	MemberRole memberRole = 
        		((ConnectionLabel)modelObject).getMemberRole();
        	return memberRole.getSet().getMode() == SetMode.INDEXED;
        } else if (modelObject instanceof SystemOwner) {
        	return true;
        } else {
        	return false;
        }
	}

}