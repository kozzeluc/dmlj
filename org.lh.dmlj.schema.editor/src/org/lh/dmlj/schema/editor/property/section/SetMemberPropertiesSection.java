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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.LocationMode;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMembershipOption;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.common.Tools;
import org.lh.dmlj.schema.editor.property.IAreaSpecificationProvider;
import org.lh.dmlj.schema.editor.property.IMemberRoleProvider;
import org.lh.dmlj.schema.editor.property.handler.AreaHandler;
import org.lh.dmlj.schema.editor.property.handler.ChainedSetPointersHandler;
import org.lh.dmlj.schema.editor.property.handler.IEditHandler;
import org.lh.dmlj.schema.editor.property.handler.IHyperlinkHandler;
import org.lh.dmlj.schema.editor.property.handler.IndexedSetPointersHandler;

public class SetMemberPropertiesSection 
	extends AbstractSetPropertiesSection 
	implements IAreaSpecificationProvider, IMemberRoleProvider {

	private IHyperlinkHandler<EAttribute, Command> areaHandler = new AreaHandler(this);	
	private IHyperlinkHandler<EAttribute, Command> chainedSetPointersHandler = 
		new ChainedSetPointersHandler(this);
	private IHyperlinkHandler<EAttribute, Command> indexedSetPointersHandler = 
		new IndexedSetPointersHandler(this);
	
	public SetMemberPropertiesSection() {
		super();	
	}	
	
	@Override
	public AreaSpecification getAreaSpecification() {		
		return target.getRecord().getAreaSpecification();
	}

	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return target.getRecord();
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return target.getRecord().getAreaSpecification().getArea();
		} else {		
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	public List<EAttribute> getAttributes() {		
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getSchemaRecord_Name());
		attributes.add(SchemaPackage.eINSTANCE.getSchemaArea_Name());
		if (set.getMode() == SetMode.CHAINED) {
			// chained set
			attributes.add(SchemaPackage.eINSTANCE
									    .getMemberRole_NextDbkeyPosition());			
			attributes.add(SchemaPackage.eINSTANCE
										.getMemberRole_PriorDbkeyPosition());
		} else {
			// indexed set			
			attributes.add(SchemaPackage.eINSTANCE
									    .getMemberRole_IndexDbkeyPosition());
		}
		if (set.getSystemOwner() == null) {
			attributes.add(SchemaPackage.eINSTANCE
									    .getMemberRole_OwnerDbkeyPosition());
		}
		attributes.add(SchemaPackage.eINSTANCE.getMemberRole_MembershipOption());		
		return attributes;
	}
	
	@Override
	public String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {
			return getPluginProperty("description.member.set.properties.record");
		} else if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return getPluginProperty("description.member.set.properties.area");
		} else {
			return super.getDescription(attribute);
		}
	}

	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE
									  .getMemberRole_MembershipOption()) {
			// in the case of system owned indexed sets without index pointers,
			// the membership option MUST be MANDATORY AUTOMATIC, so we will not
			// allow editing in that case
			if (set.getMode() == SetMode.INDEXED &&
				set.getSystemOwner() != null &&
				set.getMembers().get(0).getIndexDbkeyPosition() == null) {
				
				return super.getEditableObject(attribute);
			} else {
				return target;
			}
		} else {
			return super.getEditableObject(attribute);
		}
	}	
	
	@Override
	public IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE
				  					  .getMemberRole_MembershipOption()) {
						
			if (newValue == SetMembershipOption.MANDATORY_MANUAL ||
				newValue == SetMembershipOption.OPTIONAL_MANUAL &&
				target.getRecord().getLocationMode() == LocationMode.VIA &&
				target.getRecord().getViaSpecification().getSet() == set) {
				
				String message = 
					"specifying the MANUAL connect option for a set if the " +
					"member record is stored VIA a set is NOT recommended " +
					"since this option may result in the target page for the " +
					"member record being determined from a page that does " +
					"not hold the owner record";
				return super.getEditHandler(attribute, newValue, message);
			}
		}
		return super.getEditHandler(attribute, newValue);
	}
	
	@Override
	public IHyperlinkHandler<EAttribute, Command> getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {
			return areaHandler;
		} else if (attribute == SchemaPackage.eINSTANCE
										     .getMemberRole_NextDbkeyPosition() ||
				   attribute == SchemaPackage.eINSTANCE
										     .getMemberRole_PriorDbkeyPosition() ||
				   attribute == SchemaPackage.eINSTANCE
										     .getMemberRole_OwnerDbkeyPosition() &&
				   set.getMode() == SetMode.CHAINED) {
			
			return chainedSetPointersHandler;			
		} else if (attribute == SchemaPackage.eINSTANCE
			     							 .getMemberRole_IndexDbkeyPosition() ||
			       attribute == SchemaPackage.eINSTANCE
			     						     .getMemberRole_OwnerDbkeyPosition() &&
			       set.getMode() == SetMode.INDEXED) {

			return indexedSetPointersHandler;			
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}		
	
	@Override
	public String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSchemaArea_Name()) {			
			return getPluginProperty("label.member.set.properties.area");	
		} else {
			return super.getLabel(attribute);
		}
	}
	
	@Override
	public MemberRole getMemberRole() {
		return target;
	}

	@Override
	protected String getValue(EAttribute attribute) {		
		if (attribute == SchemaPackage.eINSTANCE.getSchemaRecord_Name()) {			
			// remove the trailing underscore from the record name if we're 
			// dealing with a DDLCATLOD member record
			return Tools.removeTrailingUnderscore(target.getRecord().getName());
		} else if (attribute == SchemaPackage.eINSTANCE
				 							 .getMemberRole_IndexDbkeyPosition() &&
				   target.getIndexDbkeyPosition() == null ||
				   attribute == SchemaPackage.eINSTANCE
					 						 .getMemberRole_OwnerDbkeyPosition() &&
				   target.getOwnerDbkeyPosition() == null  ||
				   attribute == SchemaPackage.eINSTANCE
					 						 .getMemberRole_PriorDbkeyPosition() &&
				   target.getPriorDbkeyPosition() == null) {
			
			// (when requested to return the value for the next pointer 
			// position, that position will always be available)
			return "OMITTED";
		}
		return super.getValue(attribute);
	}

}
