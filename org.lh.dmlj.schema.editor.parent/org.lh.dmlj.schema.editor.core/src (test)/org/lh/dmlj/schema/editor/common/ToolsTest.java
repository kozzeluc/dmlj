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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OccursSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class ToolsTest {
	
	private Schema schema;
	
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

	@Before
	public void setup() {
		// we'll use IDMSNTWK throughout these tests
		schema = TestTools.getIdmsntwkSchema();
	}
	
	@Test
	public void getDefaultSortKeyElementTest() {
		
		SchemaRecord record = mock(SchemaRecord.class);
		
		// no elements in record (list == null, which shouldn't happen in the real world)
		assertNull(Tools.getDefaultSortKeyElement(record));
		verify(record, times(1)).getElements();
		
		EList<Element> elements = new BasicEList<>();
		when(record.getElements()).thenReturn(elements);
		
		// no elements in record (empty list)
		assertNull(Tools.getDefaultSortKeyElement(record));	
		verify(record, times(3)).getElements();
		
		Element filler = mock(Element.class);
		when(filler.getName()).thenReturn("FILLER");
		when(filler.getLength()).thenReturn(Short.valueOf((short) 10));
		elements.add(filler);
		
		// the only element available is a FILLER
		assertNull(Tools.getDefaultSortKeyElement(record));	
		verify(filler, times(1)).getName();
		verify(filler, never()).getLength();
		
		Element length257Element = mock(Element.class);
		when(length257Element.getName()).thenReturn("A");
		when(length257Element.getLength()).thenReturn(Short.valueOf((short) 257));
		elements.add(length257Element);	
		
		// the only non-FILLER element's length is bigger than 256
		assertNull(Tools.getDefaultSortKeyElement(record));	
		verify(length257Element, times(1)).getName();
		verify(length257Element, times(1)).getLength();	
		
		Element redefinesElement = mock(Element.class);
		when(redefinesElement.getName()).thenReturn("B");
		when(redefinesElement.getLength()).thenReturn(Short.valueOf((short) 256));
		when(redefinesElement.getRedefines()).thenReturn(length257Element);
		elements.add(redefinesElement);
		
		// elements that redefine another element are discarded
		assertNull(Tools.getDefaultSortKeyElement(record));	
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
		assertNull(Tools.getDefaultSortKeyElement(record));	
		verify(occursElement, times(1)).getName();
		verify(occursElement, times(1)).getLength();	
		verify(occursElement, times(1)).getOccursSpecification();	
		
		Element expectedKeyElement = mock(Element.class);
		when(expectedKeyElement.getName()).thenReturn("D");
		when(expectedKeyElement.getLength()).thenReturn(Short.valueOf((short) 5));
		elements.add(expectedKeyElement);
		
		// default key element available
		Element actualKeyElement = Tools.getDefaultSortKeyElement(record);
		assertSame(expectedKeyElement, actualKeyElement);
		verify(expectedKeyElement, times(1)).getName();
		verify(expectedKeyElement, times(1)).getLength();	
		verify(expectedKeyElement, times(1)).getRedefines();
		verify(expectedKeyElement, times(1)).getOccursSpecification();		
		
	}
	
	@Test
	public void testIsInvolvedInOccurs() {
		
		SchemaRecord record = schema.getRecord("SDES-044");
		assertNotNull(record);	
		
		Element element = record.getElement("VALS-044");
		assertNotNull(element);
		assertEquals("element has no OCCURS clause", false, Tools.isInvolvedInOccurs(element));
		
		element = record.getElement("VAL-TEXT-044");
		assertNotNull(element);
		assertEquals("element's direct parent has no OCCURS clause", false, 
					 Tools.isInvolvedInOccurs(element));
		
		element = record.getElement("VAL1-044");
		assertNotNull(element);
		assertEquals("element's indirect parent has no OCCURS clause", false, 
					 Tools.isInvolvedInOccurs(element));
		
		record = schema.getRecord("SAM-056");
		assertNotNull(record);
		
		element = record.getElement("KEYS-056");
		assertNotNull(element);
		assertEquals("element has a OCCURS clause", true, Tools.isInvolvedInOccurs(element));		
		
		element = record.getElement("KEY-FLD-056");
		assertNotNull(element);
		assertEquals("element's direct parent has an OCCURS clause", true, 
					 Tools.isInvolvedInOccurs(element));
		
		record = schema.getRecord("LOADCTL-158");
		assertNotNull(record);
		
		element = record.getElement("LOADCTL-ACONLEN-158");
		assertNotNull(element);
		assertEquals("element's indirect parent has an OCCURS clause", true, 
					 Tools.isInvolvedInOccurs(element));
		
	}

	@Test
	public void testIsInvolvedInRedefines() {
		
		SchemaRecord record = schema.getRecord("SDES-044");
		assertNotNull(record);
		
		Element element = record.getElement("CMT-044");
		assertNotNull(element);
		assertEquals("element has no REDEFINES clause", false, 
					 Tools.isInvolvedInRedefines(element));	
		
		element = record.getElement("CMT-INFO-044");
		assertNotNull(element);
		assertEquals("element's direct parent has no REDEFINES clause", false, 
					 Tools.isInvolvedInRedefines(element));	
		
		element = record.getElement("VALS-044");
		assertNotNull(element);
		assertEquals("element has a REDEFINES clause", true, Tools.isInvolvedInRedefines(element));
		
		element = record.getElement("VAL-TEXT-044");
		assertNotNull(element);
		assertEquals("element's direct parent has a REDEFINES clause", true, 
					 Tools.isInvolvedInRedefines(element));
		
		element = record.getElement("VAL2-044");
		assertNotNull(element);
		assertEquals("element's indirect parent has a REDEFINES clause", true, 
					 Tools.isInvolvedInRedefines(element));
		
		element = record.getElement("ISEQ-044");
		assertNotNull(element);
		assertEquals("element's indirect parent has a REDEFINES clause", true, 
					 Tools.isInvolvedInRedefines(element));
		
	}
	
	@Test
	public void testGetSortkeys_One_Element_Ascending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.NOT_ALLOWED, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1) DN", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Two_Elements_Ascending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1,\n\tELEMENT-2) DF", sortKeysAsString);		
	}	
	
	@Test
	public void testGetSortkeys_Three_Elements_Ascending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.ASCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1,\n\tELEMENT-2,\n\tELEMENT-3) DF", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_One_Element_Descending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.NOT_ALLOWED, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1) DN", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Two_Elements_Descending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.DESCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1,\n\tELEMENT-2) DF", sortKeysAsString);		
	}	
	
	@Test
	public void testGetSortkeys_Three_Elements_Descending() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.DESCENDING, SortSequence.DESCENDING, 
							   SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1,\n\tELEMENT-2,\n\tELEMENT-3) DF", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Three_Elements_Mixed_1() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING, 
							   SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2),\nASC (ELEMENT-3) DF", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Three_Elements_Mixed_2() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.BY_DBKEY, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING, 
							   SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1),\nASC (ELEMENT-2),\nDESC (ELEMENT-3) DD", sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Four_Elements_Mixed_1() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.FIRST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2,\n\tELEMENT-3),\nASC (ELEMENT-4) DF", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Four_Elements_Mixed_2() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1),\nASC (ELEMENT-2,\n\tELEMENT-3),\nDESC (ELEMENT-4) DL", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Four_Elements_Mixed_3() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING, 
							   SortSequence.DESCENDING, SortSequence.ASCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("DESC (ELEMENT-1),\nASC (ELEMENT-2),\nDESC (ELEMENT-3),\nASC (ELEMENT-4) DL", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Four_Elements_Mixed_4() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2),\nASC (ELEMENT-3),\nDESC (ELEMENT-4) DL", 
					 sortKeysAsString);		
	}
	
	@Test
	public void testGetSortkeys_Four_Elements_Mixed_5() {
		MemberRole memberRole = 
			generateMemberRole(DuplicatesOption.LAST, 
							   SortSequence.ASCENDING, SortSequence.DESCENDING,
							   SortSequence.DESCENDING, SortSequence.DESCENDING,
							   SortSequence.ASCENDING, SortSequence.DESCENDING);
		String sortKeysAsString = Tools.getSortKeys(memberRole);
		assertEquals("ASC (ELEMENT-1),\nDESC (ELEMENT-2,\n\tELEMENT-3,\n\tELEMENT-4),\n" +
					 "ASC (ELEMENT-5),\nDESC (ELEMENT-6) DL", sortKeysAsString);		
	}

}
