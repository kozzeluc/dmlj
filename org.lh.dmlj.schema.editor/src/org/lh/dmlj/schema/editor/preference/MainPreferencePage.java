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
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.eclipse.swt.widgets.Text;

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	
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
	
	private Button btnLogDiagnosticMessages;
	private Combo  comboShowGrid;
	private Combo  comboShowRulers;
	private Combo  comboSnapToGeometry;
	private Combo  comboSnapToGrid;
	private Combo  comboSnapToGuides;
	private Label  lbldontCheckThis;
	private Text   textDiagramLabelOrganisation;
	
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
	public MainPreferencePage() {
		super();
		setDescription("General Settings:");
	}
	
	@Override
	protected Control createContents(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);		
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		
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
		
		Group compositeSchemaDiagramProperties = new Group(container, SWT.NONE);
		compositeSchemaDiagramProperties.setText("Default diagram properties for new and imported schemas");
		compositeSchemaDiagramProperties.setLayout(new GridLayout(2, false));
		GridData gd_compositeSchemaDiagramProperties = new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1);
		gd_compositeSchemaDiagramProperties.verticalIndent = 10;
		compositeSchemaDiagramProperties.setLayoutData(gd_compositeSchemaDiagramProperties);
		
		Label lblShowRulers = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblShowRulers.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SHOW_RULERS));
		lblShowRulers.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_RULERS));
		
		comboShowRulers = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboShowRulers = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboShowRulers.widthHint = 25;
		comboShowRulers.setLayoutData(gd_comboShowRulers);
		comboShowRulers.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_RULERS));
		
		Label lblShowGrid = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblShowGrid.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SHOW_GRID));
		lblShowGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_GRID));
		
		comboShowGrid = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboShowGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboShowGrid.widthHint = 25;
		comboShowGrid.setLayoutData(gd_comboShowGrid);
		comboShowGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SHOW_GRID));
		
		Label lblSnapToGuides = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblSnapToGuides.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SNAP_TO_GUIDES));
		lblSnapToGuides.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GUIDES));
		
		comboSnapToGuides = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboSnapToGuides = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboSnapToGuides.widthHint = 25;
		comboSnapToGuides.setLayoutData(gd_comboSnapToGuides);
		comboSnapToGuides.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GUIDES));
		
		Label lblSnapToGrid = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblSnapToGrid.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SNAP_TO_GRID));
		lblSnapToGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GRID));
		
		comboSnapToGrid = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboSnapToGrid = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboSnapToGrid.widthHint = 25;
		comboSnapToGrid.setLayoutData(gd_comboSnapToGrid);
		comboSnapToGrid.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GRID));
		
		Label lblSnapToGeometry = new Label(compositeSchemaDiagramProperties, SWT.NONE);
		lblSnapToGeometry.setText(getDiagramAttributeLabelText(DIAGRAMDATA_SNAP_TO_GEOMETRY));
		lblSnapToGeometry.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GEOMETRY));
		
		comboSnapToGeometry = new Combo(compositeSchemaDiagramProperties, SWT.READ_ONLY);
		GridData gd_comboSnapToGeometry = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_comboSnapToGeometry.widthHint = 25;
		comboSnapToGeometry.setLayoutData(gd_comboSnapToGeometry);
		comboSnapToGeometry.setToolTipText(getDiagramAttributeTooltipText(DIAGRAMDATA_SNAP_TO_GEOMETRY));
		
		Group grpDiagramLabel = new Group(container, SWT.NONE);
		grpDiagramLabel.setLayout(new GridLayout(2, false));
		grpDiagramLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		grpDiagramLabel.setText("Diagram label");
		
		Label lblOrganisation = new Label(grpDiagramLabel, SWT.NONE);
		lblOrganisation.setToolTipText("The name of the organisation to appear on the first line of the diagram label");
		lblOrganisation.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblOrganisation.setText("Organisation:");
		
		textDiagramLabelOrganisation = new Text(grpDiagramLabel, SWT.BORDER);
		textDiagramLabelOrganisation.setToolTipText("The name of the organisation to appear on the first line of the diagram label");
		textDiagramLabelOrganisation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnLogDiagnosticMessages = new Button(container, SWT.CHECK);
		btnLogDiagnosticMessages.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, true, 2, 1));
		btnLogDiagnosticMessages.setText("Log diagnostic messages to the workspace log");
		
		lbldontCheckThis = new Label(container, SWT.NONE);
		GridData gd_lbldontCheckThis = new GridData(SWT.LEFT, SWT.TOP, false, false, 2, 1);
		gd_lbldontCheckThis.horizontalIndent = 17;
		lbldontCheckThis.setLayoutData(gd_lbldontCheckThis);
		lbldontCheckThis.setText("(don't check this option unless asked)");
		
		initializeDiagramAttributeComboValues(comboShowRulers);
		initializeDiagramAttributeComboValues(comboShowGrid);
		initializeDiagramAttributeComboValues(comboSnapToGuides);
		initializeDiagramAttributeComboValues(comboSnapToGrid);
		initializeDiagramAttributeComboValues(comboSnapToGeometry);
		
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
		
		String organisation = store.getDefaultString(PreferenceConstants.ORGANISATION);
		textDiagramLabelOrganisation.setText(organisation);
		
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
		
		boolean logDiagnosticMessages = 
			store.getDefaultBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);
		
		doChecks();		
		
	}	
	
	private void initializeValues() {		
		
		IPreferenceStore store = getPreferenceStore();
		
		Unit unit = Unit.valueOf(store.getString(PreferenceConstants.UNITS));		
		btnCentimeters.setSelection(unit == Unit.CENTIMETERS);
		btnInches.setSelection(unit == Unit.INCHES);
		btnPixels.setSelection(unit == Unit.PIXELS);
		
		String organisation = store.getString(PreferenceConstants.ORGANISATION);
		textDiagramLabelOrganisation.setText(organisation);
		
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
		
		boolean logDiagnosticMessages = 
			store.getBoolean(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES);
		btnLogDiagnosticMessages.setSelection(logDiagnosticMessages);
		
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
		
		store.setValue(PreferenceConstants.ORGANISATION, 
					   textDiagramLabelOrganisation.getText().trim());
		
		store.setValue(PreferenceConstants.SHOW_RULERS,
					   getDiagramAttributeComboValue(comboShowRulers));
		store.setValue(PreferenceConstants.SHOW_GRID, getDiagramAttributeComboValue(comboShowGrid));
		store.setValue(PreferenceConstants.SNAP_TO_GUIDES,
					   getDiagramAttributeComboValue(comboSnapToGuides));
		store.setValue(PreferenceConstants.SNAP_TO_GRID,
					   getDiagramAttributeComboValue(comboSnapToGrid));
		store.setValue(PreferenceConstants.SNAP_TO_GEOMETRY,
					   getDiagramAttributeComboValue(comboSnapToGeometry));		
		
		store.setValue(PreferenceConstants.LOG_DIAGNISTIC_MESSAGES, 
					   Boolean.valueOf(btnLogDiagnosticMessages.getSelection()));
		
		return true;
	}	
}