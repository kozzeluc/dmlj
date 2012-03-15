package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaSnapToGeometrySection 
	extends AbstractDiagramDataBooleanAttributeSection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry();
	private static final String 	DESCRIPTION = 
		"Indicates whether figures should snap to geometry"; 
	
	public SchemaSnapToGeometrySection() {
		super(ATTRIBUTE);		
	}
	
	@Override
	protected String getCheckboxLabel() {
		return "Snap to geometry";
	}
	
	@Override
	protected String getCommandLabelForFalse() {
		return "Disable snap to geometry";
	}
	
	@Override
	protected String getCommandLabelForTrue() {
		return "Enable snap to geometry";
	}
	
	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}	
	
}