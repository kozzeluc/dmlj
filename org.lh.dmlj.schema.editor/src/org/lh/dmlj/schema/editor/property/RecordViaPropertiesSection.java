package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.common.NamingConventions;
import org.lh.dmlj.schema.editor.common.ValidationResult;

public class RecordViaPropertiesSection 
	extends AbstractRecordPropertiesSection {	

	private IHyperlinkHandler locationModeHandler = 
		new LocationModeHandler(this);	
	
	public RecordViaPropertiesSection() {
		super();
	}	
	
	@Override
	protected EObject getAttributeOwner(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name() ||
			attribute == SchemaPackage.eINSTANCE
									  .getViaSpecification_SymbolicDisplacementName() ||
			attribute == SchemaPackage.eINSTANCE
									  .getViaSpecification_DisplacementPageCount()) {
			
			return target.getViaSpecification();
		} else {
			return super.getAttributeOwner(attribute);
		}
	}
	
	@Override
	protected List<EAttribute> getAttributes() {
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getSet_Name());
		attributes.add(SchemaPackage.eINSTANCE
									.getViaSpecification_SymbolicDisplacementName());				
		attributes.add(SchemaPackage.eINSTANCE
							        .getViaSpecification_DisplacementPageCount());		
		return attributes;
	}
	
	@Override
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			String key = "description.via.record.properties.set";
			return getPluginProperty(key);
		} else {
			return super.getDescription(attribute);
		}
	}
	
	@Override
	protected EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE
									  .getViaSpecification_SymbolicDisplacementName() ||
			attribute == SchemaPackage.eINSTANCE
			        				  .getViaSpecification_DisplacementPageCount()) {
			
			return target.getViaSpecification();
		} else {
			return super.getEditableObject(attribute);
		}		
	}
	
	@Override
	protected IEditHandler getEditHandler(EAttribute attribute, Object newValue) {
		if (attribute == SchemaPackage.eINSTANCE
				  					  .getViaSpecification_SymbolicDisplacementName()) {
			
			if (newValue != null && target.getViaSpecification()
										  .getDisplacementPageCount() != null) {
				
				String message = 
					"'" + getLabel(SchemaPackage.eINSTANCE
							 					.getViaSpecification_DisplacementPageCount()) + 
					"' is already set";
				return new ErrorEditHandler(message);
			} else if (newValue != null) {				
				String name = ((String) newValue).toUpperCase();
				ValidationResult validationResult = 
					NamingConventions.validate(name, 
											   NamingConventions.Type
											   					.SYMBOLIC_DISPLACEMENT);
				if (validationResult.getStatus() == ValidationResult.Status.OK) {				
					return super.getEditHandler(attribute, name);
				} else {
					return new ErrorEditHandler(validationResult.getMessage());
				}
			}
		} else if (attribute == SchemaPackage.eINSTANCE
				  							 .getViaSpecification_DisplacementPageCount()) {
			
			if (newValue != null && target.getViaSpecification()
				                          .getSymbolicDisplacementName() != null) {
					
				String message = 
					"'" + getLabel(SchemaPackage.eINSTANCE
											    .getViaSpecification_SymbolicDisplacementName()) + 
					"' is already set";
				return new ErrorEditHandler(message);
			} else if (newValue != null) {
				// get the new displacement page count
				Short i = ((Short) newValue).shortValue();
				// must be an unsigned integer in the range 0 through 32,767
				if (i < 0) {
					String message = "must be an unsigned integer in the " +
									 "range 0 through 32,767";
					return new ErrorEditHandler(message);
				}
			}
		} 
		return super.getEditHandler(attribute, newValue);		
	}
	
	@Override
	protected IHyperlinkHandler getHyperlinkHandler(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			return locationModeHandler;
		} else {
			return super.getHyperlinkHandler(attribute);
		}
	}	
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {
			String key = "label.via.record.properties.set";
			return getPluginProperty(key);
		} else {
			return super.getLabel(attribute);
		}
	}
	
	@Override
	protected String getValue(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getSet_Name()) {			
			// remove the trailing underscore from the via set name if we're 
			// dealing with a DDLCATLOD record
			StringBuilder p = 
				new StringBuilder(target.getViaSpecification().getSet().getName());
			if (p.charAt(p.length() - 1) == '_') {
				p.setLength(p.length() - 1);
			}
			return p.toString();
		} else {
			return super.getValue(attribute);
		}
	}

}