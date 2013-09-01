package org.lh.dmlj.schema.editor.outline.part;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.DiagramLabel;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.SchemaEditor;

public class SchemaTreeEditPartFactory implements EditPartFactory {
	
	private CommandStack commandStack;
	
	public SchemaTreeEditPartFactory(SchemaEditor editor) {
		super();
		commandStack = (CommandStack) editor.getAdapter(CommandStack.class);
		Assert.isNotNull(commandStack, "commandStack == null");
	}

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Schema) {
			return new SchemaTreeEditPart((Schema) model, commandStack);
		} else if (model instanceof SchemaArea) {
			return new AreaTreeEditPart((SchemaArea) model, commandStack);
		} else if (model instanceof SchemaRecord) {
			return new RecordTreeEditPart((SchemaRecord) model, commandStack);
		} else if (model instanceof ConnectionPart) {
			return new SetTreeEditPart((ConnectionPart) model, commandStack);
		} else if (model instanceof SystemOwner) {
			return new IndexTreeEditPart((SystemOwner) model, commandStack);
		} else if (model instanceof DiagramLabel) {
			return new DiagramLabelTreeEditPart((DiagramLabel) model, commandStack);
		}
		throw new IllegalArgumentException("Unexpected model type: " + model.getClass().getName());
	}

}