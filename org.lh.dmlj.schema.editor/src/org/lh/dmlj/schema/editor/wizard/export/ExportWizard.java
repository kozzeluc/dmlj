package org.lh.dmlj.schema.editor.wizard.export;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

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
			PrintWriter out = new PrintWriter(new FileWriter(file));
			out.println(syntax);
			out.flush();
			out.close();		
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
