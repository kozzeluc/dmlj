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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.Syntax;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class MakeRecordCalcCommandTest {

	@Test
	public void test() {
		
		// get the IDMSNTWK schema; we'll make the LOGREC-143 CALC
		Schema schema = TestTools.getIdmsntwkSchema();
		ObjectGraph objectGraph = TestTools.asObjectGraph(schema);
		Xmi xmi = TestTools.asXmi(schema);
		Syntax syntax = TestTools.asSyntax(schema);
		SchemaRecord record = schema.getRecord("LOGREC-143");
		assertSame(LocationMode.DIRECT, record.getLocationMode());
		Element element = record.getElement("IDENT-143");
		List<Element> calcKeyElements = new ArrayList<>();
		calcKeyElements.add(element);
		
		// create the command
		Command command = 
			new MakeRecordCalcCommand(record, calcKeyElements, DuplicatesOption.NOT_ALLOWED);
		
		
		// execute the command and verify
		command.execute();
		ObjectGraph objectGraph2 = TestTools.asObjectGraph(schema);
		Xmi xmi2 = TestTools.asXmi(schema);
		Syntax syntax2 = TestTools.asSyntax(schema);
		assertSame(LocationMode.CALC, record.getLocationMode());
		Key calcKey = record.getCalcKey();
		assertNotNull(calcKey);
		assertEquals(1, calcKey.getElements().size());
		KeyElement keyElement = calcKey.getElements().get(0);
		assertSame(element, keyElement.getElement());
		assertSame(SortSequence.ASCENDING, keyElement.getSortSequence());
		assertEquals("IDENT-143", calcKey.getElementSummary());
		assertSame(DuplicatesOption.NOT_ALLOWED, calcKey.getDuplicatesOption());
		assertNull(calcKey.getMemberRole());
		assertNull(record.getViaSpecification());
		
		
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
		assertSame(calcKey, record.getCalcKey());
		assertSame(keyElement, calcKey.getElements().get(0));
		
		
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

}
