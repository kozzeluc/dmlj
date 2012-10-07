package org.lh.dmlj.schema.editor.importtool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * todo:  provide a private validate() method annotated with a (new) @Validator
 * annotation - 1 check to be done is that the viaSetMembers cache is empty,
 * indicating that all via specifications were connected to their set
 */
public abstract class AbstractSchemaImportTool {

	@org.lh.dmlj.schema.editor.importtool.annotation.Schema
	private Schema 						    schema;
	// a cache for resolving VIA records and their VIA sets:
	private Map<String, List<SchemaRecord>> viaSetMembers = new HashMap<>();
	
	public AbstractSchemaImportTool() {
		super();
	}
	
	protected final SchemaArea createArea(String name) {
		// todo: make sure the area does not already exist
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(name);												
		schema.getAreas().add(area);
		return area;
	}	
	
	protected final Element createElement(SchemaRecord record, String name) {
		Element element = SchemaFactory.eINSTANCE.createElement();
		element.setName(name);
		record.getElements().add(element);		
		record.getRootElements().add(element);
		return element;
	}	
	
	protected final AreaSpecification createAreaSpecification(SchemaArea area, 
										  			    	  SchemaRecord record) {
		
		// todo: make sure no area specification is already assigned to the 
		// record
		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		record.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		return areaSpecification;
	}
	
	protected final AreaSpecification createAreaSpecification(SchemaArea area, 
			    											  SystemOwner systemOwner) {
	
		AreaSpecification areaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		systemOwner.setAreaSpecification(areaSpecification);
		area.getAreaSpecifications().add(areaSpecification);
		return areaSpecification;
	}

	protected final IndexedSetModeSpecification createIndexedSetModeSpecification(Set set) {
		// todo: make sure the set is indexed and does not already have an
		// indexed set mode specification
		IndexedSetModeSpecification indexedSetModeSpecification =
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();		
		set.setIndexedSetModeSpecification(indexedSetModeSpecification);
		return indexedSetModeSpecification;
	}
	
	protected final Key createKey(MemberRole memberRole) {
		// todo: make sure no sort key is already defined for the member role
		// and that the set's order is sorted 
		Key key = SchemaFactory.eINSTANCE.createKey();
		memberRole.setSortKey(key);		
		memberRole.getRecord().getKeys().add(key);
		return key;
	}
	
	protected final Key createKey(SchemaRecord record) {
		// todo: make sure that no CALC key is already defined for the record 
		// and that the record's location mode is CALC
		Key key = SchemaFactory.eINSTANCE.createKey();
		record.setCalcKey(key);
		record.getKeys().add(key);
		return key;
	}
	
	protected final KeyElement createKeyElement(Key key) {
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		key.getElements().add(keyElement);
		return keyElement;
	}
	
	protected final OccursSpecification createOccursSpecification(Element element) {
		// todo: make sure the record does not already have an occurs 
		// specification
		OccursSpecification occursSpecification =
			SchemaFactory.eINSTANCE.createOccursSpecification();
		element.setOccursSpecification(occursSpecification);
		return occursSpecification;
	}

	protected final OffsetExpression createOffsetExpression(AreaSpecification areaSpecification) {
		// todo: make sure no offset expression is already set in the area 
		// specification
		OffsetExpression offsetExpression = 
			SchemaFactory.eINSTANCE.createOffsetExpression();
		areaSpecification.setOffsetExpression(offsetExpression);
		return offsetExpression;
	}

	protected final Procedure createProcedure(String name) {
		// todo: make sure the procedure does not already exist
		Procedure procedure = SchemaFactory.eINSTANCE.createProcedure();
		procedure.setName(name);	
		schema.getProcedures().add(procedure);
		return procedure;
	}
	
	protected final AreaProcedureCallSpecification createProcedureCallSpecification(SchemaArea area,
																			  	    Procedure procedure) {
		
		AreaProcedureCallSpecification callSpec =
			SchemaFactory.eINSTANCE.createAreaProcedureCallSpecification();			
		area.getProcedures().add(callSpec);
		callSpec.setProcedure(procedure);
		return callSpec;
	}
	
