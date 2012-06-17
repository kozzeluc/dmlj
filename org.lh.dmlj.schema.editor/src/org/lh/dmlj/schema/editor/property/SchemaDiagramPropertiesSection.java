package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.lh.dmlj.schema.SchemaPackage;

public class SchemaDiagramPropertiesSection 
	extends AbstractDiagramDataPropertiesSection {

	public SchemaDiagramPropertiesSection() {
		super();
	}
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers()) {
			return "Indicates whether the diagram rulers and guides are " +
				   "visible or not";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid()){
			return "Indicates whether the diagram grid is visible or not";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides()) {
			return "Indicates whether figures should snap to guides or not";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid()) {
			return "Indicates whether figures should snap to the grid or not";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
			return "Indicates whether figures should snap to geometry";
		}
		return super.getDescription(feature);
	}

	@Override
	protected EObject getEditableObject(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
				
			return target;
		}
		return super.getEditableObject(feature);
	}

	@Override
	protected List<EStructuralFeature> getFeatures() {
		List<EStructuralFeature> features = new ArrayList<>();
		features.add(SchemaPackage.eINSTANCE.getDiagramData_ShowRulers());
		features.add(SchemaPackage.eINSTANCE.getDiagramData_ShowGrid());
		if (target.isShowRulers()) {
			features.add(SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides());
		}
		if (target.isShowGrid()) {
			features.add(SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid());
		}
		features.add(SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry());
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers()) {
			return "Show rulers and guides";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid()){
			return "Show grid";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides()) {
			return "Snap to guides";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid()) {
			return "Snap to grid";
		} else if (feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
			return "Snap to geometry";
		}
		return super.getLabel(feature);
	}	
	
	@Override
	protected boolean isEditableFeature(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getDiagramData_ShowRulers() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_ShowGrid() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGuides() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGrid() ||
			feature == SchemaPackage.eINSTANCE.getDiagramData_SnapToGeometry()) {
			
			return true;
		}
		return super.isEditableFeature(feature);
	}
	
}