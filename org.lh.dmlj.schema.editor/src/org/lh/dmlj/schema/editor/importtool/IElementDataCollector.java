package org.lh.dmlj.schema.editor.importtool;

import org.lh.dmlj.schema.Usage;

public interface IElementDataCollector<T> {
	
	String getBaseName(T context);

	String getDependsOnElementName(T context);

	boolean getIsNullable(T context);

	short getLevel(T context);

	String getName(T context);

	short getOccurrenceCount(T context);

	String getPicture(T context);

	String getRedefinedElementName(T context);	
	
	Usage getUsage(T context);	
	
}