	protected final RecordProcedureCallSpecification createProcedureCallSpecification(SchemaRecord record,
																				Procedure procedure) {
		
		RecordProcedureCallSpecification callSpecification =
			SchemaFactory.eINSTANCE
						 .createRecordProcedureCallSpecification();
		record.getProcedures().add(callSpecification);
		callSpecification.setProcedure(procedure);
		return callSpecification;
	}

	protected final SchemaRecord createRecord(String name) {
		// todo: make sure the record does not already exist
		SchemaRecord record = SchemaFactory.eINSTANCE.createSchemaRecord();
		record.setName(name);
		schema.getRecords().add(record);
		return record;
	}
	
	protected final Set createSet(String name) {
		// todo: make sure the set does not already exist
		Set set = SchemaFactory.eINSTANCE.createSet();
		schema.getSets().add(set);
		set.setName(name);
		return set;
	}
	
	protected final MemberRole createSetMember(Set set, SchemaRecord record) {
		
		// todo: make sure the set does not already have a member if it is
		// indexed + if the record is stored via the set, its via specification
		// should not have its set set yet
		MemberRole memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		set.getMembers().add(memberRole);
		record.getMemberRoles().add(memberRole);
		
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
	
	protected final OwnerRole createSetOwner(Set set, SchemaRecord record) {
		// to do: make sure the set does not already have a(n) (system) owner
		OwnerRole ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		set.setOwner(ownerRole);
		record.getOwnerRoles().add(ownerRole);
		return ownerRole;
	}
	
	protected final SystemOwner createSystemOwner(Set set) {
		// todo: make sure the set is indexed and does not already have a(n)
		// (system) owner
		SystemOwner systemOwner = SchemaFactory.eINSTANCE.createSystemOwner();
		set.setSystemOwner(systemOwner);
		return systemOwner;
	}
	
	protected final ViaSpecification createViaSpecification(SchemaRecord record,
													  	    String setName) {
		
		// todo: make sure the record has a location mode of VIA and does not 
		// already have a via specification; the set in the via specification
		// should be null at this point as well and the set denoted by setName
		// should not yet exist
		
		ViaSpecification viaSpecification = 
			SchemaFactory.eINSTANCE.createViaSpecification();
		record.setViaSpecification(viaSpecification);
		
		// we cannot set the set in the via specification because the set has
		// not yet been created - cache the via record for the set
		List<SchemaRecord> viaRecordsForSet;
		if (viaSetMembers.containsKey(setName)) {
			viaRecordsForSet = viaSetMembers.get(setName);
		} else {
			viaRecordsForSet = new ArrayList<>();
			viaSetMembers.put(setName, viaRecordsForSet);
		}
		viaRecordsForSet.add(record);
		
		return viaSpecification;
	}
	
	/*protected Schema getSchema() {
		return schema;
	}*/

	protected final SchemaArea findArea(String name) {
		SchemaArea area = schema.getArea(name);
		if (area == null) {
			throw new IllegalArgumentException("area not found: " + name);
		} 
		return area;
	}
	
	protected final Procedure findProcedure(String name) {
		Procedure procedure = schema.getProcedure(name);
		if (procedure == null) {
			throw new IllegalArgumentException("procedure not found: " + name);
		} 
		return procedure;
	}
	
	protected final SchemaRecord findRecord(String name) {
		SchemaRecord record = schema.getRecord(name);
		if (record == null) {
			throw new IllegalArgumentException("record not found: " + name);
		} 
		return record;
	}
	
	protected final Set findSet(String name) {
		Set set = schema.getSet(name);
		if (set == null) {
			throw new IllegalArgumentException("set not found: " + name);
		} 
		return set;
	}	

	protected final boolean isIdmsntwk() {
		return schema.getName().equals("IDMSNTWK") && schema.getVersion() == 1;
	}

	public abstract void performTask(IDataEntryContext context, 
									 Properties parameters);
	
	protected void setSchemaDescription(String description) {
		schema.setDescription(description);
	}
	
	protected void setSchemaMemoDate(String memoDate) {
		schema.setMemoDate(memoDate);
	}
	
}