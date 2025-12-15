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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
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
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

class ModelFactory {
	private Schema schema; // the schema we're working on
	private final Map<String, List<SchemaRecord>> viaSetMembers = new HashMap<>(); // cache for resolving VIA records and their VIA sets

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

	private static String toUppercaseWithValidation(String name, NamingConventions.Type type, boolean ignoreNamingConventions) {
		var p = type.toString().toLowerCase().replace("_", " ");
		Assertions.isNotNull(name, p + " is null");
		var uName = name.trim().toUpperCase();
		if (!ignoreNamingConventions) {
			var validationResult = NamingConventions.validate(uName, type);
			if (validationResult.getStatus() != ValidationResult.Status.OK) {
				var message = "invalid " + p + ": " + uName + " (" + validationResult.getMessage() + ")";
				throw new IllegalArgumentException(message);
			}
		}
		return uName;
	}	
	
	ModelFactory(Schema schema) {
		this.schema = schema;
	}

	SchemaArea createArea(String name) {
		var areaName = toUppercaseWithValidation(name, NamingConventions.Type.LOGICAL_AREA_NAME, false);
		if (schema.getArea(areaName) != null) {
			throw new IllegalArgumentException("duplicate area name: " + areaName);
		}
		var area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(areaName);												
		schema.getAreas().add(area);
		return area;
	}	
	
	AreaSpecification createAreaSpecification(SchemaArea area, SchemaRecord schemaRecord) {
		var areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		schemaRecord.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		return areaSpecification;
	}	
	
	AreaSpecification createAreaSpecification(SchemaArea area, SystemOwner systemOwner) {
		var areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		systemOwner.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		return areaSpecification;
	}	
	
	Element createElement(SchemaRecord schemaRecord, Element parent, String name, String baseName) {
		var elementName = toUppercaseWithValidation(name, NamingConventions.Type.ELEMENT_NAME, false);
		var baseElementName = toUppercaseWithValidation(baseName, NamingConventions.Type.ELEMENT_NAME, false);
			
		if (!elementName.equals("FILLER")) {
			Assertions.isNull(schemaRecord.getElement(elementName), "duplicate element name: " + elementName);
		}
				
		var element = SchemaFactory.eINSTANCE.createElement();
		element.setName(name);
		element.setBaseName(baseElementName); // null or different from name
		schemaRecord.getElements().add(element);		
		
		// if the element is a root element, add it to the record's root element list, otherwise, add it to the
		// parent's list of children
		if (parent == null) {
			schemaRecord.getRootElements().add(element);
		} else {
			parent.getChildren().add(element);
		}
		return element;
	}	
	
	IndexedSetModeSpecification createIndexedSetModeSpecification(Set set, String symbolicIndexName, Short keyCount, Short displacementPageCount) {
		var indexedSetModeSpecification = SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();
		set.setIndexedSetModeSpecification(indexedSetModeSpecification);
		
		if (symbolicIndexName != null) {
			var ucSymbolicIndexName = toUppercaseWithValidation(symbolicIndexName, NamingConventions.Type.SYMBOLIC_INDEX_NAME, false);
			indexedSetModeSpecification.setSymbolicIndexName(ucSymbolicIndexName);
		} else {
			// key count must be an unsigned integer in the range 3 through 8180
			Assertions.isNotNull(keyCount, "key count is null (set=" + set.getName() + ")");
			if (keyCount.shortValue() < 3 || keyCount.shortValue() > 8180) {
				throw new IllegalArgumentException("key count is invalid: " + keyCount.shortValue() + " (set=" + set.getName() + ")");
			}
			indexedSetModeSpecification.setKeyCount(keyCount);
						
			if (displacementPageCount != null) {
				// displacementPageCount must be an unsigned integer in the range 0 through 32,767 (32767 is the
				// maximum value for a short, so we don't need to check that)
				if (displacementPageCount.shortValue() < 0) {
					throw new IllegalArgumentException("invalid displacement page count: " + displacementPageCount.shortValue() +
							" (set=" + set.getName() + ")");
				}
				indexedSetModeSpecification.setDisplacementPageCount(displacementPageCount);
			}			
		}		
		return indexedSetModeSpecification;
	}	
	
