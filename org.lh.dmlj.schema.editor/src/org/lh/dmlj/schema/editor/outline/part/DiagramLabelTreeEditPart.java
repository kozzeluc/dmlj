package org.lh.dmlj.schema.editor.outline.part;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.editor.policy.DiagramLabelComponentEditPolicy;

public class DiagramLabelTreeEditPart extends AbstractSchemaTreeEditPart<DiagramLabel> {

	public DiagramLabelTreeEditPart(DiagramLabel diagramLabel, CommandStack commandStack) {
		super(diagramLabel, commandStack);
	}
	
	@Override
	protected void createEditPolicies() {		
	
		// the next edit policy allows for the deletion of a (the) diagram label
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DiagramLabelComponentEditPolicy());
		
	}	

	@Override
	protected String getImagePath() {
		return "icons/label16.GIF";
	}

	@Override
	protected String getNodeText() {
		return "Diagram label";
	}

}