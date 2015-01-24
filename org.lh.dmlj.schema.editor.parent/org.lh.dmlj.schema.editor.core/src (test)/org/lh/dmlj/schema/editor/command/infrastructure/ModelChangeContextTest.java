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
package org.lh.dmlj.schema.editor.command.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.DiagramNode;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
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
	public void testPutContextData_DifferentModelTypes() {
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		
		// ConnectionPart: set name, record name and connection part index
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
		
		// Guide: ruler- and guide indexes
		context.getContextData().clear();
		Guide guide = schema.getDiagramData().getRulers().get(1).getGuides().get(0);
		context.putContextData(guide);
		assertEquals(2, context.getContextData().size());
		assertEquals("1", context.getContextData().get(IContextDataKeys.RULER_INDEX));
		assertEquals("0", context.getContextData().get(IContextDataKeys.GUIDE_INDEX));
		
		// MemberRole: set- and record name
		context.getContextData().clear();
		context.putContextData((MemberRole) schema.getRecord("COVERAGE").getRole("EMP-COVERAGE"));
		assertEquals(2, context.getContextData().size());
		assertEquals("EMP-COVERAGE", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("COVERAGE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		
		// Ruler: ruler index
		context.getContextData().clear();
		Ruler ruler = schema.getDiagramData().getRulers().get(0);
		context.putContextData(ruler);
		assertEquals(1, context.getContextData().size());
		assertEquals("0", context.getContextData().get(IContextDataKeys.RULER_INDEX));
		
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
	}
	
	@Test
	public void testPutContextData_DiagramNodeTypes() {
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		
		// ConnectionLabel: set- and record name
		MemberRole memberRole = (MemberRole) schema.getRecord("COVERAGE").getRole("EMP-COVERAGE");
		DiagramNode diagramNode = memberRole.getConnectionLabel();
		context.putContextData(diagramNode);
		assertEquals(2, context.getContextData().size());
		assertEquals("EMP-COVERAGE", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("COVERAGE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
			
		// Connector: set name, record name and connection part index
		context.getContextData().clear();
		SchemaRecord record = schema.getRecord("DENTAL-CLAIM");
		Connector connector = 
			((MemberRole) record.getRole("COVERAGE-CLAIMS")).getConnectionParts().get(1).getConnector();
		diagramNode = connector;
		context.putContextData(diagramNode);
		assertEquals(3, context.getContextData().size());
		assertEquals("COVERAGE-CLAIMS", context.getContextData().get(IContextDataKeys.SET_NAME));
		assertEquals("DENTAL-CLAIM", context.getContextData().get(IContextDataKeys.RECORD_NAME));
		assertEquals("1", context.getContextData().get(IContextDataKeys.CONNECTION_PART_INDEX));
			
		// DiagramLabel: nothing
		context.getContextData().clear();
		diagramNode = mock(DiagramLabel.class); // no diagram label in our schema
		context.putContextData(diagramNode);
		assertEquals(0, context.getContextData().size());
			
		// SchemaRecord: record name
		context.getContextData().clear();
		diagramNode = schema.getRecord("OFFICE");
		context.putContextData(diagramNode);
		assertEquals(1, context.getContextData().size());
		assertEquals("OFFICE", context.getContextData().get(IContextDataKeys.RECORD_NAME));
			
		// SystemOwner: set name
		context.getContextData().clear();
		diagramNode = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		context.putContextData(diagramNode);
		assertEquals(1, context.getContextData().size());
		assertEquals("EMP-NAME-NDX", context.getContextData().get(IContextDataKeys.SET_NAME));
		
		// invalid diagram node type: not null
		diagramNode = mock(DiagramNode.class);
		try {
			context.putContextData(diagramNode);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage(), 
					   e.getMessage().startsWith("DiagramNode type invalid: org.lh.dmlj.schema.DiagramNode"));
		}
		
		// invalid diagram node type: null
		diagramNode = null;
		try {
			context.putContextData(diagramNode);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage(), 
					   e.getMessage().startsWith("DiagramNode type invalid: null"));
		}		
	}

	@Test
	public void testPutContextData_FeatureRelated() {
		
		// test the exceptions...
		try {
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_BENDPOINT);
			context.putContextData(null, 
								   SchemaPackage.eINSTANCE.getAreaProcedureCallSpecification_Area());
			fail("should throw an IllegalStateException");
		} catch (IllegalStateException e) {
			assertEquals("Invalid model change type: ADD_BENDPOINT", e.getMessage());
		}
		try {
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
			context.putContextData(null, null);
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid feature: null", e.getMessage());
		}
		try {
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
			context.putContextData(null, SchemaPackage.eINSTANCE.getAreaSpecification_Area());
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().startsWith("Model type invalid: "));
		}
		try {
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
			context.putContextData(mock(AreaSpecification.class), 
								   SchemaPackage.eINSTANCE.getAreaSpecification_Area());
			fail("should throw an IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().startsWith("Model type invalid: "));
		}
		
		// SchemaArea: feature + area name
		SchemaArea area = mock(SchemaArea.class);
		when(area.getName()).thenReturn("AREA1");
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(area, SchemaPackage.eINSTANCE.getSchemaArea_Name());
		assertEquals(2, context.getContextData().size());
		assertEquals("SchemaArea.name", 
					 context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		assertEquals("AREA1", context.getContextData().get(IContextDataKeys.AREA_NAME));
		
		// DiagramLabel: feature
		DiagramLabel diagramLabel = mock(DiagramLabel.class);
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(diagramLabel, SchemaPackage.eINSTANCE.getDiagramLabel_Description());
		assertEquals(1, context.getContextData().size());
		assertEquals("DiagramLabel.description", 
					 context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		
		// DiagramLabel: feature
		DiagramData diagramData = mock(DiagramData.class);
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(diagramData, SchemaPackage.eINSTANCE.getDiagramData_ShowGrid());
		assertEquals(1, context.getContextData().size());
		assertEquals("DiagramData.showGrid", 
					context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		
		// SchemaRecord: feature + record name
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn("RECORD1");
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(diagramData, SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertEquals(1, context.getContextData().size());
		assertEquals("SchemaRecord.name", 
					 context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		
		// DiagramLabel: feature
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(diagramData, SchemaPackage.eINSTANCE.getSchema_Name());
		assertEquals(1, context.getContextData().size());
		assertEquals("Schema.name", context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		
		// Set: feature + set name
		Set set = mock(Set.class);
		when(set.getName()).thenReturn("SET1");
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(diagramData, SchemaPackage.eINSTANCE.getSet_Name());
		assertEquals(1, context.getContextData().size());
		assertEquals("Set.name", context.getContextData().get(IContextDataKeys.PROPERTY_NAME));
		
	}
	
	@Test
	public void testIsPropertySet() {
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(mock(SchemaRecord.class), 
							   SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		
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
	public void testAppliesTo_DifferentModelTypes_ValidSituationsOnly() {
		
		Schema schema = TestTools.getSchema("testdata/EMPSCHM version 100b.schema");
		
		// ConnectionPart (not SET_FEATURE)
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.ADD_RECORD);
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
		// the context will NOT apply to the member role, set and member record
		assertFalse(context.appliesTo(part1.getMemberRole()));
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
		// the context will NOT apply to the member role, set and member record
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getSet()));
		assertFalse(context.appliesTo(connector1.getConnectionPart().getMemberRole().getRecord()));
		
		// Guide (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.DELETE_GUIDE);
		Guide guide1 = schema.getDiagramData().getRulers().get(0).getGuides().get(0);
		Guide guide2 = schema.getDiagramData().getRulers().get(0).getGuides().get(1);
		Guide guide3 = schema.getDiagramData().getRulers().get(1).getGuides().get(0);
		Guide guide4 = schema.getDiagramData().getRulers().get(1).getGuides().get(1);
		assertFalse(context.appliesTo(guide1));
		assertFalse(context.appliesTo(guide2));
		assertFalse(context.appliesTo(guide3));
		assertFalse(context.appliesTo(guide4));
		context.putContextData(guide3);
		assertFalse(context.appliesTo(guide1));
		assertFalse(context.appliesTo(guide2));
		assertTrue(context.appliesTo(guide3));
		assertFalse(context.appliesTo(guide4));
		// the context does NOT apply to either one of the rulers
		assertFalse(context.appliesTo(guide1.getRuler()));
		assertFalse(context.appliesTo(guide3.getRuler()));
		
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
		// the context will not apply to the set, member record and both connectors
		assertFalse(context.appliesTo(memberRole1.getConnectionParts().get(0).getConnector()));
		assertFalse(context.appliesTo(memberRole1.getConnectionParts().get(1).getConnector()));
		assertFalse(context.appliesTo(memberRole1.getSet()));
		assertFalse(context.appliesTo(memberRole1.getRecord()));
		
		// Ruler (not SET_FEATURE)
		context = new ModelChangeContext(ModelChangeType.ADD_GUIDE);
		Ruler ruler1 = schema.getDiagramData().getRulers().get(0);
		Ruler ruler2 = schema.getDiagramData().getRulers().get(1);
		assertFalse(context.appliesTo(ruler1));
		assertFalse(context.appliesTo(ruler2));
		context.putContextData(ruler2);
		assertFalse(context.appliesTo(ruler1));
		assertTrue(context.appliesTo(ruler2));
		// the context does NOT apply to either one of the guides
		assertFalse(context.appliesTo(ruler1.getGuides().get(0)));
		assertFalse(context.appliesTo(ruler1.getGuides().get(1)));
		assertFalse(context.appliesTo(ruler2.getGuides().get(0)));
		assertFalse(context.appliesTo(ruler2.getGuides().get(1)));
		
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
		context.putContextData(area1, SchemaPackage.eINSTANCE.getSchemaRecord_Name());
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
		context.putContextData(record1, SchemaPackage.eINSTANCE.getSchemaRecord_Name());
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
		// the context will not apply to the member role and member record
		assertFalse(context.appliesTo(set1.getMembers().get(0)));
		assertFalse(context.appliesTo(set1.getMembers().get(0).getRecord()));
		
		// Set (SET_FEATURE)		
		context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
		context.putContextData(set1, SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		assertTrue(context.appliesTo(set1));
		assertFalse(context.appliesTo(set2));
		// the context will not apply to the member role and member record
		assertFalse(context.appliesTo(set1.getMembers().get(0)));
		assertFalse(context.appliesTo(set1.getMembers().get(0).getRecord()));
	}
	
	@Test
	public void testAppliesTo_DifferentModelTypes_InvalidSituationsOnly() 
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// the purpose of this test case is to assure that for each model type the 'checkAppliesTo'
		// method is invoked
		 		
		Class<?>[] modelTypes = {ConnectionPart.class, 
								 Connector.class,
								 Guide.class,
								 MemberRole.class,
								 Ruler.class,
								 SchemaArea.class,
								 SchemaRecord.class,
								 Set.class};
		for (Class<?> modelType : modelTypes) {
			Method method = findAppliesToMethod(modelType);
			method.setAccessible(true);
			
			// SET_PROPERTY model change type
			ModelChangeContext context = new ModelChangeContext(ModelChangeType.SET_PROPERTY);
			try {
				Object _null = null;
				method.invoke(context, new Object[] {_null});
				fail("should throw an InvocationTargetException");
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				assertTrue(modelType.getSimpleName() + " - unexpected cause: " + e.getCause(), 
						   cause instanceof IllegalArgumentException);
				assertEquals(modelType.getSimpleName() + " - Invalid model: null", 
							 "Invalid model: null", cause.getMessage());
			}			
			try {
				method.invoke(context, mock(modelType));
				fail("should throw an IllegalStateException");
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				assertTrue(modelType.getSimpleName() + " - unexpected cause: " + e.getCause(), 
						   cause instanceof IllegalStateException);
				assertEquals(modelType.getSimpleName() + " - Feature name NOT found in context data", 
							 "Feature name NOT found in context data", cause.getMessage());
			}
			
			// not SET_PROPERTY model change type			
			context = new ModelChangeContext(ModelChangeType.ADD_BENDPOINT);
			context.getContextData().put(IContextDataKeys.PROPERTY_NAME, "xyz");
			try {
				Object _null = null;
				method.invoke(context, new Object[] {_null});
				fail("should throw an InvocationTargetException");
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				assertTrue(modelType.getSimpleName() + " - unexpected cause: " + e.getCause(), 
						   cause instanceof IllegalArgumentException);
				assertEquals(modelType.getSimpleName() + " - Invalid model: null", 
							 "Invalid model: null", cause.getMessage());
			}
			try {
				method.invoke(context, mock(modelType));
				fail("should throw an IllegalStateException");
			} catch (InvocationTargetException e) {
				Throwable cause = e.getCause();
				assertTrue(modelType.getSimpleName() + " - unexpected cause: " + e.getCause(), 
						   cause instanceof IllegalStateException);
				assertEquals(modelType.getSimpleName() + " - Feature name should NOT be present in " +
							 "context data: ADD_BENDPOINT", 
							 "Feature name should NOT be present in context data: ADD_BENDPOINT", 
							 cause.getMessage());
			}
		}				
	}

	private Method findAppliesToMethod(Class<?> modelType) {
		for (Method method : ModelChangeContext.class.getMethods()) {
			if (method.getName().equals("appliesTo") && method.getReturnType() == boolean.class &&
				method.getParameterTypes().length == 1 && 
				method.getParameterTypes()[0] == modelType) {
				
				return method;
			}
		}
		fail("cannot find findAppliesTo method: " + modelType.getSimpleName());
		return null; // never reached
	}

}
