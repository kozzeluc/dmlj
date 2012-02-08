package org.lh.dmlj.schema.editor.property;


public class SetSystemOwnerNameSection extends AbstractConstantValueSection {	
	
	public SetSystemOwnerNameSection() {
		super("Name:", "SYSTEM");
	}
	
	@Override
	protected String getDescription() {
		return "Specifies that the indexed set is owned by an internal owner " +
			   "record (SR7 system record)";
	}	
	
}