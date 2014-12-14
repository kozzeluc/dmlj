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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.MOVE_ITEM;
import static org.lh.dmlj.schema.editor.command.annotation.OwnerType.NEW;
import static org.lh.dmlj.schema.editor.command.annotation.OwnerType.OLD;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EReference;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.common.Tools;

/**
 * A Command class to move a record or index to another area.  The new area might not yet exist and 
 * thus will be created if needed; if the old area does not contain any records or system owners 
 * (indexes) after the move, this command will also remove the old area from the schema.  The 
 * SchemaRecord's or SystemOwner's AreaSpecification will never be replaced with a new one; the old 
 * one is retained (this used to be different in the past).<br><br>
 * Besides moving a record or index to another area, a whole bunch of other attributes in the area
 * specification can be changed as well.
 */
@ModelChange(category=MOVE_ITEM)
public class MoveRecordOrIndexToOtherAreaCommand extends ModelChangeBasicCommand {
	
	@Owner(type=OLD) private SchemaArea		   oldArea;
	@Owner(type=NEW) private SchemaArea 	   newArea;
	@Reference 		 private EReference 	   reference = SchemaPackage.eINSTANCE.getSchemaArea_AreaSpecifications();	
	@Item			 private AreaSpecification areaSpecification;
	
	private Schema			  schema;
	private SchemaRecord 	  record;	
	private SystemOwner 	  systemOwner;			
		
	private int				  oldAreaIndex = -1;
	private int				  oldAreaSpecificationIndex;	
		
	private String 		 	  newAreaName;

	private static boolean isAreaObsolete(SchemaArea area) {
		return area.getRecords().isEmpty() && area.getIndexes().isEmpty();
	}
	
	public MoveRecordOrIndexToOtherAreaCommand(SchemaRecord record, String newAreaName) {		
		
		super("Move record '" + Tools.removeTrailingUnderscore(record.getName()) + "' to area '" + 
			  newAreaName + "'");
		this.record = record;		
		this.newAreaName = newAreaName;   	    
	}
	
	public MoveRecordOrIndexToOtherAreaCommand(SystemOwner systemOwner, String newAreaName) {		
		
		super("Move index '" + Tools.removeTrailingUnderscore(systemOwner.getSet().getName()) + 
			  "' to area '" + newAreaName + "'");
		this.systemOwner = systemOwner;		
		this.newAreaName = newAreaName;   	    
	}	
	
	@Override
	public void execute() {
		
		// perform some checks
		Assert.isTrue(record != null || systemOwner != null, 
					  "logic error: record == null && system owner == null");
		Assert.isTrue(newAreaName != null, "logic error: newAreaName == null");
		
		// save the old data; when saving the area, we also keep the references to the (area)  
		// procedure calls, which makes restoring the area easy
		if (record != null) {
			// we're moving a record to another area
			schema = record.getSchema();
			oldArea = record.getAreaSpecification().getArea();
			areaSpecification = record.getAreaSpecification();			
		} else {
			// we're moving an index to another area
			schema = systemOwner.getSet().getSchema();
			oldArea = systemOwner.getAreaSpecification().getArea();
			areaSpecification = systemOwner.getAreaSpecification();
		}
		oldAreaIndex = schema.getAreas().indexOf(oldArea);
		oldAreaSpecificationIndex = oldArea.getAreaSpecifications().indexOf(areaSpecification);			
		
		// if we're moving the record or index to a NEW area, create that area but don't hook it to 
		// the schema yet
		newArea = schema.getArea(newAreaName);
		if (newArea == null) {		
			// the record is moved to a NEW area; create it and set its name 
			newArea = SchemaFactory.eINSTANCE.createSchemaArea();
			newArea.setName(newAreaName);					
		}		
		
		// go ahead with the changes
		redo();
		
	}
	
	@Override
	public void redo() {
		
		// if we're moving the record or index to a NEW area, hook that area to the schema
		if (newArea != oldArea) {
			newArea.setSchema(schema);
		}
		
		// hook the area specification to the new area
		areaSpecification.setArea(newArea);		
		
		// remove the old area from the schema if needed
		if (isAreaObsolete(oldArea)) {
			schema.getAreas().remove(oldArea);
		}
		
	}
	
	@Override
	public void undo() {
		
		// restore the old area (together with its possible procedure calls) if it was removed from 
		// the schema; insert it at its original position in the list of areas
		if (isAreaObsolete(oldArea)) {
			schema.getAreas().add(oldAreaIndex, oldArea);
		}
		
		// restore the record's or system owner's area specification at its original position in the 
		// area's list of area specifications
		oldArea.getAreaSpecifications().add(oldAreaSpecificationIndex, areaSpecification);
		
		// finally, remove the new area from the schema if it was created during the execution of 
		// this command (the area will contain neither records nor indexes at this point)
		if (isAreaObsolete(newArea)) {			
			schema.getAreas().remove(newArea);
		}
		
	}

}
