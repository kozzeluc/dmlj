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

import static org.lh.dmlj.schema.SetMembershipOption.MANDATORY_AUTOMATIC;
import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.ADD_ITEM;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.figure.IndexFigure;

@ModelChange(category=ADD_ITEM)
public class CreateIndexCommand extends Command {
	
	private static final String AREA_NAME_SUFFIX = "-AREA";
	private static final String SET_NAME_PREFIX = "NEW-INDEX-";
	
	@Owner 	   private Schema 	  schema;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getSchema_Sets();
	@Item  	   private Set	  	  set;
	
	private SchemaArea 		  area;
	private AreaSpecification areaSpecification;
	private MemberRole 	 	  memberRole;
	private SchemaRecord 	  record;

	public CreateIndexCommand(SchemaRecord record) {
		super("Add index to record");
		this.record = record;
	}	
	
	private String calculateSetName() {
		for (int i = 1; i <= Integer.MAX_VALUE; i++) {
			String setName = SET_NAME_PREFIX + i;
			if (record.getSchema().getSet(setName) == null) {
				String areaName = setName + AREA_NAME_SUFFIX;
				if (record.getSchema().getArea(areaName) == null) {
					return setName;
				}
			}			
		}
		throw new RuntimeException("cannot determine set name");
	}

	@Override
	public void execute() {
		
		schema = record.getSchema();
		
		String setName = calculateSetName(); 		
		String areaName = setName + AREA_NAME_SUFFIX;		
		
		IndexedSetModeSpecification indexedSetModeSpecification =
			SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();		
		indexedSetModeSpecification.setSymbolicIndexName(setName);		
		
		area = SchemaFactory.eINSTANCE.createSchemaArea();
		area.setName(areaName);		
		
		areaSpecification = SchemaFactory.eINSTANCE.createAreaSpecification();		
		
		DiagramLocation recordLocation = record.getDiagramLocation();
		
		DiagramLocation systemOwnerLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();		
		systemOwnerLocation.setEyecatcher("system owner " + setName);
		systemOwnerLocation.setX(recordLocation.getX() - IndexFigure.UNSCALED_WIDTH);
		systemOwnerLocation.setY(recordLocation.getY() - 2 * IndexFigure.UNSCALED_HEIGHT);
		
		SystemOwner systemOwner = SchemaFactory.eINSTANCE.createSystemOwner();
		systemOwner.setAreaSpecification(areaSpecification);
		systemOwner.setDiagramLocation(systemOwnerLocation);
		
		DiagramLocation labelLocation = 
			SchemaFactory.eINSTANCE.createDiagramLocation();		
		labelLocation.setEyecatcher("set label " + setName + " (" + record.getName() +")");
		labelLocation.setX(systemOwnerLocation.getX() + IndexFigure.UNSCALED_WIDTH + 5);
		labelLocation.setY(systemOwnerLocation.getY());
		
		ConnectionLabel connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();		
		connectionLabel.setAlignment(LabelAlignment.LEFT);
		connectionLabel.setDiagramLocation(labelLocation);		
		
		ConnectionPart connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();			
		
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
		
		DiagramData diagramData = record.getSchema().getDiagramData();
				
		record.getSchema().getAreas().add(area);
		area.getAreaSpecifications().add(areaSpecification);
		diagramData.getLocations().add(set.getSystemOwner().getDiagramLocation());
		diagramData.getLocations().add(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().add(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().add(memberRole.getConnectionParts().get(0));
		record.getMemberRoles().add(memberRole);
		record.getSchema().getSets().add(set);		
		
	}
	
	@Override
	public void undo() {
		
		DiagramData diagramData = record.getSchema().getDiagramData();
		
		area.getAreaSpecifications().remove(areaSpecification);
		record.getSchema().getAreas().remove(area);
		diagramData.getLocations().remove(set.getSystemOwner().getDiagramLocation());
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));		
		record.getMemberRoles().remove(memberRole);
		record.getSchema().getSets().remove(set);		
		
	}
	
}