package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMode;

public class UserOwnedIndexedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();
        MemberRole memberRole;
        if (modelObject instanceof ConnectionPart) {
        	memberRole = ((ConnectionPart)modelObject).getMemberRole();
        } else if (modelObject instanceof ConnectionLabel) {
        	memberRole = ((ConnectionLabel)modelObject).getMemberRole();
        } else if (modelObject instanceof Connector) {
        	memberRole = ((Connector)modelObject).getConnectionPart()
        										 .getMemberRole();
        } else {
        	return false;
        }
        return memberRole.getSet().getMode() == SetMode.INDEXED &&
        	   memberRole.getSet().getOwner() != null;
	}

}