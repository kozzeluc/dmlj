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
package org.lh.dmlj.schema.editor.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.editor.Plugin;

public class PrintPreferencesPreferencePage extends PreferencePage implements IPropertyChangeListener, IWorkbenchPreferencePage {
	private Composite container;
	private Label lblMargins;
	private Spinner spinnerBottomMargin;
	private Spinner spinnerLeftMargin;
	private Spinner spinnerRightMargin;
	private Spinner spinnerTopMargin;
	
	static int toMargin(int pels, Unit unit) {
		if (unit == Unit.PIXELS) {
			// The result is in pixels
			return pels;
		}
		var tenthsOfAnInch = pels / 7.2d;
		if (unit == Unit.INCHES) {
			// The result is in tenths of an inch
			return (int) Math.round(tenthsOfAnInch);
		} else {
			// The result is in millimeters
			return (int) Math.round(tenthsOfAnInch * 2.54d);			
		}
	}

	static int toPels(int margin, Unit unit) {
		if (unit == Unit.PIXELS) {
			// the margin is in pixels
			return margin;
		}
		if (unit == Unit.INCHES) {
			// the margin is in tenths of an inch
			return (int) Math.round(margin * 7.2d);
		} else {
			// the margin is in millimeters
			var tenthsOfAnInch = margin / 2.54d;
			return (int) Math.round(tenthsOfAnInch * 7.2d);
		}
	}
	
	public PrintPreferencesPreferencePage() {
		setDescription("Print settings:");
	}	

	@Override
	protected Control createContents(Composite parent) {
		container = new Composite(parent, SWT.NONE);		
		var layout = new GridLayout(3, false);
		container.setLayout(layout);
		
		lblMargins = new Label(container, SWT.NONE);
		lblMargins.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblMargins.setText("Margins (centimeters) -");
		
		var lblTop = new Label(container, SWT.NONE);
		lblTop.setText("Top:");
		
		spinnerTopMargin = new Spinner(container, SWT.BORDER);
		spinnerTopMargin.setMaximum(50);
		spinnerTopMargin.setDigits(1);
		var gdSpinnerTopMargin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSpinnerTopMargin.widthHint = 30;
		spinnerTopMargin.setLayoutData(gdSpinnerTopMargin);
		new Label(container, SWT.NONE);
		
		var lblBottom = new Label(container, SWT.NONE);
		lblBottom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBottom.setText("Bottom:");
		
		spinnerBottomMargin = new Spinner(container, SWT.BORDER);
		spinnerBottomMargin.setMaximum(50);
		spinnerBottomMargin.setDigits(1);
		var gdSpinnerBottomMargin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSpinnerBottomMargin.widthHint = 30;
		spinnerBottomMargin.setLayoutData(gdSpinnerBottomMargin);
		new Label(container, SWT.NONE);
		
		var lblLeft = new Label(container, SWT.NONE);
		lblLeft.setText("Left:");
		
		spinnerLeftMargin = new Spinner(container, SWT.BORDER);
		spinnerLeftMargin.setMaximum(50);
		spinnerLeftMargin.setDigits(1);
		var gdSpinnerLeftMargin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSpinnerLeftMargin.widthHint = 30;
		spinnerLeftMargin.setLayoutData(gdSpinnerLeftMargin);
		new Label(container, SWT.NONE);
		
		var lblRight = new Label(container, SWT.NONE);
		lblRight.setText("Right:");
		
		spinnerRightMargin = new Spinner(container, SWT.BORDER);
		spinnerRightMargin.setMaximum(50);
		spinnerRightMargin.setDigits(1);
		var gdSpinnerRightMargin = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gdSpinnerRightMargin.widthHint = 30;
		spinnerRightMargin.setLayoutData(gdSpinnerRightMargin);
		
		initializeValues();
		
		return container;
	}
	
