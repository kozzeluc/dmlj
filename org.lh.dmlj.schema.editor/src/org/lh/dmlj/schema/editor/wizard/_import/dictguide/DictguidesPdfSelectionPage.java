package org.lh.dmlj.schema.editor.wizard._import.dictguide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.dictguide.DictguidesRegistry;
import org.lh.dmlj.schema.editor.service.ServicesPlugin;
import org.lh.dmlj.schema.editor.service.api.IPdfExtractorService;

public class DictguidesPdfSelectionPage extends WizardPage {
	
	private String   description;
	private Label    lblTitle;
	private String   manualType;
	private String[] manualTypeTokens;
	private Text     textDescription;
	private Text     textFile;	

	private static String getTitle(final File file) {				
		
		// declare an array to hold the title and release information
		final String[] title = new String[1];		
		
		// create a Runnable so that we can show the busy pointer as parsing the
		// .pdf file can sometimes take a few seconds
		Runnable runnable = new Runnable() {
			public void run() {		
				try {
					title[0] = 
						DictguidesRegistry.INSTANCE.getDocumentTitle(file);					
				} catch (IOException e) {
					throw new RuntimeException(e);
				}		
			}
		};
		
		// go parse the .pdf document while showing the busy cursor
		BusyIndicator.showWhile(Display.getCurrent(), runnable);		
		
		// return the title, combined with the release information if available
		return title[0];
		
	}	
	
	/**
	 * Create the wizard.
	 */
	public DictguidesPdfSelectionPage(String manualType, String description) {
		super("wizardPage");
		this.manualType = manualType;
		this.description = description;
		List<String> list = new ArrayList<>();
		for (StringTokenizer tokenizer = new StringTokenizer(manualType);
			 tokenizer.hasMoreTokens(); ) {
			
			list.add(tokenizer.nextToken());
		}
		manualTypeTokens = list.toArray(new String[] {});
		setMessage("Select the .pdf file containing the " + manualType);
		setTitle("CA IDMS/DB Dictionary Structure and SQL Reference Guides");		
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		final Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		Label lblpdfFile = new Label(container, SWT.NONE);
		lblpdfFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpdfFile.setText(".pdf File :");
		
		textFile = new Text(container, SWT.BORDER);
		textFile.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					validatePage();
				}
			}
		});
		textFile.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePage();
			}
		});
		textFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(container.getShell());
				fileDialog.setFileName(textFile.getText());
				String newValue = fileDialog.open();							
				if (newValue != null) {
					textFile.setText(newValue);
					textFile.redraw();
			        validatePage();			        			
				} else {
					textFile.setText("");
					textFile.redraw();
				}
			}
		});
		btnBrowse.setText("Browse...");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		lblTitle = new Label(container, SWT.NONE);
		lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		textDescription = new Text(container, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gd_text.widthHint = 300;
		textDescription.setLayoutData(gd_text);
		textDescription.setText(description);
		
		IPdfExtractorService pdfExtractorService = 
			ServicesPlugin.getDefault().getService(IPdfExtractorService.class);
		btnBrowse.setEnabled(pdfExtractorService != null);
		
		if (pdfExtractorService == null) {
			setErrorMessage("PDF Extractor Service is NOT available");
		}
		
		setPageComplete(false);
		
	}
	
	public File getRefGuideFile() {
		return new File(textFile.getText().trim());
	}

	public String getRefGuideTitle() {
		return lblTitle.getText();
	}

	private boolean isTitleOK(String title) {
		int i = 0;
		for (String token : manualTypeTokens) {
			i = title.indexOf(token, i);
			if (i < 0) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isVersionOK(String title) {
		return title.endsWith(" (r16 SP2)") ||
			   title.endsWith(" (r17)") ||
			   title.endsWith(" (Version 18.0.00)");
	}

	private void validatePage() {
		
		setErrorMessage(null);
		lblTitle.setText("");
		
		if (textFile.getText().trim().equals("")) {
			setPageComplete(false);
			return;
		}
		
		boolean pageComplete = true;
		
		File file = new File(textFile.getText().trim());
		if (!file.exists()) {
			setErrorMessage("File does not exist");
			pageComplete = false;
		}
	
		try {			
			String title = getTitle(file);					
			if (!isTitleOK(title)) {
				setErrorMessage("Not a " + manualType);
				pageComplete = false;
			} else if (!isVersionOK(title)) {				
				setErrorMessage("The version of the manual you selected is " +
					            "not supported; you can try to proceed but " +
						   	    "results may be unpredictable");
			}
			lblTitle.setText(title);
		} catch (Throwable t) {
			t.printStackTrace();
			setErrorMessage("Not a " + manualType);
			pageComplete = false;
		}		
		
		setPageComplete(pageComplete);
		
	}
}
