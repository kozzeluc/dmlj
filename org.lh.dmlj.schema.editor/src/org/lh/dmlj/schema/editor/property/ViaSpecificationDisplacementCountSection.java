package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class ViaSpecificationDisplacementCountSection 
	extends AbstractViaSpecificationAttributeSection {	
	
	public ViaSpecificationDisplacementCountSection() {
		super(SchemaPackage.eINSTANCE.getViaSpecification_DisplacementPageCount(), 
			  true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies how far away member records cluster from the owner " +
			   "record when the member and owner record occurrences are " +
			   "assigned to the same page range";
	}	
	
	@Override
	protected String getLabel() {
		return "Displacement pages";
	}
	
}