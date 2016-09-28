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
package org.lh.dmlj.schema.editor.command

import org.lh.dmlj.schema.Schema

class ChangeSchemaCommentsCommand extends ModelChangeBasicCommand {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private Schema schema
	private String newValue
			
	private List<String> oldLines
	private List<String> newLines
	
	ChangeSchemaCommentsCommand(Schema schema, String newValue) {
		super('Set schema comments')
		this.schema = schema
		this.newValue = newValue
	}
	
	@Override
	void execute() {
		assert schema, 'schema is null'
		assert newValue != null, 'newValue is null'
		oldLines = new ArrayList<>(schema.comments)
		newLines = newValue ? newValue.split(LINE_SEPARATOR) : [ ]		
		redo()	
	}
	
	@Override
	void undo() {
		schema.comments.clear()
		schema.comments.addAll(oldLines)
	}
	
	@Override
	void redo() {
		schema.comments.clear()
		schema.comments.addAll(newLines)
	}
	
}
