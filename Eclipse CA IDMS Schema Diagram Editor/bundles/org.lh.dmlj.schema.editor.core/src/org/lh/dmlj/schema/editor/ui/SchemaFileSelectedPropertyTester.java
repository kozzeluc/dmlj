/**
 * Copyright (C) 2013  Luc Hermans
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

public class SchemaFileSelectedPropertyTester extends PropertyTester {

	public SchemaFileSelectedPropertyTester() {
		super();
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
					    Object expectedValue) {

		ISelection selection;
		try {
			selection = PlatformUI.getWorkbench()
			 					  .getActiveWorkbenchWindow()
					 			  .getSelectionService()
					 			  .getSelection();
		} catch (Throwable t) {
			return false;
		}
		if (selection.isEmpty() || 
			!(selection instanceof IStructuredSelection)) {
			
			return false;
		}
		IStructuredSelection ss = (IStructuredSelection) selection;
		if (!(ss.getFirstElement() instanceof IFile)) {
			return false;
		}
		
		IFile iFile = (IFile) ss.getFirstElement();
		File file = iFile.getLocation().toFile();		
		
		boolean validSchema = false; 		
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String line = in.readLine();
			if (line == null || 
				!line.trim().equals("<?xml version=\"1.0\" encoding=\"ASCII\"?>")) {
				
				throw new RuntimeException("not a valid schema file");
			}
			line = in.readLine();
			if (line == null || 
				!line.trim().startsWith("<org.lh.dmlj.schema:Schema")) {
				
				throw new RuntimeException("not a valid schema file");
			}
			validSchema = true;
		} catch (Throwable t) {
		}
		return validSchema;
	}

}