	Key createKey(SchemaRecord schemaRecord, DuplicatesOption duplicatesOption, boolean naturalSequence) {
		var key = SchemaFactory.eINSTANCE.createKey();
		schemaRecord.setCalcKey(key);
		schemaRecord.getKeys().add(key);
		
		Assertions.isNotNull(duplicatesOption, DuplicatesOption.class);
		key.setDuplicatesOption(duplicatesOption);
		key.setNaturalSequence(naturalSequence);
		return key;
	}	
	
	Key createKey(MemberRole memberRole, DuplicatesOption duplicatesOption, boolean naturalSequence) {
		var key = SchemaFactory.eINSTANCE.createKey();
		memberRole.setSortKey(key);
		memberRole.getRecord().getKeys().add(key);
	
		Assertions.isNotNull(duplicatesOption, DuplicatesOption.class);
		key.setDuplicatesOption(duplicatesOption);
		key.setNaturalSequence(naturalSequence);
		return key;
	}	
	
	KeyElement createKeyElement(Key key, String elementName, SortSequence sortSequence) {
		var keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		key.getElements().add(keyElement);
				
		if (elementName != null) {
			var keyElementName = toUppercaseWithValidation(elementName, NamingConventions.Type.ELEMENT_NAME, false);
			
			// No element named FILLER can be used in a key
			if (keyElementName.equals("FILLER")) {
				throw new IllegalArgumentException("No element named FILLER can be used in a key");
			}
			
			// fetch the element, make sure it exists
			var element = key.getRecord().getElement(keyElementName);
			Assertions.isNotNull(element, "element not found: " + keyElementName);
			
			// occurs check
			if (isOccursInvolved(element)) {
				throw new IllegalArgumentException("No repeating element and no element subordinate to a " +
						"repeating element can be used in a key");
			}
			
			keyElement.setElement(element);
			
			var keyLength = key.getElements().stream()
					.map(KeyElement::getElement)
					.mapToInt(Element::getLength)
					.sum();
			if (keyLength > 256) {
				throw new IllegalArgumentException("The combined lengths of the elements in a key must not exceed 256 bytes");
			}
		} else {
			// set is sorted by dbkey; just check if the key contains exactly 1 key element
			Assertions.isSingleElementCollection(key.getElements(), "logic error: only 1 key element allowed when sorted by dbkey");
		}
		
		Assertions.isNotNull(sortSequence, SortSequence.class);
		keyElement.setSortSequence(sortSequence);
		
		return keyElement;
	}	
	
	OccursSpecification createOccursSpecification(Element element) {
		var occursSpecification = SchemaFactory.eINSTANCE.createOccursSpecification();
		element.setOccursSpecification(occursSpecification);
		return occursSpecification;
	}
	
	OffsetExpression createOffsetExpression(AreaSpecification areaSpecification, String symbolicSubareaName,
			Integer offsetPageCount, Short offsetPercent, Integer pageCount, Short percent) {
		
		// no offset expression is created if a symbolic subarea name is provided; the symbolic subarea name is
		// set directly in the area specification
		if (symbolicSubareaName != null) {
			var ucSymbolicSubareaName = toUppercaseWithValidation(symbolicSubareaName, NamingConventions.Type.SYMBOLIC_DISPLACEMENT, false);
			areaSpecification.setSymbolicSubareaName(ucSymbolicSubareaName);
			return null;
		}
		
		// only create an offset expression when at least 1 of its attributes is available
		if (offsetPageCount != null || offsetPercent != null  || pageCount != null || percent != null ) {
			var offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
			areaSpecification.setOffsetExpression(offsetExpression);
			setOffsetPageCountOrPercentIfApplicable(offsetExpression, offsetPageCount, offsetPercent);
			setPageCountOrPercentIfApplicable(offsetExpression, pageCount, percent);			
			return offsetExpression;
		} else {
			return null;
		}
	}
	
