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

import static org.lh.dmlj.schema.SetMembershipOption.MANDATORY_AUTOMATIC;

import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

public class CreateIndexCommand extends ModelChangeBasicCommand {
	private static final String AREA_NAME_SUFFIX = "-AREA";
	private static final String SET_NAME_PREFIX = "NEW-INDEX-";
	
	private Set	set;
	
	private SchemaArea area;
	private AreaSpecification areaSpecification;
	private MemberRole memberRole;
	private SchemaRecord schemaRecord;

	public CreateIndexCommand(SchemaRecord schemaRecord) {
		super("Add index to record");
		this.schemaRecord = schemaRecord;
	}	
	
	private String calculateSetName() {
		for (var i = 1; i <= Integer.MAX_VALUE; i++) {
			var setName = SET_NAME_PREFIX + i;
			if (schemaRecord.getSchema().getSet(setName) == null) {
				var areaName = setName + AREA_NAME_SUFFIX;
				if (schemaRecord.getSchema().getArea(areaName) == null) {
					return setName;
				}
			}			
		}
		throw new IllegalStateException("cannot determine set name");
	}

	@Override
	public void execute() {
		var setName = calculateSetName(); 		
		var areaName = setName + AREA_NAME_SUFFIX;		
		
		var indexedSetModeSpecification = SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();		
		indexedSetModeSpecification.setSymbolicIndexName(setName);		
		
		area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(areaName);		
		
		areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();		
		
		var recordLocation = schemaRecord.getDiagramLocation();
		
		var systemOwnerLocation = SchemaFactory.eINSTANCE.createDiagramLocation();		
		systemOwnerLocation.setEyecatcher("system owner " + setName);
		systemOwnerLocation.setX(recordLocation.getX() - IndexFigure.UNSCALED_WIDTH);
		systemOwnerLocation.setY(recordLocation.getY() - 2 * IndexFigure.UNSCALED_HEIGHT);
		
		var systemOwner = SchemaFactory.eINSTANCE.createSystemOwner();
		systemOwner.setAreaSpecification(areaSpecification);
		systemOwner.setDiagramLocation(systemOwnerLocation);
		
		var labelLocation = SchemaFactory.eINSTANCE.createDiagramLocation();		
		labelLocation.setEyecatcher("set label " + setName + " (" + schemaRecord.getName() + ")");
		labelLocation.setX(systemOwnerLocation.getX() + IndexFigure.UNSCALED_WIDTH + 5);
		labelLocation.setY(systemOwnerLocation.getY());
		
		var connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();		
		connectionLabel.setAlignment(LabelAlignment.LEFT);
		connectionLabel.setDiagramLocation(labelLocation);		
		
		var connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();			
		
		memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.setConnectionLabel(connectionLabel);
		// no pointers are appended to the member record type's prefix for the new index 			 
		memberRole.setMembershipOption(MANDATORY_AUTOMATIC); 
		
		set = SchemaFactory.eINSTANCE.createSet();
		set.setIndexedSetModeSpecification(indexedSetModeSpecification);
		set.setMode(SetMode.INDEXED);
		set.setName(setName);
		set.setOrder(SetOrder.LAST);		
		set.setSystemOwner(systemOwner);
		set.getMembers().add(memberRole);
		
		redo();
	}
	
	@Override
	public void redo() {
		var diagramData = schemaRecord.getSchema().getDiagramData();
				
		schemaRecord.getSchema().getAreas().add(area);
		area.getAreaSpecifications().add(areaSpecification);
		diagramData.getLocations().add(set.getSystemOwner().getDiagramLocation());
		diagramData.getLocations().add(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().add(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().add(memberRole.getConnectionParts().get(0));
		schemaRecord.getMemberRoles().add(memberRole);
		schemaRecord.getSchema().getSets().add(set);
	}
	
	@Override
	public void undo() {
		var diagramData = schemaRecord.getSchema().getDiagramData();
		
		area.getAreaSpecifications().remove(areaSpecification);
		schemaRecord.getSchema().getAreas().remove(area);
		diagramData.getLocations().remove(set.getSystemOwner().getDiagramLocation());
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));		
		schemaRecord.getMemberRoles().remove(memberRole);
		schemaRecord.getSchema().getSets().remove(set);
	}
	
}