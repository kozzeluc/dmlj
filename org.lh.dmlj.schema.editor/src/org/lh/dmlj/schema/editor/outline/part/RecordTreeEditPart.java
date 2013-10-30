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
package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.common.Tools;

public class RecordTreeEditPart extends AbstractSchemaTreeEditPart<SchemaRecord> {

	public RecordTreeEditPart(SchemaRecord record, CommandStack commandStack) {
		super(record, commandStack);
	}
	
	@Override
	protected List<?> getModelChildren() {
		
		List<Object> children = new ArrayList<>();
		
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			
			// only when the record is at the top level, add the sets in which the record 
			// participates, in alphabetical order
			
			// gather the list of sets and sort it
			List<Set> sets = new ArrayList<>();
			for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
				sets.add(ownerRole.getSet());
			}
			for (MemberRole memberRole : getModel().getMemberRoles()) {
				sets.add(memberRole.getSet());
			}			
			Collections.sort(sets);
			
			for (Set set : sets) {
				if (set.getSystemOwner() != null) {
					// the record is the member of a system owned indexed set; add the system owner
					children.add(set.getSystemOwner());
				} else if (set.getOwner().getRecord() == getModel()) {
					// the record is the owner of the set; add the first connection part of the
					// first member
					children.add(set.getMembers().get(0).getConnectionParts().get(0));
				} else {
					// the record is a member of the set; add the first connection part of the
					// member role
					for (MemberRole memberRole : set.getMembers()) {
						if (memberRole.getRecord() == getModel()) {
							children.add(memberRole.getConnectionParts().get(0));
						}
					}
				}
			}
		}
		
		return children;
		
	}

	@Override
	protected String getImagePath() {
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof ConnectionPart) {
			Set set = ((ConnectionPart) parentModelObject).getMemberRole().getSet();
			if (set.getOwner() != null && set.getOwner().getRecord() == getModel()) {
				return "icons/owner_record.gif";
			} else {
				return "icons/member_record.gif";
			}
		} else if (parentModelObject instanceof SystemOwner) {
			return "icons/member_record.gif";
		} else {
			return "icons/record.gif";
		}
	}

	@Override
	protected String getNodeText() {
		return Tools.removeTrailingUnderscore(getModel().getName());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void registerModel() {
		// different edit parts exist for the same record; make sure that selecting a record in the
		// SchemaEditor yields the outline view's top level record to become the current selection
		EObject parentModelObject = getParentModelObject();
		if (parentModelObject instanceof Schema) {
			// the model object is the key in the edit part registry; this is what we want so that
			// selecting a record in the SchemaEditor selects the top level record edit part in the
			// outline view
			super.registerModel(); 
		} else {
			// assure that record edit parts that are not at the top level will never be found
			// by their model object; create an artificial key to make this happen
			EditPartRegistryKey<SchemaRecord> key = new EditPartRegistryKey<>(getModel());
			getViewer().getEditPartRegistry().put(key, this);
		}		
		
		
	}

}
