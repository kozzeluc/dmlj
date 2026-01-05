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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.DictionaryElementDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.context.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.RecordElementsImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdes044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdr042;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsImportTool;

public class RecordElementsImportTool implements IRecordElementsImportTool {
	private DictionaryElementDataCollector dictionaryElementDataCollector;
	private Dictionary dictionary;
	private Rcdsyn079 rcdsyn079;
	private List<Namesyn083> toplevelElements = new ArrayList<>();

	@Override
	public void dispose() {
		// nothing to dispose
	}

	@Override
	public RootElementContexts getRootElementContexts() {		
		if (!toplevelElements.isEmpty()) {
			return new RootElementContexts(toplevelElements);
		}
		
		rcdsyn079.getSr036().setRcdsyn079b(null);		
		rcdsyn079.getNamesyn083s().clear();
		
		var session = new RecordElementsImportSession(dictionary, rcdsyn079.getRsynName079(), rcdsyn079.getRsynVer079());
		session.open();
		dictionaryElementDataCollector.setSession(session);
		
		hookBaseRecordSynonymToTheSr036(session);
		var rcdsyn079b = rcdsyn079.getSr036().getRcdsyn079b();
		
		var listOfRcdsyn079sInvolved = new ArrayList<Rcdsyn079>(); 
		listOfRcdsyn079sInvolved.add(rcdsyn079);
		if (rcdsyn079b != null) {
			listOfRcdsyn079sInvolved.add(rcdsyn079b);
		}
		Collections.sort(listOfRcdsyn079sInvolved, (r1, r2) -> {
			if (r1.getRsynName079().equals(r2.getRsynName079())) {
				return r1.getRsynVer079() - r2.getRsynVer079();
			} else {
				return r1.getRsynName079().compareTo(r2.getRsynName079());
			}
		});
		
		var sdr042rowids = getAllElementsForAllRegularRecords(session, listOfRcdsyn079sInvolved, rcdsyn079b);
		
		// RCDSYN-079b postprocessing: set each NAMESYN-083's SDR-042 reference
		if (rcdsyn079b != null) {
			for (var namesyn083 : rcdsyn079b.getNamesyn083s()) {
				var rowidOfSdr042 = sdr042rowids.get(namesyn083.getRowid());
				var sdr042 = rcdsyn079.getNamesyn083(rowidOfSdr042).getSdr042();				
				namesyn083.setSdr042(sdr042);
			}
		}
		
		var elementCommentListQuery = new Query.Builder()
				.forElementCommentList(session, List.of(rcdsyn079))
				.build();
		session.runQuery(elementCommentListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {				
				var sdr042 = rcdsyn079.getNamesyn083(JdbcTools.getRowid(row, Sdr042.ROWID)).getSdr042();					
				var sdes044 = new Sdes044();
				sdes044.setCmtId044(row.getInt(Sdes044.CMT_ID_044));
				sdes044.setFirstCmtInfo044(row.getString(Sdes044.CMT_INFO_044_1));
				sdes044.setSecondCmtInfo044(row.getString(Sdes044.CMT_INFO_044_2));
				sdr042.getSdes044s().add(sdes044);												
			}				
		});
		
		dictionaryElementDataCollector.setSession(null);
		session.close();
		
		var topLevel = rcdsyn079.getNamesyn083s().get(0).getSdr042().getDrLvl042();
		for (var namesyn083 : rcdsyn079.getNamesyn083s()) {
			var sdr042 = namesyn083.getSdr042();
			if (sdr042.getDrLvl042() == topLevel) {
				toplevelElements.add(namesyn083);
			}
		}
		
