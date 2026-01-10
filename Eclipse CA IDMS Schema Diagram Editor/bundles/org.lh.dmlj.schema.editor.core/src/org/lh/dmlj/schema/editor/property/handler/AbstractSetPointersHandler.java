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
package org.lh.dmlj.schema.editor.property.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.PluginPropertiesCache;
import org.lh.dmlj.schema.editor.command.IModelChangeCommand;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommand;
import org.lh.dmlj.schema.editor.common.Tools;

public abstract class AbstractSetPointersHandler {
	protected static final String LABEL_MEMBER_INDEX_POINTER_POSITION = "label.org.lh.dmlj.schema.MemberRole.indexDbkeyPosition";
	protected static final String LABEL_MEMBER_NEXT_POINTER_POSITION = "label.org.lh.dmlj.schema.MemberRole.nextDbkeyPosition";
	protected static final String LABEL_MEMBER_OWNER_POINTER_POSITION = "label.org.lh.dmlj.schema.MemberRole.ownerDbkeyPosition";
	protected static final String LABEL_MEMBER_PRIOR_POINTER_POSITION = "label.org.lh.dmlj.schema.MemberRole.priorDbkeyPosition";
	protected static final String LABEL_OWNER_NEXT_POINTER_POSITION = "label.org.lh.dmlj.schema.OwnerRole.nextDbkeyPosition";
	protected static final String LABEL_OWNER_PRIOR_POINTER_POSITION = "label.org.lh.dmlj.schema.OwnerRole.priorDbkeyPosition";
	
	protected static final EAttribute MEMBER_INDEX_POINTER_POSITION = SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition();
	protected static final EAttribute MEMBER_NEXT_POINTER_POSITION = SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition();
	protected static final EAttribute MEMBER_OWNER_POINTER_POSITION = SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition();
	protected static final EAttribute MEMBER_PRIOR_POINTER_POSITION = SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition();
	protected static final EAttribute OWNER_NEXT_POINTER_POSITION = SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition();
	protected static final EAttribute OWNER_PRIOR_POINTER_POSITION = SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition();

	private static String getAttributeLabel(EAttribute attribute) {
		String key = null;
		if (attribute == MEMBER_INDEX_POINTER_POSITION) {
			key = LABEL_MEMBER_INDEX_POINTER_POSITION;
		} else if (attribute == MEMBER_NEXT_POINTER_POSITION) {
			key = LABEL_MEMBER_NEXT_POINTER_POSITION;
		} else if (attribute == MEMBER_OWNER_POINTER_POSITION) {
			key = LABEL_MEMBER_OWNER_POINTER_POSITION;
		} else if (attribute == MEMBER_PRIOR_POINTER_POSITION) {
			key = LABEL_MEMBER_PRIOR_POINTER_POSITION;
		} else if (attribute == OWNER_NEXT_POINTER_POSITION) {
			key = LABEL_OWNER_NEXT_POINTER_POSITION;
		} else if (attribute == OWNER_PRIOR_POINTER_POSITION) {
			key = LABEL_OWNER_PRIOR_POINTER_POSITION;
		}
		if (key != null) {
			try {
				return PluginPropertiesCache.get(Plugin.getDefault(), key);
			} catch (MissingResourceException e) {
				Plugin.getDefault().getLog().warn("MissingResourceException: %s".formatted(key));
			}
		}
		return "?";
	}
	
	protected IModelChangeCommand createAppendPointerCommand(SchemaRecord schemaRecord, Role setPointerTarget, EAttribute pointerToAdd) {
		Assert.isTrue(setPointerTarget.eGet(pointerToAdd) == null, "logic error: pointer already set");
		var pointerPosition = Tools.getFirstAvailablePointerPosition(schemaRecord);
		return new SetObjectAttributeCommand(setPointerTarget, pointerToAdd, Short.valueOf(pointerPosition), getAttributeLabel(pointerToAdd));
	}

