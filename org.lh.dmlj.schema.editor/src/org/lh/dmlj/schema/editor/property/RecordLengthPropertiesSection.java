package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class RecordLengthPropertiesSection 
	extends AbstractRecordPropertiesSection {

	public RecordLengthPropertiesSection() {
		super();
	}	
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength()) {
			return "The length, in bytes, of the prefix portion of the " +
				   "record (including the pointers for the CALC set in the " +
				   "case of a CALC record)";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_DataLength()) {
			return "The length, in bytes, of the data portion of the record";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength()) {
			return "The length of the initial portion of the record that " +
				   "includes all bytes up to and including the last CALC, " +
				   "index, or sort control element";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength()) {
			return "Specifies the minimum portion of a variable-length " +
				   "record that can be stored on a database page";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength()) {
			return "Specifies the minimum length of subsequent segments " +
					   "(fragments) of a variable-length record";
		}
		return super.getDescription(attribute);
	}
	
	@Override
	protected List<EAttribute> getAttributes() {		
		List<EAttribute> attributes = new ArrayList<>();		
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_DataLength());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength());
		if (target.getMinimumRootLength() != null) {
			attributes.add(SchemaPackage.eINSTANCE
									  .getSchemaRecord_MinimumRootLength());
		}
		if (target.getMinimumFragmentLength() != null) {
			attributes.add(SchemaPackage.eINSTANCE
									  .getSchemaRecord_MinimumFragmentLength());
		}		
		return attributes;
	}
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_PrefixLength()) {
			return "Prefix";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_DataLength()) {
			return "Data";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_ControlLength()) {
			return "Control length";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumRootLength()) {
			return "Min. root length";
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_MinimumFragmentLength()) {
			return "Min. fragment length";
		}
		return super.getLabel(attribute);
	}	

}