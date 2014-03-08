package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SortSequence;

public interface ISortKeyDescription {
	
	String[] getElementNames();
	
	SortSequence[] getSortSequences();
	
	DuplicatesOption getDuplicatesOption();
	
	boolean isCompressed();
	
	boolean isNaturalSequence();
	
}
