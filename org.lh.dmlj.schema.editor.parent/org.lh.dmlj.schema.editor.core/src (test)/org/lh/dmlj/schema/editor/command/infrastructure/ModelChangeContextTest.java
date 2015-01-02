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
package org.lh.dmlj.schema.editor.command.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class ModelChangeContextTest {

	@Test
	public void test() {
		
		Schema schema = mock(Schema.class);
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		context.setSchema(schema);
		context.getContextData().put("key1", "value1");
		context.getContextData().put("key2", "value2");
		context.setListenerData("listener data");
		context.setCommandExecutionMode(CommandExecutionMode.UNDO);
		
		assertSame(ModelChangeType.SWAP_RECORD_ELEMENTS, context.getModelChangeType());
		assertSame(schema, context.getSchema());
		assertEquals(2, context.getContextData().size());
		assertEquals("value1", context.getContextData().get("key1"));
		assertEquals("value2", context.getContextData().get("key2"));
		assertEquals("listener data", context.getListenerData());
		assertSame(CommandExecutionMode.UNDO, context.getCommandExecutionMode());
		
		ModelChangeContext copy = context.copy();
		assertSame(ModelChangeType.SWAP_RECORD_ELEMENTS, copy.getModelChangeType());
		assertSame(schema, copy.getSchema());
		assertNotSame(context.getContextData(), copy.getContextData());
		assertEquals(2, copy.getContextData().size());
		assertEquals("value1", copy.getContextData().get("key1"));
		assertEquals("value2", copy.getContextData().get("key2"));
		assertNull("listener data should be erased for copies", copy.getListenerData());
		assertNull("command execution mode should be erased for copies", copy.getCommandExecutionMode());
		
	}
	
	@Test
	public void testGetQualifiedFeatureName() {
		String qualifiedFeatureName = 
			ModelChangeContext.getQualifiedFeatureName(SchemaPackage.eINSTANCE.getConnector_Label());
		assertEquals("Connector.label", qualifiedFeatureName);
	}
	
	@Test
	public void testPutContextData_Feature() {
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getConnector_Label());
		assertEquals("Connector.label", context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		
		// invalid feature: null
		context.getContextData().clear();
		try {
			context.putContextData((EStructuralFeature) null);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid feature: null", e.getMessage());
		}
	}
	
	@Test
	public void testPutContextData_Model() {
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		
		// ConnectionLabel: set- and record name
		ConnectionLabel connectionLabel = 
			schema.getSet("DEPT-EMPLOYEE").getMembers().get(0).getConnectionLabel();
		context.putContextData(connectionLabel);
		assertEquals(2, context.getContextData().size());
		assertEquals("DEPT-EMPLOYEE", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("EMPLOYEE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		
		// ConnectionPart : set name, record name and connection part index
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		ConnectionPart connectionPart = 
			((MemberRole) record.getRole("COVERAGE-CLAIMS")).getConnectionParts().get(1);
		context.putContextData(connectionPart);
		assertEquals(3, context.getContextData().size());
		assertEquals("COVERAGE-CLAIMS", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("DENTAL-CLAIM", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		assertEquals("1", context.getContextData().get(IContextDataKeys.CONNECTION_PART_INDEX));
		
		// Connector: set name, record name and connection part index
		context.getContextData().clear();
		record = schema.getRecord("DENTAL-CLAIM");
		Connector connector = 
			((MemberRole) record.getRole("COVERAGE-CLAIMS")).getConnectionParts().get(1).getConnector();
		context.putContextData(connector);
		assertEquals(3, context.getContextData().size());
		assertEquals("COVERAGE-CLAIMS", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("DENTAL-CLAIM", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		assertEquals("1", context.getContextData().get(IContextDataKeys.CONNECTION_PART_INDEX));
		
		// DiagramData: nothing
		context.getContextData().clear();
		context.putContextData(mock(DiagramData.class));
		assertEquals(0, context.getContextData().size());
		
		// DiagramLabel: nothing
		context.getContextData().clear();
		context.putContextData(mock(DiagramLabel.class));
		assertEquals(0, context.getContextData().size());
		
		// MemberRole: set- and record name
		context.getContextData().clear();
		context.putContextData(schema.getRecord("COVERAGE").getRole("EMP-COVERAGE"));
		assertEquals(2, context.getContextData().size());
		assertEquals("EMP-COVERAGE", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("COVERAGE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		
		// Schema: nothing
		context.getContextData().clear();
		context.putContextData(mock(Schema.class));
		assertEquals(0, context.getContextData().size());
		
		// SchemaArea: area name
		context.getContextData().clear();
		context.putContextData(schema.getArea("EMP-DEMO-REGION"));
		assertEquals(1, context.getContextData().size());
		assertEquals("EMP-DEMO-REGION", context.getContextData().get(IContextDataKeys.AREA_NAME));
		
		// SchemaRecord: record name
		context.getContextData().clear();
		context.putContextData(schema.getRecord("OFFICE"));
		assertEquals(1, context.getContextData().size());
		assertEquals("OFFICE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		
		// Set: set name
		context.getContextData().clear();
		context.putContextData(schema.getSet("OFFICE-EMPLOYEE"));
		assertEquals(1, context.getContextData().size());
		assertEquals("OFFICE-EMPLOYEE", context.getContextData().get(IContextDataKeys.SET_NAME));
		
		// SystemOwner: set name
		context.getContextData().clear();
		context.putContextData(schema.getSet("EMP-NAME-NDX").getSystemOwner());
		assertEquals(1, context.getContextData().size());
		assertEquals("EMP-NAME-NDX", context.getContextData().get(IContextDataKeys.SET_NAME));
		
		
		// invalid model type: null
		context.getContextData().clear();
		try {
			context.putContextData((EObject) null);
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid model type: null", e.getMessage());
		}
	}
	
	@Test
	public void testIsPropertySet() {
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		
		assertTrue(context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()));		
		assertTrue(context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name(),
										SchemaPackage.eINSTANCE.getSchemaRecord_BaseName()));
		assertTrue(context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_BaseName(),
										SchemaPackage.eINSTANCE.getSchemaRecord_Name()));
		
		assertTrue(!context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_BaseName()));
		assertTrue(!context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_BaseName(),
										 SchemaPackage.eINSTANCE.getSchemaRecord_BaseVersion()));
		
		context = new ModelChangeContext(ModelChangeType.ADD_BENDPOINT);
		try {
			context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Context has wrong model change type: ADD_BENDPOINT (expected: SET_PROPERTY)", 
						 e.getMessage());
		}
		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		try {
			context.isPropertySet(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Context has no feature in its context data", e.getMessage());
		}
		
	}
	
	@Test
	public void testAppliesTo() {
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		try {
			context.appliesTo(null);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid model: null", e.getMessage());
		}
		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		try {
			context.appliesTo(mock(SchemaRecord.class));
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Feature name NOT found in context data", e.getMessage());
		}
		
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		try {
			context.appliesTo(mock(SchemaRecord.class));
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Feature name should NOT be present in context data: ADD_RECORD", 
						 e.getMessage());
		}
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		
		// ConnectionLabel (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		ConnectionLabel connectionLabel1 = 
				((MemberRole) schema.getRecord("DENTAL-CLAIM").getRole("COVERAGE-CLAIMS")).getConnectionLabel();
		ConnectionLabel connectionLabel2 = 
			schema.getSet("OFFICE-EMPLOYEE").getMembers().get(0).getConnectionLabel();
		assertFalse(context.appliesTo(connectionLabel1));
		assertFalse(context.appliesTo(connectionLabel2));
		context.putContextData(connectionLabel1);
		assertTrue(context.appliesTo(connectionLabel1));
		assertFalse(context.appliesTo(connectionLabel2));
		// the context will also apply to the member role but not to the set, the member record and
		// both connectors
		assertTrue(context.appliesTo(connectionLabel1.getMemberRole()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getConnectionParts().get(0).getConnector()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getConnectionParts().get(1).getConnector()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getSet()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getRecord()));
		
		// ConnectionLabel (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(connectionLabel1));
		assertFalse(context.appliesTo(connectionLabel2));
		context.putContextData(connectionLabel1);
		assertTrue(context.appliesTo(connectionLabel1));
		assertFalse(context.appliesTo(connectionLabel2));
		// the context will also apply to the member role, but not to the set, the member record and
		// both connectors
		assertTrue(context.appliesTo(connectionLabel1.getMemberRole()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getConnectionParts().get(0).getConnector()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getConnectionParts().get(1).getConnector()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getSet()));
		assertFalse(context.appliesTo(connectionLabel1.getMemberRole().getRecord()));
		
		// ConnectionPart (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		ConnectionPart part1 = 
			((MemberRole) schema.getRecord("DENTAL-CLAIM").getRole("COVERAGE-CLAIMS")).getConnectionParts().get(0);
		ConnectionPart part2 = 
			((MemberRole) schema.getRecord("DENTAL-CLAIM").getRole("COVERAGE-CLAIMS")).getConnectionParts().get(1);
		ConnectionPart part3 = 
			((MemberRole) schema.getRecord("EMPOSITION").getRole("JOB-EMPOSITION")).getConnectionParts().get(0);
		ConnectionPart part4 = 
			((MemberRole) schema.getRecord("EMPOSITION").getRole("JOB-EMPOSITION")).getConnectionParts().get(1);
		assertFalse(context.appliesTo(part1));
		assertFalse(context.appliesTo(part2));
		assertFalse(context.appliesTo(part3));
		assertFalse(context.appliesTo(part4));
		context.putContextData(part2);
		assertFalse(context.appliesTo(part1));
		assertTrue(context.appliesTo(part2));
		assertFalse(context.appliesTo(part3));
		assertFalse(context.appliesTo(part4));
		// the context will NOT apply to the member role, connection label, set and member record
		assertFalse(context.appliesTo(part1.getMemberRole()));
		assertFalse(context.appliesTo(part1.getMemberRole().getConnectionLabel()));
		assertFalse(context.appliesTo(part1.getMemberRole().getSet()));
		assertFalse(context.appliesTo(part1.getMemberRole().getRecord()));
		
		// ConnectionPart (SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(part1));
		assertFalse(context.appliesTo(part2));
		assertFalse(context.appliesTo(part3));
		assertFalse(context.appliesTo(part4));
		context.putContextData(part1);
		assertTrue(context.appliesTo(part1));
		assertFalse(context.appliesTo(part2));
		assertFalse(context.appliesTo(part3));
		assertFalse(context.appliesTo(part4));
		// the context will NOT apply to the member role, connection label, set and member record
		assertFalse(context.appliesTo(part1.getMemberRole()));
		assertFalse(context.appliesTo(part1.getMemberRole().getConnectionLabel()));
		assertFalse(context.appliesTo(part1.getMemberRole().getSet()));
		assertFalse(context.appliesTo(part1.getMemberRole().getRecord()));
		
		// Connector (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		Connector connector1 = 
			((MemberRole) schema.getRecord("DENTAL-CLAIM").getRole("COVERAGE-CLAIMS")).getConnectionParts().get(0).getConnector();
		Connector connector2 = 
			((MemberRole) schema.getRecord("DENTAL-CLAIM").getRole("COVERAGE-CLAIMS")).getConnectionParts().get(1).getConnector();
		Connector connector3 = 
			((MemberRole) schema.getRecord("EMPOSITION").getRole("JOB-EMPOSITION")).getConnectionParts().get(0).getConnector();
		Connector connector4 = 
			((MemberRole) schema.getRecord("EMPOSITION").getRole("JOB-EMPOSITION")).getConnectionParts().get(1).getConnector();
		assertFalse(context.appliesTo(connector1));
		assertFalse(context.appliesTo(connector2));
		assertFalse(context.appliesTo(connector3));
		assertFalse(context.appliesTo(connector4));
		context.putContextData(connector1);
		assertTrue(context.appliesTo(connector1));
		assertFalse(context.appliesTo(connector2));
		assertFalse(context.appliesTo(connector3));
		assertFalse(context.appliesTo(connector4));
		// the context will NOT apply to the member role, connection label, set and member record
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getConnectionLabel()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getSet()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getRecord()));
		
		// Connector (SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(connector1));
		assertFalse(context.appliesTo(connector2));
		assertFalse(context.appliesTo(connector3));
		assertFalse(context.appliesTo(connector4));
		context.putContextData(connector1);
		assertTrue(context.appliesTo(connector1));
		assertFalse(context.appliesTo(connector2));
		assertFalse(context.appliesTo(connector3));
		assertFalse(context.appliesTo(connector4));
		// the context will NOT apply to the member role, connection label, set and member record
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getConnectionLabel()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getSet()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getRecord()));
			
		// DiagramData (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		assertTrue(context.appliesTo(mock(DiagramData.class)));
		context.putContextData(connectionLabel1);
		assertFalse(context.appliesTo(mock(DiagramData.class)));
		
		// DiagramData (SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertTrue(context.appliesTo(mock(DiagramData.class)));
		context.putContextData(connectionLabel1);
		assertFalse(context.appliesTo(mock(DiagramData.class)));
		
		// DiagramLabel (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		assertTrue(context.appliesTo(mock(DiagramLabel.class)));
		context.putContextData(connectionLabel1);
		assertFalse(context.appliesTo(mock(DiagramLabel.class)));
		
		// DiagramLabel (SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertTrue(context.appliesTo(mock(DiagramLabel.class)));
		context.putContextData(connectionLabel1);
		assertFalse(context.appliesTo(mock(DiagramLabel.class)));
		
		// MemberRole (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		MemberRole memberRole1 = 
			(MemberRole) schema.getRecord("DENTAL-CLAIM").getRole("COVERAGE-CLAIMS");
		MemberRole memberRole2 = schema.getSet("OFFICE-EMPLOYEE").getMembers().get(0);
		assertFalse(context.appliesTo(memberRole1));
		assertFalse(context.appliesTo(memberRole2));
		context.putContextData(memberRole1);
		assertTrue(context.appliesTo(memberRole1));
		assertFalse(context.appliesTo(memberRole2));
		// the context will also apply to the connection label, but not to the set, member record
		// and both connectors
		assertTrue(context.appliesTo(memberRole1.getConnectionLabel()));
		assertFalse(context.appliesTo(memberRole1.getConnectionParts().get(0).getConnector()));
		assertFalse(context.appliesTo(memberRole1.getConnectionParts().get(1).getConnector()));
		assertFalse(context.appliesTo(memberRole1.getSet()));
		assertFalse(context.appliesTo(memberRole1.getRecord()));
		
		// MemberRole (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(memberRole1));
		assertFalse(context.appliesTo(memberRole2));
		context.putContextData(memberRole1);
		assertTrue(context.appliesTo(memberRole1));
		assertFalse(context.appliesTo(memberRole2));
		// the context will also apply to the connection label, but not to the set, member record
				// and both connectors
		assertTrue(context.appliesTo(memberRole1.getConnectionLabel()));
		assertFalse(context.appliesTo(memberRole1.getConnectionParts().get(0).getConnector()));
		assertFalse(context.appliesTo(memberRole1.getConnectionParts().get(1).getConnector()));
		assertFalse(context.appliesTo(memberRole1.getSet()));
		assertFalse(context.appliesTo(memberRole1.getRecord()));
				
		// Schema (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		assertTrue(context.appliesTo(mock(Schema.class)));
		context.putContextData(connectionLabel1);
		assertFalse(context.appliesTo(mock(Schema.class)));
		
		// Schema (SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertTrue(context.appliesTo(mock(Schema.class)));
		context.putContextData(connectionLabel1);
		assertFalse(context.appliesTo(mock(Schema.class)));
		
		// SchemaArea (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		SchemaArea area1 = schema.getArea("EMP-DEMO-REGION");
		SchemaArea area2 = schema.getArea("ORG-DEMO-REGION");
		assertFalse(context.appliesTo(area1));
		assertFalse(context.appliesTo(area2));
		context.putContextData(area1);
		assertTrue(context.appliesTo(area1));
		assertFalse(context.appliesTo(area2));
		
		// SchemaArea (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(area1));
		assertFalse(context.appliesTo(area2));
		context.putContextData(area1);
		assertTrue(context.appliesTo(area1));
		assertFalse(context.appliesTo(area2));
		
		// SchemaRecord (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		SchemaRecord record1 = schema.getRecord("EMPLOYEE");
		SchemaRecord record2 = schema.getRecord("DEPARTMENT");
		assertFalse(context.appliesTo(record1));
		assertFalse(context.appliesTo(record2));
		context.putContextData(record1);
		assertTrue(context.appliesTo(record1));
		assertFalse(context.appliesTo(record2));
		
		// SchemaRecord (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(record1));
		assertFalse(context.appliesTo(record2));
		context.putContextData(record1);
		assertTrue(context.appliesTo(record1));
		assertFalse(context.appliesTo(record2));
		
		// Set (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		Set set1 = schema.getSet("EMP-NAME-NDX");
		Set set2 = schema.getSet("COVERAGE-CLAIMS");
		assertFalse(context.appliesTo(set1));
		assertFalse(context.appliesTo(set2));
		context.putContextData(set1);
		assertTrue(context.appliesTo(set1));
		assertFalse(context.appliesTo(set2));
		// the context will also apply to the system owner, but not the member role and member 
		// record
		assertTrue(context.appliesTo(set1.getSystemOwner()));
		assertFalse(context.appliesTo(set1.getMembers().get(0)));
		assertFalse(context.appliesTo(set1.getMembers().get(0).getRecord()));
		
		// Set (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(set1));
		assertFalse(context.appliesTo(set2));
		context.putContextData(set1);
		assertTrue(context.appliesTo(set1));
		assertFalse(context.appliesTo(set2));
		// the context will also apply to the system owner, but not the member role and member 
		// record
		assertTrue(context.appliesTo(set1.getSystemOwner()));
		assertFalse(context.appliesTo(set1.getMembers().get(0)));
		assertFalse(context.appliesTo(set1.getMembers().get(0).getRecord()));
				
		// SystemOwner (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		SystemOwner systemOwner1 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		SystemOwner systemOwner2 = schema.getSet("JOB-TITLE-NDX").getSystemOwner();
		assertFalse(context.appliesTo(systemOwner1));
		assertFalse(context.appliesTo(systemOwner2));
		context.putContextData(systemOwner1);
		assertTrue(context.appliesTo(systemOwner1));
		assertFalse(context.appliesTo(systemOwner2));
		// the context will also apply to the set, but not the member role and member record
		assertTrue(context.appliesTo(systemOwner1.getSet()));
		assertFalse(context.appliesTo(systemOwner1.getSet().getMembers().get(0)));
		assertFalse(context.appliesTo(systemOwner1.getSet().getMembers().get(0).getRecord()));
		
		// SystemOwner (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertFalse(context.appliesTo(systemOwner1));
		assertFalse(context.appliesTo(systemOwner2));
		context.putContextData(systemOwner1);
		assertTrue(context.appliesTo(systemOwner1));
		assertFalse(context.appliesTo(systemOwner2));	
		// the context will also apply to the set, but not the member role and member record
		assertTrue(context.appliesTo(systemOwner1.getSet()));
		assertFalse(context.appliesTo(systemOwner1.getSet().getMembers().get(0)));
		assertFalse(context.appliesTo(systemOwner1.getSet().getMembers().get(0).getRecord()));
		
		// try an invalid model type
		context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
		try {
			context.appliesTo(mock(AreaSpecification.class));
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().startsWith("Invalid model type: "));
		}
	}

}
