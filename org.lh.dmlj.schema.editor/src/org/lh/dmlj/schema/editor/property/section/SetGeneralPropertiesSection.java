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
package org.lh.dmlj.schema.editor.property.section;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Procedure;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.common.ValidationResult;
import org.lh.dmlj.schema.editor.property.handler.ErrorEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.SetOrderHandler;

public class SetGeneralPropertiesSection extends AbstractSetPropertiesSection {
	
	private IHyperlinkHandler<EAttribute, Command> setOrderHandler = new SetOrderHandler(this);

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getSet_Name(),
		 SchemaPackage.eINSTANCE.getSet_Mode(),
		 SchemaPackage.eINSTANCE.getSet_Order()};

	public SetGeneralPropertiesSection() {
		super();
	}	
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name() ||
			attribute == SchemaPackage.eINSTANCE.getSet_Mode() ||
			attribute == SchemaPackage.eINSTANCE.getSet_Order()) {
			
			return set;
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
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
			// get the new set name
			String newSetName = 
				newValue != null ? ((String) newValue).toUpperCase() : null;
			// validate the set name
			ValidationResult validationResult = 
				NamingConventions.validate(newSetName, 
										   NamingConventions.Type.SET_NAME);			
			if (validationResult.getStatus() == ValidationResult.Status.ERROR) {				
				return new ErrorEditHandler(validationResult.getMessage());
			}
			// newSetName must not be the same as the schema name or the name of
			// any other component (including synonyms) within the schema
			if (newSetName.equalsIgnoreCase(set.getSchema().getName())) {
				String message = "same as schema name";
				return new ErrorEditHandler(message);
			}
			for (SchemaArea area : set.getSchema().getAreas()){
				if (area.getName().equalsIgnoreCase(newSetName)) {
					String message = "same as area '" + area.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Procedure procedure : set.getSchema().getProcedures()){
				if (procedure.getName().equalsIgnoreCase(newSetName)) {
					String message = 
						"same as procedure '" + procedure.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (SchemaRecord record : set.getSchema().getRecords()){
				if (record.getName().equalsIgnoreCase(newSetName)) {					
					String message = 
						"same as record '" + record.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			for (Set aSet : set.getSchema().getSets()){
				if (aSet != set &&
					aSet.getName().equalsIgnoreCase(newSetName)) {
					String message = "same as set '" + aSet.getName() + "'";
					return new ErrorEditHandler(message);
				}
			}
			// pass the new set name to the set attribute command
			return super.getEditHandler(attribute, newSetName);			
		} else {
			return super.getEditHandler(attribute, newValue);
		}
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
			// remove the trailing underscore from the set name if we're dealing 
			// with a DDLCATLOD set
			return Tools.removeTrailingUnderscore(set.getName());			
		} else {
			return super.getValue(attribute);
		}
	}

}
