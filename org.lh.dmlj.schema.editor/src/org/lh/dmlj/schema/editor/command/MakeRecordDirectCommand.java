package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.ViaSpecification;

/**
 * A command that will change the record's location mode to DIRECT.  This 
 * command can only be used for CALC and VIA records and will definitely run 
 * into trouble when executed for a record that is already defined as DIRECT.
 */
public class MakeRecordDirectCommand extends Command {

	private SchemaRecord 	 	  record;
	
	private LocationMode     	  oldLocationMode;
	
	private int				 	  oldCalcKeyIndex;
	private List<Element>	 	  oldCalcKeyElements = new ArrayList<>();
	private int[] 				  oldElementIndexes;
	private DuplicatesOption 	  oldDuplicatesOption;
	private boolean			 	  oldNaturalSequence;
	
	private String			 	  oldViaSetName;
	private String			 	  oldSymbolicDisplacementName;
	private Short			 	  oldDisplacementPageCount;		
	
	public MakeRecordDirectCommand(SchemaRecord record) {
		super("Set 'Location mode' to 'DIRECT'");
		this.record = record;
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
			oldViaSetName = record.getViaSpecification().getSetName();
			oldSymbolicDisplacementName = record.getViaSpecification()
					  						    .getSymbolicDisplacementName();
			oldDisplacementPageCount = record.getViaSpecification()
											 .getDisplacementPageCount();
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
			// clear the record's CALC key
			Key calcKey = record.getCalcKey();
			record.setCalcKey(null);
			// remove the CALC key from the record's key list
			record.getKeys().remove(calcKey);
			// remove the references from the CALC elements to the CALC key
			for (KeyElement keyElement : calcKey.getElements()) {
				keyElement.setElement(null);
			}
		} else {
			// remove the VIA specification from the record AND set
			ViaSpecification viaSpecification = record.getViaSpecification(); 
			record.setViaSpecification(null);			
			viaSpecification.setSet(null);			
		}		
		
	}
	
	@Override
	public void undo() {
		
		// reconstruct the CALC key in the case of a CALC record, and the VIA
		// specification in the case of a VIA record
		if (oldLocationMode == LocationMode.CALC) {
			// create a new CalcKey and add the CALC key elements to it
			Key calcKey = SchemaFactory.eINSTANCE.createKey();
			int i = 0;
			for (Element element : oldCalcKeyElements) {
				KeyElement keyElement = 
					SchemaFactory.eINSTANCE.createKeyElement();
				keyElement.setSortSequence(SortSequence.ASCENDING);
				int j = oldElementIndexes[i++]; 	  // retain the original key 
				element.getKeyElements().add(j, keyElement);  // element indexes
				keyElement.setKey(calcKey);
			}
			calcKey.setDuplicatesOption(oldDuplicatesOption);
			calcKey.setNaturalSequence(oldNaturalSequence);
			// insert the CALC key in the record's key list at its original
			// location
			record.getKeys().add(oldCalcKeyIndex, calcKey);
			// set the record's CALC key
			record.setCalcKey(calcKey);
		} else {
			// reconstruct the VIA specification; there is a setName attribute
			// in ViaSpecification, which seems a little strange and that should
			// always match the name attribute in the ViaSpecification's set
			// reference - this attribute has its use in the schema import 
			// tool(s) but is actually a quite ugly constuct, so we should get 
			// rid of it some day
			ViaSpecification viaSpecification = 
				SchemaFactory.eINSTANCE.createViaSpecification();
			viaSpecification.setSetName(oldViaSetName);
			if (oldSymbolicDisplacementName != null) {
				viaSpecification.setSymbolicDisplacementName(oldSymbolicDisplacementName);
			} else if (oldDisplacementPageCount != null) {
				viaSpecification.setDisplacementPageCount(oldDisplacementPageCount);
			}
			Set set = record.getSchema().getSet(oldViaSetName);
			viaSpecification.setSet(set);
			record.setViaSpecification(viaSpecification);									
		}
		
		// restore the record's location mode; we do this last because
		// everything necessary is in place and edit parts can listen for 
		// changes in the appropriate objects
		record.setLocationMode(oldLocationMode);		
	}
	
}