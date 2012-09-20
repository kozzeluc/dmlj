package org.lh.dmlj.schema.editor.importtool;

public interface IDataEntryPageController {

	void setErrorMessage(IDataEntryPage page, String message);
	
	void setPageComplete(IDataEntryPage page, boolean pageComplete);	
	
}