package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaRecord;

/**
 * A command that will change the record's CALC key.  This command can only be 
 * used for CALC records and will definitely run into trouble when executed for 
 * a record that is defined as either DIRECT or VIA.
 */
public class ChangeCalcKeyCommand extends AbstractChangeLocationModeCommand {

	private int				 oldCalcKeyIndex;
	private List<Element>	 oldCalcKeyElements = new ArrayList<>();
	private int[] 			 oldElementIndexes;
	private DuplicatesOption oldDuplicatesOption;
	private boolean			 oldNaturalSequence;	
	
	private List<Element>	 newCalcKeyElements = new ArrayList<>();
	private int[] 			 newElementIndexes;
	private DuplicatesOption newDuplicatesOption;
	
	public ChangeCalcKeyCommand(SchemaRecord record,
								List<Element> calcKeyElements,
								DuplicatesOption duplicatesOption) {
		super("Change CALC key", record);
		newCalcKeyElements = new ArrayList<>(calcKeyElements);
		newDuplicatesOption = duplicatesOption;
	}
	
	@Override
	public void execute() {			
		
		// save the old data
		oldCalcKeyIndex = record.getKeys().indexOf(record.getCalcKey());
		oldElementIndexes = new int[record.getCalcKey().getElements().size()];
		int i = 0;
		for (KeyElement keyElement : record.getCalcKey().getElements()) {
			Element element = keyElement.getElement();
			oldCalcKeyElements.add(element);
			oldElementIndexes[i++] = element.getKeyElements()
											.indexOf(keyElement);					
		}
		oldDuplicatesOption = record.getCalcKey().getDuplicatesOption();
		oldNaturalSequence = record.getCalcKey().isNaturalSequence();		
		
		// retain as much element indexes as possible; if an element is still 
		// part of the CALC key, maintain the element's key element index
		newElementIndexes = new int[newCalcKeyElements.size()];
		for (i = 0; i < newElementIndexes.length; i++) {
			newElementIndexes[i] = -1;
		}
		Key calcKey = record.getCalcKey();
		for (i = 0; i < newElementIndexes.length; i++) {
			Element element = newCalcKeyElements.get(i);
			for (KeyElement keyElement : element.getKeyElements()) {
				if (keyElement.getKey() == calcKey) {
					newElementIndexes[i] = 
						element.getKeyElements().indexOf(keyElement);
					break;
				}
			}
		}
		
		// make the change
		redo();
		
	}
	
	@Override
	public void redo() {
		
		removeCalcKey();
		
		createCalcKey(newCalcKeyElements, newElementIndexes, 
				  	  newDuplicatesOption, oldNaturalSequence, 
				  	  oldCalcKeyIndex);
		
	};
	
	@Override
	public void undo() {		
		
		removeCalcKey();
		
		createCalcKey(oldCalcKeyElements, oldElementIndexes, 
				  	  oldDuplicatesOption, oldNaturalSequence, 
				  	  oldCalcKeyIndex);
		
	}

}