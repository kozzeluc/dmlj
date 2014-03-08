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
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.property.filter.IEnumFilter;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.SetOrderHandler;

public class SetSortedPropertiesSection extends AbstractSetPropertiesSection {

	private IHyperlinkHandler setOrderHandler = new SetOrderHandler(this);
	
	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getKey_ElementSummary(),
		 SchemaPackage.eINSTANCE.getKey_DuplicatesOption()};
	
	public SetSortedPropertiesSection() {
		super();	
	}	
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary() ||
			attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			
			return target.getSortKey();
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
			String key = "description.member.set.sorted.elementSummary";
			return getPluginProperty(key);			
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			String key = "description.member.set.sorted.duplicatesOption";
			return getPluginProperty(key);			
		} else {
			return super.getDescription(attribute);
		}
	}	
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getSortKey();
		} else {
			return super.getEditableObject(attribute);
		}
	}	
	
	@Override
	public IEnumFilter<? extends Enum<?>> getEnumFilter(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			// by DBkey: For MODE IS INDEX sets only
			return new IEnumFilter<DuplicatesOption>() {
				@Override
				public boolean include(EAttribute attribute, DuplicatesOption duplicatesOption) {
					
					return duplicatesOption != DuplicatesOption.BY_DBKEY ||
						   set.getMode() == SetMode.INDEXED;
				}
			};
		} else {
			return super.getEnumFilter(attribute);
		}
	}
	
	@Override
	public IHyperlinkHandler getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return setOrderHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	

}
