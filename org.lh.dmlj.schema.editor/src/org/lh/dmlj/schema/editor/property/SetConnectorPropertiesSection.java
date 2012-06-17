package org.lh.dmlj.schema.editor.property;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.SchemaPackage;
import org.lh.dmlj.schema.editor.command.SetStringAttributeCommand;

public class SetConnectorPropertiesSection extends AbstractSetPropertiesSection {

	public SetConnectorPropertiesSection() {
		super();
	}
	
	@Override
	protected String getDescription(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return "A text to be shown in this set's connectors";
		}
		return super.getDescription(feature);
	}
	
	@Override
	protected EObject getEditableObject(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getConnector_Label()) {
			// although there will always be 2 connectors involved, capturing
			// the model changes from the first will do
			return target.getConnectionParts().get(0).getConnector();
		}
		return super.getEditableObject(feature);
	}
	
	@Override
	protected Command getEditCommand(EStructuralFeature feature, 
									 String newValue) {

		if (feature == SchemaPackage.eINSTANCE.getConnector_Label()) {
			
			// we need to set the label on both connectors, so go grab them 
			// (they should always be there)
			Connector[] connector = new Connector[2];
			connector[0] = target.getConnectionParts().get(0).getConnector();
			connector[1] = target.getConnectionParts().get(1).getConnector();
			
			// create 2 SetStringAttributeCommands to set the new label
			EAttribute attribute = (EAttribute) feature;
			String value = newValue.trim();
			String label = getLabel(feature);
			Command[] command = new Command[2];
			command[0] = new SetStringAttributeCommand(connector[0], attribute, 
													   value, label);
			command[1] = new SetStringAttributeCommand(connector[1], attribute, 
													   value, label);
			
			// chain the 2 commands as the return Command
			return command[0].chain(command[1]);
			
		} else {
			return super.getEditCommand(feature, newValue);
		}
	}
	
	@Override
	protected List<EStructuralFeature> getFeatures() {
		List<EStructuralFeature> features = new ArrayList<>();
		features.add(SchemaPackage.eINSTANCE.getConnector_Label());
		return features;
	}
	
	@Override
	protected String getLabel(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return "Label";
		}
		return super.getLabel(feature);
	}
	
	@Override
	protected String getValue(EStructuralFeature feature) {
		if (feature == SchemaPackage.eINSTANCE.getConnector_Label()) {
			return target.getConnectionParts().get(0).getConnector().getLabel();
		}
		return super.getValue(feature);
	}

	@Override
	protected boolean isEditableFeature(EStructuralFeature feature) {		
		return feature == SchemaPackage.eINSTANCE.getConnector_Label();
	}

}