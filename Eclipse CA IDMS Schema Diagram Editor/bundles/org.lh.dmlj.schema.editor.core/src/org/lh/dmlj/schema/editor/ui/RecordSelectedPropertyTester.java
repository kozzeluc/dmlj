/**
 * Copyright (C) 2025  Luc Hermans
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.SchemaRecord;

public class RecordSelectedPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		
		try {
			var selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
			return !selection.isEmpty() && selection instanceof IStructuredSelection structuredSelection &&
				   structuredSelection.getFirstElement() instanceof EditPart editPart && editPart.getModel() instanceof SchemaRecord;
		} catch (Exception e) {
			return false;
		}
	}

}
