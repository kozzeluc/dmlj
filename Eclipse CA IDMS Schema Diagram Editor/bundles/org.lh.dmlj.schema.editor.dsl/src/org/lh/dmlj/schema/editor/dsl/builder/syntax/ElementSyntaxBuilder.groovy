/**
 * Copyright (C) 2016  Luc Hermans
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
	
	private static final String ELEMENT_TAB = '   '
	
	private Element element
	private String indent = ''
	
	@Override
	protected String doBuild(Element model) {
		element = model
		level()
		name()		
		baseName()		
		redefines()
		picture()
		usage()
		value()
		occurs()
		nullable()
		children()
	}
	
	private void level() {
		String level = element.level < 10 ? "0${element.level}" : "${element.level}" 
		output <<= "$indent${ELEMENT_TAB * initialTabs}$level"
	}
	
	private void name() {		
		output <<= " ${element.name}"		
	}
	
	private void baseName() {
		if (element.baseName && element.baseName != element.name) {
			output <<= " (${element.baseName})"
		}
	}
	
	private void redefines() {
		if (element.redefines) {
			output <<= " redefines ${element.redefines.name}"
		}
	}
	
	private void picture() {
		if (element.picture) {	
			output <<= " picture ${element.picture}"
		}
	}
	
	private void usage() {
		if (element.usage != Usage.DISPLAY && element.usage != Usage.CONDITION_NAME) {
			output <<= " usage '${replaceUnderscoresBySpaces(element.usage)}'"
		}
	}
	
	private void value() {
		if (element.value) {
			output <<= " value ${element.value}"			
		}
	}
	
	private void occurs() {
		if (element.occursSpecification) {
			OccursSpecification occursSpecification = element.occursSpecification
			output <<= " occurs ${occursSpecification.count}"
			if (occursSpecification.dependingOn) {
				output <<= " dependingOn ${occursSpecification.dependingOn.name}"		
			}
			boolean firstIndex = true
			occursSpecification.indexElements.each { indexElement ->
				if (firstIndex) {
					output << " indexedBy ${indexElement.name}"
					firstIndex = false
				} else {
					output << ", ${indexElement.name}"
				}
				if (indexElement.baseName || indexElement.baseName != indexElement.name) {
					output <<= " (${indexElement.baseName})"
				}
			}			
		}
	}
	
	private void nullable() {
		if (element.nullable) {
			output <<= ' nullable'
		}
	}
	
	private void children() {
		if (element.children) {
			element.children.each { child ->
				output <<= '\n'
				new ElementSyntaxBuilder([ output : output , indent : indent, initialTabs : initialTabs + 1 ]).build(child)
			}
		}
	}
	
}
