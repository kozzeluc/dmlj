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

import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;

public class DeleteIndexCommand extends Command {	
	
	private SchemaArea	 area;
	private DiagramData  diagramData;
	private MemberRole 	 memberRole;
	private SchemaRecord record;
	private Set 		 set;
	private SystemOwner	 systemOwner;
	
	private int areaInSchemaIndex;
	private int areaSpecificationIndex;
	private int systemOwnerDiagramLocationIndex;
	private int connectionLabelLocationIndex;
	private int connectionLabelIndex;
	private int connectionPartIndex;
	private int memberRoleIndex;
	private int setInSchemaIndex;

	public DeleteIndexCommand(SystemOwner systemOwner) {
		super("Delete index");
		this.systemOwner = systemOwner;
	}		

	@Override
	public void execute() {
					
		set = systemOwner.getSet();
		memberRole = systemOwner.getSet().getMembers().get(0);
		record = memberRole.getRecord();
		diagramData = record.getSchema().getDiagramData();
		area = systemOwner.getAreaSpecification().getArea();
			
		areaInSchemaIndex = record.getSchema().getAreas().indexOf(area);
		areaSpecificationIndex = 
			area.getAreaSpecifications().indexOf(systemOwner.getAreaSpecification());
		systemOwnerDiagramLocationIndex = 
			diagramData.getLocations().indexOf(set.getSystemOwner().getDiagramLocation());
		connectionLabelLocationIndex = 
			diagramData.getLocations().indexOf(memberRole.getConnectionLabel().getDiagramLocation());
		connectionLabelIndex = diagramData.getConnectionLabels().indexOf(memberRole.getConnectionLabel());
		connectionPartIndex = 
			diagramData.getConnectionParts().indexOf(memberRole.getConnectionParts().get(0));
		memberRoleIndex = record.getMemberRoles().indexOf(memberRole);
		setInSchemaIndex = record.getSchema().getSets().indexOf(set);
		
		redo();
		
	}
	
	@Override
	public void redo() {				
		
		area.getAreaSpecifications().remove(systemOwner.getAreaSpecification());
		if (area.getAreaSpecifications().isEmpty()) {
			record.getSchema().getAreas().remove(area);
		}
		diagramData.getLocations().remove(set.getSystemOwner().getDiagramLocation());
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));		
		record.getMemberRoles().remove(memberRole);
		record.getSchema().getSets().remove(set); 
		
	}

	@Override
	public void undo() {		
		
		if (area.getSchema() == null) {
			record.getSchema().getAreas().add(areaInSchemaIndex, area);
		}		
		area.getAreaSpecifications().add(areaSpecificationIndex, 
										 systemOwner.getAreaSpecification()); 
		diagramData.getLocations().add(systemOwnerDiagramLocationIndex, 
									   set.getSystemOwner().getDiagramLocation()); 
		diagramData.getLocations().add(connectionLabelLocationIndex, 
									   memberRole.getConnectionLabel().getDiagramLocation()); 
		diagramData.getConnectionLabels().add(connectionLabelIndex, 
											  memberRole.getConnectionLabel());
		diagramData.getConnectionParts().add(connectionPartIndex, 
											 memberRole.getConnectionParts().get(0));
		record.getMemberRoles().add(memberRoleIndex, memberRole);
		record.getSchema().getSets().add(setInSchemaIndex, set);		
		
	}
	
}