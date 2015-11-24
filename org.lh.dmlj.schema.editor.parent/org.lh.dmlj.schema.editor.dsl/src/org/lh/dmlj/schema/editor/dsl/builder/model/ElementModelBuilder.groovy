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

import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.IndexElement
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Usage

class ElementModelBuilder extends AbstractModelBuilder<Element> {
	
	private static final String BODY_CHILDREN = "children"
	private static final String BODY_INDEXED_BY = "indexedBy"
	private static final String BODY_OCCURS = "occurs"
	
	private Element element
	private SchemaRecord record
	private Element parent
	
	def propertyMissing(String name) {
		if (name == 'nullable') {
			nullable()
		} else {
			throw new MissingPropertyException('no such property: ' + name + ' for class: ' +
											   getClass().getName())
		}
	}
	
	void baseName(String baseName) {
		assert !bodies || [ BODY_OCCURS, BODY_INDEXED_BY ]
		if (!bodies) {
			element.baseName = baseName
		} else {
			element.occursSpecification.indexElements[-1].baseName = baseName
		}
	}
	
	Element build(Closure definition) {		
		build(null, definition)
	}
	
	Element build(String name, Closure definition) {		
		assert !bodies
		element = createElement(name)
		runClosure definition
		assert !bodies
		assert element.name, 'element name is NOT set'
		if (!element.baseName) {
			element.baseName = element.name
		}
		element
	}
	
	void children(Closure definition) {
		assert !bodies
		bodies << BODY_CHILDREN
		runClosure(definition)
		assert bodies == [ BODY_CHILDREN ]
		bodies -= BODY_CHILDREN
	}
	
	void count(int count) {
		assert bodies == [ BODY_OCCURS ]
		assert !element.occursSpecification
		element.occursSpecification = SchemaFactory.eINSTANCE.createOccursSpecification()
		element.occursSpecification.count = count
	}
	
	private Element createElement(String name) {
		assert !element
		element = SchemaFactory.eINSTANCE.createElement()
		if (name) {
			element.name = name
		}
		if (record) {
			record.elements << element
		}
		if (parent) {
			parent.children << element
		} else if (record) {
			record.rootElements << element
		}
		element
	}
	
	void dependingOn(String dependingOnElementName) {
		assert bodies == [ BODY_OCCURS ]
		assert !element.occursSpecification.dependingOn, 
			   "occurs already depends on ${element.occursSpecification.dependingOn.name}"
		assert dependingOnElementName != element.name, "occurs cannot depend on same element"
		List<Element> candidates = []
		if (record) {
			candidates = record.elements
		} else if (parent) {
			candidates = parent.children
		}
		Element candidate = 
			candidates.find { Element element -> element.name == dependingOnElementName }
		assert candidate, 
			   "occurs depending on element is NOT defined in record or parent: $dependingOnElementName"
		assert candidate.level != 88, 'depending on element cannot be a condition name'
		element.occursSpecification.dependingOn = candidate
	}
	
	void element(Closure definition) {
		assert bodies == [ BODY_CHILDREN ]
		ElementModelBuilder elementBuilder = 
			new ElementModelBuilder( [ record : record, parent : element ] )
		if (bufferedName) {
			elementBuilder.build(bufferedName, definition)
			bufferedName = null
		} else {
			elementBuilder.build(definition)
		}
		assert bodies == [ BODY_CHILDREN ]
	}
	
	void indexedBy(String indexElementName) {
		assert bodies == [ BODY_OCCURS ]
		IndexElement indexElement = SchemaFactory.eINSTANCE.createIndexElement()
		indexElement.name = indexElementName
		element.occursSpecification.indexElements << indexElement
	}
	
	void indexedBy(Closure definition) {
		assert bodies == [ BODY_OCCURS ]
		if (bufferedName) {
			indexedBy(bufferedName)
		} else {
			IndexElement indexElement = SchemaFactory.eINSTANCE.createIndexElement()
			element.occursSpecification.indexElements << indexElement
		}
		bodies << BODY_INDEXED_BY
		runClosure(definition)
		assert bodies == [ BODY_OCCURS, BODY_INDEXED_BY ]
		bodies -= BODY_INDEXED_BY
		bufferedName = null
	}
	
	void level(int level) {
		assert !bodies
		element.level = level
		if (level == 88) {
			element.usage = Usage.CONDITION_NAME
		}
	}
	
	void name(String name) {
		assert !bodies || [ BODY_OCCURS, BODY_INDEXED_BY ]
		if (!bodies) {
			element.name = name
		} else {
			element.occursSpecification.indexElements[-1].name = name
		}
	}
	
	void nullable() {
		assert !bodies
		element.nullable = true
	}
	
	void occurs(int count) {
		assert !bodies
		assert !element.occursSpecification
		element.occursSpecification = SchemaFactory.eINSTANCE.createOccursSpecification()
		element.occursSpecification.count = count
	}
	
	void occurs(Closure definition) {
		assert !bodies
		bodies << BODY_OCCURS
		runClosure(definition)
		assert bodies == [ BODY_OCCURS ]
		bodies -= BODY_OCCURS
	}
	
	void picture(String picture) {
		assert !bodies
		element.picture = picture
	}
	
	void redefines(String elementName) {
		assert !bodies
		assert !element.redefines, "element already redefines ${element.redefines.name}"
		assert element.level != 88, "redefines invalid for condition names"
		assert elementName != element.name, "element cannot redefine itself"
		List<Element> candidates = []
		if (parent) {
			candidates = parent.children	
		} else if (record) {
			candidates = record.elements
		}
		Element candidate = candidates.find { Element element -> element.name == elementName }
		assert candidate, "redefined element is NOT defined in record or (same) parent: $elementName"
		assert candidate.level == element.level, 'level of redefined element mismatch'
		element.redefines = candidate
	}
	
	void usage(String usage) {
		assert !bodies
		element.usage = Usage.valueOf(usage.replaceAll(' ', '_'))
	}
	
	void value(String value) {
		assert !bodies
		element.value = value
	}

}
