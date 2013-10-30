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
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.OffsetExpression;
import org.lh.dmlj.schema.SchemaFactory;

/**
 * A base Command class to maintain an AreaSpecification object's (optional) 
 * OffsetExpression.
 */
public abstract class AbstractOffsetExpressionManipulationCommand 
	extends Command {
	
	private static void createOffsetExpression(AreaSpecification areaSpecification,
											   Integer offsetPageCount,
											   Short offsetPercent, 
											   Integer pageCount, Short percent) {
		
		// make sure neither symbolic subarea and offset expression are set
		Assert.isTrue(areaSpecification.getSymbolicSubareaName() == null, 
				  	  "logic error: symbolic subarea != null");
		Assert.isTrue(areaSpecification.getOffsetExpression() == null, 
					  "logic error: offset expression != null");
		
		// only create an offset expression if there is at least 1 attribute
		// specified and the attribute values are different from the defaults
		// (0 PAGES FOR 0 PERCENT)
		if (offsetPageCount != null || offsetPageCount != null || 
			pageCount != null || percent != null) {
			
			// at least 1 of the offset expression values is to be set (unless 
			// they match the defaults)
			if (offsetPageCount == null || offsetPageCount.intValue() != 0 ||
				offsetPercent == null || offsetPercent.shortValue() != 0 ||
				percent == null || percent.intValue() != 100) {
				
				// offset expression contains non default values, so go ahead
				// and create it
				OffsetExpression offsetExpression = 
					SchemaFactory.eINSTANCE.createOffsetExpression();
				offsetExpression.setOffsetPageCount(offsetPageCount);
				offsetExpression.setOffsetPercent(offsetPercent);
				offsetExpression.setPageCount(pageCount);
				offsetExpression.setPercent(percent);
				areaSpecification.setOffsetExpression(offsetExpression);
				
			}					
				
		}
		
	}
	
	protected static void maintainOffsetExpression(AreaSpecification areaSpecification,
												  Integer offsetPageCount,
												  Short offsetPercent, 
												  Integer pageCount, 
												  Short percent) {
		
		// make sure no symbolic subarea is set
		Assert.isTrue(areaSpecification.getSymbolicSubareaName() == null, 
				  	  "logic error: symbolic subarea != null");
		
		// depending on the presence or absence of an offset expression, either
		// create it or modify it
		if (areaSpecification.getOffsetExpression() != null) {
			
			// offset expression present; we only need one if the
			// (new) attribute values are different from the default (0 
			// PAGES FOR 0 PERCENT)
			modifyOrRemoveOffsetExpression(areaSpecification, offsetPageCount, 
										   offsetPercent, 
										   pageCount, percent);
			
		} else {
			
			// no offset expression is present; we only need one if the
			// (new) attribute values are different from the default (0 
			// PAGES FOR 0 PERCENT)
			createOffsetExpression(areaSpecification, offsetPageCount, 
								   offsetPercent, pageCount, percent);
			
		}
		
	}
	
	private static void modifyOrRemoveOffsetExpression(AreaSpecification areaSpecification,
													   Integer offsetPageCount,
													   Short offsetPercent, 
													   Integer pageCount, 
													   Short percent) {	
	
		// make sure no symbolic subarea is set and that an offset expression 
		// currently exists
		Assert.isTrue(areaSpecification.getSymbolicSubareaName() == null, 
				  	  "logic error: symbolic subarea != null");
		Assert.isTrue(areaSpecification.getOffsetExpression() != null, 
					  "logic error: offset expression == null");		
		
		if (offsetPageCount == null || offsetPageCount.intValue() != 0 ||
			percent == null || percent.intValue() != 100) {
				
			// offset expression contains non default values	
			OffsetExpression offsetExpression = 
				areaSpecification.getOffsetExpression();
			offsetExpression.setOffsetPageCount(offsetPageCount);
			offsetExpression.setOffsetPercent(offsetPercent);
			offsetExpression.setPageCount(pageCount);
			offsetExpression.setPercent(percent);						
			
		} else {
			
			// nullify the offsetExpression because the default values apply
			// (0 PAGES FOR 0 PERCENT)
			areaSpecification.setOffsetExpression(null);
			
		}		
		
	}	

	protected AbstractOffsetExpressionManipulationCommand(String label) {
		super(label);		
	}
}
