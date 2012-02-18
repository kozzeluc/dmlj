package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.model.SetDescription;

public class SystemOwnedIndexedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();
        MemberRole memberRole;
        if (modelObject instanceof MemberRole) {
        	memberRole = (MemberRole) modelObject;
        } else if (modelObject instanceof SetDescription) {
        	memberRole = ((SetDescription)modelObject).getMemberRole();
        } else if (modelObject instanceof SystemOwner) {
        	return true;
        } else {
        	return false;
        }
        return memberRole.getSet().getMode() == SetMode.INDEXED &&
        	   memberRole.getSet().getOwner() == null;
	}

}