/**
 * Copyright (C) 2016  Luc Hermans
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
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLocation;
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
import org.lh.dmlj.schema.ViaSpecification;
import org.lh.dmlj.schema.editor.prefix.Pointer;
import org.lh.dmlj.schema.editor.prefix.PointerDescription;
import org.lh.dmlj.schema.editor.prefix.PrefixFactory;
import org.lh.dmlj.schema.editor.prefix.PrefixUtil;

/**
 * Swapping the record elements for a record might go further than just swapping the record 
 * elements because the SwapRecordElementsCommand only operates on records that have NO keys 
 * defined; this class centralizes command creation for swapping a record's elements and will try
 * to restore as much of the original situation as possible; the one thing that will NOT be restored
 * is the order in which member roles appear for the record whose elements are being swapped (and 
 * that is only impacted in the case of SORTED multiple-member sets).
 */
public abstract class SwapRecordElementsCommandCreationAssistant {
	
	public static IModelChangeCommand getCommand(SchemaRecord record, List<Element> newRootElements) {
		
		List<ModelChangeBasicCommand> commands = new ArrayList<>();	
		List<Element> newAllElements = computeAllNewElements(newRootElements);
		
		// add the commands to remove all keys from the given record; this includes removing the
		// record as a member of multiple-member sets if necessary
		addMakeRecordDirectCommand(commands, record);
		addChangeSetOrderToLastCommands(commands, record);
		addRemoveMemberFromSetCommands(commands, record);
		
		// the swap record elements command will of course always be there
		addSwapRecordElementsCommand(commands, record, newRootElements);
		
		// add the commands to add the record to the multiple-member sets it was removed and to
		// restore all keys if possible (i.e. if a key element is still present after swapping the 
		// record elements, it will be turned into a key element again)
		addAddMemberToSetCommands(commands, record, newAllElements);
		addSetMembershipOptionCommand(commands, record, newAllElements);
		addMoveEndpointCommands(commands, record, newAllElements);
		addCreateBendpointCommands(commands, record, newAllElements);
		addCreateConnectorCommands(commands, record, newAllElements);
		addChangeSetOrderToSortedCommands(commands, record, newAllElements);
		addChangeSortKeysCommands(commands, record, newAllElements);
		addMakeRecordCalcCommand(commands, record, newAllElements);
		addMakeRecordViaCommand(commands, record, newAllElements);
		addChangePointerOrderCommand(commands, record, newAllElements);
		addMoveDiagramNodeCommands(commands, record, newAllElements);
		
		// create a compound command or return the swap record elements command if that is the only
		// command we have
		if (commands.size() > 1) {
			ModelChangeCompoundCommand cc = new ModelChangeCompoundCommand("Edit Record Elements");
			for (ModelChangeBasicCommand command : commands) {
				cc.add(command);
			}
			return cc;
		} else {
			return (IModelChangeCommand) commands.get(0);
		}
	}

	private static List<Element> computeAllNewElements(List<Element> rootElements) {
		List<Element> allElements = new ArrayList<>();
		for (Element newRootElement : rootElements) {
			deepCopyElement(newRootElement, allElements);
		}	
		return allElements;
	}

	private static void deepCopyElement(Element element, List<Element> targetList) {
		targetList.add(element);
		for (Element childElement : element.getChildren()) {
			deepCopyElement(childElement, targetList);
		}
	}

	private static void addMakeRecordDirectCommand(List<ModelChangeBasicCommand> commands, 
												   SchemaRecord record) {		
		if (record.isCalc()) {			
			// if the record is CALC, add a command to make it DIRECT 
			commands.add(new MakeRecordDirectCommand(record));
		}		
	}

	private static List<Element> computeCalcKeyElements(Key existingCalcKey, 
														List<Element> newAllElements) {
		
		List<Element> calcKeyElements = new ArrayList<>();
		for (KeyElement keyElement : existingCalcKey.getElements()) {
			if (isRetained(keyElement.getElement(), newAllElements)) {
				calcKeyElements.add(findRetained(keyElement.getElement(), newAllElements));
			}
		}
		return calcKeyElements;
	}

