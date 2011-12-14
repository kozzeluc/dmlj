package org.lh.dmlj.schema.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.ConnectionEditPart;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.OwnerRole;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.anchor.IndexTargetAnchor;
import org.lh.dmlj.schema.editor.anchor.LockedChopboxAnchor;
import org.lh.dmlj.schema.editor.figure.RecordFigure;

public class RecordEditPart 
	extends AbstractSchemaElementEditPart<SchemaRecord>  {

	private RecordEditPart() {
		super(null); // disabled constructor
	}
	
	public RecordEditPart(SchemaRecord record) {
		super(record);		
	}	

	@Override
	protected IFigure createFigure() {
		return new RecordFigure();				
	}
	
	@Override
	protected EObject[] getModelObjects() {
		return new EObject[] {getModel(), 
							  getModel().getDiagramLocation()};
	}	

	@Override
	protected List<MemberRole> getModelSourceConnections() {
		List<MemberRole> memberRoles = new ArrayList<>();
		for (OwnerRole ownerRole : getModel().getOwnerRoles()) {
			memberRoles.addAll(ownerRole.getSet().getMembers());
		}
		return memberRoles;
	}
	
	@Override
	protected List<MemberRole> getModelTargetConnections() {
		List<MemberRole> memberRoles = new ArrayList<>();
		for (MemberRole memberRole : getModel().getMemberRoles()) {
			memberRoles.add(memberRole);
		}
		return memberRoles;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new LockedChopboxAnchor(getFigure());
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		if (connection.getSource() instanceof IndexEditPart) {
			return new IndexTargetAnchor((RecordFigure) getFigure());
		} else {
			return new LockedChopboxAnchor(getFigure());
		}
	}

	@Override
	protected void setFigureData() {
		SchemaRecord record = getModel();
		RecordFigure figure = (RecordFigure) getFigure();
		figure.setRecordName(record.getName());
		figure.setRecordId(record.getId());
		figure.setStorageMode(record.getStorageMode().toString());
		figure.setRecordLength(record.getDataLength());
		figure.setLocationMode(record.getLocationMode().toString());
		figure.setLocationModeDetails("?");
		figure.setDuplicatesOption("?");
		figure.setAreaName(record.getAreaSpecification().getArea().getName());
	}	
	
}