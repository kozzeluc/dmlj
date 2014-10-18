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
package org.lh.dmlj.schema.editor.dictionary.tools.jdbc;

import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;

public class SchemaImportSession extends ImportSession {
	
	private String schemaName;
	private int schemaVersion;

	public SchemaImportSession(Dictionary dictionary, String schemaName, int schemaVersion) {
		super(dictionary, "Import schema " + schemaName + " version " + schemaVersion + 
			  " from dictionary " + dictionary.getId());
		this.schemaName = schemaName;
		this.schemaVersion = schemaVersion;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public int getSchemaVersion() {
		return schemaVersion;
	}
	
	public boolean isIdmsntwkVersion1() {
		return schemaName.equals("IDMSNTWK") && schemaVersion == 1;
	}

}
