package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

public class SchemaDiagramEditPartFactory implements EditPartFactory {

	public SchemaDiagramEditPartFactory() {
		super();
	}
	
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof Schema) {
			return new SchemaEditPart((Schema) model);
		} else if (model instanceof SchemaRecord) {
			return new RecordEditPart((SchemaRecord) model);
		} else if (model instanceof SystemOwner) {
			return new IndexEditPart((SystemOwner) model);
		} else if (model instanceof MemberRole) {
			return new SetEditPart((MemberRole) model);
		} else if (model instanceof ConnectionLabel) {
			return new SetDescriptionEditPart((ConnectionLabel) model);
		}
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}

}