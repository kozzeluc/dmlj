/**
 * Copyright (C) 2021  Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class ElementDataCollector 
	implements IElementDataCollector<SchemaSyntaxWrapper> {

	private static String getFullName(String elementName, 
									  SchemaSyntaxWrapper context) {
		
		if (elementName.equals("FILLER")) {
			return elementName;
		}		
		
		boolean containsBaseNamesFlag = 
			Boolean.valueOf(context.getProperties()
							   	   .getProperty("containsBaseNamesFlag"))
				   .booleanValue();			
		
		StringBuilder p = new StringBuilder();
		if (context.getProperties().containsKey("prefix") &&
			containsBaseNamesFlag) {
			
			String prefix = context.getProperties().getProperty("prefix");
			p.append(prefix);
		}
		if (context.getProperties().containsKey("baseSuffix") &&
			containsBaseNamesFlag) {
			
			// remove the base suffix from the element name and add the 
			// remaining part to the full element name
			
			String baseSuffix = 
				context.getProperties().getProperty("baseSuffix");
			int i = elementName.lastIndexOf(baseSuffix);
			if (i < 0) {
				throw new RuntimeException("logic error: base suffix (" +
										   baseSuffix +
										   ") not in element name (" +
										   elementName + ")");
			}
			p.append(elementName.substring(0, i));
		} else {
			p.append(elementName);
		}
		if (context.getProperties().containsKey("suffix") &&
			containsBaseNamesFlag) {			
			
			String suffix = context.getProperties().getProperty("suffix");
			p.append(suffix);
		}		
		return p.toString();	
		
	}
	
	public ElementDataCollector() {
		super();
	}

	@Override
	public String getBaseName(SchemaSyntaxWrapper context) {
		String p = context.getLines().get(0).trim();
		String q = p.substring(p.lastIndexOf(" ") + 1);
		if (!context.getProperties().containsKey("suffix") || 
			q.equals("FILLER")) {
			
			return q;
		}
		String suffix = context.getProperties().getProperty("suffix");
		boolean containsBaseNamesFlag = 
			Boolean.valueOf(context.getProperties()
								   .getProperty("containsBaseNamesFlag"))
				   .booleanValue();
		if (suffix == null || containsBaseNamesFlag) {
			return q;
		} else {
			return q.substring(0, q.lastIndexOf(suffix));
		}
	}

	@Override
	public String getDependsOnElementName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			if (line.indexOf(" OCCURS ") > -1 &&
				line.indexOf(" TIMES ") > -1 &&
				line.indexOf(" DEPENDING ON ") > -1) {
				
				int i = line.indexOf(" DEPENDING ON ");
				String elementName = line.substring(i + 14).trim();
				return getFullName(elementName, context);
			}
		}
		return null;
	}

	@Override
	public Collection<String> getIndexElementBaseNames(SchemaSyntaxWrapper context) {
		List<String> list = new ArrayList<>();		
		int i = 0;
		while (i < context.getLines().size() &&
			   context.getLines().get(i).indexOf("INDEXED BY ( ") < 0) {			
			
			i += 1;
		}
		if (i >= context.getLines().size()) {
			return list;
		}
		while (i < context.getLines().size()) {
			if (context.getLines().get(i).trim().equals(")")) {
				// we've processed all elements; no relevant data on this line
				break;
			}
			int j;
			if (context.getLines().get(i).indexOf("INDEXED BY ( ") > -1) {
				// first line
				j = context.getLines().get(i).indexOf("INDEXED BY ( ") + 13;
			} else {
				j = 2;
				while (context.getLines().get(i).charAt(j) == ' ') {
					j += 1;
				}
			}
			StringBuilder p = 
				new StringBuilder(context.getLines().get(i).substring(j).trim());
			if (p.toString().endsWith(" )")) {
				p.setLength(p.length() - 2);
			}
			StringTokenizer tokenizer = new StringTokenizer(p.toString());
			while (tokenizer.hasMoreTokens()) {
				list.add(tokenizer.nextToken());
			}
			if (context.getLines().get(i).trim().endsWith(" )")) {
				// we've processed all elements
				break;
			} 
			i += 1;
		} 		
		return list;
	}

	@Override
	public Collection<String> getIndexElementNames(SchemaSyntaxWrapper context) {
		List<String> list = new ArrayList<>();
		for (String baseName : getIndexElementBaseNames(context)) {
			list.add(getFullName(baseName, context));
		}
		return list;
	}

	@Override
	public boolean getIsNullable(SchemaSyntaxWrapper context) {
		// this is only applicable for catalog tables, which we don't support
		return false;
	}

	@Override
	public short getLevel(SchemaSyntaxWrapper context) {
		String p = context.getLines().get(0).substring(2).trim();		
		return Short.valueOf(p.substring(0, p.indexOf(" "))).shortValue();
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		String p = context.getLines().get(0).trim();
		return getFullName(p.substring(p.lastIndexOf(" ") + 1), context);
	}

	@Override
	public short getOccurrenceCount(SchemaSyntaxWrapper context) {
		// examples:
		// *+           OCCURS 48 TIMES
		// *+       OCCURS 0 TO 500 TIMES DEPENDING ON SRHVSIZE-139
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			if (line.indexOf(" OCCURS ") > 1 &&
				line.indexOf(" TIMES") > 1) {
			
				int i = line.indexOf(" OCCURS ");
				int j = line.indexOf(" TIMES", i);
				String p = line.substring(i + 8, j).trim();
				if (line.indexOf(" TIMES DEPENDING ON ") < 0) {
					// not an OCCURS DEPENDING ON
					return Short.valueOf(p).shortValue();
				} else {
					// OCCURS DEPENDING ON
					int k = p.indexOf(" TO ");
					return Short.valueOf(p.substring(k + 4));
				}
			}
		}
		return 0;
	}

	@Override
	public String getPicture(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			int i = line.indexOf(" PICTURE IS ");
			if (i > -1) {
				return line.substring(i + 12).trim();
			}
		}
		return null;
	}

	@Override
	public String getRedefinedElementName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			int i = line.indexOf(" REDEFINES ");
			if (i > -1) {								
				String elementName = line.substring(i + 11).trim();
				return getFullName(elementName, context);
			}
		}
		return null;
	}

	@Override
	public Usage getUsage(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			int i = line.indexOf(" USAGE IS ");
			if (i > -1) {								
				String p = line.substring(i + 10).trim();
				// TODO refine the syntax scanning because it is likely to fail
				// on some USAGEs; we should be able to do this using the
				// IDD ref guide
				if (p.equals("COMP")) {
					return Usage.COMPUTATIONAL;
				} else if (p.equals("COMP-1")) {
					return Usage.COMPUTATIONAL_1;
				} else if (p.equals("COMP-3")) {
					return Usage.COMPUTATIONAL_3;
				} else {
					return Usage.valueOf(p.replaceAll("-", "_"));
				}
			}
		}
		return null;
	}

	@Override
	public List<String> getValues(SchemaSyntaxWrapper context) {
		List<String> values = new ArrayList<>();
		boolean capturing = false;
		for (String line : context.getLines()) {
			String uncommentedTrimmedLine = line.trim().substring(2).trim();
			if (uncommentedTrimmedLine.endsWith(".")) {
				// make sure that, in the case of group elements or elements
				// described with 1 or more condition names, no value of those
				// subordinate or condition name elements can be assigned to as 
				// the a value
				break;
			} else if (!capturing && uncommentedTrimmedLine.startsWith("VALUE IS ( ") && uncommentedTrimmedLine.endsWith(" )")) {
				values.add(uncommentedTrimmedLine.substring(11, uncommentedTrimmedLine.length() - 2));
			} else if (!capturing && uncommentedTrimmedLine.startsWith("VALUE IS ( ")) {				
				capturing = true;
				values.add(uncommentedTrimmedLine.substring(11));
			} else if (capturing && uncommentedTrimmedLine.endsWith(" )")) {
				values.add(uncommentedTrimmedLine.substring(0, uncommentedTrimmedLine.length() - 2));
				capturing = false;
			} else if (capturing) {
				values.add(uncommentedTrimmedLine);
			}
		}
		return values;
	}
	
}
