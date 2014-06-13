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
package org.lh.dmlj.schema.editor.outline.part;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.mockito.MockitoAnnotations;

public class AbstractSchemaTreeEditPartTest {

	private static final Class<?>[] CHILD_COMPARABLE_ORDER = 
		{DiagramLabel.class, SchemaArea.class, SchemaRecord.class, Set.class};
	
	private List<Object> children;
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
		// simulate a (top level) schema tree edit part, using a concrete list of (mock) children:
		// [0] the DiagramLabel
		// [1] SchemaArea		TESTAREA_V
		// [2] SchemaArea		TESTAREA_X
		// [3] SchemaRecord		TESTRECORD_V
		// [4] SchemaRecord		TESTRECORD_X
		// [5] SystemOwner		TESTSET_V
		// [6] Set				TESTSET_X
		children = new ArrayList<>();
		
		// add a diagram label edit part
		DiagramLabel diagramLabel = mock(DiagramLabel.class);		
		DiagramLabelTreeEditPart diagramLabelEditPart = mock(DiagramLabelTreeEditPart.class);
		when(diagramLabelEditPart.getModel()).thenReturn(diagramLabel);
		children.add(diagramLabelEditPart);		
		
		// add an area edit part: TESTAREA_V
		SchemaArea area = mock(SchemaArea.class);
		when(area.getName()).thenReturn("TESTAREA_V");
		AreaTreeEditPart areaEditPart = mock(AreaTreeEditPart.class);
		when(areaEditPart.getModel()).thenReturn(area);
		children.add(areaEditPart);	
		
		// add an area edit part: TESTAREA_X
		area = mock(SchemaArea.class);
		when(area.getName()).thenReturn("TESTAREA_X");
		areaEditPart = mock(AreaTreeEditPart.class);
		when(areaEditPart.getModel()).thenReturn(area);
		children.add(areaEditPart);	
		
		// add a record edit part: TESTRECORD_V
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn("TESTRECORD_V");
		RecordTreeEditPart recordEditPart = mock(RecordTreeEditPart.class);
		when(recordEditPart.getModel()).thenReturn(record);
		children.add(recordEditPart);
		
		// add a record edit part: TESTRECORD_X
		record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn("TESTRECORD_X");
		recordEditPart = mock(RecordTreeEditPart.class);
		when(recordEditPart.getModel()).thenReturn(record);
		children.add(recordEditPart);
		
		// add a system owned indexed set edit part: TESTSET_V
		SystemOwner systemOwner = mock(SystemOwner.class);
		Set index = mock(Set.class);
		when(index.getName()).thenReturn("TESTSET_V");
		when(systemOwner.getSet()).thenReturn(index);
		when(index.getSystemOwner()).thenReturn(systemOwner);
		IndexTreeEditPart indexEditPart = mock(IndexTreeEditPart.class);
		when(indexEditPart.getModel()).thenReturn(systemOwner);
		children.add(indexEditPart);		
		
		// add a chained set edit part: TESTSET_X
		Set set = mock(Set.class);
		when(set.getName()).thenReturn("TESTSET_X");
		SetTreeEditPart setEditPart = mock(SetTreeEditPart.class);		
		when(setEditPart.getModel()).thenReturn(set);
		children.add(setEditPart);
		
		assertEquals(7, children.size());
		
	}
	
	@Test
	public void testGetInsertionIndex_Area_BeforeFirstRecord() {
		
		// create the new area: TESTAREA_U
		SchemaArea area = mock(SchemaArea.class);
		when(area.getName()).thenReturn("TESTAREA_U");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, area, CHILD_COMPARABLE_ORDER);
		assertEquals(1, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Area_AfterFirstRecord() {
		
		// create the new area: TESTAREA_W
		SchemaArea record = mock(SchemaArea.class);
		when(record.getName()).thenReturn("TESTAREA_W");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, record, CHILD_COMPARABLE_ORDER);
		assertEquals(2, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Area_AfterSecondRecord() {
		
		// create the new area: TESTAREA_Y
		SchemaArea record = mock(SchemaArea.class);
		when(record.getName()).thenReturn("TESTAREA_Y");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, record, CHILD_COMPARABLE_ORDER);
		assertEquals(3, insertionIndex);
		
	}	
	
	@Test
	public void testGetInsertionIndex_Record_BeforeFirstRecord() {
		
		// create the new record: TESTRECORD_U
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn("TESTRECORD_U");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, record, CHILD_COMPARABLE_ORDER);
		assertEquals(3, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Record_AfterFirstRecord() {
		
		// create the new record: TESTRECORD_W
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn("TESTRECORD_W");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, record, CHILD_COMPARABLE_ORDER);
		assertEquals(4, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Record_AfterSecondRecord() {
		
		// create the new record: TESTRECORD_Y
		SchemaRecord record = mock(SchemaRecord.class);
		when(record.getName()).thenReturn("TESTRECORD_Y");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, record, CHILD_COMPARABLE_ORDER);
		assertEquals(5, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Set_BeforeFirstSet() {
		
		// create the new set: TESTSET_U
		Set set = mock(Set.class);
		when(set.getName()).thenReturn("TESTSET_U");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, set, CHILD_COMPARABLE_ORDER);
		assertEquals(5, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Set_AfterFirstSet() {
		
		// create the new set: TESTSET_W
		Set set = mock(Set.class);
		when(set.getName()).thenReturn("TESTSET_W");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, set, CHILD_COMPARABLE_ORDER);
		assertEquals(6, insertionIndex);
		
	}
	
	@Test
	public void testGetInsertionIndex_Set_AfterSecondSet() {
		
		// create the new record: TESTSET_Y
		Set set = mock(Set.class);
		when(set.getName()).thenReturn("TESTSET_Y");		
		
		// calculate and verify the insertion index
		int insertionIndex = 
			AbstractSchemaTreeEditPart.getInsertionIndex(children, set, CHILD_COMPARABLE_ORDER);
		assertEquals(7, insertionIndex);
		
	}	

}
