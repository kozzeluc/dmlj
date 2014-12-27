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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
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
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_FEATURE);
		context.putContextData(SchemaPackage.eINSTANCE.getConnector_Label());
		assertEquals("Connector.label", context.getContextData().get(IContextDataKeys.FEATURE_NAME));
		
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
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_FEATURE);
		
		// ConnectionLabel: set- and record name
		ConnectionLabel connectionLabel = 
			schema.getSet("DEPT-EMPLOYEE").getMembers().get(0).getConnectionLabel();
		context.putContextData(connectionLabel);
		assertEquals(2, context.getContextData().size());
		assertEquals("DEPT-EMPLOYEE", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("EMPLOYEE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		
		// Connector: set- and record name
		context.getContextData().clear();
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		Connector connector = 
			((MemberRole) record.getRole("COVERAGE-CLAIMS")).getConnectionParts().get(0).getConnector();
		context.putContextData(connector);
		assertEquals(2, context.getContextData().size());
		assertEquals("COVERAGE-CLAIMS", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("DENTAL-CLAIM", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		
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
	public void testIsFeatureSet() {
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_FEATURE);
		context.putContextData(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		
		assertTrue(context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_Name()));		
		assertTrue(context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_Name(),
										SchemaPackage.eINSTANCE.getSchemaRecord_BaseName()));
		assertTrue(context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_BaseName(),
										SchemaPackage.eINSTANCE.getSchemaRecord_Name()));
		
		assertTrue(!context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_BaseName()));
		assertTrue(!context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_BaseName(),
										 SchemaPackage.eINSTANCE.getSchemaRecord_BaseVersion()));
		
		context = new ModelChangeContext(ModelChangeType.ADD_BENDPOINT);
		try {
			context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Context has wrong model change type: ADD_BENDPOINT (expected: SET_FEATURE)", 
						 e.getMessage());
		}
		
		context = new ModelChangeContext(ModelChangeType.SET_FEATURE);
		try {
			context.isFeatureSet(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Context has no feature in its context data", e.getMessage());
		}
		
	}

}
