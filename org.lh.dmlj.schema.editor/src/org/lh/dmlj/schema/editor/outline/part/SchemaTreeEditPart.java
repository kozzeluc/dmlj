package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

public class SchemaTreeEditPart extends AbstractSchemaTreeEditPart<Schema> {

	public SchemaTreeEditPart(Schema schema, CommandStack commandStack) {
		super(schema, commandStack);
	}

	@Override
	protected String getImagePath() {
		return "icons/schema.gif";
	}

	@Override
	protected List<?> getModelChildren() {
		
		List<Object> children = new ArrayList<>();
		
		// add the diagram label, if present
		if (getModel().getDiagramData().getLabel() != null) {
			children.add(getModel().getDiagramData().getLabel());
		}
		
		// add the areas in alphabetical order
		List<SchemaArea> areas = 
			new ArrayList<>(getModel().getAreas()); // we can't sort the model's list directly
		Collections.sort(areas);
		children.addAll(areas);
		
		// add the records in alphabetical order
		List<SchemaRecord> records = 
			new ArrayList<>(getModel().getRecords()); // we can't sort the model's list directly
		Collections.sort(records);
		children.addAll(records);
		
		// add the first connection part of the first set member or the system owner of the sets in 
		// alphabetical order
		List<Set> sets = new ArrayList<>(getModel().getSets());
		Collections.sort(sets);
		for (Set set : sets) {
			if (set.getSystemOwner() == null) {
				// user owned set (chained or indexed): first connection part of first member
				children.add(set.getMembers().get(0).getConnectionParts().get(0));
			} else {
				// system owned indexed set: system owner
				children.add(set.getSystemOwner());
			}
		}
		
		return children;
	}

	@Override
	protected String getNodeText() {
		return getModel().getName() + " version " + getModel().getVersion();
	}

}