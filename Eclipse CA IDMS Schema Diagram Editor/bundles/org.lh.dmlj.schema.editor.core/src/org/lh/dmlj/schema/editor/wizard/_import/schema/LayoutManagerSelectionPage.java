/**
 * Copyright (C) 2025  Luc Hermans
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
package org.lh.dmlj.schema.editor.wizard._import.schema;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
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
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.common.ImageCache;
import org.lh.dmlj.schema.editor.extension.LayoutManagerExtensionElement;
import org.lh.dmlj.schema.editor.log.Logger;

public class LayoutManagerSelectionPage extends WizardPage {
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	private List<LayoutManagerExtensionElement> extensionElements = List.of();
	private LayoutManagerExtensionElement extensionElement;
	private ImageCache imageCache = new ImageCache();
	
	private Button btnBrowse;
	private Canvas canvas;
	private Combo combo;
	
	private final Color imageBackground = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);	
	private Text textDescription;	
	private Text textPropertiesFileName;
	
	public LayoutManagerSelectionPage() {
		super("_layoutManagerSelectionPage", "CA IDMS/DB Schema", null);
		setMessage("Select the record layout manager");	
		setPageComplete(false); // make sure the page is displayed at least once
	}
	
	@Override
	public void createControl(Composite parent) {		
		var container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		var lblInstalledLayoutManagers = new Label(container, SWT.NONE);
		lblInstalledLayoutManagers.setText("Layout manager:");
		
		combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		var lblDescription = new Label(container, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblDescription.setText("Description:");
		
		textDescription = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		var gdText = new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1);
		gdText.widthHint = 300;
		gdText.heightHint = 50;
		textDescription.setLayoutData(gdText);
		
		var lblPropertiesFile = new Label(container, SWT.NONE);
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
				selectPropertiesFile();
			}
		});
		btnBrowse.setText("Browse...");
		
		var lblExample = new Label(container, SWT.NONE);
		var gdLblExample = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gdLblExample.verticalIndent = 10;
		lblExample.setLayoutData(gdLblExample);
		lblExample.setText("Example:");
		
		canvas = new Canvas(container, SWT.NONE);
		var gdCanvas = new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1);
		gdCanvas.verticalIndent = 10;
		canvas.setLayoutData(gdCanvas);
		
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comboSelectionChanged();
			}
		});
		
		// we use the following MO to draw the layout manager image for the first time in the canvas because the
		// image might otherwise not show up...
		canvas.addPaintListener(e -> drawImage(e.gc));
		
		setExtensionElements(extensionElements);
		
		var b = extensionElements.get(0).isPromptForPropertiesFile();
		textPropertiesFileName.setEnabled(b);
		btnBrowse.setEnabled(b);
		
		validatePage();
	}
	
	private void selectPropertiesFile() {
		var fileDialog = new FileDialog(getShell());
		fileDialog.setFileName(textPropertiesFileName.getText());
		var newValue = fileDialog.open();							
		if (newValue != null) {
			textPropertiesFileName.setText(newValue);
			textPropertiesFileName.redraw();
	        validatePage();			        			
		} else {
			textPropertiesFileName.setText("");
			textPropertiesFileName.redraw();
		}
	}
	
	private void comboSelectionChanged() {
		var i = combo.getSelectionIndex();
		
		extensionElement = extensionElements.get(i);
		
		textDescription.setText(extensionElement.getDescription());
		textDescription.redraw();
		
		var promptForPropertiesFile = extensionElement.isPromptForPropertiesFile();
		textPropertiesFileName.setEnabled(promptForPropertiesFile);
		btnBrowse.setEnabled(promptForPropertiesFile);
		if (promptForPropertiesFile) {
			textPropertiesFileName.setFocus();
		}
		canvas.redraw();
		validatePage();
	}
	
	@Override
	public void dispose() {
		imageCache.dispose();
		super.dispose();
	}

	private void drawImage(GC gc) {
		if (gc != null) {
			gc.setBackground(imageBackground);
			gc.fillRectangle(0, 0, canvas.getBounds().width, canvas.getBounds().height);
			if (extensionElement != null) {
				var image = extensionElement.getImageDescriptor() != null ? imageCache.getImage(extensionElement.getImageDescriptor()) : null;
				if (image != null) {
					var width = Math.min(image.getBounds().width, canvas.getBounds().width);
					var height = Math.min(image.getBounds().height, canvas.getBounds().height);			
					gc.drawImage(image, 0, 0, width, height, 0, 0, width, height);
				}
			}
			gc.dispose();
		}
	}

	public LayoutManagerExtensionElement getExtensionElement() {
		return extensionElement;
	}	
	
	public Properties getUserEnteredParameters() {
		var properties = new Properties();
		try {			
			var file = new File(textPropertiesFileName.getText());
			if (file.exists()) {
				try (var in = new FileInputStream(file)) {
					properties.load(in);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return properties;
	}

	public void setExtensionElements(List<LayoutManagerExtensionElement> extensionElements) {
		this.extensionElements = List.copyOf(extensionElements);
		
		if (combo == null) {
			return;
		}
		combo.removeAll();
		extensionElements.stream()
				.map(LayoutManagerExtensionElement::getName)
				.forEach(combo::add);
		if (combo.getItemCount() > 0) {
			combo.select(0);
			extensionElement = extensionElements.get(0);
			textDescription.setText(extensionElement.getDescription());
		} else {
			// we shouldn't get into this situation because our plug-in provides some layout managers itself and
			// at least 1 of them should be available since it is valid for all schemas
			combo.setEnabled(false);
			setErrorMessage("No layout managers installed or none of the layout managers is valid");
			textDescription.setText("Please install at least 1 plug-in that provides a schema import layout " +
									"manager for the CA IDMS/DB schema you want to import.");
		}
		canvas.redraw();	
		validatePage();
	}

	private void validatePage() {
		setErrorMessage(null);
		var pageComplete = true;
		
		if (extensionElement.isPromptForPropertiesFile()) {
			// the properties file should be specified
			var fileName = textPropertiesFileName.getText();
			if (fileName.trim().equals("")) {
				pageComplete = false;
			} else {
				var file = new File(fileName);
				if (!file.exists()) {
					setErrorMessage("The .properties file does not exist");
					pageComplete = false;
				} else {
					try (var in = new FileInputStream(file)) {
						new Properties().load(in);
					} catch (Exception e) {
						setErrorMessage(e.getMessage());
						pageComplete = false;
					}
				}
			}
		}
		setPageComplete(pageComplete);
	}
	
}