	protected List<IModelChangeCommand> createShiftPointersCommands(SchemaRecord schemaRecord, Role resetPointerTarget, EAttribute pointerToRemove) {
		Assert.isTrue(resetPointerTarget.eGet(pointerToRemove) != null, "logic error: pointer not set");
		final var marker = ((Short) resetPointerTarget.eGet(pointerToRemove)).shortValue();
		var commands = new ArrayList<IModelChangeCommand>();
		commands.addAll(createShiftPointersCommandsForOwnerRoles(schemaRecord, resetPointerTarget, pointerToRemove, marker));
		commands.addAll(createShiftPointersCommandsForMemberRoles(schemaRecord, resetPointerTarget, pointerToRemove, marker));
		return List.copyOf(commands);
	}
	
	private List<IModelChangeCommand> createShiftPointersCommandsForOwnerRoles(SchemaRecord schemaRecord, Role resetPointerTarget,
			EAttribute pointerToRemove, short marker) {
		
		var commands = new ArrayList<IModelChangeCommand>();
		for (var ownerRole : schemaRecord.getOwnerRoles()) {
			if (ownerRole == resetPointerTarget) {
				var command = new SetObjectAttributeCommand(ownerRole, pointerToRemove, null, getAttributeLabel(pointerToRemove)); 
				commands.add(command);
			}
			if (ownerRole.getNextDbkeyPosition() > marker) {
				var newValue = (short) (ownerRole.getNextDbkeyPosition() - 1); 
				var command = new SetShortAttributeCommand(ownerRole, SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(),
						newValue, getAttributeLabel(OWNER_NEXT_POINTER_POSITION));
				commands.add(command);
			}
			if (ownerRole.getPriorDbkeyPosition() != null && ownerRole.getPriorDbkeyPosition().shortValue() > marker) {
				var newValue = (short) (ownerRole.getPriorDbkeyPosition().shortValue() - 1); 
				var command = new SetObjectAttributeCommand(ownerRole, SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(),
						Short.valueOf(newValue), getAttributeLabel(OWNER_PRIOR_POINTER_POSITION));
				commands.add(command);
			}
		}
		return commands;
	}
	
	private List<IModelChangeCommand> createShiftPointersCommandsForMemberRoles(SchemaRecord schemaRecord, Role resetPointerTarget,
			EAttribute pointerToRemove, short marker) {
		
		var commands = new ArrayList<IModelChangeCommand>();
		for (var memberRole : schemaRecord.getMemberRoles()) {
			if (memberRole == resetPointerTarget) {				
				IModelChangeCommand command = new SetObjectAttributeCommand(memberRole, pointerToRemove, null, getAttributeLabel(pointerToRemove));
				commands.add(command);				
			}
			if (memberRole.getNextDbkeyPosition() != null && memberRole.getNextDbkeyPosition() > marker) {
				var newValue = (short) (memberRole.getNextDbkeyPosition() - 1); 
				var command = new SetObjectAttributeCommand(memberRole, SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(),
						Short.valueOf(newValue), getAttributeLabel(MEMBER_NEXT_POINTER_POSITION));
				commands.add(command);
			}
			if (memberRole.getPriorDbkeyPosition() != null && memberRole.getPriorDbkeyPosition().shortValue() > marker) {
				var newValue = (short) (memberRole.getPriorDbkeyPosition().shortValue() - 1);
				var command = new SetObjectAttributeCommand(memberRole, SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(),
						Short.valueOf(newValue), getAttributeLabel(MEMBER_PRIOR_POINTER_POSITION));
				commands.add(command);
			}
			if (memberRole.getOwnerDbkeyPosition() != null && memberRole.getOwnerDbkeyPosition().shortValue() > marker) {
				var newValue = (short) (memberRole.getOwnerDbkeyPosition().shortValue() - 1);
				var command = new SetObjectAttributeCommand(memberRole, SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(),
						Short.valueOf(newValue), getAttributeLabel(MEMBER_OWNER_POINTER_POSITION));
				commands.add(command);
			}
			if (memberRole.getIndexDbkeyPosition() != null && memberRole.getIndexDbkeyPosition().shortValue() > marker) {
				var newValue = (short) (memberRole.getIndexDbkeyPosition().shortValue() - 1);
				var command = new SetObjectAttributeCommand(memberRole, SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(),
						Short.valueOf(newValue), getAttributeLabel(MEMBER_INDEX_POINTER_POSITION));
				commands.add(command);
			}
		}
		return commands;
	}

}
