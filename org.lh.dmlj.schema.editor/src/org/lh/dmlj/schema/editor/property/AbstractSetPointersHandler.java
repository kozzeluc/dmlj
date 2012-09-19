package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.Role;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.command.SetObjectAttributeCommand;
import org.lh.dmlj.schema.editor.command.SetShortAttributeCommand;
import org.lh.dmlj.schema.editor.common.Tools;

public abstract class AbstractSetPointersHandler {
	
	protected static String LABEL_MEMBER_INDEX_POINTER_POSITION = 
		"label.org.lh.dmlj.schema.MemberRole.indexDbkeyPosition";
	protected static String LABEL_MEMBER_NEXT_POINTER_POSITION = 
		"label.org.lh.dmlj.schema.MemberRole.nextDbkeyPosition";
	protected static String LABEL_MEMBER_OWNER_POINTER_POSITION = 
		"label.org.lh.dmlj.schema.MemberRole.ownerDbkeyPosition";
	protected static String LABEL_MEMBER_PRIOR_POINTER_POSITION = 
		"label.org.lh.dmlj.schema.MemberRole.priorDbkeyPosition";
	protected static String LABEL_OWNER_NEXT_POINTER_POSITION = 
		"label.org.lh.dmlj.schema.OwnerRole.nextDbkeyPosition";
	protected static String LABEL_OWNER_PRIOR_POINTER_POSITION = 
		"label.org.lh.dmlj.schema.OwnerRole.priorDbkeyPosition";	
	
	protected static EAttribute MEMBER_INDEX_POINTER_POSITION = 
		SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition();
	protected static EAttribute MEMBER_NEXT_POINTER_POSITION = 
		SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition();
	protected static EAttribute MEMBER_OWNER_POINTER_POSITION = 
		SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition();
	protected static EAttribute MEMBER_PRIOR_POINTER_POSITION = 
		SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition();
	protected static EAttribute OWNER_NEXT_POINTER_POSITION = 
		SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition();
	protected static EAttribute OWNER_PRIOR_POINTER_POSITION = 
		SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition();

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
				return Plugin.getDefault()
							 .getPluginProperties()
							 .getString(key);
			} catch (MissingResourceException e) {
				return "?";
			}
		} else {
			return "?";
		}
	}
	
	protected Command createAppendPointerCommand(SchemaRecord record, 
												 Role setPointerTarget,
												 EAttribute pointerToAdd) {
				
		Assert.isTrue(setPointerTarget.eGet(pointerToAdd) == null, 
											"logic error: pointer already set");
		short pointerPosition = Tools.getFirstAvailablePointerPosition(record);
		Command command = 
			new SetObjectAttributeCommand(setPointerTarget, pointerToAdd, 
										  Short.valueOf(pointerPosition), 
										  getAttributeLabel(pointerToAdd)); 
		return command;		
	}

	protected List<Command> createShiftPointersCommands(SchemaRecord record, 
														Role resetPointerTarget,
														EAttribute pointerToRemove) {
				
		Assert.isTrue(resetPointerTarget.eGet(pointerToRemove) != null, 
					  "logic error: pointer not set");
		
		List<Command> commands = new ArrayList<>();
		
		short marker = 
			((Short) resetPointerTarget.eGet(pointerToRemove)).shortValue();
		for (OwnerRole ownerRole : record.getOwnerRoles()) {
			if (ownerRole == resetPointerTarget) {
				Command command =
					new SetObjectAttributeCommand(ownerRole, pointerToRemove, 
												  null, 
												  getAttributeLabel(pointerToRemove)); 
				commands.add(command);
			}
			if (ownerRole.getNextDbkeyPosition() > marker) {
				short newValue = (short) (ownerRole.getNextDbkeyPosition() - 1); 
				Command command =
					new SetShortAttributeCommand(ownerRole, 
												 SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition(), 
												 newValue, 
												 getAttributeLabel(OWNER_NEXT_POINTER_POSITION)); 
				commands.add(command);
			}
			if (ownerRole.getPriorDbkeyPosition() != null &&
				ownerRole.getPriorDbkeyPosition().shortValue() > marker) {
				
				short newValue = 
					(short) (ownerRole.getPriorDbkeyPosition().shortValue() - 1); 
				Command command =
					new SetObjectAttributeCommand(ownerRole, 
												  SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition(), 
												  Short.valueOf(newValue), 
												  getAttributeLabel(OWNER_PRIOR_POINTER_POSITION)); 
				commands.add(command);
			}
		}
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			if (memberRole == resetPointerTarget) {				
				Command command =
					new SetObjectAttributeCommand(memberRole, pointerToRemove, 
												  null, 
												  getAttributeLabel(pointerToRemove)); 
				commands.add(command);				
			}
			if (memberRole.getNextDbkeyPosition() != null &&
				memberRole.getNextDbkeyPosition() > marker) {
				
				short newValue = 
					(short) (memberRole.getNextDbkeyPosition() - 1); 
				Command command =
					new SetObjectAttributeCommand(memberRole, 
												 SchemaPackage.eINSTANCE.getMemberRole_NextDbkeyPosition(), 
												 Short.valueOf(newValue), 
												 getAttributeLabel(MEMBER_NEXT_POINTER_POSITION)); 
				commands.add(command);
			}
			if (memberRole.getPriorDbkeyPosition() != null &&
				memberRole.getPriorDbkeyPosition().shortValue() > marker) {
				
				short newValue = 
					(short) (memberRole.getPriorDbkeyPosition().shortValue() - 1); 
				Command command =
					new SetObjectAttributeCommand(memberRole, 
												  SchemaPackage.eINSTANCE.getMemberRole_PriorDbkeyPosition(), 
												  Short.valueOf(newValue), 
												  getAttributeLabel(MEMBER_PRIOR_POINTER_POSITION)); 
				commands.add(command);
			}
			if (memberRole.getOwnerDbkeyPosition() != null &&
				memberRole.getOwnerDbkeyPosition().shortValue() > marker) {
				
				short newValue = (short) (memberRole.getOwnerDbkeyPosition()
													.shortValue() - 1); 
				Command command =
					new SetObjectAttributeCommand(memberRole, 
												  SchemaPackage.eINSTANCE.getMemberRole_OwnerDbkeyPosition(), 
												  Short.valueOf(newValue), 
												  getAttributeLabel(MEMBER_OWNER_POINTER_POSITION)); 
				commands.add(command);
			}
			if (memberRole.getIndexDbkeyPosition() != null &&
				memberRole.getIndexDbkeyPosition().shortValue() > marker) {
				
				short newValue = (short) (memberRole.getIndexDbkeyPosition()
													.shortValue() - 1); 
				Command command =
					new SetObjectAttributeCommand(memberRole, 
												  SchemaPackage.eINSTANCE.getMemberRole_IndexDbkeyPosition(), 
												  Short.valueOf(newValue), 
												  getAttributeLabel(MEMBER_INDEX_POINTER_POSITION)); 
				commands.add(command);
			}
		}
		
		return commands;
	}

}