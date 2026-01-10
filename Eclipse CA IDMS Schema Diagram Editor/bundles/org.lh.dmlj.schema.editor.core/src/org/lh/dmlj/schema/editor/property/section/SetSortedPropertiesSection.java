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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.property.filter.IEnumFilter;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.SetOrderHandler;

public class SetSortedPropertiesSection extends AbstractSetPropertiesSection {
	private final IHyperlinkHandler<EAttribute, Command> setOrderHandler = new SetOrderHandler(this);
	
	private static final List<EAttribute> ATTRIBUTES = List.of( 
			SchemaPackage.eINSTANCE.getKey_ElementSummary(),
			SchemaPackage.eINSTANCE.getKey_DuplicatesOption());
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary() || attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return target.getSortKey();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		return ATTRIBUTES;
	}
	
	@Override
	public String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary()) {
			return getPluginProperty("description.member.set.sorted.elementSummary");			
		} else if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return getPluginProperty("description.member.set.sorted.duplicatesOption");			
		} else {
			return super.getDescription(attribute);
		}
	}	
	
	@Override
	public IEnumFilter getEnumFilter(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			// by DBkey: For MODE IS INDEX sets only
			return (a, d) -> d != DuplicatesOption.BY_DBKEY || set.getMode() == SetMode.INDEXED;
		} else {
			return super.getEnumFilter(attribute);
		}
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getKey_ElementSummary() || attribute == SchemaPackage.eINSTANCE.getKey_DuplicatesOption()) {
			return setOrderHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	

}
