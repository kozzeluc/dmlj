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
package org.lh.dmlj.schema.editor.importtool.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class ElementDataCollector implements IElementDataCollector<SchemaSyntaxWrapper> {
	private static final String INDEXED_BY = "INDEXED BY ( ";
	private static final String OCCURS = " OCCURS ";
	private static final String SUFFIX = "suffix";

	private static String getFullName(String elementName, SchemaSyntaxWrapper context) {
		if (elementName.equals("FILLER")) {
			return elementName;
		}		
		var containsBaseNamesFlag = Boolean.parseBoolean(context.getProperties().getProperty("containsBaseNamesFlag"));
		
		var p = new StringBuilder();
		if (context.getProperties().containsKey("prefix") && containsBaseNamesFlag) {
			var prefix = context.getProperties().getProperty("prefix");
			p.append(prefix);
		}
		if (context.getProperties().containsKey("baseSuffix") && containsBaseNamesFlag) {
			// remove the base suffix from the element name and add the remaining part to the full element name
			var baseSuffix = context.getProperties().getProperty("baseSuffix");
			var i = elementName.lastIndexOf(baseSuffix);
			if (i < 0) {
				throw new IllegalStateException("logic error: base suffix (" + baseSuffix + ") not in element name (" + elementName + ")");
			}
			p.append(elementName.substring(0, i));
		} else {
			p.append(elementName);
		}
		if (context.getProperties().containsKey(SUFFIX) && containsBaseNamesFlag) {
			var suffix = context.getProperties().getProperty(SUFFIX);
			p.append(suffix);
		}		
		return p.toString();
	}

	@Override
	public String getBaseName(SchemaSyntaxWrapper context) {
		var p = context.getLines().get(0).trim();
		var q = p.substring(p.lastIndexOf(" ") + 1);
		if (!context.getProperties().containsKey(SUFFIX) || q.equals("FILLER")) {
			return q;
		}
		var suffix = context.getProperties().getProperty(SUFFIX);
		var containsBaseNamesFlag = Boolean.parseBoolean(context.getProperties().getProperty("containsBaseNamesFlag"));
		if (suffix == null || containsBaseNamesFlag) {
			return q;
		} else {
			return q.substring(0, q.lastIndexOf(suffix));
		}
	}

	@Override
	public String getDependsOnElementName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			if (line.indexOf(OCCURS) > -1 && line.indexOf(" TIMES ") > -1 && line.contains(" DEPENDING ON ")) {
				var i = line.indexOf(" DEPENDING ON ");
				var elementName = line.substring(i + 14).trim();
				return getFullName(elementName, context);
			}
		}
		return null;
	}

	@Override
	public Collection<String> getIndexElementBaseNames(SchemaSyntaxWrapper context) {
		var list = new ArrayList<String>();		
		int i = 0;
		while (i < context.getLines().size() && !context.getLines().get(i).contains(INDEXED_BY)) {
			i += 1;
		}
		if (i >= context.getLines().size()) {
			return list;
		}
		while (i < context.getLines().size() && !context.getLines().get(i).trim().equals(")")) {
			var line = context.getLines().get(i);
			list.addAll(getTokens(line));
			if (line.trim().endsWith(" )")) {
				// we've processed all elements
				break;
			} 
			i += 1;
		} 		
		return list;
	}
	
	private List<String> getTokens(String line) {
		var tokens = new ArrayList<String>();
		int j;
		if (line.contains(INDEXED_BY)) {
			// first line
			j = line.indexOf(INDEXED_BY) + 13;
		} else {
			j = 2;
			while (line.charAt(j) == ' ') {
				j += 1;
			}
		}
		var p =new StringBuilder(line.substring(j).trim());
		if (p.toString().endsWith(" )")) {
			p.setLength(p.length() - 2);
		}
		var tokenizer = new StringTokenizer(p.toString());
		while (tokenizer.hasMoreTokens()) {
			tokens.add(tokenizer.nextToken());
		}
		return tokens;
	}

	@Override
	public Collection<String> getIndexElementNames(SchemaSyntaxWrapper context) {
		return getIndexElementBaseNames(context).stream()
				.map(baseName -> getFullName(baseName, context))
				.toList();
	}

	@Override
	public boolean getIsNullable(SchemaSyntaxWrapper context) {
		// this is only applicable for catalog tables, which we don't support
		return false;
	}

	@Override
	public short getLevel(SchemaSyntaxWrapper context) {
		var p = context.getLines().get(0).substring(2).trim();		
		return Short.parseShort(p.substring(0, p.indexOf(" ")));
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		var p = context.getLines().get(0).trim();
		return getFullName(p.substring(p.lastIndexOf(" ") + 1), context);
	}

	@Override
	public short getOccurrenceCount(SchemaSyntaxWrapper context) {
		// examples:
		// *+           OCCURS 48 TIMES
		// *+       OCCURS 0 TO 500 TIMES DEPENDING ON SRHVSIZE-139
		for (var line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			if (line.indexOf(OCCURS) > 1 && line.indexOf(" TIMES") > 1) {	
				var i = line.indexOf(OCCURS);
				var j = line.indexOf(" TIMES", i);
				var p = line.substring(i + 8, j).trim();
				if (line.indexOf(" TIMES DEPENDING ON ") < 0) {
					// not an OCCURS DEPENDING ON
					return Short.parseShort(p);
				} else {
					// OCCURS DEPENDING ON
					var k = p.indexOf(" TO ");
					return Short.valueOf(p.substring(k + 4));
				}
			}
		}
		return 0;
	}

	@Override
	public String getPicture(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			var i = line.indexOf(" PICTURE IS ");
			if (i > -1) {
				return line.substring(i + 12).trim();
			}
		}
		return null;
	}

	@Override
	public String getRedefinedElementName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			var i = line.indexOf(" REDEFINES ");
			if (i > -1) {								
				var elementName = line.substring(i + 11).trim();
				return getFullName(elementName, context);
			}
		}
		return null;
	}

	@Override
	public Usage getUsage(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.trim().endsWith(".")) {
				// don't process the syntax for subordinate elements
				break;
			}
			var i = line.indexOf(" USAGE IS ");
			if (i > -1) {								
				var p = line.substring(i + 10).trim();
				// note: this is likely to fail on some USAGEs that are not covered (yet); see the IDD ref guide
				if (p.equals("COMP")) {
					return Usage.COMPUTATIONAL;
				} else if (p.equals("COMP-1")) {
					return Usage.COMPUTATIONAL_1;
				} else if (p.equals("COMP-3")) {
					return Usage.COMPUTATIONAL_3;
				} else {
					return Usage.valueOf(p.replace("-", "_"));
				}
			}
		}
		return null;
	}

	@Override
	public List<String> getValues(SchemaSyntaxWrapper context) {
		var values = new ArrayList<String>();
		var capturing = false;
		for (var line : context.getLines()) {
			var uncommentedTrimmedLine = line.trim().substring(2).trim();
			if (uncommentedTrimmedLine.endsWith(".")) {
				// make sure that, in the case of group elements or elements described with 1 or more condition
				// names, no value of those subordinate or condition name elements can be assigned as a value
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
