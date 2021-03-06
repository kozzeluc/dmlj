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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
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
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.common.ValidationResult.Status;

public class CreateRecordCommand extends ModelChangeBasicCommand {
	
	private static final String NEW_AREA_NAME = "RECORD-1-AREA";
	private static final String RECORD_NAME_PREFIX = "NEW-RECORD-";
	
	private Schema schema;
	private SchemaRecord record;		   	   
	
	private SchemaArea area;
	private Point location;
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
		record = SchemaFactory.eINSTANCE.createSchemaRecord();		
		setRecordNamesAndVersions();
		record.setStorageMode(StorageMode.FIXED);
		record.setLocationMode(LocationMode.DIRECT);
		setAreaSpecification();
		setRecordId();
		addAnElement();
		setDiagramLocation();
	}

	private void setRecordNamesAndVersions() {
		for (int i = 1; i <= Integer.MAX_VALUE; i++) {
			String recordName = RECORD_NAME_PREFIX + i;
			ValidationResult validationResult = 
				NamingConventions.validate(recordName, Type.RECORD_NAME);
			if (validationResult.getStatus() != Status.OK) {
				throw new RuntimeException("cannot set record name to " + recordName + ": " + 
										   validationResult.getMessage());
			}
			if (schema.getRecord(recordName) == null) {								
				record.setName(recordName);
				record.setBaseName(recordName);
				record.setBaseVersion((short) 1);
				record.setSynonymName(recordName);
				record.setSynonymVersion((short) 1);
				return;
			}			
		}
		throw new RuntimeException("cannot determine record name"); // we'll never get here
	}

	private void setAreaSpecification() {		
		if (!schema.getAreas().isEmpty()) {
			// use the first area in the alphabetically sorted list, provided it is compatible with
			// a non-VSAM record that we are creating here - if such an area doesn't exist, create a
			// new area
			List<SchemaArea> areas = new ArrayList<>(schema.getAreas());
			Collections.sort(areas);
			for (SchemaArea anArea : areas) {
				if (Tools.canHoldNonVsamRecords(anArea)) {
					area = anArea;
					break;
				}
			}
			if (area == null) {
				createArea();
			}
		} else {
			createArea();
		}
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		areaSpecification.setRecord(record);
		// don't hook the area specification to the area 
	}
	
	private void createArea() {
		area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(NEW_AREA_NAME);
		newAreaCreated = true;
	}

	private void setRecordId() {		
		short maxRecordId = Short.MIN_VALUE;
		for (SchemaRecord record : area.getRecords()) {
			if (record.getId() > maxRecordId) {
				maxRecordId = record.getId();
			}
		}
		short recordId;
		if (maxRecordId != Integer.MIN_VALUE && !newAreaCreated) {
			recordId = (short) (maxRecordId + 1);
		} else {
			recordId = 10;
		}
		ValidationResult validationResult = NamingConventions.validate(recordId, Type.RECORD_ID); 
		if (validationResult.getStatus() != Status.OK) {
			throw new RuntimeException("cannot set record id to " + recordId + ": " + 
									   validationResult.getMessage());
		}
		record.setId(recordId);	
	}

	private void addAnElement() {
		Element element = SchemaFactory.eINSTANCE.createElement();
		element.setLevel((short) 2);
		element.setName("ELEMENT-1");
		element.setBaseName("ELEMENT-1");
		element.setPicture("X(8)");
		element.setUsage(Usage.DISPLAY);
		element.setRecord(record);
		record.getRootElements().add(element);
	}

	private void setDiagramLocation() {
		DiagramLocation diagramLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		record.setDiagramLocation(diagramLocation);
		diagramLocation.setX(location.x);
		diagramLocation.setY(location.y);
		diagramLocation.setEyecatcher("record " + record.getName());
	}

	private void hookToSchema() {
		schema.getRecords().add(record);
		if (newAreaCreated) {
			schema.getAreas().add(area);
		}
		area.getAreaSpecifications().add(record.getAreaSpecification());
		schema.getDiagramData().getLocations().add(record.getDiagramLocation());
	}

	@Override
	public void undo() {
		unhookFromSchema();		
	}

	private void unhookFromSchema() {		
		schema.getDiagramData().getLocations().remove(record.getDiagramLocation());
		area.getAreaSpecifications().remove(record.getAreaSpecification());
		if (newAreaCreated) {
			schema.getAreas().remove(area);
		}
		schema.getRecords().remove(record);
	}

	@Override
	public void redo() {
		hookToSchema();		
	}
	
}
