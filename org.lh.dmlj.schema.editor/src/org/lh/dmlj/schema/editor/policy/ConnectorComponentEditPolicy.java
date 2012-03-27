package org.lh.dmlj.schema.editor.policy;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.editor.command.DeleteConnectorsCommand;
import org.lh.dmlj.schema.editor.part.ConnectorEditPart;

public class ConnectorComponentEditPolicy extends ComponentEditPolicy {

	public ConnectorComponentEditPolicy() {
		super();
	}
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		List<?> editParts = deleteRequest.getEditParts(); 
		if (editParts.size() != 1 ||
			!(editParts.get(0) instanceof ConnectorEditPart)) {
			
			return null;
		}
		Connector connector = ((ConnectorEditPart)editParts.get(0)).getModel();
		MemberRole memberRole = connector.getConnectionPart().getMemberRole();
		return new DeleteConnectorsCommand(memberRole);				
	}
	
}