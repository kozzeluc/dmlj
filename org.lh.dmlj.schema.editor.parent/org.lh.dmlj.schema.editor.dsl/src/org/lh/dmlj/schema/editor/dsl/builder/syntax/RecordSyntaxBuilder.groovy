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

import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.KeyElement
import org.lh.dmlj.schema.LocationMode
import org.lh.dmlj.schema.OffsetExpression
import org.lh.dmlj.schema.RecordProcedureCallSpecification
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.StorageMode
import org.lh.dmlj.schema.VsamType;

class RecordSyntaxBuilder extends AbstractSyntaxBuilder<SchemaRecord> {
	
	@Override
	protected String build() {		
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
			without_tab "name '${model.name}'"
		}
	}
	
	private void shareStructure() {
		if (model.synonymName) {
			without_tab "shareStructure '${model.synonymName} version ${model.synonymVersion}'"			
		}
	}
	
	private void primaryRecord() {
		if (model.baseName &&
			(!model.synonymName ||
			 model.baseName != model.synonymName || model.baseVersion != model.synonymVersion)) {
		
			without_tab "primaryRecord '${model.baseName} version ${model.baseVersion}'"
		}
	}
	
	private void recordId() {
		without_tab "recordId ${model.id}"
	}
	
	private void locationMode() {
		if (model.locationMode != LocationMode.DIRECT) {
			blank_line()
			if (model.locationMode == LocationMode.CALC) {
				calc()
			} else if (model.locationMode == LocationMode.VIA) {
				via()
			} else if (model.locationMode == LocationMode.VSAM) {
				vsam()
			} else if (model.locationMode == LocationMode.VSAM_CALC) {
				vsamCalc()
			}
		}
	}
	
	private void calc() {
		without_tab 'calc {'
		for (keyElement in model.calcKey.elements) {			
			with_1_tab "element '${keyElement.element.name}'"
		}			
		with_1_tab "duplicates '${replaceUnderscoresBySpaces(model.calcKey.duplicatesOption)}'"
		without_tab '}'
	}
	
	private void via() {
		without_tab 'via {'
		with_1_tab "set '${model.viaSpecification.set.name}'"
		if (model.viaSpecification.symbolicDisplacementName) {			
			with_1_tab "displacement '${model.viaSpecification.symbolicDisplacementName}'"			
		} else if (model.viaSpecification.displacementPageCount) {			
			with_1_tab "displacement ${model.viaSpecification.displacementPageCount}"
		}		
		without_tab '}'
	}
	
	private void vsam() {
		without_tab 'vsam {'
		vsamType()
		without_tab '}'
	}
	
	private void vsamCalc() {
		without_tab 'vsamCalc {'
		for (keyElement in model.calcKey.elements) {
			with_1_tab "element '${keyElement.element.name}'"
		}
		with_1_tab "duplicates '${replaceUnderscoresBySpaces(model.calcKey.duplicatesOption)}'"
		vsamType()
		without_tab '}'
	}
	
	private void vsamType() {
		VsamType vsamType = model.vsamType
		with_1_tab "type ${vsamType.lengthType}"
		if (vsamType.spanned) {
			with_1_tab 'spanned'
		}
	}

	private void area() {
		blank_line()
		if (!model.areaSpecification.symbolicSubareaName &&
			!model.areaSpecification.offsetExpression ||
			model.areaSpecification.offsetExpression &&
			!model.areaSpecification.offsetExpression.offsetPageCount &&
			!model.areaSpecification.offsetExpression.offsetPercent &&
			!model.areaSpecification.offsetExpression.pageCount &&
			!model.areaSpecification.offsetExpression.percent) {
		
			without_tab "area '${model.areaSpecification.area.name}'"
		} else {
			without_tab "area '${model.areaSpecification.area.name}' {"
			if (model.areaSpecification.symbolicSubareaName) {
				with_1_tab "subarea '${model.areaSpecification.symbolicSubareaName}'"
			} else if (model.areaSpecification.offsetExpression) {
				OffsetExpression offsetExpression = model.areaSpecification.offsetExpression
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
		if (model.minimumRootLength) { 	// we need everything non-null, so also a zero
			blank_line()
			without_tab "minimumRootLength ${model.minimumRootLength}"
		}
	}
	
	private void minimumFragmentLength() {
		if (model.minimumFragmentLength) {	// we need everything non-null, so also a zero
			if (!model.minimumRootLength) {
				blank_line()
			}
			without_tab "minimumFragmentLength ${model.minimumFragmentLength}"
		}
	}
	
	private void procedures() {
		if (model.procedures) {
			blank_line()	
		}
		for (call in model.procedures) {
			without_tab "call '${call.procedure.name} ${call.callTime} ${replaceUnderscoresBySpaces(call.verb)}'"
		}
	}
	
	private void elements() {
		def elementDslBuilderProperties = 
			[ output : output , initialTabs : initialTabs + 2 , generateName : false ];
		blank_line()
		without_tab 'elements {'
		for (element in model.rootElements) {
			if (element != model.rootElements[0]) {
				blank_line()
			}
			with_1_tab "element '${element.name}' {"
			new ElementSyntaxBuilder(elementDslBuilderProperties).build(element)
			with_1_tab '}'
		}
		without_tab '}'
	}
	
	private void diagram() {
		blank_line()
		without_tab 'diagram {'
		if (model.getStorageMode() != StorageMode.FIXED) {			
			with_1_tab "storageMode '${replaceUnderscoresBySpaces(model.storageMode)}'"			
		}							
		with_1_tab "x ${model.diagramLocation.x}"
		with_1_tab "y ${model.diagramLocation.y}"
		without_tab '}'
	}
	
}
