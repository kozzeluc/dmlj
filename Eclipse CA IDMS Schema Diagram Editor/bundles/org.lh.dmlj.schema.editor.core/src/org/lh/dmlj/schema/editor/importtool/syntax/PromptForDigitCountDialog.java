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
package org.lh.dmlj.schema.editor.importtool.syntax;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lh.dmlj.schema.editor.importtool.IRecordDataCollector;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class PromptForDigitCountDialog extends Dialog {
	private Button btnNoSuffix;
	private Button btnUse3or4Digits;
	private Button btnUse4Digits;
	private SchemaSyntaxWrapper context;
	private IRecordDataCollector<SchemaSyntaxWrapper> dataCollector;
	private Table elementTable;
	private String knownControlElementName;
	private Label lblUse3or4Digits;
	private Label lblUse4Digits;
	private short recordId;
	private short selectedDigitCount;
	private List<String> syntaxElementNames;
	private Text textSyntax;
	
	private static String formatRecordId(short recordId, int length) {
		var p = new StringBuilder(String.valueOf(recordId));
		while (p.length() < length) {
			p.insert(0, '0');
		}
		return p.toString();
	}
	
	private static String getBaseName(String syntaxName, String suffix, boolean containsBaseNamesFlag) {
		if (suffix == null || suffix.trim().equals("") || containsBaseNamesFlag) {
			return syntaxName;
		} else {
			return syntaxName.substring(0, syntaxName.lastIndexOf(suffix));
		}
	}
	
	private static String getName(String syntaxName, String suffix, boolean containsBaseNamesFlag) {
		var p = new StringBuilder();		
		p.append(syntaxName);
		if (suffix != null && !suffix.trim().equals("") && containsBaseNamesFlag) {
			p.append(suffix);
		}
		return p.toString();
	}		

	public PromptForDigitCountDialog(Shell parentShell, SchemaSyntaxWrapper context,
			IRecordDataCollector<SchemaSyntaxWrapper> dataCollector, String	knownControlElementName) {
		
		super(parentShell);
		this.context = context;
		this.dataCollector = dataCollector;
		this.knownControlElementName = knownControlElementName;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		var recordName = dataCollector.getName(context);
		shell.setText("Specify record-id suffix for record " + recordName);
	}
	
	private boolean containsBaseNamesFlag(String suffix) {
		if (suffix != null && !suffix.trim().isEmpty()) {
			return syntaxElementNames.stream()
					.anyMatch(elementName -> !elementName.endsWith(suffix));
		} else {
			return false;
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		var container = (Composite) super.createDialogArea(parent);
		var gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;
		
		textSyntax = new Text(container, SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		textSyntax.setFont(SWTResourceManager.getFont("Courier New", 8, SWT.NORMAL));
		textSyntax.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		elementTable = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		elementTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
		elementTable.setHeaderVisible(true);
		elementTable.setLinesVisible(true);
		
		var tblclmnElementName = new TableColumn(elementTable, SWT.NONE);
		tblclmnElementName.setWidth(140);
		tblclmnElementName.setText("Element Name");
		
		var tblclmnElementBaseName = new TableColumn(elementTable, SWT.NONE);
		tblclmnElementBaseName.setWidth(140);
		tblclmnElementBaseName.setText("Element Base Name");
		
		var lblPleaseIndicateWhat = new Label(container, SWT.NONE);
		var gdLblPleaseIndicateWhat = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdLblPleaseIndicateWhat.verticalIndent = 10;
		lblPleaseIndicateWhat.setLayoutData(gdLblPleaseIndicateWhat);
		lblPleaseIndicateWhat.setText("Please indicate what suffix the import tool should use for this record:");		
		
		btnNoSuffix = new Button(container, SWT.RADIO);
		btnNoSuffix.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedDigitCount();
			}
		});
		var gdBtnNoSuffix = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gdBtnNoSuffix.verticalIndent = 10;
		btnNoSuffix.setLayoutData(gdBtnNoSuffix);
		btnNoSuffix.setText("no suffix");
		new Label(container, SWT.NONE);
		
		new Label(container, SWT.NONE);
		
		btnUse3or4Digits = new Button(container, SWT.RADIO);
		btnUse3or4Digits.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedDigitCount();
			}
		});
		btnUse3or4Digits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnUse3or4Digits.setText("3 or 4 digits:");				
		
		lblUse3or4Digits = new Label(container, SWT.NONE);
		lblUse3or4Digits.setText("New Label");
		
		new Label(container, SWT.NONE);
		
		btnUse4Digits = new Button(container, SWT.RADIO);
		btnUse4Digits.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedDigitCount();
			}
		});
		btnUse4Digits.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnUse4Digits.setText("4 digits:");				
		
		lblUse4Digits = new Label(container, SWT.NONE);
		lblUse4Digits.setText("New Label");

		initialize();
		
		return container;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(750, 500);
	}
	
	public short getSelectedDigitCount() {
		return selectedDigitCount;
	}

	private void initialize() {
		textSyntax.setText(composeSyntax());
		createInitialElementTableItems();	
		
		recordId = dataCollector.getRecordId(context);
		var suffix3 = "-" + formatRecordId(recordId, 3);
		var suffix4 = "-" + formatRecordId(recordId, 4);
		
		btnNoSuffix.setSelection(true);
		
		lblUse3or4Digits.setText("\"" + suffix3 + "\"");		
		lblUse4Digits.setText("\"" + suffix4 + "\"");		
		
		if (knownControlElementName != null) {
			var found = false;
			var containsBaseNamesFlag = containsBaseNamesFlag(suffix3);
			for (var syntaxElementName : syntaxElementNames) {
				var name = getName(syntaxElementName, suffix3, containsBaseNamesFlag);
				if (knownControlElementName.equals(name)) {
					found = true;
					break;
				}
			}		
			btnUse3or4Digits.setEnabled(found);
			lblUse3or4Digits.setEnabled(found);
	
			found = false;
			containsBaseNamesFlag = containsBaseNamesFlag(suffix4);
			for (var syntaxElementName : syntaxElementNames) {
				var name = getName(syntaxElementName, suffix4, containsBaseNamesFlag);
				if (knownControlElementName.equals(name)) {
					found = true;
					break;
				}
			}
			btnUse4Digits.setEnabled(found);
			lblUse4Digits.setEnabled(found);
		}
		
		if (recordId > 999) {
			btnUse3or4Digits.setEnabled(false);
			lblUse3or4Digits.setEnabled(false);
		}
		
		setSelectedDigitCount();
	}
	
	private String composeSyntax() {
		var syntax = new StringBuilder();
		for (var line : context.getLines()) {
			if (!syntax.isEmpty()) {
				syntax.append("\n");
			}
			syntax.append(line);
		}
		return syntax.toString();
	}
	
	private void createInitialElementTableItems() {
		syntaxElementNames = new ArrayList<>();
		var inElementSyntax = false;
		for (var line : context.getLines()) {
			if (line.trim().equals(".")) {
				inElementSyntax = true;
			} else if (inElementSyntax) {
				var q = line.substring(2).trim();
				if (q.startsWith("0") && q.charAt(2) == ' ') {
					// skip level 88 elements
					var elementName = q.substring(3);
					if (!elementName.equals("FILLER")) {
						// skip FILLER elements
						syntaxElementNames.add(elementName);
						var tableItem = new TableItem(elementTable, SWT.NONE);
						tableItem.setText(0, "");
						tableItem.setText(1, "");
					}
				}
			}
		}
	}
	
	private void setSelectedDigitCount() {		
		selectedDigitCount = -1;
		String suffix = null;
		if (btnUse3or4Digits.getSelection()) {
			selectedDigitCount = 3;
			suffix = "-" + formatRecordId(recordId, 3);
		} else if (btnUse4Digits.getSelection()) {
			selectedDigitCount = 4;
			suffix = "-" + formatRecordId(recordId, 4);
		}
		var containsBaseNamesFlag = containsBaseNamesFlag(suffix);
		for (var i = 0; i < syntaxElementNames.size(); i++ ) {
			var tableItem = elementTable.getItem(i);
			var syntaxName = syntaxElementNames.get(i);
			tableItem.setText(0, getName(syntaxName, suffix, containsBaseNamesFlag));
			tableItem.setText(1, getBaseName(syntaxName, suffix, containsBaseNamesFlag));
		}		
	}	

}
