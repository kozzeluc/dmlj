/**
 * Copyright (C) 2014  Luc Hermans
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
package org.lh.dmlj.schema.editor.ui;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.SchemaRecord;

public class RecordSelectedPropertyTester extends PropertyTester {

	public RecordSelectedPropertyTester() {
		super();
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		ISelection selection;
		try {
			selection = PlatformUI.getWorkbench()
			 					  .getActiveWorkbenchWindow()
					 			  .getSelectionService()
					 			  .getSelection();
		} catch (Throwable t) {
			return false;
		}
		if (selection.isEmpty() || !(selection instanceof IStructuredSelection)) {
			return false;
		}
		IStructuredSelection ss = (IStructuredSelection) selection;
		if (!(ss.getFirstElement() instanceof EditPart)) {
			return false;
		}
		EditPart editPart = (EditPart) ss.getFirstElement();
		return editPart.getModel() instanceof SchemaRecord;
	}

}
