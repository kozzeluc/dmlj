package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
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
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

class ModelFactory {
	
	// the schema we're working on
	private Schema schema;
	
	// a cache for resolving VIA records and their VIA sets:
	private Map<String, List<SchemaRecord>> viaSetMembers = new HashMap<>();

	private static boolean isOccursInvolved(Element element) {
		if (element.getOccursSpecification() != null) {
			return true;
		}
		if (element.getParent() != null) {
			return isOccursInvolved(element.getParent());
		} else {
			return false;
		}
	}
	
	private static boolean isRedefinesInvolved(Element element) {
		if (element.getRedefines() != null) {
			return true;
		}
		if (element.getParent() != null) {
			return isRedefinesInvolved(element.getParent());
		} else {
			return false;
		}
	}	

	private static String toUppercaseWithValidation(String name,
													NamingConventions.Type type) {

		String p = type.toString().toLowerCase().replaceAll("_", " ");

		// make sure name is not null
		Assertions.isNotNull(name, p + " is null");

		// convert the name to upper case and check its validity
		String uName = name.trim().toUpperCase();
		ValidationResult validationResult = NamingConventions.validate(uName,
				type);
		if (validationResult.getStatus() != ValidationResult.Status.OK) {
			String message = "invalid " + p + ": " + uName + " (" + 
							 validationResult.getMessage() + ")";
			throw new RuntimeException(message);
		}

		// return name converted to upper case
		return uName;

	}	
	
	ModelFactory(Schema schema) {
		super();
		this.schema = schema;
	}

	SchemaArea createArea(String name) {
		
		// convert the name to uppercase and validate it
		String areaName = 
			toUppercaseWithValidation(name, 
									  NamingConventions.Type.LOGICAL_AREA_NAME);
		
		// make sure the area does not yet exist
		if (schema.getArea(areaName) != null) {
			throw new IllegalArgumentException("duplicate area name: " +
											   areaName);
		}
		
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(areaName);												
		schema.getAreas().add(area);
		return area;
	}	
	
	AreaSpecification createAreaSpecification(SchemaArea area, 
	    	  								  SchemaRecord record) {

		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		record.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		return areaSpecification;
	}	
	
	AreaSpecification createAreaSpecification(SchemaArea area, 
			  								  SystemOwner systemOwner) {

		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		systemOwner.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		return areaSpecification;
	}	
	
	Element createElement(SchemaRecord record, Element parent, String name,
						  String baseName) {		
		
		// validate the element names and convert it to upper case
		String elementName = 
			toUppercaseWithValidation(name, NamingConventions.Type.ELEMENT_NAME);
		String baseElementName = 
			toUppercaseWithValidation(baseName, 
									  NamingConventions.Type.ELEMENT_NAME);
		
		// make sure the element does not yet exist unless it's a FILLER
		if (!elementName.equals("FILLER")) {
			Assertions.isNull(record.getElement(elementName), 
							  "duplicate element name: " + elementName);
		}
			
		// create the element
		Element element = SchemaFactory.eINSTANCE.createElement();
		element.setName(name);
		element.setBaseName(baseElementName); // null or different from name
		record.getElements().add(element);		
		
		// if the element is a root element, add it to the record's root element 
		// list, otherwise, add it to the parent's list of children
		if (parent == null) {
			record.getRootElements().add(element);
		} else {
			parent.getChildren().add(element);
		}
		
		return element;
	}	
	
	IndexedSetModeSpecification createIndexedSetModeSpecification(Set set,
																  String symbolicIndexName,
																  Short keyCount,
																  Short displacementPageCount) {
		
		IndexedSetModeSpecification indexedSetModeSpecification =
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();		
		set.setIndexedSetModeSpecification(indexedSetModeSpecification);
		
		if (symbolicIndexName != null) {
			
			// a symbolic index name is specified; convert it to uppercase and 
			// validate it
			String ucSymbolicIndexName = 
				toUppercaseWithValidation(symbolicIndexName, 
										  NamingConventions.Type.SYMBOLIC_INDEX_NAME);			
			
			// set the symbolic index name
			indexedSetModeSpecification.setSymbolicIndexName(ucSymbolicIndexName);
			
		} else {
			
			// a key count must be specified and validated; the key count must 
			// be an unsigned integer in the range 3 through 8180
			Assertions.isNotNull(keyCount, "key count is null (set=" + 
								 set.getName() + ")");
			if (keyCount.shortValue() < 3 || keyCount.shortValue() > 8180) {
				throw new RuntimeException("key count is invalid: " + 
										   keyCount.shortValue() + " (set=" +
										   set.getName() + ")");
			}
			indexedSetModeSpecification.setKeyCount(keyCount);
			
			// the displacement page count is optional; if specified, validate 
			// it
			if (displacementPageCount != null) {
				// displacementPageCount must be an unsigned integer in the 
				// range 0 through 32,767 (32767 is the maximum value for a 
				// short, so we don't need to check that)
				if (displacementPageCount.shortValue() < 0) {
					throw new RuntimeException("invalid displacement page " +
											   "count: " + 
											   displacementPageCount.shortValue() + 
											   " (set=" + set.getName() + ")");
				}
				indexedSetModeSpecification.setDisplacementPageCount(displacementPageCount);
			}					
			
		}		
		
		return indexedSetModeSpecification;
	}	
	