	@Override
	public void dispose() {
		getPreferenceStore().removePropertyChangeListener(this);
		super.dispose();
	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {		
		var store = Plugin.getDefault().getPreferenceStore();
		store.addPropertyChangeListener(this);
		return store;
	}

	@Override
	public void init(IWorkbench workbench) {
		// nothing to do here
	}
	
	private void initializeDefaults() {
		var store = getPreferenceStore();
				
		var unit = Unit.valueOf(store.getDefaultString(PreferenceConstants.UNITS));
		// we do not need to change lblMargins since the unit is set at another preference page
		
		// margins are stored in pels (logical pixels; 72 pels == 1 inch)...
		var topMargin = store.getDefaultInt(PreferenceConstants.TOP_MARGIN);
		var bottomMargin = store.getDefaultInt(PreferenceConstants.BOTTOM_MARGIN);
		var leftMargin = store.getDefaultInt(PreferenceConstants.LEFT_MARGIN);
		var rightMargin = store.getDefaultInt(PreferenceConstants.RIGHT_MARGIN);
		
		spinnerTopMargin.setSelection(toMargin(topMargin, unit));
		spinnerBottomMargin.setSelection(toMargin(bottomMargin, unit));
		spinnerLeftMargin.setSelection(toMargin(leftMargin, unit));				
		spinnerRightMargin.setSelection(toMargin(rightMargin, unit));	
	}	
	
	private void initializeValues() {
		var store = getPreferenceStore();
		
		var unit = Unit.valueOf(store.getString(PreferenceConstants.UNITS));
		String p;
		if (unit == Unit.CENTIMETERS) {
			p = "centimeters";
		} else if (unit == Unit.INCHES) {
			p = "inches";
		} else {
			p = "pixels";
		}
		lblMargins.setText("Margins (" + p + ") -");
		
		// margins are stored in pels (logical pixels; 72 pels == 1 inch)...
		var topMargin = store.getInt(PreferenceConstants.TOP_MARGIN);
		var bottomMargin = store.getInt(PreferenceConstants.BOTTOM_MARGIN);
		var leftMargin = store.getInt(PreferenceConstants.LEFT_MARGIN);
		var rightMargin = store.getInt(PreferenceConstants.RIGHT_MARGIN);
		
		if (unit == Unit.PIXELS) {
			spinnerTopMargin.setDigits(0);
			spinnerBottomMargin.setDigits(0);
			spinnerLeftMargin.setDigits(0);
			spinnerRightMargin.setDigits(0);
			
			spinnerTopMargin.setMaximum(72);
			spinnerBottomMargin.setMaximum(72);
			spinnerLeftMargin.setMaximum(72);
			spinnerRightMargin.setMaximum(72);
		} else if (unit == Unit.INCHES) {
			spinnerTopMargin.setDigits(1);
			spinnerBottomMargin.setDigits(1);
			spinnerLeftMargin.setDigits(1);
			spinnerRightMargin.setDigits(1);
			
			spinnerTopMargin.setMaximum(10);
			spinnerBottomMargin.setMaximum(10);
			spinnerLeftMargin.setMaximum(10);
			spinnerRightMargin.setMaximum(10);
		} else {
			spinnerTopMargin.setDigits(1);
			spinnerBottomMargin.setDigits(1);
			spinnerLeftMargin.setDigits(1);
			spinnerRightMargin.setDigits(1);
			
			spinnerTopMargin.setMaximum(25);
			spinnerBottomMargin.setMaximum(25);
			spinnerLeftMargin.setMaximum(25);
			spinnerRightMargin.setMaximum(25);
		}
		
		spinnerTopMargin.setSelection(toMargin(topMargin, unit));
		spinnerBottomMargin.setSelection(toMargin(bottomMargin, unit));
		spinnerLeftMargin.setSelection(toMargin(leftMargin, unit));				
		spinnerRightMargin.setSelection(toMargin(rightMargin, unit));
	}
	
	@Override
	protected void performApply() {
		storeValues();
		initializeValues();
	}
	
	@Override
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
	}
	
	@Override
	public boolean performOk() {
		return storeValues();		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getProperty().equals(PreferenceConstants.UNITS)) {
			initializeValues();
			container.pack();
		}
	}
	
	private boolean storeValues() {
		setErrorMessage(null);
		
		Unit unit;
		if (lblMargins.getText().indexOf("centimeters") > -1) {
			unit = Unit.CENTIMETERS;
		} else if (lblMargins.getText().indexOf("inches") > -1) {
			unit = Unit.INCHES;
		} else {
			unit = Unit.PIXELS;
		}
		
		var store = getPreferenceStore();
		store.setValue(PreferenceConstants.TOP_MARGIN, toPels(spinnerTopMargin.getSelection(), unit));
		store.setValue(PreferenceConstants.BOTTOM_MARGIN, toPels(spinnerBottomMargin.getSelection(), unit));
		store.setValue(PreferenceConstants.LEFT_MARGIN, toPels(spinnerLeftMargin.getSelection(), unit));
		store.setValue(PreferenceConstants.RIGHT_MARGIN, toPels(spinnerRightMargin.getSelection(), unit));
		return true;
	}

}
