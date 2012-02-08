package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.editor.model.SetDescription;

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
		} else if (model instanceof SetDescription) {
			return new SetDescriptionEditPart((SetDescription) model);
		}
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}

}