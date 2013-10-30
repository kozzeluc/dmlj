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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.property.IAreaSpecificationProvider;
import org.lh.dmlj.schema.editor.property.IMemberRoleProvider;
import org.lh.dmlj.schema.editor.property.handler.AreaHandler;
import org.lh.dmlj.schema.editor.property.handler.ChainedSetPointersHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.IndexedSetPointersHandler;

public class SetOwnerPropertiesSection 
	extends AbstractSetPropertiesSection 
	implements IAreaSpecificationProvider, IMemberRoleProvider {

	private IHyperlinkHandler areaHandler = new AreaHandler(this);
	private IHyperlinkHandler chainedSetPointersHandler = 
		new ChainedSetPointersHandler(this);
	private IHyperlinkHandler indexedSetPointersHandler = 
		new IndexedSetPointersHandler(this);
	
	public SetOwnerPropertiesSection() {
		super();	
	}	
	
	@Override
	public AreaSpecification getAreaSpecification() {
		if (set.getSystemOwner() != null) {
			return set.getSystemOwner().getAreaSpecification();
		} else {
			return set.getOwner().getRecord().getAreaSpecification();
		}
	}

	@Override
	public String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			String key;
			if (set.getSystemOwner() != null) {
				key = "description.owner.set.properties.system.owner";
			} else {
				key = "description.owner.set.properties.record";
			}
			return getPluginProperty(key);
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return getPluginProperty("description.owner.set.properties.area");
		} else {
			return super.getDescription(attribute);
		}
	}
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			// we need to override the getValue(EAttribute) method for this 
			// attribute as well, in order to deal with system owned sets
			return target.getSet().getOwner().getRecord();
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			if (set.getSystemOwner() == null) {
				// user owned set
				return set.getOwner()
						  .getRecord()
						  .getAreaSpecification()
						  .getArea();
			} else {
				// system owned indexed set
				return set.getSystemOwner()
						  .getAreaSpecification()
						  .getArea();
			}
		} else if (attribute == SchemaPackage.eINSTANCE
											 .getOwnerRole_NextDbkeyPosition() ||
				   attribute == SchemaPackage.eINSTANCE
				   							 .getOwnerRole_PriorDbkeyPosition()) {
			
			return set.getOwner();
		} else {		
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getSystemOwner() == null) {		
			attributes.add(SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition());			
			attributes.add(SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition());			
		}		
		return attributes;
	}
	
	@Override
	public IHyperlinkHandler getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return areaHandler;
		} else if (set.getMode() == SetMode.CHAINED &&
				   (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition() ||
					attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition())) {
			
			return chainedSetPointersHandler;
		} else if (set.getMode() == SetMode.INDEXED &&
				   (attribute == SchemaPackage.eINSTANCE.getOwnerRole_NextDbkeyPosition() ||
					attribute == SchemaPackage.eINSTANCE.getOwnerRole_PriorDbkeyPosition())) {
			
			return indexedSetPointersHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	
	
	@Override
	public String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("label.owner.set.properties.area");
		}
		return super.getLabel(attribute);
	}
	
	@Override
	public MemberRole getMemberRole() {
		return target;
	}

	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			if (set.getSystemOwner() != null) {
				return "SYSTEM";				
			} else {
				// remove the trailing underscore from the record name if we're 
				// dealing with a DDLCATLOD owner record
				return Tools.removeTrailingUnderscore(target.getSet()
						  									.getOwner()
						  									.getRecord()
						  									.getName());
			}			
		} else if (attribute == SchemaPackage.eINSTANCE
				 							 .getOwnerRole_PriorDbkeyPosition() &&
				   target.getSet().getOwner().getPriorDbkeyPosition() == null) {
			
			
			return "OMITTED";			
		} else {				
			return super.getValue(attribute);
		}
	}

}
