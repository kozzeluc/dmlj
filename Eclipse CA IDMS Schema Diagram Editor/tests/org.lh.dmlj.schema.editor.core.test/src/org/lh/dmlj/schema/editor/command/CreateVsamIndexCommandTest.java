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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asObjectGraph;
import static org.lh.dmlj.schema.editor.testtool.TestTools.asXmi;
import static org.lh.dmlj.schema.editor.testtool.TestTools.assertEquals;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.gef.commands.Command;
import org.junit.Test;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.figure.VsamIndexFigure;
import org.lh.dmlj.schema.editor.testtool.ObjectGraph;
import org.lh.dmlj.schema.editor.testtool.TestTools;
import org.lh.dmlj.schema.editor.testtool.Xmi;


public class CreateVsamIndexCommandTest {

	@Test
	public void test() {

		Schema schema = TestTools.getSchema("testdata/VSAMTEST version 1.schema");
		ObjectGraph objectGraph1 = asObjectGraph(schema);
		Xmi xmi1 = asXmi(schema);
		SchemaRecord record = schema.getRecord("KSDS-REC-1301");
		Element element = record.getElement("SLEUTEL-KSDS-1301");
		DiagramData diagramData = schema.getDiagramData();
		assertEquals(3, schema.getSets().size());
		assertEquals(3, record.getMemberRoles().size());
		assertEquals(3, record.getKeys().size());
		assertEquals(1, element.getKeyElements().size());
		assertEquals(8, diagramData.getLocations().size());
		assertEquals(3, diagramData.getConnectionLabels().size());
		assertEquals(3, diagramData.getConnectionParts().size());
		assertEquals(0, diagramData.getConnectors().size());
		
		Command command = new CreateVsamIndexCommand(record);
		
		command.execute();
		ObjectGraph objectGraph2 = asObjectGraph(schema);
		Xmi xmi2 = asXmi(schema);
		// check collection sizes
		assertEquals(4, schema.getSets().size());
		assertEquals(4, record.getMemberRoles().size());
		assertEquals(4, record.getKeys().size());
		assertEquals(2, element.getKeyElements().size());
		assertEquals(10, diagramData.getLocations().size());
		assertEquals(4, diagramData.getConnectionLabels().size());
		assertEquals(4, diagramData.getConnectionParts().size());
		assertEquals(0, diagramData.getConnectors().size());
		// check set
		Set set = schema.getSet("NEW-VSAM-INDEX-1");
		assertNotNull(set);
		assertEquals(schema.getSets().size() - 1, schema.getSets().indexOf(set));
		assertSame(SetMode.VSAM_INDEX, set.getMode());
		assertSame(SetOrder.SORTED, set.getOrder());
		assertEquals(1, set.getMembers().size());
		assertNull(set.getOwner());
		assertNull(set.getSystemOwner());
		// check VSAM index
		VsamIndex vsamIndex = set.getVsamIndex();
		assertNotNull(vsamIndex);
		// check member role
		MemberRole memberRole = set.getMembers().get(0);
		assertEquals(record.getMemberRoles().size() - 1, record.getMemberRoles().indexOf(memberRole));
		assertEquals(SetMembershipOption.MANDATORY_AUTOMATIC, memberRole.getMembershipOption());
		// check key
		Key key = memberRole.getSortKey();
		assertNotNull(key);
		assertEquals(1, key.getElements().size());
		assertEquals(record.getKeys().size() -1, record.getKeys().indexOf(key));
		assertSame(DuplicatesOption.NOT_ALLOWED, key.getDuplicatesOption());
		// check key element
		KeyElement keyElement = key.getElements().get(0);
		assertNotNull(keyElement);
		assertSame(element, keyElement.getElement());
		assertSame(SortSequence.ASCENDING, keyElement.getSortSequence());
		// check VSAM index diagram location
		DiagramLocation vsamIndexLocation = vsamIndex.getDiagramLocation();
		assertNotNull(vsamIndexLocation);
		assertEquals(diagramData.getLocations().size() - 2, 
					 diagramData.getLocations().indexOf(vsamIndexLocation));
		assertEquals("VSAM index " + set.getName(), vsamIndexLocation.getEyecatcher());
		assertEquals(record.getDiagramLocation().getX() - VsamIndexFigure.UNSCALED_WIDTH, 
					 vsamIndexLocation.getX());
		assertEquals(record.getDiagramLocation().getY() - 2 * VsamIndexFigure.UNSCALED_HEIGHT,
					 vsamIndexLocation.getY());
		// check connection label 
		ConnectionLabel connectionLabel = memberRole.getConnectionLabel();
		assertNotNull(connectionLabel);
		assertEquals(diagramData.getConnectionLabels().size() -1, 
					 diagramData.getConnectionLabels().indexOf(connectionLabel));
		// check connection label diagram location
		DiagramLocation descriptionLocation = connectionLabel.getDiagramLocation();
		assertNotNull(descriptionLocation);
		assertEquals(diagramData.getLocations().size() - 1, 
					 diagramData.getLocations().indexOf(descriptionLocation));
		assertEquals("set label " + set.getName() + " (" + record.getName() +")", 
					 descriptionLocation.getEyecatcher());
		assertEquals(vsamIndexLocation.getX() + VsamIndexFigure.UNSCALED_WIDTH + 5, 
					 descriptionLocation.getX());
		assertEquals(vsamIndexLocation.getY(), descriptionLocation.getY());
		// check connection part
		assertNotNull(memberRole.getConnectionParts());
		assertEquals(1, memberRole.getConnectionParts().size());		
		assertNotNull(memberRole.getConnectionParts().get(0));
		assertEquals(diagramData.getConnectionParts().size() -1, 
				 	 diagramData.getConnectionParts().indexOf(memberRole.getConnectionParts().get(0)));
		
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
	
	@Test
	public void testRecordWithNoKeyCandidate() {
		Schema schema = TestTools.getSchema("testdata/VSAMTST2 version 1.schema");
		SchemaRecord record = schema.getRecord("KSDS-REC-1301");
		Command command = new CreateVsamIndexCommand(record);
		
		try {
			command.execute();
			fail("should throw an AssertionFailedException");
		} catch (AssertionFailedException e) {
			assertEquals("null argument:VSAM indexes are always SORTED and no sort element is " +
						 "available: KSDS-REC-1301", e.getMessage());
		}
	}

}
