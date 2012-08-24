package org.lh.dmlj.schema.editor.command;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.ViaSpecification;

public abstract class AbstractChangeLocationModeCommand extends Command {

	protected SchemaRecord record;	
	
	public AbstractChangeLocationModeCommand(String label, SchemaRecord record) {
		super(label);
		this.record = record;
	}
	
	/**
	 * Creates a new CALC key and adds it to the record.
	 * @param calcKeyElements a list of the elements making up the CALC key
	 * @param elementIndexes an array with the indexes at which each KeyElement 
	 *        has to be inserted into each CALC Element's keyElements reference,
	 *        or null if each key element has to be appended to the keyElements
	 *        list - if not null, this array MUST be exactly the size of the
	 *        calcKeyElements list
	 * @param duplicatesOption the CALC key's duplicates option, must not be 
	 *        null
	 * @param naturalSequence not applicable to CALC keys; whatever is passed is
	 *        set in the CALC key 
	 * @param calcKeyIndex the index at which the CALC key has to be inserted
	 *        in the record's keys reference; any value less than zero indicates 
	 *        that the CALC key has to be appended to the end of that list
	 */
	protected void createCalcKey(List<Element> calcKeyElements,
								 int[] elementIndexes,
								 DuplicatesOption duplicatesOption,
								 boolean naturalSequence,
								 int calcKeyIndex) {
		
		Assert.isTrue(record.getCalcKey() == null, 
					  "record's calcKey is already set");
		// create a new CalcKey and add the CALC key elements to it, in the 
		// order of the calcKeyElements list
		Key calcKey = SchemaFactory.eINSTANCE.createKey();
		int i = 0;
		for (Element element : calcKeyElements) {
			KeyElement keyElement = 
				SchemaFactory.eINSTANCE.createKeyElement();
			keyElement.setSortSequence(SortSequence.ASCENDING);
			if (elementIndexes != null) {
				// retain the original key element indexes, if specified
				int j = elementIndexes[i++]; 	       
				if (j > -1) {
					element.getKeyElements().add(j, keyElement);
				} else {
					// append the key element to the end of the element's list 
					// of key elements
					element.getKeyElements().add(keyElement);
				}
			} else {
				// append the key element to the end of the element's list of
				// key elements
				element.getKeyElements().add(keyElement);
			}
			keyElement.setKey(calcKey);
		}
		calcKey.setDuplicatesOption(duplicatesOption);
		calcKey.setNaturalSequence(naturalSequence);
		if (calcKeyIndex > -1) {
			// insert the CALC key in the record's keys list at its original
			// location
			record.getKeys().add(calcKeyIndex, calcKey);
		} else {
			//append the CALC key to the end of the record's keys list
			record.getKeys().add(calcKey);
		}
		// set the record's CALC key
		record.setCalcKey(calcKey);		
	}
	
	
	/**
	 * Creates a new VIA specification and adds it to the record AND set.
	 * @param viaSetName the name of the VIA set
	 * @param symbolicDisplacementName the symbolic displacement name or null if
	 *        there is no symbolic displacement to use
	 * @param displacementPageCount the displacement page count or null if there
	 *        are no displacement pages to use - ignored if symbolic 
	 *        displacement name is specified
	 * @param viaSpecificationIndex the index at which the VIA specification has 
	 *        to be inserted in the set's viaMembers reference; any value less 
	 *        than zero indicates that the VIA specification has to be appended 
	 *        to the end of that list
	 */
	protected void createViaSpecification(String viaSetName,
										  String symbolicDisplacementName,
										  Short displacementPageCount,
										  int viaSpecificationIndex) {
		
		Assert.isTrue(record.getViaSpecification() == null, 
				  	  "record's viaSpecification is already set");
		ViaSpecification viaSpecification = 
			SchemaFactory.eINSTANCE.createViaSpecification();			
		if (symbolicDisplacementName != null) {
			viaSpecification.setSymbolicDisplacementName(symbolicDisplacementName);
		} else if (displacementPageCount != null) {
			viaSpecification.setDisplacementPageCount(displacementPageCount);
		}
		Set set = record.getSchema().getSet(viaSetName);
		if (viaSpecificationIndex > -1) {
			// maintain the original index in the set's viaMembers list
			set.getViaMembers().add(viaSpecificationIndex, viaSpecification);
		} else {
			// append the VIA specification to the end of the set's viaMembers 
			// list
			set.getViaMembers().add(viaSpecification);
		}
		record.setViaSpecification(viaSpecification);			
	}
	
	/**
	 * Removes the CALC key from the record.
	 */
	protected void removeCalcKey() {
		Assert.isNotNull(record.getCalcKey(), "record's calcKey is null");
		// clear the record's CALC key
		Key calcKey = record.getCalcKey();
		record.setCalcKey(null);
		// remove the CALC key from the record's key list
		record.getKeys().remove(calcKey);
		// remove the references from the CALC elements to the CALC key
		for (KeyElement keyElement : calcKey.getElements()) {
			keyElement.setElement(null);
		}
	}
	
	/**
	 * Removes the VIA specification from the record AND set.
	 */
	protected void removeViaSpecification() {
		Assert.isNotNull(record.getViaSpecification(), 
						 "record's viaSpecification is null");
		// remove the VIA specification from the record AND set
		ViaSpecification viaSpecification = record.getViaSpecification(); 
		record.setViaSpecification(null);			
		viaSpecification.setSet(null);	
	}

}