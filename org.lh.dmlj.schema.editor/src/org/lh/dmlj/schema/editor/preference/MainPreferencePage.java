package org.lh.dmlj.schema.editor.preference;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.wizard._import.dictguide.DictguidesImportWizard;

public class MainPreferencePage 
	extends PreferencePage implements IWorkbenchPreferencePage {

	private Button btnDelete;
	private List   list;
	private Label  lblDictionaryStructureguide;
	private Label  lblSqlGuide;	
	
	/**
	 * @wbp.parser.constructor
	 */
	public MainPreferencePage() {
		super();		
	}	

	@Override
	public void init(IWorkbench workbench) {
		// init is called before createContents(Composite parent), so the 
		// controls are not yet created
	}

	@Override
	protected Control createContents(Composite parent) {
				
		Composite container = new Composite(parent, SWT.NONE);		
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		Text lblNewLabel = new Text(container, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		GridData gd_lblNewLabel = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 150;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("Reference Guide combination to use in the \"Info\" tab (Properties view) :");
		new Label(container, SWT.NONE);
		
		list = new List(container, SWT.BORDER);
		GridData gd_list = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 3);
		gd_list.heightHint = 75;
		list.setLayoutData(gd_list);
		new Label(container, SWT.NONE);
		
		Button btnImport = new Button(container, SWT.NONE);
		btnImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				Shell shell = Display.getCurrent().getShells()[0];
				IWizard importWizard = new DictguidesImportWizard(true);
				final WizardDialog wizardDialog = 						
					new WizardDialog(shell, importWizard);
				wizardDialog.create();
				// we should move the wizard title to plugin.properties...
				wizardDialog.setTitle("CA IDMS/DB Dictionary Structure and " +
									  "SQL Reference Guides");
				Display.getCurrent().syncExec(new Runnable() {
					public void run() {
						wizardDialog.open();
					}
				});	
				
				list.removeAll();
				initialize();
				
			}
		});
		GridData gd_btnImport = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
		gd_btnImport.verticalIndent = 5;
		btnImport.setLayoutData(gd_btnImport);
		btnImport.setText("Import...");
		
		btnDelete = new Button(container, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DictguidesRegistry.INSTANCE.deleteEntry(list.getSelection()[0]);
				list.removeAll();
				initialize();
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		btnDelete.setText("Delete");
		new Label(container, SWT.NONE);
		lblDictionaryStructureguide = new Label(container, SWT.NONE);
		lblDictionaryStructureguide.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		GridData gd_lblDictionaryStructureguide = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lblDictionaryStructureguide.horizontalIndent = 10;
		gd_lblDictionaryStructureguide.verticalIndent = 5;
		lblDictionaryStructureguide.setLayoutData(gd_lblDictionaryStructureguide);
		new Label(container, SWT.NONE);
		lblSqlGuide = new Label(container, SWT.NONE);
		GridData gd_lblSqlGuide = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_lblSqlGuide.horizontalIndent = 10;
		lblSqlGuide.setLayoutData(gd_lblSqlGuide);
		lblSqlGuide.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		
		list.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDocumentTitles();
			}
		});		
		
		initialize();
		
		return container;
	}

	private void initialize() {
		
		list.add("[none]"); // id will never exist because of "<" and ">"
		for (String id : DictguidesRegistry.INSTANCE.getAllIds()) {
			list.add(id);
		}
		String activeId = DictguidesRegistry.INSTANCE.getActiveId();
		if (activeId != null) {
			list.select(list.indexOf(activeId));
			String title = 
				DictguidesRegistry.INSTANCE
								  .getDictionaryStructureTitle(activeId);
			lblDictionaryStructureguide.setText(title);
			title = DictguidesRegistry.INSTANCE.getSqlTitle(activeId);
			lblSqlGuide.setText(title);
		} else {
			list.select(0);
		}
		
		setDocumentTitles();
		
		list.setFocus();
		
	}

	@Override
	protected void performApply() {
		storeValues();
	}
	
	@Override
	protected void performDefaults() {
		list.select(0);
		setDocumentTitles();
	}
	
	@Override
	public boolean performOk() {
		storeValues();
		return true;
	}
	
	protected void setDocumentTitles() {		
		if (list.getSelectionIndex() == 0) {
			lblDictionaryStructureguide.setText("");
			lblSqlGuide.setText("");
		} else {
			String id = list.getSelection()[0];
			String title = DictguidesRegistry.INSTANCE
											 .getDictionaryStructureTitle(id);
			lblDictionaryStructureguide.setText(title);
			title = DictguidesRegistry.INSTANCE.getSqlTitle(id);
			lblSqlGuide.setText(title);
		}
		btnDelete.setEnabled(list.getSelectionIndex() > 0);
	}
	
	private void storeValues() {
		boolean changed;
		if (list.getSelectionIndex() == 0) {
			changed = DictguidesRegistry.INSTANCE.setActiveId(null);
		} else {
			changed = DictguidesRegistry.INSTANCE
										.setActiveId(list.getSelection()[0]);
		}
		if (changed) {
			// todo: make sure the Properties view gets refreshed
		}
	}

}
