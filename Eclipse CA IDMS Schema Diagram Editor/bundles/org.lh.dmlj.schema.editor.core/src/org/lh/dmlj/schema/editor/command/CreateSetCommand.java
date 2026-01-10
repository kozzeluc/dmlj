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

import java.util.stream.IntStream;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerAppendage;

public class CreateSetCommand extends ModelChangeBasicCommand {
	private static final String SET_NAME_PREFIX = "NEW-SET-";

	private final SchemaRecord owner;
	private final SetMode mode;
	
	private Schema schema;
	private Set	set;
	private SchemaRecord member;
	private PrefixForPointerAppendage memberPrefix;	
	
	private PrefixForPointerAppendage ownerPrefix;
	
	private MemberRole memberRole;
	private OwnerRole ownerRole;

	public CreateSetCommand(SchemaRecord owner, SetMode mode) {
		super(mode == SetMode.CHAINED ? "Create chained set" : "Create user owned indexed set");
		this.owner = owner;
		this.mode = mode;
	}
	
	private String calculateSetName() {
		return IntStream.range(1, Integer.MAX_VALUE)
				.mapToObj(i -> SET_NAME_PREFIX + i)
				.filter(setName -> schema.getSet(setName) == null)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("cannot determine set name"));
	}	

	@Override
	public void execute() {
		schema = owner.getSchema();
		Assert.isNotNull(member, "member not set");
				
		set = SchemaFactory.eINSTANCE.createSet();
		set.setName(calculateSetName());
		set.setMode(mode);
		set.setOrder(SetOrder.LAST);
		
		if (mode == SetMode.INDEXED) {
			IndexedSetModeSpecification indexedSetModeSpecification = SchemaFactory.eINSTANCE.createIndexedSetModeSpecification();		
			indexedSetModeSpecification.setSymbolicIndexName(set.getName());
			set.setIndexedSetModeSpecification(indexedSetModeSpecification);
		}
		
		ownerRole = SchemaFactory.eINSTANCE.createOwnerRole();
		ownerRole.setSet(set);
		
		memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		memberRole.setSet(set);
		memberRole.setMembershipOption(SetMembershipOption.MANDATORY_AUTOMATIC);
		
		var connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();
		connectionPart.setMemberRole(memberRole);
		
		var labelLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		labelLocation.setEyecatcher("set label " + set.getName() + " (" + member.getName() +")");
		labelLocation.setX(owner.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5);
		labelLocation.setY(owner.getDiagramLocation().getY());
		
		var connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();
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
		if (ownerPrefix == null) {
			ownerPrefix = PrefixFactory.newPrefixForPointerAppendage(ownerRole, PointerType.OWNER_NEXT, PointerType.OWNER_PRIOR);
		}
		ownerPrefix.appendPointers();
		
		memberRole.setRecord(member);
		if (memberPrefix == null) {
			if (mode == SetMode.CHAINED) {
				memberPrefix = PrefixFactory.newPrefixForPointerAppendage(memberRole, PointerType.MEMBER_NEXT, 
						PointerType.MEMBER_PRIOR, PointerType.MEMBER_OWNER);
			} else {
				memberPrefix = PrefixFactory.newPrefixForPointerAppendage(memberRole, PointerType.MEMBER_INDEX, 
						PointerType.MEMBER_OWNER);			
			}
		}
		memberPrefix.appendPointers();
		
		schema.getDiagramData().getConnectionParts().add(memberRole.getConnectionParts().get(0));
		schema.getDiagramData().getConnectionLabels().add(memberRole.getConnectionLabel());
		schema.getDiagramData().getLocations().add(memberRole.getConnectionLabel().getDiagramLocation());
	}
	
	public void setMemberRecord(SchemaRecord member) {
		this.member = member;
	}

	@Override
	public void undo() {
		ownerRole.setRecord(null);
		
		set.setSchema(null);
		
		ownerPrefix.reset();
		ownerRole.setRecord(null);
		
		memberPrefix.reset();
		memberRole.setRecord(null);
		
		schema.getDiagramData().getConnectionParts().remove(memberRole.getConnectionParts().get(0));
		schema.getDiagramData().getConnectionLabels().remove(memberRole.getConnectionLabel());
		schema.getDiagramData().getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());		
	}

}