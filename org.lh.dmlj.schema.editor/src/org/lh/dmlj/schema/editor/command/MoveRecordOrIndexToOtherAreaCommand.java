package org.lh.dmlj.schema.editor.command;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.common.Tools;

/**
 * A Command class to move a record or index to another area.  The new area 
 * might not yet exist and thus will be created if needed; if the old area does 
 * not contain any records or system owners (indexes) after the move, this 
 * command will also remove the old area from the schema.  The SchemaRecord's or 
 * SystemOwner's AreaSpecification will always be replaced with a new one.
 */
public class MoveRecordOrIndexToOtherAreaCommand 
	extends AbstractOffsetExpressionManipulationCommand {
	
	private SchemaRecord 	  record;
	private SystemOwner 	  systemOwner;
	private String 		 	  newAreaName;
	private Schema			  schema;
	
	private SchemaArea	 	  oldArea;
	private AreaSpecification oldAreaSpecification;
	private int				  oldAreaSpecificationIndex; // SchemaArea.areaSpecifications
	private int				  oldAreaIndex = -1;		 // Schema.areas; -1 if area retained	
	
	private SchemaArea   	  newArea;
	private AreaSpecification newAreaSpecification;
	private String  		  newSymbolicSubareaName;
	private Integer 		  newOffsetPageCount;
	private Short   		  newOffsetPercent;
	private Integer 		  newPageCount;
	private Short   		  newPercent;	

	public MoveRecordOrIndexToOtherAreaCommand(SchemaRecord record, 
			 						    	   String newAreaName,
			 						    	   String newSymbolicSubareaName,
			 						    	   Integer newOffsetPageCount,
			 						    	   Short newOffsetPercent,
			 						    	   Integer newPageCount,
			 						    	   Short newPercent) {
		
		super("Move record '" + 
			  Tools.removeTrailingUnderscore(record.getName()) + "' to area '" + 
			  newAreaName + "'");
		this.record = record;
		schema = record.getSchema();
		this.newAreaName = newAreaName;
   	    this.newSymbolicSubareaName = newSymbolicSubareaName;
   	    this.newOffsetPageCount = newOffsetPageCount;
   	    this.newOffsetPercent = newOffsetPercent;
   	    this.newPageCount = newPageCount;
   	    this.newPercent = newPercent;
	}
	
	public MoveRecordOrIndexToOtherAreaCommand(SystemOwner systemOwner, 
				    						   String newAreaName,
				    						   String newSymbolicSubareaName,
				    						   Integer newOffsetPageCount,
				    						   Short newOffsetPercent,
				    						   Integer newPageCount,
				    						   Short newPercent) {
	
		super("Move index '" + 
			  Tools.removeTrailingUnderscore(systemOwner.getSet().getName()) + 
			  "' to area '" + newAreaName + "'");
		this.systemOwner = systemOwner;
		schema = systemOwner.getSet().getSchema();
		this.newAreaName = newAreaName;
   	    this.newSymbolicSubareaName = newSymbolicSubareaName;
   	    this.newOffsetPageCount = newOffsetPageCount;
   	    this.newOffsetPercent = newOffsetPercent;
   	    this.newPageCount = newPageCount;
   	    this.newPercent = newPercent;
	}	
	
	@Override
	public void execute() {
		
		// perform some checks
		Assert.isTrue(record != null || systemOwner != null, 
					  "logic error: record == null && system owner == null");
		Assert.isTrue(newAreaName != null, "logic error: newAreaName == null");
		
		// save the old data; when saving the area, we also keep the references
		// to the procedure calls, which makes restoring the area easy
		oldArea = record != null ? 
				  record.getAreaSpecification().getArea() :
				  systemOwner.getAreaSpecification().getArea();
		oldAreaSpecification = record != null ?
							   record.getAreaSpecification() :
							   systemOwner.getAreaSpecification();		
		oldAreaSpecificationIndex = 
			oldArea.getAreaSpecifications().indexOf(oldAreaSpecification);
		if (record != null && oldArea.getRecords().size() == 1 && 
			oldArea.getIndexes().isEmpty() ||
			systemOwner != null && oldArea.getRecords().isEmpty() && 
			oldArea.getIndexes().size() == 1) {
			
			// the old area will be removed; (only then) retain its index in the 
			// Schema.areas reference
			oldAreaIndex = schema.getAreas().indexOf(oldArea);
		}
		
		// go ahead with the changes
		redo();
		
	}
	
	@Override
	public void redo() {
		
		// check if we're moving the record or index to a NEW area
		newArea = schema.getArea(newAreaName);
		if (newArea == null) {
			
			// the record is moved to a NEW area; create it and hook it to the
			// schema
			newArea = SchemaFactory.eINSTANCE.createSchemaArea();
			newArea.setName(newAreaName);
			newArea.setSchema(schema);
			
		}
		
		// create the new area specification and set its attributes
		newAreaSpecification = 
			SchemaFactory.eINSTANCE.createAreaSpecification();
		if (newSymbolicSubareaName != null) {
			newAreaSpecification.setSymbolicSubareaName(newSymbolicSubareaName);
		} else {
			maintainOffsetExpression(newAreaSpecification, newOffsetPageCount, 
									 newOffsetPercent, newPageCount, 
									 newPercent);
		}
		
		// replace the record's or system owner's area specification with the 
		// new one
		newAreaSpecification.setArea(newArea);
		if (record != null) {			
			record.setAreaSpecification(newAreaSpecification);
		} else {
			systemOwner.setAreaSpecification(newAreaSpecification);
		}
		oldAreaSpecification.setArea(null);
		
		// remove the old area from the schema if needed
		if (oldAreaIndex > -1) {
			schema.getAreas().remove(oldArea);
		}
		
	}
	
	@Override
	public void undo() {
		
		// restore the old area (together with its possible procedure calls) if 
		// it was removed from the schema; insert it at its original position in 
		// the list of areas
		if (oldAreaIndex > -1) {
			schema.getAreas().add(oldAreaIndex, oldArea);
		}
		
		// restore the record's or system owner's area specification at its 
		// original position in the area's list of area specifications and in 
		// the record or system owner
		oldArea.getAreaSpecifications().add(oldAreaSpecificationIndex, 
											oldAreaSpecification);
		if (record != null) {
			record.setAreaSpecification(oldAreaSpecification);
		} else {
			systemOwner.setAreaSpecification(oldAreaSpecification);
		}
		newAreaSpecification.setArea(null);
		
		// finally, remove the new area from the schema if it was created during
		// the execution of this command (the area will contain neither records
		// nor indexes at this point)
		if (newArea.getRecords().isEmpty() && 
			newArea.getIndexes().isEmpty()) {
			
			schema.getAreas().remove(newArea);
		}
		
	}

}