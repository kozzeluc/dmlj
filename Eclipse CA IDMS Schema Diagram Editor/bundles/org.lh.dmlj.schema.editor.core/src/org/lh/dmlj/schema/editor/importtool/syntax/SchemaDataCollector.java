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
package org.lh.dmlj.schema.editor.importtool.syntax;

import java.util.List;

import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;

/**
 * This type is not a real data collector but merely a data object; all data
 * are passed to objects of this type upon construction.
 */
public class SchemaDataCollector implements ISchemaDataCollector {

	private List<String> comments;
	private String 		 memoDate;
	private String 		 schemaDescription;
	
	public SchemaDataCollector(String schemaDescription, String memoDate,
							   List<String> comments) {
		super();
		this.schemaDescription = schemaDescription;
		this.memoDate = memoDate;
		this.comments = comments;
	}

	@Override
	public List<String> getComments() {
		return comments;
	}

	@Override
	public String getSchemaDescription() {
		return schemaDescription;
	}

	@Override
	public String getSchemaMemoDate() {
		return memoDate;
	}

}
