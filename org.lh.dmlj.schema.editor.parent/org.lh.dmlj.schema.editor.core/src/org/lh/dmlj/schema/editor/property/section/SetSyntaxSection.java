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
package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.template.SetTemplate;

public class SetSyntaxSection extends AbstractSyntaxSection {

	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {ConnectionLabel.class, ConnectionPart.class, 
					 Connector.class, SystemOwner.class, Set.class, VsamIndex.class};
	
	public SetSyntaxSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS, new SetTemplate());				
	}
	
	@Override
	protected EObject getTemplateObject(Object editPartModelObject) {		
		// the template object is the edit part model object's set
		if (editPartModelObject instanceof ConnectionPart) {
			// connection part edit part model object
			MemberRole memberRole = 
				((ConnectionPart) editPartModelObject).getMemberRole();
			return memberRole.getSet();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			// connection label edit part model object
			ConnectionLabel connectionLabel = 
				(ConnectionLabel) editPartModelObject;
			return connectionLabel.getMemberRole().getSet();
		} else if (editPartModelObject instanceof Connector) {
			// connector edit part model object
			Connector connector = (Connector) editPartModelObject;
			return connector.getConnectionPart().getMemberRole().getSet();
		} else if (editPartModelObject instanceof Set) {
			// set edit part model object
			Set set = (Set) editPartModelObject;
			return set;						
		} else if (editPartModelObject instanceof VsamIndex) {
			// VSAM index edit part model object
			VsamIndex vsamIndex = (VsamIndex) editPartModelObject;
			return vsamIndex.getSet();
		} else {
			// system owner edit part model object
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			return systemOwner.getSet();
		}		
	}

	@Override
	protected Object[] getTemplateParametersOtherThanTemplateObject() {
		return new Object[] {Boolean.FALSE};
	}

}
