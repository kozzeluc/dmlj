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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asObjectGraph;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asXmi;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;

public class DeleteVsamIndexCommandTest {

	@Test
	public void test() {
		Schema schema = TestTools.getSchema("testdata/VSAMTEST version 1.schema");
		ObjectGraph objectGraph1 = asObjectGraph(schema);
		Xmi xmi1 = asXmi(schema);
		VsamIndex vsamIndex = schema.getSet("KSDSPAD").getVsamIndex();
		SchemaRecord record = vsamIndex.getRecord();
		Element element = vsamIndex.getMemberRole().getSortKey().getElements().get(0).getElement();
		assertEquals("SLEUTEL-KSDS-1301", element.getName());
		DiagramData diagramData = schema.getDiagramData();
		assertEquals(3, schema.getSets().size());
		assertEquals(3, record.getMemberRoles().size());
		assertEquals(3, record.getKeys().size());
		assertEquals(1, element.getKeyElements().size());
		assertEquals(8, diagramData.getLocations().size());
		assertEquals(3, diagramData.getConnectionLabels().size());
		assertEquals(3, diagramData.getConnectionParts().size());
		assertEquals(0, diagramData.getConnectors().size());
		
		Command command = new DeleteVsamIndexCommand(vsamIndex);
		
		command.execute();
		ObjectGraph objectGraph2 = asObjectGraph(schema);
		Xmi xmi2 = asXmi(schema);
		// check collection sizes
		assertEquals(2, schema.getSets().size());
		assertEquals(2, record.getMemberRoles().size());
		assertEquals(2, record.getKeys().size());
		assertEquals(0, element.getKeyElements().size());
		assertEquals(6, diagramData.getLocations().size());
		assertEquals(2, diagramData.getConnectionLabels().size());
		assertEquals(2, diagramData.getConnectionParts().size());
		assertEquals(0, diagramData.getConnectors().size());
		// check set
		assertNull(schema.getSet("KSDSPAD"));
		// check record
		for (MemberRole memberRole : record.getMemberRoles()) {
			assertFalse(memberRole.getSet().getName().equals("KSDSPAD"));
		}
		for (Key key : record.getKeys()) {
			assertFalse(key.isCalcKey()); // we don't expect a CALC key since the record is VSAM
			assertFalse(key.getMemberRole().getSet().getName().equals("KSDSPAD"));
		}
		// check connection labels
		for (ConnectionLabel connectionLabel : diagramData.getConnectionLabels()) {
			assertFalse(connectionLabel.getMemberRole().getSet().getName().equals("KSDSPAD"));
		}
		// check connection parts
		for (ConnectionPart connectionPart : diagramData.getConnectionParts()) {
			assertFalse(connectionPart.getMemberRole().getSet().getName().equals("KSDSPAD"));
		}
		// check diagram locations
		for (DiagramLocation diagramLocation : diagramData.getLocations()) {
			assertFalse(diagramLocation.getEyecatcher().indexOf("KSDSPAD") > -1);
		}
		// make sure the element is still referenced by the record
		assertSame(element, record.getElement("SLEUTEL-KSDS-1301"));
		
		command.undo();
		ObjectGraph objectGraph3 = asObjectGraph(schema);
		Xmi xmi3 = asXmi(schema);
		assertEquals(objectGraph1, objectGraph3);
		assertEquals(xmi1, xmi3);
		
		command.redo();
		ObjectGraph objectGraph4 = asObjectGraph(schema);
		Xmi xmi4 = asXmi(schema);
		assertEquals(objectGraph2, objectGraph4);		
		assertEquals(xmi2, xmi4);				
	}

}
