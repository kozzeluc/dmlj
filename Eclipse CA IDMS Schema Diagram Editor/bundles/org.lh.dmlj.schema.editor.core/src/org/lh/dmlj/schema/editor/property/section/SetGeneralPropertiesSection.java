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
package org.lh.dmlj.schema.editor.property.section;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.SetOrderHandler;

public class SetGeneralPropertiesSection extends AbstractSetPropertiesSection {
	private IHyperlinkHandler<EAttribute, Command> setOrderHandler = new SetOrderHandler(this);

	private static final List<EAttribute> ATTRIBUTES = List.of(
			SchemaPackage.eINSTANCE.getSet_Name(),
			SchemaPackage.eINSTANCE.getSet_Mode(),
			SchemaPackage.eINSTANCE.getSet_Order());
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name() || attribute == SchemaPackage.eINSTANCE.getSet_Mode() ||
			attribute == SchemaPackage.eINSTANCE.getSet_Order()) {
			
			return set;
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return ATTRIBUTES;
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			return set;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			var newSetName = newValue != null ? ((String) newValue).toUpperCase() : null;
			var errorMessage = Stream.of(
					checkAgainstNamingConventions(newSetName),
					checkAgainstSchemaName(newSetName),
					checkAgainstAreaNames(newSetName),
					checkAgainstProcedureNames(newSetName),
					checkAgainstRecordNames(newSetName),
					checkAgainstSetNames(newSetName))
				.flatMap(Function.identity())
				.findFirst();
			if (errorMessage.isEmpty()) {
				return super.getEditHandler(attribute, newSetName);
			} else {				
				return new ErrorEditHandler(errorMessage.orElseThrow());
			}
		} else {
			return super.getEditHandler(attribute, newValue);
		}
	}
	
	private Stream<String> checkAgainstNamingConventions(String newSetName) {
		var validationResult = NamingConventions.validate(newSetName, NamingConventions.Type.SET_NAME);	
		if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
			return Stream.of(validationResult.getMessage());
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<String> checkAgainstSchemaName(String newSetName) {
		if (set.getSchema().getName().equalsIgnoreCase(newSetName)) {
			return Stream.of("same as schema name");
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<String> checkAgainstAreaNames(String newSetName) {
		return set.getSchema().getAreas().stream()
				.filter(area -> area.getName().equalsIgnoreCase(newSetName))
				.map(area -> "same as area '" + area.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstProcedureNames(String newSetName) {
		return set.getSchema().getProcedures().stream()
				.filter(procedure -> procedure.getName().equalsIgnoreCase(newSetName))
				.map(procedure -> "same as procedure '" + procedure.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstRecordNames(String newSetName) {
		return set.getSchema().getRecords().stream()
				.filter(schemaRecord -> schemaRecord != target && schemaRecord.getName().equalsIgnoreCase(newSetName))
				.map(schemaRecord -> "same as record '" + schemaRecord.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstSetNames(String newSetName) {
		return set.getSchema().getSets().stream()
				.filter(aSet -> aSet != set && aSet.getName().equalsIgnoreCase(newSetName))
				.map(aSet -> "same as set '" + aSet.getName() + "'")
				.findFirst()
				.stream();
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Order()) {
			return setOrderHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}

	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {	
			// remove the trailing underscore from the set name if we're dealing with a DDLCATLOD set
			return Tools.removeTrailingUnderscore(set.getName());			
		} else {
			return super.getValue(attribute);
		}
	}

}
