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
import static org.junit.Assert.assertTrue;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertSetRemoved;

import org.eclipse.gef.commands.Command;
import org.junit.Before;
import org.junit.Test;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class DeleteSetCommandTest {
	
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
	
	private void testDeleteSetCommand(Set set) {
		
		approve(set);
		
		Command command = new DeleteSetCommand(set);		
		
		command.execute();
		Xmi touchedXmi = TestTools.asXmi(schema);
		ObjectGraph touchedObjectGraph = TestTools.asObjectGraph(schema);
		assertSetRemoved(schema, set.getName());		

		command.undo();
		checkXmi(xmi);
		checkObjectGraph(objectGraph);		
		
		command.redo();
		checkXmi(touchedXmi);
		checkObjectGraph(touchedObjectGraph);
		
	}
	
	private void approve(Set set) {
		assertTrue("cannot remove system owned indexed sets", set.getSystemOwner() == null);
		assertEquals("cannot directly remove multiple-member sets", 1, set.getMembers().size());
		assertTrue("make the member record DIRECT first",
				   set.getMembers().get(0).getRecord().getViaSpecification() == null ||
				   set.getMembers().get(0).getRecord().getViaSpecification().getSet() != set);
		assertEquals(1, set.getMembers().get(0).getConnectionParts().size());
		assertTrue(set.getMembers().get(0).getConnectionParts().get(0).getBendpointLocations().isEmpty());
	}
	
	@Before
	public void setup() {
		// we'll use EMPSCHM throughout these tests
		schema = TestTools.getEmpschmSchema();
		objectGraph = TestTools.asObjectGraph(schema);
		xmi = TestTools.asXmi(schema);
	}	
	
	@Test
	public void testSortedChainedSet() {		
		
		// delete set EMP-EXPERTISE, but first change record EXPERTISE's location mode to DIRECT
		SchemaRecord recordExpertise = schema.getRecord("EXPERTISE");
		TestTools.makeDirect(recordExpertise);
		xmi = TestTools.asXmi(schema);
		objectGraph = TestTools.asObjectGraph(schema);
		
		Set set = TestTools.getSet(schema, "EMP-EXPERTISE");				
		testDeleteSetCommand(set);
		
	}
	
	@Test
	public void testUnsortedChainedSet() {
		Set set = TestTools.getSet(schema, "JOB-EMPOSITION");				
		testDeleteSetCommand(set);
	}
	
	@Test
	public void testSortedIndexedSet() {
		Set set = TestTools.getSet(schema, "SKILL-EXPERTISE");				
		testDeleteSetCommand(set);
	}	
	
	@Test
	public void testUnsortedIndexedSet() {
		
		// change the order of set SKILL-EXPERTISE to LAST
		Set set = TestTools.getSet(schema, "SKILL-EXPERTISE");				
		TestTools.makeLast(set);
		xmi = TestTools.asXmi(schema);
		objectGraph = TestTools.asObjectGraph(schema);
		
		testDeleteSetCommand(set);
		
	}
	
	@Test
	public void testSourceEndPointHandling() {
		
		Set set = TestTools.getSet(schema, "SKILL-EXPERTISE");
		TestTools.addSourceEndPoint(set);
		xmi = TestTools.asXmi(schema);
		objectGraph = TestTools.asObjectGraph(schema);
		
		testDeleteSetCommand(set);
		
	}
	
	@Test
	public void testTargetEndPointHandling() {
		
		Set set = TestTools.getSet(schema, "SKILL-EXPERTISE");
		TestTools.addTargetEndPoint(set);
		xmi = TestTools.asXmi(schema);
		objectGraph = TestTools.asObjectGraph(schema);
		
		testDeleteSetCommand(set);
		
	}
	
	@Test
	public void testSourceAndTargetEndPointHandling() {
		
		Set set = TestTools.getSet(schema, "SKILL-EXPERTISE");
		TestTools.addSourceAndTargetEndPoints(set);
		xmi = TestTools.asXmi(schema);
		objectGraph = TestTools.asObjectGraph(schema);
		
		testDeleteSetCommand(set);
		
	}	

}
