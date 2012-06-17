package org.lh.dmlj.schema.editor.property;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;

public abstract class AbstractSetPropertiesSection 
	extends AbstractPropertiesSection<MemberRole> {
	
	protected Set set;

	@Override
	protected final MemberRole getTarget(Object modelObject) {		
		Assert.isTrue(modelObject instanceof ConnectionPart ||
					  modelObject instanceof ConnectionLabel ||
					  modelObject instanceof SystemOwner ||
					  modelObject instanceof Connector,
					  "not a ConnectionPart, ConnectionLabel, SystemOwner or " +
					  "Connector");
				
		MemberRole memberRole;
		if (modelObject instanceof ConnectionPart) {        	
        	ConnectionPart connectionPart = (ConnectionPart) modelObject;
        	memberRole = connectionPart.getMemberRole();
		} else if (modelObject instanceof ConnectionLabel) {
        	ConnectionLabel connectionLabel = (ConnectionLabel) modelObject;
        	memberRole = connectionLabel.getMemberRole();
        } else if (modelObject instanceof SystemOwner) {	
        	SystemOwner systemOwner = (SystemOwner) modelObject;
        	memberRole = systemOwner.getSet().getMembers().get(0);
        } else {
        	Connector connector = (Connector) modelObject;
        	memberRole = connector.getConnectionPart().getMemberRole();
        }
		
		set = memberRole.getSet();		
		
		return memberRole;
	}
	
}