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

import static org.lh.dmlj.schema.editor.common.ValidationResult.Status.OK;
import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class ChangeIndexedSetModeSpecificationCommand extends ModelChangeBasicCommand {
	
	private IndexedSetModeSpecification indexedSetModeSpecification;
	
	private String oldSymbolicIndexName;
	private Short oldKeyCount;
	private Short oldDisplacementPages;
	
	private String newSymbolicIndexName;
	private Short newKeyCount;
	private Short newDisplacementPages;

	public ChangeIndexedSetModeSpecificationCommand(Set set, String symbolicIndexName, 
													Short keyCount, Short displacementPages) {
		
		super("Change indexed set mode specification");
		Assert.isTrue(set != null, "set is null");
		this.indexedSetModeSpecification = set.getIndexedSetModeSpecification();
		this.newSymbolicIndexName = symbolicIndexName;
		this.newKeyCount = keyCount;
		this.newDisplacementPages = displacementPages;
	}
	
	@Override
	public void execute() {
		
		Assert.isTrue(indexedSetModeSpecification != null, "not an indexed set");
		Assert.isTrue(newSymbolicIndexName != null || newKeyCount != null, 
					  "neither symbolic index name nor key count specified");
		Assert.isTrue(newSymbolicIndexName == null || newKeyCount == null, 
				  	  "symbolic index name AND key count specified");
		Assert.isTrue(!(newKeyCount == null && newDisplacementPages != null), 
			  	  	  "key count NOT specified");
		
		if (newSymbolicIndexName != null) {
			ValidationResult validationResult = 
					NamingConventions.validate(newSymbolicIndexName, NamingConventions.Type.SYMBOLIC_INDEX_NAME);
			Assert.isTrue(validationResult.getStatus() == OK, 
					  	  "symbolicIndexName is invalid (" + validationResult.getMessage() +	")");
		}
		Assert.isTrue(newKeyCount == null || 
					  newKeyCount.shortValue() >= 3 && newKeyCount.shortValue() <= 8180, 
					  "keyCount is invalid (must be >= 3 and <= 8180)");
		Assert.isTrue(newDisplacementPages == null || newDisplacementPages.shortValue() >= 1, 
				  	  "displacementPages is invalid (must be >= 1 and <= 32767)");
		
		oldSymbolicIndexName = indexedSetModeSpecification.getSymbolicIndexName();
		oldKeyCount = indexedSetModeSpecification.getKeyCount();
		oldDisplacementPages = indexedSetModeSpecification.getDisplacementPageCount();
		
		redo();
	}
	
	@Override
	public void undo() {
		indexedSetModeSpecification.setSymbolicIndexName(oldSymbolicIndexName);
		indexedSetModeSpecification.setKeyCount(oldKeyCount);
		indexedSetModeSpecification.setDisplacementPageCount(oldDisplacementPages);
	}
	
	@Override
	public void redo() {
		indexedSetModeSpecification.setSymbolicIndexName(newSymbolicIndexName);
		indexedSetModeSpecification.setKeyCount(newKeyCount);
		indexedSetModeSpecification.setDisplacementPageCount(newDisplacementPages);
	}
	
}
