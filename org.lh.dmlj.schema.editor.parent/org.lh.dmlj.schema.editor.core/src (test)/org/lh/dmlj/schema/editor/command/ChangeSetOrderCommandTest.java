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
package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.*;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
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

public class ChangeSetOrderCommandTest {
	
	private ObjectGraph objectGraph;
	private Schema 		schema;	

	private void checkObjectGraph(ObjectGraph expected) {
		ObjectGraph actual = TestTools.asObjectGraph(schema);
		assertEquals(expected, actual);		
	}
	
	@Before
	public void setup() {
		// we'll use EMPSCHM throughout these tests
		schema = TestTools.getEmpschmSchema();
		objectGraph = TestTools.asObjectGraph(schema);
	}
	
	@Test
	public void testAnnotations() {
		
		Set set = schema.getSet("EMP-EMPOSITION");		
		
		Command command = new ChangeSetOrderCommand(set, SetOrder.LAST);
		
		
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
		
		// make sure the set order attribute is set
		EStructuralFeature[] features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSet_Order());		
		
		
		command.undo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);		
		
		// make sure the set order attribute is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSet_Order());		
		
		
		command.redo();
		
		// make sure the owner is still set
		owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);		
		
		// make sure the set order attribute is still set
		features = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Features.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertEquals(1, features.length);
		assertTrue(features[0] == SchemaPackage.eINSTANCE.getSet_Order());		
				
	}	
	
	@Test
	public void testUnsortedToFirst() {
		
		Set set = schema.getSet("COVERAGE-CLAIMS"); // this is a multiple-member set
		assertSame(SetOrder.LAST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		assertNull(set.getMembers().get(1).getSortKey());
		assertNull(set.getMembers().get(2).getSortKey());
		
		Command command = new ChangeSetOrderCommand(set, SetOrder.FIRST);
		
		command.execute();		
		assertSame(SetOrder.FIRST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		assertNull(set.getMembers().get(1).getSortKey());
		assertNull(set.getMembers().get(2).getSortKey());
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		
		command.undo();		
		checkObjectGraph(objectGraph);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}	
	
	@Test
	public void testUnsortedToLast() {
		
		Set set = schema.getSet("EMP-EMPOSITION");
		assertSame(SetOrder.FIRST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		
		Command command = new ChangeSetOrderCommand(set, SetOrder.LAST);
		
		command.execute();		
		assertSame(SetOrder.LAST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		
		command.undo();		
		checkObjectGraph(objectGraph);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}
	
	@Test
	public void testUnsortedToNext() {
		
		Set set = schema.getSet("EMP-EMPOSITION");
		assertSame(SetOrder.FIRST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		
		Command command = new ChangeSetOrderCommand(set, SetOrder.NEXT);
		
		command.execute();		
		assertSame(SetOrder.NEXT, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		
		command.undo();		
		checkObjectGraph(objectGraph);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}	
	
	@Test
	public void testUnsortedToPrior() {
		
		Set set = schema.getSet("EMP-EMPOSITION");
		assertSame(SetOrder.FIRST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		
		Command command = new ChangeSetOrderCommand(set, SetOrder.PRIOR);
		
		command.execute();		
		assertSame(SetOrder.PRIOR, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		
		command.undo();		
		checkObjectGraph(objectGraph);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}	
	
	@Test
	public void testSortedToFirst() {
		
		Set set = schema.getSet("DEPT-EMPLOYEE");
		assertSame(SetOrder.SORTED, set.getOrder());
		assertNotNull(set.getMembers().get(0).getSortKey());
		SchemaRecord recordEmployee = set.getMembers().get(0).getRecord();
		Element elementLastName = recordEmployee.getElement("EMP-LAST-NAME-0415");
		assertNotNull(elementLastName);
		assertEquals(3, elementLastName.getKeyElements().size());
		assertEquals("DEPT-EMPLOYEE", 
					 elementLastName.getKeyElements().get(0).getKey().getMemberRole().getSet().getName());
		assertEquals("EMP-NAME-NDX", 
					 elementLastName.getKeyElements().get(1).getKey().getMemberRole().getSet().getName());
		assertEquals("OFFICE-EMPLOYEE", 
					 elementLastName.getKeyElements().get(2).getKey().getMemberRole().getSet().getName());
		Element elementFirstName = recordEmployee.getElement("EMP-FIRST-NAME-0415");
		assertNotNull(elementFirstName);
		assertEquals(3, elementFirstName.getKeyElements().size());
		assertEquals("DEPT-EMPLOYEE", 
					 elementFirstName.getKeyElements().get(0).getKey().getMemberRole().getSet().getName());
		assertEquals("EMP-NAME-NDX", 
					 elementFirstName.getKeyElements().get(1).getKey().getMemberRole().getSet().getName());
		assertEquals("OFFICE-EMPLOYEE", 
					 elementFirstName.getKeyElements().get(2).getKey().getMemberRole().getSet().getName());
		
		Command command = new ChangeSetOrderCommand(set, SetOrder.FIRST);
		
		
		command.execute();		
		assertSame(SetOrder.FIRST, set.getOrder());
		assertNull(set.getMembers().get(0).getSortKey());
		assertEquals(2, elementLastName.getKeyElements().size());
		assertEquals("EMP-NAME-NDX", 
				 	 elementLastName.getKeyElements().get(0).getKey().getMemberRole().getSet().getName());
		assertEquals("OFFICE-EMPLOYEE", 
				 	 elementLastName.getKeyElements().get(1).getKey().getMemberRole().getSet().getName());
		assertEquals(2, elementFirstName.getKeyElements().size());
		assertEquals("EMP-NAME-NDX", 
				 	 elementFirstName.getKeyElements().get(0).getKey().getMemberRole().getSet().getName());
		assertEquals("OFFICE-EMPLOYEE", 
				 	 elementFirstName.getKeyElements().get(1).getKey().getMemberRole().getSet().getName());
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}
	
	@Test
	public void testUnsortedToSorted_SingleMemberSet() {
	
		Set set = schema.getSet("EMP-EMPOSITION");
		assertSame(SetOrder.FIRST, set.getOrder());
		MemberRole memberRole = set.getMembers().get(0); 
		assertNull(memberRole.getSortKey());		
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"START-DATE-0420", 
																		    "FINISH-DATE-0420"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING,
																				   SortSequence.DESCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(true);
		when(sortKeyDescription.isCompressed()).thenReturn(false);
		
		Command command = 
			new ChangeSetOrderCommand(set, new ISortKeyDescription[] {sortKeyDescription});
		
		
		command.execute();		
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		assertSame(SetOrder.SORTED, set.getOrder());				
		Key sortKey = memberRole.getSortKey();
		assertNotNull(sortKey);		
		assertEquals(0, memberRole.getRecord().getKeys().indexOf(sortKey));		
		assertSame(memberRole.getRecord(), sortKey.getRecord());
		assertFalse(sortKey.isCalcKey());
		assertEquals(2, sortKey.getElements().size());
		assertEquals("START-DATE-0420", sortKey.getElements().get(0).getElement().getName());
		assertEquals(0, sortKey.getElements().get(0).getElement().getKeyElements().indexOf(sortKey.getElements().get(0)));		
		assertEquals(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("FINISH-DATE-0420", sortKey.getElements().get(1).getElement().getName());
		assertEquals(0, sortKey.getElements().get(1).getElement().getKeyElements().indexOf(sortKey.getElements().get(1)));
		assertEquals(SortSequence.DESCENDING, sortKey.getElements().get(1).getSortSequence());
		assertEquals("START-DATE-0420 ASCENDING, FINISH-DATE-0420 DESCENDING", 
					 sortKey.getElementSummary());
		assertEquals(DuplicatesOption.NOT_ALLOWED, sortKey.getDuplicatesOption());
		assertTrue(sortKey.isNaturalSequence());
		assertFalse(sortKey.isCompressed());		
		
		command.undo();		
		checkObjectGraph(objectGraph);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);		
		
	}
	
	@Test
	public void testUnsortedToSorted_MultipleMemberSet() {
	
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
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		assertSame(SetOrder.SORTED, set.getOrder());				
		
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
		assertEquals(1, sortKey.getElements().size());
		assertEquals("CLAIM-DATE-0445", sortKey.getElements().get(0).getElement().getName());
		assertEquals(0, sortKey.getElements().get(0).getElement().getKeyElements().indexOf(sortKey.getElements().get(0)));		
		assertEquals(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("CLAIM-DATE-0445 ASCENDING", sortKey.getElementSummary());
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
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);		
		
	}	
	
	@Test
	public void testUnsortedToSorted_IndexedSet() {
		
		// first of all: we need to create a new indexed set because none of the indexed employee 
		// demo sets is unsorted; creating a new system owned indexed set always results in an
		// unsorted indexed set
		SchemaRecord recordEmployee = schema.getRecord("EMPLOYEE");
		assertNotNull(recordEmployee);
		new CreateIndexCommand(recordEmployee).execute();
		Set set = schema.getSet("NEW-INDEX-1");
		assertNotNull(set);		
		assertSame(SetOrder.LAST, set.getOrder());
		objectGraph = TestTools.asObjectGraph(schema); // our new reference object graph
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"EMP-FIRST-NAME-0415"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.BY_DBKEY);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(false);
		when(sortKeyDescription.isCompressed()).thenReturn(true);
		
		Command command = 
			new ChangeSetOrderCommand(set, new ISortKeyDescription[] {sortKeyDescription});	
		
		
		command.execute();
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		assertSame(SetOrder.SORTED, set.getOrder());				
		Key sortKey = set.getMembers().get(0).getSortKey();
		assertNotNull(sortKey);		
		assertEquals(4, recordEmployee.getKeys().indexOf(sortKey));
		assertSame(recordEmployee, sortKey.getRecord());
		assertFalse(sortKey.isCalcKey());
		assertEquals(1, sortKey.getElements().size());
		assertEquals("EMP-FIRST-NAME-0415", sortKey.getElements().get(0).getElement().getName());
		assertEquals(3, sortKey.getElements().get(0).getElement().getKeyElements().indexOf(sortKey.getElements().get(0)));		
		assertEquals(SortSequence.ASCENDING, sortKey.getElements().get(0).getSortSequence());
		assertEquals("EMP-FIRST-NAME-0415 ASCENDING", sortKey.getElementSummary());
		assertEquals(DuplicatesOption.BY_DBKEY, sortKey.getDuplicatesOption());
		assertFalse(sortKey.isNaturalSequence());
		assertTrue(sortKey.isCompressed());		
		
		
		command.undo();
		checkObjectGraph(objectGraph);
		
		
		command.redo();
		checkObjectGraph(touchedObjectGraph);
		
	}
	
	@Test
	public void testSetSupplierConstructor() {
		final Set set = schema.getSet("EMP-EMPOSITION");
		assertSame(SetOrder.FIRST, set.getOrder());
		MemberRole memberRole = set.getMembers().get(0); 
		assertNull(memberRole.getSortKey());		
		
		ISortKeyDescription sortKeyDescription = mock(ISortKeyDescription.class);
		when(sortKeyDescription.getElementNames()).thenReturn(new String[] {"START-DATE-0420", 
																		    "FINISH-DATE-0420"});
		when(sortKeyDescription.getSortSequences()).thenReturn(new SortSequence[] {SortSequence.ASCENDING,
																				   SortSequence.DESCENDING});
		when(sortKeyDescription.getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		when(sortKeyDescription.isNaturalSequence()).thenReturn(true);
		when(sortKeyDescription.isCompressed()).thenReturn(false);
		ISupplier<Set> setSupplier = new ISupplier<Set>() {
			@Override
			public Set supply() {
				return set;
			}
		};
		Command command = 
			new ChangeSetOrderCommand(setSupplier, new ISortKeyDescription[] {sortKeyDescription});
		command.execute();
		Set owner = ModelChangeDispatcher.getAnnotatedFieldValue(
			command, 
			Owner.class, 
			ModelChangeDispatcher.Availability.MANDATORY);
		assertSame(set, owner);
	}

}
