package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaSnapToGridSection 
	extends AbstractDiagramDataBooleanAttributeSection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid();
	private static final String 	DESCRIPTION = 
		"Indicates whether figures should snap to the grid or not"; 
	
	public SchemaSnapToGridSection() {
		super(ATTRIBUTE);		
	}
	
	@Override
	protected String getCheckboxLabel() {
		return "Snap to grid";
	}
	
	@Override
	protected String getCommandLabelForFalse() {
		return "Disable snap to grid";
	}
	
	@Override
	protected String getCommandLabelForTrue() {
		return "Enable snap to grid";
	}
	
	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}	
	
	@Override
	public boolean isCheckboxEnabled() {
		return modelObject.isShowGrid();
	}
	
}