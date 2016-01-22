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

import static org.lh.dmlj.schema.SetMembershipOption.MANDATORY_AUTOMATIC;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.LabelAlignment;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.VsamIndexFigure;

public class CreateVsamIndexCommand extends ModelChangeBasicCommand {
	
	private static final String SET_NAME_PREFIX = "NEW-VSAM-INDEX-";
	
	private MemberRole memberRole;
	private SchemaRecord record;
	private Element element;

	public CreateVsamIndexCommand(SchemaRecord record) {
		super("Add VSAM index to record");
		this.record = record;
	}	
	
	private String calculateSetName() {
		for (int i = 1; i <= Integer.MAX_VALUE; i++) {
			String setName = SET_NAME_PREFIX + i;
			if (record.getSchema().getSet(setName) == null) {
				return setName;
			}			
		}
		throw new RuntimeException("cannot determine set name");
	}

	@Override
	public void execute() {
		
		element = Tools.getDefaultSortKeyElement(record);
		Assert.isNotNull(element, "VSAM indexes are always SORTED and no sort element is " +
						 "available: " + record.getName());
		
		String setName = calculateSetName(); 				
				
		DiagramLocation recordLocation = record.getDiagramLocation();
		
		DiagramLocation vsamIndexLocation = SchemaFactory.eINSTANCE.createDiagramLocation();		
		vsamIndexLocation.setEyecatcher("VSAM index " + setName);
		vsamIndexLocation.setX(recordLocation.getX() - VsamIndexFigure.UNSCALED_WIDTH);
		vsamIndexLocation.setY(recordLocation.getY() - 2 * VsamIndexFigure.UNSCALED_HEIGHT);
		
		VsamIndex vsamIndex = SchemaFactory.eINSTANCE.createVsamIndex();
		vsamIndex.setDiagramLocation(vsamIndexLocation);
		
		DiagramLocation labelLocation = SchemaFactory.eINSTANCE.createDiagramLocation();		
		labelLocation.setEyecatcher("set label " + setName + " (" + record.getName() +")");
		labelLocation.setX(vsamIndexLocation.getX() + VsamIndexFigure.UNSCALED_WIDTH + 5);
		labelLocation.setY(vsamIndexLocation.getY());
		
		ConnectionLabel connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();		
		connectionLabel.setAlignment(LabelAlignment.LEFT);
		connectionLabel.setDiagramLocation(labelLocation);		
		
		ConnectionPart connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();			
		
		memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		memberRole.getConnectionParts().add(connectionPart);
		memberRole.setConnectionLabel(connectionLabel);
		memberRole.setMembershipOption(MANDATORY_AUTOMATIC); 
		
		Set set = SchemaFactory.eINSTANCE.createSet();
		set.setMode(SetMode.VSAM_INDEX);
		set.setName(setName);
		set.setOrder(SetOrder.SORTED);		
		set.setVsamIndex(vsamIndex);
		set.getMembers().add(memberRole);
		
		Key key = SchemaFactory.eINSTANCE.createKey();		
		key.setMemberRole(memberRole);
		key.setDuplicatesOption(DuplicatesOption.NOT_ALLOWED);
		
		KeyElement keyElement = SchemaFactory.eINSTANCE.createKeyElement();
		keyElement.setKey(key);
		keyElement.setElement(element);
		keyElement.setSortSequence(SortSequence.ASCENDING);
		
		redo();
		
	}
	
	@Override
	public void redo() {
		
		DiagramData diagramData = record.getSchema().getDiagramData();
						
		diagramData.getLocations().add(memberRole.getSet().getVsamIndex().getDiagramLocation());
		diagramData.getLocations().add(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().add(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().add(memberRole.getConnectionParts().get(0));
		
		record.getSchema().getSets().add(memberRole.getSet());
		
		record.getMemberRoles().add(memberRole);			
		record.getKeys().add(memberRole.getSortKey());
		
		element.getKeyElements().add(memberRole.getSortKey().getElements().get(0));
	}
	
	@Override
	public void undo() {
		
		DiagramData diagramData = record.getSchema().getDiagramData();
		
		diagramData.getLocations().remove(memberRole.getSet().getVsamIndex().getDiagramLocation());
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));		
		
		record.getSchema().getSets().remove(memberRole.getSet());	
		
		record.getMemberRoles().remove(memberRole);
		record.getKeys().remove(memberRole.getSortKey());
		
		element.getKeyElements().remove(memberRole.getSortKey().getElements().get(0));
	}
	
}