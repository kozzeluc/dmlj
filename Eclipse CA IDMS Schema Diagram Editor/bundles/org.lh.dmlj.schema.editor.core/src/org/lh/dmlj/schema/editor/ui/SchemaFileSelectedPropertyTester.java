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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

public class SchemaFileSelectedPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		var selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		if (!selection.isEmpty() && selection instanceof IStructuredSelection ss && ss.getFirstElement() instanceof IFile iFile) {	
			var file = iFile.getLocation().toFile();
			if (file.getName().endsWith(".schema")) {
				return testSchemaFile(file);
			} else if (file.getName().endsWith(".schemadsl")) {
				return testSchemadslFile(file);
			}
		}
		return false;
	}
	
	private boolean testSchemaFile(File file) {
		try (var in = new BufferedReader(new FileReader(file))) {
			var line = in.readLine();
			if (line == null || !line.trim().equals("<?xml version=\"1.0\" encoding=\"ASCII\"?>")) {
				throw new IllegalStateException("not a valid schema file");
			}
			line = in.readLine();
			if (line == null || !line.trim().startsWith("<org.lh.dmlj.schema:Schema")) {
				throw new IllegalStateException("not a valid schema file");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean testSchemadslFile(File file) {
		try (var in = new BufferedReader(new FileReader(file))) {
			var line = in.readLine();
			if (line == null || !line.trim().startsWith("name '") || !line.trim().endsWith("'")) {
				throw new IllegalStateException("not a valid schemadsl file");
			}
			line = in.readLine();
			if (line == null || !line.trim().startsWith("version ")) {					
				throw new IllegalStateException("not a valid schemadsl file");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
