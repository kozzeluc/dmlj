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

import static org.junit.Assert.*;

import org.junit.Test;

public class ModelChangeContextTest {

	@Test
	public void test() {
		
		ModelChangeContext context = new ModelChangeContext(ModelChangeType.SWAP_RECORD_ELEMENTS);
		context.getContextData().put("key1", "value1");
		context.getContextData().put("key2", "value2");
		context.setListenerData("listener data");
		
		assertSame(ModelChangeType.SWAP_RECORD_ELEMENTS, context.getModelChangeType());
		assertEquals(2, context.getContextData().size());
		assertEquals("value1", context.getContextData().get("key1"));
		assertEquals("value2", context.getContextData().get("key2"));
		assertEquals("listener data", context.getListenerData());
		
		ModelChangeContext copy = context.copy();
		assertSame(ModelChangeType.SWAP_RECORD_ELEMENTS, copy.getModelChangeType());
		assertNotSame(context.getContextData(), copy.getContextData());
		assertEquals(2, copy.getContextData().size());
		assertEquals("value1", copy.getContextData().get("key1"));
		assertEquals("value2", copy.getContextData().get("key2"));
		assertNull("listener data should be erased for copies", copy.getListenerData());
		
	}

}
