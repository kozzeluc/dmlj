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

import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.CatalogElementDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.CatalogRecordDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.CatalogSystemOwnedIndexDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.CatalogUserOwnedSetDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.DictionaryAreaDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.DictionaryElementDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.DictionaryRecordDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.DictionarySetDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.collector.SchemaDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.importtool.context.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Rowid;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.schema.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Column1028;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constraint1029;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Index1041;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sa018;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sacall020;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sam056;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdes044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdr042;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Smr052;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sor046;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr036;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcall040;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcd113;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table1050;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;

public class SchemaImportTool implements ISchemaImportTool {
	private boolean addMissingCatalogComponents = false;
	private IDataCollectorRegistry dataCollectorRegistry;
	private DictionaryElementDataCollector dictionaryElementDataCollector;
	private SchemaImportSession session;
	private Map<Rowid, Srcd113> srcd113s = new HashMap<>();
	private Map<Rowid, Table1050> table1050s = new HashMap<>();

	@Override
	public void dispose() {	
		session.close();				
	}

	private Table1050 findTable(String name1050) {
		return table1050s.values().stream()
				.filter(table1050 -> table1050.getName1050().equals(name1050))
				.findFirst()
				.orElse(null);
	}

	@Override
	public ContextCollection getAreaContexts() {
		var list = new ArrayList<Sa018>();
		
		var sa018s = new HashMap<Rowid, Sa018>();
		var areaListQuery = new Query.Builder().forAreaList(session).build();
		session.runQuery(areaListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {	
				var sa018 = new Sa018();
				sa018.setRowid(JdbcTools.getRowid(row, Sa018.ROWID));
				sa018.setSaNam018(row.getString(Sa018.SA_NAM_018));
				list.add(sa018);
				sa018s.put(sa018.getRowid(), sa018);
			}			
		});	
		
