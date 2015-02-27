/**
 * Copyright (C) 2015  Luc Hermans
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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;


public class DeleteAreaCommand extends ModelChangeBasicCommand {
	
	protected SchemaArea area;
	private Schema schema;
	private int areaIndex;

	public DeleteAreaCommand(SchemaArea area) {
		super("Delete area" + area.getName());
		this.area = area;
	}
	
	@Override
	public void execute() {	
		schema = area.getSchema();
		areaIndex = schema.getAreas().indexOf(area);
		removeArea();
	}
	
	@Override
	public void undo() {
		restoreArea();
	}
	
	@Override
	public void redo() {
		removeArea();
	}
	
	private void removeArea() {
		Assert.isTrue(area.getAreaSpecifications().isEmpty(), 
					  "Area is referenced by at least 1 record or system owner: " + area.getName());
		Assert.isTrue(area.getRecords().isEmpty(), 
			  	  	  "Area references at least 1 record: " + area.getName());
		Assert.isTrue(area.getIndexes().isEmpty(), 
		  	  	  	  "Area references at least 1 system owner: " + area.getName());
		Assert.isTrue(area.getProcedures().isEmpty(), 
				  	  "Area references at least 1 procedure: " + area.getName());
		area.setSchema(null);
	}
	
	private void restoreArea() {
		Assert.isTrue(area.getSchema() == null, 
				  	  "Area is already referenced by a schema: " + area.getName());
		schema.getAreas().add(areaIndex, area);
	}

}