	private static void addChangeSetOrderToLastCommands(List<ModelChangeBasicCommand> commands, 
														SchemaRecord record) {		
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (!memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted()) {
				// add a command for each sorted single-member set in which the record participates 
				// as a member
				commands.add(new ChangeSetOrderCommand(memberRole.getSet(), SetOrder.LAST));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addRemoveMemberFromSetCommands(List<ModelChangeBasicCommand> commands, 
													   SchemaRecord record) {				
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted()) {
				// add the necessary command(s) for each sorted multiple-member set in which the
				// record participates as a member
				IModelChangeCommand command = 
					DeleteSetOrIndexCommandCreationAssistant.getCommand(memberRole);
				if (command instanceof ModelChangeCompoundCommand) {
					// we need to avoid nested compound commands because the model change dispatcher
					// will not handle them correctly
					ModelChangeCompoundCommand cc = (ModelChangeCompoundCommand) command;
					commands.addAll(cc.getCommands());
				} else {
					commands.add((ModelChangeBasicCommand) command);
				}
			}
		}	
	}

	private static void addSwapRecordElementsCommand(List<ModelChangeBasicCommand> commands, 
													 SchemaRecord record, 
													 List<Element> newRootElements) {
		
		// add the command that will effectively swap the record elements (but needs some
		// preparation and after-care)
		commands.add(new SwapRecordElementsCommand(record, newRootElements));
	}

