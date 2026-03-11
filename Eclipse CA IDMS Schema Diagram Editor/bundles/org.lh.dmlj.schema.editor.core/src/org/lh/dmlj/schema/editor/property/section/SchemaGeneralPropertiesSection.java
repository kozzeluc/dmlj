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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.handler.EditSchemaCommentsHandler;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;

public class SchemaGeneralPropertiesSection extends AbstractSchemaPropertiesSection {
	private static final List<EAttribute> ATTRIBUTES = List.of(
			SchemaPackage.eINSTANCE.getSchema_Name(),
			SchemaPackage.eINSTANCE.getSchema_Version(),
			SchemaPackage.eINSTANCE.getSchema_Description(),
			SchemaPackage.eINSTANCE.getSchema_MemoDate(),
			SchemaPackage.eINSTANCE.getSchema_Comments());
	
	private final DateFormat memoDateFormat = new SimpleDateFormat("MM/dd/yy");
	private final IHyperlinkHandler<EAttribute, Command> schemaCommentsHandler = new EditSchemaCommentsHandler(this);
	
	private static String getPrettyComments(Schema schema) {
		if (schema.getComments() == null || schema.getComments().isEmpty()) {
			return "";
		} else if (schema.getComments().size() == 1) {
			return schema.getComments().get(0);
		} else {
			return schema.getComments().get(0) + "...";
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {
		return ATTRIBUTES;
	}

	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchema_Name() || attribute == SchemaPackage.eINSTANCE.getSchema_Version() ||
			attribute == SchemaPackage.eINSTANCE.getSchema_Description() || attribute == SchemaPackage.eINSTANCE.getSchema_MemoDate()) {
			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchema_Name()) {
			return getSchemaNameEditHandler(attribute, newValue);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchema_Version()) {
			return getSchemaVersionEditHandler(attribute, newValue);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchema_Description()) {
			return getSchemaDescriptionEditHandler(attribute, newValue);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchema_MemoDate()) {
			return getSchemaMemoDateEditHandler(attribute, newValue);
		} else {
			return super.getEditHandler(attribute, newValue);
		}
	}
	
	private IEditHandler getSchemaNameEditHandler(EAttribute attribute, Object newValue) {
		var newSchemaName = (String) newValue;
		var errorMessage = Stream.of(
				checkAgainstNamingConventions(newSchemaName),
				checkAgainstAreaNames(newSchemaName),
				checkAgainstProcedureNames(newSchemaName),
				checkAgainstRecordNames(newSchemaName),
				checkAgainstSetNames(newSchemaName))
			.flatMap(Function.identity())
			.findFirst();
		if (errorMessage.isEmpty()) {
			return super.getEditHandler(attribute, newSchemaName);
		} else {			
			return new ErrorEditHandler(errorMessage.orElseThrow());
		}
	}
	
	private Stream<String> checkAgainstNamingConventions(String newSchgemaName) {
		var validationResult = NamingConventions.validate(newSchgemaName, NamingConventions.Type.SCHEMA_NAME);	
		if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
			return Stream.of(validationResult.getMessage());
		} else {
			return Stream.empty();
		}
	}
	
	private Stream<String> checkAgainstAreaNames(String newSchemaName) {
		return target.getAreas().stream()
				.filter(area -> area.getName().equalsIgnoreCase(newSchemaName))
				.map(area -> "same as area '" + area.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstProcedureNames(String newSchemaName) {
		return target.getProcedures().stream()
				.filter(procedure -> procedure.getName().equalsIgnoreCase(newSchemaName))
				.map(procedure -> "same as procedure '" + procedure.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstRecordNames(String newSchemaName) {
		return target.getRecords().stream()
				.filter(schemaRecord -> schemaRecord.getName().equalsIgnoreCase(newSchemaName))
				.map(schemaRecord -> "same as record '" + schemaRecord.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private Stream<String> checkAgainstSetNames(String newSchemaName) {
		return target.getSets().stream()
				.filter(set -> set.getName().equalsIgnoreCase(newSchemaName))
				.map(set -> "same as set '" + set.getName() + "'")
				.findFirst()
				.stream();
	}
	
	private IEditHandler getSchemaVersionEditHandler(EAttribute attribute, Object newValue) {
		var newVersion = (short) newValue;
		// version must be an unsigned integer in the range 1 through 9999
		if (newVersion < 1 || newVersion > 9999) {
			return new ErrorEditHandler("must be an unsigned integer in the range 1 through 9999");
		} else {
			return super.getEditHandler(attribute, newValue);
		}
	}
	
	private IEditHandler getSchemaDescriptionEditHandler(EAttribute attribute, Object newValue) {
		var newDescription = (String) newValue;
		// description is a 1- to 40-character alphanumeric field; it is optional
		if (newDescription != null) {
			if (newDescription.isEmpty() || newDescription.length() > 40) {					
				return new ErrorEditHandler("not a 1- to 40-character alphanumeric field");
			} else {
				return super.getEditHandler(attribute, newDescription.toUpperCase());
			}
		} else {
			return super.getEditHandler(attribute, null);
		}		
	}
	
	private IEditHandler getSchemaMemoDateEditHandler(EAttribute attribute, Object newValue) {
		var newMemoDate = (String) newValue; // format: mm/dd/yy
		if (newMemoDate != null) {
			var message = "Unparsable date: \"" + newMemoDate + "\" - please enter a valid date in the format \"mm/dd/yy\"";
			try {
				// Any yy (year) specified >= 33 will be interpreted as 19yy whereas any yy specified < 33 will
				// be interpreted as 20yy, so values for the memo date attribute that refer to 29 February may
				// fail as of 2033...
				var date = memoDateFormat.parse(newMemoDate);
				if (!memoDateFormat.format(date).equals(newMemoDate)) {
					return new ErrorEditHandler(message);
				}
			} catch (ParseException e) {
				return new ErrorEditHandler(message);
			}
		}
		return super.getEditHandler(attribute, newMemoDate);		
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchema_Comments()) {			
			return schemaCommentsHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchema_Comments()) {			
			return getPrettyComments(target);
		} else {
			return super.getValue(attribute);
		}
	}

}
