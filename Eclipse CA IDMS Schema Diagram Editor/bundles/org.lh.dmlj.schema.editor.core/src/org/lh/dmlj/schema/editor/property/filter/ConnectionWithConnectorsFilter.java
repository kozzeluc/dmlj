/**
 * Copyright (C) 2015  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.property.filter;

import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;

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
        } else if (modelObject instanceof VsamIndex) {
        	VsamIndex vsamIndex = (VsamIndex) modelObject;
        	int connectionPartCount = vsamIndex.getMemberRole().getConnectionParts().size();
        	return connectionPartCount > 1;
        } else {
        	return false;
        }        
	}

}
