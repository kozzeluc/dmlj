/**
 * Copyright (C) 2013  Luc Hermans
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
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;

public class ValidForExtensionElement extends AbstractExtensionElement {
	
	private String	schemaName;
	private short[]	schemaVersions;

	public ValidForExtensionElement(IConfigurationElement configElement) {
		super(configElement);
		Assert.isTrue(configElement.getName()
				   				   .equals(ExtensionPointConstants.ELEMENT_VALID_FOR), 
				      "wrong IConfigurationElement: " + configElement.getName());		
	}

	public boolean isValidFor(String schemaName, short schemaVersion) {		
		if (!getSchemaName().equals(schemaName.toUpperCase())) {
			return false;
		}
		if (getSchemaVersions().length == 0) {
			return true;
		}
		for (short version : getSchemaVersions()) {
			if (schemaVersion == version) {
				return true;
			}
		}
		return false;
	}

	private void reportInvalidSchemaVersions() {
		String plugin = 
			configElement.getDeclaringExtension().getNamespaceIdentifier();
		String layoutManager = 
			Util.getAttribute((IConfigurationElement) configElement.getParent(), 
							  ExtensionPointConstants.ATTRIBUTE_NAME, null);
		String schemaVersions =
			Util.getAttribute(configElement, 
							  ExtensionPointConstants.ATTRIBUTE_SCHEMA_VERSIONS, 
							  null);
		System.out.println("Invalid schemaVersions attribute; contributing " +
						   "plug-in=\"" + plugin + "\" layoutManager=\"" + 
						   layoutManager + "\" validFor.schemaName=\"" + 
						   schemaName + "\" validFor.schemaVersions=\"" +
						   schemaVersions + "\"");
	}

	public String getSchemaName() {		
		if (schemaName == null) {
			// convert the schema name to uppercase
			schemaName = 
				Util.getAttribute(configElement, 
					   			  ExtensionPointConstants.ATTRIBUTE_SCHEMA_NAME, 
					   			  null) // schemaName is a mandatory attribute
					.toUpperCase();			
		}				
		return schemaName;
	}

	public short[] getSchemaVersions() {
		if (schemaVersions == null) {
			String p = 
				Util.getAttribute(configElement, 
				   			  	  ExtensionPointConstants.ATTRIBUTE_SCHEMA_VERSIONS, "");
			// The schemaVersions attribute contains either 1 schema version number,
			// a range of version numbers (e.g. "1-100") or a comma-separated list  
			// of version numbers (e.g. "1,2,3") - we construct a sorted array that 
			// contains ALL valid version numbers; an empty array means that any 
			// version number is valid and will also be taken into account if the 
			// schemaVersions attribute cannot be interpreted correctly, for 
			// whatever reason.  Schema version numbers must be unsigned integers in 
			// the range 1 through 9999..
			if (p.equals("")) {
				// all version numbers are valid since no schemaVersions attribute 
				// is specified			
				schemaVersions = new short[] {}; 
			} else if (p.indexOf("-") > -1) {
				// a version number range is specified in the schemaVersions 
				// attribute
				int i = p.indexOf("-");
				String q = p.substring(0, i).trim();  // left part (lower value)
				String r = p.substring(i + 1).trim(); // right part (higher value)
				try {
					short lowVersion = Short.valueOf(q);
					short highVersion = Short.valueOf(r);
					if (lowVersion < 1 || lowVersion > 9999 ||
						highVersion < 1 || highVersion > 9999) {
						
						// at least 1 of the 2 version numbers in the range is not a
						// valid schema number; discard the schemaVersions attribute
						schemaVersions = new short[] {};
						reportInvalidSchemaVersions();
					} else if (lowVersion == highVersion) {
						schemaVersions = new short[] {lowVersion};
					} else if (lowVersion < highVersion) {
						if (lowVersion == 1 && highVersion == 9999) {
							// if the extension specifies 1-9999 as its range, 
							// instantiate an empty array; its effect will be the
							// same as that of an array with 9999 elements
							schemaVersions = new short[] {};
						} else {
							schemaVersions = new short[highVersion - lowVersion + 1];
							for (short j = 0; j < schemaVersions.length; j++) {
								schemaVersions[j] = (short) (lowVersion + j);
							}
						}
					} else {
						// the left value is higher than the right one; discard the 
						// schemaVersions attribute
						schemaVersions = new short[] {};
						reportInvalidSchemaVersions();
					}
				} catch (NumberFormatException e) {
					// at least 1 of the 2 version numbers in the range is not a
					// numeric value; discard the schemaVersions attribute
					schemaVersions = new short[] {};
					reportInvalidSchemaVersions();
				}
			} else if (p.indexOf(",") > -1) {
				// a version number list is specified in the schemaVersions 
				// attribute
				StringTokenizer tokenizer = new StringTokenizer(p, ",");
				List<Short> values = new ArrayList<>();
				try {
					boolean discard = false;
					while (tokenizer.hasMoreTokens()) {
						String q = tokenizer.nextToken().trim();
						Short j = Short.valueOf(q);
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
						// at least value in the version number list is not a valid
						// schema number; discard the schemaVersions attribute
						schemaVersions = new short[] {};
						reportInvalidSchemaVersions();
					}
				} catch (NumberFormatException e) {
					// at least 1 value in the version number list is not a numeric
					// value; discard the schemaVersions attribute
					schemaVersions = new short[] {};
					reportInvalidSchemaVersions();
				}
			} else {
				// only 1 version number specified in the schemaVersions attribute
				try {
					short version = Short.valueOf(p);
					if (version < 1 || version > 9999) {
						// the version number is not valid; discard the 
						// schemaVersions attribute
						schemaVersions = new short[] {};
						reportInvalidSchemaVersions();
					} else {
						schemaVersions = new short[] {version};
					}
				} catch (NumberFormatException e) {
					// the version number is not a numeric value; discard the 
					// schemaVersions attribute
					schemaVersions = new short[] {};
					reportInvalidSchemaVersions();
				}
			}			
		}
		return schemaVersions;
	}
	
}
