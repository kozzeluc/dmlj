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
 */package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.infrastructure.ModelChangeDispatcher;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class ChangeSortKeysCommandTest {

	private ObjectGraph objectGraph;
	private Schema 		schema;		
	private Xmi 		xmi;
	
	private void checkObjectGraph(ObjectGraph expected) {
		ObjectGraph actual = TestTools.asObjectGraph(schema);
		assertEquals(expected, actual);		
	}
	
	private void checkXmi(Xmi expected) {
		Xmi actual = TestTools.asXmi(schema);
		assertEquals(expected, actual);		
	}
	
	@Before
	public void setup() {
		// we'll use EMPSCHM throughout these tests
		schema = TestTools.getEmpschmSchema();
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
	}
	
	@Test
	public void testAnnotations() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");		
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"EMP-FIRST-NAME-0415"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		
		Command command = 
			new ChangeSortKeysCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();		
		
		// once execute() has been called, all annotated field values should be in place; make sure
		// the command class itself is annotated with @ModelChange with its type set to 
		// ModelChangeCategory.SET_FEATURES
		ModelChange modelChangeAnnotation = command.getClass().getAnnotation(ModelChange.class);	
		assertNotNull(modelChangeAnnotation);
		assertEquals(ModelChangeCategory.SET_FEATURES, modelChangeAnnotation.category());
		
		// make sure the owner is set
		Set owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);	
		
		// make sure the sort key reference is set
		EStructuralFeature[] features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getMemberRole_SortKey());		
		
		
		command.undo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);		
		
		// make sure the sort key reference is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getMemberRole_SortKey());		
		
		
		command.redo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);		
		
		// make sure the sort key reference is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getMemberRole_SortKey());		
				
	}	
	
	@Test
	public void testSwitchSortElements() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		Key sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(2, sortKey.getElements().size());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"EMP-FIRST-NAME-0415", 
																		    "EMP-LAST-NAME-0415"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING,
																				   SortSequence.ASCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.LAST);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(true);
		when(sortKeyDescription.isCompressed()).thenReturn(false);		
		
		Command command = 
			new ChangeSortKeysCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(2, sortKey.getElements().size());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testAppendSortElement() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		Key sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(2, sortKey.getElements().size());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"EMP-FIRST-NAME-0415", 
																		    "EMP-LAST-NAME-0415",
																		    "BIRTH-DATE-0415"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING,
																				   SortSequence.ASCENDING,
																				   SortSequence.DESCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(false);
		when(sortKeyDescription.isCompressed()).thenReturn(false);		
		
		Command command = 
			new ChangeSortKeysCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(3, sortKey.getElements().size());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertEquals("BIRTH-DATE-0415", sortKey.getElements().get(2).getElement().getName());
		assertSame(SortSequence.DESCENDING, sortKey.getElements().get(2).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKey.getDuplicatesOption());
		assertFalse(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testInsertSortElement() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		Key sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(2, sortKey.getElements().size());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"BIRTH-DATE-0415",
																			"EMP-FIRST-NAME-0415", 
																		    "EMP-LAST-NAME-0415"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.DESCENDING,
																				   SortSequence.ASCENDING,
																				   SortSequence.ASCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(false);
		when(sortKeyDescription.isCompressed()).thenReturn(false);		
		
		Command command = 
			new ChangeSortKeysCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(3, sortKey.getElements().size());
		assertEquals("BIRTH-DATE-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.DESCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(2).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(2).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKey.getDuplicatesOption());
		assertFalse(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}
	
	@Test
	public void testRemoveSortElement() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		Key sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(2, sortKey.getElements().size());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(1).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(1).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"EMP-LAST-NAME-0415"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.LAST);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(true);
		when(sortKeyDescription.isCompressed()).thenReturn(false);		
		
		Command command = 
			new ChangeSortKeysCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(1, sortKey.getElements().size());
		assertEquals("EMP-LAST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}	
	
	@Test
	public void testMultipleMemberSet() {
	
		// first of all, prepare a multiple member sorted set in the employee demo schema and have
		// the object graph represent the modified schema
		Set set = schema.getSet("COVERAGE-CLAIMS");
		assertSame(SetOrder.LAST, set.getOrder());
		MemberRole[] memberRole = new MemberRole[] {
			set.getMembers().get(0), 
			set.getMembers().get(1), 
			set.getMembers().get(2)
		}; 
		assertEquals("HOSPITAL-CLAIM", memberRole[0].getRecord().getName());		
		assertEquals("NON-HOSP-CLAIM", memberRole[1].getRecord().getName());		
		assertEquals("DENTAL-CLAIM", memberRole[2].getRecord().getName());
		assertNull(memberRole[0].getSortKey());
		assertNull(memberRole[1].getSortKey());
		assertNull(memberRole[2].getSortKey());
		
		ISortKeyDescription[] sortKeyDescriptions = new ISortKeyDescription[3]; 
		
		sortKeyDescriptions[0] = mock(ISortKeyDescription.class);
		when(sortKeyDescriptions[0].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0430"});
		when(sortKeyDescriptions[0].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.DESCENDING});
		when(sortKeyDescriptions[0].getDuplicatesOption()).thenReturn(DuplicatesOption.LAST);
		when(sortKeyDescriptions[0].isNaturalSequence()).thenReturn(true);
		when(sortKeyDescriptions[0].isCompressed()).thenReturn(false);		
		
		sortKeyDescriptions[1] = mock(ISortKeyDescription.class);
		when(sortKeyDescriptions[1].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0445"});
		when(sortKeyDescriptions[1].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		when(sortKeyDescriptions[1].getDuplicatesOption()).thenReturn(DuplicatesOption.FIRST);
		when(sortKeyDescriptions[1].isNaturalSequence()).thenReturn(false);
		when(sortKeyDescriptions[1].isCompressed()).thenReturn(false);
		
		sortKeyDescriptions[2] = mock(ISortKeyDescription.class);
		when(sortKeyDescriptions[2].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0405"});
		when(sortKeyDescriptions[2].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		when(sortKeyDescriptions[2].getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		when(sortKeyDescriptions[2].isNaturalSequence()).thenReturn(false);
		when(sortKeyDescriptions[2].isCompressed()).thenReturn(false);		
		
		Command command = new ChangeSetOrderCommand(set, sortKeyDescriptions);
		
		
		command.execute();		
		objectGraph = TestTools.asObjectGraph(schema);	
		xmi = TestTools.asXmi(schema);
		
		
		// now we're ready to change a sort key in a multiple member set...
		when(sortKeyDescriptions[1].getElementNames()).thenReturn(new String[] {"CLAIM-DATE-0445", 
																				"PATIENT-NAME-0445"});
		when(sortKeyDescriptions[1].getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING, 
																					   SortSequence.DESCENDING});
		
		command = new ChangeSortKeysCommand(set, sortKeyDescriptions);
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		
		Key sortKey = memberRole[0].getSortKey();
		assertNotNull(sortKey);		
		assertEquals(0, memberRole[0].getRecord().getKeys().indexOf(sortKey));	
		assertSame(memberRole[0].getRecord(), sortKey.getRecord());
		assertFalse(sortKey.isCalcKey());
		assertEquals(1, sortKey.getElements().size());
		assertEquals("CLAIM-DATE-0430", sortKey.getElements().get(0).getElement().getName());
		assertEquals(0, sortKey.getElements().get(0).getElement().getKeyElements().indexOf(sortKey.getElements().get(0)));		
		assertEquals(SortSequence.DESCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("CLAIM-DATE-0430 DESCENDING", sortKey.getElementSummary());
		assertEquals(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		sortKey = memberRole[1].getSortKey();
		assertNotNull(sortKey);		
		assertEquals(0, memberRole[1].getRecord().getKeys().indexOf(sortKey));
		assertSame(memberRole[1].getRecord(), sortKey.getRecord());
		assertFalse(sortKey.isCalcKey());
		assertEquals(2, sortKey.getElements().size());
		assertEquals("CLAIM-DATE-0445", sortKey.getElements().get(0).getElement().getName());
		assertEquals(0, sortKey.getElements().get(0).getElement().getKeyElements().indexOf(sortKey.getElements().get(0)));		
		assertEquals(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("PATIENT-NAME-0445", sortKey.getElements().get(1).getElement().getName());
		assertEquals(0, sortKey.getElements().get(1).getElement().getKeyElements().indexOf(sortKey.getElements().get(1)));		
		assertEquals(SortSequence.DESCENDING, sortKey.getElements().get(1).getSortSequence());
		assertEquals("CLAIM-DATE-0445 ASCENDING, PATIENT-NAME-0445 DESCENDING", 
					 sortKey.getElementSummary());
		assertEquals(DuplicatesOption.FIRST, sortKey.getDuplicatesOption());
		assertFalse(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());
		
		sortKey = memberRole[2].getSortKey();
		assertNotNull(sortKey);		
		assertEquals(0, memberRole[2].getRecord().getKeys().indexOf(sortKey));	
		assertSame(memberRole[2].getRecord(), sortKey.getRecord());
		assertFalse(sortKey.isCalcKey());
		assertEquals(1, sortKey.getElements().size());
		assertEquals("CLAIM-DATE-0405", sortKey.getElements().get(0).getElement().getName());
		assertEquals(0, sortKey.getElements().get(0).getElement().getKeyElements().indexOf(sortKey.getElements().get(0)));		
		assertEquals(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("CLAIM-DATE-0405 ASCENDING", sortKey.getElementSummary());
		assertEquals(DuplicatesOption.NOT_ALLOWED, sortKey.getDuplicatesOption());
		assertFalse(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}

	@Test
	public void testChangeVariousOptions() {
		
		Set set = schema.getSet("SKILL-NAME-NDX");
		Key sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(1, sortKey.getElements().size());
		assertEquals("SKILL-NAME-0455", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.NOT_ALLOWED, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"SKILL-NAME-0455"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.DESCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.LAST);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(false);
		when(sortKeyDescription.isCompressed()).thenReturn(true);		
		
		Command command = 
			new ChangeSortKeysCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		Xmi touchedXmi = TestTools.asXmi(schema);
		sortKey = set.getMembers().get(0).getSortKey();
		assertEquals(1, sortKey.getElements().size());
		assertEquals("SKILL-NAME-0455", sortKey.getElements().get(0).getElement().getName());
		assertSame(SortSequence.DESCENDING, sortKey.getElements().get(0).getSortSequence());
		assertFalse(sortKey.isCalcKey());
		assertSame(DuplicatesOption.LAST, sortKey.getDuplicatesOption());
		assertFalse(sortKey.isNaturalSequence());
		assertTrue(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		checkXmi(xmi);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		checkXmi(touchedXmi);
		
	}

}
