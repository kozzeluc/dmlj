package org.lh.dmlj.schema.editor.part;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.anchor.IndexSourceAnchor;
import org.lh.dmlj.schema.editor.anchor.IndexTargetAnchor;
import org.lh.dmlj.schema.editor.figure.IndexFigure;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class SetEditPart extends AbstractConnectionEditPart {

	public SetEditPart(Set set) {
		super();
		setModel(set);
	}
	
	@Override
	protected void createEditPolicies() {
	}
	
	@Override
	protected IFigure createFigure() {
		PolylineConnection connection = new PolylineConnection();
		
		/*ConnectionAnchor sourceAnchor;
		ConnectionAnchor targetAnchor;
		PolylineDecoration decoration = null;*/
		
		/*if (parent instanceof RecordFigure) {
			sourceAnchor = new ChopboxAnchor(parent);
			targetAnchor = new ChopboxAnchor(child);
			decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
		} else if (parent instanceof IndexFigure) {
			sourceAnchor = new IndexSourceAnchor((IndexFigure)parent);
			targetAnchor = new IndexTargetAnchor((RecordFigure)child);
		} else if (parent instanceof ConnectorFigure) {
			sourceAnchor = new EllipseAnchor(parent);
			targetAnchor = new ChopboxAnchor(child);
			decoration = new PolylineDecoration();
			decoration.setTemplate(PolylineDecoration.TRIANGLE_TIP);
		} else {
			sourceAnchor = new ChopboxAnchor(parent);
			targetAnchor = new ChopboxAnchor(child);
		}*/
		
		/*connection.setSourceAnchor(sourceAnchor);
		connection.setTargetAnchor(targetAnchor);
		if (decoration != null) {
			connection.setTargetDecoration(decoration);
		}*/
		connection.setLineWidth(1);		
		
		/*if (parent != null) {
			SetDescription setDescription = 
				new SetDescription(label, parent, targetAnchor);
			setDescription.setFont(Plugin.getDefault().getFont());
			primary.add(setDescription);
			new FigureMover(setDescription);
		}*/
		
		return connection;		
	}
	
	public Set getModel() {
		return (Set) super.getModel();
	}
	
	
	@Override
	protected ConnectionAnchor getSourceConnectionAnchor() {
		if (getSource() instanceof RecordEditPart) {
			RecordEditPart editPart = (RecordEditPart) getSource();
			return new ChopboxAnchor(editPart.getFigure());
		} else if (getSource() instanceof IndexEditPart) {
			IndexEditPart editPart = (IndexEditPart) getSource();
			return new IndexSourceAnchor((IndexFigure) editPart.getFigure());
		}
		return super.getSourceConnectionAnchor();
	}
	
	@Override
	protected ConnectionAnchor getTargetConnectionAnchor() {
		if (getTarget() instanceof RecordEditPart) {
			RecordEditPart editPart = (RecordEditPart) getTarget();
			if (getSource() instanceof IndexEditPart) {
				return new IndexTargetAnchor((RecordFigure) editPart.getFigure());
			} else {
				return new ChopboxAnchor(editPart.getFigure());
			}
			
		}
		return super.getTargetConnectionAnchor();
	}
	
}