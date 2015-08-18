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
package org.lh.dmlj.schema.editor.dsl.builder.model

import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.Usage

import groovy.lang.Closure;

class RecordBuilder extends AbstracModelBuilder {
	
	private static final String BODY_AREA = "area"

	private SchemaRecord record
	
	private boolean areaSet
	private boolean primaryRecordSet
	private boolean shareStructureSet
	
	void area(Closure definition) {
		if (bufferedName) {
			area(bufferedName, definition)
			bufferedName = null
		} else {		
			assert !bodies
			bodies << BODY_AREA
			runClosure(definition)
			assert bodies == [ BODY_AREA ]
			assert record.areaSpecification, "name is mandatory in the $BODY_AREA body when not specified as a string preceding the $BODY_AREA body"
			bodies -= BODY_AREA
		}
	}
	
	void area(String name) {
		assert !bodies		
		createInitialAreaSpecification(name)
	}
	
	void area(String name, Closure definition) {
		assert !bodies
		createInitialAreaSpecification(name)
		bodies << BODY_AREA
		runClosure(definition)
		assert bodies == [ BODY_AREA ]
		bodies -= BODY_AREA
	}
	
	SchemaRecord build() {
		def definition = { }
		build(definition)
	}
	
	SchemaRecord build(Closure definition) {
		
		assert !bodies
		
		if (schema == null) {
			buildTemporarySchema()
		}
		record = SchemaFactory.eINSTANCE.createSchemaRecord()
		schema.getRecords().add(record)
		
		record.id = generateRecordId()
		setInitialNamesAndVersions("SR" + String.format('%04d', record.id))		
		record.locationMode = LocationMode.DIRECT
		
		record.storageMode = StorageMode.FIXED		
		record.diagramLocation = buildAndRegisterDiagramLocation('record ' + record.name)
		
		runClosure definition
		
		assert !bodies
		
		if (!record.areaSpecification) {
			createInitialAreaSpecification(record.name + "-AREA")
		}
		
		if (!record.elements) {
			createInitialElement() 		
		}		
		
		record
	}
	
	SchemaRecord build(String name) {
		
		assert !bodies
		
		if (schema == null) {
			buildTemporarySchema()
		}
		record = SchemaFactory.eINSTANCE.createSchemaRecord()
		schema.getRecords().add(record)
		
		record.id = generateRecordId()
		setInitialNamesAndVersions(name)										
		record.locationMode = LocationMode.DIRECT		
				
		createInitialAreaSpecification(record.name + "-AREA")
		
		createInitialElement() 		
		
		record.storageMode = StorageMode.FIXED
		record.diagramLocation = buildAndRegisterDiagramLocation('record ' + name)
		
		record
	}
	
	SchemaRecord build(String name, Closure definition) {
		record = build(definition)
		setRecordNames(name)
		record
	}
	
	private void createInitialAreaSpecification(String areaName) {
		assert !record.areaSpecification
		record.areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification()
		record.areaSpecification.area = record.schema.getArea(areaName)		
		if (!record.areaSpecification.area) {
			record.areaSpecification.area = SchemaFactory.eINSTANCE.createSchemaArea()		
			record.schema.areas << record.areaSpecification.area
			record.areaSpecification.area.name = areaName
		}
	}
	
	private void createInitialElement() {
		assert !record.elements
		record.elements << SchemaFactory.eINSTANCE.createElement()
		record.rootElements << record.elements[0]
		record.elements[0].level = 2
		record.elements[0].name = 'ELEMENT1'
		record.elements[0].baseName = 'ELEMENT1'
		record.elements[0].picture = 'X(8)'
		record.elements[0].usage = Usage.DISPLAY
	}
	
	private void createOffsetExpression() {
		record.areaSpecification.offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression()
	}
	
	private short generateRecordId() {
		assert record.schema
		if (record.schema.records.empty || record.schema.records.size == 1 && 
			record in schema.records && record.id == 0) {
			
			return (short) 10
		}
		short highestRecordId = -1
		record.schema.records.each {SchemaRecord record ->
			if (record.id > highestRecordId) {
				highestRecordId = record.id
			}
		}
		(short) highestRecordId + 1
	}
	
	void minimumFragmentLength(int minimumFragmentLength) {
		assert !bodies
		record.minimumFragmentLength = minimumFragmentLength
	}
	
	void minimumRootLength(int minimumRootLength) {
		assert !bodies
		record.minimumRootLength = minimumRootLength
	}
	
	void name(String name) {
		assert !bodies || bodies == [ BODY_AREA ]
		if (!bodies) {
			setRecordNames(name)
		} else {
			assert !record.areaSpecification, "area name is already set"
			createInitialAreaSpecification(name)
		}
	}
	
	void offsetPages(int offsetPageCount) {
		assert bodies == [ BODY_AREA ]
		assert !record.areaSpecification.symbolicSubareaName, 'offsetPages and subarea are mutually exclusive'
		if (record.areaSpecification.offsetExpression) {
			assert !record.areaSpecification.offsetExpression.offsetPageCount, 'offsetPages is already set'
			assert !record.areaSpecification.offsetExpression.offsetPercent, 'offsetPages and offsetPercent are mutually exclusive'
		} else {
			createOffsetExpression()
		}
		record.areaSpecification.offsetExpression.offsetPageCount = offsetPageCount
	}
	
