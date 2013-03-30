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