	private void setOffsetPageCountOrPercentIfApplicable(OffsetExpression target, Integer offsetPageCount, Short offsetPercent) {
		if (offsetPageCount != null) {
			// Offset-page-count must be an integer in the range 0 through the number of pages in physical-area-name
			// minus 1 - we don't have the number of pages in the physical area, so we cannot check that.
			var i = offsetPageCount.intValue();
			if (i < 0) {
				throw new IllegalArgumentException("invalid offset page count");
			}
			target.setOffsetPageCount(offsetPageCount);					
		} else if (offsetPercent != null) {
			// Offset-percent must be an integer in the range 0 through 100.
			var i = offsetPercent.shortValue();
			if (i < 0 || i > 100) {
				throw new IllegalArgumentException("invalue offset percent");
			}
			target.setOffsetPercent(offsetPercent);
		} else {
			// set the default value for the offset page count: 0
			target.setOffsetPageCount(Integer.valueOf(0));
		}		
	}
	
	private void setPageCountOrPercentIfApplicable(OffsetExpression target, Integer pageCount, Short percent) {
		if (pageCount != null) {
			// we'll accept any value bigger than zero
			var i = pageCount.intValue();
			if (i < 1) {
				throw new IllegalArgumentException("invalid page count");
			}
			target.setPageCount(pageCount);
		} else if (percent != null) {
			// Percent must be an integer in the range 1 through 100.
			var i = percent.shortValue();
			if (i < 1 || i > 100) {
				throw new IllegalArgumentException("invalid percent");
			}
			target.setPercent(percent);				
		} else {
			// set the default value for the percent: 100
			target.setPercent(Short.valueOf((short) 100));
		}
	}
	
	private Procedure createProcedureIfNotExists(String name) {
		var procedureName = toUppercaseWithValidation(name, NamingConventions.Type.PROCEDURE_NAME, false);
		var procedure = schema.getProcedure(procedureName);
		if (procedure == null) {			
			procedure = SchemaFactory.eINSTANCE.createProcedure();
			procedure.setName(procedureName);
			schema.getProcedures().add(procedure);
		}
		return procedure;
	}
	
	public AreaProcedureCallSpecification createProcedureCallSpecification(SchemaArea area, String procedureName,
			ProcedureCallTime callTime, AreaProcedureCallFunction callFunction) {
		
		var procedure = createProcedureIfNotExists(procedureName);
		
		var callSpecification = SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();
		area.getProcedures().add(callSpecification);
		callSpecification.setProcedure(procedure);
		
		Assertions.isNotNull(callTime, ProcedureCallTime.class);
		callSpecification.setCallTime(callTime);
		Assertions.isNotNull(callFunction, AreaProcedureCallFunction.class);
		callSpecification.setFunction(callFunction);	
		
		return callSpecification;		
	}

	RecordProcedureCallSpecification createProcedureCallSpecification(SchemaRecord schemaRecord, String procedureName,
			ProcedureCallTime callTime, RecordProcedureCallVerb callVerb) {

		var procedure = createProcedureIfNotExists(procedureName);
		
		var callSpecification = SchemaFactory.eINSTANCE.createRecordProcedureCallSpecification();
		schemaRecord.getProcedures().add(callSpecification);
		callSpecification.setProcedure(procedure);
		
		Assertions.isNotNull(callTime, ProcedureCallTime.class);
		callSpecification.setCallTime(callTime);
		Assertions.isNotNull(callVerb, RecordProcedureCallVerb.class);
		callSpecification.setVerb(callVerb);	
		
		return callSpecification;
	}	
	
	SchemaRecord createRecord(String name, short recordId, StorageMode storageMode, LocationMode locationMode,
			String viaSetName, String areaName) {
		
		return createRecord(name, recordId, storageMode, locationMode, viaSetName, areaName, false);
	}
	
