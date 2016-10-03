/**
 * Copyright (C) 2016  Luc Hermans
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
package org.lh.dmlj.schema.editor.importtool;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.lh.dmlj.schema.editor.wizard._import.schema.Context;
import org.lh.dmlj.schema.editor.wizard._import.schema.Controller;

public abstract class AbstractDataEntryPage {

	@Context
	private IDataEntryContext 		 context;
	@Controller
	private IDataEntryPageController controller;

	public void aboutToShow() {		
	}
	
	public abstract Control createControl(Composite parent);

	public void dispose() {
	}
	
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
