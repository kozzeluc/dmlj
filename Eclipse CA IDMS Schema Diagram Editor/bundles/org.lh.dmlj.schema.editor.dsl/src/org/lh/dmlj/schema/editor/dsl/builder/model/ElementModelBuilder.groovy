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

import java.util.List;

import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.IndexElement
import org.lh.dmlj.schema.SchemaFactory
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Usage

class ElementModelBuilder extends AbstractModelBuilder<Element> {
	
	private static final String ATTRIBUTE_DEPENDING_ON = 'dependingOn'
	private static final String ATTRIBUTE_INDEXED_BY = 'indexedBy'
	private static final String ATTRIBUTE_NULLABLE = 'nullable' // attribute without a value
	private static final String ATTRIBUTE_OCCURS = 'occurs'
	private static final String ATTRIBUTE_PICTURE = 'picture'
	private static final String ATTRIBUTE_REDEFINES = 'redefines' 
	private static final String ATTRIBUTE_USAGE = 'usage'
	private static final String ATTRIBUTE_VALUE = 'value'
	
	private static final String[] ATTRIBUTES = 
		[ ATTRIBUTE_DEPENDING_ON, ATTRIBUTE_INDEXED_BY, ATTRIBUTE_NULLABLE, ATTRIBUTE_OCCURS, 
		  ATTRIBUTE_PICTURE, ATTRIBUTE_REDEFINES, ATTRIBUTE_USAGE, ATTRIBUTE_VALUE ]
		
	private static final char QUOTE = "'"
	
	private String definition
	private Element element
	
	SchemaRecord record
	Element parent
	
	static int extractLevel(String line) {
		assert line, 'line is mandatory'
		String definition = filterDefinition(line)
		int i = definition.indexOf(' ') - 1
		Integer.valueOf(definition[0..i])
	}
	
	static String filterDefinition(String definition) {
		definition.trim().replace('\t', ' ')
	}
	
	private static String stripLeadingAndTrailing(String aString, char _char) {
		if (aString && aString.length() > 1 && aString[0] == _char && aString[-1] == _char) {
			return aString[1..-2]
		} else {
			return aString
		}
	}
	
	Element build(String definition) {
		
		this.definition = filterDefinition(definition)
		
		int level = extractLevel(this.definition)
		def (name, baseName) = extractNames()
		String picture = extractAttribute(ATTRIBUTE_PICTURE, false)
		String usage = extractAttribute(ATTRIBUTE_USAGE, true)
		String redefinedElementName = extractAttribute(ATTRIBUTE_REDEFINES, false)
		String value = extractAttribute(ATTRIBUTE_VALUE, false)
		int occurs = Integer.valueOf(extractAttribute(ATTRIBUTE_OCCURS, false) ?: '0')
		boolean nullable = definition.endsWith('nullable') || definition.indexOf(' nullable ') > -1
		
		element = SchemaFactory.eINSTANCE.createElement()
		element.name = name
		element.baseName = baseName
		element.level = level
		element.picture = picture
		if (level == 88) {
			element.usage = Usage.CONDITION_NAME
		} else if (usage) {
			element.usage = Usage.valueOf(usage.replace(' ', '_'))
		} else {
			element.usage = Usage.DISPLAY
		}
		element.value = value
		if (occurs) {
			element.occursSpecification = SchemaFactory.eINSTANCE.createOccursSpecification()
			element.occursSpecification.count = occurs
			String occursDependingOnElementName = extractAttribute(ATTRIBUTE_DEPENDING_ON, false)
			if (occursDependingOnElementName) {				
				assert occursDependingOnElementName != element.name, "occurs cannot depend on same element"
				Element candidate = findElement(occursDependingOnElementName)
				assert candidate, "occurs depending on element is NOT defined in record or parent: $occursDependingOnElementName"
				assert candidate.level != 88, 'depending on element cannot be a condition name'
				element.occursSpecification.dependingOn = candidate	
			}
			String indexedBy = extractAttribute(ATTRIBUTE_INDEXED_BY, false)
			if (indexedBy) {
				List<String[]> indexNames = extractIndexNames(indexedBy)	
				for (String[] indexNameCombination : indexNames) {
					IndexElement indexElement = SchemaFactory.eINSTANCE.createIndexElement()
					element.occursSpecification.indexElements << indexElement
					indexElement.name = indexNameCombination[0]
					if (indexNameCombination[1]) {
						indexElement.baseName = indexNameCombination[1]
					}
				}
			}
		}
		element.nullable = nullable
		if (redefinedElementName) {
			assert element.level != 88, 'redefines invalid for condition names'
			assert redefinedElementName != element.name, 'element cannot redefine itself'
			Element candidate = findElement(redefinedElementName)
			assert candidate, "redefined element is NOT defined in record or (same) parent: $redefinedElementName"			
			assert candidate.level == element.level, 'level of redefined element mismatch'
			element.redefines = candidate
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
	
	private String[] extractNames() {
		String name
		String baseName
		
		int i = definition.indexOf(' ')
		int j = definition.indexOf(' ', i + 1)
		if (j > -1) {
			name = definition[i + 1..j - 1]
			int k = definition.indexOf(' (', i + 1)
			if (k == j) {
				k = definition.indexOf(')', i + 1)
				baseName = definition[j + 2..k - 1]
			} else {
				baseName = name
			}
		} else {
			name = definition[i + 1..-1]
			baseName = name
		}
		assert name, 'element name is NOT set'
		assert baseName, 'element base name is NOT set'
		[ name, baseName ]
	}
	
	private String extractAttribute(String attribute, boolean stripSingleQuotes) {
		stripSingleQuotes ? stripLeadingAndTrailing(extract(" $attribute "), QUOTE) : extract(" $attribute ")
	}
	
	private String extract(String searchItem) {
		int i = definition.indexOf(searchItem)
		if (i > -1) {
			int j = -1
			int k = -1
			for (String anAttribute in ATTRIBUTES) {
				k = definition.indexOf(" $anAttribute", i + searchItem.length())
				if (k > -1 && (j == -1 || k < j)) {
					j = k
				}
			}
			if (j > -1) {
				return definition[i + searchItem.length()..j - 1]
			} else {
				return definition[i + searchItem.length()..-1]
			}
		}
		null
	}
	
	private List<String[]> extractIndexNames(indexedBy) {
		List<String[]> indexNames = [ ]
		for (String token in indexedBy.split(',')) {
			String[] indexNameCombination = new String[2]
			indexNames << indexNameCombination
			String p = token.trim()
			int i = p.indexOf(' (')	
			if (i > -1) {
				assert p[-1] == ')', "missing closing bracket: $p"
				indexNameCombination[0] = p[0..(i - 1)].trim()
				indexNameCombination[1] = p[i + 2..-2]
			} else {
				indexNameCombination[0] = p
				indexNameCombination[1] = null
			}
		}
		indexNames
	}

	private Element findElement(String name) {
		List<Element> candidates = []
		if (parent) {
			findElement(name, parent.children)
		} else if (record) {
			findElement(name, record.elements)
		} else {
			null
		}
	}
	
	private Element findElement(String name, List<Element> candidates) {
		for (Element candidate : candidates) {
			if (candidate.name == name) {
				return candidate	
			} else if (candidate.children) {
				Element someChildOfCandidate = findElement(name, candidate.children)
				if (someChildOfCandidate) {
					return someChildOfCandidate
				}
			}
		}
	}

}
