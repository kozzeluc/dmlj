package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMode;

public class ChainedSetFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof ConnectionPart) {
        	MemberRole memberRole =	
        		((ConnectionPart)modelObject).getMemberRole();
        	return memberRole.getSet().getMode() == SetMode.CHAINED;
        } else if (modelObject instanceof ConnectionLabel) {
        	MemberRole memberRole = 
        		((ConnectionLabel)modelObject).getMemberRole();
        	return memberRole.getSet().getMode() == SetMode.CHAINED;
        } else if (modelObject instanceof Connector) {
        	MemberRole memberRole = 
            	((Connector)modelObject).getConnectionPart().getMemberRole();
            return memberRole.getSet().getMode() == SetMode.CHAINED;
        } else {
        	return false;
        }        
	}

}