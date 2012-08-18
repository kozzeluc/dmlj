package org.lh.dmlj.schema.editor.tool;

import static dmlj.core.NavigationType.FIRST;
import static dmlj.core.NavigationType.NEXT;
import static dmlj.core.NavigationType.OWNER;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.draw2d.geometry.Rectangle;
import org.lh.dmlj.idmsntwk.CalcKey_S_010;
import org.lh.dmlj.idmsntwk.CalcKey_Schema_1045;
import org.lh.dmlj.idmsntwk.Column_1028;
import org.lh.dmlj.idmsntwk.Constkey_1030;
import org.lh.dmlj.idmsntwk.Constraint_1029;
import org.lh.dmlj.idmsntwk.Index_1041;
import org.lh.dmlj.idmsntwk.Indexkey_1042;
import org.lh.dmlj.idmsntwk.Orderkey_1044;
import org.lh.dmlj.idmsntwk.Rcdsyn_079;
import org.lh.dmlj.idmsntwk.S_010;
import org.lh.dmlj.idmsntwk.Sa_018;
import org.lh.dmlj.idmsntwk.Sacall_020;
import org.lh.dmlj.idmsntwk.Sam_056;
import org.lh.dmlj.idmsntwk.Schema_1045;
import org.lh.dmlj.idmsntwk.Scr_054;
import org.lh.dmlj.idmsntwk.Smr_052;
import org.lh.dmlj.idmsntwk.Sor_046;
import org.lh.dmlj.idmsntwk.SortKey_Column_1028_TABLE_COLNUM;
import org.lh.dmlj.idmsntwk.Srcall_040;
import org.lh.dmlj.idmsntwk.Srcd_113;
import org.lh.dmlj.idmsntwk.Table_1050;
import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallVerb;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.RulerType;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.StorageMode;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.ViaSpecification;

import dmlj.core.IDatabase;
import dmlj.core.filter.RecordIdFilter;


public class SchemaImportTool {
	
	// DUP-052 values:
	private static final int DUPLICATES_NOT_ALLOWED = 0;
	private static final int DUPLICATES_FIRST = 1;
	private static final int DUPLICATES_LAST = 2;
	private static final int DUPLICATES_BY_DBKEY = 4;
	
	// MR-CNTRL-052 values:
	private static final SetMembershipOption[] MEMBERSHIP_OPTION = 
		{SetMembershipOption.MANDATORY_AUTOMATIC,	// 0
		 SetMembershipOption.MANDATORY_MANUAL,		// 1
	     SetMembershipOption.OPTIONAL_AUTOMATIC,	// 2
	     SetMembershipOption.OPTIONAL_MANUAL};		// 3
	
	// MODE-113 values:
	private static final int MODE_CALC = 1;
	private static final int MODE_VIA = 0;
	
	// SET-MODE-046 values:
	private static final short[] MODE_CHAINED = {13, 15};
	private static final short   MODE_INDEXED = 21;
	
	// SORT-052 values:
	private static final int SORT_SEQUENCE_NATURAL = 1;
	@SuppressWarnings("unused")
	private static final int SORT_SEQUENCE_STANDARD = 0;
		
	private static final int OPTION_COMPLETE_OOAK_012 = 0;
	private static final int OPTION_COMPLETE_LOOAK_155 = 1;
	private static final int OPTION_COMPLETE_CATALOG = 2;	
	
	// SET-ORD-046 values:
	private static final SetOrder[][] ORDER = 
		new SetOrder[][] {{SetOrder.LAST, SetOrder.PRIOR}, 
						  {SetOrder.SORTED, SetOrder.SORTED}, 
						  {SetOrder.FIRST, SetOrder.NEXT}};
	
	// SCR-POS-054 special value:
	private static final int SORTED_BY_DBKEY = -4;
	
	// SORT-054 values:
	private static final SortSequence[] SORT_SEQUENCE = 
		{SortSequence.ASCENDING,		// 0 
		 SortSequence.DESCENDING};		// 1
	
	// schema diagram related things:
	private static final int X_INITIAL_VALUE = 50;	
	private static final int X_HALF_INCREMENT = 130;	
	private static final int X_INCREMENT = 2 * X_HALF_INCREMENT;	
	private static final int Y_INITIAL_VALUE = 100;		
	private static final int Y_INCREMENT = 135;
	
	private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";	
	
	private IDatabase catalog;
	private IDatabase dictionary;
	private boolean[] options;    
	private String 	  schemaName;
    private int 	  schemaVersion;  
    
    private Map<String, List<SchemaRecord>> viaSetMembers = new HashMap<>();
    
    public SchemaImportTool(IDatabase dictionary, String schemaName, 
			 				int schemaVersion, boolean[] options, 
			 				IDatabase catalog) {
    	super();
    	this.dictionary = dictionary;
    	this.schemaName = schemaName;
		this.schemaVersion = schemaVersion;
		this.options = options;
		this.catalog = catalog;
    }
    
	private void addRecordToArea(SchemaRecord record, Srcd_113 srcd_113, 
								 Sam_056 sam_056, Sor_046 sor_046,
								 boolean[] option, 
								 SchemaArea targetArea) {		
		// assumptions: 
		// - the record is connected to its schema
		// - the record is not a system record
		SchemaArea area;
		if (targetArea != null) {
			area = targetArea;
		} else {
			area = record.getSchema().getArea(sam_056.getSaNam_056());
		}
		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		record.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		//
		if (isIdmsntwk(record.getSchema()) && 
			(option[OPTION_COMPLETE_OOAK_012] && 
			 sam_056.getSrNam_056().equals("OOAK-012")) ||
			 option[OPTION_COMPLETE_LOOAK_155] &&
			 sam_056.getSrNam_056().equals("LOOAK-155")) {

			OffsetExpression offsetExpression = 
				SchemaFactory.eINSTANCE.createOffsetExpression();
			areaSpecification.setOffsetExpression(offsetExpression);
			offsetExpression.setOffsetPageCount(1);
			offsetExpression.setPageCount(1);
		} else {		
			short pageOffsetPercent = srcd_113.getPageOffsetPercent_113();
			int pageOffset = srcd_113.getPageOffset_113();
			short pageCountPercent = srcd_113.getPageCountPercent_113();
			int pageCount = srcd_113.getPageCount_113();
			String subarea = srcd_113.getSubarea_113();				
			if ((pageOffsetPercent != -1 || pageOffset != -1) &&
				!(pageOffsetPercent == 0 && pageCountPercent == 100)) {
		
				OffsetExpression offsetExpression = 
					SchemaFactory.eINSTANCE.createOffsetExpression();
				areaSpecification.setOffsetExpression(offsetExpression);
				if (pageOffsetPercent != -1) {
					offsetExpression.setOffsetPercent(pageOffsetPercent);
				} else {
					offsetExpression.setOffsetPageCount(pageOffset);
				}			
				if (pageCountPercent != -1) {
					offsetExpression.setPercent(pageCountPercent);				
				} else {
					offsetExpression.setPageCount(pageCount);
				}						
			} else if (!subarea.equals("")) {
				areaSpecification.setSymbolicSubareaName(subarea);			
			}
		}
	}
	
