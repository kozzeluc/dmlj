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

import static java.util.stream.Collectors.summarizingInt;

import org.eclipse.draw2d.geometry.Point;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.NamingConventions.Type;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult.Status;

public class CreateRecordCommand extends ModelChangeBasicCommand {
	private static final int DEFAULT_RECORDID = 10;
	private static final String NEW_AREA_NAME = "RECORD-1-AREA";
	private static final String RECORD_NAME_PREFIX = "NEW-RECORD-";
	
	private final Schema schema;
	private final Point location;
	
	private SchemaRecord schemaRecord;
	private SchemaArea area;
	
	private boolean newAreaCreated = false;
	
	public CreateRecordCommand(Schema schema, Point location) {
		super("Create record");
		this.schema = schema;
		this.location = location;
	}
	
	@Override
	public void execute() {
		createRecord();
		hookToSchema();						
	}
	
	private void createRecord() {
		schemaRecord = SchemaFactory.eINSTANCE.createSchemaRecord();		
		setRecordNamesAndVersions();
		schemaRecord.setStorageMode(StorageMode.FIXED);
		schemaRecord.setLocationMode(LocationMode.DIRECT);
		setAreaSpecification();
		setRecordId();
		addAnElement();
		setDiagramLocation();
	}

	private void setRecordNamesAndVersions() {
		for (var i = 1; i <= Integer.MAX_VALUE; i++) {
			var recordName = RECORD_NAME_PREFIX + i;
			var validationResult = NamingConventions.validate(recordName, Type.RECORD_NAME);
			if (validationResult.getStatus() != Status.OK) {
				throw new IllegalStateException("cannot set record name to " + recordName + ": " + validationResult.getMessage());
			}
			if (schema.getRecord(recordName) == null) {								
				schemaRecord.setName(recordName);
				schemaRecord.setBaseName(recordName);
				schemaRecord.setBaseVersion((short) 1);
				schemaRecord.setSynonymName(recordName);
				schemaRecord.setSynonymVersion((short) 1);
				return;
			}			
		}
		throw new IllegalStateException("cannot determine record name"); // we'll never get here
	}

	private void setAreaSpecification() {
		// use the first area in the alphabetically sorted list, provided it is compatible with a non-VSAM record
		// that we are creating here - if such an area doesn't exist, create a new area
		area = schema.getAreas().stream()
				.sorted()
				.filter(Tools::canHoldNonVsamRecords)
				.findFirst()
				.orElse(null);
		if (area == null) {
			area = SchemaFactory.eINSTANCE.createSchemaArea();
			area.setName(NEW_AREA_NAME);
			newAreaCreated = true;
		}
		
		var areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		areaSpecification.setRecord(schemaRecord);
		// don't hook the area specification to the area
	}

	private void setRecordId() {
		var highestRecordIdFoundInArea = (short) area.getRecords().stream()
			.collect(summarizingInt(SchemaRecord::getId))
			.getMax();
		
		short recordId = highestRecordIdFoundInArea != 0 && !newAreaCreated ?  (short) (highestRecordIdFoundInArea + 1) : DEFAULT_RECORDID;
		var validationResult = NamingConventions.validate(recordId, Type.RECORD_ID); 
		if (validationResult.getStatus() != Status.OK) {
			throw new IllegalStateException("cannot set record id to " + recordId + ": " + validationResult.getMessage());
		}
		schemaRecord.setId(recordId);	
	}

	private void addAnElement() {
		var element = SchemaFactory.eINSTANCE.createElement();
		element.setLevel((short) 2);
		element.setName("ELEMENT-1");
		element.setBaseName("ELEMENT-1");
		element.setPicture("X(8)");
		element.setUsage(Usage.DISPLAY);
		element.setRecord(schemaRecord);
		schemaRecord.getRootElements().add(element);
	}

	private void setDiagramLocation() {
		var diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		schemaRecord.setDiagramLocation(diagramLocation);
		diagramLocation.setX(location.x);
		diagramLocation.setY(location.y);
		diagramLocation.setEyecatcher("record " + schemaRecord.getName());
	}

	private void hookToSchema() {
		schema.getRecords().add(schemaRecord);
		if (newAreaCreated) {
			schema.getAreas().add(area);
		}
		area.getAreaSpecifications().add(schemaRecord.getAreaSpecification());
		schema.getDiagramData().getLocations().add(schemaRecord.getDiagramLocation());
	}

	@Override
	public void undo() {
		unhookFromSchema();		
	}

	private void unhookFromSchema() {		
		schema.getDiagramData().getLocations().remove(schemaRecord.getDiagramLocation());
		area.getAreaSpecifications().remove(schemaRecord.getAreaSpecification());
		if (newAreaCreated) {
			schema.getAreas().remove(area);
		}
		schema.getRecords().remove(schemaRecord);
	}

	@Override
	public void redo() {
		hookToSchema();		
	}
	
}