	Key createKey(SchemaRecord record, DuplicatesOption duplicatesOption, 
				  boolean naturalSequence) {
		
		Key key = SchemaFactory.eINSTANCE.createKey();
		record.setCalcKey(key);
		record.getKeys().add(key);
		
		Assertions.isNotNull(duplicatesOption, DuplicatesOption.class);
		key.setDuplicatesOption(duplicatesOption);
		key.setNaturalSequence(naturalSequence);
		
		return key;
	}	
	
	Key createKey(MemberRole memberRole, DuplicatesOption duplicatesOption, 
			  	  boolean naturalSequence) {
	
		Key key = SchemaFactory.eINSTANCE.createKey();
		memberRole.setSortKey(key);
		memberRole.getRecord().getKeys().add(key);
	
		Assertions.isNotNull(duplicatesOption, DuplicatesOption.class);
		key.setDuplicatesOption(duplicatesOption);
		key.setNaturalSequence(naturalSequence);
	
		return key;
	}	
	
	KeyElement createKeyElement(Key key, String elementName, 
								SortSequence sortSequence) {
		
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		key.getElements().add(keyElement);
		
		// if no element name is provided, the set is sorted by dbkey
		if (elementName != null) {
			
			// convert the element name to uppercase and have it validated
			String keyElementName = 
				toUppercaseWithValidation(elementName, 
										  NamingConventions.Type.ELEMENT_NAME);
			
			// No element named FILLER can be used in a key
			if (keyElementName.equals("FILLER")) {
				throw new RuntimeException("No element named FILLER can be " +
										   "used in a key");
			}
			
			// fetch the element, make sure it exists
			Element element = key.getRecord().getElement(keyElementName);
			Assertions.isNotNull(element, 
								 "element not found: " + keyElementName);
			
			// occurs check
			if (isOccursInvolved(element)) {
				throw new RuntimeException("No repeating element and no " +
										   "element subordinate to a " +
										   "repeating element can be used in " +
										   "a key");
			}
			
			// redefines check (not for CALC keys)
			if (!key.isCalcKey() && isRedefinesInvolved(element)) {
				throw new RuntimeException("No element that redefines " +
										   "another element or is " +
										   "subordinate to an element that " +
										   "redefines another element can be " +
										   "used as a sort key element");
			}
			
			keyElement.setElement(element);
			
			// the maximum key length is 256
			int i = 0;
			for (KeyElement aKeyElement : key.getElements()) {
				i += aKeyElement.getElement().getLength();
			}
			if (i > 256) {
				throw new RuntimeException("The combined lengths of the " +
										   "elements in a key must not " +
										   "exceed 256 bytes");
			}
			
		} else {
			// just check if the key contains exactly 1 key element
			Assertions.isSingleElementCollection(key.getElements(), 
												 "logic error: only 1 key " +
												 "element allowed when " +
												 "sorted by dbkey");
		}
		
		Assertions.isNotNull(sortSequence, SortSequence.class);
		keyElement.setSortSequence(sortSequence);
		
		return keyElement;
	}	
	
	OccursSpecification createOccursSpecification(Element element) {
		OccursSpecification occursSpecification =
			SchemaFactory.eINSTANCE.createOccursSpecification();
		element.setOccursSpecification(occursSpecification);
		return occursSpecification;
	}
	
