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

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.LocationModeHandler;

public class RecordCalcPropertiesSection 
	extends AbstractRecordPropertiesSection {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getKey_ElementSummary(),
		 SchemaPackage.eINSTANCE.getKey_DuplicatesOption()};
	
	private IHyperlinkHandler 		  locationModeHandler = 
		new LocationModeHandler(this);	

	public RecordCalcPropertiesSection() {
		super();
	}	
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary() ||
			attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			
			return target.getCalcKey();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return Arrays.asList(ATTRIBUTES);
	}
	
	@Override
	public String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return getPluginProperty("description.calc.record.properties.elementSummary");
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return getPluginProperty("description.calc.record.properties.duplicatesOption");
		} else {
			return super.getDescription(attribute);
		}
	}	
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getCalcKey();
		} else {
			return super.getEditableObject(attribute);
		}
	}
	
	@Override
	public IHyperlinkHandler getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return locationModeHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	
	
}
