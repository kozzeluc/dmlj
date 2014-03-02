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
package org.lh.dmlj.schema.editor.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.testtool.TestTools;

public class ToolsTest {
	
	private Schema schema;
	
	@Before
	public void setup() {
		// we'll use IDMSNTWK throughout these tests
		schema = TestTools.getIdmsntwkSchema();
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

}
