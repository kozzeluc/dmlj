package org.lh.dmlj.schema.editor.importtool;

import java.util.Collection;
import java.util.Properties;

public interface ISchemaImportTool {

	void dispose();
	
	Collection<?> getAreaContexts();
		
	Collection<?> getRecordContexts();
	
	Collection<?> getRootElementContexts(Object recordContext);

	Collection<?> getSetContexts();
	
	<T> Collection<T> getSubordinateElementContexts(T elementContext);

	void init(IDataEntryContext dataEntryContext, Properties parameters,
			  IDataCollectorRegistry dataCollectorRegistry);	
	
}