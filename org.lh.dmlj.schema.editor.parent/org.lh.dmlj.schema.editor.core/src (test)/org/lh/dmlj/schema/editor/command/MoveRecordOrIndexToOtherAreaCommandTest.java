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
package org.lh.dmlj.schema.editor.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava.schema;

import org.eclipse.core.runtime.AssertionFailedException;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class MoveRecordOrIndexToOtherAreaCommandTest {

	@Test
	public void test_MoveRecordToExistingArea() {

		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMPLOYEE record
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		AreaSpecification areaSpecification = record.getAreaSpecification();
		SchemaArea area = areaSpecification.getArea();
		OffsetExpression offsetExpression = areaSpecification.getOffsetExpression();
		int i = area.getAreaSpecifications().indexOf(areaSpecification);
		assertFalse(i == area.getAreaSpecifications().size() - 1);
		
		// create the command - save all the items from the record's current offset expression (it
		// doesn't have a symbolic subarea)
		Integer offsetPageCount = offsetExpression.getOffsetPageCount();
		Short offsetPercent = offsetExpression.getOffsetPercent();
		Integer pageCount = offsetExpression.getPageCount();
		Short percent = offsetExpression.getPercent();
		MoveRecordOrIndexToOtherAreaCommand command =
			new MoveRecordOrIndexToOtherAreaCommand(record, "ORG-DEMO-REGION");
		
		// execute the command and check if the record is moved to the other area and that all
		// offset expression attributes are retained
		command.execute();
		assertEquals(3, schema.getAreas().size());
		SchemaRecord record2 = schema.getRecord("EMPLOYEE");
		assertTrue(record2 == record);
		AreaSpecification areaSpecification2 = record2.getAreaSpecification();
		assertTrue(areaSpecification2 == areaSpecification);
		SchemaArea area2 = areaSpecification.getArea();
		assertFalse(area2 == area);
		assertEquals("ORG-DEMO-REGION", area2.getName());
		assertEquals(area2.getAreaSpecifications().size() - 1, 
					 area2.getAreaSpecifications().indexOf(areaSpecification2));
		OffsetExpression offsetExpression2 = areaSpecification2.getOffsetExpression();
		assertTrue(offsetExpression2 == offsetExpression);
		assertEquals(offsetPageCount, offsetExpression2.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression2.getOffsetPercent());
		assertEquals(pageCount, offsetExpression2.getPageCount());
		assertEquals(percent, offsetExpression2.getPercent());
		assertNull(areaSpecification2.getSymbolicSubareaName());
		

		// undo the command and check if the record is moved to its original area again
		command.undo();
		assertEquals(3, schema.getAreas().size());
		SchemaRecord record3 = schema.getRecord("EMPLOYEE");
		assertTrue(record3 == record);
		AreaSpecification areaSpecification3 = record3.getAreaSpecification();
		assertTrue(areaSpecification3 == areaSpecification);
		SchemaArea area3 = areaSpecification.getArea();
		assertTrue(area3 == area);		
		assertEquals(i, area3.getAreaSpecifications().indexOf(areaSpecification3));
		OffsetExpression offsetExpression3 = areaSpecification3.getOffsetExpression();
		assertTrue(offsetExpression3 == offsetExpression);
		assertEquals(offsetPageCount, offsetExpression3.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression3.getOffsetPercent());
		assertEquals(pageCount, offsetExpression3.getPageCount());
		assertEquals(percent, offsetExpression3.getPercent());
		assertNull(areaSpecification3.getSymbolicSubareaName());
		
		
		// redo the command and check if the record is moved to the other area again
		command.execute();
		assertEquals(3, schema.getAreas().size());
		SchemaRecord record4 = schema.getRecord("EMPLOYEE");
		assertTrue(record4 == record);
		AreaSpecification areaSpecification4 = record4.getAreaSpecification();
		assertTrue(areaSpecification4 == areaSpecification);
		SchemaArea area4 = areaSpecification.getArea();
		assertTrue(area4 == area2);		
		assertEquals(area4.getAreaSpecifications().size() - 1, 
					 area4.getAreaSpecifications().indexOf(areaSpecification4));
		OffsetExpression offsetExpression4 = areaSpecification4.getOffsetExpression();
		assertTrue(offsetExpression4 == offsetExpression);
		assertEquals(offsetPageCount, offsetExpression4.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression4.getOffsetPercent());
		assertEquals(pageCount, offsetExpression4.getPageCount());
		assertEquals(percent, offsetExpression4.getPercent());
		assertNull(areaSpecification4.getSymbolicSubareaName());		
	}
	
	@Test
	public void test_MoveIndexToExistingArea() {

		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMP-NAME-NDX index
		Schema schema = TestTools.getEmpschmSchema();
		SystemOwner index = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		AreaSpecification areaSpecification = index.getAreaSpecification();
		SchemaArea area = areaSpecification.getArea();
		OffsetExpression offsetExpression = areaSpecification.getOffsetExpression();
		int i = area.getAreaSpecifications().indexOf(areaSpecification);		
		
		// create the command - save all the items from the index' current offset expression (it
		// doesn't have a symbolic subarea)		
		Integer offsetPageCount = offsetExpression.getOffsetPageCount();
		Short offsetPercent = offsetExpression.getOffsetPercent();
		Integer pageCount = offsetExpression.getPageCount();
		Short percent = offsetExpression.getPercent();		
		MoveRecordOrIndexToOtherAreaCommand command =
			new MoveRecordOrIndexToOtherAreaCommand(index, "ORG-DEMO-REGION");
		
		// execute the command and check if the index is moved to the other area and that the offset
		// expression is retained
		command.execute();
		assertEquals(3, schema.getAreas().size());
		SystemOwner index2 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertTrue(index2 == index);
		AreaSpecification areaSpecification2 = index2.getAreaSpecification();
		assertTrue(areaSpecification2 == areaSpecification);
		SchemaArea area2 = areaSpecification.getArea();
		assertFalse(area2 == area);
		assertEquals("ORG-DEMO-REGION", area2.getName());
		assertEquals(area2.getAreaSpecifications().size() - 1, 
					 area2.getAreaSpecifications().indexOf(areaSpecification2));
		OffsetExpression offsetExpression2 = areaSpecification2.getOffsetExpression();
		assertTrue(offsetExpression2 == offsetExpression);
		assertNull(areaSpecification2.getSymbolicSubareaName());		

		
		// undo the command and check if the index is moved to its original area again
		command.undo();
		assertEquals(3, schema.getAreas().size());
		SystemOwner index3 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertTrue(index3 == index);
		AreaSpecification areaSpecification3 = index3.getAreaSpecification();
		assertTrue(areaSpecification3 == areaSpecification);
		SchemaArea area3 = areaSpecification.getArea();
		assertTrue(area3 == area);		
		assertEquals(i, area3.getAreaSpecifications().indexOf(areaSpecification3));
		OffsetExpression offsetExpression3 = areaSpecification3.getOffsetExpression();
		assertNotNull(offsetExpression3);
		assertTrue(offsetExpression3 == offsetExpression); 
		assertEquals(offsetPageCount, offsetExpression3.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression3.getOffsetPercent());
		assertEquals(pageCount, offsetExpression3.getPageCount());
		assertEquals(percent, offsetExpression3.getPercent());
		assertNull(areaSpecification3.getSymbolicSubareaName());
		
		
		// redo the command and check if the index is moved to the other area again
		command.execute();
		assertEquals(3, schema.getAreas().size());
		SystemOwner index4 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertTrue(index4 == index);
		AreaSpecification areaSpecification4 = index4.getAreaSpecification();
		assertTrue(areaSpecification4 == areaSpecification);
		SchemaArea area4 = areaSpecification.getArea();
		assertTrue(area4 == area2);		
		assertEquals(area4.getAreaSpecifications().size() - 1, 
					 area4.getAreaSpecifications().indexOf(areaSpecification4));
		OffsetExpression offsetExpression4 = areaSpecification4.getOffsetExpression();
		assertTrue(offsetExpression4 == offsetExpression);
		assertNull(areaSpecification4.getSymbolicSubareaName());
	}	
	
	@Test
	public void test_MoveRecordToNewArea() {

		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMPLOYEE record
		Schema schema = TestTools.getEmpschmSchema();
		SchemaRecord record = schema.getRecord("EMPLOYEE");		
		
		// create the command; we don't care about the symbolic subarea or offset expression
		MoveRecordOrIndexToOtherAreaCommand command =
			new MoveRecordOrIndexToOtherAreaCommand(record, "NWAREA");
		
		// execute the command; check if the new area is created and that the record is moved to it
		command.execute();
		assertEquals(4, schema.getAreas().size());
		SchemaArea area2 = schema.getArea("NWAREA");
		assertEquals("NWAREA", schema.getAreas().get(3).getName());
		SchemaRecord record2 = schema.getRecord("EMPLOYEE");
		assertTrue(record2 == record);
		assertEquals("NWAREA", record2.getAreaSpecification().getArea().getName());
		
		// undo the command; check if the new area is removed and that the record is moved to its
		// original area again
		command.undo();
		assertEquals(3, schema.getAreas().size());
		SchemaRecord record3 = schema.getRecord("EMPLOYEE");
		assertTrue(record3 == record);
		assertEquals("EMP-DEMO-REGION", record3.getAreaSpecification().getArea().getName());
		
		// redo the command; check if the new area is restored and that the record is moved to it
		command.redo();
		assertEquals(4, schema.getAreas().size());
		SchemaArea area4 = schema.getArea("NWAREA");
		assertTrue(area4 == area2);
		assertEquals("NWAREA", schema.getAreas().get(3).getName());
		SchemaRecord record4 = schema.getRecord("EMPLOYEE");
		assertTrue(record4 == record);
		assertEquals("NWAREA", record4.getAreaSpecification().getArea().getName());
		
	}
	
	@Test
	public void test_MoveIndexToNewArea() {

		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMP-NAME-NDX
		Schema schema = TestTools.getEmpschmSchema();
		SystemOwner index = schema.getSet("EMP-NAME-NDX").getSystemOwner();		
		
		// create the command; we don't care about the symbolic subarea or offset expression
		MoveRecordOrIndexToOtherAreaCommand command =
			new MoveRecordOrIndexToOtherAreaCommand(index, "NWAREA");
		
		// execute the command; check if the new area is created and that the index is moved to it
		command.execute();
		assertEquals(4, schema.getAreas().size());
		SchemaArea area2 = schema.getArea("NWAREA");
		assertEquals("NWAREA", schema.getAreas().get(3).getName());
		SystemOwner index2 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertTrue(index2 == index);
		assertEquals("NWAREA", index2.getAreaSpecification().getArea().getName());
		
		// undo the command; check if the new area is removed and that the index is moved to its
		// original area again
		command.undo();
		assertEquals(3, schema.getAreas().size());
		SystemOwner index3 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertTrue(index3 == index);
		assertEquals("EMP-DEMO-REGION", index3.getAreaSpecification().getArea().getName());
		
		// redo the command; check if the new area is restored and that the index is moved to it
		command.redo();
		assertEquals(4, schema.getAreas().size());
		SchemaArea area4 = schema.getArea("NWAREA");
		assertTrue(area4 == area2);
		assertEquals("NWAREA", schema.getAreas().get(3).getName());
		SystemOwner index4 = schema.getSet("EMP-NAME-NDX").getSystemOwner();
		assertTrue(index4 == index);
		assertEquals("NWAREA", index4.getAreaSpecification().getArea().getName());
		
	}
	
	@Test
	public void test_MoveRecordFromSubsequentlyObsoleteArea() {

		// get the EMPSCHM version 1 schema from the testdata folder; locate the EMPLOYEE record and
		// put it - as the only entity - in a new area
		Schema schema = TestTools.getEmpschmSchema();
		SchemaArea area = SchemaFactory.eINSTANCE.createSchemaArea(); // create area to become
		area.setName("TOBECOMEOBSOLETE");							  // obsolete and insert it in
		schema.getAreas().add(1, area);							      // the area list as 2nd item
		assertEquals(4, schema.getAreas().size());
		SchemaRecord record = schema.getRecord("EMPLOYEE");
		AreaSpecification areaSpecification = record.getAreaSpecification();
		areaSpecification.setArea(area);
		OffsetExpression offsetExpression = areaSpecification.getOffsetExpression();		
		
		// create the command - save all the items from the record's current offset expression
		Integer offsetPageCount = offsetExpression.getOffsetPageCount();
		Short offsetPercent = offsetExpression.getOffsetPercent();
		Integer pageCount = offsetExpression.getPageCount();
		Short percent = offsetExpression.getPercent();
		MoveRecordOrIndexToOtherAreaCommand command =
			new MoveRecordOrIndexToOtherAreaCommand(record, "EMP-DEMO-REGION");
		
		// execute the command and check if the record is moved to the other area AND that the
		// original area is removed because it became obsolete
		command.execute();
		assertEquals(3, schema.getAreas().size());
		assertNull(schema.getArea("TOBECOMEOBSOLETE"));
		SchemaRecord record2 = schema.getRecord("EMPLOYEE");
		assertTrue(record2 == record);
		AreaSpecification areaSpecification2 = record2.getAreaSpecification();
		assertTrue(areaSpecification2 == areaSpecification);
		SchemaArea area2 = areaSpecification.getArea();
		assertFalse(area2 == area);
		assertEquals("EMP-DEMO-REGION", area2.getName());
		assertEquals(area2.getAreaSpecifications().size() - 1, 
					 area2.getAreaSpecifications().indexOf(areaSpecification2));
		OffsetExpression offsetExpression2 = areaSpecification2.getOffsetExpression();
		assertTrue(offsetExpression2 == offsetExpression);
		assertEquals(offsetPageCount, offsetExpression2.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression2.getOffsetPercent());
		assertEquals(pageCount, offsetExpression2.getPageCount());
		assertEquals(percent, offsetExpression2.getPercent());
		assertNull(areaSpecification2.getSymbolicSubareaName());
		
		// undo the command and check if the record is moved to its original area again
		command.undo();
		assertEquals(4, schema.getAreas().size());
		SchemaRecord record3 = schema.getRecord("EMPLOYEE");
		assertTrue(record3 == record);
		AreaSpecification areaSpecification3 = record3.getAreaSpecification();
		assertTrue(areaSpecification3 == areaSpecification);
		SchemaArea area3 = areaSpecification.getArea();
		assertTrue(area3 == area);		
		assertEquals(1, schema.getAreas().indexOf(area3));
		OffsetExpression offsetExpression3 = areaSpecification3.getOffsetExpression();
		assertTrue(offsetExpression3 == offsetExpression);
		assertEquals(offsetPageCount, offsetExpression3.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression3.getOffsetPercent());
		assertEquals(pageCount, offsetExpression3.getPageCount());
		assertEquals(percent, offsetExpression3.getPercent());
		assertNull(areaSpecification3.getSymbolicSubareaName());		
		
		
		// redo the command and check if the record is moved to the other area again
		command.execute();
		assertEquals(3, schema.getAreas().size());
		SchemaRecord record4 = schema.getRecord("EMPLOYEE");
		assertTrue(record4 == record);
		AreaSpecification areaSpecification4 = record4.getAreaSpecification();
		assertTrue(areaSpecification4 == areaSpecification);
		SchemaArea area4 = areaSpecification.getArea();
		assertTrue(area4 == area2);		
		assertEquals(area4.getAreaSpecifications().size() - 1, 
					 area4.getAreaSpecifications().indexOf(areaSpecification4));
		OffsetExpression offsetExpression4 = areaSpecification4.getOffsetExpression();
		assertTrue(offsetExpression4 == offsetExpression);
		assertEquals(offsetPageCount, offsetExpression4.getOffsetPageCount());
		assertEquals(offsetPercent, offsetExpression4.getOffsetPercent());
		assertEquals(pageCount, offsetExpression4.getPageCount());
		assertEquals(percent, offsetExpression4.getPercent());
		assertNull(areaSpecification4.getSymbolicSubareaName());
		
	}
	
	@Test
	public void test_VsamAndNonVsamRecordsDontMix() {
		
		Schema schema = 
			schema("record 'VSAM-RECORD' { vsam; area 'VSAM-AREA' }; " +
				   "record 'VSAM-CALC-RECORD' { vsamCalc { element 'ELEMENT1' }; area 'VSAM-CALC-AREA' }; " +
				   "record 'NON-VSAM-RECORD' { area 'NON-VSAM-AREA' }; " +
				   "set 'INDEXED-SET' { systemOwner { area 'INDEX-AREA' }}");
		
		SchemaRecord vsamRecord = schema.getRecord("VSAM-RECORD");
		assertNotNull("record VSAM-RECORD not found in schema", vsamRecord);
		assertTrue("record VSAM-RECORD has wrong location mode", vsamRecord.isVsam());
		assertEquals("VSAM-AREA", vsamRecord.getAreaSpecification().getArea().getName());
		
		SchemaRecord vsamCalcRecord = schema.getRecord("VSAM-CALC-RECORD");
		assertNotNull("record VSAM-CALC-RECORD not found in schema", vsamCalcRecord);
		assertTrue("record VSAM-RECORD has wrong location mode", vsamCalcRecord.isVsamCalc());
		assertEquals("VSAM-CALC-AREA", vsamCalcRecord.getAreaSpecification().getArea().getName());
		
		SchemaRecord nonVsamRecord = schema.getRecord("NON-VSAM-RECORD");
		assertNotNull("record NON-VSAM-RECORD not found in schema", nonVsamRecord);
		assertTrue("record VSAM-RECORD has wrong location mode", nonVsamRecord.isDirect());
		assertEquals("NON-VSAM-AREA", nonVsamRecord.getAreaSpecification().getArea().getName());
		
		Set set = schema.getSet("INDEXED-SET");
		assertNotNull("set INDEXED-SET not found in schema", set);
		SystemOwner systemOwner = set.getSystemOwner();
		assertNotNull("set INDEXED-SET has no system owner", systemOwner);
		assertEquals("INDEX-AREA", systemOwner.getAreaSpecification().getArea().getName());
		
		// moving a VSAM record to an area containing a non-VSAM record is NOT allowed
		MoveRecordOrIndexToOtherAreaCommand command =
			new MoveRecordOrIndexToOtherAreaCommand(vsamRecord, "NON-VSAM-AREA");
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area NON-VSAM-AREA is NOT compatible", e.getMessage());
		}
		
		// moving a VSAM record to an area containing a system owner is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(vsamRecord, "INDEX-AREA");				
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area INDEX-AREA is NOT compatible", e.getMessage());
		}
		
		// moving a VSAM CALC record to an area containing a non-VSAM record is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(vsamCalcRecord, "NON-VSAM-AREA");		
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area NON-VSAM-AREA is NOT compatible", e.getMessage());
		}
		
		// moving a VSAM CALC record to an area containing a system owner is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(vsamCalcRecord, "INDEX-AREA");
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area INDEX-AREA is NOT compatible", e.getMessage());
		}
		
		// moving a non-VSAM record to an area containing a VSAM record is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(nonVsamRecord, "VSAM-AREA");
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area VSAM-AREA is NOT compatible", e.getMessage());
		}
		
		// moving a non-VSAM record to an area containing a VSAM CALC record is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(nonVsamRecord, "VSAM-CALC-AREA");
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area VSAM-CALC-AREA is NOT compatible", e.getMessage());
		}
		
		// moving a system owner to an area containing a VSAM record is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(systemOwner, "VSAM-AREA");
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area VSAM-AREA is NOT compatible", e.getMessage());
		}
				
		// moving a system owner to an area containing a VSAM CALC record is NOT allowed
		command = new MoveRecordOrIndexToOtherAreaCommand(systemOwner, "VSAM-CALC-AREA");
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: area VSAM-CALC-AREA is NOT compatible", e.getMessage());
		}	
	}
	
}
