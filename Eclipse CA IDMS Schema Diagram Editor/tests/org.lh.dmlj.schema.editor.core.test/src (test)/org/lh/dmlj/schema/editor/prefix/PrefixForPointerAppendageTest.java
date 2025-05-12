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
		
		List<Pointer<?>> pointers = new ArrayList<>();
				
		Pointer<?> pointer1 = mock(Pointer.class);
		pointers.add(pointer1);
				
		PointerToSet<?> pointer2 = mock(PointerToSet.class);
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
