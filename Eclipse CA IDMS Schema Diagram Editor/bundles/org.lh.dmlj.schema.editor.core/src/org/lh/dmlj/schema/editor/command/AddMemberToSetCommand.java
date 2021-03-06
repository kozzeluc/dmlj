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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramData;
import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaFactory;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.figure.RecordFigure;
import org.lh.dmlj.schema.editor.prefix.PointerType;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixForPointerAppendage;

public class AddMemberToSetCommand extends AbstractSortKeyManipulationCommand {

	protected Set set;
	private MemberRole memberRole;
			
	protected SchemaRecord 			  memberRecord;
	private PrefixForPointerAppendage memberPrefix;
	private DiagramData	    		  diagramData;
	protected PointerType[] pointerTypes = 
		{PointerType.MEMBER_NEXT, PointerType.MEMBER_PRIOR, PointerType.MEMBER_OWNER};	
	
	public AddMemberToSetCommand(Set set) {
		super(set, null);
		Assert.isNotNull(set, "set is null");
		Assert.isTrue(set.getMode() == SetMode.CHAINED, "set is NOT chained");		
		this.set = set;
		this.diagramData = set.getSchema().getDiagramData();				
		setLabel("Add member record type to set " + set.getName());
	}
	
	public AddMemberToSetCommand(Set set, PointerType[] pointerTypes) {
		this(set);
		List<PointerType> filteredPointerTypes = new ArrayList<>();
		boolean nextPointerOk = false;
		for (PointerType pointerType : pointerTypes) {
			if (pointerType == PointerType.MEMBER_NEXT) {
				nextPointerOk = true;
			} else if (pointerType != PointerType.MEMBER_PRIOR &&
					   pointerType != PointerType.MEMBER_OWNER) {
				
				throw new IllegalArgumentException("invalid pointer type: " + pointerType);
			}
			filteredPointerTypes.add(pointerType);
		}
		if (!nextPointerOk) {
			throw new IllegalArgumentException("NEXT pointer is mandatory");
		}
		this.pointerTypes = filteredPointerTypes.toArray(new PointerType[] {});
	}
	
	@Override
	public void execute() {	
		
		Assert.isNotNull(memberRecord, "member not set");		
		
		memberRole = SchemaFactory.eINSTANCE.createMemberRole();
		memberRole.setMembershipOption(SetMembershipOption.MANDATORY_AUTOMATIC);		
		
		if (set.getOrder() == SetOrder.SORTED) {
			// we only supply a sort key description for the new member record type; we do need to
			// supply it at the correct position in the array though
			sortKeyDescriptions = new ISortKeyDescription[set.getMembers().size() + 1];
			sortKeyDescriptions[set.getMembers().size()] = new SortKeyDescription(memberRecord);
			prepareSortKey(memberRecord, set.getMembers().size(), 0);
		}
		
		ConnectionPart connectionPart = SchemaFactory.eINSTANCE.createConnectionPart();
		connectionPart.setMemberRole(memberRole);
		
		DiagramLocation labelLocation = SchemaFactory.eINSTANCE.createDiagramLocation();
		labelLocation.setEyecatcher("set label " + set.getName() + " (" + memberRecord.getName() +
									")");
		// TODO make calculating the label location more intelligent
		SchemaRecord ownerRecord = set.getOwner().getRecord();
		labelLocation.setX(ownerRecord.getDiagramLocation().getX() + RecordFigure.UNSCALED_WIDTH + 5);
		labelLocation.setY(ownerRecord.getDiagramLocation().getY());
		
		ConnectionLabel connectionLabel = SchemaFactory.eINSTANCE.createConnectionLabel();
		connectionLabel.setMemberRole(memberRole);
		connectionLabel.setDiagramLocation(labelLocation);
		
		redo();		
				
	}
	
	public Set getSet() {
		return set;
	}
	
	@Override
	public void redo() {		
		
		memberRole.setRecord(memberRecord);	
		memberRole.setSet(set);
		
		if (memberPrefix == null) {
			memberPrefix = 
				PrefixFactory.newPrefixForPointerAppendage(memberRole, pointerTypes);			
		}
		memberPrefix.appendPointers();
		
		if (set.getOrder() == SetOrder.SORTED) {
			restoreSortKey(memberRecord, set.getMembers().size() - 1, 0);			
		}
		
		diagramData.getConnectionParts().add(memberRole.getConnectionParts().get(0));
		diagramData.getConnectionLabels().add(memberRole.getConnectionLabel());
		diagramData.getLocations().add(memberRole.getConnectionLabel().getDiagramLocation());				
	}
	
	public void setMemberRecord(SchemaRecord memberRecord) {
		this.memberRecord = memberRecord;		
	}
	
	@Override
	public void undo() {
		if (set.getOrder() == SetOrder.SORTED) {			
			removeSortKey(memberRole);			
		}
		memberRole.setRecord(null);	
		memberRole.setSet(null);
		diagramData.getConnectionParts().remove(memberRole.getConnectionParts().get(0));
		diagramData.getConnectionLabels().remove(memberRole.getConnectionLabel());
		diagramData.getLocations().remove(memberRole.getConnectionLabel().getDiagramLocation());	
	}
	
	private static class SortKeyDescription implements ISortKeyDescription {

		private SchemaRecord memberRecord;

		private SortKeyDescription(SchemaRecord memberRecord) {
			super();
			this.memberRecord = memberRecord;
		}
		
		@Override
		public String[] getElementNames() {
			return new String[] {Tools.getDefaultSortKeyElement(memberRecord).getName()};
		}

		@Override
		public SortSequence[] getSortSequences() {
			return new SortSequence[] {SortSequence.ASCENDING};
		}

		@Override
		public DuplicatesOption getDuplicatesOption() {
			return DuplicatesOption.NOT_ALLOWED;
		}

		@Override
		public boolean isCompressed() {
			return false;
		}

		@Override
		public boolean isNaturalSequence() {
			return false;
		}
		
	}
	
}
