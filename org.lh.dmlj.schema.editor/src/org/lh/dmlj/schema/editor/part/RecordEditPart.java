package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.anchor.IndexTargetAnchor;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class RecordEditPart 
	extends AbstractDiagramElementEditPart implements NodeEditPart {

	@SuppressWarnings("unused")
	private RecordEditPart() {
		super(); // disabled constructor
	}
	
	public RecordEditPart(SchemaRecord record) {
		super();
		setModel(record);
	}
	
	@Override
	protected void createEditPolicies() {
	}

	@Override
	protected IFigure createFigure() {
		
		SchemaRecord record = getModel();
		
		RecordFigure figure = new RecordFigure();
		figure.setRecordName(record.getName());
		figure.setRecordId(record.getId());
		figure.setStorageMode(record.getStorageMode().toString());
		figure.setRecordLength(record.getDataLength());
		figure.setLocationMode(record.getLocationMode().toString());
		figure.setLocationModeDetails("?");
		figure.setDuplicatesOption("?");
		figure.setAreaName(record.getAreaSpecification().getArea().getName());		
		
		return figure;
		
	}
	
	public SchemaRecord getModel() {
		return (SchemaRecord) super.getModel();
	}
	
	@Override
	protected List<Set> getModelSourceConnections() {
		
		SchemaRecord record = getModel();
		
		List<Set> sets = new ArrayList<>();
		
		for (OwnerRole ownerRole : record.getOwnerRoles()) {
			sets.add(ownerRole.getSet());
		}
		
		return sets;
	}
	
	@Override
	protected List<Set> getModelTargetConnections() {
		
		SchemaRecord record = getModel();
		
		List<Set> sets = new ArrayList<>();
		
		for (MemberRole memberRole : record.getMemberRoles()) {
			sets.add(memberRole.getSet());
		}
		
		return sets;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		if (connection.getSource() instanceof IndexEditPart) {
			return new IndexTargetAnchor((RecordFigure) getFigure());
		} else {
			return new ChopboxAnchor(getFigure());
		}
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}
}