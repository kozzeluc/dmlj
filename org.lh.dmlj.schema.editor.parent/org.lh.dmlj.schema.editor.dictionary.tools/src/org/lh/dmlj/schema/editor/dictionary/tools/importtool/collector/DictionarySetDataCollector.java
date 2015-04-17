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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sam_056;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Scr_054;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Smr_052;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sor_046;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcd_113;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class DictionarySetDataCollector implements ISetDataCollector<Sor_046> {
	
	// MR-CNTRL-052 values:
	private static final SetMembershipOption[] MEMBERSHIP_OPTION = 
		{SetMembershipOption.MANDATORY_AUTOMATIC,	// 0
		 SetMembershipOption.MANDATORY_MANUAL,		// 1
	     SetMembershipOption.OPTIONAL_AUTOMATIC,	// 2
	     SetMembershipOption.OPTIONAL_MANUAL};		// 3	
	
	// SET-ORD-046 values:
	private static final SetOrder[][] ORDER = 
		new SetOrder[][] {{SetOrder.LAST, SetOrder.PRIOR}, 
						  {SetOrder.SORTED, SetOrder.SORTED}, 
						  {SetOrder.FIRST, SetOrder.NEXT}};
	
	// SORT-054 values:
	private static final SortSequence[] SORT_SEQUENCE = 
		{SortSequence.ASCENDING,		// 0 
		 SortSequence.DESCENDING};		// 1	

	private SchemaImportSession session;
	private Map<Long, Smr_052> sortKeyElementsMap;
	
	private static short getAdjustedDbkeyPosition(Srcd_113 srcd_113, short dbkeyPosition) {
		if (dbkeyPosition == -1) {
			return dbkeyPosition;
		} else if (srcd_113.getMode_113() == 1) {
			// record with location mode CALC
			return (short)(dbkeyPosition - 1);
		} else {
			return (short)(dbkeyPosition + 1);
		}
	}
	
	public DictionarySetDataCollector(SchemaImportSession session) {
		super();
		this.session = session;
	}
	
	private void addSortKeyElementsIfNeeded(Smr_052 smr_052) {
		buildSortKeyElementsMapIfNeeded();
		if (!smr_052.getScr_054s().isEmpty()) {
			return;
		}
		Smr_052 smr_052b = sortKeyElementsMap.get(Long.valueOf(smr_052.getDbkey()));
		if (smr_052b == null || smr_052b.getScr_054s().isEmpty()) {
			throw new RuntimeException("no sort elements for SMR-052 with dbkey X'" +
									   JdbcTools.toHexString(smr_052.getDbkey()) + "'");
		}
		smr_052.getScr_054s().addAll(smr_052b.getScr_054s());
	}

	private void buildSortKeyElementsMapIfNeeded() {
		if (sortKeyElementsMap != null) {
			return;
		}
		sortKeyElementsMap = new HashMap<>();
		IQuery sortKeyElementListQuery = new Query.Builder().forSortKeyElementList(session).build();
		session.runQuery(sortKeyElementListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				Long dbkeyOfSmr_052 = JdbcTools.getDbkey(row, Smr_052.ROWID);
				if (!sortKeyElementsMap.containsKey(Long.valueOf(dbkeyOfSmr_052))) {
					Smr_052 smr_052 = new Smr_052();
					// we only care about collecting the sort key elements, so ignore most of the
					// SMR-052's fields
					smr_052.setDbkey(JdbcTools.getDbkey(row, Smr_052.ROWID));
					sortKeyElementsMap.put(Long.valueOf(dbkeyOfSmr_052), smr_052);
				}
				Smr_052 smr_052 = sortKeyElementsMap.get(Long.valueOf(dbkeyOfSmr_052));
				Scr_054 scr_054 = new Scr_054();
				scr_054.setIndex_054(row.getShort(Scr_054.INDEX_054));
				scr_054.setScrNam_054(row.getString(Scr_054.SCR_NAM_054));
				scr_054.setScrPos_054(row.getShort(Scr_054.SCR_POS_054));
				scr_054.setSort_054(row.getShort(Scr_054.SORT_054));
				smr_052.getScr_054s().add(scr_054);		
			}
		});
	}

	private Smr_052 findMember(Sor_046 sor_046, String memberRecordName) {
		for (Smr_052 smr_052 : sor_046.getSmr_052s()) {			
			Srcd_113 srcd_113 = smr_052.getSrcd_113();
			Sam_056 sam_056 = srcd_113.getSam_056();			
			if (sam_056.getSrNam_056().equals(memberRecordName)) {
				return smr_052;
			}
		}
		throw new RuntimeException(memberRecordName + " is not a member of " + getName(sor_046));
	}

	@Override
	public Short getDisplacementPageCount(Sor_046 sor_046) {
		return Short.valueOf(sor_046.getIndexDisp_046());
	}

	@Override
	public DuplicatesOption getDuplicatesOption(Sor_046 sor_046, String memberRecordName) {
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		if (smr_052.getDup_052() == 0) {
			return DuplicatesOption.NOT_ALLOWED;
		} else if (smr_052.getDup_052() == 1) {
			return DuplicatesOption.FIRST;
		} else if (smr_052.getDup_052() == 2) {
			return DuplicatesOption.LAST;
		} else if (smr_052.getDup_052() == 3) {
			return DuplicatesOption.UNORDERED;
		} else if (smr_052.getDup_052() == 4) {
			return DuplicatesOption.BY_DBKEY;
		} else {
			return null;
		}
	}

	@Override
	public Short getKeyCount(Sor_046 sor_046) {
		return Short.valueOf(sor_046.getIndexMembers_046());
	}

	@Override
	public Short getMemberIndexDbkeyPosition(Sor_046 sor_046) {
		// this method will only be called for indexed sets, which only have 1 member record		
		Smr_052 smr_052 = sor_046.getSmr_052s().get(0);
		Srcd_113 srcd_113 = smr_052.getSrcd_113();
		short i = getAdjustedDbkeyPosition(srcd_113, smr_052.getNxtDbk_052());
		if (i > -1) {
			return Short.valueOf(i);
		}
		return null;
	}

	@Override
	public Short getMemberNextDbkeyPosition(Sor_046 sor_046, String memberRecordName) {
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		Srcd_113 srcd_113 = smr_052.getSrcd_113();
		short i = getAdjustedDbkeyPosition(srcd_113, smr_052.getNxtDbk_052());
		if (i > -1) {
			return Short.valueOf(i);
		}
		return null;
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(Sor_046 sor_046, String memberRecordName) {
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		Srcd_113 srcd_113 = smr_052.getSrcd_113();
		short i = getAdjustedDbkeyPosition(srcd_113, smr_052.getOwnDbk_052());
		if (i > -1) {
			return Short.valueOf(i);
		}
		return null;
	}

	@Override
	public Short getMemberPriorDbkeyPosition(Sor_046 sor_046, String memberRecordName) {
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		Srcd_113 srcd_113 = smr_052.getSrcd_113();
		short i = getAdjustedDbkeyPosition(srcd_113, smr_052.getPriDbk_052());
		if (i > -1) {
			return Short.valueOf(i);
		}
		return null;
	}

	@Override
	public Collection<String> getMemberRecordNames(Sor_046 sor_046) {
		List<String> list = new ArrayList<>();
		for (Smr_052 smr_052 : sor_046.getSmr_052s()) {
			Srcd_113 srcd_113 = smr_052.getSrcd_113();
			Sam_056 sam_056 = srcd_113.getSam_056();
			list.add(sam_056.getSrNam_056());
		}
		return list;
	}

	@Override
	public String getName(Sor_046 sor_046) {
		return sor_046.getSetNam_046();
	}

	@Override
	public short getOwnerNextDbkeyPosition(Sor_046 sor_046) {
		Srcd_113 srcd_113 = sor_046.getSrcd_113();
		return getAdjustedDbkeyPosition(srcd_113, sor_046.getNxtDbk_046());
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(Sor_046 sor_046) {
		Srcd_113 srcd_113 = sor_046.getSrcd_113();
		short i = getAdjustedDbkeyPosition(srcd_113, sor_046.getPriDbk_046());
		if (i > -1) {
			return Short.valueOf(i);
		} else {
			return null;
		}
	}

	@Override
	public String getOwnerRecordName(Sor_046 sor_046) {
		Srcd_113 srcd_113 = sor_046.getSrcd_113();
		Sam_056 sam_056 = srcd_113.getSam_056();
		return sam_056.getSrNam_056();
	}

	@Override
	public SetMembershipOption getSetMembershipOption(Sor_046 sor_046, String memberRecordName) {
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		return MEMBERSHIP_OPTION[smr_052.getMrCntrl_052()];
	}

	@Override
	public SetMode getSetMode(Sor_046 sor_046) {
		short mode = sor_046.getSetMode_046();
		if (mode == 13 || mode == 15) {
			return SetMode.CHAINED;
		} else if (mode == 21) {
			return SetMode.INDEXED;
		} else {
			return null;
		}
	}

	@Override
	public SetOrder getSetOrder(Sor_046 sor_046) {
		return ORDER[sor_046.getSetOrd_046()][sor_046.getOrd_046()];
	}

	@Override
	public SortSequence getSortSequence(Sor_046 sor_046, String memberRecordName, 
										String keyElementName) {
		
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		addSortKeyElementsIfNeeded(smr_052);
		for (Scr_054 scr_054 : smr_052.getScr_054s()) {			
			if (scr_054.getScrNam_054().equals(keyElementName)) {
				return SORT_SEQUENCE[scr_054.getSort_054()];
			}
		}
		return null;
	}

	@Override
	public Collection<String> getSortKeyElements(Sor_046 sor_046, String memberRecordName) {
		List<String> list = new ArrayList<>();
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		addSortKeyElementsIfNeeded(smr_052);
		for (Scr_054 scr_054 : smr_052.getScr_054s()) {
			list.add(scr_054.getScrNam_054());
		}
		return list;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(Sor_046 sor_046, String memberRecordName) {
		Smr_052 smr_052 = findMember(sor_046, memberRecordName);
		return smr_052.getSort_052() == 1;
	}

	@Override
	public String getSymbolicIndexName(Sor_046 sor_046) {
		if (!sor_046.getSymbolIndex_046().equals("")) {
			return sor_046.getSymbolIndex_046();
		} else {
			return null;
		}
	}

	@Override
	public String getSystemOwnerAreaName(Sor_046 sor_046) {
		if (!sor_046.getSaNam_046().equals("")) {
			return sor_046.getSaNam_046();
		} else {
			return null;
		}
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(Sor_046 sor_046) {
		if (sor_046.getPageOffset_046() > -1) {
			return Integer.valueOf(sor_046.getPageOffset_046());
		} else {
			return null;
		}
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(Sor_046 sor_046) {
		if (sor_046.getPageOffsetPercent_046() > -1) {
			return Short.valueOf(sor_046.getPageOffsetPercent_046());
		} else {
			return null;
		}
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(Sor_046 sor_046) {
		if (sor_046.getPageCount_046() > 0) {
			return Integer.valueOf(sor_046.getPageCount_046());
		} else {
			return null;
		}
	}

	@Override
	public Short getSystemOwnerOffsetPercent(Sor_046 sor_046) {
		if (sor_046.getPageCountPercent_046() > -1) {
			return Short.valueOf(sor_046.getPageCountPercent_046());
		} else {
			return null;
		}
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(Sor_046 sor_046) {
		if (!sor_046.getSubarea_046().equals("")) {
			return sor_046.getSubarea_046();
		} else {
			return null;
		}
	}

	@Override
	public boolean isKeyCompressed(Sor_046 sor_046) {
		Smr_052 smr_052 = sor_046.getSmr_052s().get(0);
		addSortKeyElementsIfNeeded(smr_052);
		Scr_054 scr_054 = smr_052.getScr_054s().get(0);
		return scr_054.getIndex_054() == 1;		
	}

	@Override
	public boolean isSortedByDbkey(Sor_046 sor_046) {
		// this method will only be called for (sorted) indexed sets, which can have only 1 member 
		// record
		Smr_052 smr_052 = sor_046.getSmr_052s().get(0);
		addSortKeyElementsIfNeeded(smr_052);
		Scr_054 scr_054 = smr_052.getScr_054s().get(0);
		return scr_054.getScrPos_054() == -4;
	}

	@Override
	public boolean isSystemOwned(Sor_046 sor_046) {
		return sor_046.getSorId_046() == 7;
	}

}
