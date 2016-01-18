/**
 * Copyright (C) 2015  Luc Hermans
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
import org.lh.dmlj.schema.IndexedSetModeSpecification;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.property.IIndexedSetModeSpecificationProvider;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.IndexedSetModeSpecificationHandler;

public class SetIndexSection 
	extends AbstractSetPropertiesSection implements IIndexedSetModeSpecificationProvider {

	private static final EAttribute[] ATTRIBUTES = 
		{SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_SymbolicIndexName(),
		 SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_KeyCount(),
		 SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_DisplacementPageCount()};
	
	private IHyperlinkHandler<EAttribute, Command> indexHandler = 
		new IndexedSetModeSpecificationHandler(this);
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_SymbolicIndexName() ||
			attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_KeyCount() ||
			attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_DisplacementPageCount()) {
			
			return target.getSet().getIndexedSetModeSpecification();
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
		if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_SymbolicIndexName()) {
			return "Specifies the name of a symbol representing the index";
		} else if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_KeyCount()) {
			return "Establishes the number of entries in each bottom-level index record (SR8 system record)";
		} else if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_DisplacementPageCount()) {
			return "Indicates how far away from their owners the bottom level index records are to be stored";
		} else {
			return super.getDescription(attribute);
		}
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_SymbolicIndexName() ||
			attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_KeyCount() ||
			attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_DisplacementPageCount()) {
			
			return indexHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}
	
	@Override
	public IndexedSetModeSpecification getIndexedSetModeSpecification() {
		return target.getSet().getIndexedSetModeSpecification();
	}

	@Override
	public String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_SymbolicIndexName()) {
			return "Symbolic index name";
		} else if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_KeyCount()) {
			return "Index block key count";
		} else if (attribute == SchemaPackage.eINSTANCE.getIndexedSetModeSpecification_DisplacementPageCount()) {
			return "DISplacement";
		} else {
			return super.getLabel(attribute);
		}
	}

}
