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
package org.lh.dmlj.schema.editor.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.area;
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.record;
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.set;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.impl.SchemaFactoryImpl;

@DisplayName("Tools Tests")
public class ToolsTest {
	private static final String LEVEL_1_S_MESSAGE = "Level 1's message";
	private static final String LEVEL_2_S_MESSAGE = "Level 2's message";
	private static final String LEVEL_3_S_MESSAGE = "Level 3's message";

	private static final SchemaFactory schemaFactory = SchemaFactoryImpl.init();
	
	private Schema schema;
	
	private SchemaArea emptyArea = area("name 'AREA1'");		
	private SchemaArea areaWithNonVsamRecord;		
	private SchemaArea areaWithSystemOwner;	
	private SchemaArea areaWithVsamRecord;		
	
	private SchemaRecord nonVsamRecord;
	private SchemaRecord vsamRecord;
	
	private static EList<KeyElement> generateKeyElementList(SortSequence... sortSequences) {
		EList<KeyElement> keyElements = new BasicEList<>();
		for (SortSequence sortSequence : sortSequences) {
			Element element = mock(Element.class);
			when(element.getName()).thenReturn("ELEMENT-" + (keyElements.size() + 1));
			
			KeyElement keyElement = mock(KeyElement.class);
			when(keyElement.getElement()).thenReturn(element);
			when(keyElement.getSortSequence()).thenReturn(sortSequence);
			
			keyElements.add(keyElement);
		}
		return keyElements;
	}
	
	private static MemberRole generateMemberRole(DuplicatesOption duplicatesOption, 
												 SortSequence... sortSequences) {
		Set set = mock(Set.class);
		when(set.getOrder()).thenReturn(SetOrder.SORTED);
		
		EList<KeyElement> keyElements = generateKeyElementList(sortSequences);
		
		Key sortKey = mock(Key.class);
		when(sortKey.getElements()).thenReturn(keyElements);
		when(sortKey.getDuplicatesOption()).thenReturn(duplicatesOption);
		
		MemberRole memberRole = mock(MemberRole.class);
		when(memberRole.getSet()).thenReturn(set);
		when(memberRole.getSortKey()).thenReturn(sortKey);
		
		return memberRole;
	}

	@BeforeEach
	public void setup() {
		// we'll use IDMSNTWK throughout (some) of these tests
		schema = TestTools.getIdmsntwkSchema();
		
		// we also need a number of test records and areas, each containing a combination mix of VSAM and 
		// non-VSAM records and system owners
		nonVsamRecord = record("name 'RECORD1'; area 'AREA1'");
		SystemOwner systemOwner = set("name 'INDEX1'; systemOwner { area 'AREA1' }").getSystemOwner();
		vsamRecord = record("name 'RECORD1'; vsam");
		emptyArea = area("name 'AREA1'");		
		areaWithNonVsamRecord = nonVsamRecord.getAreaSpecification().getArea();		
		areaWithSystemOwner = systemOwner.getAreaSpecification().getArea();			
		areaWithVsamRecord = vsamRecord.getAreaSpecification().getArea();		
	}
	
