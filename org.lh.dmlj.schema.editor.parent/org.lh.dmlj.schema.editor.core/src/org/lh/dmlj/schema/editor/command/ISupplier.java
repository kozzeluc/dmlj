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
package org.lh.dmlj.schema.editor.command;

/**
 * Implementations supply commands with an object that cannot be provided at command construction 
 * time (e.g. because it is not yet created); the first thing during command executing is invoking 
 * the supplier to obtain that object. 
 * 
 * @param <T> the type of the object that will be supplied
 */
public interface ISupplier<T> {
	/**
	 * Invoked as the first step during command execution.
	 * @return the object that cannot be supplied at command construction time
	 */
	T supply();
}
