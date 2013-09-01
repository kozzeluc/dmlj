package org.lh.dmlj.schema.editor.property.section;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.Plugin;

public class DiagramLabelSection extends AbstractAttributesBasedPropertiesSection<DiagramLabel> {

	public DiagramLabelSection() {
		super(Plugin.getDefault());
	}

	@Override
	public List<EAttribute> getAttributes() {
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getDiagramLabel_Description());
		return attributes;
	}
	
	@Override
	public EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getDiagramLabel_Description()) {			
			return target;
		} else {
			return super.getEditableObject(attribute);
		}
	}

	@Override
	public DiagramLabel getTarget(Object object) {
		Assert.isTrue(modelObject instanceof DiagramLabel, "not a DiagramLabel");
		return (DiagramLabel) modelObject;
	}

}