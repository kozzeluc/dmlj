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
package org.lh.dmlj.schema.editor.common;

public class ValidationResult {
	
	public static enum Status {OK, ERROR};
	
	private String message;
	private Status status;

	@SuppressWarnings("unused")
	private ValidationResult() {
		// disabled constructor
		super();
	}
	
	ValidationResult(Status status) {
		this(status, null);
	}
	
	ValidationResult(Status status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Status getStatus() {
		return status;
	}
	
}
