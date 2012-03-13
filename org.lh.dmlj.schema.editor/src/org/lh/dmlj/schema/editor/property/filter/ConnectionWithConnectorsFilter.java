package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SystemOwner;

public class ConnectionWithConnectorsFilter implements IFilter {

	@Override
	public boolean select(Object object) {
		if (!(object instanceof EditPart)) {
			return false;
		}
        Object modelObject = ((EditPart) object).getModel();        
        if (modelObject instanceof ConnectionLabel) {
        	ConnectionLabel connectionLabel = (ConnectionLabel) modelObject;
        	int connectionPartCount = 
        		connectionLabel.getMemberRole().getConnectionParts().size(); 
        	return connectionPartCount > 1;
        } else if (modelObject instanceof ConnectionPart) {
            ConnectionPart connectionPart = (ConnectionPart) modelObject;
            int connectionPartCount = 
            	connectionPart.getMemberRole().getConnectionParts().size(); 
            return connectionPartCount > 1;
        } else if (modelObject instanceof Connector) {
            Connector connector = (Connector) modelObject;
            int connectionPartCount = connector.getConnectionPart()
            			 					   .getMemberRole()
            			 					   .getConnectionParts().size(); 
            return connectionPartCount > 1;
        } else if (modelObject instanceof SystemOwner) {
            SystemOwner systemOwner = (SystemOwner) modelObject;
            int connectionPartCount = systemOwner.getSet()
            			   						 .getMembers()
            			   						 .get(0)
            			   						 .getConnectionParts()
            			   						 .size(); 
            return connectionPartCount > 1;
        } else {
        	return false;
        }        
	}

}