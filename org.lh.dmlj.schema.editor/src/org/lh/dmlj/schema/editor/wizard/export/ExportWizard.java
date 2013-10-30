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
package org.lh.dmlj.schema.editor.wizard.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.editor.template.SchemaTemplate;

public class ExportWizard extends Wizard implements IExportWizard {

	private OutputFileSelectionPage outputFileSelectionPage;
	private SchemaSelectionPage 	schemaSelectionPage;
	private IStructuredSelection 	selection;
	
	private static String rtrim(String line) {
		StringBuilder p = new StringBuilder(line);
		while (p.length() > 0 && p.charAt(p.length() - 1) == ' ') {
			p.setLength(p.length() - 1);
		}
		return p.toString();
	}

	public ExportWizard() {
		super();
		setWindowTitle("Export");
	}
	
	@Override
	public void addPages() {
		schemaSelectionPage = new SchemaSelectionPage(selection);
		addPage(schemaSelectionPage);
		outputFileSelectionPage = new OutputFileSelectionPage();
		addPage(outputFileSelectionPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean performFinish() {
		
		Schema schema = schemaSelectionPage.getSchema();
		File file = outputFileSelectionPage.getFile();
		
		if (file.exists()) {
			String title = "Overwrite file ?";
			String message = "File '" + file.getAbsolutePath() + 
							 "' exists.  Do you want to overwrite it ?";
			String[] buttons = { "Yes", "No" };
			MessageDialog dialog = 
				new MessageDialog(getShell(), title, null, message, 
								  MessageDialog.QUESTION, buttons, 1);
			if (dialog.open() == 1) {
				return false;
			}			
		}		
		
		// generate the schema syntax (no busy cursor needed since this is lightning fast)
		try {
			SchemaTemplate template = new SchemaTemplate();
			List<Object> args = new ArrayList<Object>();
			args.add(schema);
			args.add(Boolean.TRUE);	// full syntax
			String syntax = template.generate(args); 
			// remove trailing spaces...
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			BufferedReader in = new BufferedReader(new StringReader(syntax));
			
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				out.println(rtrim(line));
			}
			
			out.flush();
			out.close();
			
			in.close();
			
		} catch (Throwable t) {
			String title = "Error";
			String p = t.getMessage() != null ? " (" + t.getMessage() + ")": "";
			String message = "An error occurred: " + 
							 t.getClass().getSimpleName() + p + ". See log.";
			String[] buttons = { "OK" };
			MessageDialog dialog = 
				new MessageDialog(getShell(), title, null, message, MessageDialog.QUESTION, buttons, 
								  1);
			dialog.open();
			return false;
		}
		
		// open the generated syntax file in a text editor:
        try {           
        	// replace backward slashes into forward ones...
        	StringBuilder p = new StringBuilder(file.getAbsolutePath());
        	for (int i = 0; i < p.length(); i++) {
        		if (p.charAt(i) == '\\') {
        			p.setCharAt(i, '/');
        		}
        	}
            URI uri = new URI("file", p.toString(), null); // make sure the file name is encoded
        	IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), 
            			   uri, "org.eclipse.ui.DefaultTextEditor", true);
	    } catch (Throwable t) {
	            // something went wrong while opening the file in a text editor, provide the user 
	    		// with some feedback...
	            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error", 
	            						"Cannot open syntax file '" + file.getAbsolutePath() + 
	            						"'.");
	            // and print a stack trace...
	            t.printStackTrace();
	    }
        
        return true;
        
	}

}
