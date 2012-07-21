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
	protected String getDescription(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers()) {
			return "Indicates whether the diagram rulers and guides are " +
				   "visible or not";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid()){
			return "Indicates whether the diagram grid is visible or not";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides()) {
			return "Indicates whether figures should snap to guides or not";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid()) {
			return "Indicates whether figures should snap to the grid or not";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
			return "Indicates whether figures should snap to geometry";
		}
		return super.getDescription(attribute);
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
	
	@Override
	protected String getLabel(EAttribute attribute) {
		if (attribute == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers()) {
			return "Show rulers and guides";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid()){
			return "Show grid";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides()) {
			return "Snap to guides";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid()) {
			return "Snap to grid";
		} else if (attribute == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
			return "Snap to geometry";
		}
		return super.getLabel(attribute);
	}	
	
}