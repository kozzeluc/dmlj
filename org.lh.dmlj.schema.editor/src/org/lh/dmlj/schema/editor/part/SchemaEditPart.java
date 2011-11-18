package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SetMode;

public class SchemaEditPart extends AbstractGraphicalEditPart {

	@SuppressWarnings("unused")
	private SchemaEditPart() {
		super(); // disabled constructor
	}
	
	public SchemaEditPart(Schema schema) {
		super();
		setModel(schema);
	}
	
	@Override
	protected void createEditPolicies() {
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setBorder(new MarginBorder(3));
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}
	
	public Schema getModel() {
		return (Schema) super.getModel();
	}
	
	@Override
	protected List<?> getModelChildren() {
		List<Object> allObjects = new ArrayList<>();
		
		// records
		allObjects.addAll(getModel().getRecords());
		
		// indexes
		for (Set set : getModel().getSets()) {
			if (set.getMode() == SetMode.INDEXED && 
				set.getSystemOwner() != null) {
				
				allObjects.add(set.getSystemOwner());
			}
		}
		
		return allObjects;
	}
	
}