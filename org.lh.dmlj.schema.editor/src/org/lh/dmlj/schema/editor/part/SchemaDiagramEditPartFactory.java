package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
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
		} else if (model instanceof Set) {
			return new SetEditPart((Set) model);
		}
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}

}