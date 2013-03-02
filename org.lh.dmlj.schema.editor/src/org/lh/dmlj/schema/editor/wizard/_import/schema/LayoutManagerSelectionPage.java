package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.lh.dmlj.schema.editor.common.ImageCache;
import org.lh.dmlj.schema.editor.extension.LayoutManagerExtensionElement;

public class LayoutManagerSelectionPage extends WizardPage {

	private Button 								btnBrowse;
	private Canvas 						  		canvas;
	private Combo 						  		combo;
	private LayoutManagerExtensionElement 	    extensionElement;
	private List<LayoutManagerExtensionElement> extensionElements = 
	new ArrayList<>();
	private Color 						  		imageBackground = 
		Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	private ImageCache 				 	  		imageCache = new ImageCache();
	private Label 								lblExample;
	private Text 					  			textDescription;	
	private Text 								textPropertiesFileName;
	
	public LayoutManagerSelectionPage() {
		super("_layoutManagerSelectionPage", "CA IDMS/DB Schema", null);
		setMessage("Select the record layout manager");	
		setPageComplete(false); // make sure the page is displayed at least once
	}
	
	@Override
	public void createControl(Composite parent) {		
		final Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		Label lblInstalledLayoutManagers = new Label(container, SWT.NONE);
		lblInstalledLayoutManagers.setText("Layout manager:");
		
		combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description:");
		
		textDescription = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_text = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gd_text.widthHint = 300;
		gd_text.heightHint = 50;
		textDescription.setLayoutData(gd_text);
		
		Label lblPropertiesFile = new Label(container, SWT.NONE);
		lblPropertiesFile.setText("Properties file:");
		
		textPropertiesFileName = new Text(container, SWT.BORDER);
		textPropertiesFileName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					validatePage();
				}
			}
		});
		textPropertiesFileName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validatePage();
			}
		});
		textPropertiesFileName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnBrowse = new Button(container, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(container.getShell());
				fileDialog.setFileName(textPropertiesFileName.getText());
				String newValue = fileDialog.open();							
				if (newValue != null) {
					textPropertiesFileName.setText(newValue);
					textPropertiesFileName.redraw();
			        validatePage();			        			
				} else {
					textPropertiesFileName.setText("");
					textPropertiesFileName.redraw();
				}				
			}
		});
		btnBrowse.setText("Browse...");
		
		lblExample = new Label(container, SWT.NONE);
		GridData gd_lblExample = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_lblExample.verticalIndent = 10;
		lblExample.setLayoutData(gd_lblExample);
		lblExample.setText("Example:");
		
		canvas = new Canvas(container, SWT.NONE);
		GridData gd_canvas = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gd_canvas.verticalIndent = 10;
		canvas.setLayoutData(gd_canvas);
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				int i = combo.getSelectionIndex();
				
				extensionElement = extensionElements.get(i);
				
				textDescription.setText(extensionElement.getDescription());
				textDescription.redraw();
				
				boolean b = extensionElement.isPromptForPropertiesFile();
				textPropertiesFileName.setEnabled(b);
				btnBrowse.setEnabled(b);
				if (b) {
					textPropertiesFileName.setFocus();
				}
			
				drawImage();
				
				validatePage();
					
			}
		});		
		
		// we use the following MO to draw the layout manager image for the 
		// first time in the canvas because the image might otherwise not show 
		// up...
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				drawImage();
			}
		});
		
		setExtensionElements(extensionElements);
		
		boolean b = extensionElements.get(0).isPromptForPropertiesFile();
		textPropertiesFileName.setEnabled(b);
		btnBrowse.setEnabled(b);
		
		validatePage();
			
	}
	
	@Override
	public void dispose() {
		imageBackground.dispose();
		imageCache.dispose();
		super.dispose();
	}

	private void drawImage() {
				
		final GC gc = new GC(canvas);
		// clear the canvas first
		gc.setBackground(imageBackground);
		gc.fillRectangle(0, 0, canvas.getBounds().width, 
						 canvas.getBounds().height);
		// get the image if possible and available
		Image image = 
			extensionElement != null ? 
			imageCache.getImage(extensionElement.getImageDescriptor()) : 
			null;
		// fill the canvas with the image
		if (image != null) {			
			// an image is available; make it show up in the canvas
			int width = 
				Math.min(image.getBounds().width, canvas.getBounds().width);
			int height = 
				Math.min(image.getBounds().height, canvas.getBounds().height);			
			gc.drawImage(image, 0, 0, width, height, 0, 0, width, height);
		}
		gc.dispose();		
				
	}

	public LayoutManagerExtensionElement getExtensionElement() {
		return extensionElement;
	}	
	
	public Properties getUserEnteredParameters() {
		Properties properties = new Properties();
		try {			
			File file = new File(textPropertiesFileName.getText());
			InputStream in = new FileInputStream(file);
			properties.load(in);
			in.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return properties;
	}

	public void setExtensionElements(List<LayoutManagerExtensionElement> extensionElements) {
		
		this.extensionElements = extensionElements;
		
		if (combo == null) {
			return;
		}
		
		combo.removeAll();
		
		for (LayoutManagerExtensionElement extensionElement : extensionElements) {			
			combo.add(extensionElement.getName());
		}
		
		if (combo.getItemCount() > 0) {
			combo.select(0);
			extensionElement = extensionElements.get(0);
			textDescription.setText(extensionElement.getDescription());
			drawImage();
		} else {
			// we shouldn't get into this situation because our plug-in provides 
			// some layout managers itself and at least 1 of them should be
			// available since it is valid for all schemas
			combo.setEnabled(false);
			setErrorMessage("No layout managers installed or none of the " +
							"layout managers is valid");
			textDescription.setText("Please install at least 1 plug-in that provides a " +
						 "schema import layout manager for the CA IDMS/DB " +
						 "schema you want to import.");
			drawImage();
		}		
		
		validatePage();		
		
	}

	private void validatePage() {
		
		setErrorMessage(null);
		boolean pageComplete = true;
		
		if (extensionElement.isPromptForPropertiesFile()) {
			// the properties file should be specified
			String fileName = textPropertiesFileName.getText();
			if (fileName.trim().equals("")) {
				pageComplete = false;
			} else {
				File file = new File(fileName);
				if (!file.exists()) {
					setErrorMessage("The .properties file does not exist");
					pageComplete = false;
				} else {
					try {
						Properties properties = new Properties();
						InputStream in = new FileInputStream(file);
						properties.load(in);
						in.close();
					} catch (Throwable t) {
						setErrorMessage(t.getMessage());
						pageComplete = false;
					}
				}
			}
		}
		
		setPageComplete(pageComplete);
		
	}
}