	private void addRecordToArea(SchemaRecord record, Table_1050 table_1050) {		
		SchemaArea area = record.getSchema().getArea(table_1050.getArea_1050());
		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		record.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);		
	}	
	
	private void addSystemOwnerToArea(Schema schema, SystemOwner systemOwner,  
									  Sam_056 sam_056, Sor_046 sor_046) {	
		
		SchemaArea area = schema.getArea(sam_056.getSaNam_056());
		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		systemOwner.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		//
		short pageOffsetPercent = sor_046.getPageOffsetPercent_046();
		int pageOffset = sor_046.getPageOffset_046();
		short pageCountPercent = sor_046.getPageCountPercent_046();
		int pageCount = sor_046.getPageCount_046();
		String subarea = sor_046.getSubarea_046();				
		//
		if ((pageOffsetPercent != -1 || pageOffset != -1) &&
			!(pageOffsetPercent == 0 && pageCountPercent == 100)) {

			OffsetExpression offsetExpression = 
				SchemaFactory.eINSTANCE.createOffsetExpression();
			areaSpecification.setOffsetExpression(offsetExpression);
			if (pageOffsetPercent != -1) {
				offsetExpression.setOffsetPercent(pageOffsetPercent);
			} else {
				offsetExpression.setOffsetPageCount(pageOffset);
			}			
			if (pageCountPercent != -1) {
				offsetExpression.setPercent(pageCountPercent);				
			} else {
				offsetExpression.setPageCount(pageCount);
			}						
		} else if (!subarea.equals("")) {
			areaSpecification.setSymbolicSubareaName(subarea);			
		}		
	}

	private boolean containsOccursDependingOnField(FieldExtractorForDMLJ fieldExtractor,
												   IDatabase dictionary,
			  									   Srcd_113 srcd_113) {
		boolean b = false;
		for (Iterator<FieldForDMLJ> it = fieldExtractor.createFieldIterator(); 
			 !b && it.hasNext(); ) {

			FieldForDMLJ field = it.next();
			if (field.getDependsOnField() != null) {
				b = true;
			}
		}
		return b;
	}		
	
	private short getAdjustedDbkeyPosition(SchemaRecord record,
										   short dbkeyPosition) {
		if (dbkeyPosition == -1) {
			return dbkeyPosition;
		} else if (record.getLocationMode() == LocationMode.CALC) {
			return (short)(dbkeyPosition - 1);
		} else {
			return (short)(dbkeyPosition + 1);
		}
	}
	
	private SetMembershipOption getMembershipOption(IDatabase catalog, 
													Constraint_1029 constraint_1029) {
		// Unless at least 1 of the constraint key columns is nullable in the
		// referencing table, the membership option is considered to be
		// MANDATORY AUTOMATIC.  In the other case, make it OPTIONAL AUTOMATIC.
		// The only 2 sets/constraints to which this really applies are
		// AREA-TABLE and AREA-INDEX.
		Table_1050 table_1050 = 
			catalog.find(constraint_1029, "REFERENCING-TABLE", OWNER);
		boolean nullableColumnEncountered = false;		
		for (Constkey_1030 constkey_1030 :
			 catalog.<Constkey_1030>walk(constraint_1029, "CONSTRAINT-KEY", NEXT)) {

			short columnNumber = constkey_1030.getNumber_1030();
			Column_1028 column_1028 = 
				catalog.find(Column_1028.class, table_1050, "TABLE-COLNUM", 
						     new SortKey_Column_1028_TABLE_COLNUM(columnNumber));
			if (column_1028 == null) {
				throw new RuntimeException("logic error: column not found (" +
										   table_1050.getName_1050() + "/" + 
										   columnNumber + ")");
			}			
			if (column_1028.getNulls_1028().equals("Y")) {
				nullableColumnEncountered = true;
			}
		}
		if (nullableColumnEncountered) {
			return SetMembershipOption.OPTIONAL_AUTOMATIC;
		} else {
			return SetMembershipOption.MANDATORY_AUTOMATIC;
		}
	}
	
	private StorageMode getStorageMode(FieldExtractorForDMLJ fieldExtractor,
									   IDatabase dictionary, Srcd_113 srcd_113) {
		StringBuilder p = new StringBuilder();
		if (!containsOccursDependingOnField(fieldExtractor, dictionary, 
											srcd_113)) {
			p.append("FIXED");
		} else {
			p.append("VARIABLE");
		}
		if (isCompressedRecord(dictionary, srcd_113)) {
			p.append(" COMPRESSED");
		}
		String q = p.toString(); 
		if (q.equals("FIXED")) {
			return StorageMode.FIXED;
		} else if (q.equals("FIXED COMPRESSED")) {
			return StorageMode.FIXED_COMPRESSED;
		} else if (q.equals("VARIABLE")) {
			return StorageMode.VARIABLE;
		} else {
			return StorageMode.VARIABLE_COMPRESSED;
		}
	}	
	
	private boolean isCompressedRecord(IDatabase dictionary, Srcd_113 srcd_113) {
		boolean b = false;
		for (Srcall_040 srcall_040 : 
			 dictionary.<Srcall_040>walk(srcd_113, "SRCD-SRCALL", NEXT)) {

			if (srcall_040.getCallProc_040().equals("IDMSCOMP")) {
				b = true;
			}
		}
		return b;
	}	
	
	private boolean isIdmsntwk(Schema schema) {
		return schema.getName().equals("IDMSNTWK") && 
			   schema.getVersion() == 1;
	}
	
	/**
	 * Creates an extended schema.<br><br>
	 * The dictionary and catalog passed will not be closed by this method.
	 * @param dictionary the dictionary containing the CA IDMS/DB schema for 
	 *        which an extended schema is to be created 
	 * @param schemaName the name of the CA IDMS/DB schema
	 * @param schemaVersion the version of the CA IDMS/DB schema
	 * @param options the processing options for schema IDMSNTWK version 1 :<br>
	 *        [0] add missing offset-expression for record OOAK-012<br>
	 *        [1] add missing offset-expression for record LOOAK-155<br>
	 *        [2] add missing catalog records and sets<br>
	 * @param catalog the catalog to extract catalog records and sets (only 
	 *        required for schema IDMSNTWK version 1 when options[2] is set to 
	 *        true)
	 * @return the extended schema
	 */
	public Schema run() {	
		 	
		// check the mandatory arguments...
		if (dictionary == null) {
			throw new IllegalArgumentException("dictionary is null");
		}
		if (schemaName == null) {
			throw new IllegalArgumentException("schemaName is null");
		}
		
		// get the S-010...
		Object calcKey = new CalcKey_S_010(schemaName);
		S_010 s_010 = dictionary.find(S_010.class, calcKey);		
		while (s_010 != null && s_010.getSSer_010() != schemaVersion) {
			s_010 = dictionary.find(s_010);
		}
		if (s_010 == null) {
			String message = "schema " + schemaName + " version " + 
			   		   		 schemaVersion + " could NOT be found";
			throw new IllegalArgumentException(message);
		}
		
		// make sure that the options array is passed with the correct length
		// in the case of schema IDMSNTWK version 1...
		if (schemaName.equals("IDMSNTWK") && schemaVersion == 1 &&
			(options == null || options.length != 3)) {
			
			String message = "options is null or length != 3";
			throw new IllegalArgumentException(message);
		}
		
		// make sure a catalog is passed when the schema is IDMSNTWK version 1
		// and the missing catalog records and sets are to be added...
		if (schemaName.equals("IDMSNTWK") && schemaVersion == 1 &&
			options[2] && catalog == null) {
				
			String message = "catalog is null";
			throw new IllegalArgumentException(message);
		}
		
		// create the schema and return it...
		return processSchema(dictionary, s_010, options, catalog);
		 			
	}

	private SchemaArea processArea(Schema schema, IDatabase dictionary, 
								   Sa_018 sa_018, boolean[] option) {		
		
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(sa_018.getSaNam_018());												
		schema.getAreas().add(area);
		//
		if (dictionary.isConnected(sa_018, "SA-SACALL")) {			
			for (Sacall_020 sacall_020 : 
				 dictionary.<Sacall_020>walk(sa_018, "SA-SACALL", NEXT)) {
				
				String procedureName = sacall_020.getCallProc_020();
				Procedure procedure = schema.getProcedure(procedureName);
				if (procedure == null) {
					procedure = SchemaFactory.eINSTANCE.createProcedure();
					procedure.setName(procedureName);	
					schema.getProcedures().add(procedure);
				}
				AreaProcedureCallSpecification callSpec =
					SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();			
				area.getProcedures().add(callSpec);
				callSpec.setProcedure(procedure);
				if (sacall_020.getCallTime_020().equals("00")) {
					callSpec.setCallTime(ProcedureCallTime.BEFORE);
				} else if (sacall_020.getCallTime_020().equals("01")) {
					callSpec.setCallTime(ProcedureCallTime.ON_ERROR_DURING);
				} else if (sacall_020.getCallTime_020().equals("02")) {
					callSpec.setCallTime(ProcedureCallTime.AFTER);
				}					
				String func =
					sacall_020.getDbpFunc_020();   // READY/FINISH/COMMIT/ROLLBACK				
				String mode =
					sacall_020.getDbpMode_020();   // UPDATE/RETRIEVAL
				String access =
					sacall_020.getDbpAccess_020(); // EXCLUSIVE/PROTECTED/SHARED
				StringBuilder trigger = 
					new StringBuilder(func.toLowerCase());
				if (func.equalsIgnoreCase("READY")) {
					if (!access.equals("")) {
						trigger.append("_");
						trigger.append(access.toUpperCase());
					}
					if (!mode.equals("")) {
						trigger.append("_");
						trigger.append(mode.toUpperCase());
					}
				}
				AreaProcedureCallFunction function;
				if (trigger.length() > 0) {
					function = 
						AreaProcedureCallFunction.valueOf(trigger.toString());					
				} else {
					// null doesn't work here and hence the EVERY_DML_FUNCTION
					// function was created					
					function = AreaProcedureCallFunction.EVERY_DML_FUNCTION;
				}
				callSpec.setFunction(function);
			}			
		}
		return area;
	}
	
	private SchemaArea processCatalogArea(Schema schema, String areaName) {
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(areaName);												
		schema.getAreas().add(area);
		//
		return area;
	}
	
	private void processCatalogChainedSet(Schema schema, IDatabase catalog,
										  Constraint_1029 constraint_1029) {

		String setName = constraint_1029.getName_1029();

		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		set.setMode(SetMode.CHAINED);
		if (constraint_1029.getSortorder_1029().equals("")) {
			set.setOrder(SetOrder.LAST);
		} else {
			set.setOrder(SetOrder.SORTED);
		}

		OwnerRole ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);

		Table_1050 table_1050 = 
			catalog.find(constraint_1029, "REFERENCED-TABLE", OWNER);
		String recordName = table_1050.getName_1050().replaceAll("_", "-") + 
							"-" + table_1050.getTableid_1050();
		SchemaRecord owner = schema.getRecord(recordName);
		owner.getOwnerRoles().add(ownerRole);

		short nextDbkeyPosition =
			getAdjustedDbkeyPosition(owner, (short)(constraint_1029.getRefnext_1029() / 4));
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition);
		short priorDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, (short)(constraint_1029.getRefprior_1029() / 4));
		ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);		
				
		processCatalogChainedSetMember(set, catalog, constraint_1029);
	}
	
	private void processCatalogChainedSetMember(Set set, IDatabase catalog,
												Constraint_1029 constraint_1029) {
		// assumption: the set is connected to its schema
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		
		// create the Connection for the MemberRole; a Connection represents a
		// line in the diagram...
		ConnectionPart connectionPart = 
			SchemaFactory.eINSTANCE.createConnectionPart();		
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.getSet()
				  .getSchema()
				  .getDiagramData()
				  .getConnectionParts()
				  .add(connectionPart);

		Table_1050 table_1050 = 
			catalog.find(constraint_1029, "REFERENCING-TABLE", OWNER);
		String recordName = table_1050.getName_1050().replaceAll("_", "-") +
							"-" + table_1050.getTableid_1050();
		SchemaRecord member = set.getSchema().getRecord(recordName);
		member.getMemberRoles().add(memberRole);

		ViaSpecification viaSpecification = member.getViaSpecification();
		if (viaSpecification != null &&
			viaSetMembers.containsKey(set.getName()) &&
			viaSetMembers.get(set.getName()).contains(member)) {			

			// the member record is stored via the set
			viaSpecification.setSet(set);
		}

		short nextDbkeyPosition = 
			getAdjustedDbkeyPosition(member, (short)(constraint_1029.getNext_1029() / 4));
		memberRole.setNextDbkeyPosition(nextDbkeyPosition);

		short priorDbkeyPosition = 
			getAdjustedDbkeyPosition(member, (short)(constraint_1029.getPrior_1029() / 4));		
		memberRole.setPriorDbkeyPosition(priorDbkeyPosition);		

		short ownerDbkeyPosition = 
			getAdjustedDbkeyPosition(member, (short)(constraint_1029.getOwner_1029() / 4));
		memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);		

		SetMembershipOption membershipOption = 
			getMembershipOption(catalog, constraint_1029);
		memberRole.setMembershipOption(membershipOption);

		if (set.getOrder() == SetOrder.SORTED) {
			processControlKeys(memberRole, catalog, constraint_1029);
		}
	}	
	
	private Set processCatalogIndex(Schema schema, IDatabase catalog, 
									Index_1041 index_1041) {
		
		String setName = index_1041.getName_1041();		

		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		set.setMode(SetMode.INDEXED);
		set.setOrder(SetOrder.SORTED); // where can we derive this from ?

		SystemOwner systemOwner = 
			SchemaFactory.eINSTANCE.createSystemOwner();
		set.setSystemOwner(systemOwner);		
		
		IndexedSetModeSpecification indexedSetModeSpec =
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();		
		set.setIndexedSetModeSpecification(indexedSetModeSpec);
		indexedSetModeSpec.setKeyCount(index_1041.getIxblkcontains_1041());
		short displacement = index_1041.getDisplacement_1041();		
		indexedSetModeSpec.setDisplacementPageCount(displacement);
		
		SchemaArea area = schema.getArea(index_1041.getArea_1041());
		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		systemOwner.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);		
				
		processCatalogIndexMember(set, catalog, index_1041);
		
		//setDiagramData(systemOwner);
		
		return set;
	}
	
	private void processCatalogIndexedSetMember(Set set, IDatabase catalog,
												Constraint_1029 constraint_1029) {

		// assumption: the set is connected to its schema
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		
		// create the Connection for the MemberRole; a Connection represents a
		// line in the diagram...
		ConnectionPart connectionPart = 
			SchemaFactory.eINSTANCE.createConnectionPart();		
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.getSet().getSchema()
		  				   .getDiagramData()
		  				   .getConnectionParts()
		  				   .add(connectionPart);

		Table_1050 table_1050 = 
			catalog.find(constraint_1029, "REFERENCING-TABLE", OWNER);

		String recordName = 
			table_1050.getName_1050().replaceAll("_", "-") + "-" + 
			table_1050.getTableid_1050();
		SchemaRecord member = set.getSchema().getRecord(recordName);
		member.getMemberRoles().add(memberRole);

		ViaSpecification viaSpecification = member.getViaSpecification();
		if (viaSpecification != null && 
			viaSetMembers.containsKey(set.getName()) &&
			viaSetMembers.get(set.getName()).contains(member)) {

			// the member record is stored via the set
			viaSpecification.setSet(set);
		}
		
		short indexDbkeyPosition = 
			getAdjustedDbkeyPosition(member, (short)(constraint_1029.getNext_1029() / 4)); 		
		memberRole.setIndexDbkeyPosition(indexDbkeyPosition); 		
		
		if (constraint_1029.getOwner_1029() != -1) {
			short ownerDbkeyPosition = 
				getAdjustedDbkeyPosition(member, (short)(constraint_1029.getOwner_1029() / 4)); 		
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition); 
		}		

		SetMembershipOption membershipOption = 
			getMembershipOption(catalog, constraint_1029);
		memberRole.setMembershipOption(membershipOption);

		if (set.getOrder() == SetOrder.SORTED) {
			processControlKeys(memberRole, catalog, constraint_1029);
		}
	}
	
	private void processCatalogIndexMember(Set set, IDatabase catalog,
										   Index_1041 index_1041) {
	
		// assumption: the set is connected to its schema
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		
		// create the Connection for the MemberRole; a Connection represents a
		// line in the diagram...
		ConnectionPart connectionPart = 
			SchemaFactory.eINSTANCE.createConnectionPart();		
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectionParts()
		  		  .add(connectionPart);
		
		Table_1050 table_1050 = catalog.find(index_1041, "TABLE-INDEX", OWNER);
		
		String recordName = table_1050.getName_1050().replaceAll("_", "-") +
							"-" + table_1050.getTableid_1050();
		SchemaRecord member = set.getSchema().getRecord(recordName);
		member.getMemberRoles().add(memberRole);
	
		ViaSpecification viaSpecification = member.getViaSpecification();
		if (viaSpecification != null && 
			viaSetMembers.containsKey(set.getName()) &&
			viaSetMembers.get(set.getName()).contains(member)) {
	
			viaSpecification.setSet(set);
		}
	
		memberRole.setMembershipOption(SetMembershipOption.MANDATORY_AUTOMATIC);
	
		if (set.getOrder() == SetOrder.SORTED) {
			processControlKeys(memberRole, catalog, index_1041);
		}
	}

	private SchemaRecord processCatalogRecord(Schema schema, IDatabase catalog, 
											  Table_1050 table_1050) {		
		
		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		schema.getRecords().add(record);

		record.setName(table_1050.getName_1050().replaceAll("_", "-") + "-" +
					   table_1050.getTableid_1050());		
		record.setId(table_1050.getTableid_1050());

		for (Column_1028 column_1028 :
			 catalog.<Column_1028>walk(table_1050, "TABLE-COLNUM", NEXT)) {
		
			Element element = SchemaFactory.eINSTANCE.createElement();
			record.getElements().add(element);		
			record.getRootElements().add(element);		
			StringBuilder elementName = 
				new StringBuilder(column_1028.getName_1028()
										     .replaceAll("_", "-"));
			if (!elementName.toString().equals("FILLER")) {
				elementName.append("-");
				elementName.append(String.valueOf(table_1050.getTableid_1050()));
			}
			element.setName(elementName.toString());
			element.setLevel((short)2); // no hierarchical element structure
			//element.setOffset(column_1028.getVoffset_1028()); // derived
			//element.setLength(column_1028.getVlength_1028()); // derived
			if (column_1028.getNulls_1028().equals("Y")) {
				element.setNullable(true);
			}
			String type = column_1028.getType_1028();			
			if (type.equals("CHARACTER")) {
				element.setPicture("X(" + column_1028.getVlength_1028() + ")");
				element.setUsage(Usage.DISPLAY);
			} else if (type.equals("INTEGER")) {
				element.setPicture("S9(8) SYNC");
				element.setUsage(Usage.COMPUTATIONAL);
			} else if (type.equals("REAL")) {
				element.setPicture("S9(8) SYNC");
				element.setUsage(Usage.COMPUTATIONAL);
			} else if (type.equals("SMALLINT")) {
				element.setPicture("S9(4) SYNC");
				element.setUsage(Usage.COMPUTATIONAL);
			} else if (type.equals("TIMESTAMP")) {
				element.setPicture("X(8)");
				// should we make this kind of element COMPUTATIONAL ?				
			} else if (type.equals("BINARY")) {
				if (element.getName().equals("FILLER")) {
					element.setPicture("X(" + column_1028.getVlength_1028() + 
									   ")");					
				} else {
					String picture = 
						"X(" + (column_1028.getVlength_1028() * 8) + ")";
					element.setPicture(picture);					
					element.setUsage(Usage.BIT);					
				}				
			}
		}		

		if (table_1050.getLocmode_1050().equals("C")) {
			record.setLocationMode(LocationMode.VIA);
		} else if (table_1050.getLocmode_1050().equals("D")) {
			record.setLocationMode(LocationMode.DIRECT);
		} else if (table_1050.getLocmode_1050().equals("H") || 
				   table_1050.getLocmode_1050().equals("U")) {
			record.setLocationMode(LocationMode.CALC);
		} else {
			throw new RuntimeException("unsupported location mode for table: " +
									   table_1050.getLocmode_1050());
		}
				
		record.setStorageMode(StorageMode.FIXED);		

		if (record.getLocationMode() == LocationMode.VIA) {
			ViaSpecification viaSpecification = 
				SchemaFactory.eINSTANCE.createViaSpecification();
						
			boolean viaSetStored = false;
			for (Constraint_1029 constraint_1029 :
				 catalog.<Constraint_1029>walk(table_1050, "REFERENCING-TABLE", 
						 					   NEXT)) {
				
				if (constraint_1029.getCluster_1029().equals("Y")) {
					String setName = constraint_1029.getName_1029();
					List<SchemaRecord> viaRecordsForSet;
					if (viaSetMembers.containsKey(setName)) {
						viaRecordsForSet = viaSetMembers.get(setName);
					} else {
						viaRecordsForSet = new ArrayList<>();
						viaSetMembers.put(setName, viaRecordsForSet);
					}
					viaRecordsForSet.add(record);
					viaSetStored = true;
				}
			}
			if (!viaSetStored) {
				throw new RuntimeException("could not derive via set for " +
										   "table: " + 
										   table_1050.getName_1050());
			}
									
			record.setViaSpecification(viaSpecification);
			
		} else if (record.getLocationMode() == LocationMode.CALC) {
			// the definition of the CALC key is maintained through an index
			// named "HASH", so we need that INDEX-1041 first...
			Index_1041 index_1041 = null;
			for (Index_1041 anIndex_1041 :
				 catalog.<Index_1041>walk(table_1050, "TABLE-INDEX", NEXT)) {
				
				if (anIndex_1041.getName_1041().equals("HASH")) {
					index_1041 = anIndex_1041;
				}
			}
			if (index_1041 == null) {
				throw new RuntimeException("could not find HASH index for " +
						   				   "table: " + 
						   				   table_1050.getName_1050());
			}
			// now create the CALC key...
			processControlKeys(record, catalog, index_1041);
		}

		addRecordToArea(record, table_1050);		

		return record;
	}	
	
	private void processCatalogUserOwnedIndexedSet(Schema schema, 
												   IDatabase catalog, 
												   Constraint_1029 constraint_1029) {

		String setName = constraint_1029.getName_1029();

		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		set.setMode(SetMode.INDEXED);
		if (constraint_1029.getSortorder_1029().equals("")) {
			set.setOrder(SetOrder.LAST);
		} else {
			set.setOrder(SetOrder.SORTED);
		}

		OwnerRole ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);

		Table_1050 table_1050 = 
			catalog.find(constraint_1029, "REFERENCED-TABLE", OWNER);
		String recordName = table_1050.getName_1050().replaceAll("_", "-") + 
							"-" + table_1050.getTableid_1050();
		SchemaRecord owner = schema.getRecord(recordName);
		owner.getOwnerRoles().add(ownerRole);

		short nextDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, (short)(constraint_1029.getRefnext_1029() / 4 ));
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition);
		short priorDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, (short)(constraint_1029.getRefprior_1029() / 4));
		ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);

		IndexedSetModeSpecification indexedSetModeSpec = 
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();
		set.setIndexedSetModeSpecification(indexedSetModeSpec);
		indexedSetModeSpec.setKeyCount(constraint_1029.getIxblkcontains_1029());
		short displacement = constraint_1029.getDisplacement_1029();		
		indexedSetModeSpec.setDisplacementPageCount(displacement);

		processCatalogIndexedSetMember(set, catalog, constraint_1029);		
	}
	
	private void processChainedSet(Schema schema, IDatabase dictionary,
								   Sor_046 sor_046, boolean[] option,
								   String suffix) {		
		
		String setName;
		if (suffix == null) {
			setName = sor_046.getSetNam_046();
		} else {
			setName = sor_046.getSetNam_046() + suffix;
		}

		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		set.setMode(SetMode.CHAINED);
		set.setOrder(ORDER[sor_046.getSetOrd_046()][sor_046.getOrd_046()]);		

		OwnerRole ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);
		
		Srcd_113 srcd_113 = dictionary.find(sor_046, "SRCD-SOR", OWNER);
		Sam_056 sam_056 = dictionary.find(srcd_113, "SRCD-SAM", FIRST);	
		String recordName;
		if (suffix == null) {
			recordName = sam_056.getSrNam_056(); 
		} else {
			recordName = sam_056.getSrNam_056() + suffix;
		}
		SchemaRecord owner = schema.getRecord(recordName);
		owner.getOwnerRoles().add(ownerRole);
		
		short nextDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, sor_046.getNxtDbk_046());
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition);
		short priorDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, sor_046.getPriDbk_046());
		if (priorDbkeyPosition != -1) {
			ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);			
		}			

		for (Smr_052 smr_052 : 
			 dictionary.<Smr_052>walk(sor_046, "SOR-SMR", NEXT)) {

			processChainedSetMember(set, dictionary, smr_052, suffix);
		}		
	}	
	
	private void processChainedSetMember(Set set, IDatabase dictionary,
									     Smr_052 smr_052, String suffix) {
		// - the set is connected to its schema
		Srcd_113 srcd_113 = dictionary.find(smr_052, "SRCD-SMR", OWNER);
		Sam_056 sam_056 = dictionary.find(srcd_113, "SRCD-SAM", FIRST);
		processChainedSetMember(set, dictionary, smr_052, sam_056, suffix);
	}

	private void processChainedSetMember(Set set, IDatabase dictionary, 
										 Smr_052 smr_052, Sam_056 sam_056,
										 String suffix) {
		// assumption: the set is connected to its schema		
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		
		// create the Connection for the MemberRole; a Connection represents a
		// line in the diagram...
		ConnectionPart connectionPart = 
			SchemaFactory.eINSTANCE.createConnectionPart();		
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectionParts()
		  		  .add(connectionPart);
		
		String recordName;
		if (suffix == null) {
			recordName = sam_056.getSrNam_056();
		} else {
			recordName = sam_056.getSrNam_056() + suffix;
		}
		SchemaRecord member = set.getSchema().getRecord(recordName);
		member.getMemberRoles().add(memberRole);
		
		ViaSpecification viaSpecification = member.getViaSpecification();
		if (viaSpecification != null && 
			viaSetMembers.containsKey(set.getName()) &&
			viaSetMembers.get(set.getName()).contains(member)) {
			
			// the member record is stored via the set
			viaSpecification.setSet(set);
		}

		short nextDbkeyPosition = 
			getAdjustedDbkeyPosition(member, smr_052.getNxtDbk_052());
		memberRole.setNextDbkeyPosition(nextDbkeyPosition);

		short priorDbkeyPosition = 
			getAdjustedDbkeyPosition(member, smr_052.getPriDbk_052());
		if (priorDbkeyPosition != -1) {
			memberRole.setPriorDbkeyPosition(priorDbkeyPosition);
		}

		short ownerDbkeyPosition =  
			getAdjustedDbkeyPosition(member, smr_052.getOwnDbk_052());
		if (ownerDbkeyPosition != -1) {
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);
		}

		memberRole.setMembershipOption(MEMBERSHIP_OPTION[smr_052.getMrCntrl_052()]);
		
		if (dictionary.isConnected(smr_052, "SMR-SCR")) {			
			processControlKeys(memberRole, dictionary, smr_052);			
		}		
	}	
	
	/**
	 * This method creates a sort key element for a non-catalog set or index.
	 */
	private void processControlKey(Key key, Scr_054 scr_054) {		
		// assumption: the Key is connected to a SchemaRecord
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		key.getElements().add(keyElement);		
		keyElement.setSortSequence(SORT_SEQUENCE[scr_054.getSort_054()]);	    
		if (scr_054.getScrPos_054() != SORTED_BY_DBKEY) {			
			// no element has to be set in this situation
			Element element = 
				key.getRecord().getElement(scr_054.getScrNam_054());			
			keyElement.setElement(element);									
		}
	}
	
	/**
	 * This method creates a sort key element for a catalog set.
	 */
	private void processControlKey(Key key, IDatabase catalog,
								   Orderkey_1044 orderkey_1044) {		
		// assumption: the Key is connected to a SchemaRecord
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		key.getElements().add(keyElement);
		if (orderkey_1044.getSortorder_1044().equals("A")) {
			keyElement.setSortSequence(SortSequence.ASCENDING);
		} else {
			keyElement.setSortSequence(SortSequence.DESCENDING);
		}
		Constraint_1029 constraint_1029 =
			catalog.find(orderkey_1044, "CONSTRAINT-ORDER", OWNER);
		Table_1050 table_1050 = 
			catalog.find(constraint_1029, "REFERENCING-TABLE", OWNER);
		String elementName = 
			orderkey_1044.getColumn_1044().replaceAll("_", "-") + "-" +
			table_1050.getTableid_1050();
		Element element = key.getRecord().getElement(elementName);		
		if (element == null) {
			throw new RuntimeException("logic error: element " + elementName + 
					   				   " was not found in record " +
					   				   key.getRecord().getName());			
		}
		keyElement.setElement(element);											
	}
	
	/**
	 * This method creates a sort key element for a catalog index.
	 */
	private void processControlKey(Key key, IDatabase catalog,
								   Indexkey_1042 indexkey_1042) {		
		// assumption: the Key is connected to a SchemaRecord
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		key.getElements().add(keyElement);
		if (indexkey_1042.getSortorder_1042().equals("A")) {
			keyElement.setSortSequence(SortSequence.ASCENDING);
		} else {
			keyElement.setSortSequence(SortSequence.DESCENDING);
		}
		Index_1041 index_1041 =
			catalog.find(indexkey_1042, "INDEX-INDEXKEY", OWNER);
		Table_1050 table_1050 = catalog.find(index_1041, "TABLE-INDEX", OWNER);
		String elementName = 
			indexkey_1042.getColumn_1042().replaceAll("_", "-") + "-" +
			table_1050.getTableid_1050();
		Element element = key.getRecord().getElement(elementName);		
		if (element == null) {
			throw new RuntimeException("logic error: element " + elementName + 
					   				   " was not found in record " +
					   				   key.getRecord().getName());			
		}
		keyElement.setElement(element);											
	}	
	
	/**
	 * This method creates a non-catalog record's CALC key.
	 */
	private void processControlKeys(SchemaRecord record, IDatabase dictionary, 
			   						Smr_052 smr_052) {
		Key key = SchemaFactory.eINSTANCE.createKey();
		record.setCalcKey(key);
		record.getKeys().add(key);
		//
		if (smr_052.getDup_052() == DUPLICATES_NOT_ALLOWED) {
			key.setDuplicatesOption(DuplicatesOption.NOT_ALLOWED);
		} else if (smr_052.getDup_052() == DUPLICATES_FIRST) {
			key.setDuplicatesOption(DuplicatesOption.FIRST);
		} else if (smr_052.getDup_052() == DUPLICATES_LAST) {
			key.setDuplicatesOption(DuplicatesOption.LAST);
		} else if (smr_052.getDup_052() == DUPLICATES_BY_DBKEY) {
			key.setDuplicatesOption(DuplicatesOption.BY_DBKEY);
		}
		//
		key.setNaturalSequence(smr_052.getSort_052() == SORT_SEQUENCE_NATURAL);
		//
		for (Scr_054 scr_054 : 
			 dictionary.<Scr_054>walk(smr_052, "SMR-SCR", NEXT)) {

			processControlKey(key, scr_054);
		}		
	}	

	/**
	 * This method creates a sort key for a non-catalog set or index.
	 */
	private void processControlKeys(MemberRole memberRole, IDatabase dictionary, 
									Smr_052 smr_052) {
		Key key = SchemaFactory.eINSTANCE.createKey();
		memberRole.setSortKey(key);
		memberRole.getRecord().getKeys().add(key);
		//
		if (smr_052.getDup_052() == DUPLICATES_NOT_ALLOWED) {
			key.setDuplicatesOption(DuplicatesOption.NOT_ALLOWED);
		} else if (smr_052.getDup_052() == DUPLICATES_FIRST) {
			key.setDuplicatesOption(DuplicatesOption.FIRST);
		} else if (smr_052.getDup_052() == DUPLICATES_LAST) {
			key.setDuplicatesOption(DuplicatesOption.LAST);
		} else if (smr_052.getDup_052() == DUPLICATES_BY_DBKEY) {
			key.setDuplicatesOption(DuplicatesOption.BY_DBKEY);
		}
		//
		key.setNaturalSequence(smr_052.getSort_052() == SORT_SEQUENCE_NATURAL);
		//
		Sor_046 sor_046 = dictionary.find(smr_052, "SOR-SMR", OWNER);				
		if (sor_046.getSetMode_046() == MODE_INDEXED) {
			Scr_054 scr_054 = 
				dictionary.find(smr_052, "SMR-SCR", FIRST);
			key.setCompressed(scr_054.getIndex_054() == 1);													
		}					
		for (Scr_054 scr_054 : 
			 dictionary.<Scr_054>walk(smr_052, "SMR-SCR", NEXT)) {
									
			processControlKey(key, scr_054);
		}		
	}
	
	/**
	 * This method creates a sort key for a catalog set.
	 */
	private void processControlKeys(MemberRole memberRole, IDatabase catalog, 
									Constraint_1029 constraint_1029) {		
		Key key = SchemaFactory.eINSTANCE.createKey();
		memberRole.setSortKey(key);		
		memberRole.getRecord().getKeys().add(key);
		//
		if (constraint_1029.getUnique_1029().equals("Y")) {		
			key.setDuplicatesOption(DuplicatesOption.NOT_ALLOWED);
		} else {
			key.setDuplicatesOption(DuplicatesOption.LAST);
		} 
		// Keep compatibility with the old style schemas in which only sets
		// AREA-TABLE and AREA-INDEX provide a key in natural sequence; the 
		// truth is that we don't know how to derive this attribute from the
		// catalog.
		if (constraint_1029.getName_1029().equals("AREA-TABLE") ||
			constraint_1029.getName_1029().equals("AREA-INDEX")) {
			
			key.setNaturalSequence(false);
		} else {
			key.setNaturalSequence(true);
		}
		//		
		boolean compressed = constraint_1029.getCompress_1029().equals("Y");			
		key.setCompressed(compressed);
		//	
		for (Orderkey_1044 orderkey_1044 :
			 catalog.<Orderkey_1044>walk(constraint_1029, "CONSTRAINT-ORDER", NEXT)) {

			processControlKey(key, catalog, orderkey_1044);
		}
	}	
	
	/**
	 * This method creates a sort key for a catalog index.
	 */
	private void processControlKeys(MemberRole memberRole, IDatabase catalog, 
									Index_1041 index_1041) {		
		Key key = SchemaFactory.eINSTANCE.createKey();
		memberRole.setSortKey(key);		
		memberRole.getRecord().getKeys().add(key);
		//
		if (index_1041.getUnique_1041().equals("Y")) {		
			key.setDuplicatesOption(DuplicatesOption.NOT_ALLOWED);
		} else {
			key.setDuplicatesOption(DuplicatesOption.LAST);
		} 
		//
		key.setNaturalSequence(true);
		//		
		boolean compressed = index_1041.getCompress_1041().equals("Y");			
		key.setCompressed(compressed);
		//
		for (Indexkey_1042 indexkey_1042 :
			 catalog.<Indexkey_1042>walk(index_1041, "INDEX-INDEXKEY", NEXT)) {

			processControlKey(key, catalog, indexkey_1042);
		}
	}
	
	/**
	 * This method creates a catalog record's CALC key.
	 */
	private void processControlKeys(SchemaRecord record, IDatabase catalog, 
									Index_1041 index_1041) {		
		Key key = SchemaFactory.eINSTANCE.createKey();
		record.setCalcKey(key);
		record.getKeys().add(key);
		//
		if (index_1041.getUnique_1041().equals("Y")) {		
			key.setDuplicatesOption(DuplicatesOption.NOT_ALLOWED);
		} else {
			key.setDuplicatesOption(DuplicatesOption.LAST);
		} 
		//
		key.setNaturalSequence(true);
		//		
		boolean compressed = index_1041.getCompress_1041().equals("Y");			
		key.setCompressed(compressed);
		//
		for (Indexkey_1042 indexkey_1042 :
			 catalog.<Indexkey_1042>walk(index_1041, "INDEX-INDEXKEY", NEXT)) {

			processControlKey(key, catalog, indexkey_1042);
		}
	}	
	
	private Element processElement(SchemaRecord record, 
								   FieldForDMLJ fieldForDMLJ) {
		Element element = SchemaFactory.eINSTANCE.createElement();
		record.getElements().add(element);		
		element.setName(fieldForDMLJ.getName());
		element.setLevel(fieldForDMLJ.getLevel());		
		element.setUsage(Usage.get(fieldForDMLJ.getSdr_042().getUse_042()));				
		// element.setOffset(fieldForDMLJ.getOffset()); //derived
		// element.setLength(fieldForDMLJ.getLength()); // derived		
		String picture = fieldForDMLJ.getPicture();
		if (picture != null) {
			element.setPicture(picture);
		}
		element.setNullable(false);
		if (fieldForDMLJ.isInRedefines()) {
			String redefinedElementName = fieldForDMLJ.getRedefinedFieldName();
			Element redefinedElement = record.getElement(redefinedElementName);
			if (redefinedElement == null) {
				throw new RuntimeException("logic error: element " +
										   element.getName() + " redefines " +
										   redefinedElementName + ", but " +
										   redefinedElementName + 
										   " was not found in the record");
			} else if (redefinedElement.getLevel() != element.getLevel()) {
				throw new RuntimeException("logic error: element " +
						   				   element.getName() + " redefines " +
						   				   redefinedElementName + ", but " + 
						   				   element.getName() + 
						   				   "'s level number (" + 
						   				   element.getLevel() + 
						   				   ") does not match that of " +
						   				   redefinedElementName + 
						   				   " (" + redefinedElement.getLevel() + 
						   				   ")");
			}
			element.setRedefines(redefinedElement);
		}
		short occurrenceCount = fieldForDMLJ.getOccurrenceCount();
		if (occurrenceCount > 1) {
			OccursSpecification occursSpecification =
				SchemaFactory.eINSTANCE.createOccursSpecification();
			element.setOccursSpecification(occursSpecification);
			occursSpecification.setCount(occurrenceCount);
			FieldForDMLJ dependsOnField = fieldForDMLJ.getDependsOnField();
			if (dependsOnField != null) {
				Element dependsOnElement = 
					record.getElement(dependsOnField.getName());
				if (dependsOnElement == null) {
					throw new RuntimeException("logic error: element " +
							   				   element.getName() + 
							   				   "'s occurs-depending-on-element, " +							   
							   				   dependsOnField.getName() + 
							   				   ", was not found in the record");
				}
				occursSpecification.setDependingOn(dependsOnElement);
			}
		}
		for (FieldForDMLJ subordinateField : fieldForDMLJ.getChildren()) {
			Element childElement = 
				processElement(record,subordinateField);			
			element.getChildren().add(childElement);
		}
		return element;
	}
	
	private void processElements(SchemaRecord record, 
								 FieldExtractorForDMLJ fieldExtractor) {
		for (FieldForDMLJ fieldForDMLJ : fieldExtractor.getTopLevelFields()) {			
			Element element = processElement(record, fieldForDMLJ);			
			record.getRootElements().add(element);
		}
	}

	private void processIndexedSetMember(Set set, IDatabase dictionary,
										 		Smr_052 smr_052, 
										 		String suffix) {
		
		// assumption: the set is connected to its schema		
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		
		// create the Connection for the MemberRole; a Connection represents a
		// line in the diagram...
		ConnectionPart connectionPart = 
			SchemaFactory.eINSTANCE.createConnectionPart();		
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.getSet()
		  		  .getSchema()
		  		  .getDiagramData()
		  		  .getConnectionParts()
		  		  .add(connectionPart);
		
		Srcd_113 srcd_113 = dictionary.find(smr_052, "SRCD-SMR", OWNER);
		Sam_056 sam_056 = dictionary.find(srcd_113, "SRCD-SAM", FIRST);		
		
		String recordName;
		if (suffix == null) {
			recordName = sam_056.getSrNam_056();
		} else {
			recordName = sam_056.getSrNam_056() + suffix;
		}
		SchemaRecord member = set.getSchema().getRecord(recordName);
		member.getMemberRoles().add(memberRole);
		
		ViaSpecification viaSpecification = member.getViaSpecification();
		if (viaSpecification != null && 
			viaSetMembers.containsKey(set.getName()) &&
			viaSetMembers.get(set.getName()).contains(member)) {
			
			// the member record is stored via the set
			viaSpecification.setSet(set);
		}

		short indexDbkeyPosition = 
			getAdjustedDbkeyPosition(member, smr_052.getNxtDbk_052());
		if (indexDbkeyPosition != -1) {
			memberRole.setIndexDbkeyPosition(indexDbkeyPosition);
		}

		short ownerDbkeyPosition = 
			getAdjustedDbkeyPosition(member, smr_052.getOwnDbk_052());
		if (ownerDbkeyPosition != -1) {
			memberRole.setOwnerDbkeyPosition(ownerDbkeyPosition);
		}

		memberRole.setMembershipOption(MEMBERSHIP_OPTION[smr_052.getMrCntrl_052()]);
		
		if (dictionary.isConnected(smr_052, "SMR-SCR")) {
			processControlKeys(memberRole, dictionary, smr_052);			
		}		
	}
	
	private SchemaRecord processRecord(Schema schema, IDatabase dictionary, 
									   Sam_056 sam_056, Srcd_113 srcd_113, 
									   boolean[] option, SchemaArea targetArea, 
									   String suffix) {		

		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		schema.getRecords().add(record);
		
		if (suffix == null) {
			record.setName(sam_056.getSrNam_056());
		} else {
			record.setName(sam_056.getSrNam_056() + suffix.trim());
		}
		record.setId(srcd_113.getSrId_113());	
		
		Rcdsyn_079 rcdsyn_079 = dictionary.find(srcd_113, "RCDSYN-SRCD", OWNER); 
		FieldExtractorForDMLJ fieldExtractor = 
			new FieldExtractorForDMLJ(dictionary, rcdsyn_079);
		fieldExtractor.processFields();
		
		processElements(record, fieldExtractor);
		
		record.setStorageMode(getStorageMode(fieldExtractor, dictionary, 
							  srcd_113)); 
				
		if (srcd_113.getRecType_113().equals("V")) {			
			record.setMinimumRootLength((short)(srcd_113.getMinRoot_113() - 4));
			record.setMinimumFragmentLength(srcd_113.getMinFrag_113());
		}

		if (srcd_113.getMode_113() == MODE_VIA) {			
			record.setLocationMode(LocationMode.VIA);
			ViaSpecification viaSpecification = 
				SchemaFactory.eINSTANCE.createViaSpecification();			
			for (Smr_052 smr_052 : 
				dictionary.<Smr_052>walk(srcd_113, "SRCD-SMR", NEXT)) {

				if (smr_052.getVia_052() == 1) {
					// the reference to the Set will be set when the Set is
					// created, so temporarily put away the record
					String setName;
					if (suffix != null) {
						setName = smr_052.getSetNam_052() + suffix;
					} else {
						setName = smr_052.getSetNam_052();
					}
					List<SchemaRecord> viaRecordsForSet;
					if (viaSetMembers.containsKey(setName)) {
						viaRecordsForSet = viaSetMembers.get(setName);
					} else {
						viaRecordsForSet = new ArrayList<>();
						viaSetMembers.put(setName, viaRecordsForSet);
					}
					viaRecordsForSet.add(record);					
				}
			}
			record.setViaSpecification(viaSpecification);

			if (!srcd_113.getSymbolDisplace_113().equals("") ||
				srcd_113.getDspl_113() != 0) {

				if (!srcd_113.getSymbolDisplace_113().equals("")) {
					viaSpecification.setSymbolicDisplacementName(srcd_113.getSymbolDisplace_113());
				} else  {
					viaSpecification.setDisplacementPageCount(srcd_113.getDspl_113());
				}				
			}						
		} else if (srcd_113.getMode_113() == MODE_CALC) {
			record.setLocationMode(LocationMode.CALC);
			for (Smr_052 smr_052 : 
				 dictionary.<Smr_052>walk(srcd_113, "SRCD-SMR", NEXT)) {

				if (smr_052.getSetNam_052().equals("CALC")) {					
					processControlKeys(record, dictionary, smr_052);
				}
			}			
		} else {
			record.setLocationMode(LocationMode.DIRECT);
		}
		
		addRecordToArea(record, srcd_113, sam_056, null, option, targetArea);

		if (dictionary.isConnected(srcd_113, "SRCD-SRCALL")) {			
			for (Srcall_040 srcall_040 : 
				 dictionary.<Srcall_040>walk(srcd_113, "SRCD-SRCALL", NEXT)) {
			
				String procedureName = srcall_040.getCallProc_040();
				Procedure procedure = schema.getProcedure(procedureName);
				if (procedure == null) {
					procedure = SchemaFactory.eINSTANCE.createProcedure();
					procedure.setName(procedureName);					 
					schema.getProcedures().add(procedure);
				}
				RecordProcedureCallSpecification callSpec =
					SchemaFactory.eINSTANCE
								 .createRecordProcedureCallSpecification();
				record.getProcedures().add(callSpec);
				callSpec.setProcedure(procedure);
				if (srcall_040.getCallTime_040().equals("00")) {
					callSpec.setCallTime(ProcedureCallTime.BEFORE);
				} else if (srcall_040.getCallTime_040().equals("01")) {
					callSpec.setCallTime(ProcedureCallTime.ON_ERROR_DURING);
				} else if (srcall_040.getCallTime_040().equals("02")) {
					callSpec.setCallTime(ProcedureCallTime.AFTER);
				}				
				RecordProcedureCallVerb verb;				
				if (!srcall_040.getDbpFunc_040().equals("")) {
					String trigger = srcall_040.getDbpFunc_040().toUpperCase();					
					verb = RecordProcedureCallVerb.valueOf(trigger.toString());				
				} else {
					// null doesn't work here and hence the EVERY_DML_FUNCTION
					// verb was created
					verb = RecordProcedureCallVerb.EVERY_DML_FUNCTION;
				}
				callSpec.setVerb(verb);
			}			
		}
				
		return record;
	}	
	
	private Schema processSchema(IDatabase dictionary, S_010 s_010, 
								 boolean[] option, IDatabase catalog) {
		
		// schema...
		Schema schema = SchemaFactory.eINSTANCE.createSchema();
		schema.setName(s_010.getSNam_010());
		schema.setVersion(s_010.getSSer_010());
		schema.setDescription(s_010.getDescr_010());
		schema.setMemoDate(s_010.getSDt_010());
		
		// diagram data with rulers...
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();		
		schema.setDiagramData(diagramData);
		Ruler verticalRuler = SchemaFactory.eINSTANCE.createRuler();
		verticalRuler.setType(RulerType.VERTICAL);
		diagramData.setVerticalRuler(verticalRuler);
		diagramData.getRulers().add(verticalRuler); 	// ruler container
		Ruler horizontalRuler = SchemaFactory.eINSTANCE.createRuler();
		horizontalRuler.setType(RulerType.HORIZONTAL);
		diagramData.setHorizontalRuler(horizontalRuler);
		diagramData.getRulers().add(horizontalRuler);	// ruler container
		
		// areas...
		for (Sa_018 sa_018 : dictionary.<Sa_018>walk(s_010, "S-SA", NEXT)) {
			processArea(schema, dictionary, sa_018, option);
		}
		
		// areas (catalog)...
		if (isIdmsntwk(schema) && option[OPTION_COMPLETE_CATALOG]) {
			// the DDLCAT and DDLCATX areas should be created already; the
			// DDLCATLOD area is normally missing, so create it...
			if (schema.getArea("DDLCATLOD") == null) {
				processCatalogArea(schema, "DDLCATLOD");
			}
		}		
		
		// records (without diagram data)...
		for (Srcd_113 srcd_113 : 
			 dictionary.<Srcd_113>walk(s_010, "S-SRCD", NEXT)) {
			
			// skip system records...
			if (srcd_113.getSrId_113() >= 10) {
				for (Sam_056 sam_056 : 
					 dictionary.<Sam_056>walk(srcd_113, "SRCD-SAM", NEXT)) {
								
					// definitions for INDEX-1041 and TABLE-1050 are much more
					// complete in the catalog, so defer the creation of these
					// records...
					if (!option[OPTION_COMPLETE_CATALOG] ||
						!sam_056.getSrNam_056().equals("INDEX-1041") &&
						!sam_056.getSrNam_056().equals("TABLE-1050")) {
						
						processRecord(schema, dictionary, sam_056, srcd_113, 
								  	  option, null, null);
					}
				}
			}
		}
		
		// records (catalog; without diagram data)...
		if (isIdmsntwk(schema) && option[OPTION_COMPLETE_CATALOG]) {			
			// clone the DDLDCLOD records to the DDLCATLOD area...
			SchemaArea area = schema.getArea("DDLCATLOD");
			if (area == null) {
				throw new RuntimeException("area DDLCATLOD could NOT be found");
			}
			for (Srcd_113 srcd_113 : 
				 dictionary.<Srcd_113>walk(s_010, "S-SRCD", NEXT)) {
				
				// skip system records and records that are not stored in the
				// DDLDCLOD area...
				if (srcd_113.getSrId_113() >= 10) {
					for (Sam_056 sam_056 : 
						 dictionary.<Sam_056>walk(srcd_113, "SRCD-SAM", NEXT)) {
									
						if (sam_056.getSaNam_056().equals("DDLDCLOD")) {
							processRecord(schema, dictionary, sam_056, srcd_113, 
										  option, area, "_");
						}
					}
				}
			}
			// generate catalog related records...
			Schema_1045 schema_1045 =
				catalog.find(Schema_1045.class, 
							 new CalcKey_Schema_1045("SYSTEM"));
			for (Table_1050 table_1050 :
				 catalog.<Table_1050>walk(schema_1045, "SCHEMA-TABLE", NEXT)) {
				
				String recordName = 
					table_1050.getName_1050().replace("_", "-") + "-" + 
					table_1050.getTableid_1050();
				// avoid duplicate records at all time...
				if (schema.getRecord(recordName) == null) {
					processCatalogRecord(schema, catalog, table_1050);
				}
			}
		}	
		
		  
		if (schema.getName().equals("IDMSNTWK") && schema.getVersion() == 1 ||
			schema.getName().equals("EMPSCHM") && schema.getVersion() == 100) {
			
			// get the diagram data for all records using a Properties object...
			ClassLoader cl = SchemaImportTool.class.getClassLoader();
			InputStream in = 
				cl.getResourceAsStream("/resources/" + schema.getName() +
									   " version " + schema.getVersion() +
									   " (Record Locations).txt");
			if (in == null) {
				// We get this when we run the schema import tool in a normal 
				// (i.e. non plug-in) environment, outside the new (extended)
				// schema wizard (from the command line).
				in = cl.getResourceAsStream("\\resources\\" + schema.getName() +
										    " version " + schema.getVersion() +
										    " (Record Locations).txt");
			}
			Properties locations = new Properties();
			try {
				locations.load(in);
				in.close();
			} catch (Throwable t) {
				throw new Error(t);
			}			
			List<SchemaRecord> notSet = new ArrayList<>();
			for (SchemaRecord record : schema.getRecords()) {	
				// calculate and set the record's diagram data...
				if (locations.containsKey(record.getName())) {
					Rectangle rectangle = 
						toRectangle(locations.getProperty(record.getName()));
					setDiagramData(record, rectangle.x, rectangle.y);
				} else {
					notSet.add(record);
				}
			}	
			if (!notSet.isEmpty()) {
				throw new Error("not all record diagram data set: " + 
								notSet.toString());
			}
		} else {
		    // now that we know how many records the schema contains, compute 
			// the number of columns so that it is about the same as the number
			// of rows...
			int columnCount = 
				(int) Math.ceil(Math.sqrt((double)schema.getRecords().size()));
			int maxX = X_INITIAL_VALUE + (columnCount - 1) * X_INCREMENT;		    
			int x = X_INITIAL_VALUE;
		    int y = Y_INITIAL_VALUE;
			for (SchemaRecord record : schema.getRecords()) {
				setDiagramData(record, x, y);
				x += X_INCREMENT;				
				if (x > maxX) {
					x = X_INITIAL_VALUE;
					y += Y_INCREMENT;				
				}				
			}
		}
		
		// sets...
		// REMARK:  the order of the member roles for a record in our model
		// differ from that of the schema compiler output; we should look into
		// this at some time later !
		for (Sor_046 sor_046 : dictionary.<Sor_046>walk(s_010, "S-SOR", NEXT)) {
			// defer the creation of "AREA-INDEX", "AREA-TABLE" and 
			// "TABLE-INDEX" since they are stored in the catalog too... 
			if (!option[OPTION_COMPLETE_CATALOG] ||
				!sor_046.getSetNam_046().equals("AREA-INDEX") &&
				!sor_046.getSetNam_046().equals("AREA-TABLE") &&
				!sor_046.getSetNam_046().equals("TABLE-INDEX")) {
				
				short mode = sor_046.getSetMode_046();
				if (mode == 11 && sor_046.getSorId_046() == 1) {
					// skip CALC sets
				} else if (mode == MODE_CHAINED[0] || mode == MODE_CHAINED[1]) {
					processChainedSet(schema, dictionary, sor_046, option,
									  null);
				} else if (mode == MODE_INDEXED) {
					if (sor_046.getSorId_046() == 7) {		
						processSystemOwnedIndexedSet(schema, dictionary, 
													 sor_046, option, null);
					} else {								
						processUserOwnedIndexedSet(schema, dictionary, sor_046, 
												   option, null);
					}
				} else {
					// cannot happen...
					throw new RuntimeException("unsupported set type: set=" + 
											   sor_046.getSetNam_046() +
											   ", mode=" + mode + ", owner=SR" +
											   sor_046.getSorId_046());
				}
			}
		}
		
		// sets (catalog)...
		if (isIdmsntwk(schema) && option[OPTION_COMPLETE_CATALOG]) {
			// clone the DDLDCLOD sets to the DDLCATLOD area; there are no 
			// references to other areas...
			for (Sor_046 sor_046 : dictionary.<Sor_046>walk(s_010, "S-SOR", NEXT)) {
				Srcd_113 srcd_113 = dictionary.find(sor_046, "SRCD-SOR", OWNER);
				if (srcd_113.getSrId_113() != 1) { // ignore all CALC sets
					Sam_056 sam_056 = dictionary.find(srcd_113, "SRCD-SAM", FIRST);
					if (sam_056.getSaNam_056().equals("DDLDCLOD")) {
						short mode = sor_046.getSetMode_046();
						if (mode == MODE_CHAINED[0] || 
							mode == MODE_CHAINED[1]) {
							
							processChainedSet(schema, dictionary, sor_046, 
											  option, "_");
						} else if (mode == MODE_INDEXED) {
							if (sor_046.getSorId_046() == 7) {		
								processSystemOwnedIndexedSet(schema, dictionary, 
															 sor_046, option, 
															 "_");
							} else {								
								processUserOwnedIndexedSet(schema, dictionary, 
														   sor_046, option, 
														   "_");
							}
						} else {
							// cannot happen...
							String p = 
								"unsupported set type: set=" + 
								sor_046.getSetNam_046() + ", mode=" + mode + 
								", owner=SR" + sor_046.getSorId_046();
							throw new RuntimeException(p);
						}						
					}
				}
			}
			// generate catalog related sets...
			Schema_1045 schema_1045 =
				catalog.find(Schema_1045.class, 
							 new CalcKey_Schema_1045("SYSTEM"));
			// chained and user-owned indexed sets...
			for (Constraint_1029 constraint_1029 :
				 catalog.<Constraint_1029>walk(schema_1045, "SCHEMA-CONSTRAINT", NEXT)) {
								
				String setName = constraint_1029.getName_1029();
				// avoid duplicate sets at all time...
				if (schema.getSet(setName) == null) {
					if (constraint_1029.getType_1029().equals("L")) {
						// chained set						
						processCatalogChainedSet(schema, catalog, 
												 constraint_1029);								
					} else if (constraint_1029.getType_1029().equals("X")) {
						// indexed set (user-owned)	
						processCatalogUserOwnedIndexedSet(schema, catalog, 
								 						  constraint_1029);
					} else {
						throw new RuntimeException("unsupported constraint " +
												   "type: set=" + setName +
												   ", type=" + 
												   constraint_1029.getType_1029());
					}
				}
			}	
			// system-owned indexed sets...
			// (we cannot use IX-INDEX because we don't have the DDLCATX area)
			Map<String, Index_1041> map = new HashMap<String, Index_1041>();
			for (Index_1041 index_1041 :
				 catalog.<Index_1041>sweep("DDLCAT", RecordIdFilter.include(1041))) {
				
				// ignore indexes from schemas other than SYSTEM and indexes
				// that go by the name "HASH"...
				if (index_1041.getSchema_1041().equals("SYSTEM") &&
					!index_1041.getName_1041().equals("HASH")) {
					
					String setName = index_1041.getName_1041();
					// avoid duplicate sets at all time...
					if (schema.getSet(setName) == null) {
						map.put(setName, index_1041);
					}
				}
			}
			List<String> keys = new ArrayList<String>(map.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				Index_1041 index_1041 = map.get(key);			
				processCatalogIndex(schema, catalog, index_1041);
			}
		}
		
		// create all all member role diagram labels and set the diagram data 
		// for them (all labels for a given record will overlap), as well as for 
		// all system owned indexed sets...
		for (SchemaRecord record : schema.getRecords()) {
			int i = 0;
			for (MemberRole memberRole : record.getMemberRoles()) {
								
				// deal with the set label...
				ConnectionLabel connectionLabel = 
					SchemaFactory.eINSTANCE.createConnectionLabel();
				schema.getDiagramData()
					  .getConnectionLabels()
					  .add(connectionLabel);
				DiagramLocation location = 
					SchemaFactory.eINSTANCE.createDiagramLocation();
				schema.getDiagramData().getLocations().add(location);
				connectionLabel.setDiagramLocation(location);
				memberRole.setConnectionLabel(connectionLabel);
				
				// we don't provide a source- and targetAnchor nor any 
				// bendpoints for the diagram connection here
				location.setX(record.getDiagramLocation().getX());
				location.setY(record.getDiagramLocation().getY() - 25);
				location.setEyecatcher("set label " + 
									   memberRole.getSet().getName() + " (" + 
									   record.getName() + ")");
				
				Set set = memberRole.getSet();
				SystemOwner systemOwner = set.getSystemOwner();
				if (systemOwner != null) {
					location = 
						SchemaFactory.eINSTANCE.createDiagramLocation();
					schema.getDiagramData().getLocations().add(location);
					systemOwner.setDiagramLocation(location);
					location.setX(record.getDiagramLocation()
										.getX() + 25 * i++ - 11);
					location.setY(record.getDiagramLocation().getY() - 70);
					location.setEyecatcher("system owner " + 
										   memberRole.getSet().getName());
				}
			}
		}
				
		return schema;
		
	}	
	
	private void processSystemOwnedIndexedSet(Schema schema,
											  IDatabase dictionary, 
											  Sor_046 sor_046, boolean[] option,
											  String suffix) {		
		String setName;
		if (suffix == null) {
			setName = sor_046.getSetNam_046();
		} else {
			setName = sor_046.getSetNam_046() + suffix;
		}

		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		set.setMode(SetMode.INDEXED);
		set.setOrder(ORDER[sor_046.getSetOrd_046()][sor_046.getOrd_046()]);

		SystemOwner systemOwner = SchemaFactory.eINSTANCE.createSystemOwner();
		set.setSystemOwner(systemOwner);		
		
		IndexedSetModeSpecification indexedSetModeSpec =
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();
		set.setIndexedSetModeSpecification(indexedSetModeSpec);
		if (sor_046.getSymbolIndex_046().equals("")) {
			indexedSetModeSpec.setKeyCount(sor_046.getIndexMembers_046());
			short displacement = sor_046.getIndexDisp_046();
			indexedSetModeSpec.setDisplacementPageCount(displacement);			
		} else {
			indexedSetModeSpec.setSymbolicIndexName(sor_046.getSymbolIndex_046());
		}

		Srcd_113 srcd_113 = dictionary.find(sor_046, "SRCD-SOR", OWNER);
		Sam_056 sam_056 = null;
		
		String p = sor_046.getSaNam_046();
		for (Sam_056 aSam_056 : 
			 dictionary.<Sam_056>walk(srcd_113, "SRCD-SAM", NEXT)) {

			if (p.equals(aSam_056.getSaNam_056())) {
				sam_056 = aSam_056;
			}
		}
		
		addSystemOwnerToArea(schema, systemOwner, sam_056, sor_046);		
		
		Smr_052 smr_052 = dictionary.find(sor_046, "SOR-SMR", FIRST);
		processIndexedSetMember(set, dictionary, smr_052, suffix);		
		
	}	
	
	private void processUserOwnedIndexedSet(Schema schema, IDatabase dictionary, 
											Sor_046 sor_046, boolean[] option,
			 				 				String suffix) {
		
		String setName;
		if (suffix == null) {
			setName = sor_046.getSetNam_046();
		} else {
			setName = sor_046.getSetNam_046() + suffix;
		}
	
		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		set.setMode(SetMode.INDEXED);
		set.setOrder(ORDER[sor_046.getSetOrd_046()][sor_046.getOrd_046()]);			
	
		OwnerRole ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);
		
		Srcd_113 srcd_113 = dictionary.find(sor_046, "SRCD-SOR", OWNER);
		Sam_056 sam_056 = dictionary.find(srcd_113, "SRCD-SAM", FIRST);	
		String recordName;
		if (suffix == null) {
			recordName = sam_056.getSrNam_056();
		} else {
			recordName = sam_056.getSrNam_056() + suffix;
		}
		SchemaRecord owner = schema.getRecord(recordName);
		owner.getOwnerRoles().add(ownerRole);
				
		short nextDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, sor_046.getNxtDbk_046());
		ownerRole.setNextDbkeyPosition(nextDbkeyPosition);
		short priorDbkeyPosition = 
			getAdjustedDbkeyPosition(owner, sor_046.getPriDbk_046());
		ownerRole.setPriorDbkeyPosition(priorDbkeyPosition);
		
		IndexedSetModeSpecification indexedSetModeSpec =
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();
		set.setIndexedSetModeSpecification(indexedSetModeSpec);
		if (sor_046.getSymbolIndex_046().equals("")) {
			indexedSetModeSpec.setKeyCount(sor_046.getIndexMembers_046());
			short displacement = sor_046.getIndexDisp_046();
			indexedSetModeSpec.setDisplacementPageCount(displacement);
		} else {
			indexedSetModeSpec.setSymbolicIndexName(sor_046.getSymbolIndex_046());
		}		
		
		Smr_052 smr_052 = dictionary.find(sor_046, "SOR-SMR", FIRST);
		processIndexedSetMember(set, dictionary, smr_052, suffix);
	}

	private void setDiagramData(SchemaRecord record, int x, int y) {		
		DiagramLocation location =
			SchemaFactory.eINSTANCE.createDiagramLocation();
		record.getSchema().getDiagramData().getLocations().add(location);
		record.setDiagramLocation(location);		
		location.setX(x);
		location.setY(y);
		location.setEyecatcher("record " + record.getName());		
	}

	private Rectangle toRectangle(String property) {
		
		// the first part of the property value passed consists of one or two
		// letters corresponding to the row, the second part is any number from 
		// 1 onwards and represents the column		
		int row;
		int column;		
		try {
			row = LETTERS.indexOf(property.substring(0, 1));
			column = Integer.valueOf(String.valueOf(property.substring(1))) - 1;			
		} catch (NumberFormatException e) {
			row = 26 + 26 * LETTERS.indexOf(property.substring(0, 1)) +
				  LETTERS.indexOf(property.substring(1, 2));
			column = Integer.valueOf(String.valueOf(property.substring(2))) - 1;				
		}
				
		return new Rectangle(X_INITIAL_VALUE + column * X_HALF_INCREMENT, 
							 Y_INITIAL_VALUE + row * Y_INCREMENT, 0, 0);
	}	
}