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

import java.util.Arrays;

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
 * A command that will change a sort key.
 */
@ModelChange(category=ModelChangeCategory.SET_FEATURES)
public class ChangeSortKeysCommand extends AbstractSortKeyManipulationCommand {

	@Owner	  protected Set 		   	   set;
	@Features private EStructuralFeature[] features = {
		SchemaPackage.eINSTANCE.getMemberRole_SortKey()
	};
	
	protected ISupplier<Set> setSupplier;
	
	public ChangeSortKeysCommand(Set set, ISortKeyDescription[] sortKeyDescriptions) {
		super(set, sortKeyDescriptions);
		Assert.isTrue(set.getOrder() == SetOrder.SORTED, "not a sorted set");
		Assert.isNotNull(sortKeyDescriptions, "sortKeyDescriptions is null");
		this.set = set;
	}
	
	public ChangeSortKeysCommand(ISupplier<Set> setSupplier, 
								 ISortKeyDescription[] sortKeyDescriptions) {
		
		super(null, sortKeyDescriptions);
		Assert.isNotNull(sortKeyDescriptions, "sortKeyDescriptions is null");
		this.setSupplier = setSupplier;
	}
	
	@Override
	public void execute() {
		
		if (setSupplier != null) {
			super.set = setSupplier.supply();
			set = super.set;
			Assert.isTrue(set.getOrder() == SetOrder.SORTED, "not a sorted set");
			Assert.isTrue(set.getMembers().size() == sortKeyDescriptions.length, 
				  	  	  "the number of sort key descriptions does NOT match the number of set " +
				  	  	  "members: " + set.getName() + " " + Arrays.asList(sortKeyDescriptions));
		}
		
		// remember the current sort keys		
		stashSortKeys(0);		
		
		// remove the current sort keys
		removeSortKeys();		
		
		// prepare the new sort key for each member 		
		prepareSortKeys(1);
		
		// install the new sort keys
		restoreSortKeys(1);
		
	}
	
	@Override
	public void redo() {
		
		// remove the current sort keys
		removeSortKeys();			
		
		// install the new sort keys
		restoreSortKeys(1);
		
	}
	
	@Override
	public void undo() {
		
		// remove the current sort keys
		removeSortKeys();			
		
		// install the old sort keys
		restoreSortKeys(0);		
		
	}

}
