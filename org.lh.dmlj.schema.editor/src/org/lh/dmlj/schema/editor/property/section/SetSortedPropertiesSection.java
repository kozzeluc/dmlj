package org.lh.dmlj.schema.editor.property.section;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.DuplicatesOption;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.SetMode;
import org.lh.dmlj.schema.editor.property.filter.IEnumFilter;

public class SetSortedPropertiesSection extends AbstractSetPropertiesSection {

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

}