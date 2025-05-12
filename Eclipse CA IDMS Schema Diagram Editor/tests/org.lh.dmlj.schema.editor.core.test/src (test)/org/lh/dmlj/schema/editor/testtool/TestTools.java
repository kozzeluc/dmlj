/**
 * Copyright (C) 2015  Luc Hermans
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
package org.lh.dmlj.schema.editor.testtool;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gef.commands.Command;
import org.junit.Assert;
import org.lh.dmlj.schema.AreaProcedureCallSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.ProcedureCallSpecification;
import org.lh.dmlj.schema.RecordProcedureCallSpecification;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.ChangeSetOrderCommand;
import org.lh.dmlj.schema.editor.command.DeleteBendpointCommand;
import org.lh.dmlj.schema.editor.command.LockEndpointsCommand;
import org.lh.dmlj.schema.editor.command.MakeRecordDirectCommand;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.Prefix;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;

public abstract class TestTools {	
	
	private static final Schema UNTOUCHED_EMPSCHM;

	static {
		
		// reference the SchemaPackage instance to be able to read schemas from the file system
		@SuppressWarnings("unused")
		SchemaPackage schemaPackage = SchemaPackage.eINSTANCE;
		
		UNTOUCHED_EMPSCHM = getEmpschmSchema();
				
	}	
	
	public static void addProcedureCallSpecification(Procedure procedure, SchemaArea area) {		
		AreaProcedureCallSpecification callSpec = mock(AreaProcedureCallSpecification.class);
		when(callSpec.getArea()).thenReturn(area);
		when(callSpec.getProcedure()).thenReturn(procedure);
		if (area.getProcedures() == null) {
			// the area should be a mock object
			when(area.getProcedures()).thenReturn(new BasicEList<AreaProcedureCallSpecification>());	
		}
		area.getProcedures().add(callSpec);
		procedure.getCallSpecifications().add(callSpec);
	}

	public static void addProcedureCallSpecification(Procedure procedure, SchemaRecord record) {
		RecordProcedureCallSpecification callSpec = mock(RecordProcedureCallSpecification.class);
		when(callSpec.getRecord()).thenReturn(record);
		when(callSpec.getProcedure()).thenReturn(procedure);
		if (record.getProcedures() == null) {
			// the record should be a mock object
			when(record.getProcedures()).thenReturn(new BasicEList<RecordProcedureCallSpecification>());	
		}
		record.getProcedures().add(callSpec);
		procedure.getCallSpecifications().add(callSpec);
	}

	public static void addSourceAndTargetEndPoints(Set set) {
		Assert.assertEquals(1, set.getMembers().size());
		Assert.assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getTargetEndpointLocation());
		new LockEndpointsCommand(set.getMembers().get(0), new Point(0, 0), new Point(0, 0)).execute();
		assertNotNull(set.getMembers().get(0).getConnectionParts().get(0).getSourceEndpointLocation());
		assertNotNull(set.getMembers().get(0).getConnectionParts().get(0).getTargetEndpointLocation());
	}

	public static void addSourceEndPoint(Set set) {
		Assert.assertEquals(1, set.getMembers().size());
		Assert.assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getTargetEndpointLocation());
		new LockEndpointsCommand(set.getMembers().get(0), new Point(0, 0), null).execute();
		assertNotNull(set.getMembers().get(0).getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getTargetEndpointLocation());
	}

	public static void addTargetEndPoint(Set set) {
		Assert.assertEquals(1, set.getMembers().size());
		Assert.assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getSourceEndpointLocation());
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getTargetEndpointLocation());
		new LockEndpointsCommand(set.getMembers().get(0), null, new Point(0, 0)).execute();
		assertNull(set.getMembers().get(0).getConnectionParts().get(0).getSourceEndpointLocation());
		assertNotNull(set.getMembers().get(0).getConnectionParts().get(0).getTargetEndpointLocation());
	}

	public static ObjectGraph asObjectGraph(EObject root) {				
		return new ObjectGraph(root);			
	}

	public static Syntax asSyntax(Schema schema) {
		return new Syntax(schema);
	}

	public static Xmi asXmi(Schema schema) {				
		return new Xmi(schema);			
	}
	
	public static void assertConnectionLabelRemoved(Schema touchedSchema, String setName,
			 										String recordName) {
	
		MemberRole untouchedMemberRole = 
			(MemberRole) getRole(UNTOUCHED_EMPSCHM, recordName, setName);
	
		List<ConnectionLabel> expectedConnectionLabels = 
			new ArrayList<>(UNTOUCHED_EMPSCHM.getDiagramData().getConnectionLabels());
		expectedConnectionLabels.remove(untouchedMemberRole.getConnectionLabel());
	
		assertEquals(expectedConnectionLabels, touchedSchema.getDiagramData().getConnectionLabels(), 
					 new Asserter<ConnectionLabel>() {
	
			@Override
			public void doAssert(int itemIndex, ConnectionLabel expected, ConnectionLabel actual) {
				Assert.assertEquals(String.valueOf(itemIndex), 
									expected.getMemberRole().getSet().getName(),
									actual.getMemberRole().getSet().getName());				
			}			
		});
	
	}

	public static void assertConnectionPartsRemoved(Schema touchedSchema, String setName,
												 	String recordName) {
		
		MemberRole untouchedMemberRole = 
			(MemberRole) getRole(UNTOUCHED_EMPSCHM, recordName, setName);
		
		List<ConnectionPart> expectedConnectionParts = 
			new ArrayList<>(UNTOUCHED_EMPSCHM.getDiagramData().getConnectionParts());
		expectedConnectionParts.removeAll(untouchedMemberRole.getConnectionParts());
		
		assertEquals(expectedConnectionParts, touchedSchema.getDiagramData().getConnectionParts(), 
					 new Asserter<ConnectionPart>() {
	
			@Override
			public void doAssert(int itemIndex, ConnectionPart expected, ConnectionPart actual) {
				Assert.assertEquals(String.valueOf(itemIndex), 
									expected.getMemberRole().getSet().getName(),
									actual.getMemberRole().getSet().getName());
				Assert.assertEquals(String.valueOf(itemIndex), 
									expected.getMemberRole().getConnectionParts().indexOf(expected),
									actual.getMemberRole().getConnectionParts().indexOf(actual));
			}			
		});
		
	}

	public static void assertConnectorsRemoved(Schema touchedSchema, String setName, 
											   String recordName) {
		
		
		
	}

	public static void assertDiagramLocationsRemoved(Schema touchedSchema, String setName, 
													 String recordName) {
		
		Set untouchedSet = getSet(UNTOUCHED_EMPSCHM, setName);
		
		List<DiagramLocation> obsoleteDiagramLocations = new ArrayList<>();
		
		if (untouchedSet.getSystemOwner() != null) {
			obsoleteDiagramLocations.add(untouchedSet.getSystemOwner().getDiagramLocation());			
		}
		
		MemberRole untouchedMemberRole = 
			(MemberRole) getRole(UNTOUCHED_EMPSCHM, recordName, setName);
		obsoleteDiagramLocations.add(untouchedMemberRole.getConnectionLabel().getDiagramLocation());
		for (ConnectionPart untouchedConnectionPart : untouchedMemberRole.getConnectionParts()) {
			obsoleteDiagramLocations.addAll(untouchedConnectionPart.getBendpointLocations());
			if (untouchedConnectionPart.getSourceEndpointLocation() != null) {
				obsoleteDiagramLocations.add(untouchedConnectionPart.getSourceEndpointLocation());
			}
			if (untouchedConnectionPart.getTargetEndpointLocation() != null) {
				obsoleteDiagramLocations.add(untouchedConnectionPart.getTargetEndpointLocation());
			}
			if (untouchedConnectionPart.getConnector() != null) {
				obsoleteDiagramLocations.add(untouchedConnectionPart.getConnector().getDiagramLocation());
			}
		}
		
		List<DiagramLocation> expectedDiagramLocations = 
			new ArrayList<>(UNTOUCHED_EMPSCHM.getDiagramData().getLocations());
		expectedDiagramLocations.removeAll(obsoleteDiagramLocations);
		
		assertEquals(expectedDiagramLocations, touchedSchema.getDiagramData().getLocations(), 
					 new Asserter<DiagramLocation>() {
	
			@Override
			public void doAssert(int itemIndex, DiagramLocation expected, DiagramLocation actual) {				
				Assert.assertEquals(String.valueOf(itemIndex), expected.getX(), actual.getX());
				Assert.assertEquals(String.valueOf(itemIndex), expected.getY(), actual.getY());
				Assert.assertEquals(String.valueOf(itemIndex), expected.getEyecatcher(),
									actual.getEyecatcher());
			}			
		});
		
	}

	public static <T> void assertEquals(List<T> expected, List<T> actual, Asserter<T> asserter) {		
		Assert. assertEquals("internal error: ", expected.size(), actual.size());		
		for (int i = 0; i < actual.size(); i++) {
			T expectedItem = expected.get(i);
			T actualItem = actual.get(i);			
			asserter.doAssert(i, expectedItem, actualItem);			
		}		
	}

	private static void assertEquals(List<String> expectedLines, List<String> actualLines) {
		for (int i = 0; i < expectedLines.size() && i < actualLines.size(); i++) {
			if (!expectedLines.get(i).equals(actualLines.get(i))) {
				StringBuilder message = new StringBuilder();
				message.append("expected[" + i + "]: <");
				message.append(expectedLines.get(i));
				message.append("> but was[" + i + "]: <");
				message.append(actualLines.get(i));
				message.append(">");
				throw new AssertionError(message.toString());
			}
		}
		if (expectedLines.size() > actualLines.size()) {
			StringBuilder message = new StringBuilder();
			message.append("expected[" + (expectedLines.size() - 1) + "]: <");
			message.append(expectedLines.get(expectedLines.size() - 1));
			message.append("> but was: null>");						
			throw new AssertionError(message.toString());
		} else if (expectedLines.size() != actualLines.size()) {
			StringBuilder message = new StringBuilder();
			message.append("expected: null but was[" + (actualLines.size() - 1) + "]: <");
			message.append(actualLines.get(actualLines.size() - 1));
			message.append(">");
			throw new AssertionError(message.toString());
		}
	}

	public static void assertEquals(ObjectGraph expected, ObjectGraph actual) {
		List<String> expectedLines = expected.getLines(); 
		List<String> actualLines = actual.getLines();
		assertEquals(expectedLines, actualLines);
	}
	
	public static void assertEquals(Syntax expected, Syntax actual) {
		List<String> expectedLines = expected.getLines(); 
		List<String> actualLines = actual.getLines();
		assertEquals(expectedLines, actualLines);
	}
	
	public static void assertEquals(Xmi expected, Xmi actual) {
		List<String> expectedLines = expected.getLines(); 
		List<String> actualLines = actual.getLines();
		assertEquals(expectedLines, actualLines);
	}
	
	private static void assertMemberRoleRemoved(Schema touchedSchema, String recordName, 
			  								    String setName) {
	
		SchemaRecord untouchedRecord = getRecord(UNTOUCHED_EMPSCHM, recordName);
		SchemaRecord touchedRecord = getRecord(touchedSchema, recordName);
	
		Assert.assertEquals(untouchedRecord.getMemberRoles().size() - 1, 
							touchedRecord.getMemberRoles().size());
	
		List<MemberRole> expectedMemberRoles = new ArrayList<>();
		for (MemberRole memberRole : untouchedRecord.getMemberRoles()) {
			if (!memberRole.getSet().getName().equals(setName)) {
				expectedMemberRoles.add(memberRole);
			}
		}		
	
		assertEquals(expectedMemberRoles, touchedRecord.getMemberRoles(), new Asserter<MemberRole>() {
			@Override
			public void doAssert(int itemIndex, MemberRole expected, MemberRole actual) {
				Assert.assertEquals(String.valueOf(itemIndex), expected.getSet().getName(), 
									actual.getSet().getName());				
			}			
		});
		
	}

	private static void assertOwnerRoleRemoved(Schema touchedSchema, String recordName, 
											   String setName) {
		
		SchemaRecord untouchedRecord = getRecord(UNTOUCHED_EMPSCHM, recordName);
		SchemaRecord touchedRecord = getRecord(touchedSchema, recordName);
		
		Assert.assertEquals(untouchedRecord.getOwnerRoles().size() - 1, 
							touchedRecord.getOwnerRoles().size());
		
		List<OwnerRole> expectedOwnerRoles = new ArrayList<>();
		for (OwnerRole ownerRole : untouchedRecord.getOwnerRoles()) {
			if (!ownerRole.getSet().getName().equals(setName)) {
				expectedOwnerRoles.add(ownerRole);
			}
		}		
		
		assertEquals(expectedOwnerRoles, touchedRecord.getOwnerRoles(), new Asserter<OwnerRole>() {
			@Override
			public void doAssert(int itemIndex, OwnerRole expected, OwnerRole actual) {
				Assert.assertEquals(String.valueOf(itemIndex), expected.getSet().getName(), 
									actual.getSet().getName());					
			}			
		});
		
	}
	
	public static void assertPointersRemoved(Schema touchedSchema, String recordName, String setName) {		

		Prefix untouchedPrefix = getPrefix(UNTOUCHED_EMPSCHM, recordName);
		PointerType[] pointersToCheck = getPointerTypes(UNTOUCHED_EMPSCHM, recordName, setName);

		Prefix touchedPrefix = getPrefix(touchedSchema, recordName);
		Assert.assertEquals(untouchedPrefix.getPointers().size() - pointersToCheck.length, 
							touchedPrefix.getPointers().size());

		List<Pointer<?>> expectedPointers = new ArrayList<>();
		for (Pointer<?> pointer : untouchedPrefix.getPointers()) {
			if (!pointer.getSetName().equals(setName)) {
				expectedPointers.add(pointer);
			}
		}
		
		assertEquals(expectedPointers, touchedPrefix.getPointers(), new Asserter<Pointer<?>>() {
			@Override
			public void doAssert(int itemIndex, Pointer<?> expected, Pointer<?> actual) {
				Assert.assertSame(String.valueOf(itemIndex), expected.getType(), actual.getType());
				Assert.assertEquals(String.valueOf(itemIndex), expected.getSetName(), 
									actual.getSetName());
				Assert.assertEquals(String.valueOf(itemIndex), 
									Short.valueOf((short) (itemIndex + 1)), 
									actual.getCurrentPositionInPrefix());							
			}			
		});

	}	
	
	public static void assertSetRemoved(Schema touchedSchema, String setName) {
		Set set = getSet(UNTOUCHED_EMPSCHM, setName);
		Assert.assertEquals("set " + setName + " is a multiple-member set and all members but 1 " +
							"should be removed first", 1, set.getMembers().size());
		assertSetRemoved(touchedSchema, setName, set.getMembers().get(0).getRecord().getName());
	}
	
	public static void assertRecordRemovedFromSet(Schema touchedSchema, String setName, 
												  String recordName) {
		
		assertPointersRemoved(touchedSchema, recordName, setName);		
		assertMemberRoleRemoved(touchedSchema, recordName, setName);
		if (isSortedSet(UNTOUCHED_EMPSCHM, setName)) {
			assertSortKeyRemoved(touchedSchema, recordName, setName);
		}		
		assertConnectionPartsRemoved(touchedSchema, setName, recordName);		
		assertConnectionLabelRemoved(touchedSchema, setName, recordName);				
		assertDiagramLocationsRemoved(touchedSchema, setName, recordName);
		assertConnectorsRemoved(touchedSchema, setName, recordName);		
	}
	
	public static void assertSetRemoved(Schema touchedSchema, String setName, 
										String lastRemainingMemberRecordName) {
		
		assertNull(touchedSchema.getSet(setName));
		
		if (isUserOwnedSet(UNTOUCHED_EMPSCHM, setName)) {
			String ownerRecordName = 
				UNTOUCHED_EMPSCHM.getSet(setName).getOwner().getRecord().getName();
			assertPointersRemoved(touchedSchema, ownerRecordName, setName);
			assertOwnerRoleRemoved(touchedSchema, ownerRecordName, setName);
		} else {
			assertSystemOwnerRemoved(touchedSchema, setName);
		}
				
		assertPointersRemoved(touchedSchema, lastRemainingMemberRecordName, setName);		
		assertMemberRoleRemoved(touchedSchema, lastRemainingMemberRecordName, setName);
		if (isSortedSet(UNTOUCHED_EMPSCHM, setName)) {
			assertSortKeyRemoved(touchedSchema, lastRemainingMemberRecordName, setName);
		}		
		assertConnectionPartsRemoved(touchedSchema, setName, lastRemainingMemberRecordName);		
		assertConnectionLabelRemoved(touchedSchema, setName, lastRemainingMemberRecordName);				
		assertDiagramLocationsRemoved(touchedSchema, setName, lastRemainingMemberRecordName);
		assertConnectorsRemoved(touchedSchema, setName, lastRemainingMemberRecordName);
		
	}
	
	public static void assertSortKeyRemoved(Schema touchedSchema, String recordName, String setName) {		
		
		SchemaRecord untouchedRecord = getRecord(UNTOUCHED_EMPSCHM, recordName);
		SchemaRecord touchedRecord = getRecord(touchedSchema, recordName);		

		Assert.assertEquals(untouchedRecord.getKeys().size() - 1, touchedRecord.getKeys().size());

		Key obsoleteSortKey = null;
		List<Key> expectedKeys = new ArrayList<>();
		for (Key key : untouchedRecord.getKeys()) {
			if (!key.isCalcKey() && key.getMemberRole().getSet().getName().equals(setName)) {				
				obsoleteSortKey = key;
			} else {
				expectedKeys.add(key);
			}
		}
		assertNotNull(obsoleteSortKey);		
		
		assertEquals(expectedKeys, touchedRecord.getKeys(), new Asserter<Key>() {
			@Override
			public void doAssert(int itemIndex, Key expected, Key actual) {
				Assert.assertEquals(String.valueOf(itemIndex), 
									expected.isCalcKey(), actual.isCalcKey());
				if (!expected.isCalcKey()) {				
					Assert.assertEquals(String.valueOf(itemIndex), 
										expected.getMemberRole().getSet().getName(), 
						    			actual.getMemberRole().getSet().getName());
				}
			}			
		});
				
		List<Element> untouchedElements = getElements(obsoleteSortKey);
		for (Element untouchedElement : untouchedElements) {
			
			int elementIndex = untouchedRecord.getElements().indexOf(untouchedElement);
			assertNotEquals(-1, elementIndex);
			Element touchedElement = touchedRecord.getElements().get(elementIndex);
			Assert.assertEquals(untouchedElement.getName(), touchedElement.getName());
			
			List<KeyElement> expectedKeyElements = new ArrayList<>();
			for (KeyElement keyElement : untouchedElement.getKeyElements()) {
				Key untouchedKey = keyElement.getKey();
				if (untouchedKey.isCalcKey() || 
					!untouchedKey.getMemberRole().getSet().getName().equals(setName)) {					
					
					expectedKeyElements.add(keyElement);
				}
			}
			
			assertEquals(expectedKeyElements, touchedElement.getKeyElements(), new Asserter<KeyElement>() {
				@Override
				public void doAssert(int itemIndex, KeyElement expected, KeyElement actual) {
					Assert.assertEquals(String.valueOf(itemIndex), expected.getKey().isCalcKey(), 
										actual.getKey().isCalcKey());
					if (!expected.getKey().isCalcKey()) {
						Assert.assertEquals(String.valueOf(itemIndex), 
											expected.getKey().getMemberRole().getSet().getName(), 
										    actual.getKey().getMemberRole().getSet().getName());
					}
				}				
			});
			
		}
				
	}

	public static void assertSystemOwnerRemoved(Schema touchedSchema, String setName) {
		
		Set untouchedSet = getSet(UNTOUCHED_EMPSCHM, setName);
		SystemOwner untouchedSystemOwner = untouchedSet.getSystemOwner();
		String areaName = untouchedSystemOwner.getAreaSpecification().getArea().getName();
		
		SchemaArea untouchedArea = getArea(UNTOUCHED_EMPSCHM, areaName);
		
		if (untouchedArea.getIndexes().size() == 1) {
			assertNull(touchedSchema.getArea(areaName));
			return;
		} 
		
		SchemaArea touchedArea = touchedSchema.getArea(areaName);
		assertNotNull(touchedArea);
		Assert.assertEquals(untouchedArea.getIndexes().size() - 1, touchedArea.getIndexes().size());
		
		List<SystemOwner> expectedSystemOwners = new ArrayList<>(untouchedArea.getIndexes());
		expectedSystemOwners.remove(untouchedSystemOwner);
				
		assertEquals(expectedSystemOwners, touchedArea.getIndexes(), new Asserter<SystemOwner>() {
			@Override
			public void doAssert(int itemIndex, SystemOwner expected, SystemOwner actual) {
				Assert.assertEquals(String.valueOf(itemIndex), expected.getSet().getName(), 
									actual.getSet().getName());			
			}			
		});
		
	}
	
	public static boolean compare(Map<String, ?> expected, Map<String, ?> actual) {
		assertEquals((ObjectGraph) expected.get("objectGraph"), 
					 (ObjectGraph) actual.get("objectGraph"));
		assertEquals((Xmi) expected.get("xmi"), (Xmi) actual.get("xmi"));
		assertEquals((Syntax) expected.get("syntax"), (Syntax) actual.get("syntax"));
		return true;
	}

	public static Procedure createProcedure() {
		Procedure procedure = mock(Procedure.class);
		EList<ProcedureCallSpecification> callSpecs = new BasicEList<>();
		when(procedure.getCallSpecifications()).thenReturn(callSpecs);
		return procedure;
	}

	public static Procedure createProcedure(String name) {
		Procedure procedure = createProcedure();
		when(procedure.getName()).thenReturn(name);
		return procedure;
	}

	public static SchemaArea getArea(Schema schema, String areaName) {
		SchemaArea area = schema.getArea(areaName);
		assertNotNull("area " + areaName + " is not defined in schema " + schema.getName() + 
				  	  " version " + schema.getVersion(), area);
		return area;
	}

	public static List<Element> getElements(Key key) {
		List<Element> elements = new ArrayList<>();
		for (KeyElement keyElement : key.getElements()) {
			elements.add(keyElement.getElement());
		}
		return elements;
	}

	public static Schema getEmpschmSchema() {
		return getSchema("testdata/EMPSCHM version 100.schema");
	}
	
	public static Schema getIdmsntwkSchema() {
		return getSchema("testdata/IDMSNTWK version 1.schema");		
	}
	
	public static PointerType[] getPointerTypes(Schema schema, String recordName, String setName) {
		PointerType[] pointers;
		Role role = getRole(schema, recordName, setName);
		if (role instanceof OwnerRole) {
			pointers = PrefixUtil.getDefinedPointerTypes((OwnerRole) role);
		} else {
			pointers = PrefixUtil.getDefinedPointerTypes((MemberRole) role);
		}
		return pointers;
	}

	public static Prefix getPrefix(Schema schema, String recordName) {		
		return PrefixFactory.newPrefixForInquiry(getRecord(schema, recordName));
	}
	
	public static SchemaRecord getRecord(Schema schema, String recordName) {
		SchemaRecord record = schema.getRecord(recordName);
		assertNotNull("record " + recordName + " is not defined in schema " + schema.getName() + 
				  	  " version " + schema.getVersion(), record);
		return record;
	}
	
	public static Role getRole(Schema schema, String recordName, String setName) {
		SchemaRecord record = getRecord(schema, recordName);
		Role role = record.getRole(setName);
		assertNotNull("record " + recordName + " does NOT participate in set " + setName + 
					  " (schema: " + schema.getName() + " version " + schema.getVersion() + ")", 
					  role);
		return role;
	}

	public static Schema getSchema(String path) {
		
		URI uri = URI.createFileURI(new File(path).getAbsolutePath());
		
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry()
		   		   .getExtensionToFactoryMap()
		   		   .put("schema", new XMIResourceFactoryImpl());
		Resource resource = resourceSet.getResource(uri, true);
		Schema schema = (Schema) resource.getContents().get(0);
		
		return schema;		
		
	}

	public static Set getSet(Schema schema, String setName) {
		Set set = schema.getSet(setName);
		assertNotNull("set " + setName + " is not defined in schema " + schema.getName() + 
				  	  " version " + schema.getVersion(), set);
		return set;
	}

	public static boolean isSortedSet(Schema schema, String setName) {
		Set set = getSet(schema, setName);
		return set.getOrder() == SetOrder.SORTED;
	}

	public static boolean isUserOwnedSet(Schema schema, String setName) {
		Set set = getSet(schema, setName);
		return set.getOwner() != null;
	}
	
	public static void makeDirect(SchemaRecord record) {		
		assertNotSame(LocationMode.DIRECT, record.getLocationMode());
		new MakeRecordDirectCommand(record).execute();
		assertSame(LocationMode.DIRECT, record.getLocationMode());
	}
	
	public static void makeLast(Set set) {
		assertNotSame(SetOrder.LAST, set.getOrder());
		new ChangeSetOrderCommand(set, SetOrder.LAST).execute();
		assertSame(SetOrder.LAST, set.getOrder());
	}
	
	public static void removeAllBendpoints(MemberRole memberRole) {
		for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
			while (!connectionPart.getBendpointLocations().isEmpty()) {
				Command command = new DeleteBendpointCommand(connectionPart, 0);
				command.execute();
			}
		}
	}
	
	public static Map<String, ?> snapshot(Schema schema) {
		Map<String, Object> snapshot = new HashMap<>();
		snapshot.put("objectGraph", asObjectGraph(schema));
		snapshot.put("xmi", asXmi(schema));
		snapshot.put("syntax", asSyntax(schema));
		return snapshot;
	}
	
	public static abstract class Asserter<T> {
		public abstract void doAssert(int itemIndex, T expected, T actual);
	}	
	
}
