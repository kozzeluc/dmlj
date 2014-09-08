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
package org.lh.dmlj.schema.editor.dictionary.tools._import.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.lh.dmlj.schema.editor.dictionary.tools._import.common.ContextAttributeKeys;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.CatalogElementDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.CatalogRecordDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.CatalogSystemOwnedIndexDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.CatalogUserOwnedSetDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.DictionaryAreaDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.DictionaryElementDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.DictionaryRecordDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.DictionarySetDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools._import.tool.collector.SchemaDataCollector;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.IRowProcessor;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.JdbcTools;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.Query;
import org.lh.dmlj.schema.editor.dictionary.tools.jdbc.SchemaImportSession;
import org.lh.dmlj.schema.editor.dictionary.tools.model.Dictionary;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Column_1028;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Constraint_1029;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Index_1041;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Namesyn_083;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Rcdsyn_079;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sa_018;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sacall_020;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sam_056;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdes_044;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sdr_042;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Smr_052;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sor_046;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Sr_036;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcall_040;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Srcd_113;
import org.lh.dmlj.schema.editor.dictionary.tools.table.Table_1050;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;
import org.lh.dmlj.schema.editor.importtool.IDataCollectorRegistry;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaDataCollector;
import org.lh.dmlj.schema.editor.importtool.ISchemaImportTool;
import org.lh.dmlj.schema.editor.importtool.ISetDataCollector;

public class SchemaImportTool implements ISchemaImportTool {

	private boolean addMissingCatalogComponents = false;
	private SchemaImportSession session;
	private Map<Long, Srcd_113> srcd_113s = new HashMap<>();
	private Map<Long, Table_1050> table_1050s = new HashMap<>();
	
	public SchemaImportTool() {
		super();
	}

	@Override
	public void dispose() {	
		session.close();				
	}

