/**
 * Copyright (C) 2025  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.VsamLengthType;
import org.lh.dmlj.schema.VsamType;

public abstract class AbstractChangeLocationModeCommand extends ModelChangeBasicCommand {
	protected final SchemaRecord schemaRecord;	
	private final StashedData[] stash = new StashedData[5];
	
	protected AbstractChangeLocationModeCommand(String label, SchemaRecord schemaRecord) {
		super(label);
		this.schemaRecord = schemaRecord;
	}
	
	/**
	 * Creates a new CALC key and adds it to the record.
	 * @param calcKeyElements a list of the elements making up the CALC key
	 * @param elementIndexes an array with the indexes at which each KeyElement has to be inserted into each CALC
	 * 		  Element's keyElements reference, or null if each key element has to be appended to the keyElements
	 *        list - if not null, this array MUST be exactly the size of the calcKeyElements list
	 * @param duplicatesOption the CALC key's duplicates option, must not be null
	 * @param naturalSequence not applicable to CALC keys; whatever is passed is set in the CALC key 
	 * @param calcKeyIndex the index at which the CALC key has to be inserted in the record's keys reference;
	 * 		  any value less than zero indicates that the CALC key has to be appended to the end of that list
	 */
	protected void createCalcKey(List<Element> calcKeyElements, int[] elementIndexes, DuplicatesOption duplicatesOption,
			boolean naturalSequence, int calcKeyIndex) {
		
		Assert.isTrue(schemaRecord.getCalcKey() == null, "record's calcKey is already set");
		
		// create a new CalcKey and add the CALC key elements to it, in the  order of the calcKeyElements list
		var calcKey = SchemaFactory.eINSTANCE.createKey();
		var i = 0;
		for (var element : calcKeyElements) {
			var keyElement = SchemaFactory.eINSTANCE.createKeyElement();
			keyElement.setSortSequence(SortSequence.ASCENDING);
			if (elementIndexes != null) {
				// retain the original key element indexes, if specified
				var j = elementIndexes[i++]; 	       
				if (j > -1) {
					element.getKeyElements().add(j, keyElement);
				} else {
					// append the key element to the end of the element's list of key elements
					element.getKeyElements().add(keyElement);
				}
			} else {
				// append the key element to the end of the element's list of key elements
				element.getKeyElements().add(keyElement);
			}
			keyElement.setKey(calcKey);
		}
		calcKey.setDuplicatesOption(duplicatesOption);
		calcKey.setNaturalSequence(naturalSequence);
		if (calcKeyIndex > -1) {
			// insert the CALC key in the record's keys list at its original location
			schemaRecord.getKeys().add(calcKeyIndex, calcKey);
		} else {
			//append the CALC key to the end of the record's keys list
			schemaRecord.getKeys().add(calcKey);
		}
		
		schemaRecord.setCalcKey(calcKey);		
	}
	
	
	/**
	 * Creates a new VIA specification and adds it to the record AND set.
	 * @param viaSetName the name of the VIA set
	 * @param symbolicDisplacementName the symbolic displacement name or null if there is no symbolic displacement to use
	 * @param displacementPageCount the displacement page count or null if there are no displacement pages to use -
	 * 		  ignored if symbolic displacement name is specified
	 * @param viaSpecificationIndex the index at which the VIA specification has to be inserted in the set's
	 * 		  viaMembers reference; any value less than zero indicates that the VIA specification has to be
	 * 		  appended to the end of that list
	 */
	protected void createViaSpecification(String viaSetName, String symbolicDisplacementName,
			Short displacementPageCount, int viaSpecificationIndex) {
		
		Assert.isTrue(schemaRecord.getViaSpecification() == null, "record's viaSpecification is already set");
		var viaSpecification = SchemaFactory.eINSTANCE.createViaSpecification();			
		if (symbolicDisplacementName != null) {
			viaSpecification.setSymbolicDisplacementName(symbolicDisplacementName);
		} else if (displacementPageCount != null) {
			viaSpecification.setDisplacementPageCount(displacementPageCount);
		}
		var set = schemaRecord.getSchema().getSet(viaSetName);
		if (viaSpecificationIndex > -1) {
			// maintain the original index in the set's viaMembers list
			set.getViaMembers().add(viaSpecificationIndex, viaSpecification);
		} else {
			// append the VIA specification to the end of the set's viaMembers list
			set.getViaMembers().add(viaSpecification);
		}
		schemaRecord.setViaSpecification(viaSpecification);			
	}
	
	protected void createVsamType() {
		Assert.isTrue(schemaRecord.getVsamType() == null, "record's vsamType is already set");
		var vsamType = SchemaFactory.eINSTANCE.createVsamType();
		schemaRecord.setVsamType(vsamType);
		vsamType.setLengthType(VsamLengthType.FIXED);
		vsamType.setSpanned(false);		
	}
	
	protected int getStashedCalcKeyIndex(int index) {
		return stash[index].calcKeyIndex;
	}
	
	protected LocationMode getStashedLocationMode(int index) {
		Assert.isTrue(stash[index] != null, "CALC key not stashed: " + index);
		return stash[index].locationMode;
	}
	
	protected boolean getStashedNaturalSequence(int index) {
		return stash[index].calcKey.isNaturalSequence();
	}
		
	protected void removeCalcKey() {
		Assert.isNotNull(schemaRecord.getCalcKey(), "record's calcKey is null");
		// clear the record's CALC key
		Key calcKey = schemaRecord.getCalcKey();
		schemaRecord.setCalcKey(null);
		// remove the CALC key from the record's key list
		schemaRecord.getKeys().remove(calcKey);
		// remove the references from the CALC elements to the CALC key
		for (var keyElement : calcKey.getElements()) {
			keyElement.setElement(null);
		}
	}
		
	protected void removeViaSpecification() {
		Assert.isNotNull(schemaRecord.getViaSpecification(), "record's viaSpecification is null");
		// remove the VIA specification from the record AND set
		ViaSpecification viaSpecification = schemaRecord.getViaSpecification(); 
		schemaRecord.setViaSpecification(null);			
		viaSpecification.setSet(null);	
	}
	
	protected void removeVsamType() {
		Assert.isNotNull(schemaRecord.getVsamType(), "record's vsamType is null");
		schemaRecord.setVsamType(null);
	}

	protected void restoreCalcKey(int index) {
		Assert.isTrue(stash[index] != null || stash[index].locationMode != LocationMode.CALC, "CALC key not stashed: " + index);
		Assert.isTrue(schemaRecord.getCalcKey() == null, "record's calcKey is already set");
		
		// we need to reconnect each CALC key element to the right element again and restore the key elements
		// list for each element in its original shape again
		for (var i = 0; i < stash[index].calcKeyElements.size(); i++) {						
			var keyElement = stash[index].calcKey.getElements().get(i);
			var element = stash[index].calcKeyElements.get(i);
			element.getKeyElements().add(stash[index].calcKeyElementIndexes[i], keyElement);			
		}		
		
		// insert the CALC key in the record's keys list at its original location
		schemaRecord.getKeys().add(stash[index].calcKeyIndex, stash[index].calcKey);		
				
		schemaRecord.setCalcKey(stash[index].calcKey);		
	}

	protected void restoreViaSpecification(int index) {		
		Assert.isTrue(stash[index] != null || stash[index].locationMode != LocationMode.VIA, "VIA specification not stashed: " + index);
		Assert.isTrue(schemaRecord.getViaSpecification() == null, "record's viaSpecification is already set");
				
		// maintain the original index in the set's viaMembers list
		stash[index].viaSet.getViaMembers().add(stash[index].viaSpecificationIndex, stash[index].viaSpecification);
				
		schemaRecord.setViaSpecification(stash[index].viaSpecification);
	}
	
	protected void restoreVsamType(int index) {				
		Assert.isTrue(stash[index] != null || stash[index].locationMode != LocationMode.VSAM && stash[index].locationMode != LocationMode.VSAM_CALC,
				"VSAM type not stashed: " + index);
		Assert.isTrue(schemaRecord.getVsamType() == null, "record's vsamType is already set");
		
		schemaRecord.setVsamType(stash[index].vsamType);
	}
	
	protected void stash(int index) {
		Assert.isTrue(stash[index] == null, "already stashed: " + index);
		stash[index] = new StashedData();
		stash[index].locationMode = schemaRecord.getLocationMode();		
		if (stash[index].locationMode == LocationMode.CALC || stash[index].locationMode == LocationMode.VSAM_CALC) {
			stashCalcKey(index);				
		} else if (stash[index].locationMode == LocationMode.VIA) {
			stashViaSpecification(index);
		} 
		if (stash[index].locationMode == LocationMode.VSAM || stash[index].locationMode == LocationMode.VSAM_CALC) {
			stashVsamType(index);
		}
	}
	
	private void stashCalcKey(int index) {
		Assert.isTrue(schemaRecord.getCalcKey() != null, "cannot stash: no CALC key");
		stash[index].calcKey = schemaRecord.getCalcKey();
		stash[index].calcKeyIndex = schemaRecord.getKeys().indexOf(stash[index].calcKey);
		stash[index].calcKeyElementIndexes = new int[schemaRecord.getCalcKey().getElements().size()];
		stash[index].calcKeyElements = new ArrayList<>();
		var i = 0;
		for (var keyElement : schemaRecord.getCalcKey().getElements()) {
			var element = keyElement.getElement();
			stash[index].calcKeyElements.add(element);
			stash[index].calcKeyElementIndexes[i++] = element.getKeyElements().indexOf(keyElement);					
		}
	}
	
	private void stashViaSpecification(int index) {
		Assert.isTrue(schemaRecord.getViaSpecification() != null, "cannot stash: no VIA specification");
		stash[index].viaSpecification = schemaRecord.getViaSpecification();
		stash[index].viaSet = schemaRecord.getViaSpecification().getSet();
		stash[index].viaSpecificationIndex = schemaRecord.getViaSpecification().getSet().getViaMembers().indexOf(stash[index].viaSpecification);
	}
	
	private void stashVsamType(int index) {
		Assert.isTrue(schemaRecord.getVsamType() != null, "cannot stash: no VSAM type");
		stash[index].vsamType = schemaRecord.getVsamType();
	}
	
	private static class StashedData {
		protected LocationMode locationMode;	
		
		private Key 	calcKey;
		private int	calcKeyIndex;
		private List<Element> calcKeyElements;
		private int[] calcKeyElementIndexes;			
		
		private Set	viaSet;
		private ViaSpecification viaSpecification;
		private int	viaSpecificationIndex;
		
		private VsamType	 vsamType;
	}

}
