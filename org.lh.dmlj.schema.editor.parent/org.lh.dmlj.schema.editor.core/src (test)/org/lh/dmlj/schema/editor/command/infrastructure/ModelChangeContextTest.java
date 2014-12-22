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
package org.lh.dmlj.schema.editor.command.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.lh.dmlj.schema.Schema;

public class ModelChangeContextTest {

	@Test
	public void test() {
		
		Schema schema = mock(Schema.class);
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		context.setSchema(schema);
		context.getContextData().put("key1", "value1");
		context.getContextData().put("key2", "value2");
		context.setListenerData("listener data");
		context.setCommandExecutionMode(CommandExecutionMode.UNDO);
		
		assertSame(ModelChangeType.SWAP_RECORD_ELEMENTS, context.getModelChangeType());
		assertSame(schema, context.getSchema());
		assertEquals(2, context.getContextData().size());
		assertEquals("value1", context.getContextData().get("key1"));
		assertEquals("value2", context.getContextData().get("key2"));
		assertEquals("listener data", context.getListenerData());
		assertSame(CommandExecutionMode.UNDO, context.getCommandExecutionMode());
		
		ModelChangeContext copy = context.copy();
		assertSame(ModelChangeType.SWAP_RECORD_ELEMENTS, copy.getModelChangeType());
		assertSame(schema, copy.getSchema());
		assertNotSame(context.getContextData(), copy.getContextData());
		assertEquals(2, copy.getContextData().size());
		assertEquals("value1", copy.getContextData().get("key1"));
		assertEquals("value2", copy.getContextData().get("key2"));
		assertNull("listener data should be erased for copies", copy.getListenerData());
		assertNull("command execution mode should be erased for copies", copy.getCommandExecutionMode());
		
	}

}
