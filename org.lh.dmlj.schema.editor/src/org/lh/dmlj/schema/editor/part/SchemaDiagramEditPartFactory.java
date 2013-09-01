package org.lh.dmlj.schema.editor.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.DiagramLabel;
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
		} else if (model instanceof ConnectionPart) {
			return new SetEditPart((ConnectionPart) model);
		} else if (model instanceof ConnectionLabel) {
			return new SetDescriptionEditPart((ConnectionLabel) model);
		} else if (model instanceof Connector) {
			return new ConnectorEditPart((Connector) model);
		} else if (model instanceof DiagramLabel) {
			return new DiagramLabelEditPart((DiagramLabel) model);
		}
		throw new IllegalStateException("No EditPart for " + model.getClass());
	}

}