	SchemaRecord createRecord(String name, short recordId, StorageMode storageMode, LocationMode locationMode,
			String viaSetName, String areaName, boolean ddlcatlod) {
				
		String recordName;
		if (!ddlcatlod) {
			recordName = toUppercaseWithValidation(name, NamingConventions.Type.RECORD_NAME, false);
		} else {
			// if we're cloning DDLCATLOD entities, the name is considered to be OK
			recordName = name;
		}
		
		// make sure the record does not yet exist
		if (schema.getRecord(recordName) != null) {
			throw new IllegalArgumentException("duplicate record name: " + recordName);
		}		
		
		var schemaRecord = SchemaFactory.eINSTANCE.createSchemaRecord();
		schemaRecord.setName(recordName);
		schema.getRecords().add(schemaRecord);
		
		schemaRecord.setId(recordId);
		Assertions.isNotNull(storageMode, StorageMode.class);
		schemaRecord.setStorageMode(storageMode); 
		Assertions.isNotNull(locationMode, LocationMode.class);
		schemaRecord.setLocationMode(locationMode);
		
		// check the name of the area in which the record is stored 
		var ucAreaName = toUppercaseWithValidation(areaName, NamingConventions.Type.LOGICAL_AREA_NAME, false);
		
		// add the record to the area (which must already be there)
		var area = schema.getArea(ucAreaName);
		Assertions.isNotNull(area, "area not found: " + ucAreaName);		
		// record IDs can be duplicated across areas in the schema, however, record IDs must be unique for all
		// records within one area
		if (area.getRecord(recordId) != null) {
			var message = "record id must be unique for all records within one area: " + recordId + "(record=" +
					schemaRecord.getName() + ", area=" + areaName;
			throw new IllegalArgumentException(message);
		}			
		
		createAreaSpecification(area, schemaRecord);		
		
		if (locationMode == LocationMode.VIA) {
			var ucViaSetName = toUppercaseWithValidation(viaSetName, NamingConventions.Type.SET_NAME, false);
			
			// create the VIA specification
			createViaSpecification(schemaRecord);
			
			// cache the VIA record for the set
			var viaRecordsForSet = viaSetMembers.putIfAbsent(ucViaSetName, new ArrayList<>());
			viaRecordsForSet.add(schemaRecord);
		}		
		return schemaRecord;
	}	
	
	Schema createSchema(String name, short version) {
		// instantiate a Schema and set some of its key attributes
		schema = SchemaFactory.eINSTANCE.createSchema();
		schema.setName(name);
		schema.setVersion(version);
		
		// diagram data with rulers
		var diagramData = SchemaFactory.eINSTANCE.createDiagramData();
		schema.setDiagramData(diagramData);
		var verticalRuler = SchemaFactory.eINSTANCE.createRuler();
		verticalRuler.setType(RulerType.VERTICAL);
		diagramData.setVerticalRuler(verticalRuler);
		diagramData.getRulers().add(verticalRuler); // ruler container
		var horizontalRuler = SchemaFactory.eINSTANCE.createRuler();
		horizontalRuler.setType(RulerType.HORIZONTAL);
		diagramData.setHorizontalRuler(horizontalRuler);
		diagramData.getRulers().add(horizontalRuler); // ruler container
		
		return schema;
	}
	
	Set createSet(String name, SetMode mode, SetOrder order) {
		var setName = toUppercaseWithValidation(name, NamingConventions.Type.SET_NAME, false);
		
		// make sure the set does not yet exist
		Assertions.isNull(schema.getSet(setName), "duplicate set name: " + setName);
		
		// create the set in the schema and set its name
		var set = SchemaFactory.eINSTANCE.createSet();
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
	
	MemberRole createSetMember(Set set, String recordName, SetMembershipOption membershipOption) {
		return createSetMember(set, recordName, membershipOption, false);
	}
	
	MemberRole createSetMember(Set set, String recordName, SetMembershipOption membershipOption, boolean ddlcatlod) {
		String memberRecordName;
		if (!ddlcatlod) {
			memberRecordName = toUppercaseWithValidation(recordName, NamingConventions.Type.RECORD_NAME, false);
		} else {
			// if we're cloning DDLCATLOD entities, the record name is considered to be OK
			memberRecordName = recordName;
		}		
		
		// locate the member record
		var schemaRecord = schema.getRecord(memberRecordName);
		Assertions.isNotNull(schemaRecord, "set member not found: " + memberRecordName);
		
		// verify that the member record is not already a member of this set
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().getName().equals(set.getName())) {
				var message = "record is already a member of this set: " + set.getName() + "/" + memberRecordName;
				throw new IllegalArgumentException(message);
			}
		}		
				
