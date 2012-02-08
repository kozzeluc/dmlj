package org.lh.dmlj.schema.editor.property;

import org.lh.dmlj.schema.SchemaPackage;

public class RecordViaSetSection extends AbstractSetAttributeSection {

	public RecordViaSetSection() {
		super(SchemaPackage.eINSTANCE.getSet_Name(), true); // read-only
	}
	
	@Override
	protected String getDescription() {
		return "Specifies that occurrences of the record are to be stored " +
			   "relative to their owner in a specific set";
	}
	
	@Override
	protected String getLabel() {
		return "Set";
	}

}