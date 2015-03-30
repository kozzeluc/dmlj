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

public class DeleteGuideCommandTest {

	@Test
	public void test() {
		
		// create the ruler and add 3 guides to it; we will be removing the middle one
		Ruler ruler = SchemaFactory.eINSTANCE.createRuler();
		ruler.getGuides().add(SchemaFactory.eINSTANCE.createGuide());
		ruler.getGuides().get(0).setPosition(1);
		Guide targetForDeletion = SchemaFactory.eINSTANCE.createGuide();
		ruler.getGuides().add(targetForDeletion);
		ruler.getGuides().get(1).setPosition(2);
		ruler.getGuides().add(SchemaFactory.eINSTANCE.createGuide());
		ruler.getGuides().get(2).setPosition(3);
		
		// create the command
		DeleteGuideCommand command = new DeleteGuideCommand(ruler, targetForDeletion);		
		
		// execute the command and check if the guide is removed
		command.execute();
		assertEquals(2, ruler.getGuides().size());
		assertEquals(1, ruler.getGuides().get(0).getPosition());
		assertEquals(3, ruler.getGuides().get(1).getPosition());
		
		
		// undo the command and check if the guide is inserted again
		command.undo();
		assertEquals(3, ruler.getGuides().size());
		assertEquals(1, ruler.getGuides().get(0).getPosition());
		assertTrue(ruler.getGuides().get(1) == targetForDeletion);
		assertEquals(2, ruler.getGuides().get(1).getPosition());
		assertEquals(3, ruler.getGuides().get(2).getPosition());		
		
		
		// redo the command and check if the guide is removed again
		command.redo();
		assertEquals(2, ruler.getGuides().size());
		assertEquals(1, ruler.getGuides().get(0).getPosition());
		assertEquals(3, ruler.getGuides().get(1).getPosition());
	}

}
