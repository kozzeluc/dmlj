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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.SchemaFactory;

public class ChangeAreaSpecificationCommandTest {

	/**
	 * Test the various assertions; all assertions are done when executing the command.
	 */
	@Test
	public void testAssertions() {
		
		// no area specification
		Command command = new ChangeAreaSpecificationCommand(null, null, null, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("null argument:area specification", e.getMessage());
		}
		
		// create an area specification for all further assertions
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		
		// suply both a subarea and an offset page count
		command = 
			new ChangeAreaSpecificationCommand(areaSpecification, "SUBAREA1", 50, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: cannot change area specification to " +
						 "contain both a subarea and offset expression data", e.getMessage());
		}
		
		// suply both a subarea and an offset percent
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
											   		 "SUBAREA1", null, (short) 5, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: cannot change area specification to " +
						 "contain both a subarea and offset expression data", e.getMessage());
		}
				
		// suply both a subarea and a page count
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
											   		 "SUBAREA1", null, null, 1000, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: cannot change area specification to " +
						 "contain both a subarea and offset expression data", e.getMessage());
		}
		
		// suply both a subarea and a percent
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
											   		 "SUBAREA1", null, null, null, (short) 50);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: cannot change area specification to " +
						 "contain both a subarea and offset expression data", e.getMessage());
		}	
		
		
		// supply both an offset page count and offset percent
		command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 10, (short) 5, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: cannot set both an offset page count " +
						 "and offset percent in an offset expression", e.getMessage());
		}	
		
		// neither a symbolic subarea, an offset page count or offset percent are supplied
		command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, null, null, 1000, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: need to set an offset page count or " +
						 "offset percent in an offset expression", e.getMessage());
		}
		
		// supply both a page count and percent
		command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 5, null, 1000, (short) 20);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: cannot set both a page count and " +
						 "percent in an offset expression", e.getMessage());
		}	
		
		// neither a symbolic subarea, a page count or percent are supplied
		command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 5, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: need to set a page count or percent " +
						 "in an offset expression", e.getMessage());
		}	
		
		
		// the area specification contains both a subarea and a valid offset expression
		OffsetExpression offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(5);
		offsetExpression.setPageCount(1000);
		areaSpecification.setOffsetExpression(offsetExpression);
		areaSpecification.setSymbolicSubareaName("OLDSUBA");
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
													 "SUBAREA1", null, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: area specification with both a subarea " +
					  	 "and an offset expression", e.getMessage());
		}
		
		// existing offset expression with both an offset page count and offset percent
		offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(5);
		offsetExpression.setOffsetPercent((short) 15);
		areaSpecification.setOffsetExpression(offsetExpression);
		areaSpecification.setSymbolicSubareaName(null);
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
													 "SUBAREA1", null, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: offset expression with both an offset " +
						 "page count and offset percent", e.getMessage());
		}		
		
		// existing offset expression with neither an offset page count or offset percent
		offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setPageCount(5);
		areaSpecification.setOffsetExpression(offsetExpression);
		areaSpecification.setSymbolicSubareaName(null);
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
													 "SUBAREA1", null, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: offset expression must contain an " +
					 	  "offset page count or offset percent", e.getMessage());
		}
		
		// existing offset expression with both a page count and percent
		offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(5);
		offsetExpression.setPageCount(5);
		offsetExpression.setPercent((short) 15);
		areaSpecification.setOffsetExpression(offsetExpression);
		areaSpecification.setSymbolicSubareaName(null);
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
													 "SUBAREA1", null, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: offset expression with both a page " +
					 	 "count and percent", e.getMessage());
		}
		
		// existing offset expression with neither a page count or percent
		offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(5);
		areaSpecification.setOffsetExpression(offsetExpression);
		areaSpecification.setSymbolicSubareaName(null);
		command = new ChangeAreaSpecificationCommand(areaSpecification, 
													 "SUBAREA1", null, null, null, null);
		try {
			command.execute();
			fail("should throw an AssertionException");
		} catch (AssertionFailedException e) {
			assertEquals("assertion failed: logic error: offset expression must contain a " +
						 "page count or percent", e.getMessage());
		}		
		
	}
	
	/**
	 * When no offset expression is present, executing the command will only create one if the
	 * supplied offset expression attributes do NOT correspond to the default offset expression 
	 * (OFFSET 0 PAGES FOR 100 PERCENT).
	 */
	@Test
	public void testDefaultOffsetExpressionValues_A() {
		
		// create an area specification with no offset expression (which corresponds to the default
		// specification: OFFSET 0 PAGES FOR 100 PERCENT) and no symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		
		// create the command that sets the attributes for the offset expression 'OFFSET 0 PAGES 
		// FOR 100 PERCENT'
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 0, null, null, (short) 100);
		
		// execute the command and check the offset expression and symbolic subarea; both should
		// still be null
		command.execute();
		assertNull(areaSpecification.getOffsetExpression());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// undo the command and check that the offset expression and symbolic subarea are still null
		command.undo();
		assertNull(areaSpecification.getOffsetExpression());
		assertNull(areaSpecification.getSymbolicSubareaName());		
		
		
		// redo the command and check that no offset expression is added
		command.redo();
		assertNull(areaSpecification.getOffsetExpression());
		assertNull(areaSpecification.getSymbolicSubareaName());		
	}
	
	/**
	 * When an offset expression is present, executing the command will remove it if the supplied
	 * offset expression attributes correspond to the default offset expression (OFFSET 0 PAGES 
	 * FOR 100 PERCENT).  Undoing the command will restore the original offset expression again.
	 */
	@Test
	public void testDefaultOffsetExpressionValues_B() {
		
		// create an area specification with an offset expression that corresponds to the default
		// specification: OFFSET 0 PAGES FOR 100 PERCENT - no symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		OffsetExpression offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(Integer.valueOf(0));
		offsetExpression.setPercent(Short.valueOf((short) 100));
		areaSpecification.setOffsetExpression(offsetExpression);
		
		// create the command that sets the attributes for the offset expression 'OFFSET 0 PAGES 
		// FOR 100 PERCENT' (the default offset expression)
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 0, null, null, (short) 100);
		
		// execute the command and check the offset expression, which should be removed, and the
		// symbolic subarea, which shouln't be there neither		
		command.execute();
		OffsetExpression offsetExpression2 = areaSpecification.getOffsetExpression();
		assertNull(offsetExpression2);								
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// undo the command and check that the original offset expression was restored
		command.undo();
		OffsetExpression offsetExpression3 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression3);
		assertTrue(offsetExpression3 == offsetExpression);
		assertEquals(0, offsetExpression3.getOffsetPageCount().intValue());	// OFFSET 0 PAGES...
		assertNull(offsetExpression3.getOffsetPercent());					
		assertNull(offsetExpression3.getPageCount());	
		assertNotNull(offsetExpression3.getPercent());
		assertEquals(100, offsetExpression3.getPercent().shortValue());		// ... FOR 100 PERCENT
		assertNull(areaSpecification.getSymbolicSubareaName());	
		
		
		// redo the command and check that the offset expression is removed again
		command.redo();
		OffsetExpression offsetExpression4 = areaSpecification.getOffsetExpression();
		assertNull(offsetExpression4);							
		assertNull(areaSpecification.getSymbolicSubareaName());
		
	}
	
	
	/**
	 * When no offset expression is present, executing the command will create one if the supplied
	 * offset expression attributes do NOT correspond to the default offset expression (OFFSET 0  
	 * PAGES FOR 100 PERCENT).  Undoing the command will remove the offset expression again; redoing
	 * will again create a(nother) OffsetExpression instance.
	 */
	@Test
	public void testNonDefaultOffsetExpressionValues_A() {
		
		// create an area specification with no offset expression (which corresponds to the default
		// specification: OFFSET 0 PAGES FOR 100 PERCENT) and no symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		
		// create the command that sets the attributes for the offset expression 'OFFSET 5 PAGES 
		// FOR 80 PERCENT'
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 5, null, null, (short) 80);
		
		// execute the command and check the offset expression and symbolic subarea		
		command.execute();
		OffsetExpression offsetExpression = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression);
		assertNotNull(offsetExpression.getOffsetPageCount());	
		assertEquals(5, offsetExpression.getOffsetPageCount().intValue());	// OFFSET 5 PAGES...
		assertNull(offsetExpression.getOffsetPercent());					
		assertNull(offsetExpression.getPageCount());	
		assertNotNull(offsetExpression.getPercent());
		assertEquals(80, offsetExpression.getPercent().shortValue());		// ... FOR 80 PERCENT
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// undo the command and check that the offset expression was removed
		command.undo();
		assertNull(areaSpecification.getOffsetExpression());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// redo the command and check that the offset expression is added again; note that another
		// OffsetExpression instance is created
		command.redo();
		OffsetExpression offsetExpression2 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression2);
		assertTrue(offsetExpression2 != offsetExpression);
		assertEquals(5, offsetExpression2.getOffsetPageCount().intValue());	// OFFSET 0 PAGES...
		assertNull(offsetExpression2.getOffsetPercent());					
		assertNull(offsetExpression2.getPageCount());	
		assertNotNull(offsetExpression2.getPercent());
		assertEquals(80, offsetExpression2.getPercent().shortValue());		// ... FOR 80 PERCENT
		assertNull(areaSpecification.getSymbolicSubareaName());
		
	}
	
	/**
	 * When an offset expression is present, executing the command will modify it and never create
	 * a new one, but only if the supplied offset expression attributes do NOT correspond to the 
	 * default offset expression (OFFSET 0 PAGES FOR 100 PERCENT) - if the new offset expression 
	 * corresponds to the default offset expression, the offset expression will be removed (this is 
	 * tested with another @Test method).  Undoing the command will restore the original offset 
	 * expression attributes again.
	 */
	@Test
	public void testNonDefaultOffsetExpressionValues_B() {
		
		// create an area specification with an arbitrary offset expression (which does not 
		// correspond to the default specification: OFFSET 10 PERCENT FOR 1000 PAGES) and no 
		// symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		OffsetExpression offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPercent((short) 10);
		offsetExpression.setPageCount(1000);
		areaSpecification.setOffsetExpression(offsetExpression);
		
		// create the command that sets the attributes for the offset expression 'OFFSET 5 PAGES 
		// FOR 80 PERCENT'
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, 5, null, null, (short) 80);
		
		// execute the command and check the offset expression and symbolic subarea		
		command.execute();
		OffsetExpression offsetExpression2 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression2);
		assertTrue(offsetExpression2 == offsetExpression);
		assertNotNull(offsetExpression2.getOffsetPageCount());	
		assertEquals(5, offsetExpression2.getOffsetPageCount().intValue());	// OFFSET 5 PAGES...
		assertNull(offsetExpression2.getOffsetPercent());					
		assertNull(offsetExpression2.getPageCount());	
		assertNotNull(offsetExpression2.getPercent());
		assertEquals(80, offsetExpression2.getPercent().shortValue());		// ... FOR 80 PERCENT
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// undo the command and check that the original offset expression was restored
		command.undo();
		OffsetExpression offsetExpression3 = areaSpecification.getOffsetExpression();
		assertTrue(offsetExpression3 == offsetExpression);
		assertNotNull(offsetExpression3.getOffsetPercent());
		assertEquals((short) 10, offsetExpression3.getOffsetPercent().shortValue());
		assertNotNull(offsetExpression3.getPageCount());
		assertEquals((int) 1000, offsetExpression3.getPageCount().intValue());
		assertNull(offsetExpression3.getOffsetPageCount());
		assertNull(offsetExpression3.getPercent());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// redo the command and check the offset expression
		command.redo();
		OffsetExpression offsetExpression4 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression4);
		assertTrue(offsetExpression4 == offsetExpression2);
		assertNotNull(offsetExpression4.getOffsetPageCount());	
		assertEquals(5, offsetExpression4.getOffsetPageCount().intValue());	// OFFSET 5 PAGES...
		assertNull(offsetExpression4.getOffsetPercent());					
		assertNull(offsetExpression4.getPageCount());	
		assertNotNull(offsetExpression4.getPercent());
		assertEquals(80, offsetExpression4.getPercent().shortValue());		// ... FOR 80 PERCENT
		assertNull(areaSpecification.getSymbolicSubareaName());
		
	}
	
	/**
	 * When an offset expression is present, executing the command will modify it and never create
	 * a new one, but only if the supplied offset expression attributes do NOT correspond to the 
	 * default offset expression (OFFSET 0 PERCENT FOR 1000 PAGES) - if the new offset expression 
	 * corresponds to the default offset expression, the offset expression will be removed (this is 
	 * tested with another @Test method).  Undoing the command will restore the original offset 
	 * expression attributes again.
	 * 
	 * Same as previous test but we're working with other attributes for the modified offset 
	 * expression here.
	 */
	@Test
	public void testNonDefaultOffsetExpressionValues_C() {
		
		// create an area specification with an arbitrary offset expression (which does not 
		// correspond to the default specification: OFFSET 10 PERCENT FOR 1000 PAGES) and no 
		// symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		OffsetExpression offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPercent((short) 10);
		offsetExpression.setPageCount(1000);
		areaSpecification.setOffsetExpression(offsetExpression);
		
		// create the command that sets the attributes for the offset expression 'OFFSET 15 PERCENT 
		// FOR 2000 PAGES'
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, null, (short) 15, 2000, null);
		
		// execute the command and check the offset expression and symbolic subarea		
		command.execute();
		OffsetExpression offsetExpression2 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression2);
		assertTrue(offsetExpression2 == offsetExpression);
		assertNull(offsetExpression2.getOffsetPageCount());			
		assertNotNull(offsetExpression2.getOffsetPercent());
		assertEquals(15, offsetExpression2.getOffsetPercent().shortValue());	// OFFSET 15 PERCENT...
		assertNotNull(offsetExpression2.getPageCount());	
		assertEquals(2000, offsetExpression2.getPageCount().shortValue());		// ... FOR 2000 PAGES
		assertNull(offsetExpression2.getPercent());		
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// undo the command and check that the original offset expression was restored
		command.undo();
		OffsetExpression offsetExpression3 = areaSpecification.getOffsetExpression();
		assertTrue(offsetExpression3 == offsetExpression);
		assertNotNull(offsetExpression3.getOffsetPercent());
		assertEquals((short) 10, offsetExpression3.getOffsetPercent().shortValue());
		assertNotNull(offsetExpression3.getPageCount());
		assertEquals((int) 1000, offsetExpression3.getPageCount().intValue());
		assertNull(offsetExpression3.getOffsetPageCount());
		assertNull(offsetExpression3.getPercent());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// redo the command and check the offset expression
		command.redo();
		OffsetExpression offsetExpression4 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression4);
		assertTrue(offsetExpression4 == offsetExpression2);
		assertNull(offsetExpression4.getOffsetPageCount());			
		assertNotNull(offsetExpression4.getOffsetPercent());
		assertEquals(15, offsetExpression4.getOffsetPercent().shortValue());	// OFFSET 15 PERCENT...
		assertNotNull(offsetExpression4.getPageCount());	
		assertEquals(2000, offsetExpression4.getPageCount().shortValue());		// ... FOR 2000 PAGES
		assertNull(offsetExpression4.getPercent());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
	}
	
	/**
	 * When an offset expression is present, executing the command will modify it and never create
	 * a new one, but only if the supplied offset expression attributes do NOT correspond to the 
	 * default offset expression (OFFSET 0 PAGES FOR 100 PERCENT) - if the new offset expression 
	 * corresponds to the default offset expression, the offset expression will be removed (this is 
	 * tested with another @Test method).  Undoing the command will restore the original offset 
	 * expression attributes again.
	 * 
	 * Same as previous test but we're working with other attributes for the original offset 
	 * expression here.
	 */
	@Test
	public void testNonDefaultOffsetExpressionValues_D() {
		
		// create an area specification with an arbitrary offset expression (which does not 
		// correspond to the default specification: OFFSET 2000 PAGES FOR 15 PERCENT) and no 
		// symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		OffsetExpression offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(2000);
		offsetExpression.setPercent((short) 15);
		areaSpecification.setOffsetExpression(offsetExpression);
		
		// create the command that sets the attributes for the offset expression 'OFFSET 15 PERCENT 
		// FOR 2000 PAGES'
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, null, null, (short) 15, 2000, null);
		
		// execute the command and check the offset expression and symbolic subarea		
		command.execute();
		OffsetExpression offsetExpression2 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression2);
		assertTrue(offsetExpression2 == offsetExpression);
		assertNull(offsetExpression2.getOffsetPageCount());		
		assertNotNull(offsetExpression2.getOffsetPercent());
		assertEquals(15, offsetExpression2.getOffsetPercent().shortValue());	// OFFSET 2000 PAGES...
		assertNotNull(offsetExpression2.getPageCount());			
		assertEquals(2000, offsetExpression2.getPageCount().intValue());		// ... FOR 2000 PAGES
		assertNull(offsetExpression2.getPercent());				
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// undo the command and check that the original offset expression was restored
		command.undo();
		OffsetExpression offsetExpression3 = areaSpecification.getOffsetExpression();
		assertTrue(offsetExpression3 == offsetExpression);
		assertNull(offsetExpression3.getOffsetPercent());		
		assertNotNull(offsetExpression3.getOffsetPageCount());
		assertEquals((int) 2000, offsetExpression3.getOffsetPageCount().intValue());
		assertNull(offsetExpression3.getPageCount());		
		assertNotNull(offsetExpression3.getPercent());
		assertEquals((short) 15, offsetExpression3.getPercent().shortValue());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
		
		// redo the command and check the offset expression
		command.redo();
		OffsetExpression offsetExpression4 = areaSpecification.getOffsetExpression();
		assertNotNull(offsetExpression4);
		assertTrue(offsetExpression4 == offsetExpression2);
		assertNull(offsetExpression4.getOffsetPageCount());		
		assertNotNull(offsetExpression4.getOffsetPercent());
		assertEquals(15, offsetExpression4.getOffsetPercent().shortValue());	// OFFSET 2000 PAGES...
		assertNotNull(offsetExpression4.getPageCount());			
		assertEquals(2000, offsetExpression4.getPageCount().intValue());		// ... FOR 2000 PAGES
		assertNull(offsetExpression4.getPercent());
		assertNull(areaSpecification.getSymbolicSubareaName());
		
	}	
	
	/**
	 * When neither an offset expression nor a symbolic subarea area present and a symbolic subarea
	 * is supplied, executing the command will set the symbolic subarea; undoing the command will 
	 * remove it. 
	 */
	@Test
	public void testSymbolicSubareaName_NoOffsetExpressionSet() {
		
		// create an area specification with neither an offset expression or symbolic subarea
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		
		// create the command that sets a symbolic subarea
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, "SUB1", null, null, null, null);
		
		// execute the command and check the symbolic subarea		
		command.execute();
		assertEquals("SUB1", areaSpecification.getSymbolicSubareaName());
		assertNull(areaSpecification.getOffsetExpression());
		
		// undo the command and verify that the symbolic subarea is removed
		command.undo();
		assertNull(areaSpecification.getSymbolicSubareaName());
		assertNull(areaSpecification.getOffsetExpression());
		
		// redo the command and check the symbolic subarea		
		command.redo();
		assertEquals("SUB1", areaSpecification.getSymbolicSubareaName());
		assertNull(areaSpecification.getOffsetExpression());
		
	}
	
	/**
	 * When an offset expression is set and a symbolic subarea is supplied, executing the command 
	 * will remove the offset expression and set the symbolic subarea; undoing the command will 
	 * remove the symbolic subarea and restore the offset expression. 
	 */
	@Test
	public void testSymbolicSubareaName_OffsetExpressionSet() {
		
		// create an area specification with an arbitrary offset expression (OFFSET 15 PAGES FOR 50 
		// PERCENT)
		AreaSpecification areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();
		OffsetExpression offsetExpression = SchemaFactory.eINSTANCE.createOffsetExpression();
		offsetExpression.setOffsetPageCount(15);
		offsetExpression.setPercent((short) 50);
		areaSpecification.setOffsetExpression(offsetExpression);
		
		// create the command that sets a symbolic subarea
		ChangeAreaSpecificationCommand command = 
			new ChangeAreaSpecificationCommand(areaSpecification, "SUB2", null, null, null, null);
		
		// execute the command and check the symbolic subarea		
		command.execute();
		assertEquals("SUB2", areaSpecification.getSymbolicSubareaName());
		assertNull(areaSpecification.getOffsetExpression());
		
		// undo the command and verify that the symbolic subarea is removed and the offset 
		// expression restored
		command.undo();
		assertNull(areaSpecification.getSymbolicSubareaName());
		OffsetExpression offsetExpression2 = areaSpecification.getOffsetExpression(); 
		assertTrue(offsetExpression2 == offsetExpression);
		assertEquals(Integer.valueOf(15), offsetExpression2.getOffsetPageCount());
		assertEquals(Short.valueOf((short) 50), offsetExpression2.getPercent());
		assertNull(offsetExpression2.getOffsetPercent());
		assertNull(offsetExpression2.getPageCount());
		
		// redo the command and check the symbolic subarea		
		command.redo();
		assertEquals("SUB2", areaSpecification.getSymbolicSubareaName());
		assertNull(areaSpecification.getOffsetExpression());
		
	}	

}
