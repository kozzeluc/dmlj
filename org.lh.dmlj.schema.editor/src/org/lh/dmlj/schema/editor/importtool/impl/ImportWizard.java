package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;

public class ImportWizard extends Wizard implements IImportWizard {

	private IDataEntryContext 		   context = new DataEntryContextImpl();
	private List<ImportToolDescriptor> importToolDescriptors = new ArrayList<>();
	private ImportToolSelectionPage    importToolSelectionPage;
	private OutputFileSelectionPage    outputFileSelectionPage;
	private IStructuredSelection 	   selection;
	
	private static String getAttribute(IConfigurationElement configElem,
									   String name, String defaultValue) {
		
		String value = configElem.getAttribute(name);
		if (value != null) {
			return value;
		}
		if (defaultValue != null) {
			return defaultValue;
		}
		throw new IllegalArgumentException("Missing " + name + " attribute");
	}
	
	public ImportWizard() {
		super();
		setWindowTitle("Import");
	}

	@Override
	public void addPages() {
		
		importToolSelectionPage = 
			new ImportToolSelectionPage(importToolDescriptors);
		addPage(importToolSelectionPage);
		
		outputFileSelectionPage = new OutputFileSelectionPage(selection);
		outputFileSelectionPage.setContext(context);
		addPage(outputFileSelectionPage);		
		
	}	
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == importToolSelectionPage) {
			ImportToolDescriptor importToolDescriptor =
				importToolSelectionPage.getImportToolDescriptor();
			outputFileSelectionPage.setImportToolDescriptor(importToolDescriptor);
			return outputFileSelectionPage;
		}
		return null;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {		
		
		this.selection = selection;
		
		IExtension[] extensions = 
			Platform.getExtensionRegistry()
					.getExtensionPoint(Plugin.PLUGIN_ID, "import")
					.getExtensions();
		for (IExtension extension : extensions) {	
			ImportToolDescriptor importToolDescriptor =
				new ImportToolDescriptor();
			importToolDescriptor.setPluginId(extension.getNamespaceIdentifier());
			IConfigurationElement[] configElements =
				extension.getConfigurationElements();			
			for (IConfigurationElement configElement : configElements) {				
				if (configElement.getName().equals("dataEntryPage")) {
					DataEntryPageDescriptor dataEntryPageDescriptor = 
						new DataEntryPageDescriptor();
					dataEntryPageDescriptor.setId(getAttribute(configElement, "id", null));
					dataEntryPageDescriptor.setName(getAttribute(configElement, "name", null));
					dataEntryPageDescriptor.setImplementingClass(getAttribute(configElement, "class", null));
					dataEntryPageDescriptor.setMessage(getAttribute(configElement, "message", 
									 								"[no message available]"));
					importToolDescriptor.getPageDescriptors()
										.add(dataEntryPageDescriptor);
				} else if (configElement.getName().equals("importTool")) {
					importToolDescriptor.setId(getAttribute(configElement, "id", null));
					importToolDescriptor.setName(getAttribute(configElement, "name", null));
					importToolDescriptor.setSource(getAttribute(configElement, "source", null));
					importToolDescriptor.setImplementingClass(getAttribute(configElement, "class", null));
					importToolDescriptor.setDescription(getAttribute(configElement, "description", null));
				}
			}
			importToolDescriptors.add(importToolDescriptor);
		}		
	}

	@Override
	public boolean performFinish() {
		
		ImportToolDescriptor importToolDescriptor =
			importToolSelectionPage.getImportToolDescriptor();		
		
		System.out.println("the schema import would be executed by class " +
						   importToolDescriptor.getImplementingClass() + "'s " +
						   "performTask(...) method; context passed:");
		for (String name : context.getAttributeNames()) {
			Object value = context.getAttribute(name);
			System.out.println(name + "=" + value.toString());
		}
		
		return true;
	}
	
}