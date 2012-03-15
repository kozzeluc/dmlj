package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaSnapToGuidesSection 
	extends AbstractDiagramDataBooleanAttributeSection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides();
	private static final String 	DESCRIPTION = 
		"Indicates whether figures should snap to guides or not"; 
	
	public SchemaSnapToGuidesSection() {
		super(ATTRIBUTE);		
	}
	
	@Override
	protected String getCheckboxLabel() {
		return "Snap to guides";
	}
	
	@Override
	protected String getCommandLabelForFalse() {
		return "Disable snap to guides";
	}
	
	@Override
	protected String getCommandLabelForTrue() {
		return "Enable snap to guides";
	}
	
	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}	
	
	@Override
	public boolean isCheckboxEnabled() {
		return modelObject.isShowRulers();
	}
	
}