	OffsetExpression createOffsetExpression(AreaSpecification areaSpecification,
											String symbolicSubareaName,
											Integer offsetPageCount,
											Short offsetPercent,
											Integer pageCount,			
											Short percent) {
		
		// no offset expression is created if a symbolic subarea name is 
		// provided; the symbolic subarea name is set directly in the area
		// specification
		if (symbolicSubareaName != null) {
		
			// validate the symbolic subarea name after converting it to 
			// uppercase 
			String ucSymbolicSubareaName = 
				toUppercaseWithValidation(symbolicSubareaName, 
										  NamingConventions.Type
										  				   .SYMBOLIC_DISPLACEMENT);			
				
			// set the symbolic subarea name in the area specification
			areaSpecification.setSymbolicSubareaName(ucSymbolicSubareaName);
		
			return null;
		}
		
		// only create an offset expression when at least 1 of its attributes is
		// available
		if (offsetPageCount != null || offsetPercent != null  || 
			pageCount != null || percent != null ) {
		
			// create the offset expression
			OffsetExpression offsetExpression = 
				SchemaFactory.eINSTANCE.createOffsetExpression();
			areaSpecification.setOffsetExpression(offsetExpression);
				
			// set the offset page count or percent
			if (offsetPageCount != null) {
				// Offset-page-count must be an integer in the range 0 
				// through the number of pages in physical-area-name minus 
				// 1 - we don't have the number of pages in the physical
				// area, so we cannot check that.					
				int i = offsetPageCount.intValue();
				if (i < 0) {
					throw new RuntimeException("invalid offset page count");
				}
				offsetExpression.setOffsetPageCount(offsetPageCount);					
			} else if (offsetPercent != null) {
				// Offset-percent must be an integer in the range 0 through 
				// 100.
				short i = offsetPercent.shortValue();
				if (i < 0 || i > 100) {
					throw new RuntimeException("invalue offset percent");
				}
				offsetExpression.setOffsetPercent(offsetPercent);
			} else {
				// set the default value for the offset page count: 0
				offsetExpression.setOffsetPageCount(Integer.valueOf(0));
			}
				
			// set the page count or percent
			if (pageCount != null) {
				// we'll accept any value bigger than zero
				int i = pageCount.intValue();
				if (i < 1) {
					throw new RuntimeException("invalid page count");
				}
				offsetExpression.setPageCount(pageCount);
			} else if (percent != null) {
				// Percent must be an integer in the range 1 through 100.
				short i = percent.shortValue();
				if (i < 1 || i > 100) {
					throw new RuntimeException("invalid percent");
				}
				offsetExpression.setPercent(percent);				
			} else {
				// set the default value for the percent: 100
				offsetExpression.setPercent(Short.valueOf((short) 100));
			}
		
			return offsetExpression;
			
		} else {
			
			return null;
			
		}
				
	}	
	
	private Procedure createProcedureIfNotExists(String name) {
		
		String procedureName = 
			toUppercaseWithValidation(name, 
									  NamingConventions.Type.PROCEDURE_NAME);
		
		Procedure procedure = schema.getProcedure(procedureName);
		if (procedure == null) {			
			procedure = SchemaFactory.eINSTANCE.createProcedure();
			procedure.setName(procedureName);
			schema.getProcedures().add(procedure);
		}		
		
		return procedure;
	}
	
	public AreaProcedureCallSpecification createProcedureCallSpecification(SchemaArea area,
												 						   String procedureName, 
												 						   ProcedureCallTime callTime,
												 						   AreaProcedureCallFunction callFunction) {
		
		Procedure procedure = createProcedureIfNotExists(procedureName);
		
		AreaProcedureCallSpecification callSpecification =
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		area.getProcedures().add(callSpecification);
		callSpecification.setProcedure(procedure);
		
		Assertions.isNotNull(callTime, ProcedureCallTime.class);
		callSpecification.setCallTime(callTime);
		Assertions.isNotNull(callFunction, AreaProcedureCallFunction.class);
		callSpecification.setFunction(callFunction);	
		
		return callSpecification;		
		
	}

	RecordProcedureCallSpecification createProcedureCallSpecification(SchemaRecord record,
																	  String procedureName,
																	  ProcedureCallTime callTime,
																	  RecordProcedureCallVerb callVerb) {

		Procedure procedure = createProcedureIfNotExists(procedureName);
		
		RecordProcedureCallSpecification callSpecification =
				SchemaFactory.eINSTANCE
							 .createRecordProcedureCallSpecification();
		record.getProcedures().add(callSpecification);
		callSpecification.setProcedure(procedure);
		
		Assertions.isNotNull(callTime, ProcedureCallTime.class);
		callSpecification.setCallTime(callTime);
		Assertions.isNotNull(callVerb, RecordProcedureCallVerb.class);
		callSpecification.setVerb(callVerb);	
		
		return callSpecification;
	}	
	
	SchemaRecord createRecord(String name, short recordId, 
			  				  StorageMode storageMode, LocationMode locationMode, 
			  				  String viaSetName, String areaName) {
		
		return createRecord(name, recordId, storageMode, locationMode, 
							viaSetName, areaName, false);
	}
	
