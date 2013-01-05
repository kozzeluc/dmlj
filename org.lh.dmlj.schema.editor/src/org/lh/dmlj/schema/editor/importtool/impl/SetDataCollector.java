package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class SetDataCollector 
	implements ISetDataCollector<SchemaSyntaxWrapper> {

	public SetDataCollector() {
		super();
	}

	@Override
	public Short getDisplacementPageCount(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         MODE IS INDEX ") &&
				line.indexOf(" BLOCK CONTAINS ") > -1 &&
				line.indexOf(" KEYS DISPLACEMENT IS ") > -1 &&
				line.indexOf(" PAGES") > -1) {				
				
				int i = line.indexOf(" KEYS DISPLACEMENT IS ");
				int j = line.indexOf(" PAGES");
				return Short.valueOf(line.substring(i + 22, j).trim());
			}
		}
		return null;
	}

	@Override
	public DuplicatesOption getDuplicatesOption(SchemaSyntaxWrapper context,
												String memberRecordName) {
		
		int i = indexOf("                 DUPLICATES ARE ", context, 
					    memberRecordName);
		
		String line = context.getLines().get(i);
		if (line.substring(32).startsWith("BY DBKEY")) {
			return DuplicatesOption.BY_DBKEY;
		} else if (line.substring(32).startsWith("FIRST")) {
			return DuplicatesOption.FIRST;
		} else if (line.substring(32).startsWith("LAST")) {
			return DuplicatesOption.LAST;
		} else if (line.substring(32).startsWith("NOT ALLOWED")) {
			return DuplicatesOption.NOT_ALLOWED;
		} else {
			return null;
		}
		
	}

	@Override
	public Short getKeyCount(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         MODE IS INDEX ") &&
				line.indexOf(" BLOCK CONTAINS ") > -1 &&
				line.indexOf(" KEYS") > -1) {				
				
				int i = line.indexOf(" BLOCK CONTAINS ");
				int j = line.indexOf(" KEYS");
				return Short.valueOf(line.substring(i + 16, j).trim());
			}
		}
		return null;
	}

	@Override
	public Short getMemberIndexDbkeyPosition(SchemaSyntaxWrapper context) {
		// for indexed sets only; indexed sets always have exactly 1 member		
		String memberRecordName =
			getMemberRecordNames(context).toArray(new String[] {})[0];				
		int i = indexOf("             INDEX DBKEY POSITION IS ", context, 
					    memberRecordName);
		String p = context.getLines().get(i).substring(37).trim();
		if (p.equals("OMITTED")) {
			return null;
		} else {
			return Short.valueOf(p);
		}		
	}

	@Override
	public Short getMemberNextDbkeyPosition(SchemaSyntaxWrapper context,
											String memberRecordName) {
		
		int i = indexOf("             NEXT DBKEY POSITION IS ", context, 
			    		memberRecordName);
		String p = context.getLines().get(i).substring(36).trim();
		return Short.valueOf(p);
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(SchemaSyntaxWrapper context,
											 String memberRecordName) {
		
		int i = indexOf("             OWNER DBKEY POSITION IS ", context, 
			    		memberRecordName);
		if (i > -1) {
			String p = context.getLines().get(i).substring(37).trim();
			return Short.valueOf(p);
		} else {
			return null;
		}
	}

	@Override
	public Short getMemberPriorDbkeyPosition(SchemaSyntaxWrapper context,
											 String memberRecordName) {
		
		int i = indexOf("             PRIOR DBKEY POSITION IS ", context, 
	    				memberRecordName);
		if (i > -1) {
			String p = context.getLines().get(i).substring(37).trim();
			return Short.valueOf(p);
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getMemberRecordNames(SchemaSyntaxWrapper context) {
		List<String> list = new ArrayList<>();
		for (String line : context.getLines()) {
			if (line.startsWith("         MEMBER IS ")) {
				
				String memberRecordName = line.substring(19).trim();; 
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
		
		int i = 0;
		while (!context.getLines().get(i).startsWith("         OWNER IS ")) {
			i += 1;
		}
		i += 1;
		
		while (!context.getLines().get(i).startsWith("             NEXT DBKEY POSITION IS ") &&
			   !context.getLines().get(i).startsWith("         MEMBER IS ") ) {
			
			i += 1;
		}
		if (context.getLines().get(i).startsWith("             NEXT DBKEY POSITION IS ")) {
			String p = context.getLines().get(i).substring(36).trim();
			return Short.valueOf(p).shortValue();
		} else {		
			return -1;
		}
		
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(SchemaSyntaxWrapper context) {
		
		int i = 0;
		while (!context.getLines().get(i).startsWith("         OWNER IS ")) {
			i += 1;
		}
		i += 1;
		
		while (!context.getLines().get(i).startsWith("             PRIOR DBKEY POSITION IS ") &&
			   !context.getLines().get(i).startsWith("         MEMBER IS ") ) {
			
			i += 1;
		}
		if (context.getLines().get(i).startsWith("             PRIOR DBKEY POSITION IS ")) {
			String p = context.getLines().get(i).substring(37).trim();
			return Short.valueOf(p);
		} else {		
			return null;
		}		
		
	}

	@Override
	public String getOwnerRecordName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         OWNER IS ") &&
				!line.startsWith("         OWNER IS SYSTEM")) {
				
				return line.substring(18).trim();
			}
		}
		return null;
	}

	@Override
	public SetMembershipOption getSetMembershipOption(SchemaSyntaxWrapper context, 
													  String memberRecordName) {
		
		int i = indexOf(context, memberRecordName) + 1;		                
		
		while (i < context.getLines().size() &&
			   !context.getLines().get(i).startsWith("         MEMBER IS ")) {
			
			String line = context.getLines().get(i);
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
		for (String line : context.getLines()) {
			if (line.startsWith("         MODE IS ")) {
				if (line.substring(17).startsWith("CHAIN")) {
					return SetMode.CHAINED;
				} else if (line.substring(17).startsWith("INDEX")) {
					return SetMode.INDEXED;
				}
			}
		}
		return null;
	}

	@Override
	public SetOrder getSetOrder(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         ORDER IS ")) {
				if (line.substring(18).startsWith("FIRST")) {
					return SetOrder.FIRST;
				} else if (line.substring(18).startsWith("LAST")) {
					return SetOrder.LAST;
				} else if (line.substring(18).startsWith("NEXT")) {
					return SetOrder.NEXT;
				} else if (line.substring(18).startsWith("PRIOR")) {
					return SetOrder.PRIOR;
				} else if (line.substring(18).startsWith("SORTED")) {
					return SetOrder.SORTED;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<String> getSortKeyElements(SchemaSyntaxWrapper context,
												 String memberRecordName) {
						
		List<String> list = new ArrayList<>();		
		int i = indexOf("             KEY IS (", context, memberRecordName);		
		do {
			i += 1;
			int j = context.getLines().get(i).indexOf(" ", 17);
			String elementName = context.getLines().get(i).substring(17, j); 
			list.add(elementName);			
		} while (!context.getLines().get(i).trim().endsWith(" )"));
		
		return list;
	}

	@Override
	public SortSequence getSortSequence(SchemaSyntaxWrapper context,
										String memberRecordName, 
										String keyElementName) {
		
		String scanItem = "                 " + keyElementName + " ";
		int i = indexOf(scanItem, context, memberRecordName);
		int j = context.getLines().get(i).indexOf(" ", 17);
		String p = context.getLines().get(i).substring(j + 1).trim();
		if (p.startsWith("ASCENDING")) {
			return SortSequence.ASCENDING;
		} else if (p.startsWith("DESCENDING")) {
			return SortSequence.DESCENDING;
		} else {
			return null;
		}
	}

	@Override
	public String getSymbolicIndexName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         MODE IS INDEX USING ")) {				
				return line.substring(29).trim();
			}
		}
		return null;
	}

	@Override
	public String getSystemOwnerAreaName(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("             WITHIN AREA ")) {				
				return line.substring(25).trim();
			}
		}
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public Short getSystemOwnerOffsetPercent(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return null;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(SchemaSyntaxWrapper context,
											   String memberRecordName) {
		// TODO figure this out
		return false;
	}

	private int indexOf(SchemaSyntaxWrapper context, String memberRecordName) {
	
		String scanItem = "         MEMBER IS " + memberRecordName;
		int i = 0;
		while (i < context.getLines().size() &&
			   !context.getLines().get(i).startsWith(scanItem)) {
			
			i += 1;
		}
		
		if (i < context.getLines().size()) {
			return i;
		}
		
		throw new RuntimeException("logic error: no line found starting with '" +
					   			   "         MEMBER IS ' for member record " + 
					   			   memberRecordName + " (set=" +
					   			   getName(context) + ")");

}	
	
	private int indexOf(String scanItem, SchemaSyntaxWrapper context, 
						String memberRecordName) {
				
		int i = indexOf(context, memberRecordName) + 1;
		
		while (i < context.getLines().size() &&
			   !context.getLines().get(i).startsWith("         MEMBER IS ")) {
			
			String line = context.getLines().get(i);
			if (line.startsWith(scanItem)) {
				return i;
			}
			i += 1;
		}
		
		return -1;
		
	}	
	
	@Override
	public boolean isKeyCompressed(SchemaSyntaxWrapper context) {
		// TODO figure this out
		return false;
	}

	@Override
	public boolean isSortedByDbkey(SchemaSyntaxWrapper context) {
		// for indexed sets only; indexed sets always have exactly 1 member
		for (String line : context.getLines()) {
			if (line.trim().equals("DBKEY ASCENDING") ||
				line.trim().equals("DBKEY DESCENDING")) {				
				
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSystemOwned(SchemaSyntaxWrapper context) {
		for (String line : context.getLines()) {
			if (line.startsWith("         OWNER IS SYSTEM")) {				
				return true;
			}
		}
		return false;
	}	
	
}