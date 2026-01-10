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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

public class DeleteRecordCommand extends ModelChangeBasicCommand {
	protected final SchemaRecord schemaRecord;
	
	private Schema schema;
	private int recordIndex;
	private SchemaArea area;
	private int areaSpecificationIndex;
	private Set viaSet;
	private int viaSpecificationIndex;
	private DiagramLocation diagramLocation;
	private int diagramLocationIndex;

	public DeleteRecordCommand(SchemaRecord schemaRecord) {
		super();
		this.schemaRecord = schemaRecord;
	}
	
	@Override
	public void execute() {
		schema = schemaRecord.getSchema();
		recordIndex = schema.getRecords().indexOf(schemaRecord);
		area = schemaRecord.getAreaSpecification().getArea();
		areaSpecificationIndex = area.getAreaSpecifications().indexOf(schemaRecord.getAreaSpecification());
		if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			viaSet = schemaRecord.getViaSpecification().getSet();
			viaSpecificationIndex = viaSet.getViaMembers().indexOf(schemaRecord.getViaSpecification());
		}
		diagramLocation = schemaRecord.getDiagramLocation();
		diagramLocationIndex = schema.getDiagramData().getLocations().indexOf(diagramLocation);
		removeRecord();
	}
	
	@Override
	public void undo() {
		restoreRecord();
	}
	
	@Override
	public void redo() {
		removeRecord();
	}
	
	private void removeRecord() {
		Assert.isTrue(schemaRecord.getOwnerRoles().isEmpty(), "Record is an owner of at least 1 set: " + schemaRecord.getName());
		Assert.isTrue(schemaRecord.getMemberRoles().isEmpty(), "Record is a member of at least 1 set: " + schemaRecord.getName());
		Assert.isTrue(schemaRecord.getProcedures().isEmpty(), "Record references at least 1 procedure: " + schemaRecord.getName());
		
		schemaRecord.setSchema(null);
		area.getAreaSpecifications().remove(schemaRecord.getAreaSpecification());
		if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			schemaRecord.getViaSpecification().setSet(null);
		}
		schema.getDiagramData().getLocations().remove(diagramLocation);
	}
	
	private void restoreRecord() {
		Assert.isTrue(schemaRecord.getSchema() == null, "Record is already referenced by a schema: " + schemaRecord.getName());		
		schema.getDiagramData().getLocations().add(diagramLocationIndex, diagramLocation);
		if (schemaRecord.getLocationMode() == LocationMode.VIA) {
			viaSet.getViaMembers().add(viaSpecificationIndex, schemaRecord.getViaSpecification());
		}
		area.getAreaSpecifications().add(areaSpecificationIndex, schemaRecord.getAreaSpecification());
		schema.getRecords().add(recordIndex, schemaRecord);
	}
	
}