	@Override
	public Collection<?> getAreaContexts() {
		final List<Sa_018> list = new ArrayList<>(); 
		Query areaListQuery = new Query.Builder().forAreaList(session).build();
		session.runQuery(areaListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				final Sa_018 sa_018 = new Sa_018();
				sa_018.setDbkey(JdbcTools.getDbkey(row, Sa_018.ROWID));
				sa_018.setSaNam_018(row.getString(Sa_018.SA_NAM_018));
				Query areaProcedureListQuery = 
					new Query.Builder().forAreaProcedureList(session, sa_018.getDbkey()).build();
				session.runQuery(areaProcedureListQuery, new IRowProcessor() {
					@Override
					public void processRow(ResultSet row) throws SQLException {
						Sacall_020 sacall_020 = new Sacall_020();
						sacall_020.setCallProc_020(row.getString(Sacall_020.CALL_PROC_020));
						sacall_020.setCallTime_020(row.getString(Sacall_020.CALL_TIME_020));
						sacall_020.setDbpAccess_020(row.getString(Sacall_020.DBP_ACCESS_020));
						sacall_020.setDbpFunc_020(row.getString(Sacall_020.DBP_FUNC_020));
						sacall_020.setDbpMode_020(row.getString(Sacall_020.DBP_MODE_020));
						sa_018.getSacall_020s().add(sacall_020);
					}			
				});
				list.add(sa_018);				
			}			
		});
		return list;
	}

	@Override
	public Collection<?> getRecordContexts() {
		
		final List<Object> list = new ArrayList<>();
		
		// regular records
		final List<String> regularRecords = new ArrayList<>();
		Query recordListQuery = new Query.Builder().forRecordList(session).build();
		session.runQuery(recordListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				
				final Sr_036 sr_036 = new Sr_036();
				sr_036.setDbkey(JdbcTools.getDbkey(row, Sr_036.ROWID));
				sr_036.setSrNam_036(row.getString(Sr_036.SR_NAM_036));
				sr_036.setRcdVers_036(row.getShort(Sr_036.RCD_VERS_036));
								
				final Rcdsyn_079 rcdsyn_079 = new Rcdsyn_079();
				rcdsyn_079.setDbkey(JdbcTools.getDbkey(row, Rcdsyn_079.ROWID));
				rcdsyn_079.setRsynName_079(row.getString(Rcdsyn_079.RSYN_NAME_079));
				rcdsyn_079.setRsynVer_079(row.getShort(Rcdsyn_079.RSYN_VER_079));
				rcdsyn_079.setSr_036(sr_036);
				sr_036.setRcdsyn_079(rcdsyn_079);
								
				Sam_056 sam_056 = new Sam_056();
				sam_056.setSrNam_056(row.getString(Sam_056.SR_NAM_056));
				sam_056.setSaNam_056(row.getString(Sam_056.SA_NAM_056));
								
				final Srcd_113 srcd_113 = new Srcd_113();
				srcd_113.setDbkey(JdbcTools.getDbkey(row, Srcd_113.ROWID));
				srcd_113.setDspl_113(row.getShort(Srcd_113.DSPL_113));
				srcd_113.setMinFrag_113(row.getShort(Srcd_113.MIN_FRAG_113));
				srcd_113.setMinRoot_113(row.getShort(Srcd_113.MIN_ROOT_113));
				srcd_113.setMode_113(row.getShort(Srcd_113.MODE_113));
				srcd_113.setPageCount_113(row.getInt(Srcd_113.PAGE_COUNT_113));
				srcd_113.setPageCountPercent_113(row.getShort(Srcd_113.PAGE_COUNT_PERCENT_113));
				srcd_113.setPageOffset_113(row.getInt(Srcd_113.PAGE_OFFSET_113));
				srcd_113.setPageOffsetPercent_113(row.getShort(Srcd_113.PAGE_OFFSET_PERCENT_113));
				srcd_113.setRecType_113(row.getString(Srcd_113.REC_TYPE_113));
				srcd_113.setSrId_113(row.getShort(Srcd_113.SR_ID_113));
				srcd_113.setSubarea_113(row.getString(Srcd_113.SUBAREA_113));
				srcd_113.setSymbolDisplace_113(row.getString(Srcd_113.SYMBOL_DISPLACE_113));
				srcd_113.setRcdsyn_079(rcdsyn_079);
				srcd_113.setSam_056(sam_056);
				srcd_113s.put(Long.valueOf(srcd_113.getDbkey()), srcd_113);
				
				// locate the base record synonym and hook it to the SR-036 when different from the
				// record synonym referenced by the schema
				Query baseRecordSynonymQuery = 
					new Query.Builder().forBaseRecordSynonym(session, sr_036.getDbkey()).build();
				session.runQuery(baseRecordSynonymQuery, new IRowProcessor() {
					@Override
					public void processRow(ResultSet row) throws SQLException {
						// TODO make sure that EXACTLY 1 row is processed
						long dbkeyRcdsyn_079b = JdbcTools.getDbkey(row, Rcdsyn_079.ROWID);
						if (dbkeyRcdsyn_079b != rcdsyn_079.getDbkey()) {
							Rcdsyn_079 rcdsyn_079 = new Rcdsyn_079();
							rcdsyn_079.setDbkey(dbkeyRcdsyn_079b);
							rcdsyn_079.setRsynName_079(row.getString(Rcdsyn_079.RSYN_NAME_079));
							rcdsyn_079.setRsynVer_079(row.getShort(Rcdsyn_079.RSYN_VER_079));
							rcdsyn_079.setSr_036(sr_036);
							sr_036.setRcdsyn_079b(rcdsyn_079);
						}
					}					
				});
				
				// record procedures
				Query recordProcedureListQuery =
					new Query.Builder().forRecordProcedureList(session, srcd_113.getDbkey()).build();
				session.runQuery(recordProcedureListQuery, new IRowProcessor() {
					@Override
					public void processRow(ResultSet row) throws SQLException {
						Srcall_040 srcall_040 = new Srcall_040();
						srcall_040.setCallProc_040(row.getString(Srcall_040.CALL_PROC_040));
						srcall_040.setCallTime_040(row.getString(Srcall_040.CALL_TIME_040));
						srcall_040.setDbpFunc_040(row.getString(Srcall_040.DBP_FUNC_040));
						srcd_113.getSrcall_040s().add(srcall_040);
					}			
				});
				
				// definitions for INDEX-1041 and TABLE-1050 are much more complete in the catalog, 
				// so defer the creation of these records in case we're dealing with IDMSNTWK 
				// version 1 AND the user has indicated to not add the catalog records...
				if (!session.isIdmsntwkVersion1() || !addMissingCatalogComponents || 
					!sam_056.getSrNam_056().equals("INDEX-1041") && 
					!sam_056.getSrNam_056().equals("TABLE-1050")) {

					list.add(srcd_113);
					regularRecords.add(sam_056.getSrNam_056());
				}				
			}			
		});
		
		// catalog records (only if the user wants us to add them)
		if (session.isIdmsntwkVersion1() && addMissingCatalogComponents) {		
			Query catalogRecordListQuery = new Query.Builder().forCatalogRecordList().build();
			session.runQuery(catalogRecordListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					Table_1050 table_1050 = new Table_1050();
					table_1050.setDbkey(JdbcTools.getDbkey(row, Table_1050.ROWID));
					table_1050.setArea_1050(row.getString(Table_1050.AREA_1050));
					table_1050.setLocmode_1050(row.getString(Table_1050.LOCMODE_1050));
					table_1050.setName_1050(row.getString(Table_1050.NAME_1050));
					table_1050.setTableid_1050(row.getShort(Table_1050.TABLEID_1050));
					table_1050s.put(Long.valueOf(table_1050.getDbkey()), table_1050);
					if (!regularRecords.contains(table_1050.getName_1050())) {									
						list.add(table_1050);	
					}
				}				
			});						
		}		
		
		return list;
		
	}

	@Override
	public Collection<?> getRootElementContexts(Object recordContext) {		
		
		if (recordContext instanceof Srcd_113) {
			
			// regular record
			
			// get all elements for once and for all
			Srcd_113 srcd_113 = (Srcd_113) recordContext;
			final Rcdsyn_079 rcdsyn_079 = srcd_113.getRcdsyn_079();
			Query elementListQuery = 
				new Query.Builder().forElementList(session, rcdsyn_079.getDbkey())
								   .withContext(rcdsyn_079.getRsynName_079() + " version " + 
										   		rcdsyn_079.getRsynVer_079())
								   .build();
			session.runQuery(elementListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					
					Namesyn_083 namesyn_083 = new Namesyn_083();	
					namesyn_083.setDbkey(JdbcTools.getDbkey(row, Namesyn_083.ROWID));
					namesyn_083.setDependOn_083(row.getString(Namesyn_083.DEPEND_ON_083));
					namesyn_083.setSynName_083(row.getString(Namesyn_083.SYN_NAME_083));
					namesyn_083.setRcdsyn_079(rcdsyn_079);
					rcdsyn_079.getNamesyn_083s().add(namesyn_083);
					
					Sdr_042 sdr_042 = new Sdr_042();					
					sdr_042.setDbkey(JdbcTools.getDbkey(row, Sdr_042.ROWID));
					sdr_042.setDrLvl_042(row.getShort(Sdr_042.DR_LVL_042));
					sdr_042.setDrNam_042(row.getString(Sdr_042.DR_NAM_042));
					sdr_042.setOcc_042(row.getShort(Sdr_042.OCC_042));
					sdr_042.setUse_042(row.getShort(Sdr_042.USE_042));
					namesyn_083.setSdr_042(sdr_042);
				
				}				
			});
			Query elementCommentListQuery = 
				new Query.Builder().forElementCommentList(session, rcdsyn_079.getDbkey())
							 	   .withContext(rcdsyn_079.getRsynName_079() + " version " + 
							 			   		rcdsyn_079.getRsynVer_079())
							 	   .build();
			session.runQuery(elementCommentListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {				
					Sdr_042 sdr_042 = 
						rcdsyn_079.getNamesyn_083(JdbcTools.getDbkey(row, Sdr_042.ROWID)).getSdr_042();					
					Sdes_044 sdes_044 = new Sdes_044();
					sdes_044.setAsfFieldName_044(row.getString(Sdes_044.ASF_FIELD_NAME_044));
					sdes_044.setCmtId_044(row.getInt(Sdes_044.CMT_ID_044));
					sdes_044.setVal1_044(row.getString(Sdes_044.VAL1_044));
					sdes_044.setVal2_044(row.getString(Sdes_044.VAL2_044));
					sdes_044.setValLgth2_044(row.getShort(Sdes_044.VAL_LGTH2_044));
					sdr_042.getSdes_044s().add(sdes_044);
				}				
			});
			final Rcdsyn_079 rcdsyn_079b = rcdsyn_079.getSr_036().getRcdsyn_079b();
			if (rcdsyn_079b != null) {				
				elementListQuery = 
					new Query.Builder().forElementList(session, rcdsyn_079b.getDbkey())
									   .withContext(rcdsyn_079b.getRsynName_079() + " version " + 
										   			rcdsyn_079b.getRsynVer_079())
									   .build();
				session.runQuery(elementListQuery, new IRowProcessor() {
					@Override
					public void processRow(ResultSet row) throws SQLException {
						
						Namesyn_083 namesyn_083 = new Namesyn_083();	
						namesyn_083.setDbkey(JdbcTools.getDbkey(row, Namesyn_083.ROWID));
						namesyn_083.setDependOn_083(row.getString(Namesyn_083.DEPEND_ON_083));
						namesyn_083.setSynName_083(row.getString(Namesyn_083.SYN_NAME_083));
						namesyn_083.setRcdsyn_079(rcdsyn_079b);
						rcdsyn_079b.getNamesyn_083s().add(namesyn_083);
						
						long dbkeyOfSdr_042 = JdbcTools.getDbkey(row, Sdr_042.ROWID);
						Sdr_042 sdr_042 = rcdsyn_079.getNamesyn_083(dbkeyOfSdr_042).getSdr_042();
						namesyn_083.setSdr_042(sdr_042);
					}				
				});				
			}
			
			// now filter out the root elements
			List<Namesyn_083> list = new ArrayList<>();
			short topLevel = rcdsyn_079.getNamesyn_083s().get(0).getSdr_042().getDrLvl_042();
			for (Namesyn_083 namesyn_083 : rcdsyn_079.getNamesyn_083s()) {
				Sdr_042 sdr_042 = namesyn_083.getSdr_042();
				if (sdr_042.getDrLvl_042() == topLevel) {
					list.add(namesyn_083);
				}
			}			
			
			// we need the information from the base record as well
			
			return list;			
			
		} else if (recordContext instanceof Table_1050) {	
			
			// catalog derived record
			final List<Column_1028> list = new ArrayList<>();
			final Table_1050 table_1050 = (Table_1050) recordContext;
			Query catalogElementListQuery = 
				new Query.Builder().forCatalogElementList(table_1050.getDbkey()).build();
			session.runQuery(catalogElementListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					Column_1028 column_1028 = new Column_1028();
					column_1028.setName_1028(row.getString(Column_1028.NAME_1028));
					column_1028.setNulls_1028(row.getString(Column_1028.NULLS_1028));
					column_1028.setNumber_1028(row.getShort(Column_1028.NUMBER_1028));
					column_1028.setType_1028(row.getString(Column_1028.TYPE_1028));
					column_1028.setVlength_1028(row.getShort(Column_1028.V_LENGTH_1028));
					column_1028.setTable_1050(table_1050);
					table_1050.getColumn_1028s().add(column_1028);
					list.add(column_1028);
				}				
			});
			return list;			
		} else {
			throw new IllegalArgumentException("unknown record context type: " +
											   recordContext.getClass().getName());
		}		
	}	

	@Override
	public Collection<?> getSetContexts() {
		
		final List<Object> list = new ArrayList<>();
		
		// regular sets
		final Map<Long, Long> sor_046ToSrcdMappings = new HashMap<>();
		Query setOwnerListQuery = new Query.Builder().forSetOwnerList(session).build();
		session.runQuery(setOwnerListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				long dbkeyOfSor_046 = JdbcTools.getDbkey(row, Sor_046.ROWID);
				long dbkeyOfSrcd_113 = JdbcTools.getDbkey(row, Srcd_113.ROWID);
				sor_046ToSrcdMappings.put(Long.valueOf(dbkeyOfSor_046), 
										  Long.valueOf(dbkeyOfSrcd_113));
			}
		});				
		final List<String> regularSets = new ArrayList<>();
		final Map<Long, Sor_046> sor_046s = new HashMap<>();
		Query setListQuery = new Query.Builder().forSetList(session).build();
		session.runQuery(setListQuery, new IRowProcessor() {
			@Override
			public void processRow(ResultSet row) throws SQLException {
				long dbkeyOfSor_046 = JdbcTools.getDbkey(row, Sor_046.ROWID);
				Sor_046 sor_046;
				if (!sor_046s.containsKey(Long.valueOf(dbkeyOfSor_046))) {				
					
					Srcd_113 srcd_113 = 
						srcd_113s.get(sor_046ToSrcdMappings.get(Long.valueOf(dbkeyOfSor_046))); 
					
					sor_046 = new Sor_046();
					sor_046.setSrcd_113(srcd_113);
					sor_046.setDbkey(dbkeyOfSor_046);
					sor_046.setIndexDisp_046(row.getShort(Sor_046.INDEX_DISP_046));
					sor_046.setIndexMembers_046(row.getShort(Sor_046.INDEX_MEMBERS_046));
					sor_046.setNxtDbk_046(row.getShort(Sor_046.NXT_DBK_046));
					sor_046.setOrd_046(row.getShort(Sor_046.ORD_046));
					sor_046.setPageCount_046(row.getInt(Sor_046.PAGE_COUNT_046));
					sor_046.setPageCountPercent_046(row.getShort(Sor_046.PAGE_COUNT_PERCENT_046));
					sor_046.setPageOffset_046(row.getInt(Sor_046.PAGE_OFFSET_046));
					sor_046.setPageOffsetPercent_046(row.getShort(Sor_046.PAGE_OFFSET_PERCENT_046));
					sor_046.setPriDbk_046(row.getShort(Sor_046.PRI_DBK_046));
					sor_046.setSaNam_046(row.getString(Sor_046.SA_NAM_046));
					sor_046.setSetMode_046(row.getShort(Sor_046.SET_MODE_046));
					sor_046.setSetNam_046(row.getString(Sor_046.SET_NAM_046));
					sor_046.setSetOrd_046(row.getShort(Sor_046.SET_ORD_046));
					sor_046.setSorId_046(row.getShort(Sor_046.SOR_ID_046));
					sor_046.setSymbolIndex_046(row.getString(Sor_046.SYMBOL_INDEX_046));
					sor_046.setSubarea_046(row.getString(Sor_046.SUBAREA_046));
					sor_046s.put(Long.valueOf(dbkeyOfSor_046), sor_046);
					
				} else {
					sor_046 = sor_046s.get(Long.valueOf(dbkeyOfSor_046));
				}
				
				long dbkeyOfSrcd_113 = JdbcTools.getDbkey(row, Srcd_113.ROWID);
				Srcd_113 srcd_113 = srcd_113s.get(Long.valueOf(dbkeyOfSrcd_113));
				
				Smr_052 smr_052 = new Smr_052();
				smr_052.setDbkey(JdbcTools.getDbkey(row, Smr_052.ROWID));
				smr_052.setSrcd_113(srcd_113);
				smr_052.setDup_052(row.getShort(Smr_052.DUP_052));
				smr_052.setMrCntrl_052(row.getShort(Smr_052.MR_CNTRL_052));
				smr_052.setNxtDbk_052(row.getShort(Smr_052.NXT_DBK_052));
				smr_052.setOwnDbk_052(row.getShort(Smr_052.OWN_DBK_052));
				smr_052.setSetNam_052(row.getString(Smr_052.SET_NAM_052));
				smr_052.setSort_052(row.getShort(Smr_052.SORT_052));
				sor_046.getSmr_052s().add(smr_052);
				
				// defer the creation of "AREA-INDEX", "AREA-TABLE" and "TABLE-INDEX" since they are 
				// stored in the catalog too...
				if (!session.isIdmsntwkVersion1() || !addMissingCatalogComponents ||  
					!sor_046.getSetNam_046().equals("AREA-INDEX") &&
					!sor_046.getSetNam_046().equals("AREA-TABLE") &&
					!sor_046.getSetNam_046().equals("TABLE-INDEX")) {
					
					list.add(sor_046);
					regularSets.add(sor_046.getSetNam_046());
				}
			}
		});
		
		// catalog derived sets (only if the user wants us to add them)
		if (session.isIdmsntwkVersion1() && addMissingCatalogComponents) {
			Query catalogSetListQuery = new Query.Builder().forCatalogSetList().build();
			session.runQuery(catalogSetListQuery, new IRowProcessor() {
				@Override
				public void processRow(ResultSet row) throws SQLException {
					Constraint_1029 constraint_1029 = new Constraint_1029();
					constraint_1029.setName_1029(row.getString(Constraint_1029.NAME_1029));															
					if (!regularSets.contains(constraint_1029.getName_1029())) {
						constraint_1029.setCompress_1029(row.getString(Constraint_1029.COMPRESS_1029));
						constraint_1029.setDisplacement_1029(row.getShort(Constraint_1029.DISPLACEMENT_1029));
						constraint_1029.setIxblkcontains_1029(row.getShort(Constraint_1029.IXBLKCONTAINS_1029));
						constraint_1029.setNext_1029(row.getShort(Constraint_1029.NEXT_1029));
						constraint_1029.setOwner_1029(row.getShort(Constraint_1029.OWNER_1029));
						constraint_1029.setPrior_1029(row.getShort(Constraint_1029.PRIOR_1029));
						long dbkeyOfReferencingTable_1050 = JdbcTools.getDbkey(row, Table_1050.ROWID);
						Table_1050 referencingTable_1050 = 
							table_1050s.get(Long.valueOf(dbkeyOfReferencingTable_1050));
						constraint_1029.setReferencingTable_1050(referencingTable_1050);
						constraint_1029.setRefnext_1029(row.getShort(Constraint_1029.REFNEXT_1029));
						constraint_1029.setRefprior_1029(row.getShort(Constraint_1029.REFPRIOR_1029));
						constraint_1029.setSortorder_1029(row.getString(Constraint_1029.SORTORDER_1029));
						constraint_1029.setType_1029(row.getString(Constraint_1029.TYPE_1029));
						constraint_1029.setUnique_1029(row.getString(Constraint_1029.UNIQUE_1029));
						list.add(constraint_1029);
					}
				}
			});
			
			// system owned indexed sets...
			Query catalogIndexListQuery = new Query.Builder().forCatalogIndexList().build();
			session.runQuery(catalogIndexListQuery, new IRowProcessor() {				
				@Override
				public void processRow(ResultSet row) throws SQLException {
					
					long dbkeyOfTable_1050 = JdbcTools.getDbkey(row, Table_1050.ROWID);
					Table_1050 table_1050 = 
						table_1050s.get(table_1050s.get(Long.valueOf(dbkeyOfTable_1050)));
					
					Index_1041 index_1041 = new Index_1041();
					index_1041.setArea_1041(row.getString(Index_1041.AREA_1041));
					index_1041.setCompress_1041(row.getString(Index_1041.COMPRESS_1041));
					index_1041.setDisplacement_1041(row.getShort(Index_1041.DISPLACEMENT_1041));
					index_1041.setIxblkcontains_1041(row.getShort(Index_1041.IXBLKCONTAINS_1041));
					index_1041.setName_1041(row.getString(Index_1041.NAME_1041));
					index_1041.setUnique_1041(row.getString(Index_1041.UNIQUE_1041));					
					index_1041.setTable_1050(table_1050);
					if (!regularSets.contains(index_1041.getName_1041())) {
						list.add(index_1041);
					}					
				}
			});			
		}		
		
		return list;
	}	

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T elementContext) {
		
		if (elementContext instanceof Namesyn_083) {
			
			// regular dictionary element
			
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
					sdr_042 = namesyn_083.getSdr_042();
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
			
		} else if (elementContext instanceof Column_1028) {
			// no element hierarchies for catalog derived records
			return Collections.emptyList();
		} else {
			throw new IllegalArgumentException("unknown element context type: " +
											   elementContext.getClass().getName());
		}
		
	}

	@Override
	public void init(IDataEntryContext context, Properties parameters,
					 IDataCollectorRegistry dataCollectorRegistry) {
		
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
			schemaVersion = 
				((Short) context.getAttribute(IDataEntryContext.SCHEMA_VERSION)).shortValue();					
		}
		
		if (context.containsAttribute(ContextAttributeKeys.ADD_MISSING_CATALOG_COMPONENTS)) {
			Boolean b = context.getAttribute(ContextAttributeKeys.ADD_MISSING_CATALOG_COMPONENTS);
			addMissingCatalogComponents = b.booleanValue();
		}
		
		session = new SchemaImportSession(dictionary, schemaName, schemaVersion);
		session.open();
		
		ISchemaDataCollector schemaDataCollector = new SchemaDataCollector(session);
		dataCollectorRegistry.registerSchemaDataCollector(schemaDataCollector);
		
		IAreaDataCollector<Sa_018> dictionaryAreaDataCollector = 
			new DictionaryAreaDataCollector(session);
		dataCollectorRegistry.registerAreaDataCollector(Sa_018.class, dictionaryAreaDataCollector);
		
		IRecordDataCollector<Srcd_113> dictionaryRecordDataCollector = 
			new DictionaryRecordDataCollector(session);
		dataCollectorRegistry.registerRecordDataCollector(Srcd_113.class, 
														  dictionaryRecordDataCollector);
		
		IElementDataCollector<Namesyn_083> dictionaryElementDataCollector = 
			new DictionaryElementDataCollector(session);	
		dataCollectorRegistry.registerElementDataCollector(Namesyn_083.class, 
														   dictionaryElementDataCollector);
		
		ISetDataCollector<Sor_046> dictionarySetDataCollector = new DictionarySetDataCollector(session);	
		dataCollectorRegistry.registerSetDataCollector(Sor_046.class, dictionarySetDataCollector);
		
		if (addMissingCatalogComponents) {
			
			IRecordDataCollector<Table_1050> catalogRecordDataCollector = 
				new CatalogRecordDataCollector(session);
			dataCollectorRegistry.registerRecordDataCollector(Table_1050.class, 
															   catalogRecordDataCollector);
			
			IElementDataCollector<Column_1028> catalogElementDataCollector = 
				new CatalogElementDataCollector(session);
			dataCollectorRegistry.registerElementDataCollector(Column_1028.class, 
															   catalogElementDataCollector);
			
			ISetDataCollector<Constraint_1029> catalogUserOwnedSetDataCollector = 
				new CatalogUserOwnedSetDataCollector(session);
			dataCollectorRegistry.registerSetDataCollector(Constraint_1029.class, 
														   catalogUserOwnedSetDataCollector);
			
			ISetDataCollector<Index_1041> catalogSystemOwnedIndexDataCollector = 
				new CatalogSystemOwnedIndexDataCollector(session);
			dataCollectorRegistry.registerSetDataCollector(Index_1041.class, 
														   catalogSystemOwnedIndexDataCollector);
			
		}
		
	}

}
