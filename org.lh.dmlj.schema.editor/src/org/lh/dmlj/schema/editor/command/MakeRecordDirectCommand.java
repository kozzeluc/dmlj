package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * A command that will change the record's location mode to DIRECT.  This 
 * command can only be used for CALC and VIA records and will definitely run 
 * into trouble when executed for a record that is already defined as DIRECT.
 */
public class MakeRecordDirectCommand extends AbstractChangeLocationModeCommand {
	
	private LocationMode     oldLocationMode;
	
	private int				 oldCalcKeyIndex;
	private List<Element>	 oldCalcKeyElements = new ArrayList<>();
	private int[] 			 oldElementIndexes;
	private DuplicatesOption oldDuplicatesOption;
	private boolean			 oldNaturalSequence;
	
	private String			 oldViaSetName;
	private String			 oldSymbolicDisplacementName;
	private Short			 oldDisplacementPageCount;
	private int				 oldViaSpecificationIndex;
	
	public MakeRecordDirectCommand(SchemaRecord record) {
		super("Set 'Location mode' to 'DIRECT'", record);		
	}
	
	@Override
	public void execute() {
		
		// save the old data
		oldLocationMode = record.getLocationMode();		
		if (oldLocationMode == LocationMode.CALC) {
			// the record is CALC, save the old CALC key information
			oldCalcKeyIndex = record.getKeys().indexOf(record.getCalcKey());
			oldElementIndexes = 
				new int[record.getCalcKey().getElements().size()];
			int i = 0;
			for (KeyElement keyElement : record.getCalcKey().getElements()) {
				Element element = keyElement.getElement();
				oldCalcKeyElements.add(element);
				oldElementIndexes[i++] = element.getKeyElements()
												.indexOf(keyElement);					
			}
			oldDuplicatesOption = record.getCalcKey().getDuplicatesOption();
			oldNaturalSequence = record.getCalcKey().isNaturalSequence();
		} else {
			// the record should be VIA, save the old via specification
			oldViaSetName = record.getViaSpecification().getSet().getName();
			oldSymbolicDisplacementName = record.getViaSpecification()
					  						    .getSymbolicDisplacementName();
			oldDisplacementPageCount = record.getViaSpecification()
											 .getDisplacementPageCount();
			oldViaSpecificationIndex = 
				record.getViaSpecification()
					  .getSet()
					  .getViaMembers()
					  .indexOf(record.getViaSpecification());
		}
		
		// make the change
		redo();
		
	}
	
	@Override
	public void redo() {
				
		// set the record's location mode to DIRECT; we do this first so that
		// edit parts can stop listening for change events that have become 
		// irrelevant and might even cause trouble
		record.setLocationMode(LocationMode.DIRECT);
		
		// remove the CALC key in the case of a CALC record, remove the VIA
		// specification for a VIA record
		if (oldLocationMode == LocationMode.CALC) {
			removeCalcKey();
		} else {
			// remove the VIA specification from the record AND set
			removeViaSpecification();			
		}		
		
	}
	
	@Override
	public void undo() {
		
		// reconstruct the CALC key in the case of a CALC record, and the VIA
		// specification in the case of a VIA record
		if (oldLocationMode == LocationMode.CALC) {
			createCalcKey(oldCalcKeyElements, oldElementIndexes, 
						  oldDuplicatesOption, oldNaturalSequence, 
						  oldCalcKeyIndex);
		} else {
			createViaSpecification(oldViaSetName, oldSymbolicDisplacementName, 
								   oldDisplacementPageCount, 
								   oldViaSpecificationIndex);
		}
		
		// restore the record's location mode; we do this last because
		// everything necessary is in place and edit parts can listen for 
		// changes in the appropriate objects
		record.setLocationMode(oldLocationMode);
		
	}
	
}