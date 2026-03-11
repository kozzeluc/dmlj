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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.DictionarySession;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namedes186;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn079;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class DictionaryElementDataCollector implements IElementDataCollector<Namesyn083> {
	private DictionarySession session;
	private Map<Rowid, List<Namedes186>> namedes186Map;
	private List<Rcdsyn079> rcdsyn079s;
	
	public DictionaryElementDataCollector() {
	}
	
	public DictionaryElementDataCollector(SchemaImportSession session) {
		this.session = session;
	}

	private void buildNamedes186MapIfNeeded() {
		if (namedes186Map != null) {
			return;
		}
		namedes186Map = new HashMap<>();
		var elementSynonymCommentListQuery = new Query.Builder().forElementSynonymCommentList(session, rcdsyn079s).build();
		session.runQuery(elementSynonymCommentListQuery, new IRowProcessor() {			
			@Override
			public void processRow(ResultSet row) throws SQLException {	
				var rowidOfNamesyn083 = JdbcTools.getRowid(row, Namesyn083.ROWID);
				List<Namedes186> namedes186s;
				if (namedes186Map.containsKey(rowidOfNamesyn083)) {
					namedes186s = namedes186Map.get(rowidOfNamesyn083);
				} else {
					namedes186s = new ArrayList<>();
					namedes186Map.put(rowidOfNamesyn083, namedes186s);
				}
				var namedes186 = new Namedes186();
				namedes186.setCmtId186(row.getInt(Namedes186.CMT_ID_186));
				namedes186.setFirstCmtInfo186(row.getString(Namedes186.CMT_INFO_186_1));
				namedes186s.add(namedes186);
			}
		});
	}
	
	@Override
	public String getBaseName(Namesyn083 namesyn083) {
		var sdr042 = namesyn083.getSdr042();
		if (sdr042.getDrNam042().startsWith("FIL ")) {
			return "FILLER";
		}		
		var rcdsyn079b = namesyn083.getRcdsyn079().getSr036().getRcdsyn079b();		
		if (rcdsyn079b == null) {
			return namesyn083.getSynName083();
		} else {
			var aNamesyn083 = rcdsyn079b.getNamesyn083(sdr042.getRowid());
			return aNamesyn083.getSynName083();
		}		
	}

	@Override
	public String getDependsOnElementName(Namesyn083 namesyn083) {
		if (!namesyn083.getDependOn083().isEmpty()) {
			return namesyn083.getDependOn083();
		} else {
			return null;
		}
	}

	@Override
	public Collection<String> getIndexElementBaseNames(Namesyn083 namesyn083) {
		var sdr042 = namesyn083.getSdr042();
		var list = new ArrayList<String>();
		for (var sdes044 : sdr042.getSdes044s()) {
			// CMT-ID-044 == -11: INDEXED BY (SDES-044 and NAMEDES-186 only)
			if (sdes044.getCmtId044() == -11) {
				// the index name appears to be in the ASF-FIELD-NAME-044 field (position 5)
				list.add(JdbcTools.removeTrailingSpaces(sdes044.getAsfFieldName044()));
			}							
		}
		return list;
	}

	@Override
	public Collection<String> getIndexElementNames(Namesyn083 namesyn083) {
		buildNamedes186MapIfNeeded();
		if (!namedes186Map.containsKey(namesyn083.getRowid())) {
			return Collections.emptyList();
		}
		var list = new ArrayList<String>();
		for (var namedes186 : namedes186Map.get(namesyn083.getRowid())) {
			list.add(namedes186.getIxName186());
		}
		return list;
	}

	@Override
	public boolean getIsNullable(Namesyn083 namesyn083) {
		return false;
	}

	@Override
	public short getLevel(Namesyn083 namesyn083) {
		return namesyn083.getSdr042().getDrLvl042();
	}

	@Override
	public String getName(Namesyn083 namesyn083) {
		return namesyn083.getSynName083();
	}

	@Override
	public short getOccurrenceCount(Namesyn083 namesyn083) {
		var sdr042 = namesyn083.getSdr042();
		if (sdr042.getOcc042() > 1) {
			return sdr042.getOcc042();
		} else {
			return 1;
		}
	}

	@Override
	public String getPicture(Namesyn083 namesyn083) {
		var sdr042 = namesyn083.getSdr042();
		if (!sdr042.getPic042().isEmpty()) {
			return sdr042.getPic042();
		} else {
			return null;
		}
	}

	@Override
	public String getRedefinedElementName(Namesyn083 namesyn083) {
		if (!namesyn083.getRdfNam083().isEmpty()) {
			return namesyn083.getRdfNam083();
		} else {			
			return null;
		}
	}

	@Override
	public Usage getUsage(Namesyn083 namesyn083) {
		return Usage.get(namesyn083.getSdr042().getUse042());
	}

	@Override
	public List<String> getValues(Namesyn083 namesyn083) {
		var values = new ArrayList<String>();
		var sdr042 = namesyn083.getSdr042();
		for (var sdes044 : sdr042.getSdes044s()) {
			// CMT-ID-044 == -3: VALUES (ELEMCMT-082 and SDES-044 only)
			if (sdes044.getCmtId044() == -3) {
				var p = new StringBuilder();
				p.append(sdes044.getVal1044());
				var val2044 = sdes044.getVal2044();
				if (!val2044.isEmpty()) {
					p.append(" THRU ");					
					p.append(val2044);
				}
				values.add(p.toString());
			}
		}
		return values;
	}

	public void setRcdsyn079s(List<Rcdsyn079> rcdsyn079s) {
		this.rcdsyn079s = rcdsyn079s;
	}
	
	public void setSession(DictionarySession session) {
		this.session = session;
	}

}
