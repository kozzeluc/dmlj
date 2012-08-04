package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EAttribute;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaDiagramPropertiesSection 
	extends AbstractDiagramDataPropertiesSection {

	public SchemaDiagramPropertiesSection() {
		super();
	}

	@Override
	protected EObject getEditableObject(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers() ||
			attribute == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid() ||
			attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides() ||
			attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid() ||
			attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
				
			return target;
		}
		return super.getEditableObject(attribute);
	}

	@Override
	protected List<EAttribute> getAttributes() {
		List<EAttribute> attributes = new ArrayList<>();
		attributes.add(SchemaPackage.eINSTANCE.getDiagramData_ShowRulers());
		attributes.add(SchemaPackage.eINSTANCE.getDiagramData_ShowGrid());
		if (target.isShowRulers()) {
			attributes.add(SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides());
		}
		if (target.isShowGrid()) {
			attributes.add(SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid());
		}
		attributes.add(SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry());
		return attributes;
	}	
	
}