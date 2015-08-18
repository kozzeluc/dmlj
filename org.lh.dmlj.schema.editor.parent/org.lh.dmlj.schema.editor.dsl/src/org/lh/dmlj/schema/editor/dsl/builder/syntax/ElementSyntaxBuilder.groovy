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
import org.lh.dmlj.schema.Usage;

class ElementSyntaxBuilder extends AbstractSyntaxBuilder<Element> {
	
	@Override
	protected String build() {
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
			without_tab "name '${model.name}'"
		}
	}
	
	private void level() {
		without_tab "level ${model.level}"
	}
	
	private void baseName() {
		if (model.baseName && model.baseName != model.name) {
			without_tab "baseName '${model.baseName}'"
		}
	}
	
	private void redefines() {
		if (model.redefines) {
			without_tab "redefines '${model.redefines.name}'"
		}
	}
	
	private void children() {
		def elementDslBuilderProperties =
			[ output : output , initialTabs : initialTabs + 2 , generateName : false ];
		if (model.children) {
			without_tab 'children {'
			for (child in model.children) {
				if (child != model.children[0]) {
					blank_line()
				}
				with_1_tab "element '${child.name}' {"
				new ElementSyntaxBuilder(elementDslBuilderProperties).build(child)
				with_1_tab '}'
			}
			without_tab '}'
		}
	}
	
	private void picture() {
		if (model.picture) {	
			without_tab "picture '${model.picture}'"
		}
	}
	
	private void usage() {
		if (model.usage != Usage.DISPLAY && model.usage != Usage.CONDITION_NAME) {
			without_tab "usage '${replaceUnderscoresBySpaces(model.usage)}'"
		}
	}
	
	private void value() {
		if (model.value) {
			without_tab "value ${withQuotes(model.value)}"			
		}
	}
	
	private void occurs() {
		if (model.occursSpecification) {
			OccursSpecification occursSpecification = model.occursSpecification
			if (!occursSpecification.dependingOn && !occursSpecification.indexElements) {
				without_tab "occurs ${occursSpecification.count}"
			} else {
				without_tab "occurs ${occursSpecification.count} {"
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
		if (model.nullable) {
			without_tab 'nullable'
		}
	}
	
}