		var areaProcedureListQuery = new Query.Builder().forAreaProcedureList(session).build();
		session.runQuery(areaProcedureListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var sacall020 = new Sacall020();
				sacall020.setCallProc020(row.getString(Sacall020.CALL_PROC_020));
				sacall020.setCallTime020(row.getString(Sacall020.CALL_TIME_020));
				sacall020.setDbpAccess020(row.getString(Sacall020.DBP_ACCESS_020));
				sacall020.setDbpFunc020(row.getString(Sacall020.DBP_FUNC_020));
				sacall020.setDbpMode020(row.getString(Sacall020.DBP_MODE_020));				
				
				var sa018 = sa018s.get(JdbcTools.getRowid(row, Sa018.ROWID));
				sa018.getSacall020s().add(sacall020);
			}			
		});				
		return new ContextCollection(list);
	}
	
	@Override
	public ContextCollection getRecordContexts() {
		var regularRecordContextLookups = doRegularRecordContextLookups();
		var allRecordContexts = new ArrayList<Object>();
		allRecordContexts.addAll(regularRecordContextLookups.srcd113s());
		dictionaryElementDataCollector.setRcdsyn079s(new ArrayList<>(regularRecordContextLookups.rcdsyn079s().values()));						
		
		var rcdsyn079bs = locateBaseRecordSynonyms(regularRecordContextLookups);
		var listOfRcdsyn079sInvolved = new ArrayList<Rcdsyn079>(regularRecordContextLookups.rcdsyn079s().values());
		for (var rcdsyn079b : rcdsyn079bs.values()) {		
			if (!regularRecordContextLookups.rcdsyn079s().containsKey(rcdsyn079b.getRowid())) {
				listOfRcdsyn079sInvolved.add(rcdsyn079b);
			}
		}
		Collections.sort(listOfRcdsyn079sInvolved, (r1, r2) -> {
			if (r1.getRsynName079().equals(r2.getRsynName079())) {
				return r1.getRsynVer079() - r2.getRsynVer079();
			} else {
				return r1.getRsynName079().compareTo(r2.getRsynName079());
			}
		});
		
		collectAllElementsForAllRegularRecords(regularRecordContextLookups, listOfRcdsyn079sInvolved, rcdsyn079bs);
		collectRecordProcedures();
		
		// catalog records (only if the user wants us to add them)
		if (session.isIdmsntwkVersion1() && addMissingCatalogComponents) {
			// we need a record data collector capable of dealing with TABLE-1050 records (to derive the record
			// name, which is equal to the table name suffixed with the record id
			var rdc =dataCollectorRegistry.getRecordDataCollector(Table1050.class);
			var catalogRecordListQuery = new Query.Builder().forCatalogRecordList().build();
			session.runQuery(catalogRecordListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					var table1050 = new Table1050();
					table1050.setRowid(JdbcTools.getRowid(row, Table1050.ROWID));
					table1050.setArea1050(row.getString(Table1050.AREA_1050));
					table1050.setLocmode1050(row.getString(Table1050.LOCMODE_1050));
					table1050.setName1050(row.getString(Table1050.NAME_1050));
					table1050.setTableid1050(row.getShort(Table1050.TABLEID_1050));
					table1050s.put(table1050.getRowid(), table1050);
					var recordName = rdc.getName(table1050);
					if (!regularRecordContextLookups.regularRecords().contains(recordName)) {									
						allRecordContexts.add(table1050);	
					}
				}				
			});	
			
			// get ALL elements for ALL catalog records; these are all root elements
			var catalogElementListQuery = new Query.Builder().forCatalogElementList().build();
			session.runQuery(catalogElementListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					var rowidOfTable1050 = JdbcTools.getRowid(row, Table1050.ROWID);
					var table1050 = table1050s.get(rowidOfTable1050);
					
					var column1028 = new Column1028();
					column1028.setName1028(row.getString(Column1028.NAME_1028));
					column1028.setNulls1028(row.getString(Column1028.NULLS_1028));
					column1028.setNumber1028(row.getShort(Column1028.NUMBER_1028));
					column1028.setType1028(row.getString(Column1028.TYPE_1028));
					column1028.setVlength1028(row.getShort(Column1028.VLENGTH_1028));
					column1028.setTable1050(table1050);
					table1050.getColumn1028s().add(column1028);
				}				
			});
		}
		return new ContextCollection(allRecordContexts);
	}
	
	private RegularRecordContextLookups doRegularRecordContextLookups() {
		var srcd113List = new ArrayList<Srcd113>();
		var sr036s = new HashMap<Rowid, Sr036>();
		var rcdsyn079s = new HashMap<Rowid, Rcdsyn079>();
		var regularRecords = new ArrayList<String>();
		var recordListQuery = new Query.Builder().forRecordList(session).build();
		session.runQuery(recordListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var sr036 = new Sr036();
				sr036.setRowid(JdbcTools.getRowid(row, Sr036.ROWID));
				sr036.setSrNam036(row.getString(Sr036.SR_NAM_036));
				sr036.setRcdVers036(row.getShort(Sr036.RCD_VERS_036));
				sr036s.put(sr036.getRowid(), sr036);
								
				var rcdsyn079 = new Rcdsyn079();
				rcdsyn079.setRowid(JdbcTools.getRowid(row, Rcdsyn079.ROWID));
				rcdsyn079.setRsynName079(row.getString(Rcdsyn079.RSYN_NAME_079));
				rcdsyn079.setRsynVer079(row.getShort(Rcdsyn079.RSYN_VER_079));
				rcdsyn079.setSr036(sr036);
				sr036.setRcdsyn079(rcdsyn079);
				rcdsyn079s.put(rcdsyn079.getRowid(), rcdsyn079);
								
				var sam056 = new Sam056();
				sam056.setSrNam056(row.getString(Sam056.SR_NAM_056));
				sam056.setSaNam056(row.getString(Sam056.SA_NAM_056));
								
				var srcd113 = createSrcd113(row);
				srcd113.setRcdsyn079(rcdsyn079);
				srcd113.setSam056(sam056);
				srcd113s.put(srcd113.getRowid(), srcd113);
				
				// definitions for INDEX-1041 and TABLE-1050 are much more complete in the catalog, so defer the
				// creation of these records in case we're dealing with IDMSNTWK version 1 AND the user has
				// indicated to not add the catalog records...
				if (!session.isIdmsntwkVersion1() || !addMissingCatalogComponents || 
					!sam056.getSrNam056().equals("INDEX-1041") && !sam056.getSrNam056().equals("TABLE-1050")) {

					srcd113List.add(srcd113);
					regularRecords.add(sam056.getSrNam056());
				}				
			}			
		});
		return new RegularRecordContextLookups(srcd113List, sr036s, rcdsyn079s, regularRecords);
	}
	
	private Srcd113 createSrcd113(ResultSet row) throws SQLException {
		var srcd113 = new Srcd113();
		srcd113.setRowid(JdbcTools.getRowid(row, Srcd113.ROWID));
		srcd113.setDspl113(row.getShort(Srcd113.DSPL_113));
		srcd113.setMinFrag113(row.getShort(Srcd113.MIN_FRAG_113));
		srcd113.setMinRoot113(row.getShort(Srcd113.MIN_ROOT_113));
		srcd113.setMode113(row.getShort(Srcd113.MODE_113));
		srcd113.setPageCount113(row.getInt(Srcd113.PAGE_COUNT_113));
		srcd113.setPageCountPercent113(row.getShort(Srcd113.PAGE_COUNT_PERCENT_113));
		srcd113.setPageOffset113(row.getInt(Srcd113.PAGE_OFFSET_113));
		srcd113.setPageOffsetPercent113(row.getShort(Srcd113.PAGE_OFFSET_PERCENT_113));
		srcd113.setRecType113(row.getString(Srcd113.REC_TYPE_113));
		srcd113.setSrId113(row.getShort(Srcd113.SR_ID_113));
		srcd113.setSubarea113(row.getString(Srcd113.SUBAREA_113));
		srcd113.setSymbolDisplace113(row.getString(Srcd113.SYMBOL_DISPLACE_113));
		srcd113.setVsamType113(row.getString(Srcd113.VSAM_TYPE_113));
		return srcd113;
	}
	
	private Map<Rowid, Rcdsyn079> locateBaseRecordSynonyms(RegularRecordContextLookups regularRecordContextLookups) {
		// locate the base record synonym and hook it to the SR-036 when different from the record synonym
		// referenced by the schema
		var rcdsyn079bs = new HashMap<Rowid, Rcdsyn079>();
		var baseRecordSynonymListQuery = new Query.Builder()
				.forBaseRecordSynonymList(session, new ArrayList<>(regularRecordContextLookups.sr036s().values()))
				.build();
		session.runQuery(baseRecordSynonymListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var rowidSr036 = JdbcTools.getRowid(row, Sr036.ROWID);
				if (regularRecordContextLookups.sr036s().containsKey(rowidSr036)) {
					var sr036 = regularRecordContextLookups.sr036s().get(rowidSr036);
					var rowidRcdsyn079b = JdbcTools.getRowid(row, Rcdsyn079.ROWID);
					var currentRcdsyn079b = sr036.getRcdsyn079();
					if (!rowidRcdsyn079b.equals(currentRcdsyn079b.getRowid())) {
						var rsynName079 = row.getString(Rcdsyn079.RSYN_NAME_079).trim();
						var rsynVer079 = row.getShort(Rcdsyn079.RSYN_VER_079);
						if (!rsynName079.equals(currentRcdsyn079b.getRsynName079()) || 
							rsynVer079 != currentRcdsyn079b.getRsynVer079()) {
							
							// only set the base RCDSYN-079 occurrence when the name or version are different
							// from the RCDSYN-079 that is connected to the schema
							var newRcdsyn079 = new Rcdsyn079();
							newRcdsyn079.setRowid(rowidRcdsyn079b);
							newRcdsyn079.setRsynName079(rsynName079);
							newRcdsyn079.setRsynVer079(rsynVer079);
							newRcdsyn079.setSr036(sr036);
							sr036.setRcdsyn079b(newRcdsyn079);
							rcdsyn079bs.put(rowidRcdsyn079b, newRcdsyn079);
						}
					}
				}
			}					
		});
		return rcdsyn079bs;
	}
	
	private void collectAllElementsForAllRegularRecords(RegularRecordContextLookups regularRecordContextLookups,
			List<Rcdsyn079> listOfRcdsyn079sInvolved, Map<Rowid, Rcdsyn079> rcdsyn079bs) {
		
		// get ALL elements for ALL regular records
		var sdr042rowids = new HashMap<Rowid, Rowid>(); // for postprocessing RCDSYN-079bs
		var elementListQuery = new Query.Builder().forElementList(session, listOfRcdsyn079sInvolved).build();
		session.runQuery(elementListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {				
				var rowidOfRcdsyn079 = JdbcTools.getRowid(row, Rcdsyn079.ROWID);
				if (regularRecordContextLookups.rcdsyn079s().containsKey(rowidOfRcdsyn079)) {					
					var rcdsyn079 = regularRecordContextLookups.rcdsyn079s().get(rowidOfRcdsyn079);					
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
				} else if (rcdsyn079bs.containsKey(rowidOfRcdsyn079)) {					
					var rcdsyn079b = rcdsyn079bs.get(rowidOfRcdsyn079);
					var namesyn083 = new Namesyn083();	
					namesyn083.setRowid(JdbcTools.getRowid(row, Namesyn083.ROWID));
					namesyn083.setDependOn083(row.getString(Namesyn083.DEPEND_ON_083));
					namesyn083.setRdfNam083(row.getString(Namesyn083.RDF_NAM_083));
					namesyn083.setSynName083(row.getString(Namesyn083.SYN_NAME_083));
					namesyn083.setRcdsyn079(rcdsyn079b);
					rcdsyn079b.getNamesyn083s().add(namesyn083);					
					// we have no control over the order in which record synonyms are returned, so defer setting
					// the NAMESYN-083's SDR-042 reference until all rows are processed
					var rowidOfSdr042 = JdbcTools.getRowid(row, Sdr042.ROWID);
					sdr042rowids.put(namesyn083.getRowid(), rowidOfSdr042);
				} else {
					throw new IllegalStateException("unexpected row; rowid of RCDSYN-079=" + rowidOfRcdsyn079);
				}			
			}				
		});
		for (var rcdsyn079b : rcdsyn079bs.values()) {
			// RCDSYN-079b postprocessing: set each NAMESYN-083's SDR-042 reference
			var rcdsyn079 = rcdsyn079b.getSr036().getRcdsyn079();
			for (var namesyn083 : rcdsyn079b.getNamesyn083s()) {
				var rowidOfSdr042 = sdr042rowids.get(namesyn083.getRowid());
				var sdr042 = rcdsyn079.getNamesyn083(rowidOfSdr042).getSdr042();				
				namesyn083.setSdr042(sdr042);
			}
		}
		var elementCommentListQuery = new Query.Builder()
				 .forElementCommentList(session, new ArrayList<>(regularRecordContextLookups.rcdsyn079s().values()))
				 .build();
		session.runQuery(elementCommentListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {				
				var rowidOfRcdsyn079 = JdbcTools.getRowid(row, Rcdsyn079.ROWID);
				if (regularRecordContextLookups.rcdsyn079s().containsKey(rowidOfRcdsyn079)) {	
					var rcdsyn079 = regularRecordContextLookups.rcdsyn079s().get(rowidOfRcdsyn079);				
					var sdr042 = rcdsyn079.getNamesyn083(JdbcTools.getRowid(row, Sdr042.ROWID)).getSdr042();					
					var sdes044 = new Sdes044();
					sdes044.setCmtId044(row.getInt(Sdes044.CMT_ID_044));
					sdes044.setFirstCmtInfo044(row.getString(Sdes044.CMT_INFO_044_1));
					sdes044.setSecondCmtInfo044(row.getString(Sdes044.CMT_INFO_044_2));
					sdr042.getSdes044s().add(sdes044);				
				} else {
					throw new IllegalStateException("unexpected row; rowid of RCDSYN-079=" + rowidOfRcdsyn079);
				}				
			}				
		});
	}

	private void collectRecordProcedures() {
		var recordProcedureListQuery = new Query.Builder().forRecordProcedureList(session).build();
		session.runQuery(recordProcedureListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var srcall040 = new Srcall040();
				srcall040.setCallProc040(row.getString(Srcall040.CALL_PROC_040));
				srcall040.setCallTime040(row.getString(Srcall040.CALL_TIME_040));
				srcall040.setDbpFunc040(row.getString(Srcall040.DBP_FUNC_040));
				
				var rowidSrcd113 = JdbcTools.getRowid(row, Srcd113.ROWID);
				var srcd113 = srcd113s.get(rowidSrcd113);
				srcd113.getSrcall040s().add(srcall040);
			}			
		});
	}
	
	@Override
	public ContextCollection getRootElementContexts(Object recordContext) {		
		if (recordContext instanceof Srcd113 srcd113) {
			// regular record
			var list = new ArrayList<Namesyn083>();
			var rcdsyn079 = srcd113.getRcdsyn079();
			var topLevel = rcdsyn079.getNamesyn083s().get(0).getSdr042().getDrLvl042();
			for (var namesyn_83 : rcdsyn079.getNamesyn083s()) {
				var sdr042 = namesyn_83.getSdr042();
				if (sdr042.getDrLvl042() == topLevel) {
					list.add(namesyn_83);
				}
			}
			return new ContextCollection(list);
		} else if (recordContext instanceof Table1050 table1050) {				
			// catalog derived record
			return new ContextCollection(table1050.getColumn1028s());			
		} else {
			throw new IllegalArgumentException("unknown record context type: " + recordContext.getClass().getName());
		}		
	}	

	@Override
	public ContextCollection getSetContexts() {
		var regularSetContexts = getRegularSetContexts();
		var regularSetNames = regularSetContexts.stream()
				.map(Sor046::getSetNam046)
				.toList();
		var allSetContexts = new ArrayList<Object>();
		allSetContexts.addAll(regularSetContexts);
		if (session.isIdmsntwkVersion1() && addMissingCatalogComponents) {
			allSetContexts.addAll(getCatalogDerivedSetContexts(regularSetNames));
		}		
		return new ContextCollection(allSetContexts);
	}
	
	private List<Sor046> getRegularSetContexts() {
		var list = new ArrayList<Sor046>();
		var sor046ToSrcdMappings = new HashMap<Rowid, Rowid>();
		var setOwnerListQuery = new Query.Builder().forSetOwnerList(session).build();
		session.runQuery(setOwnerListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var rowidOfSor046 = JdbcTools.getRowid(row, Sor046.ROWID);
				var rowidOfSrcd113 = JdbcTools.getRowid(row, Srcd113.ROWID);
				sor046ToSrcdMappings.put(rowidOfSor046, rowidOfSrcd113);
			}
		});		
		var sor046s = new HashMap<Rowid, Sor046>();
		var setListQuery = new Query.Builder().forSetList(session).build();
		session.runQuery(setListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var rowidOfSor046 = JdbcTools.getRowid(row, Sor046.ROWID);
				Sor046 sor046;
				if (!sor046s.containsKey(rowidOfSor046)) {					
					var srcd113 = srcd113s.get(sor046ToSrcdMappings.get(rowidOfSor046)); 
					
					sor046 = new Sor046();
					sor046.setSrcd113(srcd113);
					sor046.setRowid(rowidOfSor046);
					sor046.setIndexDisp046(row.getShort(Sor046.INDEX_DISP_046));
					sor046.setIndexMembers046(row.getShort(Sor046.INDEX_MEMBERS_046));
					sor046.setNxtDbk046(row.getShort(Sor046.NXT_DBK_046));
					sor046.setOrd046(row.getShort(Sor046.ORD_046));
					sor046.setPageCount046(row.getInt(Sor046.PAGE_COUNT_046));
					sor046.setPageCountPercent046(row.getShort(Sor046.PAGE_COUNT_PERCENT_046));
					sor046.setPageOffset046(row.getInt(Sor046.PAGE_OFFSET_046));
					sor046.setPageOffsetPercent046(row.getShort(Sor046.PAGE_OFFSET_PERCENT_046));
					sor046.setPriDbk046(row.getShort(Sor046.PRI_DBK_046));
					sor046.setSaNam046(row.getString(Sor046.SA_NAM_046));
					sor046.setSetMode046(row.getShort(Sor046.SET_MODE_046));
					sor046.setSetNam046(row.getString(Sor046.SET_NAM_046));
					sor046.setSetOrd046(row.getShort(Sor046.SET_ORD_046));
					sor046.setSorId046(row.getShort(Sor046.SOR_ID_046));
					sor046.setSymbolIndex046(row.getString(Sor046.SYMBOL_INDEX_046));
					sor046.setSubarea046(row.getString(Sor046.SUBAREA_046));
					sor046s.put(rowidOfSor046, sor046);
					
					// defer the creation of "AREA-INDEX", "AREA-TABLE" and "TABLE-INDEX" since they are stored
					// in the catalog too...
					if (!session.isIdmsntwkVersion1() || !addMissingCatalogComponents ||  
						!sor046.getSetNam046().equals("AREA-INDEX") && !sor046.getSetNam046().equals("AREA-TABLE") &&
						!sor046.getSetNam046().equals("TABLE-INDEX")) {
						
						list.add(sor046);
					}
				} else {
					sor046 = sor046s.get(rowidOfSor046);
				}
				
				var rowidOfSrcd113 = JdbcTools.getRowid(row, Srcd113.ROWID);
				var srcd113 = srcd113s.get(rowidOfSrcd113);
				
				var smr052 = new Smr052();
				smr052.setRowid(JdbcTools.getRowid(row, Smr052.ROWID));
				smr052.setSrcd113(srcd113);
				smr052.setDup052(row.getShort(Smr052.DUP_052));
				smr052.setMrCntrl052(row.getShort(Smr052.MR_CNTRL_052));
				smr052.setNxtDbk052(row.getShort(Smr052.NXT_DBK_052));
				smr052.setOwnDbk052(row.getShort(Smr052.OWN_DBK_052));
				smr052.setPriDbk052(row.getShort(Smr052.PRI_DBK_052));
				smr052.setSetNam052(row.getString(Smr052.SET_NAM_052));
				smr052.setSort052(row.getShort(Smr052.SORT_052));
				sor046.getSmr052s().add(smr052);
			}
		});
		return list;
	}
	
	private List<Object> getCatalogDerivedSetContexts(List<String> regularSets) {
		var list = new ArrayList<Object>();
		var catalogSetListQuery = new Query.Builder().forCatalogSetList().build();
		session.runQuery(catalogSetListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var constraint1029 = new Constraint1029();
				constraint1029.setName1029(row.getString(Constraint1029.NAME_1029));															
				if (!regularSets.contains(constraint1029.getName1029())) {
					var rowidOfReferencedTable1050 = JdbcTools.getRowid(row, Table1050.ROWID);
					var referencedTable1050 = table1050s.get(rowidOfReferencedTable1050);
					constraint1029.setReferencedTable1050(referencedTable1050);
					var referencingTableName = JdbcTools.removeTrailingSpaces(row.getString(Constraint1029.TABLE_1029));
					var referencingTable1050 = findTable(referencingTableName);
					constraint1029.setReferencingTable1050(referencingTable1050);
					constraint1029.setCompress1029(row.getString(Constraint1029.COMPRESS_1029));
					constraint1029.setDisplacement1029(row.getShort(Constraint1029.DISPLACEMENT_1029));
					constraint1029.setIxblkcontains1029(row.getShort(Constraint1029.IXBLKCONTAINS_1029));
					constraint1029.setNext1029(row.getShort(Constraint1029.NEXT_1029));
					constraint1029.setOwner1029(row.getShort(Constraint1029.OWNER_1029)); 		
					constraint1029.setPrior1029(row.getShort(Constraint1029.PRIOR_1029)); 		
					constraint1029.setRefnext1029(row.getShort(Constraint1029.REFNEXT_1029));
					constraint1029.setRefprior1029(row.getShort(Constraint1029.REFPRIOR_1029)); 						
					constraint1029.setSortorder1029(row.getString(Constraint1029.SORTORDER_1029));
					constraint1029.setType1029(row.getString(Constraint1029.TYPE_1029));
					constraint1029.setUnique1029(row.getString(Constraint1029.UNIQUE_1029));
					list.add(constraint1029);
				}
			}
		});
		
		// system owned indexed sets...
		var catalogIndexListQuery = new Query.Builder().forCatalogIndexList().build();
		session.runQuery(catalogIndexListQuery, new IRowProcessor() {				
			@Override
			public void processRow(ResultSet row) throws SQLException {
				var rowidOfTable1050 = JdbcTools.getRowid(row, Table1050.ROWID);
				var table1050 = table1050s.get(rowidOfTable1050);
				
				var index1041 = new Index1041();
				index1041.setArea1041(row.getString(Index1041.AREA_1041));
				index1041.setCompress1041(row.getString(Index1041.COMPRESS_1041));
				index1041.setDisplacement1041(row.getShort(Index1041.DISPLACEMENT_1041));
				index1041.setIxblkcontains1041(row.getShort(Index1041.IXBLKCONTAINS_1041));
				index1041.setName1041(row.getString(Index1041.NAME_1041));
				index1041.setUnique1041(row.getString(Index1041.UNIQUE_1041));					
				index1041.setTable1050(table1050);
				if (!regularSets.contains(index1041.getName1041())) {
					list.add(index1041);
				}					
			}
		});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T elementContext) {
		if (elementContext instanceof Namesyn083 namesyn083) {
			// regular dictionary element
			return (Collection<T>) getSubordinateElementContextsFromNamesyn083(namesyn083);
		} else if (elementContext instanceof Column1028) {
			// no element hierarchies for catalog derived records
			return Collections.emptyList();
		} else {
			throw new IllegalArgumentException("unknown element context type: " + elementContext.getClass().getName());
		}
	}
	
	private List<Namesyn083> getSubordinateElementContextsFromNamesyn083(Namesyn083 namesyn083) {
		// we don't use the nested element structure in the dictionary because that is not always what is in the record
		var context = new SubordinateElementWorkContext(namesyn083);
		for (var namesyn083b : context.rcdsyn079.getNamesyn083s()) {
			processNameSyn083(namesyn083b, context);
			if (context.done) {
				break;
			}
		}
		return context.list;
	}
	
	private void processNameSyn083(Namesyn083 namesyn083, SubordinateElementWorkContext context) {
		if (context.active) {
			var sdr042 = namesyn083.getSdr042();
			var level = sdr042.getDrLvl042();					
			if (level <= context.ourLevel) {
				// a level number smaller than or equal to ours means we're done
				context.done = true;
			} else if (level == 88) {
				// a condition name is encountered
				if (context.level88level == context.ourLevel) {
					context.list.add(namesyn083);
				}
			} else {
				// a higher level number is encountered
				if (context.nextHigherLevel == -1) {
					context.nextHigherLevel = level;
				}
				context.level88level = level;
				if (level == context.nextHigherLevel) {
					context.list.add(namesyn083);
				}
			}
		} else if (namesyn083 == context.namesyn083) {
			context.active = true;
		}
	}

	@Override
	public void init(IDataEntryContext context, Properties parameters, IDataCollectorRegistry dataCollectorRegistry) {
		this.dataCollectorRegistry = dataCollectorRegistry;		
		
		Dictionary dictionary;
		if (!context.containsAttribute(ContextAttributeKeys.DICTIONARY)) {
			throw new IllegalArgumentException("the context's dictionary attribute is not set");
		} else {
			dictionary = context.getAttribute(ContextAttributeKeys.DICTIONARY);
		}
		
		String schemaName;
		if (!context.containsAttribute(IDataEntryContext.SCHEMA_NAME)) {
			throw new IllegalArgumentException("the context's schemaName attribute is not set");
		} else {
			schemaName = context.getAttribute(IDataEntryContext.SCHEMA_NAME);
		}
		
		short schemaVersion;
		if (!context.containsAttribute(IDataEntryContext.SCHEMA_VERSION)) {
			schemaVersion = 1;
		} else {
			schemaVersion = ((Short) context.getAttribute(IDataEntryContext.SCHEMA_VERSION)).shortValue();
		}
		
		if (context.containsAttribute(ContextAttributeKeys.ADD_MISSING_CATALOG_COMPONENTS)) {
			Boolean b = context.getAttribute(ContextAttributeKeys.ADD_MISSING_CATALOG_COMPONENTS);
			addMissingCatalogComponents = b.booleanValue();
		}
		
		session = new SchemaImportSession(dictionary, schemaName, schemaVersion);
		session.open();
		
		var schemaDataCollector = new SchemaDataCollector(session);
		dataCollectorRegistry.registerSchemaDataCollector(schemaDataCollector);
		
		var dictionaryAreaDataCollector = new DictionaryAreaDataCollector(session);
		dataCollectorRegistry.registerAreaDataCollector(Sa018.class, dictionaryAreaDataCollector);
		
		var dictionaryRecordDataCollector = new DictionaryRecordDataCollector(session);
		dataCollectorRegistry.registerRecordDataCollector(Srcd113.class, dictionaryRecordDataCollector);
		
		dictionaryElementDataCollector = new DictionaryElementDataCollector(session);	
		dataCollectorRegistry.registerElementDataCollector(Namesyn083.class, dictionaryElementDataCollector);
		
		var dictionarySetDataCollector = new DictionarySetDataCollector(session);	
		dataCollectorRegistry.registerSetDataCollector(Sor046.class, dictionarySetDataCollector);
		
		if (addMissingCatalogComponents) {
			var catalogRecordDataCollector = new CatalogRecordDataCollector(session);
			dataCollectorRegistry.registerRecordDataCollector(Table1050.class, catalogRecordDataCollector);
			
			var catalogElementDataCollector = new CatalogElementDataCollector(session);
			dataCollectorRegistry.registerElementDataCollector(Column1028.class, catalogElementDataCollector);
			
			var catalogUserOwnedSetDataCollector = new CatalogUserOwnedSetDataCollector(session);
			dataCollectorRegistry.registerSetDataCollector(Constraint1029.class, catalogUserOwnedSetDataCollector);
			
			var catalogSystemOwnedIndexDataCollector = new CatalogSystemOwnedIndexDataCollector(session);
			dataCollectorRegistry.registerSetDataCollector(Index1041.class, catalogSystemOwnedIndexDataCollector);
		}
	}
	
	private static record RegularRecordContextLookups(List<Srcd113> srcd113s, Map<Rowid, Sr036> sr036s,
			Map<Rowid, Rcdsyn079> rcdsyn079s, List<String> regularRecords) {
	}
	
	private static class SubordinateElementWorkContext {
		private final Namesyn083 namesyn083;
		private final Rcdsyn079 rcdsyn079;
		private final short ourLevel;
		private final List<Namesyn083> list = new ArrayList<>();
		private short nextHigherLevel = -1;
		private short level88level;
		private boolean active = false;
		private boolean done = false;
		
		private SubordinateElementWorkContext(Namesyn083 namesyn083) {
			this.namesyn083 = namesyn083;
			rcdsyn079 = namesyn083.getRcdsyn079();
			ourLevel = namesyn083.getSdr042().getDrLvl042();
			level88level = ourLevel;
		}
		
	}

}
