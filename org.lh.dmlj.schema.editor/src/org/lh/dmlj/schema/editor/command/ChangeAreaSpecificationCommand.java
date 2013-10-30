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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;

/**
 * A Command class to change 1 or more attributes in an AreaSpecification and
 * its (optional) OffsetExpression.
 */
public class ChangeAreaSpecificationCommand 
	extends AbstractOffsetExpressionManipulationCommand {
	
	private AreaSpecification areaSpecification;
	
	private String  		 newSymbolicSubareaName;
	private Integer 		 newOffsetPageCount;
	private Short   		 newOffsetPercent;
	private Integer 		 newPageCount;
	private Short   		 newPercent;
	
	private String  		 oldSymbolicSubareaName;
	private OffsetExpression oldOffsetExpression;	
	
	public ChangeAreaSpecificationCommand(AreaSpecification areaSpecification,
										  String newSymbolicSubareaName,
										  Integer newOffsetPageCount,
										  Short newOffsetPercent,
										  Integer newPageCount,
										  Short newPercent) {
		
		super("Change area specification");	
		this.areaSpecification = areaSpecification;
		this.newSymbolicSubareaName = newSymbolicSubareaName;
		this.newOffsetPageCount = newOffsetPageCount;
		this.newOffsetPercent = newOffsetPercent;
		this.newPageCount = newPageCount;
		this.newPercent = newPercent;
	}	

	@Override
	public void execute() {
		
		// perform some integrity checks on the new data
		Assert.isTrue(!(newSymbolicSubareaName != null &&
			    		(newOffsetPageCount != null || 
			    		 newOffsetPercent != null || newPageCount != null ||
			    		 newPercent != null)), 
			    	  "logic error: cannot change area specification to " +
			    	  "contain both a subarea and offset expression data");
		if (newSymbolicSubareaName == null) {
			Assert.isTrue(!(newOffsetPageCount != null &&
						    newOffsetPercent != null), 
		    	    	  "logic error: cannot set both an offset page " +
		    	    	  "count and offset percent in an offset expression");
			Assert.isTrue(newOffsetPageCount != null || newOffsetPercent != null, 
				    	  "logic error: need to set an offset page count or " +
				    	  "offset percent in an offset expression");
			Assert.isTrue(!(newPageCount != null && newPercent != null), 
						  "logic error: cannot set both a page count and " +
						  "percent in an offset expression");
			Assert.isTrue(newPageCount != null || newPercent != null, 
						  "logic error: need to set a page count or percent " +
						  "in an offset expression");
		}
		
		// save the old data and perform some integrity checks
		oldSymbolicSubareaName = areaSpecification.getSymbolicSubareaName();
		oldOffsetExpression = areaSpecification.getOffsetExpression();
		Assert.isTrue(!(oldSymbolicSubareaName != null &&
					    oldOffsetExpression != null), 
					  "logic error: area specification with both a subarea " +
					  "and an offset expression");
		if (oldOffsetExpression != null) {
			Assert.isTrue(!(oldOffsetExpression.getOffsetPageCount() != null &&
				    	    oldOffsetExpression.getOffsetPercent() != null), 
				    	  "logic error: offset expression with both an " +
				    	  "offset page count and offset percent");
			Assert.isTrue(oldOffsetExpression.getOffsetPageCount() != null ||
		    	    	  oldOffsetExpression.getOffsetPercent() != null, 
		    	    	  "logic error: offset expression must contain an " +
					 	  "offset page count or offset percent");
			Assert.isTrue(!(oldOffsetExpression.getPageCount() != null &&
							oldOffsetExpression.getPercent() != null), 
						  "logic error: offset expression with both a page " +
					 	  "count and percent");
			Assert.isTrue(oldOffsetExpression.getPageCount() != null ||
						  oldOffsetExpression.getPercent() != null, 
						  "logic error: offset expression must contain a " +
						  "page count or percent");
		}
		
		// perform the change(s)
		redo();
		
	}
	
	@Override
	public void redo() {
	
		if (oldSymbolicSubareaName != null) {
			
			// the area specification contains a symbolic subarea
			
			if (newSymbolicSubareaName != null) {
				
				// the area specification will remain to contain a symbolic
				// subarea; make sure the value is different, if it's not, we
				// have some kind of logic error going on...			
				Assert.isTrue(!newSymbolicSubareaName.equals(oldSymbolicSubareaName), 
							  "logic error: new symbolic subarea == old " +
							  "symbolic subarea");
				
				// modify the symbolic subarea in the area specification
				areaSpecification.setSymbolicSubareaName(newSymbolicSubareaName);
				
			} else {
				
				// nullify the symbolic subarea in the area specification 
				// because the user has removed it or explicitly specified the
				// offset expression himself
				areaSpecification.setSymbolicSubareaName(null);
				
				// if the user has specified any of the offset expression data,
				// set them in the area specification's offset expression, 
				// unless they contain the default values (0 PAGES FOR 0 
				// PERCENT), in that case, keep the null offset expression
				maintainOffsetExpression(areaSpecification, newOffsetPageCount, 
										 newOffsetPercent, newPageCount, 
										 newPercent);
				
			}
			
		} else {
			
			// the area specification does NOT contain a symbolic subarea
			
			if (newSymbolicSubareaName != null) {
				
				// the user has specified a symbolic subarea, which means we 
				// have to nullify the offset expression
				areaSpecification.setOffsetExpression(null);
				
				// set the newly specified symbolic subarea in the area 
				// specification
				areaSpecification.setSymbolicSubareaName(newSymbolicSubareaName);
				
			} else {
				
				// maintain the offset expression using the new attributes
				maintainOffsetExpression(areaSpecification, newOffsetPageCount, 
										 newOffsetPercent, newPageCount, 
										 newPercent);
				
			}
		}
		
	}
	
	@Override
	public void undo() {
		areaSpecification.setSymbolicSubareaName(oldSymbolicSubareaName);
		areaSpecification.setOffsetExpression(oldOffsetExpression);		
	}
	
}
