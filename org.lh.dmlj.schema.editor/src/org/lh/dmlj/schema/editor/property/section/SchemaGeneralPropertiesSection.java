/**
 * Copyright (C) 2013  Luc Hermans
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;

public class SchemaGeneralPropertiesSection 
	extends AbstractSchemaPropertiesSection {

	private static final DateFormat MEMO_DATE_FORMAT = 
		new SimpleDateFormat("MM/dd/yy");
	
	public SchemaGeneralPropertiesSection() {
		super();
	}
	
	@Override
	public List<EAttribute> getAttributes() {
		List<EAttribute> attributes = new ArrayList<>();	
		attributes.add(SchemaPackage.eINSTANCE.getSchema_Name());
		attributes.add(SchemaPackage.eINSTANCE.getSchema_Version());
		attributes.add(SchemaPackage.eINSTANCE.getSchema_Description());
		attributes.add(SchemaPackage.eINSTANCE.getSchema_MemoDate());
		return attributes;
	}

	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchema_Name() ||
			attribute == SchemaPackage.eINSTANCE.getSchema_Version() ||
			attribute == SchemaPackage.eINSTANCE.getSchema_Description() ||
			attribute == SchemaPackage.eINSTANCE.getSchema_MemoDate()) {
			
			return target;
		}
		return super.getEditableObject(attribute);
	}
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE.getSchema_Name()) {
			// get the new schema name
			String newSchemaName = (String) newValue;
			// newSchemaName must be a 1- to 8-character value; it must not be 
			// the same (case insensitive) as any components or synonyms within 
			// the schema
			ValidationResult validationResult = 
				NamingConventions.validate(newSchemaName, 
										   NamingConventions.Type.SCHEMA_NAME);
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {
				return new ErrorEditHandler(validationResult.getMessage());
			}
			for (SchemaArea area : target.getAreas()){
				if (area.getName().equalsIgnoreCase(newSchemaName)) {
					String message = "same as area '" + area.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Procedure procedure : target.getProcedures()){
				if (procedure.getName().equalsIgnoreCase(newSchemaName)) {
					String message = 
						"same as procedure '" + procedure.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (SchemaRecord record : target.getRecords()){
				if (record.getName().equalsIgnoreCase(newSchemaName)) {
					String message = 
						"same as record '" + record.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Set set : target.getSets()){
				if (set.getName().equalsIgnoreCase(newSchemaName)) {
					String message = "same as set '" + set.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			// convert the new schema name to uppercase before passing it to the 
			// set attribute command
			return super.getEditHandler(attribute, newSchemaName.toUpperCase());
		} else if (attribute == SchemaPackage.eINSTANCE.getSchema_Version()) {
			// get the new version
			short newVersion = (short) newValue;
			// version must be an unsigned integer in the range 1 through 9999
			if (newVersion < 1 || newVersion > 9999) {
				String message = 
					"must be an unsigned integer in the range 1 through 9999";
				return new ErrorEditHandler(message);
			}
		} else if (attribute == SchemaPackage.eINSTANCE.getSchema_Description()) {
			// get the new description
			String newDescription = (String) newValue;
			// description is a 1- to 40-character alphanumeric field; it is 
			// optional
			if (newDescription != null) {
				if (newDescription.length() < 1 || newDescription.length() > 40) {				
					String message = "not a 1- to 40-character alphanumeric field";
					return new ErrorEditHandler(message);
				} else {
					return super.getEditHandler(attribute, 
												newDescription.toUpperCase());
				}
			} else {
				return super.getEditHandler(attribute, null);
			}
		} else if (attribute == SchemaPackage.eINSTANCE.getSchema_MemoDate()) {
			// get the new memo date (format: mm/dd/yy)
			String newMemoDate = (String) newValue;
			if (newMemoDate != null) {
				String message = 
					"Unparsable date: \"" + newMemoDate + "\" - please enter " +
					"a valid date in the format \"mm/dd/yy\"";
				try {
					// Any yy (year) specified >= 33 will be interpreted as 19yy
					// whereas any yy specified < 33 will be interpreted as 
					// 20yy, so values for the memo date attribute that refer to 
					// 29 February may fail as of 2033...
					Date date = MEMO_DATE_FORMAT.parse(newMemoDate);
					if (!MEMO_DATE_FORMAT.format(date).equals(newMemoDate)) {
						return new ErrorEditHandler(message);
					}
				} catch (ParseException e) {
					return new ErrorEditHandler(message);
				}
			}
			return super.getEditHandler(attribute, newMemoDate);
		}
		return super.getEditHandler(attribute, newValue);
	}

}
