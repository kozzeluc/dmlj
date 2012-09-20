package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ImportToolSelectionPage extends WizardPage {
	
	private ImportToolDescriptor 	   importToolDescriptor;
	private List<ImportToolDescriptor> importToolDescriptors;	

	protected ImportToolSelectionPage(List<ImportToolDescriptor> importToolDescriptors) {
		super("page2", "CA IDMS Schema", null);
		this.importToolDescriptors = importToolDescriptors;
		setMessage("Select the (data) source");
	}

	@Override
	public void createControl(Composite parent) {		
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label lblInstalledImportTools = new Label(container, SWT.NONE);
		lblInstalledImportTools.setText("Source :");
		
		final Combo combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description :");
		
		final Text text = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		text.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int i = combo.getSelectionIndex();
				importToolDescriptor = importToolDescriptors.get(i);
				text.setText(importToolDescriptor.getDescription());
				text.redraw();
			}
		});
						
		for (ImportToolDescriptor importToolDescriptor : importToolDescriptors) {
			combo.add(importToolDescriptor.getSource());
		}
		
		if (combo.getItemCount() > 0) {
			combo.select(0);
			importToolDescriptor = importToolDescriptors.get(0);
			text.setText(importToolDescriptor.getDescription());
		} else {
			combo.setEnabled(false);
			setErrorMessage("No import tools installed - cannot import a " +
					 		"CA IDMS schema");
			text.setText("Please install at least 1 plug-in that provides " +
						 "the required schema import functionality.");
		}
		
		setPageComplete(combo.getItemCount() > 0);
			
	}

	public ImportToolDescriptor getImportToolDescriptor() {
		return importToolDescriptor;
	}
	
}