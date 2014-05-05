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

import org.lh.dmlj.schema.Role;

public abstract class AbstractRemovableRole<T extends Role> {

	protected T  	role;
	private boolean removed = false;
	
	protected AbstractRemovableRole(T role) {
		super();
		this.role = role;
	}
	
	public final void remove() {				
		assertNotRemoved();
		removeData();
		markAsRemoved();
	}	
	
	private void assertNotRemoved() {
		if (removed) {
			throw new UnsupportedOperationException("already removed");
		}
	}

	protected abstract void removeData();

	private void markAsRemoved() {
		removed = true;
	}
	
	public final void restore() {		
		assertRemoved();
		restoreData();
		markAsNotRemoved();
	}	

	private void assertRemoved() {
		if (!removed) {
			throw new UnsupportedOperationException("not removed");
		}
	}

	protected abstract void restoreData();

	private void markAsNotRemoved() {
		removed = false;
	}	
	
}
