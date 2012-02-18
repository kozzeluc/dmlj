package org.lh.dmlj.schema.editor.wizard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.idmsntwk.CalcKey_Schema_1045;
import org.lh.dmlj.idmsntwk.Schema_1045;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.template.SegmentTemplate;

import dmlj.core.DatabaseFactory;
import dmlj.core.DbkeyFormat;
import dmlj.core.IDatabase;
import dmlj.core.IDatabaseFactory;
import dmlj.core.type.Type;

public class NewSchemaPage5 extends WizardPage {
	private static final DateFormat DATE_FORMAT =
		new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private IDatabase				catalog;
	private Text 					text;

	protected NewSchemaPage5() {
		super("page5", "Schema", null);		
		setMessage("Select a file containing the catalog (DDLCAT area)");		
	}
	
	/*@Override
	public boolean canFlipToNextPage() {
		return catalog != null;
	}*/

	@Override
	public void createControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		
		setControl(top);
		top.setLayout(new GridLayout(3, false));
		
		Label lblPage = new Label(top, SWT.NONE);
		lblPage.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPage.setText("Catalog file :");
		
		text = new Text(top, SWT.BORDER);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePage();				
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					validatePage();
				}
			}
		});
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnNewButton = new Button(top, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(getShell());
				fileDialog.setFileName(text.getText());
				String newValue = fileDialog.open();
				text.setText(newValue);
				text.redraw();
				if (newValue != null) {
			        validatePage();			        			
				}
			}
		});
		btnNewButton.setText("Browse...");
		
		Label lblNewLabel = new Label(top, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd_lblNewLabel.verticalIndent = 5;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		lblNewLabel.setText("This file is needed to add the missing catalog records and sets.");
		new Label(top, SWT.NONE);
		new Label(top, SWT.NONE);
		new Label(top, SWT.NONE);		
				
	}
		
	public IDatabase getCatalog() {
		return catalog;
	}
	
	private IDatabase getCatalog(File catalogFile) {
		
		// get the temporary files folder from the plug-in; the IDMSNTWK mapping 
		// and schema files will be available there...
		File tmpFolder = Plugin.getDefault().getTemporaryFilesFolder();
		
		// determine the maximum records per page, pagesize, low and high page 
		// number from the dictionary file...
		int lowPageNumber;
		int highPageNumber;
		try {
			RandomAccessFile raf = new RandomAccessFile(catalogFile, "r");
			raf.seek(0);
			byte[] b = new byte[4];
			raf.read(b);
			lowPageNumber = 
				(Integer)Type.BINARY.getDecoder().decode(b, 0, 4, "S9(8)");
			raf.seek(catalogFile.length() - 4);
			raf.read(b);
			highPageNumber = 
				(Integer)Type.BINARY.getDecoder().decode(b, 0, 4, "S9(8)");
			raf.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		int pagesize = 
			((int)(catalogFile.length() / (highPageNumber - lowPageNumber + 1)));	
		int maximumRecordsPerPage = 
			DbkeyFormat.newInstance((short)8).getMaxLineNumber();
		
		// generate the segment file...
		String segmentName = 
			"temporary_DDLCAT_segment_" + DATE_FORMAT.format(new Date());
		String areaName = "DDLCAT";
		SegmentTemplate template = new SegmentTemplate();		
		Object[] args = 
			new Object[] {segmentName, areaName, 
						  Integer.valueOf(maximumRecordsPerPage),
					      catalogFile.getAbsolutePath(), 
					      Integer.valueOf(pagesize),
					      Integer.valueOf(lowPageNumber),
					      Integer.valueOf(highPageNumber)};
		String xml = template.generate(args);
		File segmentFile = new File(tmpFolder, segmentName + ".xml");
		try {
			PrintWriter out = new PrintWriter(new FileWriter(segmentFile));
			out.print(xml);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new Error(e);
		}
		
		// the mapping file property must specify the absolute mapping file path
		File mappingFile = 
			new File(tmpFolder, "org.lh.dmlj.idmsntwk.mapping");
		
		// configure and create the database factory (don't specify a .jar)...
		Properties properties = new Properties();		
		properties.put("db.default.rhdccode", "CP1140R");		
		properties.put("db.factory.class", 
					   "dmlj.core.spring.SpringDatabaseFactory");		
		properties.put("spf.segment.directory", tmpFolder.getAbsolutePath());
		properties.put("spf.schema.directory", tmpFolder.getAbsolutePath());		
		properties.put("db.default.segment", segmentName);
		properties.put("db.default.schema", "IDMSNTWK version 1");		
		properties.put("db.default.mapping", mappingFile.getAbsolutePath());		
		properties.put("db.default.rtrim_strings", "true");
		properties.put("db.default.cache_schema", "true");		
		IDatabaseFactory factory = DatabaseFactory.newInstance(properties);
		
		// create and return the dictionary...
		return factory.getDefaultDatabase();		
	}
	
	public File getCatalogFile() {
		return new File(text.getText());
	}	
	
	private void validatePage() {
		
		// it's important to nullify the catalog before manipulating the page
		// complete indicator
		if (catalog != null) {
			catalog.close();
		}
		catalog = null;
		
		setPageComplete(false);
		setErrorMessage(null);		
		
		if (text.getText().trim().equals("")) {
			return;
		} else if (!new File(text.getText()).exists()) {
			setErrorMessage("File not found");
			return;
		}
		
		final Display display = PlatformUI.getWorkbench().getDisplay();        
    	display.asyncExec(new Runnable() {
    		public void run() {		
				try {			
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							catalog = getCatalog(new File(text.getText()));
							Schema_1045 schema_1045 =
								catalog.find(Schema_1045.class, 
											 new CalcKey_Schema_1045("SYSTEM"));
							if (schema_1045 == null) {
								throw new Error("Schema SYSTEM not found");
							}					
						}
					};
					BusyIndicator.showWhile(display, runnable);
					setPageComplete(true);
				} catch (Throwable t) {
					setErrorMessage("Not a valid catalog file");			
				}
    		}
        });
        		
	}

}