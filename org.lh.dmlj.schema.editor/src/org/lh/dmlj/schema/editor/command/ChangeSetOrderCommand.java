/**
 * Copyright (C) 2014  Luc Hermans
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

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.command.annotation.Features;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory;
import org.lh.dmlj.schema.editor.command.annotation.Owner;

/**
 * A command that will change a set's order.  In the case the set order is changed to SORTED, the
 * sort key details for each member record have to be provided.
 */
@ModelChange(category=ModelChangeCategory.SET_FEATURES)
public class ChangeSetOrderCommand extends AbstractSortKeyManipulationCommand {

	@Owner	  private Set 		   		   set;
	@Features private EStructuralFeature[] features = {
		SchemaPackage.eINSTANCE.getSet_Order()
	};
	
	private SetOrder oldOrder;
	private SetOrder newOrder;				
	
	public ChangeSetOrderCommand(Set set, SetOrder order) {
		super(set, null);
		Assert.isTrue(order != set.getOrder(), "same set order specified");
		Assert.isTrue(order != SetOrder.SORTED, "sorted set; specify sort key description(s)");
		this.set = set;
		newOrder = order;
	}
	
	public ChangeSetOrderCommand(Set set, ISortKeyDescription[] sortKeyDescriptions) {
		super(set, sortKeyDescriptions);
		Assert.isTrue(set.getOrder() != SetOrder.SORTED, "set is already sorted");
		this.set = set;
		newOrder = SetOrder.SORTED;		
	}

	@Override
	public void execute() {
		
		// remember the current set order
		oldOrder = set.getOrder();
		
		// remember the current sort keys if the set is sorted
		if (set.getOrder() == SetOrder.SORTED) {
			stashSortKeys(0);
		}
		
		// prepare the sort key for each member if the set order is changed to sorted 
		if (newOrder == SetOrder.SORTED) {
			prepareSortKeys(0);
		}
		
		redo();
		
	}

	@Override
	public void redo() {
		
		// change the set order
		set.setOrder(newOrder);
		
		// deal with the sort key if applicable
		if (oldOrder == SetOrder.SORTED) {			
			removeSortKeys();			
		} else if (newOrder == SetOrder.SORTED) {
			restoreSortKeys(0);
		}
		
	}
	
	@Override
	public void undo() {
		
		// restore the set order
		set.setOrder(oldOrder);
		
		// deal with the sort key if applicable
		if (oldOrder == SetOrder.SORTED) {			
			restoreSortKeys(0);			
		} else if (newOrder == SetOrder.SORTED) {
			removeSortKeys();
		}
		
	}	
	
}
