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
package org.lh.dmlj.schema.editor.dictionary.tools._import.tool;

import java.util.Collection;
import java.util.Properties;

import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;

public class SchemaImportTool implements ISchemaImportTool {

	public SchemaImportTool() {
		super();
	}
	
	@Override
	public void dispose() {	
	}

	@Override
	public Collection<?> getAreaContexts() {
		return null;
	}

	@Override
	public Collection<?> getRecordContexts() {
		return null;
	}

	@Override
	public Collection<?> getRootElementContexts(Object recordContext) {
		return null;
	}

	@Override
	public Collection<?> getSetContexts() {
		return null;
	}

	@Override
	public <T> Collection<T> getSubordinateElementContexts(T elementContext) {
		return null;
	}

	@Override
	public void init(IDataEntryContext dataEntryContext, Properties parameters,
					 IDataCollectorRegistry dataCollectorRegistry) {
	}

}