		return new RootElementContexts(toplevelElements);
	}
	
	private Map<Rowid, Rowid> getAllElementsForAllRegularRecords(RecordElementsImportSession session,
			List<Rcdsyn079> listOfRcdsyn079sInvolved, Rcdsyn079 rcdsyn079b) {
		
		// get ALL elements for ALL regular records
		var sdr042rowids = new HashMap<Rowid, Rowid>(); // for postprocessing RCDSYN-079bs
		var elementListQuery = new Query.Builder().forElementList(session, listOfRcdsyn079sInvolved).build();
		session.runQuery(elementListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {				
				var rowidOfRcdsyn079 = JdbcTools.getRowid(row, Rcdsyn079.ROWID);
				if (rowidOfRcdsyn079.equals(rcdsyn079.getRowid())) {										
					var namesyn083 = new Namesyn083();	
					namesyn083.setRowid(JdbcTools.getRowid(row, Namesyn083.ROWID));
					namesyn083.setDependOn083(row.getString(Namesyn083.DEPEND_ON_083));
					namesyn083.setRdfNam083(row.getString(Namesyn083.RDF_NAM_083));
					namesyn083.setSynName083(row.getString(Namesyn083.SYN_NAME_083));
					namesyn083.setRcdsyn079(rcdsyn079);
					rcdsyn079.getNamesyn083s().add(namesyn083);					
					var sdr042 = new Sdr042();					
					sdr042.setRowid(JdbcTools.getRowid(row, Sdr042.ROWID));
					sdr042.setDrLvl042(row.getShort(Sdr042.DR_LVL_042));
					sdr042.setDrNam042(row.getString(Sdr042.DR_NAM_042));
					sdr042.setOcc042(row.getShort(Sdr042.OCC_042));
					sdr042.setPic042(row.getString(Sdr042.PIC_042));
					sdr042.setUse042(row.getShort(Sdr042.USE_042));
					namesyn083.setSdr042(sdr042);					
				} else if (rcdsyn079b != null && rowidOfRcdsyn079.equals(rcdsyn079b.getRowid())) {					
					var namesyn083 = new Namesyn083();	
					namesyn083.setRowid(JdbcTools.getRowid(row, Namesyn083.ROWID));
					namesyn083.setDependOn083(row.getString(Namesyn083.DEPEND_ON_083));
					namesyn083.setRdfNam083(row.getString(Namesyn083.RDF_NAM_083));
					namesyn083.setSynName083(row.getString(Namesyn083.SYN_NAME_083));
					namesyn083.setRcdsyn079(rcdsyn079b);
					rcdsyn079b.getNamesyn083s().add(namesyn083);					
					// we have no control over the order in which record synonyms are returned, so 
					// defer setting the NAMESYN-083's SDR-042 reference until all rows are 
					// processed
					var rowidOfSdr042 = JdbcTools.getRowid(row, Sdr042.ROWID);
					sdr042rowids.put(namesyn083.getRowid(), rowidOfSdr042);
				} else {
					throw new IllegalStateException("unexpected row; rowid of RCDSYN-079=" + rowidOfRcdsyn079);
				}			
			}				
		});
		return sdr042rowids;
	}

	private void hookBaseRecordSynonymToTheSr036(RecordElementsImportSession session) {
		// locate the base record synonym and hook it to the SR-036 when different from the record synonym
		var baseRecordSynonymListQuery = new Query.Builder()
				.forBaseRecordSynonymList(session, List.of(rcdsyn079.getSr036()))
				.build();
		session.runQuery(baseRecordSynonymListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var rowidRcdsyn079b = JdbcTools.getRowid(row, Rcdsyn079.ROWID);
				if (!rowidRcdsyn079b.equals(rcdsyn079.getRowid())) {
					var rsynName079 = row.getString(Rcdsyn079.RSYN_NAME_079).trim();
					var rsynVer079 = row.getShort(Rcdsyn079.RSYN_VER_079);
					if (!rsynName079.equals(rcdsyn079.getRsynName079()) || rsynVer079 != rcdsyn079.getRsynVer079()) {
						// only set the base RCDSYN-079 occurrence when the name or version are different from
						// the RCDSYN-079 that is in the context
						var rcdsyn079b = new Rcdsyn079();
						rcdsyn079b.setRowid(rowidRcdsyn079b);
						rcdsyn079b.setRsynName079(rsynName079);
						rcdsyn079b.setRsynVer079(rsynVer079);
						rcdsyn079b.setSr036(rcdsyn079.getSr036());
						rcdsyn079.getSr036().setRcdsyn079b(rcdsyn079b);
					}
				}				
			}					
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T elementContext) {
		var namesyn083 = (Namesyn083) elementContext;
		var list = new ArrayList<Namesyn083>();
		
		// we don't use the nested element structure in the dictionary because that is not always what is in the record
		var sdr042 = namesyn083.getSdr042();
		var ourLevel = sdr042.getDrLvl042();
		var nextHigherLevel = -1;
		var level88level = ourLevel;
		var active = false;
		for (var namesyn083b : namesyn083.getRcdsyn079().getNamesyn083s()) {				
			if (active) {
				sdr042 = namesyn083b.getSdr042();
				var level = sdr042.getDrLvl042();					
				if (level <= ourLevel) {
					// a level number smaller than or equal to ours means we're done
					break;
				} else if (level == 88) {
					// a condition name is encountered
					if (level88level == ourLevel) {
						list.add(namesyn083b);
					}
				} else {
					// a higher level number is encountered
					if (nextHigherLevel == -1) {
						nextHigherLevel = level;
					}
					level88level = level;
					if (level == nextHigherLevel) {
						list.add(namesyn083b);
					}
				}
			} else if (namesyn083b == namesyn083) {
				active = true;
			}				
		}
		return (Collection<T>) list;		
	}

	@Override
	public void init(Properties parameters, IRecordElementsDataCollectorRegistry registry) {
		dictionaryElementDataCollector = new DictionaryElementDataCollector();	
		registry.registerDataCollector(Namesyn083.class, dictionaryElementDataCollector);
	}

	@Override
	public void setContext(IDataEntryContext context) {
		var previousDictionary = dictionary;
		var previousRcdsyn079 = rcdsyn079;
		
		if (!context.containsAttribute(ContextAttributeKeys.DICTIONARY)) {
			var message = "the context's " + ContextAttributeKeys.DICTIONARY + " attribute is not set";
			throw new IllegalArgumentException(message);
		} else {
			dictionary = context.getAttribute(ContextAttributeKeys.DICTIONARY);
		}
		
		if (!context.containsAttribute(ContextAttributeKeys.RCDSYN_079)) {
			var message = "the context's " + ContextAttributeKeys.RCDSYN_079 + " attribute is not set";
			throw new IllegalArgumentException(message);
		} else {
			rcdsyn079 = context.getAttribute(ContextAttributeKeys.RCDSYN_079);
			dictionaryElementDataCollector.setRcdsyn079s(List.of(rcdsyn079));
		}
		
		if (dictionary != previousDictionary || rcdsyn079 != previousRcdsyn079) {
			// only get data from the dictionary when needed
			toplevelElements.clear();
		}
	}

}