	private static void addAddMemberToSetCommands(List<ModelChangeBasicCommand> commands, 
												  SchemaRecord record,
												  List<Element> newAllElements) {
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record 
				// participates as a member, provided that at least 1 key element is retained in the
				// new record structure (note that we cannot directly specify the membership option,
				// nor the right sort key if applicable - we do need to specify the applicable
				// pointers but will get the order right in a later command)
				AddMemberToSetCommand command = 
					new AddMemberToSetCommand(memberRole.getSet(), 
											  PrefixUtil.getDefinedPointerTypes(memberRole));
				command.setMemberRecord(record);
				commands.add(command);				
			}
		}
	}

	private static void addSetMembershipOptionCommand(List<ModelChangeBasicCommand> commands, 
													  final SchemaRecord record, 
													  List<Element> newAllElements) {
		
		for (final MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record 
				// participates as a member, provided that at least 1 key element is retained in the
				// new record structure	
				final String setName = memberRole.getSet().getName();
				ISupplier<EObject> eObjectSupplier = new ISupplier<EObject>() {
					@Override
					public EObject supply() {
						return record.getRole(setName);
					}					
				};
				SetObjectAttributeCommand command = 
					new SetObjectAttributeCommand(eObjectSupplier, 
												  SchemaPackage.eINSTANCE.getMemberRole_MembershipOption(), 
												  memberRole.getMembershipOption(), null);
				commands.add(command);				
			}
		}
	}

	private static void addMoveEndpointCommands(List<ModelChangeBasicCommand> commands, 
												final SchemaRecord record,
												List<Element> newAllElements) {
		
		for (final MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add commands for each sorted multiple-member set in which the record participates 
				// as a member, provided that at least 1 key element is retained in the new record 
				// structure				
				DiagramLocation sourceEndpointLocation = memberRole.getConnectionParts()
						  										   .get(0)
						  										   .getSourceEndpointLocation();
				final String setName = memberRole.getSet().getName();
				ISupplier<ConnectionPart> connectionPartSupplier = new ISupplier<ConnectionPart>() {
					@Override
					public ConnectionPart supply() {
						MemberRole futureMemberRole = (MemberRole) record.getRole(setName);
						return futureMemberRole.getConnectionParts().get(0);
					}										
				};
				// source endpoint location
				if (sourceEndpointLocation != null) {						
					MoveEndpointCommand command = 
						new MoveEndpointCommand(connectionPartSupplier,	sourceEndpointLocation.getX(), 
											    sourceEndpointLocation.getY(), true);
					commands.add(command);
				}
				// target endpoint location
				DiagramLocation targetEndpointLocation = 
					memberRole.getConnectionParts()
						  	  .get(memberRole.getConnectionParts().size() - 1)
						  	  .getTargetEndpointLocation();
				if (targetEndpointLocation != null) {
					MoveEndpointCommand command = 
						new MoveEndpointCommand(connectionPartSupplier, targetEndpointLocation.getX(), 
												targetEndpointLocation.getY(), false);
					commands.add(command);
				}				
			}
		}		
	}

	private static void addCreateBendpointCommands(List<ModelChangeBasicCommand> commands, 
												   final SchemaRecord record,
												   List<Element> newAllElements) {
		
		for (final MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add commands for each sorted multiple-member set in which the record participates 
				// as a member, provided that at least 1 key element is retained in the new record 
				// structure				
				final String setName = memberRole.getSet().getName();
				ISupplier<ConnectionPart> connectionPartSupplier = new ISupplier<ConnectionPart>() {
					@Override
					public ConnectionPart supply() {
						MemberRole futureMemberRole = (MemberRole) record.getRole(setName);
						// the future replacement member role will only have 1 connection part to
						// start with; the connectors will be added AFTER the bendpoints are created
						return futureMemberRole.getConnectionParts().get(0);
					}										
				};
				int i = 0;				
				for (ConnectionPart connectionPart : memberRole.getConnectionParts()) {
					for (DiagramLocation bendpointLocation : connectionPart.getBendpointLocations()) {
						CreateBendpointCommand command = 
							new CreateBendpointCommand(connectionPartSupplier, i++, 
													   bendpointLocation.getX(), 
													   bendpointLocation.getY());
						commands.add(command);
					}
				}				
			}
		}
	}

	private static void addCreateConnectorCommands(List<ModelChangeBasicCommand> commands, 
												   final SchemaRecord record,
												   List<Element> newAllElements) {

		for (final MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements) && 
				memberRole.getConnectionParts().size() > 1) {
				
				// add a command for each sorted multiple-member set in which the record 
				// participates as a member, provided that at least 1 key element is retained in the
				// new record structure and that connectors are present
				Connector connector = memberRole.getConnectionParts().get(0).getConnector();
				final String setName = memberRole.getSet().getName();
				ISupplier<MemberRole> memberRoleSupplier = new ISupplier<MemberRole>() {					
					@Override
					public MemberRole supply() {
						return (MemberRole) record.getRole(setName);
					}
				};
				CreateConnectorCommand command = 
					new CreateConnectorCommand(memberRoleSupplier, 
											   new Point(connector.getDiagramLocation().getX(), 
													     connector.getDiagramLocation().getY()));
				commands.add(command);
			}
		}		
	}

	private static void addChangeSetOrderToSortedCommands(List<ModelChangeBasicCommand> commands, 
														  final SchemaRecord record,
		 	   											  List<Element> newAllElements) {

		for (MemberRole memberRole : record.getMemberRoles()) {
			final String setName = memberRole.getSet().getName();
			// the set is SORTED at the moment, in the case of a single-member set it will NOT be 
			// when the supplier's supply method is called during command execution; for multiple-
			// member sets, the set order is NEVER changed but the record is removed as a member in
			// the case of a SORTED multiple-member set and only added again if the sort key can be
			// retained, either partly or in its whole
			ISupplier<Set> setSupplier = new ISupplier<Set>() {
				@Override
				public Set supply() {
					return record.getSchema().getSet(setName);
				}					
			};
			if (!memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() && 
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted single-member set in which the record participates 
				// as a member
				ISortKeyDescription[] sortKeyDescriptions =
					new ISortKeyDescription[] {computeSortKeyDescription(memberRole.getSortKey(), 
																		 newAllElements)};
				commands.add(new ChangeSetOrderCommand(setSupplier, sortKeyDescriptions));
			}
		}
	}	
	
	private static void addChangeSortKeysCommands(List<ModelChangeBasicCommand> commands, 
												  final SchemaRecord record,
				  								  List<Element> newAllElements) {
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			final String setName = memberRole.getSet().getName();
			// the set is SORTED at the moment, in the case of a single-member set it will NOT be 
			// when the supplier's supply method is called during command execution; for multiple-
			// member sets, the set order is NEVER changed but the record is removed as a member in
			// the case of a SORTED multiple-member set and only added again if the sort key can be
			// retained, either partly or in its whole
			ISupplier<Set> setSupplier = new ISupplier<Set>() {
				@Override
				public Set supply() {
					return record.getSchema().getSet(setName);
				}					
			};
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record 
				// participates as a member, provided that at least 1 key element is retained in the
				// new record structure
				List<ISortKeyDescription> sortKeyDescriptions = new ArrayList<>();
				ISortKeyDescription lastSortKeyDescription = null;
				for (MemberRole siblingOrSelf : memberRole.getSet().getMembers()) {
					if (siblingOrSelf == memberRole) { 
						lastSortKeyDescription = 
							computeSortKeyDescription(siblingOrSelf.getSortKey(), newAllElements);
					} else {
						ISortKeyDescription sortKeyDescription = 
							computeSortKeyDescription(siblingOrSelf.getSortKey(), 
												  	  siblingOrSelf.getRecord().getElements());
						sortKeyDescriptions.add(sortKeyDescription);
					}
				}
				// because we're removing and adding members from sets, the order in which sort keys
				// have to be supplied changes...
				Assert.isNotNull(lastSortKeyDescription, "logic error: lastSortKeyDescription is null");
				sortKeyDescriptions.add(lastSortKeyDescription);
				ChangeSortKeysCommand command = 
					new ChangeSortKeysCommand(setSupplier, 
											  sortKeyDescriptions.toArray(new ISortKeyDescription[] {}));
				commands.add(command);
			}
		}		
	}

	private static void addMakeRecordCalcCommand(List<ModelChangeBasicCommand> commands, 
												 SchemaRecord record,
												 List<Element> newAllElements) {
		
		if (record.isCalc() && canRetainInWholeOrPartly(record.getCalcKey(), newAllElements)) {			
			// add a command to make the record CALC again since we can retain the CALC key in its
			// whole or partly
			final List<Element> calcKeyElements = 
				computeCalcKeyElements(record.getCalcKey(), newAllElements);
			ISupplier<List<Element>> calcKeyElementSupplier = new ISupplier<List<Element>>() {				
				@Override
				public List<Element> supply() {
					return calcKeyElements;
				}
			};
			MakeRecordCalcCommand command = 
				new MakeRecordCalcCommand(record, calcKeyElementSupplier, 
										  record.getCalcKey().getDuplicatesOption());
			commands.add(command);
		}
	}
	
	private static void addMakeRecordViaCommand(List<ModelChangeBasicCommand> commands, 
												SchemaRecord record, 
												List<Element> newAllElements) {
		
		if (record.isVia() && record.getViaSpecification().getSet().isMultipleMember() && 
			record.getViaSpecification().getSet().isSorted()) {
			
			ViaSpecification viaSpecification = record.getViaSpecification();
			Set set = viaSpecification.getSet();
			MemberRole memberRole = (MemberRole) record.getRole(set.getName());
			if (canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				// add a command to make the record VIA again; the VIA set is a sorted 
				// multiple-member set for which the sort key can be (partly or completely) retained
				// (if the sort key cannot be retained, the record isn't added as a member to the 
				// set again)
				commands.add(new MakeRecordViaCommand(record, 
													  set.getName(), 
													  viaSpecification.getSymbolicDisplacementName(), 
													  viaSpecification.getDisplacementPageCount()));
			}
		}
	}

	private static void addChangePointerOrderCommand(List<ModelChangeBasicCommand> commands, 
													 final SchemaRecord record, 
													 List<Element> newAllElements) {
		
		// only add a command to restore the order of the pointers in the record's prefix if the 
		// record was removed from AND added again to at least 1 SORTED multiple-member set
		boolean addCommand = false;
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				addCommand = true;
				break;
			}
		}
		if (!addCommand) {
			return;
		}
		
		final List<PointerDescription> desiredOrder = PrefixUtil.getPointerDescriptions(record);		
		ISupplier<List<Pointer<?>>> pointerSupplier = new ISupplier<List<Pointer<?>>>() {
			@Override
			public List<Pointer<?>> supply() {
				List<Pointer<?>> pointers = PrefixFactory.newPrefixForInquiry(record).getPointers();
				PrefixUtil.reorder(pointers, desiredOrder);
				return pointers;
			}			
		};
		
		ChangePointerOrderCommand command = new ChangePointerOrderCommand(record, pointerSupplier);
		commands.add(command);
		
	}

	private static void addMoveDiagramNodeCommands(List<ModelChangeBasicCommand> commands, 
												   final SchemaRecord record, 
												   List<Element> newAllElements) {
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole.getSet().isMultipleMember() && memberRole.getSet().isSorted() &&
				canRetainInWholeOrPartly(memberRole.getSortKey(), newAllElements)) {
				
				// add a command for each sorted multiple-member set in which the record 
				// participates as a member, provided that at least 1 key element is retained in the
				// new record structure (if that is not the case, the record will not be added as a
				// member again)
				final String setName = memberRole.getSet().getName();
				ISupplier<ConnectionLabel> connectionLabelSupplier = new ISupplier<ConnectionLabel>() {
					@Override
					public ConnectionLabel supply() {
						MemberRole futureMemberRole = (MemberRole) record.getRole(setName);
						return futureMemberRole.getConnectionLabel();
					}				
				};
				DiagramLocation diagramLocation = 
					memberRole.getConnectionLabel().getDiagramLocation();
				MoveDiagramNodeCommand command = 
					new MoveDiagramNodeCommand(connectionLabelSupplier, diagramLocation.getX(), 
											   diagramLocation.getY());
				commands.add(command);				
			}
		}		
	}

	private static boolean canRetainInWholeOrPartly(Key existingKey, List<Element> newAllElements) {		
		for (KeyElement existingKeyElement : existingKey.getElements()) {
			Element existingElement = existingKeyElement.getElement();
			if (isRetained(existingElement, newAllElements)) {
				return true;
			}
		}
		return false;
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
		for (Element newElement : newAllElements) {
			if (newElement.getName().equalsIgnoreCase(element.getName())) {
				return newElement;
			}
		}
		throw new IllegalArgumentException("not retained: " + element.getName());
	}

	private static ISortKeyDescription computeSortKeyDescription(final Key existingKey, 
																 final List<Element> newAllElements) {
		
		List<String> elementNameList = new ArrayList<>();
		for (KeyElement keyElement : existingKey.getElements()) {
			if (isRetained(keyElement.getElement(), newAllElements)) {
				elementNameList.add(keyElement.getElement().getName());
			}
		}
		List<SortSequence> sortSequenceList = new ArrayList<>();
		for (KeyElement keyElement : existingKey.getElements()) {
			if (isRetained(keyElement.getElement(), newAllElements)) {
				sortSequenceList.add(keyElement.getSortSequence());
			}
		}
		final SortSequence[] sortSequences = sortSequenceList.toArray(new SortSequence[] {});
		final String[] elementNames = elementNameList.toArray(new String[] {});
		final DuplicatesOption duplicatesOption = existingKey.getDuplicatesOption();
		final boolean compressed = existingKey.isCompressed();
		final boolean naturalSequence = existingKey.isNaturalSequence();
		
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
	
}
