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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import groovy.transform.CompileStatic

import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.OffsetExpression
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.VsamLengthType
import org.lh.dmlj.schema.VsamType

@CompileStatic
class RecordSyntaxBuilder extends AbstractSyntaxBuilder<SchemaRecord> {
	
	private SchemaRecord record
	
	@Override
	protected String doBuild(SchemaRecord model) {
		record = model		
		name()
		shareStructure()
		primaryRecord()
		recordId()
		locationMode()
		area()
		minimumRootLength()
		minimumFragmentLength()
		procedures()
		elements()
		diagram()
	}
	
	private void name() {
		if (generateName) {
			without_tab "name '${record.name}'"
		}
	}
	
	private void shareStructure() {
		if (record.synonymName) {
			without_tab "shareStructure '${record.synonymName} version ${record.synonymVersion}'"			
		}
	}
	
	private void primaryRecord() {
		if (record.baseName &&
			(!record.synonymName ||
			 record.baseName != record.synonymName || record.baseVersion != record.synonymVersion)) {
		
			without_tab "primaryRecord '${record.baseName} version ${record.baseVersion}'"
		}
	}
	
	private void recordId() {
		without_tab "recordId ${record.id}"
	}
	
	private void locationMode() {
		if (record.locationMode != LocationMode.DIRECT) {
			blank_line()
			if (record.locationMode == LocationMode.CALC) {
				calc()
			} else if (record.locationMode == LocationMode.VIA) {
				via()
			} else if (record.locationMode == LocationMode.VSAM) {
				vsam()
			} else if (record.locationMode == LocationMode.VSAM_CALC) {
				vsamCalc()
			}
		}
	}
	
	private void calc() {
		without_tab 'calc {'
		record.calcKey.elements.each { keyElement ->			
			with_1_tab "element '${keyElement.element.name}'"
		}			
		with_1_tab "duplicates '${replaceUnderscoresBySpaces(record.calcKey.duplicatesOption)}'"
		without_tab '}'
	}
	
	private void via() {
		without_tab 'via {'
		with_1_tab "set '${record.viaSpecification.set.name}'"
		if (record.viaSpecification.symbolicDisplacementName) {			
			with_1_tab "displacement '${record.viaSpecification.symbolicDisplacementName}'"			
		} else if (record.viaSpecification.displacementPageCount) {			
			with_1_tab "displacement ${record.viaSpecification.displacementPageCount}"
		}		
		without_tab '}'
	}
	
	private void vsam() {
		if (record.vsamType.lengthType == VsamLengthType.FIXED && !record.vsamType.spanned) {
			without_tab 'vsam'
		} else {
			without_tab 'vsam {'
			vsamType()
			without_tab '}'
		}
	}
	
	private void vsamCalc() {
		without_tab 'vsamCalc {'
		record.calcKey.elements.each { keyElement ->
			with_1_tab "element '${keyElement.element.name}'"
		}
		with_1_tab "duplicates '${replaceUnderscoresBySpaces(record.calcKey.duplicatesOption)}'"
		vsamType()
		without_tab '}'
	}
	
	private void vsamType() {
		VsamType vsamType = record.vsamType
		if (vsamType.lengthType != VsamLengthType.FIXED) {
			with_1_tab "type '${vsamType.lengthType}'"
		}
		if (vsamType.spanned) {
			with_1_tab 'spanned'
		}
	}

	private void area() {
		blank_line()
		if (!record.areaSpecification.symbolicSubareaName &&
			!record.areaSpecification.offsetExpression ||
			record.areaSpecification.offsetExpression &&
			!record.areaSpecification.offsetExpression.offsetPageCount &&
			!record.areaSpecification.offsetExpression.offsetPercent &&
			!record.areaSpecification.offsetExpression.pageCount &&
			!record.areaSpecification.offsetExpression.percent) {
		
			without_tab "area '${record.areaSpecification.area.name}'"
		} else {
			without_tab "area '${record.areaSpecification.area.name}' {"
			if (record.areaSpecification.symbolicSubareaName) {
				with_1_tab "subarea '${record.areaSpecification.symbolicSubareaName}'"
			} else if (record.areaSpecification.offsetExpression) {
				OffsetExpression offsetExpression = record.areaSpecification.offsetExpression
				if (offsetExpression.offsetPageCount) {
					with_1_tab "offsetPages ${offsetExpression.offsetPageCount}"		
				} else if (offsetExpression.offsetPercent) {		
					with_1_tab "offsetPercent ${offsetExpression.offsetPercent}"		
				}
				if (offsetExpression.pageCount) {		
					with_1_tab "pages ${offsetExpression.pageCount}"		
				} else if (offsetExpression.percent) {		
					with_1_tab "percent ${offsetExpression.percent}"		
				}
			}
			without_tab '}'
		}
	}
	
	private void minimumRootLength() {
		if (record.minimumRootLength != null) { 	// we need everything non-null, so also a zero
			blank_line()
			without_tab "minimumRootLength ${record.minimumRootLength}"
		}
	}
	
	private void minimumFragmentLength() {
		if (record.minimumFragmentLength != null) {	// we need everything non-null, so also a zero
			if (record.minimumRootLength == null) {
				blank_line()
			}
			without_tab "minimumFragmentLength ${record.minimumFragmentLength}"
		}
	}
	
	private void procedures() {
		if (record.procedures) {
			blank_line()	
		}
		record.procedures.each { call ->
			without_tab "procedure '${call.procedure.name} ${call.callTime} ${replaceUnderscoresBySpaces(call.verb)}'"
		}
	}
	
	private void elements() {
		blank_line()
		without_tab 'elements {'
		record.rootElements.each { element ->
			if (element != record.rootElements[0]) {
				blank_line()
			}
			with_1_tab "element '${element.name}' {"
			new ElementSyntaxBuilder([ output : output , initialTabs : initialTabs + 2 , 
									   generateName : false ]).build(element)
			with_1_tab '}'
		}
		without_tab '}'
	}
	
	private void diagram() {
		blank_line()
		without_tab 'diagram {'
		if (record.getStorageMode() != StorageMode.FIXED) {			
			with_1_tab "storageMode '${replaceUnderscoresBySpaces(record.storageMode)}'"			
		}							
		with_1_tab "x ${xOrY(record.diagramLocation.x)}"
		with_1_tab "y ${xOrY(record.diagramLocation.y)}"
		without_tab '}'
	}
	
}
