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
package org.lh.dmlj.schema.editor.dictionary.tools.importtool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.DictionaryElementDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.context.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IQuery;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.RecordElementsImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn_083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn_079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdes_044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdr_042;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr_036;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.elements.IRecordElementsImportTool;

public class RecordElementsImportTool implements IRecordElementsImportTool {
	
	private DictionaryElementDataCollector dictionaryElementDataCollector;
	private Dictionary dictionary;
	private Rcdsyn_079 rcdsyn_079;	
	private List<Namesyn_083> toplevelElements = new ArrayList<>();

	public RecordElementsImportTool() {
		super();
	}

	@Override
	public void dispose() {
	}

	@Override
	public Collection<?> getRootElementContexts() {
		
		if (!toplevelElements.isEmpty()) {
			return new ArrayList<>(toplevelElements);
		}
		
		rcdsyn_079.getSr_036().setRcdsyn_079b(null);		
		rcdsyn_079.getNamesyn_083s().clear();
		
		RecordElementsImportSession session =
			new RecordElementsImportSession(dictionary, rcdsyn_079.getRsynName_079(), 
										    rcdsyn_079.getRsynVer_079());
		session.open();
		dictionaryElementDataCollector.SetSession(session);
		
		// locate the base record synonym and hook it to the SR-036 when different from the record 
		// synonym 	
		IQuery baseRecordSynonymListQuery = 
			new Query.Builder()
					 .forBaseRecordSynonymList(session, Arrays.asList(new Sr_036[] { rcdsyn_079.getSr_036() }))
					 .build();
		session.runQuery(baseRecordSynonymListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				Rowid rowidRcdsyn_079b = JdbcTools.getRowid(row, Rcdsyn_079.ROWID);
				if (rowidRcdsyn_079b != rcdsyn_079.getRowid()) {
					String rsynName_079 = row.getString(Rcdsyn_079.RSYN_NAME_079).trim();
					short rsynVer_079 = row.getShort(Rcdsyn_079.RSYN_VER_079);
					if (!rsynName_079.equals(rcdsyn_079.getRsynName_079()) || 
						rsynVer_079 != rcdsyn_079.getRsynVer_079()) {
						
						// only set the base RCDSYN-079 occurrence when the name or version are
						// different from the RCDSYN-079 that is in the context
						Rcdsyn_079 rcdsyn_079b = new Rcdsyn_079();
						rcdsyn_079b.setRowid(rowidRcdsyn_079b);
						rcdsyn_079b.setRsynName_079(rsynName_079);
						rcdsyn_079b.setRsynVer_079(rsynVer_079);
						rcdsyn_079b.setSr_036(rcdsyn_079.getSr_036());
						rcdsyn_079.getSr_036().setRcdsyn_079b(rcdsyn_079b);
					}
				}				
			}					
		});	
		final Rcdsyn_079 rcdsyn_079b = rcdsyn_079.getSr_036().getRcdsyn_079b();
		
		List<Rcdsyn_079> listOfRcdsyn_079sInvolved = new ArrayList<>(); 
		listOfRcdsyn_079sInvolved.add(rcdsyn_079);
		if (rcdsyn_079b != null) {
			listOfRcdsyn_079sInvolved.add(rcdsyn_079b);
		}
		Collections.sort(listOfRcdsyn_079sInvolved, new Comparator<Rcdsyn_079>() {
			@Override
			public int compare(Rcdsyn_079 r1, Rcdsyn_079 r2) {
				if (r1.getRsynName_079().equals(r2.getRsynName_079())) {
					return r1.getRsynVer_079() - r2.getRsynVer_079();
				} else {
					return r1.getRsynName_079().compareTo(r2.getRsynName_079());
				}
			}			
		});
		
		// get ALL elements for ALL regular records
		final Map<Rowid, Rowid> sdr_042_rowids = new HashMap<>(); // for postprocessing RCDSYN-079bs
		IQuery elementListQuery = new Query.Builder().forElementList(session, listOfRcdsyn_079sInvolved).build();
		session.runQuery(elementListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {				
				Rowid rowidOfRcdsyn_079 = JdbcTools.getRowid(row, Rcdsyn_079.ROWID);
				if (rowidOfRcdsyn_079 == rcdsyn_079.getRowid()) {										
					Namesyn_083 namesyn_083 = new Namesyn_083();	
					namesyn_083.setRowid(JdbcTools.getRowid(row, Namesyn_083.ROWID));
					namesyn_083.setDependOn_083(row.getString(Namesyn_083.DEPEND_ON_083));
					namesyn_083.setRdfNam_083(row.getString(Namesyn_083.RDF_NAM_083));
					namesyn_083.setSynName_083(row.getString(Namesyn_083.SYN_NAME_083));
					namesyn_083.setRcdsyn_079(rcdsyn_079);
					rcdsyn_079.getNamesyn_083s().add(namesyn_083);					
					Sdr_042 sdr_042 = new Sdr_042();					
					sdr_042.setRowid(JdbcTools.getRowid(row, Sdr_042.ROWID));
					sdr_042.setDrLvl_042(row.getShort(Sdr_042.DR_LVL_042));
					sdr_042.setDrNam_042(row.getString(Sdr_042.DR_NAM_042));
					sdr_042.setOcc_042(row.getShort(Sdr_042.OCC_042));
					sdr_042.setPic_042(row.getString(Sdr_042.PIC_042));
					sdr_042.setUse_042(row.getShort(Sdr_042.USE_042));
					namesyn_083.setSdr_042(sdr_042);					
				} else if (rcdsyn_079b != null && rowidOfRcdsyn_079 == rcdsyn_079b.getRowid()) {					
					Namesyn_083 namesyn_083 = new Namesyn_083();	
					namesyn_083.setRowid(JdbcTools.getRowid(row, Namesyn_083.ROWID));
					namesyn_083.setDependOn_083(row.getString(Namesyn_083.DEPEND_ON_083));
					namesyn_083.setRdfNam_083(row.getString(Namesyn_083.RDF_NAM_083));
					namesyn_083.setSynName_083(row.getString(Namesyn_083.SYN_NAME_083));
					namesyn_083.setRcdsyn_079(rcdsyn_079b);
					rcdsyn_079b.getNamesyn_083s().add(namesyn_083);					
					// we have no control over the order in which record synonyms are returned, so 
					// defer setting the NAMESYN-083's SDR-042 reference until all rows are 
					// processed
					Rowid rowidOfSdr_042 = JdbcTools.getRowid(row, Sdr_042.ROWID);
					sdr_042_rowids.put(namesyn_083.getRowid(), rowidOfSdr_042);
				} else {
					throw new RuntimeException("unexpected row; rowid of RCDSYN-079=" + rowidOfRcdsyn_079);
				}			
			}				
		});
		
		// RCDSYN-079b postprocessing: set each NAMESYN-083's SDR-042 reference
		if (rcdsyn_079b != null) {
			for (Namesyn_083 namesyn_083 : rcdsyn_079b.getNamesyn_083s()) {
				Rowid rowidOfSdr_042 = sdr_042_rowids.get(namesyn_083.getRowid());
				Sdr_042 sdr_042 = rcdsyn_079.getNamesyn_083(rowidOfSdr_042).getSdr_042();				
				namesyn_083.setSdr_042(sdr_042);
			}
		}
		
		IQuery elementCommentListQuery = 
			new Query.Builder()
					 .forElementCommentList(session, Arrays.asList(new Rcdsyn_079[] { rcdsyn_079 }))
					 .build();
		session.runQuery(elementCommentListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {				
				Sdr_042 sdr_042 = rcdsyn_079.getNamesyn_083(JdbcTools.getRowid(row, Sdr_042.ROWID)).getSdr_042();					
				Sdes_044 sdes_044 = new Sdes_044();
				sdes_044.setCmtId_044(row.getInt(Sdes_044.CMT_ID_044));
				sdes_044.setCmtInfo_044_1(row.getString(Sdes_044.CMT_INFO_044_1));
				sdes_044.setCmtInfo_044_2(row.getString(Sdes_044.CMT_INFO_044_2));
				sdr_042.getSdes_044s().add(sdes_044);												
			}				
		});
		
		dictionaryElementDataCollector.SetSession(null);
		session.close();
		
		short topLevel = rcdsyn_079.getNamesyn_083s().get(0).getSdr_042().getDrLvl_042();
		for (Namesyn_083 namesyn_083 : rcdsyn_079.getNamesyn_083s()) {
			Sdr_042 sdr_042 = namesyn_083.getSdr_042();
			if (sdr_042.getDrLvl_042() == topLevel) {
				toplevelElements.add(namesyn_083);
			}
		}
		
		return toplevelElements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T elementContext) {
		
		Namesyn_083 namesyn_083 = (Namesyn_083) elementContext;
		Rcdsyn_079 rcdsyn_079 = namesyn_083.getRcdsyn_079();
		
		List<Namesyn_083> list = new ArrayList<>();
		
		// we don't use the nested element structure in the dictionary because that is not 
		// always what is in the record
		Sdr_042 sdr_042 = namesyn_083.getSdr_042();
		short ourLevel = sdr_042.getDrLvl_042();
		short nextHigherLevel = -1;
		short level88level = ourLevel;
		boolean active = false;
		for (Namesyn_083 namesyn_083b : rcdsyn_079.getNamesyn_083s()) {				
			if (active) {
				sdr_042 = namesyn_083b.getSdr_042();
				short level = sdr_042.getDrLvl_042();					
				if (level <= ourLevel) {
					// a level number smaller than or equal to ours means we're done
					break;
				} else if (level == 88) {
					// a condition name is encountered
					if (level88level == ourLevel) {
						list.add(namesyn_083b);
					}
				} else {
					// a higher level number is encountered
					if (nextHigherLevel == -1) {
						nextHigherLevel = level;
					}
					level88level = level;
					if (level == nextHigherLevel) {
						list.add(namesyn_083b);
					}
				}
			} else if (namesyn_083b == namesyn_083) {
				active = true;
			}				
		}
		
		return (Collection<T>) list;		
	}

	@Override
	public void init(Properties parameters, IRecordElementsDataCollectorRegistry registry) {
		dictionaryElementDataCollector = new DictionaryElementDataCollector();	
		registry.registerDataCollector(Namesyn_083.class, dictionaryElementDataCollector);
	}

	@Override
	public void setContext(IDataEntryContext context) {
		
		Dictionary previousDictionary = dictionary;
		Rcdsyn_079 previousRcdsyn_079 = rcdsyn_079;
		
		if (!context.containsAttribute(ContextAttributeKeys.DICTIONARY)) {
			String message = 
				"the context's " + ContextAttributeKeys.DICTIONARY + " attribute is not set";
			throw new IllegalArgumentException(message);
		} else {
			dictionary = context.getAttribute(ContextAttributeKeys.DICTIONARY);
		}
		
		if (!context.containsAttribute(ContextAttributeKeys.RCDSYN_079)) {
			String message = 
				"the context's " + ContextAttributeKeys.RCDSYN_079 + " attribute is not set";
			throw new IllegalArgumentException(message);
		} else {
			rcdsyn_079 = context.getAttribute(ContextAttributeKeys.RCDSYN_079);
			dictionaryElementDataCollector.setRcdsyn_079s(Arrays.asList(new Rcdsyn_079[] { rcdsyn_079 }));
		}
		
		if (dictionary != previousDictionary || rcdsyn_079 != previousRcdsyn_079) {
			// only get data from the dictionary when needed
			toplevelElements.clear();
		}
	}

}
