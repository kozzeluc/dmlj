/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.property;

import org.eclipse.gef.EditPart;
import org.eclipse.ui.views.properties.tabbed.AbstractTypeMapper;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SystemOwner;

public class TypeMapper extends AbstractTypeMapper {

	@Override
	public Class<?> mapType(Object object) {
		if (object instanceof EditPart) {
			EditPart editPart = (EditPart) object;
			Object modelObject = editPart.getModel();            
            if (modelObject instanceof ConnectionPart ||
            	modelObject instanceof ConnectionLabel ||
            	modelObject instanceof SystemOwner ||
            	modelObject instanceof Connector) {
            	
            	return MemberRole.class;
            } else { 
            	return editPart.getModel().getClass();
            }
        }
        return super.mapType(object);
	}

}
