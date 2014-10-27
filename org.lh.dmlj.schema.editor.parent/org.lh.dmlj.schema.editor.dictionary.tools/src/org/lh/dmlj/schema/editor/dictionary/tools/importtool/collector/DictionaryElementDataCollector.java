/**
 * Copyright (C) 2014  Luc Hermans
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namedes_186;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn_083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn_079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdes_044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdr_042;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class DictionaryElementDataCollector implements IElementDataCollector<Namesyn_083> {

	private Map<Long, List<Namedes_186>> namedes_186Map;
	private List<Rcdsyn_079> rcdsyn_079s;
	private SchemaImportSession session;
	
	public DictionaryElementDataCollector(SchemaImportSession session) {
		super();
		this.session = session;
	}

	private void buildNamedes_186MapIfNeeded() {
		if (namedes_186Map != null) {
			return;
		}
		namedes_186Map = new HashMap<>();
		Query elementSynonymCommentListQuery = 
			new Query.Builder().forElementSynonymCommentList(session, rcdsyn_079s).build();
		session.runQuery(elementSynonymCommentListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {	
				long dbkeyOfNamesyn_083 = JdbcTools.getDbkey(row, Namesyn_083.ROWID);
				List<Namedes_186> namedes_186s;
				if (namedes_186Map.containsKey(Long.valueOf(dbkeyOfNamesyn_083))) {
					namedes_186s = namedes_186Map.get(Long.valueOf(dbkeyOfNamesyn_083));
				} else {
					namedes_186s = new ArrayList<>();
					namedes_186Map.put(Long.valueOf(dbkeyOfNamesyn_083), namedes_186s);
				}
				Namedes_186 namedes_186 = new Namedes_186();
				namedes_186.setCmtId_186(row.getInt(Namedes_186.CMT_ID_186));
				namedes_186.setCmtInfo_186_1(row.getString(Namedes_186.CMT_INFO_186_1));
				namedes_186s.add(namedes_186);
			}
		});
	}
	
	@Override
	public String getBaseName(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		if (sdr_042.getDrNam_042().startsWith("FIL ")) {
			return "FILLER";
		}		
		Rcdsyn_079 rcdsyn_079b = namesyn_083.getRcdsyn_079().getSr_036().getRcdsyn_079b();		
		if (rcdsyn_079b == null) {
			return namesyn_083.getSynName_083();
		} else {
			Namesyn_083 aNamesyn_083 = rcdsyn_079b.getNamesyn_083(sdr_042.getDbkey());
			return aNamesyn_083.getSynName_083();
		}		
	}

	@Override
	public String getDependsOnElementName(Namesyn_083 namesyn_083) {
		if (!namesyn_083.getDependOn_083().equals("")) {
			return namesyn_083.getDependOn_083();
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getIndexElementBaseNames(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		final List<String> list = new ArrayList<>();
		for (Sdes_044 sdes_044 : sdr_042.getSdes_044s()) {
			// CMT-ID-044 == -11: INDEXED BY (SDES-044 and NAMEDES-186 only)
			if (sdes_044.getCmtId_044() == -11) {
				// the index name appears to be in the ASF-FIELD-NAME-044 field (position 5)
				list.add(JdbcTools.removeTrailingSpaces(sdes_044.getAsfFieldName_044()));
			}							
		}
		return list;
	}

	@Override
	public Collection<String> getIndexElementNames(Namesyn_083 namesyn_083) {
		buildNamedes_186MapIfNeeded();
		if (!namedes_186Map.containsKey(Long.valueOf(namesyn_083.getDbkey()))) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<>();
		for (Namedes_186 namedes_186 : namedes_186Map.get(Long.valueOf(namesyn_083.getDbkey()))) {
			list.add(namedes_186.getIxName_186());
		}
		return list;
	}

	@Override
	public boolean getIsNullable(Namesyn_083 namesyn_083) {
		return false;
	}

	@Override
	public short getLevel(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		return sdr_042.getDrLvl_042();
	}

	@Override
	public String getName(Namesyn_083 namesyn_083) {
		return namesyn_083.getSynName_083();
	}

	@Override
	public short getOccurrenceCount(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		if (sdr_042.getOcc_042() > 1) {
			return sdr_042.getOcc_042();
		} else {
			return 1;
		}
	}

	@Override
	public String getPicture(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		if (!sdr_042.getPic_042().equals("")) {
			return sdr_042.getPic_042();
		} else {
			return null;
		}
	}

	@Override
	public String getRedefinedElementName(Namesyn_083 namesyn_083) {
		if (!namesyn_083.getRdfNam_083().equals("")) {
			return namesyn_083.getRdfNam_083();
		} else {			
			return null;
		}
	}

	@Override
	public Usage getUsage(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		return Usage.get(sdr_042.getUse_042());
	}

	@Override
	public String getValue(Namesyn_083 namesyn_083) {
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		for (Sdes_044 sdes_044 : sdr_042.getSdes_044s()) {
			// CMT-ID-044 == -3: VALUES (ELEMCMT-082 and SDES-044 only)
			if (sdes_044.getCmtId_044() == -3) {
				StringBuilder p = new StringBuilder();
				p.append(sdes_044.getVal1_044());
				String val2_044 = sdes_044.getVal2_044();
				if (!val2_044.equals("")) {
					p.append(" THRU ");					
					p.append(val2_044);
				}
				return p.toString();
			}
		}
		return null;
	}

	public void setRcdsyn_079s(List<Rcdsyn_079> rcdsyn_079s) {
		this.rcdsyn_079s = rcdsyn_079s;
	}

}