	@Test
	public void getDefaultSortKeyElementTest() {
		SchemaRecord schemaRecord = mock(SchemaRecord.class);
		
		// no elements in record (list == null, which shouldn't happen in the real world)
		assertNull(Tools.getDefaultSortKeyElement(schemaRecord));
		verify(schemaRecord, times(1)).getElements();
		
		EList<Element> elements = new BasicEList<>();
		when(schemaRecord.getElements()).thenReturn(elements);
		
		// no elements in record (empty list)
		assertNull(Tools.getDefaultSortKeyElement(schemaRecord));	
		verify(schemaRecord, times(3)).getElements();
		
		Element filler = mock(Element.class);
		when(filler.getName()).thenReturn("FILLER");
		when(filler.getLength()).thenReturn(Short.valueOf((short) 10));
		elements.add(filler);
		
		// the only element available is a FILLER
		assertNull(Tools.getDefaultSortKeyElement(schemaRecord));	
		verify(filler, times(1)).getName();
		verify(filler, never()).getLength();
		
		Element length257Element = mock(Element.class);
		when(length257Element.getName()).thenReturn("A");
		when(length257Element.getLength()).thenReturn(Short.valueOf((short) 257));
		elements.add(length257Element);	
		
		// the only non-FILLER element's length is bigger than 256
		assertNull(Tools.getDefaultSortKeyElement(schemaRecord));	
		verify(length257Element, times(1)).getName();
		verify(length257Element, times(1)).getLength();	
		
		Element redefinesElement = mock(Element.class);
		when(redefinesElement.getName()).thenReturn("B");
		when(redefinesElement.getLength()).thenReturn(Short.valueOf((short) 256));
		when(redefinesElement.getRedefines()).thenReturn(length257Element);
		elements.add(redefinesElement);
		
		// elements that redefine another element are discarded
		assertNull(Tools.getDefaultSortKeyElement(schemaRecord));	
		verify(redefinesElement, times(1)).getName();
		verify(redefinesElement, times(1)).getLength();	
		verify(redefinesElement, times(1)).getRedefines();
				
		Element occursElement = mock(Element.class);
		when(occursElement.getName()).thenReturn("C");
		when(occursElement.getLength()).thenReturn(Short.valueOf((short) 5));
		OccursSpecification occursSpecification = mock(OccursSpecification.class);
		when(occursElement.getOccursSpecification()).thenReturn(occursSpecification);
		elements.add(occursElement);
		
		// elements described with an OCCURS clause are discarded
		assertNull(Tools.getDefaultSortKeyElement(schemaRecord));	
		verify(occursElement, times(1)).getName();
		verify(occursElement, times(1)).getLength();	
		verify(occursElement, times(1)).getOccursSpecification();	
		
		Element expectedKeyElement = mock(Element.class);
		when(expectedKeyElement.getName()).thenReturn("D");
		when(expectedKeyElement.getLength()).thenReturn(Short.valueOf((short) 5));
		elements.add(expectedKeyElement);
		
		// default key element available
		Element actualKeyElement = Tools.getDefaultSortKeyElement(schemaRecord);
		assertSame(expectedKeyElement, actualKeyElement);
		verify(expectedKeyElement, times(1)).getName();
		verify(expectedKeyElement, times(1)).getLength();	
		verify(expectedKeyElement, times(1)).getRedefines();
		verify(expectedKeyElement, times(1)).getOccursSpecification();		
		
	}
	
	@Test
	public void testIsInvolvedInOccurs() {
		
		SchemaRecord schemaRecord = schema.getRecord("SDES-044");
		assertNotNull(schemaRecord);	
		
		Element element = schemaRecord.getElement("VALS-044");
		assertNotNull(element);
		assertEquals(false, Tools.isInvolvedInOccurs(element), "element has no OCCURS clause");
		
		element = schemaRecord.getElement("VAL-TEXT-044");
		assertNotNull(element);
		assertEquals(false, Tools.isInvolvedInOccurs(element), "element's direct parent has no OCCURS clause");
		
		element = schemaRecord.getElement("VAL1-044");
		assertNotNull(element);
		assertEquals(false, Tools.isInvolvedInOccurs(element), "element's indirect parent has no OCCURS clause");
		
		schemaRecord = schema.getRecord("SAM-056");
		assertNotNull(schemaRecord);
		
		element = schemaRecord.getElement("KEYS-056");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInOccurs(element), "element has a OCCURS clause");		
		