	SchemaRecord createRecord(String name, short recordId, 
							  StorageMode storageMode, LocationMode locationMode, 
							  String viaSetName, String areaName, 
							  boolean ddlcatlod) {
		
		// convert the name to uppercase and validate it; if we're cloning
		// DDLCATLOD entities, the name is considered to be OK
		String recordName;
		if (!ddlcatlod) {
			recordName = 
				toUppercaseWithValidation(name, 
									  	  NamingConventions.Type.RECORD_NAME);
		} else {
			recordName = name;
		}
		
		// make sure the record does not yet exist
		if (schema.getRecord(recordName) != null) {
			throw new IllegalArgumentException("duplicate record name: " +
											   recordName);
		}		
		
		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		record.setName(recordName);
		schema.getRecords().add(record);
		
		record.setId(recordId);
		Assertions.isNotNull(storageMode, StorageMode.class);
		record.setStorageMode(storageMode); 
		Assertions.isNotNull(locationMode, LocationMode.class);
		record.setLocationMode(locationMode);
		
		// check the name of the area in which the record is stored 
		String ucAreaName = 
			toUppercaseWithValidation(areaName, 
									  NamingConventions.Type.LOGICAL_AREA_NAME);		
		
		// add the record to the area (which must already be there)
		SchemaArea area = schema.getArea(ucAreaName);
		Assertions.isNotNull(area, "area not found: " + ucAreaName);		
		// record IDs can be duplicated across areas in the schema, however, 
		// record IDs must be unique for all records within one area
		if (area.getRecord(recordId) != null) {
			String message = 
				"record id must be unique for all records within one area: " + 
				recordId + "(record=" + record.getName() + ", area=" + 
				areaName;
			throw new RuntimeException(message);				
		}			
		
		createAreaSpecification(area, record);		
		
		if (locationMode == LocationMode.VIA) {
			
			// check the VIA set name and convert it to uppercase
			String ucViaSetName = 
				toUppercaseWithValidation(viaSetName, 
										  NamingConventions.Type.SET_NAME);
			
			// create the VIA specification
			createViaSpecification(record, ucViaSetName);
			
			// cache the VIA record for the set
			List<SchemaRecord> viaRecordsForSet;
			if (viaSetMembers.containsKey(ucViaSetName)) {
				viaRecordsForSet = viaSetMembers.get(ucViaSetName);
			} else {
				viaRecordsForSet = new ArrayList<>();
				viaSetMembers.put(ucViaSetName, viaRecordsForSet);
			}
			viaRecordsForSet.add(record);			
			
		}		
		
		return record;
	}	
	
	Schema createSchema(String name, short version) {
		
		// instantiate a Schema and set some of its key attributes
		schema = SchemaFactory.eINSTANCE.createSchema();
		schema.setName(name);
		schema.setVersion(version);
		
		// diagram data with rulers (todo: set these attributes according to 
		// the editor's preferences)...
		DiagramData diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		schema.setDiagramData(diagramData);
		Ruler verticalRuler = SchemaFactory.eINSTANCE.createRuler();
		verticalRuler.setType(RulerType.VERTICAL);
		diagramData.setVerticalRuler(verticalRuler);
		diagramData.getRulers().add(verticalRuler); // ruler container
		Ruler horizontalRuler = SchemaFactory.eINSTANCE.createRuler();
		horizontalRuler.setType(RulerType.HORIZONTAL);
		diagramData.setHorizontalRuler(horizontalRuler);
		diagramData.getRulers().add(horizontalRuler); // ruler container
		
		return schema;
		
	}
	
	Set createSet(String name, SetMode mode, SetOrder order) {
		
		// have name converted to uppercase and validate it
		String setName = 
			toUppercaseWithValidation(name, NamingConventions.Type.SET_NAME);		
		
		// make sure the set does not yet exist
		Assertions.isNull(schema.getSet(setName), 
						  "duplicate set name: " + setName);
		
		// create the set in the schema and set its name
		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(setName);
		
		// set the set's mode, make sure it's not null
		Assertions.isNotNull(mode, SetMode.class);
		set.setMode(mode);
		
		// set the set's order, make sure it's not null		
		Assertions.isNotNull(order, SetOrder.class);
		set.setOrder(order);		
		
		return set;
		
	}
	
	MemberRole createSetMember(Set set, String recordName,
			   				   SetMembershipOption membershipOption) {
		
		return createSetMember(set, recordName, membershipOption, false);
	}
	
