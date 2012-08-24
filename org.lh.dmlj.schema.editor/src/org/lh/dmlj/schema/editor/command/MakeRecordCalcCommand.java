package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * A command that will change the record's location mode to CALC and set the 
 * CALC key.  This command can only be used for DIRECT records (at the time of
 * execution) and will definitely run into trouble when executed for a record 
 * that is defined as either CALC or VIA.
 */
public class MakeRecordCalcCommand extends AbstractChangeLocationModeCommand {

	private List<Element> 	 calcKeyElements;
	private DuplicatesOption duplicatesOption;
	
	public MakeRecordCalcCommand(SchemaRecord record,
								 List<Element> calcKeyElements,
								 DuplicatesOption duplicatesOption) {
		super("Set 'Location mode' to 'CALC'", record);
		this.calcKeyElements = new ArrayList<>(calcKeyElements);
		this.duplicatesOption = duplicatesOption;
	}	
	
	@Override
	public void execute() {
		if (record.getLocationMode() != LocationMode.DIRECT) {
			throw new IllegalArgumentException("record not DIRECT");
		}
		createCalcKey(calcKeyElements, null, duplicatesOption, false, -1);
		record.setLocationMode(LocationMode.CALC);
	}
	
	@Override
	public void undo() {
		record.setLocationMode(LocationMode.DIRECT);
		removeCalcKey();		
	}
	

}