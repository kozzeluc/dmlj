package org.lh.dmlj.schema.editor.prefix;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.lh.dmlj.schema.SchemaRecord;

public class PrefixForPointerRemovalTest extends AbstractPointerOrPrefixRelatedTestCase {

	@Test
	public void test() {

		SchemaRecord record = mock(SchemaRecord.class);
		
		List<Pointer<?>> pointers = new ArrayList<>();
				
		Pointer<?> pointer1 = mock(Pointer.class);
		pointers.add(pointer1);
				
		PointerToUnset<?> pointer2 = mock(PointerToUnset.class);
		pointers.add(pointer2);
		
		PointerToMove<?> pointer3 = mock(PointerToMove.class);
		pointers.add(pointer3);
		
		PrefixForPointerRemoval prefix = new PrefixForPointerRemoval(record, pointers);
		
		prefix.removePointers();		
		verify(pointer2, times(1)).unset();
		verify(pointer2, never()).reset();
		verify(pointer3, times(1)).move();
		verify(pointer3, never()).moveBack();
		
		prefix.reset();
		verify(pointer2, times(1)).unset();
		verify(pointer2, times(1)).reset();
		verify(pointer3, times(1)).move();
		verify(pointer3, times(1)).moveBack();
		
	}

}
