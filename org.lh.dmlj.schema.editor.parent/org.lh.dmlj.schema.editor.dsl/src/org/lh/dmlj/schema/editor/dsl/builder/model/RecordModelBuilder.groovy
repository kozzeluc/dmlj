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

import org.lh.dmlj.schema.DuplicatesOption
import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.KeyElement
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.Procedure
import org.lh.dmlj.schema.ProcedureCallTime
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.RecordProcedureCallVerb
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.Usage
import org.lh.dmlj.schema.VsamLengthType

class RecordModelBuilder extends AbstractModelBuilder {
	
	private static final String BODY_AREA = "area"
	private static final String BODY_CALC = "calc"
	private static final String BODY_ELEMENTS = "elements"
	private static final String BODY_VIA = "via"
	private static final String BODY_VSAM = "vsam"
	private static final String BODY_VSAM_CALC = "vsamCalc"

	private SchemaRecord record
	
	private boolean areaSet
	private boolean primaryRecordSet
	private boolean shareStructureSet
	
	def propertyMissing(String name) {
		if (name == 'direct') {
			direct()
		} else if (name == 'spanned') {
			spanned()
		} else if (name == 'vsam') {
			Closure definition = { 
				type 'FIXED'
			}
			vsam(definition)
		} else {
			throw new MissingPropertyException('no such property: ' + name + ' for class: ' +
											   getClass().getName())
		}
	}
	
