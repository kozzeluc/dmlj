/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.command.helper;

import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerRemoval;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;

public class RemovableOwnerRole extends AbstractRemovableRole<OwnerRole> {
	
	private SchemaRecord record;
	private int indexOfRoleInRecordsOwnerRoles;
	private PrefixForPointerRemoval prefix;
	
	private Set	set;
	
	public RemovableOwnerRole(OwnerRole role) {
		super(role);		
		rememberRecordData();
		rememberSetData();
	}
	
	private void rememberRecordData() {
		record = role.getRecord();
		indexOfRoleInRecordsOwnerRoles = record.getOwnerRoles().indexOf(role);
		PointerType[] definedPointerTypes = PrefixUtil.getDefinedPointerTypes(role);
		prefix = PrefixFactory.newPrefixForPointerRemoval(role, definedPointerTypes);
	}
	
	private void rememberSetData() {
		set = role.getSet();
	}
	
	protected void removeData() {		
		removeRecordData();
		removeSetData();		
	}
	
	private void removeRecordData() {
		prefix.removePointers();
		record.getOwnerRoles().remove(role);
	}
	
	private void removeSetData() {
		set.setOwner(null);
	}
	
	protected void restoreData() {		
		restoreRecordData();
		restoreSetData();		
	}
	
	private void restoreRecordData() {
		record.getOwnerRoles().add(indexOfRoleInRecordsOwnerRoles, role);
		prefix.reset();	
	}
	
	private void restoreSetData() {
		set.setOwner(role);
	}	
	
}
