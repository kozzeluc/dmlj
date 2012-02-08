package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class ViaSpecificationSymbolicDisplacementNameSection 
	extends AbstractViaSpecificationAttributeSection {	
	
	public ViaSpecificationSymbolicDisplacementNameSection() {
		super(SchemaPackage.eINSTANCE.getViaSpecification_SymbolicDisplacementName(), 
			  true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Names a symbol used to represent the displacement";
	}	
	
	@Override
	protected String getLabel() {
		return "Symbolic displ. name";
	}
	
}