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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Scr054;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Smr052;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sor046;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcd113;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class DictionarySetDataCollector implements ISetDataCollector<Sor046> {
	// MR-CNTRL-052 values:
	private static final SetMembershipOption[] MEMBERSHIP_OPTION = {
			SetMembershipOption.MANDATORY_AUTOMATIC,	// 0
			SetMembershipOption.MANDATORY_MANUAL,	// 1
			SetMembershipOption.OPTIONAL_AUTOMATIC,	// 2
			SetMembershipOption.OPTIONAL_MANUAL 		// 3
		};
	
	// SET-ORD-046 values:
	private static final SetOrder[][] ORDER = {
			{ SetOrder.LAST, SetOrder.PRIOR}, 
			{ SetOrder.SORTED, SetOrder.SORTED}, 
			{ SetOrder.FIRST, SetOrder.NEXT}
		};
	
	// SORT-054 values:
	private static final SortSequence[] SORT_SEQUENCE = {
			SortSequence.ASCENDING,	// 0 
			SortSequence.DESCENDING	// 1
		};

	private final SchemaImportSession session;
	private Map<Rowid, Smr052> sortKeyElementsMap;
	
	private static short getAdjustedDbkeyPosition(Srcd113 srcd113, short dbkeyPosition) {
		if (dbkeyPosition == -1) {
			return dbkeyPosition;
		} else if (srcd113.getMode113() == 1) {
			// record with location mode CALC
			return (short) (dbkeyPosition - 1);
		} else {
			return (short) (dbkeyPosition + 1);
		}
	}
	
	public DictionarySetDataCollector(SchemaImportSession session) {
		this.session = session;
	}
	
	private void addSortKeyElementsIfNeeded(Smr052 smr052) {
		buildSortKeyElementsMapIfNeeded();
		if (!smr052.getScr054s().isEmpty()) {
			return;
		}
		var smr052b = sortKeyElementsMap.get(smr052.getRowid());
		if (smr052b == null || smr052b.getScr054s().isEmpty()) {
			throw new IllegalStateException("no sort elements for SMR-052 with rowid " + smr052.getRowid());
		}
		smr052.getScr054s().addAll(smr052b.getScr054s());
	}

	private void buildSortKeyElementsMapIfNeeded() {
		if (sortKeyElementsMap != null) {
			return;
		}
		sortKeyElementsMap = new HashMap<>();
		var sortKeyElementListQuery = new Query.Builder().forSortKeyElementList(session).build();
		session.runQuery(sortKeyElementListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var rowidOfSmr052 = JdbcTools.getRowid(row, Smr052.ROWID);
				var smr052 = sortKeyElementsMap.computeIfAbsent(rowidOfSmr052, unused -> createSmr052(row));
				var scr054 = new Scr054();
				scr054.setIndex054(row.getShort(Scr054.INDEX_054));
				scr054.setScrNam054(row.getString(Scr054.SCR_NAM_054));
				scr054.setScrPos054(row.getShort(Scr054.SCR_POS_054));
				scr054.setSort054(row.getShort(Scr054.SORT_054));
				smr052.getScr054s().add(scr054);		
			}
		});
	}
	
	private Smr052 createSmr052(ResultSet row) {
		try {
			var smr052 = new Smr052();
			// we only care about collecting the sort key elements, so ignore most of the SMR-052's fields
			smr052.setRowid(JdbcTools.getRowid(row, Smr052.ROWID));
			return smr052;
		} catch (SQLException e) {
			throw new IllegalStateException(e);
		}
	}

	private Smr052 findMember(Sor046 sor046, String memberRecordName) {
		for (var smr052 : sor046.getSmr052s()) {			
			var srcd113 = smr052.getSrcd113();
			var sam056 = srcd113.getSam056();
			if (sam056.getSrNam056().equals(memberRecordName)) {
				return smr052;
			}
		}
		throw new IllegalStateException(memberRecordName + " is not a member of " + getName(sor046));
	}

	@Override
	public Short getDisplacementPageCount(Sor046 sor046) {
		return sor046.getIndexDisp046();
	}

	@Override
	public DuplicatesOption getDuplicatesOption(Sor046 sor046, String memberRecordName) {
		return switch (findMember(sor046, memberRecordName).getDup052()) {
			case 0 -> DuplicatesOption.NOT_ALLOWED;
			case 1 -> DuplicatesOption.FIRST;
			case 2 -> DuplicatesOption.LAST;
			case 3 -> DuplicatesOption.UNORDERED;
			case 4 -> DuplicatesOption.BY_DBKEY;
			default -> null;
		};
	}

	@Override
	public Short getKeyCount(Sor046 sor046) {
		return sor046.getIndexMembers046();
	}

	@Override
	public Short getMemberIndexDbkeyPosition(Sor046 sor046) {
		// this method will only be called for indexed sets, which only have 1 member record		
		var smr052 = sor046.getSmr052s().get(0);
		var srcd113 = smr052.getSrcd113();
		var i = getAdjustedDbkeyPosition(srcd113, smr052.getNxtDbk052());
		if (i > -1) {
			return i;
		}
		return null;
	}

	@Override
	public Short getMemberNextDbkeyPosition(Sor046 sor046, String memberRecordName) {
		var smr052 = findMember(sor046, memberRecordName);
		var srcd113 = smr052.getSrcd113();
		var i = getAdjustedDbkeyPosition(srcd113, smr052.getNxtDbk052());
		if (i > -1) {
			return i;
		}
		return null;
	}

	@Override
	public Short getMemberOwnerDbkeyPosition(Sor046 sor046, String memberRecordName) {
		var smr052 = findMember(sor046, memberRecordName);
		var srcd113 = smr052.getSrcd113();
		var i = getAdjustedDbkeyPosition(srcd113, smr052.getOwnDbk052());
		if (i > -1) {
			return i;
		}
		return null;
	}

	@Override
	public Short getMemberPriorDbkeyPosition(Sor046 sor046, String memberRecordName) {
		var smr052 = findMember(sor046, memberRecordName);
		var srcd113 = smr052.getSrcd113();
		var i = getAdjustedDbkeyPosition(srcd113, smr052.getPriDbk052());
		if (i > -1) {
			return i;
		}
		return null;
	}

	@Override
	public Collection<String> getMemberRecordNames(Sor046 sor046) {
		var list = new ArrayList<String>();
		for (var smr052 : sor046.getSmr052s()) {
			var srcd113 = smr052.getSrcd113();
			var sam056 = srcd113.getSam056();
			list.add(sam056.getSrNam056());
		}
		return list;
	}

	@Override
	public String getName(Sor046 sor046) {
		return sor046.getSetNam046();
	}

	@Override
	public short getOwnerNextDbkeyPosition(Sor046 sor046) {
		var srcd113 = sor046.getSrcd113();
		return getAdjustedDbkeyPosition(srcd113, sor046.getNxtDbk046());
	}

	@Override
	public Short getOwnerPriorDbkeyPosition(Sor046 sor046) {
		var srcd113 = sor046.getSrcd113();
		var i = getAdjustedDbkeyPosition(srcd113, sor046.getPriDbk046());
		if (i > -1) {
			return i;
		} else {
			return null;
		}
	}

	@Override
	public String getOwnerRecordName(Sor046 sor046) {
		var srcd113 = sor046.getSrcd113();
		var sam056 = srcd113.getSam056();
		return sam056.getSrNam056();
	}

	@Override
	public SetMembershipOption getSetMembershipOption(Sor046 sor046, String memberRecordName) {
		var smr052 = findMember(sor046, memberRecordName);
		return MEMBERSHIP_OPTION[smr052.getMrCntrl052()];
	}

	@Override
	public SetMode getSetMode(Sor046 sor046) {
		return switch (sor046.getSetMode046()) {
			case 13, 15 -> SetMode.CHAINED;
			case 21 -> SetMode.INDEXED;
			case 32 -> SetMode.VSAM_INDEX;
			default -> null;
		};
	}

	@Override
	public SetOrder getSetOrder(Sor046 sor046) {
		return ORDER[sor046.getSetOrd046()][sor046.getOrd046()];
	}

	@Override
	public SortSequence getSortSequence(Sor046 sor046, String memberRecordName, String keyElementName) {
		var smr052 = findMember(sor046, memberRecordName);
		addSortKeyElementsIfNeeded(smr052);
		for (var scr054 : smr052.getScr054s()) {			
			if (scr054.getScrNam054().equals(keyElementName)) {
				return SORT_SEQUENCE[scr054.getSort054()];
			}
		}
		return null;
	}

	@Override
	public Collection<String> getSortKeyElements(Sor046 sor046, String memberRecordName) {
		var list = new ArrayList<String>();
		var smr052 = findMember(sor046, memberRecordName);
		addSortKeyElementsIfNeeded(smr052);
		for (var scr054 : smr052.getScr054s()) {
			list.add(scr054.getScrNam054());
		}
		return list;
	}

	@Override
	public boolean getSortKeyIsNaturalSequence(Sor046 sor046, String memberRecordName) {
		var smr052 = findMember(sor046, memberRecordName);
		return smr052.getSort052() == 1;
	}

	@Override
	public String getSymbolicIndexName(Sor046 sor046) {
		if (!sor046.getSymbolIndex046().isEmpty()) {
			return sor046.getSymbolIndex046();
		} else {
			return null;
		}
	}

	@Override
	public String getSystemOwnerAreaName(Sor046 sor046) {
		if (!sor046.getSaNam046().isEmpty()) {
			return sor046.getSaNam046();
		} else {
			return null;
		}
	}

	@Override
	public Integer getSystemOwnerOffsetOffsetPageCount(Sor046 sor046) {
		if (sor046.getPageOffset046() > -1) {
			return sor046.getPageOffset046();
		} else {
			return null;
		}
	}

	@Override
	public Short getSystemOwnerOffsetOffsetPercent(Sor046 sor046) {
		if (sor046.getPageOffsetPercent046() > -1) {
			return sor046.getPageOffsetPercent046();
		} else {
			return null;
		}
	}

	@Override
	public Integer getSystemOwnerOffsetPageCount(Sor046 sor046) {
		if (sor046.getPageCount046() > 0) {
			return sor046.getPageCount046();
		} else {
			return null;
		}
	}

	@Override
	public Short getSystemOwnerOffsetPercent(Sor046 sor046) {
		if (sor046.getPageCountPercent046() > -1) {
			return sor046.getPageCountPercent046();
		} else {
			return null;
		}
	}

	@Override
	public String getSystemOwnerSymbolicSubareaName(Sor046 sor046) {
		if (!sor046.getSubarea046().isEmpty()) {
			return sor046.getSubarea046();
		} else {
			return null;
		}
	}

	@Override
	public boolean isKeyCompressed(Sor046 sor046) {
		var smr052 = sor046.getSmr052s().get(0);
		addSortKeyElementsIfNeeded(smr052);
		var scr054 = smr052.getScr054s().get(0);
		return scr054.getIndex054() == 1;		
	}

	@Override
	public boolean isSortedByDbkey(Sor046 sor046) {
		// only called for sorted indexed sets, which can have only 1 member record
		var smr052 = sor046.getSmr052s().get(0);
		addSortKeyElementsIfNeeded(smr052);
		var scr054 = smr052.getScr054s().get(0);
		return scr054.getScrPos054() == -4;
	}

	@Override
	public boolean isSystemOwned(Sor046 sor046) {
		return sor046.getSorId046() == 7;
	}

}
