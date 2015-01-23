/**
 * Copyright (C) 2015  Luc Hermans
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

import java.util.Arrays;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;

/**
 * A command that will change a set's order.  In the case the set order is changed to SORTED, the
 * sort key details for each member record have to be provided.
 */
public class ChangeSetOrderCommand extends AbstractSortKeyManipulationCommand {

	protected Set set;
	protected ISupplier<Set> setSupplier;
	
	private SetOrder oldOrder;
	protected SetOrder newOrder;	
	
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
	
	public ChangeSetOrderCommand(ISupplier<Set> setSupplier, ISortKeyDescription[] sortKeyDescriptions) {
		super(null, sortKeyDescriptions);
		this.setSupplier = setSupplier;
		newOrder = SetOrder.SORTED;		
	}	

	@Override
	public void execute() {
		
		if (setSupplier != null) {
			super.set = setSupplier.supply();
			this.set = super.set;
			if (sortKeyDescriptions != null) {
				Assert.isTrue(set.getMembers().size() == sortKeyDescriptions.length, 
						  	  "the number of sort key descriptions does NOT match the number of set " +
						  	  "members: " + set.getName() + " " + Arrays.asList(sortKeyDescriptions));
			}
		}
		
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
