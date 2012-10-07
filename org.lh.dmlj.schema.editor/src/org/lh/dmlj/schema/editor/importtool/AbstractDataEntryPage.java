package org.lh.dmlj.schema.editor.importtool;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.lh.dmlj.schema.editor.importtool.annotation.Context;
import org.lh.dmlj.schema.editor.importtool.annotation.Controller;

public abstract class AbstractDataEntryPage {

	@Context
	private IDataEntryContext 		 context;
	@Controller
	private IDataEntryPageController controller;

	public void aboutToShow() {		
	}
	
	public abstract Control createControl(Composite parent);

	protected final IDataEntryContext getContext() {
		return context;
	}

	protected final IDataEntryPageController getController() {
		return controller;
	}

	public boolean isPageRelevant() {
		return true;
	}	

}