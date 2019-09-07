/**
 * Copyright (C) 2019  Luc Hermans
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;

public class DiagramPreferencesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
	private static final String FALSE = "false";
	private static final String TRUE = "true";
	
	private static final EAttribute[] DIAGRAMDATA_ATTRIBUTES = 
		new EAttribute[] {SchemaPackage.eINSTANCE.getDiagramData_ShowRulers(),
						  SchemaPackage.eINSTANCE.getDiagramData_ShowGrid(),
						  SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides(),
						  SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid(),
						  SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()};
	private static final int DIAGRAMDATA_SHOW_RULERS = 0;
	private static final int DIAGRAMDATA_SHOW_GRID = 1;
	private static final int DIAGRAMDATA_SNAP_TO_GUIDES = 2;
	private static final int DIAGRAMDATA_SNAP_TO_GRID = 3;
	private static final int DIAGRAMDATA_SNAP_TO_GEOMETRY = 4;

	private Button btnCentimeters;
	private Button btnInches;
	private Button btnPixels;
	private Combo  comboShowGrid;
	private Combo  comboShowRulers;
	private Combo  comboSnapToGeometry;
	private Combo  comboSnapToGrid;
	private Combo  comboSnapToGuides;
	private Text   textDiagramLabelOrganisation;
	private Button btnDiagramLabelShowLastModified;
	private Text textDiagramLabelLastModifiedPattern;
	private Group grpFontSizeAdjustment;
	private Button btnFontSize100;
	private Button btnFontSize125;
	private Button btnFontSize150;
	private Button btnFontSize175;
	private Label lblNewLabel;
	private Label lblNewLabel_1;
	
	private static void initializeDiagramAttributeComboValues(Combo combo) {
		combo.add(FALSE);
		combo.add(TRUE);
		combo.select(0);
	}
	
	private static String getDiagramAttributeLabelText(int i) {
		EAttribute attribute = DIAGRAMDATA_ATTRIBUTES[i];
		String key = "label." + attribute.getContainerClass().getName() + "." + attribute.getName();
		String label = PluginPropertiesCache.get(Plugin.getDefault(), key);		
		if (label != null) {
			return label + ":";
		} else {
			return attribute.getName() + ":";
		}
	}
	
	private static String getDiagramAttributeTooltipText(int i) {
		EAttribute attribute = DIAGRAMDATA_ATTRIBUTES[i];
		String key = "description." + attribute.getContainerClass().getName() + "." + attribute.getName();
		String label = PluginPropertiesCache.get(Plugin.getDefault(), key);		
		return label;
	}	
	
	private static void selectDiagramAttributeComboValue(Combo combo, boolean b) {
		combo.select(b ? 1 : 0);		
	}
	
	private static boolean getDiagramAttributeComboValue(Combo combo) {
		return combo.getSelectionIndex() == 1;		
	}	

	/**
	 * @wbp.parser.constructor
	 */
	public DiagramPreferencesPreferencePage() {
		super();
		setDescription("Diagram Settings:");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);		
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		new Label(container, SWT.NONE);
		
		Group compositeUnits = new Group(container, SWT.NONE);
		compositeUnits.setText("Units");
		compositeUnits.setLayout(new GridLayout(3, false));
		compositeUnits.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnCentimeters = new Button(compositeUnits, SWT.RADIO);
		btnCentimeters.setBounds(0, 0, 90, 16);
		btnCentimeters.setText("Centimeters");
		
		btnInches = new Button(compositeUnits, SWT.RADIO);
		btnInches.setBounds(0, 0, 90, 16);
		btnInches.setText("Inches");
		
		btnPixels = new Button(compositeUnits, SWT.RADIO);
		btnPixels.setText("Pixels");
		new Label(container, SWT.NONE);
		
		Group compositeSchemaDiagramProperties = new Group(container, SWT.NONE);
		compositeSchemaDiagramProperties.setText("Default diagram properties for new and imported schemas");
		compositeSchemaDiagramProperties.setLayout(new GridLayout(4, false));
		compositeSchemaDiagramProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Label lblShowRulers = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblShowRulers.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SHOW_RULERS));
		lblShowRulers.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_RULERS));
		
		comboShowRulers = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboShowRulers = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboShowRulers.widthHint = 75;
		comboShowRulers.setLayoutData(gd_comboShowRulers);
		comboShowRulers.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_RULERS));
		
		Label lblSnapToGuides = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		GridData gd_lblSnapToGuides = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSnapToGuides.horizontalIndent = 10;
		lblSnapToGuides.setLayoutData(gd_lblSnapToGuides);
		lblSnapToGuides.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SNAP_TO_GUIDES));
		lblSnapToGuides.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GUIDES));
		
		comboSnapToGuides = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboSnapToGuides = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboSnapToGuides.widthHint = 75;
		comboSnapToGuides.setLayoutData(gd_comboSnapToGuides);
		comboSnapToGuides.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GUIDES));
		initializeDiagramAttributeComboValues(comboSnapToGuides);
		
		Label lblShowGrid = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblShowGrid.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SHOW_GRID));
		lblShowGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_GRID));
		
		comboShowGrid = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboShowGrid = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboShowGrid.widthHint = 25;
		comboShowGrid.setLayoutData(gd_comboShowGrid);
		comboShowGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_GRID));
		
		Label lblSnapToGrid = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		GridData gd_lblSnapToGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSnapToGrid.horizontalIndent = 10;
		lblSnapToGrid.setLayoutData(gd_lblSnapToGrid);
		lblSnapToGrid.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SNAP_TO_GRID));
		lblSnapToGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GRID));
		
		comboSnapToGrid = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboSnapToGrid = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboSnapToGrid.widthHint = 25;
		comboSnapToGrid.setLayoutData(gd_comboSnapToGrid);
		comboSnapToGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GRID));
		initializeDiagramAttributeComboValues(comboSnapToGrid);
		new Label(compositeSchemaDiagramProperties, SWT.NONE);
		new Label(compositeSchemaDiagramProperties, SWT.NONE);
		
		Label lblSnapToGeometry = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		GridData gd_lblSnapToGeometry = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblSnapToGeometry.horizontalIndent = 10;
		lblSnapToGeometry.setLayoutData(gd_lblSnapToGeometry);
		lblSnapToGeometry.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SNAP_TO_GEOMETRY));
		lblSnapToGeometry.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GEOMETRY));
		
		comboSnapToGeometry = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboSnapToGeometry = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_comboSnapToGeometry.widthHint = 25;
		comboSnapToGeometry.setLayoutData(gd_comboSnapToGeometry);
		comboSnapToGeometry.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GEOMETRY));
		initializeDiagramAttributeComboValues(comboSnapToGeometry);
		new Label(container, SWT.NONE);
		
		Group grpDiagramLabel = new Group(container, SWT.NONE);
		grpDiagramLabel.setLayout(new GridLayout(3, false));
		grpDiagramLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpDiagramLabel.setText("Diagram label");
		
		Label lblOrganisation = new Label(grpDiagramLabel, SWT.NONE);
		lblOrganisation.setToolTipText("The name of the organisation to appear on the first line of the diagram label");
		lblOrganisation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrganisation.setText("Organisation:");
		
		textDiagramLabelOrganisation = new Text(grpDiagramLabel, SWT.BORDER);
		textDiagramLabelOrganisation.setToolTipText("The name of the organisation to appear on the first line of the diagram label");
		textDiagramLabelOrganisation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		btnDiagramLabelShowLastModified = new Button(grpDiagramLabel, SWT.CHECK);
		btnDiagramLabelShowLastModified.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		btnDiagramLabelShowLastModified.setText("Show Last Modified; format:");
		
		textDiagramLabelLastModifiedPattern = new Text(grpDiagramLabel, SWT.BORDER);
		textDiagramLabelLastModifiedPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		
		grpFontSizeAdjustment = new Group(container, SWT.NONE);
		grpFontSizeAdjustment.setLayout(new GridLayout(4, false));
		grpFontSizeAdjustment.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 2, 1));
		grpFontSizeAdjustment.setText("Font Size Adjustment");
		
		lblNewLabel = new Label(grpFontSizeAdjustment, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 4, 1));
		lblNewLabel.setText("Select the text size configured in your OS:");
		
		btnFontSize100 = new Button(grpFontSizeAdjustment, SWT.RADIO);
		btnFontSize100.setText("100%");
		
		btnFontSize125 = new Button(grpFontSizeAdjustment, SWT.RADIO);
		btnFontSize125.setText("125%");
		
		btnFontSize150 = new Button(grpFontSizeAdjustment, SWT.RADIO);
		btnFontSize150.setText("150%");
		
		btnFontSize175 = new Button(grpFontSizeAdjustment, SWT.RADIO);
		btnFontSize175.setText("175%");
		
		lblNewLabel_1 = new Label(grpFontSizeAdjustment, SWT.WRAP);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 4, 1);
		gd_lblNewLabel_1.widthHint = 400;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		lblNewLabel_1.setText("This setting affects the font size used in your diagrams; changing it requires a workbench restart.");
		
		initializeDiagramAttributeComboValues(comboShowRulers);
		initializeDiagramAttributeComboValues(comboShowGrid);
		
		initializeValues();
		
		return container;
	}

	private void doChecks() {
		// no checks to perform so far
	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {		
		return Plugin.getDefault().getPreferenceStore();
	}	

	@Override
	public void init(IWorkbench workbench) {		
	}
	
	private void initializeDefaults() {
		
		IPreferenceStore store = getPreferenceStore();
		
		Unit unit = Unit.valueOf(store.getDefaultString(PreferenceConstants.UNITS));		
		btnCentimeters.setSelection(unit == Unit.CENTIMETERS);
		btnInches.setSelection(unit == Unit.INCHES);
		btnPixels.setSelection(unit == Unit.PIXELS);
		
		String organisation = store.getDefaultString(PreferenceConstants.DIAGRAMLABEL_ORGANISATION);
		textDiagramLabelOrganisation.setText(organisation);
		boolean showLastModified = 
			store.getDefaultBoolean(PreferenceConstants.DIAGRAMLABEL_SHOW_LAST_MODIFIED);
		btnDiagramLabelShowLastModified.setSelection(showLastModified);
		String pattern = 
			store.getDefaultString(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN);
		textDiagramLabelLastModifiedPattern.setText(pattern);
		
		boolean showRulers = store.getDefaultBoolean(PreferenceConstants.SHOW_RULERS);
		selectDiagramAttributeComboValue(comboShowRulers, showRulers);
		boolean showGrid = store.getDefaultBoolean(PreferenceConstants.SHOW_GRID);
		selectDiagramAttributeComboValue(comboShowGrid, showGrid);
		boolean snapToGuides = store.getDefaultBoolean(PreferenceConstants.SNAP_TO_GUIDES);
		selectDiagramAttributeComboValue(comboSnapToGuides, snapToGuides);
		boolean snapToGrid = store.getDefaultBoolean(PreferenceConstants.SNAP_TO_GRID);
		selectDiagramAttributeComboValue(comboSnapToGrid, snapToGrid);
		boolean snapToGeometry = store.getDefaultBoolean(PreferenceConstants.SNAP_TO_GEOMETRY);		
		selectDiagramAttributeComboValue(comboSnapToGeometry, snapToGeometry);
		
		int operatingSystemTextSize = store.getDefaultInt(PreferenceConstants.OPERATING_SYSTEM_TEXT_SIZE);
		btnFontSize100.setSelection(operatingSystemTextSize == 100);
		btnFontSize125.setSelection(operatingSystemTextSize == 125);
		btnFontSize150.setSelection(operatingSystemTextSize == 150);
		btnFontSize175.setSelection(operatingSystemTextSize == 175);
		
		doChecks();		
		
	}	
	
	private void initializeValues() {		
		
		IPreferenceStore store = getPreferenceStore();
		
		Unit unit = Unit.valueOf(store.getString(PreferenceConstants.UNITS));		
		btnCentimeters.setSelection(unit == Unit.CENTIMETERS);
		btnInches.setSelection(unit == Unit.INCHES);
		btnPixels.setSelection(unit == Unit.PIXELS);
		
		String organisation = store.getString(PreferenceConstants.DIAGRAMLABEL_ORGANISATION);
		textDiagramLabelOrganisation.setText(organisation);
		boolean showLastModified = 
			store.getBoolean(PreferenceConstants.DIAGRAMLABEL_SHOW_LAST_MODIFIED);
		btnDiagramLabelShowLastModified.setSelection(showLastModified);
		String pattern = 
			store.getString(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN);
		textDiagramLabelLastModifiedPattern.setText(pattern);
		
		boolean showRulers = store.getBoolean(PreferenceConstants.SHOW_RULERS);
		selectDiagramAttributeComboValue(comboShowRulers, showRulers);
		boolean showGrid = store.getBoolean(PreferenceConstants.SHOW_GRID);
		selectDiagramAttributeComboValue(comboShowGrid, showGrid);
		boolean snapToGuides = store.getBoolean(PreferenceConstants.SNAP_TO_GUIDES);
		selectDiagramAttributeComboValue(comboSnapToGuides, snapToGuides);
		boolean snapToGrid = store.getBoolean(PreferenceConstants.SNAP_TO_GRID);
		selectDiagramAttributeComboValue(comboSnapToGrid, snapToGrid);
		boolean snapToGeometry = store.getBoolean(PreferenceConstants.SNAP_TO_GEOMETRY);		
		selectDiagramAttributeComboValue(comboSnapToGeometry, snapToGeometry);		
		
		int operatingSystemTextSize = store.getInt(PreferenceConstants.OPERATING_SYSTEM_TEXT_SIZE);
		btnFontSize100.setSelection(operatingSystemTextSize == 100);
		btnFontSize125.setSelection(operatingSystemTextSize == 125);
		btnFontSize150.setSelection(operatingSystemTextSize == 150);
		btnFontSize175.setSelection(operatingSystemTextSize == 175);
		
		doChecks();		
	}
	
	@Override
	protected void performApply() {
		storeValues();
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

	private boolean storeValues() {
		
		setErrorMessage(null);
		
		IPreferenceStore store = getPreferenceStore();
		
		Unit unit;
		if (btnCentimeters.getSelection()) {
			unit = Unit.CENTIMETERS;
		} else if (btnInches.getSelection()) {
			unit = Unit.INCHES;
		} else {
			unit = Unit.PIXELS;		
		}
		store.setValue(PreferenceConstants.UNITS, unit.toString());
		
		store.setValue(PreferenceConstants.DIAGRAMLABEL_ORGANISATION, 
					   textDiagramLabelOrganisation.getText().trim());
		store.setValue(PreferenceConstants.DIAGRAMLABEL_SHOW_LAST_MODIFIED, 
				   	   btnDiagramLabelShowLastModified.getSelection());
		store.setValue(PreferenceConstants.DIAGRAMLABEL_LAST_MODIFIED_DATE_FORMAT_PATTERN, 
				   	   textDiagramLabelLastModifiedPattern.getText().trim());
		
		store.setValue(PreferenceConstants.SHOW_RULERS,
					   getDiagramAttributeComboValue(comboShowRulers));
		store.setValue(PreferenceConstants.SHOW_GRID, getDiagramAttributeComboValue(comboShowGrid));
		store.setValue(PreferenceConstants.SNAP_TO_GUIDES,
					   getDiagramAttributeComboValue(comboSnapToGuides));
		store.setValue(PreferenceConstants.SNAP_TO_GRID,
					   getDiagramAttributeComboValue(comboSnapToGrid));
		store.setValue(PreferenceConstants.SNAP_TO_GEOMETRY,
					   getDiagramAttributeComboValue(comboSnapToGeometry));
		
		int operatingSystemTextSize;
		if (btnFontSize125.getSelection()) {
			operatingSystemTextSize = 125;
		} else if (btnFontSize150.getSelection()) {
			operatingSystemTextSize = 150;
		} else if (btnFontSize175.getSelection()) {
			operatingSystemTextSize = 175;
		} else {
			operatingSystemTextSize = 100;
		}
		store.setValue(PreferenceConstants.OPERATING_SYSTEM_TEXT_SIZE, operatingSystemTextSize);
		
		return true;
	}	
}
