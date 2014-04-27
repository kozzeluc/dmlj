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

import static org.lh.dmlj.schema.editor.command.annotation.ModelChangeCategory.REMOVE_ITEM;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EReference;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.editor.command.annotation.Item;
import org.lh.dmlj.schema.editor.command.annotation.ModelChange;
import org.lh.dmlj.schema.editor.command.annotation.Owner;
import org.lh.dmlj.schema.editor.command.annotation.Reference;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerRemoval;

@ModelChange(category=REMOVE_ITEM)
public class RemoveMemberFromSetCommand extends AbstractSortKeyManipulationCommand {
		
	@Owner 	   private Set 		  set;
	@Reference private EReference reference = SchemaPackage.eINSTANCE.getSet_Members();
	@Item 	   private MemberRole memberRole;
	
	private DiagramData  			diagramData;
	private SchemaRecord 			memberRecord;
	private PrefixForPointerRemoval prefix;
	
	private int memberRoleInRecordIndex;
	private int memberRoleInSetIndex;
	private int connectionLabelLocationIndex;
	private int connectionLabelIndex;
	private int connectionPartIndex;

	public RemoveMemberFromSetCommand(MemberRole memberRole) {
		super(memberRole.getSet());
		this.memberRole = memberRole;
		set = memberRole.getSet();
		setLabel("Remove member record type from set " + 
				  Tools.removeTrailingUnderscore(memberRole.getSet().getName()));
	}
	
	@Override
	public void execute() {
		
		diagramData = set.getSchema().getDiagramData();
		memberRecord = memberRole.getRecord();
		
		if (set.getOrder() == SetOrder.SORTED) {
			stashSortKeys(0);
		}
		
		// gather all the different (re-)insertion indexes
		memberRoleInRecordIndex = memberRecord.getMemberRoles().indexOf(memberRole);
		memberRoleInSetIndex = set.getMembers().indexOf(memberRole);
		connectionLabelLocationIndex = 
			diagramData.getLocations().indexOf(memberRole.getConnectionLabel().getDiagramLocation());
		connectionLabelIndex = 
			diagramData.getConnectionLabels().indexOf(memberRole.getConnectionLabel());
		connectionPartIndex = 
			diagramData.getConnectionParts().indexOf(memberRole.getConnectionParts().get(0)); 
		
		// make sure we can keep the member record's prefix in a consistent state (we're only 
		// expecting chained sets here because this command is to be used only for multiple-member
		// sets		
		List<PointerType> pointersToRemove = new ArrayList<>();		
		pointersToRemove.add(PointerType.MEMBER_NEXT); // mandatory pointer for chained sets		
		if (memberRole.getPriorDbkeyPosition() != null) {
			pointersToRemove.add(PointerType.MEMBER_PRIOR);
		}
		if (memberRole.getOwnerDbkeyPosition() != null) {
			pointersToRemove.add(PointerType.MEMBER_OWNER);
		}
		PointerType[] pointersToRemoveAsArray = pointersToRemove.toArray(new PointerType[] {});		
		prefix = PrefixFactory.newPrefixForPointerRemoval(memberRole, pointersToRemoveAsArray);
		
		redo();
		
	}
	
	@Override
	public void redo() {
		
		Assert.isTrue(memberRole.getSet().getMembers().size() > 1, "not a multiple-member set: " + 
					  set.getName() + " (" + memberRole.getRecord().getName() + ")");
		Assert.isTrue(memberRole.getRecord().getViaSpecification() == null ||
					  memberRole.getRecord().getViaSpecification().getSet() != set, 
					  "cannot remove record from its VIA set");
		Assert.isTrue(memberRole.getConnectionParts().size() == 1, "connectors not removed");
		Assert.isTrue(memberRole.getConnectionParts().get(0).getBendpointLocations().isEmpty(), 
					  "bendpoints not removed");
		
		// remove the key if applicable
		if (set.getOrder() == SetOrder.SORTED) {
			removeSortKey(memberRole);
		}
		
		// take care of the member record's prefix
		prefix.removePointers();
		
		// decouple the member role from both the record and set
		memberRole.setRecord(null);
		memberRole.setSet(null);

		// remove the diagram location of the connection label		
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());
		
		// remove the connection label
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		
		// remove the (one and only) connection part
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));						
		
	}
	
	@Override
	public void undo() {
		
		// reattach the member role to both the record and set
		memberRecord.getMemberRoles().add(memberRoleInRecordIndex, memberRole);
		set.getMembers().add(memberRoleInSetIndex, memberRole);
		
		// take care of the member record's prefix
		prefix.reset();		
		
		diagramData.getLocations().add(connectionLabelLocationIndex, 
									   memberRole.getConnectionLabel().getDiagramLocation());
		diagramData.getConnectionLabels().add(connectionLabelIndex, 
											  memberRole.getConnectionLabel());
		diagramData.getConnectionParts().add(connectionPartIndex, 
											 memberRole.getConnectionParts().get(0));
		
		// restore the key if applicable
		if (set.getOrder() == SetOrder.SORTED) {
			restoreSortKey(memberRole.getRecord(), memberRoleInSetIndex, 0);
		}
		
	}
	
}
