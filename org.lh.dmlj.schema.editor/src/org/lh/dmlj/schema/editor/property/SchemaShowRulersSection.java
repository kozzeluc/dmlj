package org.lh.dmlj.schema.editor.property;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaShowRulersSection 
	extends AbstractDiagramDataBooleanAttributeSection {	

	private static final EAttribute ATTRIBUTE = 
		SchemaPackage.eINSTANCE.getDiagramData_ShowRulers();
	private static final String 	DESCRIPTION = 
		"Indicates whether the diagram rulers and guides are visible or not"; 
	
	public SchemaShowRulersSection() {
		super(ATTRIBUTE);		
	}
	
	@Override
	protected String getCheckboxLabel() {
		return "Show rulers and guides";
	}
	
	@Override
	protected String getCommandLabelForFalse() {
		return "Hide rulers and guides";
	}
	
	@Override
	protected String getCommandLabelForTrue() {
		return "Show rulers and guides";
	}
	
	@Override
	protected String getDescription() {
		return DESCRIPTION;
	}	
	
}