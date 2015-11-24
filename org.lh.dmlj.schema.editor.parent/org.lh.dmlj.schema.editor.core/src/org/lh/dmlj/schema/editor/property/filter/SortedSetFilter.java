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
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;

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
        } else if (modelObject instanceof VsamIndex) {
        	return true;
        } else {
        	return false;
        }
        return set.getOrder() == SetOrder.SORTED;
	}

}
