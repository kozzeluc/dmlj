package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.Syntax;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class MakeRecordViaCommandTest {

	@Test
	public void test_WithSymbolicDisplacement() {

		// we'll work with the IDMSNTWK schema and will make the SROOT-DCS-139 record VIA (set
		// QUEUE-SROOT)
		Schema schema = TestTools.getIdmsntwkSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		SchemaRecord record = schema.getRecord("SROOT-DCS-139");
		assertSame(LocationMode.DIRECT, record.getLocationMode());
		assertEquals(1, record.getMemberRoles().size());		
		assertEquals("QUEUE-SROOT", record.getMemberRoles().get(0).getSet().getName());
		Set set = schema.getSet("QUEUE-SROOT");
		assertNotNull(set);
		assertEquals(0, set.getViaMembers().size());
		
		// create the command
		Command command = new MakeRecordViaCommand(record, "QUEUE-SROOT", "SD1", null); 
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		assertSame(LocationMode.VIA, record.getLocationMode());
		ViaSpecification viaSpecification = record.getViaSpecification();
		assertNotNull(viaSpecification);
		assertEquals("SD1", viaSpecification.getSymbolicDisplacementName());
		assertNull(viaSpecification.getDisplacementPageCount());
		assertSame(set, viaSpecification.getSet());
		assertEquals(0, set.getViaMembers().indexOf(viaSpecification));
		assertNull(record.getCalcKey());
		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.SET_FEATURES
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.SET_FEATURES, modelChangeAnnotation.category());
		
		// make sure the owner is set
		SchemaRecord owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(record, owner);		
		
		// make sure the attribute is set
		EStructuralFeature[] features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode());		
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(record, owner);		
		
		// make sure the attribute is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode());		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);		
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);
		assertSame(viaSpecification, record.getViaSpecification());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(record, owner);		
		
		// make sure the attribute is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSchemaRecord_LocationMode());		
		
	}
	
	@Test
	public void test_WithDisplacementPageCount() {

		// we'll work with the IDMSNTWK schema and will make the SROOT-DCS-139 record VIA (set
		// QUEUE-SROOT)
		Schema schema = TestTools.getIdmsntwkSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		SchemaRecord record = schema.getRecord("SROOT-DCS-139");
		assertSame(LocationMode.DIRECT, record.getLocationMode());
		assertEquals(1, record.getMemberRoles().size());		
		assertEquals("QUEUE-SROOT", record.getMemberRoles().get(0).getSet().getName());
		Set set = schema.getSet("QUEUE-SROOT");
		assertNotNull(set);
		assertEquals(0, set.getViaMembers().size());
		
		// create the command
		Command command = 
			new MakeRecordViaCommand(record, "QUEUE-SROOT", null, Short.valueOf((short) 100)); 
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		assertSame(LocationMode.VIA, record.getLocationMode());
		ViaSpecification viaSpecification = record.getViaSpecification();
		assertNotNull(viaSpecification);
		assertNull(viaSpecification.getSymbolicDisplacementName());
		assertNotNull(viaSpecification.getDisplacementPageCount());
		assertEquals(100, viaSpecification.getDisplacementPageCount().shortValue());
		assertSame(set, viaSpecification.getSet());
		assertEquals(0, set.getViaMembers().indexOf(viaSpecification));
		assertNull(record.getCalcKey());		
		
		
		// undo the command and verify
		command.undo();
		ObjectGraph objectGraph3 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph, objectGraph3);
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);		
		
		
		// redo the command and verify
		command.redo();
		ObjectGraph objectGraph4 = TestTools.asObjectGraph(schema);
		assertEquals(objectGraph2, objectGraph4);
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);		
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);
		assertSame(viaSpecification, record.getViaSpecification());		
		
	}	

}