		element = schemaRecord.getElement("KEY-FLD-056");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInOccurs(element), "element's direct parent has an OCCURS clause");
		
		schemaRecord = schema.getRecord("LOADCTL-158");
		assertNotNull(schemaRecord);
		
		element = schemaRecord.getElement("LOADCTL-ACONLEN-158");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInOccurs(element), "element's indirect parent has an OCCURS clause");
		
	}

	@Test
	public void testIsInvolvedInRedefines() {
		
		SchemaRecord schemaRecord = schema.getRecord("SDES-044");
		assertNotNull(schemaRecord);
		
		Element element = schemaRecord.getElement("CMT-044");
		assertNotNull(element);
		assertEquals(false, Tools.isInvolvedInRedefines(element), "element has no REDEFINES clause");	
		
		element = schemaRecord.getElement("CMT-INFO-044");
		assertNotNull(element);
		assertEquals(false, Tools.isInvolvedInRedefines(element), "element's direct parent has no REDEFINES clause");	
		
		element = schemaRecord.getElement("VALS-044");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInRedefines(element), "element has a REDEFINES clause");
		
		element = schemaRecord.getElement("VAL-TEXT-044");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInRedefines(element), "element's direct parent has a REDEFINES clause");
		
		element = schemaRecord.getElement("VAL2-044");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInRedefines(element), "element's indirect parent has a REDEFINES clause");
		
		element = schemaRecord.getElement("ISEQ-044");
		assertNotNull(element);
		assertEquals(true, Tools.isInvolvedInRedefines(element), "element's indirect parent has a REDEFINES clause");
		
	}
	
	@Test
	public void testGetSortkeysOneElementAscending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.NOT_ALLOWED, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1) DN", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysTwoElementsAscending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1,\n\tELEMENT-2) DF", sortKeysAsString);		
	}	
	
	@Test
	public void testGetSortkeysThreeElementsAscending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.ASCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1,\n\tELEMENT-2,\n\tELEMENT-3) DF", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysOneElementDescending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.NOT_ALLOWED, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1) DN", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysTwoElementsDescending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.DESCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1,\n\tELEMENT-2) DF", sortKeysAsString);		
	}	
	
	@Test
	public void testGetSortkeysThreeElementsDescending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.DESCENDING, SortSequence.DESCENDING, 
							   SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1,\n\tELEMENT-2,\n\tELEMENT-3) DF", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysThreeElementsMixed1() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING, 
							   SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2),\nASC (ELEMENT-3) DF", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysThreeElementsMixed2() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.BY_DBKEY, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING, 
							   SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1),\nASC (ELEMENT-2),\nDESC (ELEMENT-3) DD", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysFourElementsMixed1() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2,\n\tELEMENT-3),\nASC (ELEMENT-4) DF", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysFourElementsMixed2() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1),\nASC (ELEMENT-2,\n\tELEMENT-3),\nDESC (ELEMENT-4) DL", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysFourElementsMixed3() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1),\nASC (ELEMENT-2),\nDESC (ELEMENT-3),\nASC (ELEMENT-4) DL", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysFourElementsMixed4() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2),\nASC (ELEMENT-3),\nDESC (ELEMENT-4) DL", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysFourElementsMixed5() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING,
							   SortSequence.DESCENDING, SortSequence.DESCENDING,
							   SortSequence.ASCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2,\n\tELEMENT-3,\n\tELEMENT-4),\nASC (ELEMENT-5),\nDESC (ELEMENT-6) DL", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeysDbkey() {
		Set set = mock(Set.class);
		when(set.getOrder()).thenReturn(SetOrder.SORTED);
		KeyElement keyElement = mock(KeyElement.class);
		when(keyElement.isDbkey()).thenReturn(true);
		when (keyElement.getSortSequence()).thenReturn(SortSequence.ASCENDING);
		EList<KeyElement> keyElements = new BasicEList<>();
		keyElements.add(keyElement);
		Key sortKey = mock(Key.class);
		when(sortKey.getElements()).thenReturn(keyElements);
		when(sortKey.getDuplicatesOption()).thenReturn(DuplicatesOption.NOT_ALLOWED);
		MemberRole memberRole = mock(MemberRole.class);
		when(memberRole.getSet()).thenReturn(set);
		when(memberRole.getSortKey()).thenReturn(sortKey);
		
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (DBKEY) DN", sortKeysAsString);
	}
	
	@Test
	public void testCanHoldNonVsamRecordsemptyArea() {		
		assertTrue(Tools.canHoldNonVsamRecords(emptyArea));
	}
	
	@Test
	public void testCanHoldNonVsamRecordsAreaWithNonVsamRecord() {	
		assertTrue(Tools.canHoldNonVsamRecords(areaWithNonVsamRecord));	
	}
	
	@Test
	public void testCanHoldNonVsamRecordsAreaWithSystemOwner() {	
		assertTrue(Tools.canHoldNonVsamRecords(areaWithSystemOwner));
	}
	
	@Test
	public void testCanHoldNonVsamRecordsAreaWithVsamRecord() {		
		assertFalse(Tools.canHoldNonVsamRecords(areaWithVsamRecord));
	}
	
	@Test
	public void testCanHoldSystemOwnersEmptyArea() {
		assertTrue(Tools.canHoldSystemOwners(emptyArea));
	}	
	
	@Test
	public void testCanHoldSystemOwnersAreaWithNonVsamRecord() {	
		assertTrue(Tools.canHoldSystemOwners(areaWithNonVsamRecord));
	}
	
	@Test
	public void testCanHoldSystemOwnersAreaWithSystemOwner() {	
		assertTrue(Tools.canHoldSystemOwners(areaWithSystemOwner));
	}
	
	@Test
	public void testCanHoldSystemOwnersAreaWithVsamRecord() {	
		assertFalse(Tools.canHoldSystemOwners(areaWithVsamRecord));
	}
	
	@Test
	public void testCanHoldVsamRecordsEmptyArea() {		
		assertTrue(Tools.canHoldVsamRecords(emptyArea));
	}
	
	@Test
	public void testCanHoldVsamRecordsAreaWithNonVsamRecord() {
		assertFalse(Tools.canHoldVsamRecords(areaWithNonVsamRecord));
	}
	
	@Test
	public void testCanHoldVsamRecordsAreaWithSystemOwner() {
		assertFalse(Tools.canHoldVsamRecords(areaWithSystemOwner));	
	}
	
	@Test
	public void testCanHoldVsamRecordsAreaWithVsamRecord() {
		assertTrue(Tools.canHoldVsamRecords(areaWithVsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordNonVsamRecordEmptyArea() {
		assertTrue(Tools.areaMixesWithRecord(emptyArea, nonVsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordNonVsamRecordAreaWithNonVsamRecord() {
		assertTrue(Tools.areaMixesWithRecord(areaWithNonVsamRecord, nonVsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordNonVsamRecordAreaWithSystemOwner() {
		assertTrue(Tools.areaMixesWithRecord(areaWithSystemOwner, nonVsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordNonVsamRecordAreaWithVsamRecord() {
		assertFalse(Tools.areaMixesWithRecord(areaWithVsamRecord, nonVsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordVsamRecordEmptyArea() {
		assertTrue(Tools.areaMixesWithRecord(emptyArea, vsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordVsamRecordAreaWithNonVsamRecord() {
		assertFalse(Tools.areaMixesWithRecord(areaWithNonVsamRecord, vsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordVsamRecordAreaWithSystemOwner() {
		assertFalse(Tools.areaMixesWithRecord(areaWithSystemOwner, vsamRecord));
	}
	
	@Test
	public void testAreaMixesWithRecordVsamRecordAreaWithVsamRecord() {
		assertTrue(Tools.areaMixesWithRecord(areaWithVsamRecord, vsamRecord));
	}
	
	@Test
	public void testGetRootMessageWithTopLevelMessage() {
		
		Throwable level3 = mock(Throwable.class);
		when(level3.getMessage()).thenReturn(LEVEL_3_S_MESSAGE);
		when(level3.getCause()).thenReturn(null);
		
		Throwable level2 = mock(Throwable.class);
		when(level2.getMessage()).thenReturn(LEVEL_2_S_MESSAGE);
		when(level2.getCause()).thenReturn(level3);
		
		Throwable level1 = mock(Throwable.class);
		when(level1.getMessage()).thenReturn(LEVEL_1_S_MESSAGE);
		when(level1.getCause()).thenReturn(level2);
		
		assertEquals(LEVEL_3_S_MESSAGE, Tools.getRootMessage(level1));
	}
	
	@Test
	public void testGetRootMessageWithoutTopLevelMessage() {
		
		Throwable level3 = mock(Throwable.class);
		when(level3.getMessage()).thenReturn(LEVEL_3_S_MESSAGE);
		when(level3.getCause()).thenReturn(null);
		
		Throwable level2 = mock(Throwable.class);
		when(level2.getMessage()).thenReturn(LEVEL_2_S_MESSAGE);
		when(level2.getCause()).thenReturn(level3);
		
		Throwable level1 = mock(Throwable.class);
		when(level1.getMessage()).thenReturn(null);
		when(level1.getCause()).thenReturn(level2);
		
		assertEquals(LEVEL_3_S_MESSAGE, Tools.getRootMessage(level1));
		
		when(level1.getMessage()).thenReturn("");
		assertEquals(LEVEL_3_S_MESSAGE, Tools.getRootMessage(level1));
		
		when(level1.getMessage()).thenReturn(" ");
		assertEquals(LEVEL_3_S_MESSAGE, Tools.getRootMessage(level1));
	}
	
	@Test
	public void testGetRootMessageWithoutBottomLevelMessage() {
		
		Throwable level3 = mock(Throwable.class);
		when(level3.getMessage()).thenReturn(null);
		when(level3.getCause()).thenReturn(null);
		
		Throwable level2 = mock(Throwable.class);
		when(level2.getMessage()).thenReturn(LEVEL_2_S_MESSAGE);
		when(level2.getCause()).thenReturn(level3);
		
		Throwable level1 = mock(Throwable.class);
		when(level1.getMessage()).thenReturn(LEVEL_1_S_MESSAGE);
		when(level1.getCause()).thenReturn(level2);
		
		assertEquals(LEVEL_2_S_MESSAGE, Tools.getRootMessage(level1));
		
		when(level3.getMessage()).thenReturn("");
		assertEquals(LEVEL_2_S_MESSAGE, Tools.getRootMessage(level1));
		
		when(level3.getMessage()).thenReturn(" ");
		assertEquals(LEVEL_2_S_MESSAGE, Tools.getRootMessage(level1));
	}
	
	@Test
	public void testGetRootMessageWithoutAnylMessage() {
		
		Throwable level3 = mock(Throwable.class);
		when(level3.getMessage()).thenReturn(null);
		when(level3.getCause()).thenReturn(null);
		
		Throwable level2 = mock(Throwable.class);
		when(level2.getMessage()).thenReturn(null);
		when(level2.getCause()).thenReturn(level3);
		
		Throwable level1 = mock(Throwable.class);
		when(level1.getMessage()).thenReturn(null);
		when(level1.getCause()).thenReturn(level2);
		
		assertEquals("An error occurred", Tools.getRootMessage(level1));
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition1() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 1);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setNextDbkeyPosition((short) 2);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(3, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition2() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 2);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setNextDbkeyPosition((short) 1);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(3, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition3() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 1);
		ownerRole.setPriorDbkeyPosition((short) 2);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setNextDbkeyPosition((short) 3);
		memberRole.setPriorDbkeyPosition((short) 4);
		memberRole.setOwnerDbkeyPosition((short) 5);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(6, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition4() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 2);
		ownerRole.setPriorDbkeyPosition((short) 1);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setNextDbkeyPosition((short) 4);
		memberRole.setPriorDbkeyPosition((short) 5);
		memberRole.setOwnerDbkeyPosition((short) 3);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(6, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition5() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 4);
		ownerRole.setPriorDbkeyPosition((short) 5);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setNextDbkeyPosition((short) 1);
		memberRole.setPriorDbkeyPosition((short) 2);
		memberRole.setOwnerDbkeyPosition((short) 3);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(6, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition6() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 1);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setIndexDbkeyPosition((short) 2);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(3, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition7() {
		var ownerRole = schemaFactory.createOwnerRole();
		ownerRole.setNextDbkeyPosition((short) 2);
		
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setIndexDbkeyPosition((short) 1);
		
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole);
		schemaRecord.getMemberRoles().add(memberRole);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(3, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition8() {
		var ownerRole1 = schemaFactory.createOwnerRole();
		ownerRole1.setNextDbkeyPosition((short) 1);
		var ownerRole2 = schemaFactory.createOwnerRole();
		ownerRole1.setNextDbkeyPosition((short) 2);
				
		var schemaRecord = schemaFactory.createSchemaRecord();
		schemaRecord.getOwnerRoles().add(ownerRole1);
		schemaRecord.getOwnerRoles().add(ownerRole2);
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(3, firstAvailablePointerPosition);
	}
	
	@Test
	public void testGetFirstAvailablePointerPosition9() {
		var schemaRecord = schemaFactory.createSchemaRecord();
		
		var firstAvailablePointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		
		assertEquals(1, firstAvailablePointerPosition);
	}
	
	@Test
	public void getPointersForChainedSetWithAllPointers() {
		var set = schemaFactory.createSet();
		set.setMode(SetMode.CHAINED);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);
		memberRole.setNextDbkeyPosition((short) 1);
		memberRole.setPriorDbkeyPosition((short) 2);
		memberRole.setOwnerDbkeyPosition((short) 3);
		
		assertEquals("NPO", Tools.getPointers(memberRole));
	}
	
	@Test
	public void getPointersForChainedSetWithOnlyNextPointer() {
		var set = schemaFactory.createSet();
		set.setMode(SetMode.CHAINED);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);
		memberRole.setNextDbkeyPosition((short) 1);
		
		assertEquals("N", Tools.getPointers(memberRole));
	}
	
	@Test
	public void getPointersForIndexedSetIndexPointerOmitted() {
		var set = schemaFactory.createSet();
		set.setMode(SetMode.INDEXED);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);		
		
		assertEquals("-", Tools.getPointers(memberRole));
	}
	
	@Test
	public void getPointersForIndexedSetWithAllPointers() {
		var set = schemaFactory.createSet();
		set.setMode(SetMode.INDEXED);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);
		memberRole.setIndexDbkeyPosition((short) 1);
		memberRole.setOwnerDbkeyPosition((short) 2);
		
		assertEquals("IO", Tools.getPointers(memberRole));
	}
	
	@Test
	public void getPointersForIndexedSetWithOnlyIndexPointer() {
		var set = schemaFactory.createSet();
		set.setMode(SetMode.INDEXED);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);
		memberRole.setIndexDbkeyPosition((short) 1);
		
		assertEquals("I", Tools.getPointers(memberRole));
	}
	
	@Test
	public void getPointersForIndexedSetWithOnlyOwnerPointer() {
		// note: this is not a realistic situation
		var set = schemaFactory.createSet();
		set.setMode(SetMode.INDEXED);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);
		memberRole.setOwnerDbkeyPosition((short) 1);
		
		assertEquals("O", Tools.getPointers(memberRole));
	}
	
	@Test
	public void getPointersForVsamIndex() {
		var set = schemaFactory.createSet();
		set.setMode(SetMode.VSAM_INDEX);
		var memberRole = schemaFactory.createMemberRole();
		memberRole.setSet(set);
		
		assertEquals("", Tools.getPointers(memberRole));
	}

}
