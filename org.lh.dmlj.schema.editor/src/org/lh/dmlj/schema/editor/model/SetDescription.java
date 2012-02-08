package org.lh.dmlj.schema.editor.model;

import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramLocationProvider;
import org.lh.dmlj.schema.MemberRole;

public class SetDescription implements DiagramLocationProvider {
	
	private MemberRole memberRole;

	public SetDescription(MemberRole memberRole) {
		super();
		this.memberRole = memberRole;
	}

	@Override
	public DiagramLocation getDiagramLocation() {
		return memberRole.getDiagramLabelLocation();
	}
	
	@Override
	public String getLabel() {
		return "set label " + memberRole.getSet().getName();
	}

	public MemberRole getMemberRole() {
		return memberRole;
	}	

}