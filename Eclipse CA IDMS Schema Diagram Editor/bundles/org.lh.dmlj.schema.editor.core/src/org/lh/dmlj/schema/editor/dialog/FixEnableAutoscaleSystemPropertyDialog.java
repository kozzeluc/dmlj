/**
 * Copyright (C) 2026  Luc Hermans
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
package org.lh.dmlj.schema.editor.dialog;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.log.Logger;

public class FixEnableAutoscaleSystemPropertyDialog extends TitleAreaDialog {
	private static final String DIAGRAM_EDITOR = "CA IDMS/DB Schema Diagram Editor";
	private static final String ECLIPSE_HOME_LOCATION = "eclipse.home.location";
	private static final String PROPERTY_NAME = "draw2d.enableAutoscale";
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	private final String currentValue;
	private final File eclipseHomeLocation;
	private File eclipseIniFile;
	private List<String> eclipseIniFileContent = List.of();
	private boolean fixInConfigurrationFile = true;
	private boolean restartWorkbench = true;
	
	private Text textCurrentValue;
	private Text textConfigurationFile;
	private Button btnFixInConfigurationFile;
	private Button btnRestartWorkbench;

	public FixEnableAutoscaleSystemPropertyDialog(Shell parentShell) {
		super(parentShell);
		currentValue = System.getProperty(PROPERTY_NAME);
		eclipseHomeLocation = calculateEclipseHomeLocation();
		eclipseIniFile = new File(eclipseHomeLocation, "eclipse.ini");
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setHelpAvailable(false);
	}
	
	private File calculateEclipseHomeLocation() {
		try {			
			return new File(new URI(System.getProperty(ECLIPSE_HOME_LOCATION)));
		} catch (URISyntaxException e) {
			logger.error("An exception was thrown while processing '%s'".formatted(ECLIPSE_HOME_LOCATION), e);
			return null;
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(750, 500);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setImage(Display.getCurrent().getSystemImage(SWT.ICON_WARNING));
		newShell.setText("Warning");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("System property '%s' not set or set to true".formatted(PROPERTY_NAME));
		setMessage("Please review the following warning message and take appropriate action");
		
		var area = (Composite) super.createDialogArea(parent);
		var container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(3, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		var textDescription = new Text(container, SWT.READ_ONLY | SWT.WRAP);
		textDescription.setText(
				"""
				In order for the %s to work properly on Eclipse 2025-12 and higher, system property '%s' must be \
				set to false. Not doing so will result in diagrams not being rendered correctly.
				
				You should set this system property in the Eclipse configuration file, which for standard Eclipse \
				distros is called 'eclipse.ini'. You do this by specifying the following line:
				
				-D%s=false
				
				Please fix this or have the %s do this for you.
				""".formatted(DIAGRAM_EDITOR, PROPERTY_NAME, PROPERTY_NAME, DIAGRAM_EDITOR));
		textDescription.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		var lblCurrentValue = new Label(container, SWT.NONE);
		var gdLblCurrentValue = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblCurrentValue.verticalIndent = 20;
		lblCurrentValue.setLayoutData(gdLblCurrentValue);
		lblCurrentValue.setText("Current value:");
		
		textCurrentValue = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		var gdTextCurrentValue = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gdTextCurrentValue.verticalIndent = 20;
		gdTextCurrentValue.widthHint = 75;
		textCurrentValue.setLayoutData(gdTextCurrentValue);
		
		var lblConfiguredIn = new Label(container, SWT.NONE);
		lblConfiguredIn.setText("Configured in:");
		
		textConfigurationFile = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		textConfigurationFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		var btnSelectEclipseIniFile = new Button(container, SWT.NONE);
		btnSelectEclipseIniFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectEclipseIniFile();
			}
		});
		btnSelectEclipseIniFile.setText("Select...");
		
		btnFixInConfigurationFile = new Button(container, SWT.CHECK);
		btnFixInConfigurationFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fixInConfigurrationFile = btnFixInConfigurationFile.getSelection();
			}
		});
		btnFixInConfigurationFile.setSelection(true);
		var gdBtnFixInConfigurationFile = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gdBtnFixInConfigurationFile.verticalIndent = 20;
		btnFixInConfigurationFile.setLayoutData(gdBtnFixInConfigurationFile);
		btnFixInConfigurationFile.setText("Fix in configuration file");
		
		btnRestartWorkbench = new Button(container, SWT.CHECK);
		btnRestartWorkbench.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				restartWorkbench = btnRestartWorkbench.getSelection();
			}
		});
		btnRestartWorkbench.setSelection(true);
		btnRestartWorkbench.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnRestartWorkbench.setText("Restart workbench");

		btnSelectEclipseIniFile.setFocus();
		return area;
	}

	private void selectEclipseIniFile() {
		var fileDialog = new FileDialog(getShell(), SWT.OPEN);
		fileDialog.setText("Select eclipse.ini file");
		fileDialog.setFilterPath(eclipseHomeLocation.getAbsolutePath());
		fileDialog.setFilterExtensions(new String[] { "*.ini", "*.*" });
		var path = fileDialog.open();
		if (path != null) {
			eclipseIniFile = new File(path);
			initialize();
		}
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		initialize();
	}

	private void initialize() {
		if (eclipseHomeLocation == null) {
			setErrorMessage("An error occurred while processing '%s'; see log".formatted(ECLIPSE_HOME_LOCATION));
		} else if (!eclipseIniFile.exists() || !eclipseIniFile.isFile()) {
			setErrorMessage("Configuration file does not exist or is not a file");
		} else if (!eclipseIniFile.getParentFile().equals(eclipseHomeLocation)) {
			setErrorMessage("Configuration file NOT in %s".formatted(eclipseHomeLocation.getAbsolutePath()));
		} else {
			try {
				eclipseIniFileContent = getEclipseIniFileContent();
				var valueInEclipseIniFile = eclipseIniFileContent.stream()
						.filter(line -> line.startsWith("-D%s".formatted(PROPERTY_NAME)))
						.map(line -> line.substring(line.indexOf("=") + 1).trim())
						.findFirst()
						.orElse(null);
				if (!Objects.equals(currentValue, valueInEclipseIniFile)) {
					setErrorMessage("Configuration file does not match current value: %s".formatted(valueInEclipseIniFile));
				} else {
					setErrorMessage(null);
				}
			} catch (SecurityException | IOException e) {
				setErrorMessage("%s: %s".formatted(e.getClass().getSimpleName(), e.getMessage()));
			}
		}
		textCurrentValue.setText(currentValue != null ? currentValue : "(not set)");
		textConfigurationFile.setText(eclipseIniFile != null ? eclipseIniFile.getAbsolutePath() : "");
		btnFixInConfigurationFile.setEnabled(getErrorMessage() == null);
		btnRestartWorkbench.setEnabled(getErrorMessage() == null);
		getButton(IDialogConstants.OK_ID).setEnabled(getErrorMessage() == null);
	}
	
	@Override
	public int open() {
		var returnCode = super.open();
		if (returnCode == Window.OK) {
			var okToRestartWorkbench = fixInConfigurrationFile && fixEclipseIniFile();
			if (restartWorkbench && okToRestartWorkbench) {
				PlatformUI.getWorkbench().restart();
			}
		}
		return returnCode;
	}
	
	private boolean fixEclipseIniFile() {
		try (var out = new PrintWriter(eclipseIniFile, StandardCharsets.UTF_8)) {
			getUpdatedEclipseIniFileContent().stream()
				.forEach(out::println);
			return true;
		} catch (IOException e) {
			logger.error("an exception was thrown while modifying %s".formatted(eclipseIniFile.getAbsolutePath()), e);
			MessageDialog.openError(getShell(), "Error", "An error occurred; see log: %s".formatted(e.getMessage()));
			return false;
		}
	}
	
	private List<String> getUpdatedEclipseIniFileContent() {
		var copyOfEclipseIniFileContent = new ArrayList<>(eclipseIniFileContent);
		var systemPropertyLineIndex = calculateSystemPropertyLineIndex();
		int insertionIdex;
		if (systemPropertyLineIndex < 0) {
			var reverseCopyOfEclipseIniFileContent = new ArrayList<>(eclipseIniFileContent);
			Collections.reverse(reverseCopyOfEclipseIniFileContent);
			var lastSystemPropertyLine = reverseCopyOfEclipseIniFileContent.stream()
					.filter(line -> line.startsWith("-D"))
					.findFirst();
			if (lastSystemPropertyLine.isPresent()) {
				insertionIdex = eclipseIniFileContent.indexOf(lastSystemPropertyLine.orElseThrow()) + 1;
			} else {
				insertionIdex = eclipseIniFileContent.size();
			}
		} else {
			copyOfEclipseIniFileContent.remove(systemPropertyLineIndex);
			insertionIdex = systemPropertyLineIndex;
		}
		copyOfEclipseIniFileContent.add(insertionIdex, "-D%s=false".formatted(PROPERTY_NAME));
		return copyOfEclipseIniFileContent;
	}
	
	private int calculateSystemPropertyLineIndex() {
		var systemPropertyLine = eclipseIniFileContent.stream()
				.filter(line -> line.startsWith("-D%s=".formatted(PROPERTY_NAME)))
				.findFirst();
		return systemPropertyLine.isPresent() ? eclipseIniFileContent.indexOf(systemPropertyLine.orElseThrow()) : -1;
	}

	private List<String> getEclipseIniFileContent() throws IOException, SecurityException {
		try (var lines = Files.lines(eclipseIniFile.toPath())) {
			return lines.toList();
		}
	}
	
}
