package org.lh.dmlj.schema.editor.importtool;


public interface IDataEntryPage {	
	
	void aboutToShow();
	
	boolean isPageRelevant();
	
	void setContext(IDataEntryContext context);	

	void setController(IDataEntryPageController controller);
	
}