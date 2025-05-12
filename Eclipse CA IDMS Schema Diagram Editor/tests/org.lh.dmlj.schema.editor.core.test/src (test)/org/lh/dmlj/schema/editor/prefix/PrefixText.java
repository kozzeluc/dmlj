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
package org.lh.dmlj.schema.editor.prefix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.SchemaRecord;

public class PrefixText extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void test() {
		
		SchemaRecord record = mock(SchemaRecord.class);
		
		List<Pointer<?>> pointers = new ArrayList<>();
				
		Pointer<?> pointer1 = mock(Pointer.class);
		pointers.add(pointer1);
				
		Pointer<?> pointer2 = mock(Pointer.class);
		pointers.add(pointer2);
		
		Prefix prefix = new Prefix(record, pointers);
		
		assertSame(record, prefix.getRecord());
		
		List<?> copyOfPointers = prefix.getPointers();
		assertNotNull(copyOfPointers);
		assertNotSame(pointers, copyOfPointers);
		assertEquals(2, copyOfPointers.size());
		assertSame(pointer1, copyOfPointers.get(0));
		assertSame(pointer2, copyOfPointers.get(1));
		
		assertNotSame(copyOfPointers, prefix.getPointers());
		
	}

}
