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

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class SetDataCollector implements ISetDataCollector<SchemaSyntaxWrapper> {
	private static final String FOR = " FOR ";
	private static final String WITHIN_AREA2 = "WITHIN AREA ";
	private static final String OFFSET = " OFFSET ";
	private static final String WITHIN_AREA = "           WITHIN AREA ";
	private static final String MEMBER_IS = "         MEMBER IS ";
	private static final String OWNER_IS = "         OWNER IS ";
	private static final String PRIOR_DBKEY_POSITION_IS = "             PRIOR DBKEY POSITION IS ";
	private static final String NEXT_DBKEY_POSITION_IS = "             NEXT DBKEY POSITION IS ";
	private static final String PAGES = " PAGES";
	private static final String BLOCK_CONTAINS = " BLOCK CONTAINS ";

	@Override
	public Short getDisplacementPageCount(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         MODE IS INDEX ") && line.contains(BLOCK_CONTAINS) &&
				line.contains(" KEYS DISPLACEMENT IS ") && line.contains(PAGES)) {				
				
				var i = line.indexOf(" KEYS DISPLACEMENT IS ");
				var j = line.indexOf(PAGES);
				return Short.valueOf(line.substring(i + 22, j).trim());
			}
		}
		return null;
	}

	@Override
	public DuplicatesOption getDuplicatesOption(SchemaSyntaxWrapper context, String memberRecordName) {
		var i = indexOf("                 DUPLICATES ARE ", context, memberRecordName);
		var line = context.getLines().get(i);
		if (line.startsWith("BY DBKEY", 32)) {
			return DuplicatesOption.BY_DBKEY;
		} else if (line.startsWith("FIRST", 32)) {
			return DuplicatesOption.FIRST;
		} else if (line.startsWith("LAST", 32)) {
			return DuplicatesOption.LAST;
		} else if (line.startsWith("NOT ALLOWED", 32)) {
			return DuplicatesOption.NOT_ALLOWED;
		} else if (line.startsWith("UNORDERED", 32)) {
			return DuplicatesOption.UNORDERED;
		} else {
			return null;
		}
	}

	@Override
	public Short getKeyCount(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         MODE IS INDEX ") && line.contains(BLOCK_CONTAINS) && line.contains(" KEYS")) {				
				var i = line.indexOf(BLOCK_CONTAINS);
				var j = line.indexOf(" KEYS");
				return Short.valueOf(line.substring(i + 16, j).trim());
			}
		}
		return null;
	}

	@Override
	public Short getMemberIndexDbkeyPosition(SchemaSyntaxWrapper context) {
		// for indexed sets only; indexed sets always have exactly 1 member		
		var memberRecordName = getMemberRecordNames(context).toArray(new String[] {})[0];				
		var i = indexOf("             INDEX DBKEY POSITION IS ", context, memberRecordName);
		var p = context.getLines().get(i).substring(37).trim();
		if (p.equals("OMITTED")) {
			return null;
		} else {
			return Short.valueOf(p);
		}		
	}

	@Override
	public Short getMemberNextDbkeyPosition(SchemaSyntaxWrapper context, String memberRecordName) {
		var i = indexOf(NEXT_DBKEY_POSITION_IS, context, memberRecordName);
		var p = context.getLines().get(i).substring(36).trim();
		return Short.valueOf(p);
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(SchemaSyntaxWrapper context, String memberRecordName) {
		var i = indexOf("             OWNER DBKEY POSITION IS ", context, memberRecordName);
		if (i > -1) {
			var p = context.getLines().get(i).substring(37).trim();
			return Short.valueOf(p);
		} else {
			return null;
		}
	}

	@Override
	public Short getMemberPriorDbkeyPosition(SchemaSyntaxWrapper context, String memberRecordName) {
		var i = indexOf(PRIOR_DBKEY_POSITION_IS, context, memberRecordName);
		if (i > -1) {
			var p = context.getLines().get(i).substring(37).trim();
			return Short.valueOf(p);
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getMemberRecordNames(SchemaSyntaxWrapper context) {
		var list = new ArrayList<String>();
		for (var line : context.getLines()) {
			if (line.startsWith(MEMBER_IS)) {
				var memberRecordName = line.substring(19).trim(); 
				list.add(memberRecordName);
			}
		}
		return list;
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		return context.getLines().get(1).substring(17).trim();
	}

	@Override
	public short getOwnerNextDbkeyPosition(SchemaSyntaxWrapper context) {
		var i = 0;
		while (!context.getLines().get(i).startsWith(OWNER_IS)) {
			i += 1;
		}
		i += 1;
		
		while (!context.getLines().get(i).startsWith(NEXT_DBKEY_POSITION_IS) && !context.getLines().get(i).startsWith(MEMBER_IS) ) {
			i += 1;
		}
		if (context.getLines().get(i).startsWith(NEXT_DBKEY_POSITION_IS)) {
			var p = context.getLines().get(i).substring(36).trim();
			return Short.parseShort(p);
		} else {		
			return -1;
		}
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(SchemaSyntaxWrapper context) {
		var i = 0;
		while (!context.getLines().get(i).startsWith(OWNER_IS)) {
			i += 1;
		}
		i += 1;
		
		while (!context.getLines().get(i).startsWith(PRIOR_DBKEY_POSITION_IS) && !context.getLines().get(i).startsWith(MEMBER_IS) ) {
			i += 1;
		}
		if (context.getLines().get(i).startsWith(PRIOR_DBKEY_POSITION_IS)) {
			var p = context.getLines().get(i).substring(37).trim();
			return Short.valueOf(p);
		} else {		
			return null;
		}
	}

	@Override
	public String getOwnerRecordName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith(OWNER_IS) && !line.startsWith("         OWNER IS SYSTEM")) {
				return line.substring(18).trim();
			}
		}
		return null;
	}

	@Override
	public SetMembershipOption getSetMembershipOption(SchemaSyntaxWrapper context, String memberRecordName) {
		var i = indexOf(context, memberRecordName) + 1;
		while (i < context.getLines().size() && !context.getLines().get(i).startsWith(MEMBER_IS)) {
			var line = context.getLines().get(i);
			if (line.startsWith("             MANDATORY AUTOMATIC")) {
				return SetMembershipOption.MANDATORY_AUTOMATIC;
			} else if (line.startsWith("             MANDATORY MANUAL")) {
				return SetMembershipOption.MANDATORY_MANUAL;
			} else if (line.startsWith("             OPTIONAL AUTOMATIC")) {
				return SetMembershipOption.OPTIONAL_AUTOMATIC;
			} else if (line.startsWith("             OPTIONAL MANUAL")) {				
				return SetMembershipOption.OPTIONAL_MANUAL;
			}
			i += 1;
		}
		return null;
	}

	@Override
	public SetMode getSetMode(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         MODE IS ")) {
				if (line.startsWith("CHAIN", 17)) {
					return SetMode.CHAINED;
				} else if (line.startsWith("INDEX", 17)) {
					return SetMode.INDEXED;
				} else if (line.startsWith("VSAM INDEX", 17)) {
					return SetMode.VSAM_INDEX;
				}
			}
		}
		return null;
	}

	@Override
	public SetOrder getSetOrder(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         ORDER IS ")) {
				if (line.startsWith("FIRST", 18)) {
					return SetOrder.FIRST;
				} else if (line.startsWith("LAST", 18)) {
					return SetOrder.LAST;
				} else if (line.startsWith("NEXT", 18)) {
					return SetOrder.NEXT;
				} else if (line.startsWith("PRIOR", 18)) {
					return SetOrder.PRIOR;
				} else if (line.startsWith("SORTED", 18)) {
					return SetOrder.SORTED;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<String> getSortKeyElements(SchemaSyntaxWrapper context, String memberRecordName) {
		var list = new ArrayList<String>();		
		var i = indexOf("             KEY IS (", context, memberRecordName);		
		do {
			i += 1;
			var j = context.getLines().get(i).indexOf(" ", 17);
			var elementName = context.getLines().get(i).substring(17, j); 
			list.add(elementName);			
		} while (!context.getLines().get(i).trim().endsWith(" )"));
		return list;
	}

	@Override
	public SortSequence getSortSequence(SchemaSyntaxWrapper context, String memberRecordName, String keyElementName) {
		if (keyElementName != null) {
			// the set member is NOT sorted by dbkey
			var scanItem = "                 " + keyElementName + " ";
			var i = indexOf(scanItem, context, memberRecordName);
			var j = context.getLines().get(i).indexOf(" ", 17);
			var p = context.getLines().get(i).substring(j + 1).trim();
			if (p.startsWith("ASCENDING")) {
				return SortSequence.ASCENDING;
			} else if (p.startsWith("DESCENDING")) {
				return SortSequence.DESCENDING;
			} else {
				return null;
			}
		} else {
			// the set member IS sorted by dbkey
			var scanItem = "                 DBKEY DESCENDING";
			var i = indexOf(scanItem, context, memberRecordName);
			if (i > -1) {				
				return SortSequence.DESCENDING;
			} else {
				return SortSequence.ASCENDING;
			}
		}
	}

	@Override
	public String getSymbolicIndexName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.startsWith("         MODE IS INDEX USING ")) {				
				return line.substring(29).trim();
			}
		}
		return null;
	}

	@Override
	public String getSystemOwnerAreaName(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			// check for a WITHIN AREA clause, taking in mind that it can be commented out (meaning the system
			// owner is in the same area as the member record (but we return the area name found in the WITHIN
			// AREA clause)
			if (line.length() > 2 && line.startsWith(WITHIN_AREA, 2)) {				
				var p = line.substring(25).trim();
				var i = p.indexOf(" ");
				if (i > -1) {
					return p.substring(0, i);
				} else {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(SchemaSyntaxWrapper context) {
		// [...] WITHIN AREA EMP-DEMO-REGION OFFSET 1 PAGES FOR 4 PAGES
		for (var line : context.getLines()) {
			if (line.contains(WITHIN_AREA2) && line.contains(OFFSET) && line.contains(" PAGES FOR ")) {
				var i = line.indexOf(OFFSET);
				var j = line.indexOf(" PAGES FOR ", i);
				return Integer.valueOf(line.substring(i + 8, j).trim());
			}
		}
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(SchemaSyntaxWrapper context) {
		// [...] WITHIN AREA EMP-DEMO-REGION OFFSET 5 PERCENT FOR 4 PAGES
		for (var line : context.getLines()) {
			if (line.contains(WITHIN_AREA2) && line.contains(OFFSET) && line.contains(" PERCENT FOR ")) {
				var i = line.indexOf(OFFSET);
				var j = line.indexOf(" PERCENT FOR ", i);
				return Short.valueOf(line.substring(i + 8, j).trim());
			}
		}
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(SchemaSyntaxWrapper context) {
		// [...] WITHIN AREA EMP-DEMO-REGION OFFSET 5 PERCENT FOR 4 PAGES
		for (var line : context.getLines()) {
			if (line.contains(WITHIN_AREA2) && line.contains(OFFSET) && line.contains(FOR) && line.trim().endsWith(PAGES)) {
				var i = line.indexOf(FOR);
				var j = line.indexOf(PAGES, i);
				return Integer.valueOf(line.substring(i + 5, j).trim());				
			}
		}
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetPercent(SchemaSyntaxWrapper context) {
		// [...] WITHIN AREA EMP-DEMO-REGION OFFSET 5 PERCENT FOR 5 PERCENT
		for (var line : context.getLines()) {
			if (line.contains(WITHIN_AREA2) && line.contains(OFFSET) && line.contains(FOR) && line.trim().endsWith(" PERCENT")) {
				var i = line.indexOf(FOR);
				var j = line.indexOf(" PERCENT", i);
				return Short.valueOf(line.substring(i + 5, j).trim());				
			}
		}
		return null;
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(SchemaSyntaxWrapper context) {
		// [..] WITHIN AREA ALMAI102 SUBAREA AREA1
		for (var line : context.getLines()) {
			if (line.contains(WITHIN_AREA2) && line.contains(" SUBAREA ")) {
			    var i = line.indexOf(" SUBAREA ");
				return line.substring(i + 9).trim();
			}
		}
		return null;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(SchemaSyntaxWrapper context, String memberRecordName) {
		var scanItem = "                 NATURAL SEQUENCE";
		return indexOf(scanItem, context, memberRecordName) > -1;
	}

	private int indexOf(SchemaSyntaxWrapper context, String memberRecordName) {
		var scanItem = MEMBER_IS + memberRecordName;
		var i = 0;
		while (i < context.getLines().size() && !context.getLines().get(i).startsWith(scanItem)) {
			i += 1;
		}
		if (i < context.getLines().size()) {
			return i;
		}
		throw new IllegalStateException("logic error: no line found starting with '         MEMBER IS ' for member record " +
				memberRecordName + " (set=" + getName(context) + ")");
}	
	
	private int indexOf(String scanItem, SchemaSyntaxWrapper context, String memberRecordName) {
		var i = indexOf(context, memberRecordName) + 1;
		while (i < context.getLines().size() && !context.getLines().get(i).startsWith(MEMBER_IS)) {
			var line = context.getLines().get(i);
			if (line.startsWith(scanItem)) {
				return i;
			}
			i += 1;
		}
		return -1;
		
	}	
	
	@Override
	public boolean isKeyCompressed(SchemaSyntaxWrapper context) {
		for (var line : context.getLines()) {
			if (line.trim().equals("COMPRESSED") || line.trim().equals("UNCOMPRESSED")) {
				return line.trim().equals("COMPRESSED");
			}
		}
		return false;
	}

	@Override
	public boolean isSortedByDbkey(SchemaSyntaxWrapper context) {
		// for indexed sets only; indexed sets always have exactly 1 member
		for (var line : context.getLines()) {
			if (line.trim().equals("DBKEY ASCENDING") || line.trim().equals("DBKEY DESCENDING")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSystemOwned(SchemaSyntaxWrapper context) {
		return context.getLines().stream()
				.anyMatch(line -> line.startsWith("         OWNER IS SYSTEM"));
	}	
	
}
