package org.lh.dmlj.schema.editor.wizard.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
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
		
		try {
			SchemaTemplate template = new SchemaTemplate();
			String syntax = template.generate(schema);
			// remove trailing spaces...
			PrintWriter out = new PrintWriter(new FileWriter(file));
			
			BufferedReader in = new BufferedReader(new StringReader(syntax));
			
			for (String line = in.readLine(); line != null; line = in.readLine()) {
				out.println(rtrim(line));
			}
			
			out.flush();
			out.close();
			
			in.close();
			
			return true;
		} catch (Throwable t) {
			String title = "Error";
			String p = t.getMessage() != null ? " (" + t.getMessage() + ")": "";
			String message = "An error occurred: " + 
							 t.getClass().getSimpleName() + p + ". See log.";
			String[] buttons = { "OK" };
			MessageDialog dialog = 
				new MessageDialog(getShell(), title, null, message, 
								  MessageDialog.QUESTION, buttons, 1);
			dialog.open();
			return false;
		}
	}

}