		var memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		schemaRecord.getMemberRoles().add(memberRole);
		Assertions.isNotNull(membershipOption, SetMembershipOption.class);
		memberRole.setMembershipOption(membershipOption);
		
		// if the record is stored VIA the set, set the set in the via specification
		var viaSpecification = schemaRecord.getViaSpecification();		
		if (viaSpecification != null && viaSetMembers.containsKey(set.getName()) && viaSetMembers.get(set.getName()).contains(schemaRecord)) {
			// the member record is stored via the set 
			viaSpecification.setSet(set);
			
			// remove the record from the cache list and remove the list from the cache if it's empty
			var viaRecords = viaSetMembers.get(set.getName());
			if (viaRecords.size() > 1) {
				// remove the record from the cache list
				viaRecords.remove(schemaRecord);
			} else {
				// remove the list from the cache because it would become empty
				viaSetMembers.remove(set.getName());
			}
		}
		
		// create the Connection for the MemberRole; a Connection represents a line in the diagram...		
		var connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.getSet().getSchema().getDiagramData().getConnectionParts().add(connectionPart);
	
		// create the ConnectionLabel for the MemberRole; this will contain the diagram location for the set member's set label
		var connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();
		schema.getDiagramData().getConnectionLabels().add(connectionLabel);				
		memberRole.setConnectionLabel(connectionLabel);
		
		return memberRole;
	}	
	
	OwnerRole createSetOwner(Set set, String recordName) {
		return createSetOwner(set, recordName, false);
	}
		
	OwnerRole createSetOwner(Set set, String recordName, boolean ddlcatlod) {
		String ownerRecordName;
		if (!ddlcatlod) {
			ownerRecordName = toUppercaseWithValidation(recordName, NamingConventions.Type.RECORD_NAME, false);
		} else {
			// if we're cloning DDLCATLOD entities, the record name is considered to be OK
			ownerRecordName = recordName;
		}
		
		// locate the owner record
		var schemaRecord = schema.getRecord(ownerRecordName);
		Assertions.isNotNull(schemaRecord, "set owner not found: " + ownerRecordName);
		
		// create the owner role and connect it with the set and owner record
		var ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);
		schemaRecord.getOwnerRoles().add(ownerRole);
		
		return ownerRole;
	}	
	
	SystemOwner createSystemOwner(Set set, String areaName) {
		Assertions.isNotNull(areaName, "area name is null");
		
		var ucAreaName = toUppercaseWithValidation(areaName, NamingConventions.Type.LOGICAL_AREA_NAME, false);
		
		var systemOwner = SchemaFactory.eINSTANCE.createSystemOwner();
		set.setSystemOwner(systemOwner);
		
		// get the area which must be there already		
		var area = schema.getArea(ucAreaName);
		Assertions.isNotNull(area, "area not found: " + ucAreaName);
				
		createAreaSpecification(area, systemOwner);		
		
		return systemOwner;		
	}
	
	private final ViaSpecification createViaSpecification(SchemaRecord schemaRecord) {
		var viaSpecification = SchemaFactory.eINSTANCE.createViaSpecification();
		schemaRecord.setViaSpecification(viaSpecification);
		return viaSpecification;
	}	
	
	boolean isAllViaSetsResolved() {
		if (!viaSetMembers.isEmpty()) {
			Plugin.getDefault().getLog().warn("unresolved VIA sets: " + viaSetMembers.keySet());
		}
		return viaSetMembers.isEmpty();
	}
	
}