	MemberRole createSetMember(Set set, String recordName,
							   SetMembershipOption membershipOption, 
							   boolean ddlcatlod) {
		
		// convert the record name to uppercase and validate it; if we're 
		// cloning DDLCATLOD entities, the record name is considered to be OK
		String memberRecordName;
		if (!ddlcatlod) {
			memberRecordName = 
				toUppercaseWithValidation(recordName, 
									  	  NamingConventions.Type.RECORD_NAME);
		} else {
			memberRecordName = recordName;
		}		
		
		// locate the member record
		SchemaRecord record = schema.getRecord(memberRecordName);
		Assertions.isNotNull(record, 
							 "set member not found: " + memberRecordName);		
		
		// verify that the member record is not already a member of this set
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().getName().equals(set.getName())) {
				String message =
					"record is already a member of this set: " + set.getName() + 
					"/" + memberRecordName;
				throw new RuntimeException(message);
			}
		}		
		
		// create the member role
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		record.getMemberRoles().add(memberRole);
		Assertions.isNotNull(membershipOption, SetMembershipOption.class);
		memberRole.setMembershipOption(membershipOption);
		
		// if the record is stored VIA the set, set the set in the via 
		// specification
		ViaSpecification viaSpecification = record.getViaSpecification();		
		if (viaSpecification != null && 
			viaSetMembers.containsKey(set.getName()) &&
			viaSetMembers.get(set.getName()).contains(record)) {
			
			// the member record is stored via the set, set the via 
			// specification's set 
			viaSpecification.setSet(set);
			
			// remove the record from the cache list and remove the list from 
			// the cache if it's empty
			List<SchemaRecord> viaRecords = viaSetMembers.get(set.getName());
			if (viaRecords.size() > 1) {
				// remove the record from the cache list
				viaRecords.remove(record);
			} else {
				// remove the list from the cache because it would become empty
				viaSetMembers.remove(set.getName());
			}
			
		}
		
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
	
		// create the ConnectionLabel for the MemberRole; this will contain the
		// diagram location for the set member's set label
		ConnectionLabel connectionLabel = 
			SchemaFactory.eINSTANCE.createConnectionLabel();
		schema.getDiagramData().getConnectionLabels().add(connectionLabel);				
		memberRole.setConnectionLabel(connectionLabel);
		
		return memberRole;
	}	
	
	OwnerRole createSetOwner(Set set, String recordName) {
		return createSetOwner(set, recordName, false);
	}
		
	OwnerRole createSetOwner(Set set, String recordName, boolean ddlcatlod) {	
		
		// convert the record name to uppercase and validate it; if we're 
		// cloning DDLCATLOD entities, the record name is considered to be OK
		String ownerRecordName;
		if (!ddlcatlod) {
			ownerRecordName = 
				toUppercaseWithValidation(recordName, 
									  	  NamingConventions.Type.RECORD_NAME);
		} else {
			ownerRecordName = recordName;
		}
		
		// locate the owner record
		SchemaRecord record = schema.getRecord(ownerRecordName);
		Assertions.isNotNull(record, "set owner not found: " + ownerRecordName);
		
		// create the owner role and connect it with the set and owner record
		OwnerRole ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);
		record.getOwnerRoles().add(ownerRole);
		
		return ownerRole;
	}	
	
	SystemOwner createSystemOwner(Set set, String areaName) {
		
		Assertions.isNotNull(areaName, "area name is null");
		
		String ucAreaName = 
			toUppercaseWithValidation(areaName, 
									  NamingConventions.Type.LOGICAL_AREA_NAME);
		
		SystemOwner systemOwner = SchemaFactory.eINSTANCE.createSystemOwner();
		set.setSystemOwner(systemOwner);
		
		// get the area which must be there already		
		SchemaArea area = schema.getArea(ucAreaName);
		Assertions.isNotNull(area, "area not found: " + ucAreaName);
		
		// create the area specification
		createAreaSpecification(area, systemOwner);		
		
		return systemOwner;		
		
	}
	
	private final ViaSpecification createViaSpecification(SchemaRecord record,
	  	    											  String setName) {		

		ViaSpecification viaSpecification = 
			SchemaFactory.eINSTANCE.createViaSpecification();
		record.setViaSpecification(viaSpecification);		
		
		return viaSpecification;
	}	
	
	boolean isAllViaSetsResolved() {
		if (!viaSetMembers.isEmpty()) {
			System.out.println("unresolved VIA sets: " + viaSetMembers.keySet());
		}
		return viaSetMembers.isEmpty();
	}
}