/**
 * Copyright (C) 2025  Luc Hermans
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.Schema;

public class ChangeSchemaCommentsCommand extends ModelChangeBasicCommand {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	private Schema schema;
	private String newValue;
			
	private List<String> oldLines;
	private List<String> newLines;
	
	public ChangeSchemaCommentsCommand(Schema schema, String newValue) {
		super("Set schema comments");
		this.schema = schema;
		this.newValue = newValue;
	}
	
	@Override
	public void execute() {
		Assert.isTrue(schema != null, "schema is null");
		Assert.isTrue(newValue != null, "newValue is null");
		oldLines = new ArrayList<>(schema.getComments());
		newLines = !newValue.isBlank() ? List.of(newValue.split(LINE_SEPARATOR)) : Collections.emptyList();
		redo();
	}
	
	@Override
	public void undo() {
		schema.getComments().clear();
		schema.getComments().addAll(oldLines);
	}
	
	@Override
	public void redo() {
		schema.getComments().clear();
		schema.getComments().addAll(newLines);
	}
	
}
