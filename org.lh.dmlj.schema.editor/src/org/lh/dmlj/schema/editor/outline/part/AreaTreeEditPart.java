package org.lh.dmlj.schema.editor.outline.part;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.commands.CommandStack;
import org.lh.dmlj.schema.AreaSpecification;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.SystemOwner;

public class AreaTreeEditPart extends AbstractSchemaTreeEditPart<SchemaArea> {

	public AreaTreeEditPart(SchemaArea area, CommandStack commandStack) {
		super(area, commandStack);
	}

	@Override
	protected String getImagePath() {
		return "icons/data_source.gif";
	}

	@Override
	protected List<?> getModelChildren() {
		
		List<Object> children = new ArrayList<>();
		
		// add the area's records in alphabetical order
		List<SchemaRecord> records = new ArrayList<>();
		for (AreaSpecification areaSpecification : getModel().getAreaSpecifications()) {
			if (areaSpecification.getRecord() != null) {
				records.add(areaSpecification.getRecord());
			}
		}
		Collections.sort(records);
		children.addAll(records);
		
		// add the area's system owned indexed set owners in alphabetical order
		List<SystemOwner> systemOwners = new ArrayList<>();
		for (AreaSpecification areaSpecification : getModel().getAreaSpecifications()) {
			if (areaSpecification.getSystemOwner() != null) {
				systemOwners.add(areaSpecification.getSystemOwner());
			}
		}
		Collections.sort(systemOwners);
		children.addAll(systemOwners);
		
		return children;
	}

	@Override
	protected String getNodeText() {
		return getModel().getName();
	}

}