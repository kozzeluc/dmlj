package org.lh.dmlj.schema.editor.model;

import org.lh.dmlj.schema.DiagramLocation;
import org.lh.dmlj.schema.DiagramLocationProvider;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.SetMode;

public class SetLabel implements DiagramLocationProvider {
	
	private MemberRole memberRole;

	public SetLabel(MemberRole memberRole) {
		super();
		this.memberRole = memberRole;
	}

	@Override
	public DiagramLocation getDiagramLocation() {
		return memberRole.getDiagramLabelLocation();
	}
	
	public MemberRole getMemberRole() {
		return memberRole;
	}

	public String getValue() {
		StringBuilder p = new StringBuilder();
		p.append(memberRole.getSet().getName());
		p.append("\n");
		if (memberRole.getSet().getMode() == SetMode.CHAINED) {
			// chained set
			p.append("N");
			if (memberRole.getPriorDbkeyPosition() != null) {
				p.append("P");
			}
			if (memberRole.getOwnerDbkeyPosition() != null) {
				p.append("O");
			}
		} else {
			// indexed set
			if (memberRole.getIndexDbkeyPosition() == null &&
				memberRole.getOwnerDbkeyPosition() == null) {
				
				if (memberRole.getIndexDbkeyPosition() != null) {
					p.append("I");
				}
				if (memberRole.getOwnerDbkeyPosition() != null) {
					p.append("O");
				}
				
			} else {
				p.append("-");
			}
		}
		p.append(" ");
		p.append("...");
		return p.toString();
	}

}