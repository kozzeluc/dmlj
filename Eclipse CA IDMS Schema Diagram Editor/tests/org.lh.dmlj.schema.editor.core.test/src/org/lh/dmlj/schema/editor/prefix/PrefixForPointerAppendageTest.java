/**
 * Copyright (C) 2025  Luc Hermans
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.SchemaRecord;

public class PrefixForPointerAppendageTest extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void test() {

		SchemaRecord record = mock(SchemaRecord.class);
		
		List<Pointer> pointers = new ArrayList<>();
				
		Pointer pointer1 = mock(Pointer.class);
		pointers.add(pointer1);
				
		PointerToSet pointer2 = mock(PointerToSet.class);
		pointers.add(pointer2);
		
		PrefixForPointerAppendage prefix = new PrefixForPointerAppendage(record, pointers);
		
		prefix.appendPointers();		
		verify(pointer2, times(1)).set();
		verify(pointer2, never()).unset();
		
		prefix.reset();
		verify(pointer2, times(1)).set();
		verify(pointer2, times(1)).unset();
		
	}

}
