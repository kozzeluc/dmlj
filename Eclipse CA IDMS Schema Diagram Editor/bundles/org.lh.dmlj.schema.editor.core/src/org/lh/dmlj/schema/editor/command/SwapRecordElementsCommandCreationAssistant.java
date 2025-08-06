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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.Key;
import org.lh.dmlj.schema.KeyElement;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetOrder;
import org.lh.dmlj.schema.SortSequence;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;

/**
 * Swapping the record elements for a record might go further than just swapping the record elements because the
 * SwapRecordElementsCommand only operates on records that have NO keys defined; this class centralizes command
 * creation for swapping a record's elements and will try to restore as much of the original situation as possible;
 * the one thing that will NOT be restored is the order in which member roles appear for the record whose elements
 * are being swapped (and that is only impacted in the case of SORTED multiple-member sets).
 */
public class SwapRecordElementsCommandCreationAssistant {
	
	public static IModelChangeCommand getCommand(SchemaRecord schemaRecord, List<Element> newRootElements) {
		var commands = new ArrayList<ModelChangeBasicCommand>();	
		var newAllElements = computeAllNewElements(newRootElements);
		
		// add the commands to remove all keys from the given record; this includes removing the record as a
		// member of multiple-member sets if necessary
		addMakeRecordDirectCommand(commands, schemaRecord);
		addChangeSetOrderToLastCommands(commands, schemaRecord);
		addRemoveMemberFromSetCommands(commands, schemaRecord);
		
		// the swap record elements command will of course always be there
		addSwapRecordElementsCommand(commands, schemaRecord, newRootElements);
		
		// add the commands to add the record to the multiple-member sets it was removed and to restore all keys
		// if possible (i.e. if a key element is still present after swapping the record elements, it will be
		// turned into a key element again)
		addAddMemberToSetCommands(commands, schemaRecord, newAllElements);
		addSetMembershipOptionCommand(commands, schemaRecord, newAllElements);
		addMoveEndpointCommands(commands, schemaRecord, newAllElements);
		addCreateBendpointCommands(commands, schemaRecord, newAllElements);
		addCreateConnectorCommands(commands, schemaRecord, newAllElements);
		addChangeSetOrderToSortedCommands(commands, schemaRecord, newAllElements);
		addChangeSortKeysCommands(commands, schemaRecord, newAllElements);
		addMakeRecordCalcCommand(commands, schemaRecord, newAllElements);
		addMakeRecordViaCommand(commands, schemaRecord, newAllElements);
		addChangePointerOrderCommand(commands, schemaRecord, newAllElements);
		addMoveDiagramNodeCommands(commands, schemaRecord, newAllElements);
		
		// create a compound command or return the swap record elements command if that is the only command we have
		if (commands.size() > 1) {
			var cc = new ModelChangeCompoundCommand("Edit Record Elements");
			for (var command : commands) {
				cc.add(command);
			}
			return cc;
		} else {
			return commands.get(0);
		}
	}

	private static List<Element> computeAllNewElements(List<Element> rootElements) {
		var allElements = new ArrayList<Element>();
		for (var newRootElement : rootElements) {
			deepCopyElement(newRootElement, allElements);
		}	
		return allElements;
	}

	private static void deepCopyElement(Element element, List<Element> targetList) {
		targetList.add(element);
		for (var childElement : element.getChildren()) {
			deepCopyElement(childElement, targetList);
		}
	}

	private static void addMakeRecordDirectCommand(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord) {		
		if (schemaRecord.isCalc()) { 
			commands.add(new MakeRecordDirectCommand(schemaRecord));
		}		
	}

	private static List<Element> computeCalcKeyElements(Key existingCalcKey, List<Element> newAllElements) {
		var calcKeyElements = new ArrayList<Element>();
		for (var keyElement : existingCalcKey.getElements()) {
			if (isRetained(keyElement.getElement(), newAllElements)) {
				calcKeyElements.add(findRetained(keyElement.getElement(), newAllElements));
			}
		}
		return calcKeyElements;
	}

	private static void addChangeSetOrderToLastCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord) {		
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (!memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted()) {
				// add a command for each sorted single-member set in which the record participates as a member
				commands.add(new ChangeSetOrderCommand(memberRole.getSet(), SetOrder.LAST));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addRemoveMemberFromSetCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord) {				
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted()) {
				// add the necessary command(s) for each sorted multiple-member set in which the record
				// participates as a member
				var command = DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
				if (command instanceof ModelChangeCompoundCommand cc) {
					// we need to avoid nested compound commands because the model change dispatcher will not
					// handle them correctly
					commands.addAll((Collection<? extends ModelChangeBasicCommand>) cc.getCommands());
				} else {
					commands.add((ModelChangeBasicCommand) command);
				}
			}
		}	
	}

	private static void addSwapRecordElementsCommand(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newRootElements) {
		// add the command that will effectively swap the record elements (but needs some preparation and after-care)
		commands.add(new SwapRecordElementsCommand(schemaRecord, newRootElements));
	}

	private static void addAddMemberToSetCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure (note that we
				// cannot directly specify the membership option, nor the right sort key if applicable - we do
				// need to specify the applicable pointers but will get the order right in a later command)
				var command = new AddMemberToSetCommand(memberRole.getSet(), PrefixUtil.getDefinedPointerTypes(memberRole));
				command.setMemberRecord(schemaRecord);
				commands.add(command);				
			}
		}
	}

	private static void addSetMembershipOptionCommand(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {		
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure	
				var setName = memberRole.getSet().getName();
				Supplier<EObject> eObjectSupplier = () -> schemaRecord.getRole(setName);
				var command =  new SetObjectAttributeCommand(eObjectSupplier, SchemaPackage.eINSTANCE.getMemberRole_MembershipOption(),
						memberRole.getMembershipOption(), null);
				commands.add(command);				
			}
		}
	}

	private static void addMoveEndpointCommands(List<ModelChangeBasicCommand> commands, final SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add commands for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure				
				var sourceEndpointLocation = memberRole.getConnectionParts().get(0).getSourceEndpointLocation();
				var setName = memberRole.getSet().getName();
				Supplier<ConnectionPart> connectionPartSupplier = () -> {	
					var futureMemberRole = (MemberRole) schemaRecord.getRole(setName);
					return futureMemberRole.getConnectionParts().get(0);
				};
				// source endpoint location
				if (sourceEndpointLocation != null) {						
					var command = new MoveEndpointCommand(connectionPartSupplier,	sourceEndpointLocation.getX(),
							sourceEndpointLocation.getY(), true);
					commands.add(command);
				}
				// target endpoint location
				var targetEndpointLocation =
						memberRole.getConnectionParts().get(memberRole.getConnectionParts().size() - 1).getTargetEndpointLocation();
				if (targetEndpointLocation != null) {
					var command = new MoveEndpointCommand(connectionPartSupplier, targetEndpointLocation.getX(),
							targetEndpointLocation.getY(), false);
					commands.add(command);
				}				
			}
		}		
	}

	private static void addCreateBendpointCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add commands for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure				
				var setName = memberRole.getSet().getName();
				Supplier<ConnectionPart> connectionPartSupplier = () -> {	
					var futureMemberRole = (MemberRole) schemaRecord.getRole(setName);
					// the future replacement member role will only have 1 connection part to start with; the
					// connectors will be added AFTER the bendpoints are created
					return futureMemberRole.getConnectionParts().get(0);
				};
				var i = 0;				
				for (var connectionPart : memberRole.getConnectionParts()) {
					for (var bendpointLocation : connectionPart.getBendpointLocations()) {
						var command = new CreateBendpointCommand(connectionPartSupplier, i++,
								bendpointLocation.getX(), bendpointLocation.getY());
						commands.add(command);
					}
				}				
			}
		}
	}

	private static void addCreateConnectorCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements) &&
					memberRole.getConnectionParts().size() > 1) {
				
				// add a command for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure and that
				// connectors are present
				var connector = memberRole.getConnectionParts().get(0).getConnector();
				var setName = memberRole.getSet().getName();
				Supplier<MemberRole> memberRoleSupplier = () -> (MemberRole) schemaRecord.getRole(setName);
				var command = new CreateConnectorCommand(memberRoleSupplier,
						new Point(connector.getDiagramLocation().getX(), connector.getDiagramLocation().getY()));
				commands.add(command);
			}
		}		
	}

	private static void addChangeSetOrderToSortedCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			var setName = memberRole.getSet().getName();
			// the set is SORTED at the moment, in the case of a single-member set it will NOT be when the
			// supplier's supply method is called during command execution; for multiple-member sets, the set
			// order is NEVER changed but the record is removed as a member in the case of a SORTEDmultiple-member
			// set and only added again if the sort key can be retained, either partly or in itswhole
			Supplier<Set> setSupplier = () -> schemaRecord.getSchema().getSet(setName);
			if (!memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted single-member set in which the record participates as a member
				var sortKeyDescriptions =
					new ISortKeyDescription[] { computeSortKeyDescription(memberRole.getSortKey(), newAllElements) };
				commands.add(new ChangeSetOrderCommand(setSupplier, sortKeyDescriptions));
			}
		}
	}	
	
	private static void addChangeSortKeysCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			var setName = memberRole.getSet().getName();
			// the set is SORTED at the moment, in the case of a single-member set it will NOT be when the
			// supplier's supply method is called during command execution; for multiple-member sets, the set
			// order is NEVER changed but the record is removed as a member in the case of a SORTED multiple-member
			// set and only added again if the sort key can be retained, either partly or in its whole
			Supplier<Set> setSupplier = () -> schemaRecord.getSchema().getSet(setName);
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure
				var sortKeyDescriptions = new ArrayList<ISortKeyDescription>();
				ISortKeyDescription lastSortKeyDescription = null;
				for (var siblingOrSelf : memberRole.getSet().getMembers()) {
					if (siblingOrSelf == memberRole) { 
						lastSortKeyDescription = computeSortKeyDescription(siblingOrSelf.getSortKey(), newAllElements);
					} else {
						ISortKeyDescription sortKeyDescription = computeSortKeyDescription(siblingOrSelf.getSortKey(),
								siblingOrSelf.getRecord().getElements());
						sortKeyDescriptions.add(sortKeyDescription);
					}
				}
				// because we're removing and adding members from sets, the order in which sort keys have to be
				// supplied changes...
				Assert.isNotNull(lastSortKeyDescription, "logic error: lastSortKeyDescription is null");
				sortKeyDescriptions.add(lastSortKeyDescription);
				var command = new ChangeSortKeysCommand(setSupplier, sortKeyDescriptions.toArray(new ISortKeyDescription[] {}));
				commands.add(command);
			}
		}		
	}

	private static void addMakeRecordCalcCommand(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		if (schemaRecord.isCalc() && canRetainInWholeOrPartly(schemaRecord.getCalcKey(), newAllElements)) {			
			// add a command to make the record CALC again since we can retain the CALC key in its whole or partly
			var calcKeyElements = computeCalcKeyElements(schemaRecord.getCalcKey(), newAllElements);
			Supplier<List<Element>> calcKeyElementSupplier = () -> calcKeyElements;
			var command = new MakeRecordCalcCommand(schemaRecord, calcKeyElementSupplier, schemaRecord.getCalcKey().getDuplicatesOption());
			commands.add(command);
		}
	}
	
	private static void addMakeRecordViaCommand(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		if (schemaRecord.isVia() && schemaRecord.getViaSpecification().getSet().isMultipleMember() &&
				schemaRecord.getViaSpecification().getSet().isSorted()) {
			
			var viaSpecification = schemaRecord.getViaSpecification();
			var set = viaSpecification.getSet();
			var memberRole = (MemberRole) schemaRecord.getRole(set.getName());
			if (canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				// add a command to make the record VIA again; the VIA set is a sorted multiple-member set for
				// which the sort key can be (partly or completely) retained (if the sort key cannot be retained,
				// the record isn't added as a member to the set again)
				commands.add(new MakeRecordViaCommand(schemaRecord, set.getName(), viaSpecification.getSymbolicDisplacementName(),
						viaSpecification.getDisplacementPageCount()));
			}
		}
	}

	private static void addChangePointerOrderCommand(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		// only add a command to restore the order of the pointers in the record's prefix if the record was
		// removed from AND added again to at least 1 SORTED multiple-member set
		var addCommand = schemaRecord.getMemberRoles().stream()
				.anyMatch(memberRole -> memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
						canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements));
		if (!addCommand) {
			return;
		}
		
		var desiredOrder = PrefixUtil.getPointerDescriptions(schemaRecord);		
		Supplier<List<Pointer<?>>> pointerSupplier = () -> {
			var pointers = PrefixFactory.newPrefixForInquiry(schemaRecord).getPointers();
			PrefixUtil.reorder(pointers, desiredOrder);
			return pointers;
		};
		
		commands.add(new ChangePointerOrderCommand(schemaRecord, pointerSupplier));
	}

	private static void addMoveDiagramNodeCommands(List<ModelChangeBasicCommand> commands, SchemaRecord schemaRecord, List<Element> newAllElements) {
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
					canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record participates as a member,
				// provided that at least 1 key element is retained in the new record structure (if that is not
				// the case, the record will not be added as a member again)
				var setName = memberRole.getSet().getName();
				Supplier<ConnectionLabel> connectionLabelSupplier = () -> {
					MemberRole futureMemberRole = (MemberRole) schemaRecord.getRole(setName);
					return futureMemberRole.getConnectionLabel();
				};
				var diagramLocation = memberRole.getConnectionLabel().getDiagramLocation();
				var command = new MoveDiagramNodeCommand(connectionLabelSupplier, diagramLocation.getX(), diagramLocation.getY());
				commands.add(command);				
			}
		}		
	}

	private static boolean canRetainInWholeOrPartly(Key existingKey, List<Element> newAllElements) {
		return existingKey.getElements().stream()
				.map(KeyElement::getElement)
				.anyMatch(existingElement -> isRetained(existingElement, newAllElements));
	}

	private static boolean isRetained(Element element, List<Element> newAllElements) {
		try {
			findRetained(element, newAllElements);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private static Element findRetained(Element element, List<Element> newAllElements) {
		return newAllElements.stream()
				.filter(newElement -> newElement.getName().equalsIgnoreCase(element.getName()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("not retained: " + element.getName()));
	}

	private static ISortKeyDescription computeSortKeyDescription(Key existingKey, List<Element> newAllElements) {
		var elementNameList = existingKey.getElements().stream()
				.filter(keyElement -> isRetained(keyElement.getElement(), newAllElements))
				.map(KeyElement::getElement)
				.map(Element::getName)
				.toList();
		var sortSequenceList = existingKey.getElements().stream()
				.filter(keyElement -> isRetained(keyElement.getElement(), newAllElements))
				.map(KeyElement::getSortSequence)
				.toList();
		var sortSequences = sortSequenceList.toArray(new SortSequence[] {});
		var elementNames = elementNameList.toArray(new String[] {});
		var duplicatesOption = existingKey.getDuplicatesOption();
		var compressed = existingKey.isCompressed();
		var naturalSequence = existingKey.isNaturalSequence();
		return new ISortKeyDescription() {
	
			@Override
			public String[] getElementNames() {
				return elementNames;
			}
	
			@Override
			public SortSequence[] getSortSequences() {
				return sortSequences;
			}
	
			@Override
			public DuplicatesOption getDuplicatesOption() {
				return duplicatesOption;
			}
	
			@Override
			public boolean isCompressed() {
				return compressed;
			}
	
			@Override
			public boolean isNaturalSequence() {
				return naturalSequence;
			}
			
		};
	}
	
	private SwapRecordElementsCommandCreationAssistant() {
	}
	
}
