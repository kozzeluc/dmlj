package org.lh.dmlj.schema.editor.command;

import org.lh.dmlj.schema.SchemaRecord;


/**
 * A command that will change the record's VIA specification.  This command can 
 * only be used for VIA records and will definitely run into trouble when 
 * executed for a record that is defined as either CALC or DIRECT.
 */
public class ChangeViaSpecificationCommand 
	extends AbstractChangeLocationModeCommand {

	private String newViaSetName; 
	private String newSymbolicDisplacementName; 
	private Short  newDisplacementPageCount;	
	
	private String oldViaSetName; 
	private String oldSymbolicDisplacementName; 
	private Short  oldDisplacementPageCount;
	private int    oldViaSpecificationIndex;
	
	public ChangeViaSpecificationCommand(SchemaRecord record, String viaSetName, 
										 String symbolicDisplacementName, 
										 Short displacementPageCount) {
		
		super("Change VIA specification", record);
		newViaSetName = viaSetName;
		newSymbolicDisplacementName = symbolicDisplacementName;
		newDisplacementPageCount = displacementPageCount;
	}
	
	@Override
	public void execute() {
		
		// save the old data
		oldViaSetName = record.getViaSpecification().getSet().getName();
		oldSymbolicDisplacementName = 
			record.getViaSpecification().getSymbolicDisplacementName();
		oldDisplacementPageCount = 
			record.getViaSpecification().getDisplacementPageCount();
		oldViaSpecificationIndex = record.getViaSpecification()
				  						 .getSet()
				  						 .getViaMembers()
				  						 .indexOf(record.getViaSpecification());
		
		// make the change
		redo();
		
	}
	
	public void redo() {
		
		removeViaSpecification();
		
		createViaSpecification(newViaSetName, newSymbolicDisplacementName, 
							   newDisplacementPageCount, -1);
		
	};
	
	@Override
	public void undo() {
	
		removeViaSpecification();
		
		createViaSpecification(oldViaSetName, oldSymbolicDisplacementName, 
							   oldDisplacementPageCount, 
							   oldViaSpecificationIndex);
		
	}

}