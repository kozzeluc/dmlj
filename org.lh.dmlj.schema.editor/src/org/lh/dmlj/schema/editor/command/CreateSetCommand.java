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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.ADD_ITEM;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

@ModelChange(category=ADD_ITEM)
public class CreateSetCommand extends Command {
	
	private static final String SET_NAME_PREFIX = "NEW-SET-";

	@Owner 	   private Schema 	  schema;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getSchema_Sets();
	@Item  	   private Set	  	  set;
	
	private SchemaRecord member;
	private SetMode 	 mode;
	private SchemaRecord owner;
	
	private MemberRole 		memberRole;
	private OwnerRole  		ownerRole;
	private DiagramLocation labelLocation;

	public CreateSetCommand(SchemaRecord owner, SetMode mode) {
		super(mode == SetMode.CHAINED ? "Create chained set" : "Create user owned indexed set");
		this.owner = owner;
		this.mode = mode;
	}
	
	private String calculateSetName() {
		for (int i = 1; i <= Integer.MAX_VALUE; i++) {
			String setName = SET_NAME_PREFIX + i;
			if (schema.getSet(setName) == null) {				
				return setName;				
			}			
		}
		throw new RuntimeException("cannot determine set name");
	}	

	@Override
	public void execute() {
				
		schema = owner.getSchema();
		Assert.isNotNull(member, "member not set");
				
		set = SchemaFactory.eINSTANCE.createSet();
		set.setName(calculateSetName());
		set.setMode(mode);
		set.setOrder(SetOrder.LAST);
		
		ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		ownerRole.setSet(set);
		ownerRole.setNextDbkeyPosition(Tools.getFirstAvailablePointerPosition(owner));
		ownerRole.setPriorDbkeyPosition((short) (ownerRole.getNextDbkeyPosition() + 1));
		
		memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		memberRole.setSet(set);
		memberRole.setMembershipOption(SetMembershipOption.MANDATORY_AUTOMATIC);
		if (mode == SetMode.CHAINED) {
			memberRole.setNextDbkeyPosition(Tools.getFirstAvailablePointerPosition(member));
			memberRole.setPriorDbkeyPosition((short) (memberRole.getNextDbkeyPosition() + 1));
			memberRole.setOwnerDbkeyPosition((short) (memberRole.getPriorDbkeyPosition() + 1));
		} else {
			memberRole.setIndexDbkeyPosition((short) (Tools.getFirstAvailablePointerPosition(member)));
			memberRole.setOwnerDbkeyPosition((short) (memberRole.getIndexDbkeyPosition() + 1));			
		}
		
		ConnectionPart connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();
		connectionPart.setMemberRole(memberRole);
		
		labelLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		labelLocation.setEyecatcher("set label " + set.getName() + " (" + member.getName() +")");
		// TODO make calculating the label location more intelligent
		labelLocation.setX(owner.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5);
		labelLocation.setY(owner.getDiagramLocation().getY());
		
		ConnectionLabel connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();
		connectionLabel.setMemberRole(memberRole);
		connectionLabel.setDiagramLocation(labelLocation);
		
		redo();
		
	}

	public final SchemaRecord getOwner() {
		return owner;
	}

	@Override
	public void redo() {
		
		ownerRole.setRecord(owner);
		
		set.setSchema(schema);
		
		ownerRole.setRecord(owner);
		
		memberRole.setRecord(member);
		
		schema.getDiagramData().getLocations().add(labelLocation);
		
	}
	
	public void setMemberRecord(SchemaRecord member) {
		this.member = member;
	}

	@Override
	public void undo() {
	
		ownerRole.setRecord(null);
		
		set.setSchema(null);
		
		ownerRole.setRecord(null);
		
		memberRole.setRecord(null);
		
		schema.getDiagramData().getLocations().remove(labelLocation);		
		
	}

}