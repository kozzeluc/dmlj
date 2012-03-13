package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;

public class SortedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();
        Set set;
        if (modelObject instanceof ConnectionPart) {
        	MemberRole memberRole = 
            	((ConnectionPart)modelObject).getMemberRole();
        	set = memberRole.getSet();
        } else if (modelObject instanceof ConnectionLabel) {
        	MemberRole memberRole = 
        		((ConnectionLabel)modelObject).getMemberRole();
        	set = memberRole.getSet();
        } else if (modelObject instanceof Connector) {
        	MemberRole memberRole = 
        		((Connector)modelObject).getConnectionPart().getMemberRole();
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