package org.lh.dmlj.schema.editor.prefix;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.SchemaRecord;

public class PrefixForPointerReorderingTest extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void test() {

		SchemaRecord record = mock(SchemaRecord.class);
		
		List<Pointer<?>> pointers = new ArrayList<>();
				
		Pointer<?> pointer1 = mock(Pointer.class);
		pointers.add(pointer1);
				
		PointerToMove<?> pointer2 = mock(PointerToMove.class);
		pointers.add(pointer2);
		
		PrefixForPointerReordering prefix = new PrefixForPointerReordering(record, pointers);
		
		prefix.reorderPointers();		
		verify(pointer2, times(1)).move();
		verify(pointer2, never()).moveBack();		
		
		prefix.reset();
		verify(pointer2, times(1)).move();
		verify(pointer2, times(1)).moveBack();		
		
	}

}
