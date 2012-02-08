package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.model.SetDescription;

public class UserOwnedSetFilter implements IFilter {

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
        } else {
        	return false;
        }
        return memberRole.getSet().getOwner() != null;
	}

}