	void offsetPercent(int offsetPercent) {
		assert bodies == [ BODY_AREA ]
		assert !record.areaSpecification.symbolicSubareaName, 'offsetPercent and subarea are mutually exclusive'
		if (record.areaSpecification.offsetExpression) {
			assert !record.areaSpecification.offsetExpression.offsetPercent, 'offsetPercent is already set'
			assert !record.areaSpecification.offsetExpression.offsetPageCount, 'offsetPercent and offsetPages are mutually exclusive'
		} else {
			createOffsetExpression()
		}
		record.areaSpecification.offsetExpression.offsetPercent = (short) offsetPercent
	}
	
	void pages(int pageCount) {
		assert bodies == [ BODY_AREA ]
		assert !record.areaSpecification.symbolicSubareaName, 'pages and subarea are mutually exclusive'
		if (record.areaSpecification.offsetExpression) {
			assert !record.areaSpecification.offsetExpression.pageCount, 'pages is already set'
			assert !record.areaSpecification.offsetExpression.percent, 'pages and percent are mutually exclusive'
		} else {
			createOffsetExpression()
		}
		record.areaSpecification.offsetExpression.pageCount = pageCount
	}
	
	void percent(int percent) {
		assert bodies == [ BODY_AREA ]
		assert !record.areaSpecification.symbolicSubareaName, 'percent and subarea are mutually exclusive'
		if (record.areaSpecification.offsetExpression) {
			assert !record.areaSpecification.offsetExpression.percent, 'percent is already set'
			assert !record.areaSpecification.offsetExpression.pageCount, 'percent and pages are mutually exclusive'
		} else {
			createOffsetExpression()
		}
		record.areaSpecification.offsetExpression.percent = (short) percent
	}
	
	void primaryRecord(String primaryRecord) {
		List<String> tokens = primaryRecord.split(' ')
		assert tokens.size == 3
		assert tokens[1] == 'version'
		record.baseName = tokens[0]
		record.baseVersion = tokens[2].toShort()
		primaryRecordSet = true	
	}
	
	void procedure(String procedureCallSpecAsString) {
		
		assert !bodies
		
		int i = procedureCallSpecAsString.indexOf(" ")
		int j = procedureCallSpecAsString.indexOf(" ", i + 1)
		String procedureName = procedureCallSpecAsString.substring(0, i);
		String callTimeAsString = procedureCallSpecAsString.substring(i + 1, j).replaceAll(" ", "_")
		String verbAsString = procedureCallSpecAsString.substring(j + 1).replaceAll(" ", "_")
			
		ProcedureCallTime callTime = ProcedureCallTime.valueOf(callTimeAsString)
		RecordProcedureCallVerb verb = RecordProcedureCallVerb.valueOf(verbAsString)
		
		Procedure procedure = createOrGetProcedure(procedureName)
		
		RecordProcedureCallSpecification recordProcedureCallSpecification =
			SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification()
		recordProcedureCallSpecification.record = record
		recordProcedureCallSpecification.procedure = procedure
		recordProcedureCallSpecification.callTime = callTime
		recordProcedureCallSpecification.verb = verb
	}

	void recordId(int recordId) {
		assert !bodies
		record.id = recordId
	}
	
	void setInitialNamesAndVersions(String name) {
		record.name = name
		record.baseName = name
		record.baseVersion = 1
		record.synonymName = name
		record.synonymVersion = 1
	}
	
	private void setRecordNames(String name) {
		assert !bodies
		record.name = name
		if (!primaryRecordSet) {
			record.baseName = name
		}
		if (!shareStructureSet) {
			record.synonymName = name
		}
		record.diagramLocation.eyecatcher = 'record ' + name
		
		if (!areaSet && record.areaSpecification) {
			// correct the name of the automatically created area
			String areaName = record.name + "-AREA"
			assert !record.schema.getArea(areaName)
			record.areaSpecification.area.name = areaName
		}
	}
	
	void shareStructure(String shareStructure) {
		List<String> tokens = shareStructure.split(' ')
		assert tokens.size == 3
		assert tokens[1] == 'version'
		record.synonymName = tokens[0]
		record.synonymVersion = tokens[2].toShort()
		shareStructureSet = true
	}
	
	void storageMode(String storageMode) {
		assert bodies == [ BODY_DIAGRAM ]
		record.storageMode = StorageMode.valueOf(storageMode.replaceAll(' ', '_'))
	}
	
	void subarea(String symbolicSubareaName) {
		assert bodies == [ BODY_AREA ]
		assert !record.areaSpecification.symbolicSubareaName, 'subarea is already set'
		assert !record.areaSpecification.offsetExpression, 'subarea and anything of [offsetPages, offsetPercent, pages, percent] area mutually exclusive'
		record.areaSpecification.symbolicSubareaName = symbolicSubareaName
	}
	
	void x(int x) {
		assert bodies == [ BODY_DIAGRAM ]
		record.diagramLocation.x = x
	}
	
	void y(int y) {
		assert bodies == [ BODY_DIAGRAM ]
		record.diagramLocation.y = y
	}
	
}
