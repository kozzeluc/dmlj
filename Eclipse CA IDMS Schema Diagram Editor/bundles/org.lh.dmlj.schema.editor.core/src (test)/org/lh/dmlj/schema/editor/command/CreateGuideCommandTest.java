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

import org.junit.Test;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaFactory;

public class CreateGuideCommandTest {

	@Test
	public void test() {
		
		// create the ruler and add 2 guides to it
		Ruler ruler = SchemaFactory.eINSTANCE.createRuler();
		ruler.getGuides().add(SchemaFactory.eINSTANCE.createGuide());
		ruler.getGuides().get(0).setPosition(1);
		ruler.getGuides().add(SchemaFactory.eINSTANCE.createGuide());
		ruler.getGuides().get(1).setPosition(2);
		
		// the new guide should have its position attribute set to 5
		int position = 5;
		
		// create the command
		CreateGuideCommand command = new CreateGuideCommand(ruler, position);		
		
		// execute the command and check if the guide is added
		command.execute();
		assertEquals(3, ruler.getGuides().size());
		assertEquals(1, ruler.getGuides().get(0).getPosition());
		assertEquals(2, ruler.getGuides().get(1).getPosition());
		Guide guide = ruler.getGuides().get(2);
		assertEquals(position, guide.getPosition());
		
		
		// undo the command and check if the guide is removed
		command.undo();
		assertEquals(2, ruler.getGuides().size());
		assertEquals(1, ruler.getGuides().get(0).getPosition());		
		assertEquals(2, ruler.getGuides().get(1).getPosition());
		
				
		// redo the command and check if the guide is there again, unchanged
		command.redo();
		assertEquals(3, ruler.getGuides().size());
		assertEquals(1, ruler.getGuides().get(0).getPosition());
		assertEquals(2, ruler.getGuides().get(1).getPosition());
		Guide guide2 = ruler.getGuides().get(2);
		assertTrue(guide2 == guide);
		assertEquals(position, guide2.getPosition());
	}

}
