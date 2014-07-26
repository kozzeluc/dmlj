/**
 * Copyright (C) 2013  Luc Hermans
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
package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;
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
import org.lh.dmlj.schema.editor.testtool.Syntax;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class ChangeViaSpecificationCommandTest {

	/**
	 * When changing the VIA specification for a record, the record has to have a location mode of
	 * VIA and it should already be a member of the specified set - no assertions on this matter are
	 * made, so NPEs can and will occur when these conditions are not met.
	 */
	@Test
	public void test() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; locate the EXPERTISE record
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EXPERTISE");
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		ViaSpecification viaSpecification = record.getViaSpecification();
		assertNotNull(viaSpecification);
		assertNull(viaSpecification.getSymbolicDisplacementName());
		assertNull(viaSpecification.getDisplacementPageCount());
		Set oldSet = viaSpecification.getSet();
		assertNotNull(oldSet);
		assertEquals("EMP-EXPERTISE", oldSet.getName());		
		
		// create the command 
		Set newSet = schema.getSet("SKILL-EXPERTISE");
		assertNotNull(newSet);
		ChangeViaSpecificationCommand command = 
			new ChangeViaSpecificationCommand(record, newSet.getName(), null, null);
		
		
		// execute the command and verify
		command.execute();
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		ViaSpecification viaSpecification2 = record.getViaSpecification();
		assertNotNull(viaSpecification2);
		assertNotSame(viaSpecification, viaSpecification2);
		assertNull(viaSpecification2.getSymbolicDisplacementName());
		assertNull(viaSpecification2.getDisplacementPageCount());
		assertSame(newSet, viaSpecification2.getSet());
		
		
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
		
		// make sure the VIA specification reference is set
		EStructuralFeature[] features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSchemaRecord_ViaSpecification());		
		
		
		// undo the command and verify
		command.undo();
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);
		ViaSpecification viaSpecification3 = record.getViaSpecification();
		assertNotNull(viaSpecification3);
		assertNotSame(viaSpecification, viaSpecification3);
		assertNotSame(viaSpecification2, viaSpecification3);
		assertNull(viaSpecification3.getSymbolicDisplacementName());
		assertNull(viaSpecification3.getDisplacementPageCount());
		assertSame(oldSet, viaSpecification3.getSet());
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(record, owner);		
		
		// make sure the VIA specification reference is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSchemaRecord_ViaSpecification());		
		
		
		// redo the command and verify
		command.redo();
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);
		ViaSpecification viaSpecification4 = record.getViaSpecification();
		assertNotNull(viaSpecification4);
		assertNotSame(viaSpecification, viaSpecification4);
		assertNotSame(viaSpecification2, viaSpecification4);
		assertNotSame(viaSpecification3, viaSpecification4);
		assertNull(viaSpecification4.getSymbolicDisplacementName());
		assertNull(viaSpecification4.getDisplacementPageCount());
		assertSame(newSet, viaSpecification4.getSet());	
		
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(record, owner);		
		
		// make sure the VIA specification reference is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSchemaRecord_ViaSpecification());		
		
	}	
	
	/**
	 * When changing the VIA specification for a record, the record has to have a location mode of
	 * VIA and it should already be a member of the specified set - no assertions on this matter are
	 * made, so NPEs can and will occur when these conditions are not met.
	 */
	@Test
	public void test_WithSymbolicDisplacement() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; locate the EXPERTISE record
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EXPERTISE");
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		ViaSpecification viaSpecification = record.getViaSpecification();
		assertNotNull(viaSpecification);
		assertNull(viaSpecification.getSymbolicDisplacementName());
		assertNull(viaSpecification.getDisplacementPageCount());
		Set oldSet = viaSpecification.getSet();
		assertNotNull(oldSet);
		assertEquals("EMP-EXPERTISE", oldSet.getName());		
		
		// create the command 
		Set newSet = schema.getSet("SKILL-EXPERTISE");
		assertNotNull(newSet);
		ChangeViaSpecificationCommand command = 
			new ChangeViaSpecificationCommand(record, newSet.getName(), "DISP1", null);
		
		
		// execute the command and verify
		command.execute();
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		ViaSpecification viaSpecification2 = record.getViaSpecification();
		assertNotNull(viaSpecification2);
		assertNotSame(viaSpecification, viaSpecification2);
		assertEquals("DISP1", viaSpecification2.getSymbolicDisplacementName());
		assertNull(viaSpecification2.getDisplacementPageCount());
		assertSame(newSet, viaSpecification2.getSet());		
		
		
		// undo the command and verify
		command.undo();
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);
		ViaSpecification viaSpecification3 = record.getViaSpecification();
		assertNotNull(viaSpecification3);
		assertNotSame(viaSpecification, viaSpecification3);
		assertNotSame(viaSpecification2, viaSpecification3);
		assertNull(viaSpecification3.getSymbolicDisplacementName());
		assertNull(viaSpecification3.getDisplacementPageCount());
		assertSame(oldSet, viaSpecification3.getSet());
		
		
		// redo the command and verify
		command.redo();
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);
		ViaSpecification viaSpecification4 = record.getViaSpecification();
		assertNotNull(viaSpecification4);
		assertNotSame(viaSpecification, viaSpecification4);
		assertNotSame(viaSpecification2, viaSpecification4);
		assertNotSame(viaSpecification3, viaSpecification4);
		assertEquals("DISP1", viaSpecification4.getSymbolicDisplacementName());
		assertNull(viaSpecification4.getDisplacementPageCount());
		assertSame(newSet, viaSpecification4.getSet());	
				
	}
	
	/**
	 * When changing the VIA specification for a record, the record has to have a location mode of
	 * VIA and it should already be a member of the specified set - no assertions on this matter are
	 * made, so NPEs can and will occur when these conditions are not met.
	 */
	@Test
	public void test_WithDisplacementPageCount() {
		
		// get the EMPSCHM version 1 schema from the testdata folder; locate the EXPERTISE record
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EXPERTISE");
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		ViaSpecification viaSpecification = record.getViaSpecification();
		assertNotNull(viaSpecification);
		assertNull(viaSpecification.getSymbolicDisplacementName());
		assertNull(viaSpecification.getDisplacementPageCount());
		Set oldSet = viaSpecification.getSet();
		assertNotNull(oldSet);
		assertEquals("EMP-EXPERTISE", oldSet.getName());		
		
		// create the command 
		Set newSet = schema.getSet("SKILL-EXPERTISE");
		assertNotNull(newSet);
		ChangeViaSpecificationCommand command = 
			new ChangeViaSpecificationCommand(record, newSet.getName(), null, 
											  Short.valueOf((short) 5));
		
		
		// execute the command and verify
		command.execute();
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		ViaSpecification viaSpecification2 = record.getViaSpecification();
		assertNotNull(viaSpecification2);
		assertNotSame(viaSpecification, viaSpecification2);
		assertNull(viaSpecification2.getSymbolicDisplacementName());
		assertNotNull(viaSpecification2.getDisplacementPageCount());
		assertEquals(Short.valueOf((short) 5), viaSpecification2.getDisplacementPageCount());
		assertSame(newSet, viaSpecification2.getSet());
		
		
		// undo the command and verify
		command.undo();
		Xmi xmi3 = TestTools.asXmi(schema);
		assertEquals(xmi, xmi3);
		Syntax syntax3 = TestTools.asSyntax(schema);
		assertEquals(syntax, syntax3);
		ViaSpecification viaSpecification3 = record.getViaSpecification();
		assertNotNull(viaSpecification3);
		assertNotSame(viaSpecification, viaSpecification3);
		assertNotSame(viaSpecification2, viaSpecification3);
		assertNull(viaSpecification3.getSymbolicDisplacementName());
		assertNull(viaSpecification3.getDisplacementPageCount());
		assertSame(oldSet, viaSpecification3.getSet());
		
		
		// redo the command and verify
		command.redo();
		Xmi xmi4 = TestTools.asXmi(schema);
		assertEquals(xmi2, xmi4);
		Syntax syntax4 = TestTools.asSyntax(schema);
		assertEquals(syntax2, syntax4);
		ViaSpecification viaSpecification4 = record.getViaSpecification();
		assertNotNull(viaSpecification4);
		assertNotSame(viaSpecification, viaSpecification4);
		assertNotSame(viaSpecification2, viaSpecification4);
		assertNotSame(viaSpecification3, viaSpecification4);
		assertNull(viaSpecification4.getSymbolicDisplacementName());
		assertNotNull(viaSpecification4.getDisplacementPageCount());
		assertEquals(Short.valueOf((short) 5), viaSpecification4.getDisplacementPageCount());		
		assertSame(newSet, viaSpecification4.getSet());	
		
	}	
	
	

}
