/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.lh.dmlj.schema.editor.Plugin;

public class ValidForExtensionElement extends AbstractExtensionElement {
	private String schemaName;
	private short[]	schemaVersions;

	public ValidForExtensionElement(IConfigurationElement configElement) {
		super(configElement);
		Assert.isTrue(configElement.getName().equals(ExtensionPointConstants.ELEMENT_VALID_FOR),
				"wrong IConfigurationElement: " + configElement.getName());		
	}

	public boolean isValidFor(String schemaName, short schemaVersion) {		
		if (!getSchemaName().equals(schemaName.toUpperCase())) {
			return false;
		}
		if (getSchemaVersions().length == 0) {
			return true;
		}
		for (var version : getSchemaVersions()) {
			if (schemaVersion == version) {
				return true;
			}
		}
		return false;
	}

	private void reportInvalidSchemaVersions() {
		var plugin = configElement.getDeclaringExtension().getNamespaceIdentifier();
		var layoutManager = Util.getAttribute((IConfigurationElement) configElement.getParent(), ExtensionPointConstants.ATTRIBUTE_NAME, null);
		var extensionSchemaVersions = Util.getAttribute(configElement, ExtensionPointConstants.ATTRIBUTE_SCHEMA_VERSIONS, null);
		Plugin.getDefault().getLog().warn("Invalid schemaVersions attribute; contributing plug-in=\"" + plugin + 
				"\" layoutManager=\"" + layoutManager + "\" validFor.schemaName=\"" + schemaName + 
				"\" validFor.schemaVersions=\"" + extensionSchemaVersions + "\"");
	}

	public String getSchemaName() {		
		if (schemaName == null) {
			schemaName =  Util.getAttribute(configElement, ExtensionPointConstants.ATTRIBUTE_SCHEMA_NAME, null).toUpperCase();			
		}				
		return schemaName;
	}

	public short[] getSchemaVersions() {
		if (schemaVersions == null) {
			var schemaVersionsAttribute = Util.getAttribute(configElement, ExtensionPointConstants.ATTRIBUTE_SCHEMA_VERSIONS, "");
			// The schemaVersions attribute contains either 1 schema version number, a range of version numbers
			// (e.g. "1-100") or a comma-separated list of version numbers (e.g. "1,2,3") - we construct a sorted
			// array that contains ALL valid version numbers; an empty array means that any version number is
			// valid and will also be taken into account if the schemaVersions attribute cannot be interpreted
			// correctly, for whatever reason.  Schema version numbers must be unsigned integers in the range 1
			// through 9999..
			if (schemaVersionsAttribute.isEmpty()) {
				setDefaultSchemaVersions();				 
			} else if (schemaVersionsAttribute.contains("-")) {
				setSchemaVersionsFromRange(schemaVersionsAttribute);
			} else if (schemaVersionsAttribute.contains(",")) {
				setSchemaVersionsFromList(schemaVersionsAttribute);
			} else {
				setSchemaVersionsFromSingleValue(schemaVersionsAttribute);
			}
		}
		return schemaVersions;
	}
	
	private void setDefaultSchemaVersions() {
		schemaVersions = new short[] {};
	}
	
	private void setSchemaVersionsFromRange(String schemaVersionsAttribute) {
		var i = schemaVersionsAttribute.indexOf("-");
		var q = schemaVersionsAttribute.substring(0, i).trim();  // left part (lower value)
		var r = schemaVersionsAttribute.substring(i + 1).trim(); // right part (higher value)
		try {
			var lowVersion = Short.parseShort(q);
			var highVersion = Short.parseShort(r);
			if (lowVersion < 1 || lowVersion > 9999 || highVersion < 1 || highVersion > 9999 || lowVersion > highVersion) {
				// at least 1 of the 2 version numbers in the range is not a valid schema number or the left
				// value is higher than the right one; discard the schemaVersions attribute
				schemaVersions = new short[] {};
				reportInvalidSchemaVersions();
			} else if (lowVersion == highVersion) {
				schemaVersions = new short[] { lowVersion };
			} else if (lowVersion == 1 && highVersion == 9999) {
				// if the extension specifies 1-9999 as its range, instantiate an empty array; its effect will be
				// the same as that of an array with 9999 elements
				schemaVersions = new short[] {};
			} else {
				schemaVersions = new short[highVersion - lowVersion + 1];
				for (var j = 0; j < schemaVersions.length; j++) {
					schemaVersions[j] = (short) (lowVersion + j);
				}
			}
		} catch (NumberFormatException e) {
			// at least 1 of the 2 version numbers in the range is not a numeric value; discard the
			// schemaVersions attribute
			schemaVersions = new short[] {};
			reportInvalidSchemaVersions();
		}		
	}
	
	private void setSchemaVersionsFromList(String schemaVersionsAttribute) {
		var tokenizer = new StringTokenizer(schemaVersionsAttribute, ",");
		var values = new ArrayList<Short>();
		try {
			var discard = false;
			while (tokenizer.hasMoreTokens()) {
				var q = tokenizer.nextToken().trim();
				var j = Short.valueOf(q);
				if (j < 1 || j > 9999) {
					discard = true;
					break;						
				} else {
					values.add(j);
				}
			}
			if (!discard) {
				Collections.sort(values);
				schemaVersions = new short[values.size()];
				for (int j = 0; j < schemaVersions.length; j++) {
					schemaVersions[j] = values.get(j).shortValue();
				}
			} else {
				// at least value in the version number list is not a valid schema number; discard the
				// schemaVersions attribute
				schemaVersions = new short[] {};
				reportInvalidSchemaVersions();
			}
		} catch (NumberFormatException e) {
			// at least 1 value in the version number list is not a numeric value; discard the schemaVersions
			// attribute
			schemaVersions = new short[] {};
			reportInvalidSchemaVersions();
		}		
	}
	
	private void setSchemaVersionsFromSingleValue(String schemaVersionsAttribute) {
		try {
			var version = Short.valueOf(schemaVersionsAttribute);
			if (version < 1 || version > 9999) {
				// the version number is not valid; discard the schemaVersions attribute
				schemaVersions = new short[] {};
				reportInvalidSchemaVersions();
			} else {
				schemaVersions = new short[] { version };
			}
		} catch (NumberFormatException e) {
			// the version number is not a numeric value; discard the schemaVersions attribute
			schemaVersions = new short[] {};
			reportInvalidSchemaVersions();
		}		
	}
	
}
