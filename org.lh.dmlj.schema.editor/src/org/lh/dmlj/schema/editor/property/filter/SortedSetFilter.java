package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.model.SetDescription;

public class SortedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();
        Set set;
        if (modelObject instanceof MemberRole) {
        	MemberRole memberRole = (MemberRole) modelObject;
        	set = memberRole.getSet();
        } else if (modelObject instanceof SetDescription) {
        	MemberRole memberRole = 
        		((SetDescription)modelObject).getMemberRole();
        	set = memberRole.getSet();
        } else if (modelObject instanceof SystemOwner) {
        	SystemOwner systemOwner = (SystemOwner) modelObject;
        	set = systemOwner.getSet();
        } else {
        	return false;
        }
        return set.getOrder() == SetOrder.SORTED;
	}

}