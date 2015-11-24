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
import org.lh.dmlj.schema.IndexElement
import org.lh.dmlj.schema.OccursSpecification
import org.lh.dmlj.schema.Usage

import groovy.transform.CompileStatic;;

@CompileStatic
class ElementSyntaxBuilder extends AbstractSyntaxBuilder<Element> {
	
	private Element element
	
	@Override
	protected String doBuild(Element model) {
		element = model
		level()
		name()		
		baseName()		
		redefines()
		children()
		picture()
		usage()
		value()
		occurs()
		nullable()
	}
	
	private void name() {
		if (generateName) {
			without_tab "name '${element.name}'"
		}
	}
	
	private void level() {
		without_tab "level ${element.level}"
	}
	
	private void baseName() {
		if (element.baseName && element.baseName != element.name) {
			without_tab "baseName '${element.baseName}'"
		}
	}
	
	private void redefines() {
		if (element.redefines) {
			without_tab "redefines '${element.redefines.name}'"
		}
	}
	
	private void children() {
		if (element.children) {
			without_tab 'children {'
			for (child in element.children) {
				if (child != element.children[0]) {
					blank_line()
				}
				with_1_tab "element '${child.name}' {"
				new ElementSyntaxBuilder([ output : output , initialTabs : initialTabs + 2 , 
										   generateName : false ]).build(child)
				with_1_tab '}'
			}
			without_tab '}'
		}
	}
	
	private void picture() {
		if (element.picture) {	
			without_tab "picture '${element.picture}'"
		}
	}
	
	private void usage() {
		if (element.usage != Usage.DISPLAY && element.usage != Usage.CONDITION_NAME) {
			without_tab "usage '${replaceUnderscoresBySpaces(element.usage)}'"
		}
	}
	
	private void value() {
		if (element.value) {
			without_tab "value ${withQuotes(element.value)}"			
		}
	}
	
	private void occurs() {
		if (element.occursSpecification) {
			OccursSpecification occursSpecification = element.occursSpecification
			if (!occursSpecification.dependingOn && !occursSpecification.indexElements) {
				without_tab "occurs ${occursSpecification.count}"
			} else {
				without_tab "occurs {"
				with_1_tab "count ${occursSpecification.count}"
				if (occursSpecification.dependingOn) {
					with_1_tab "dependingOn '${occursSpecification.dependingOn.name}'"		
				}
				for (indexElement in occursSpecification.indexElements) {
					if (indexElement.baseName || indexElement.baseName != indexElement.name) {
						with_1_tab "indexedBy {"
						with_2_tabs "name '${indexElement.name}'"
						with_2_tabs "baseName '${indexElement.baseName}'"
						with_1_tab '}'
					} else {		
						with_1_tab "indexedBy '${indexElement.name}'"
					}
				}
				without_tab '}'
			}
		}
	}
	
	private void nullable() {
		if (element.nullable) {
			without_tab 'nullable'
		}
	}
	
}
