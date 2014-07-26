/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

/**
 * A command that will change the record's CALC key.  This command can only be  used for CALC 
 * records and will definitely run into trouble when executed for a record that is defined as either 
 * DIRECT or VIA. 
 */
@ModelChange(category=ModelChangeCategory.SET_FEATURES)
public class ChangeCalcKeyCommand extends AbstractChangeLocationModeCommand {

	@Owner	  private SchemaRecord 		   record;
	@Features private EStructuralFeature[] features = {
		SchemaPackage.eINSTANCE.getSchemaRecord_CalcKey()
	};
	
	private List<Element>	 newCalcKeyElements = new ArrayList<>();
	private DuplicatesOption newDuplicatesOption;
	private int[] 			 newElementIndexes;
	
	public ChangeCalcKeyCommand(SchemaRecord record,
								List<Element> calcKeyElements,
								DuplicatesOption duplicatesOption) {
		super("Change CALC key", record);
		this.record = record;
		newCalcKeyElements = new ArrayList<>(calcKeyElements);
		newDuplicatesOption = duplicatesOption;
	}
	
	@Override
	public void execute() {			
		
		// save the old data
		stash(0);
		
		// retain as much element indexes as possible; if an element is still 
		// part of the CALC key, maintain the element's key element index
		newElementIndexes = new int[newCalcKeyElements.size()];
		for (int i = 0; i < newElementIndexes.length; i++) {
			newElementIndexes[i] = -1;
		}
		Key calcKey = record.getCalcKey();
		for (int i = 0; i < newElementIndexes.length; i++) {
			Element element = newCalcKeyElements.get(i);
			for (KeyElement keyElement : element.getKeyElements()) {
				if (keyElement.getKey() == calcKey) {
					newElementIndexes[i] = element.getKeyElements().indexOf(keyElement);
					break;
				}
			}
		}
		
		// remove the CALC key...
		removeCalcKey();
		
		// ...and create a new one; stash the new CALC key
		createCalcKey(newCalcKeyElements, newElementIndexes, newDuplicatesOption, 
					  getStashedNaturalSequence(0), getStashedCalcKeyIndex(0));
		stash(1);
		
	}
	
	@Override
	public void redo() {		
		removeCalcKey();		
		restoreCalcKey(1);		
	};
	
	@Override
	public void undo() {				
		removeCalcKey();		
		restoreCalcKey(0);		
	}

}