	private void makeFinalRecordNameAdjustments() {
		
		if (!shareStructureSet) {
			record.synonymName = record.name
			record.synonymVersion = 1
		}
		
		if (!primaryRecordSet) {
			record.baseName = record.synonymName
			record.baseVersion = record.synonymVersion
		}
		
		record.diagramLocation.eyecatcher = 'record ' + record.name
		
		if (!areaSet && record.areaSpecification) {
			// correct the name of the automatically created area
			String areaName = record.name + "-AREA"
			assert !record.schema.getArea(areaName)
			record.areaSpecification.area.name = areaName
		}
	}
	
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
		areaSet = true
	}
	
	void area(String name, Closure definition) {
		assert !bodies
		createInitialAreaSpecification(name)
		areaSet = true
		bodies << BODY_AREA
		runClosure(definition)
		assert bodies == [ BODY_AREA ]
		bodies -= BODY_AREA
	}
	
	void bindCalcKeyElements() {
		for (KeyElement keyElement in record.calcKey.elements) {
			String elementName = keyElement.element.name // get element name from placeholder element
			Element element = record.getElement(elementName)
			assert element, "CALC key element $elementName is not defined for record ${record.name}"
			keyElement.element = element // replace placeholder element with the real element
		}
	}
	
	SchemaRecord build() {
		build(null, { })
	}
	
	SchemaRecord build(Closure definition) {
		build(null, definition)
	}
	
	SchemaRecord build(String name) {		
		build(name, { })
	}
	
	SchemaRecord build(String recordName, Closure definition) {
		
		assert !bodies
		
		if (schema == null) {
			buildTemporarySchema()
		}
		record = SchemaFactory.eINSTANCE.createSchemaRecord()
		schema.getRecords().add(record)
		
		record.id = generateRecordId()
		record.name = recordName ?: "SR" + String.format('%04d', record.id)
		record.locationMode = LocationMode.DIRECT
		
		record.storageMode = StorageMode.FIXED		
		record.diagramLocation = buildAndRegisterDiagramLocation(null) // eyecatcher is set later
		
		runClosure definition
		
		assert !bodies
		
		makeFinalRecordNameAdjustments()
		
		if (!record.areaSpecification) {
			createInitialAreaSpecification(record.name + "-AREA")
		}
		
		if (!record.elements) {
			createInitialElement() 		
		}
		
		if (record.locationMode == LocationMode.CALC || 
			record.locationMode == LocationMode.VSAM_CALC) {
			
			bindCalcKeyElements()
		}		
		
		record
	}
	
	void calc(Closure definition) {
		assert !bodies
		assert record.locationMode == LocationMode.DIRECT, "record $record.name is $record.locationMode"
		record.locationMode = LocationMode.CALC
		record.calcKey = SchemaFactory.eINSTANCE.createKey()
		record.keys << record.calcKey
		record.calcKey.duplicatesOption = DuplicatesOption.NOT_ALLOWED
		bodies << BODY_CALC
		runClosure(definition)
		assert bodies == [ BODY_CALC ]
		assert record.calcKey.elements, 'CALC key must consist of at least 1 element'
		bodies -= BODY_CALC
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
	
	void diagram(Closure definition) {
		assert !bodies
		bodies << BODY_DIAGRAM
		runClosure(definition)
		assert bodies == [ BODY_DIAGRAM ]
		bodies -= BODY_DIAGRAM		
	}
	
	void direct() {
		assert !bodies
		assert record.locationMode == LocationMode.DIRECT
	}
	
	void displacement(String symbolicDisplacementName) {
		assert bodies == [ BODY_VIA ]
		assert !record.viaSpecification.displacementPageCount, 'symbolic displacement name and displacement page count are mutually exclusive'
		record.viaSpecification.symbolicDisplacementName = symbolicDisplacementName
	}
	
	void displacement(int displacementPageCount) {
		assert bodies == [ BODY_VIA ]
		assert !record.viaSpecification.symbolicDisplacementName, 'displacement page count and symbolic displacement name are mutually exclusive'
		record.viaSpecification.displacementPageCount = displacementPageCount
	}
	
	void duplicates(String duplicatesOption) {
		assert bodies == [ BODY_CALC ] || bodies == [ BODY_VSAM_CALC ]
		record.calcKey.duplicatesOption = 
			DuplicatesOption.valueOf(duplicatesOption.replaceAll(' ', '_'))
	}
	
	void element(Closure definition) {
		assert bodies == [ BODY_ELEMENTS ]
		ElementModelBuilder elementBuilder = new ElementModelBuilder( [ record : record ] )
		if (bufferedName) {
			elementBuilder.build(bufferedName, definition)
			bufferedName = null
		} else {
			elementBuilder.build(definition)
		}
		assert bodies == [ BODY_ELEMENTS ]		
	}
	
	void element(String calcElementName) {
		assert bodies == [ BODY_CALC ] || bodies == [ BODY_VSAM_CALC ]
		Element placeHolderElement = SchemaFactory.eINSTANCE.createElement()
		placeHolderElement.name = calcElementName
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement()
		keyElement.element = placeHolderElement
		record.calcKey.elements << keyElement
	}
	
	void elements(Closure definition) {
		assert !bodies
		bodies << BODY_ELEMENTS
		runClosure(definition)
		assert bodies == [ BODY_ELEMENTS ]
		bodies -= BODY_ELEMENTS
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
			record.name = name
		} else {
			assert !record.areaSpecification, "area name is already set"
			createInitialAreaSpecification(name)
			areaSet = true
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
	
	void set(String setName) {
		assert bodies == [ BODY_VIA ]
		if (schemaIsAutomatic) {
			// in the case of a temporary schema, also create the VIA set and make sure it is 
			// resolved
			String recordName = record.name
			SetModelBuilder setBuilder = 
				new SetModelBuilder( schema : schema, resolveViaSet : false )
			Set realSet = setBuilder.build { 
				name setName
				member recordName
			}
			record.viaSpecification.set = realSet
		} else {
			// if we have a real schema, create a placeholder set but don't attach it to the schema;
			// the set builder will detect this situation when the VIA set is eventually built and
			// replace the placeholder with the real set
			Set placeHolderSet = SchemaFactory.eINSTANCE.createSet()
			placeHolderSet.name = setName
			record.viaSpecification.set = placeHolderSet
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
	
	void spanned() {
		assert bodies == [ BODY_VSAM ] || bodies == [ BODY_VSAM_CALC ]
		record.vsamType.spanned = true
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
	
	void type(String type) {
		assert bodies == [ BODY_VSAM ] || bodies == [ BODY_VSAM_CALC ]
		record.vsamType.lengthType = VsamLengthType.valueOf(type.replaceAll(' ', '_'))
	}

	void via(Closure definition) {
		assert !bodies
		assert record.locationMode == LocationMode.DIRECT
		record.locationMode = LocationMode.VIA
		record.viaSpecification = SchemaFactory.eINSTANCE.createViaSpecification()
		bodies << BODY_VIA
		runClosure(definition)
		assert bodies == [ BODY_VIA ]
		bodies -= BODY_VIA
	}
	
	void vsam(Closure definition) {
		assert !bodies
		assert record.locationMode == LocationMode.DIRECT
		record.locationMode = LocationMode.VSAM
		record.vsamType = SchemaFactory.eINSTANCE.createVsamType()
		bodies << BODY_VSAM
		runClosure(definition)
		assert bodies == [ BODY_VSAM ]
		bodies -= BODY_VSAM
	}
	
	void vsamCalc(Closure definition) {
		assert !bodies
		assert record.locationMode == LocationMode.DIRECT
		record.locationMode = LocationMode.VSAM_CALC
		record.vsamType = SchemaFactory.eINSTANCE.createVsamType()
		record.calcKey = SchemaFactory.eINSTANCE.createKey()
		record.keys << record.calcKey
		record.calcKey.duplicatesOption = DuplicatesOption.NOT_ALLOWED
		bodies << BODY_VSAM_CALC
		runClosure(definition)
		assert bodies == [ BODY_VSAM_CALC ]
		assert record.calcKey.elements, 'CALC key must consist of at least 1 element'
		bodies -= BODY_VSAM_CALC
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
