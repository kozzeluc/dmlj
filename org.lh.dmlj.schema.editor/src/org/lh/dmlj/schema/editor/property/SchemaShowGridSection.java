package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaShowGridSection 
	extends AbstractDiagramDataBooleanAttributeSection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowGrid();
	private static final String 	DESCRIPTION = 
		"Indicates whether the diagram grid is visible or not"; 
	
	public SchemaShowGridSection() {
		super(ATTRIBUTE);		
	}
	
	@Override
	protected String getCheckboxLabel() {
		return "Show grid";
	}
	
	@Override
	protected String getCommandLabelForFalse() {
		return "Hide grid";
	}
	
	@Override
	protected String getCommandLabelForTrue() {
		return "Show grid";
	}
	
	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}	
	
}