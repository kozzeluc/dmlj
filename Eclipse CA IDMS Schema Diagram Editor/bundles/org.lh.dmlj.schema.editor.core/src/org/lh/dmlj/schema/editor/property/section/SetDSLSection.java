/**
 * Copyright (C) 2016  Luc Hermans
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

import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SetSyntaxBuilder;

public class SetDSLSection extends AbstractSectionWithStyledText {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS =
		new Class[] {ConnectionLabel.class, ConnectionPart.class, 
					 Connector.class, SystemOwner.class, Set.class, VsamIndex.class};

	public SetDSLSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS);
	}

	@Override
	protected String getValue(Object editPartModelObject) {
		Set set;
		if (editPartModelObject instanceof ConnectionPart) {
			MemberRole memberRole = ((ConnectionPart) editPartModelObject).getMemberRole();
			set = memberRole.getSet();
		} else if (editPartModelObject instanceof ConnectionLabel) {
			ConnectionLabel connectionLabel = (ConnectionLabel) editPartModelObject;
			set = connectionLabel.getMemberRole().getSet();
		} else if (editPartModelObject instanceof Connector) {
			Connector connector = (Connector) editPartModelObject;
			set = connector.getConnectionPart().getMemberRole().getSet();
		} else if (editPartModelObject instanceof Set) {
			set = (Set) editPartModelObject;						
		} else if (editPartModelObject instanceof VsamIndex) {
			VsamIndex vsamIndex = (VsamIndex) editPartModelObject;
			set = vsamIndex.getSet();
		} else {
			SystemOwner systemOwner = (SystemOwner) editPartModelObject;
			set = systemOwner.getSet();
		}
		return new SetSyntaxBuilder().build(set);		
	}

}
