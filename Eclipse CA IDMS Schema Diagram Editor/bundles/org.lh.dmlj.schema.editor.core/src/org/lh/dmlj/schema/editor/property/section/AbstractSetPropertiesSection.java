/**
 * Copyright (C) 2025  Luc Hermans
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program.  If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact information: kozzeluc@gmail.com.
 */
package org.lh.dmlj.schema.editor.property.section;

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.ConnectionLabel;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.SystemOwner;
import org.lh.dmlj.schema.VsamIndex;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.property.ISetProvider;

public abstract class AbstractSetPropertiesSection extends AbstractAttributesBasedPropertiesSection<MemberRole> implements ISetProvider {
	protected Set set;
	
	protected AbstractSetPropertiesSection() {
		super(Plugin.getDefault());
	}	

	@Override
	public final Set getSet() {
		return target.getSet();
	}
	
	@Override
	protected final MemberRole getTarget(Object modelObject) {		
		Assert.isTrue(modelObject instanceof ConnectionPart || modelObject instanceof ConnectionLabel ||
					  modelObject instanceof SystemOwner || modelObject instanceof Connector || modelObject instanceof Set ||
					  modelObject instanceof VsamIndex, "not a ConnectionPart, ConnectionLabel, SystemOwner, Connector, Set or VsamIndex");
				
		MemberRole memberRole;
		if (modelObject instanceof ConnectionPart connectionPart) {        		
        		memberRole = connectionPart.getMemberRole();
		} else if (modelObject instanceof ConnectionLabel connectionLabel) {
        		memberRole = connectionLabel.getMemberRole();
        } else if (modelObject instanceof SystemOwner systemOwner) {
        		memberRole = systemOwner.getSet().getMembers().get(0);
        } else if (modelObject instanceof Set aSet) {
        		memberRole = aSet.getMembers().get(0);
        } else if (modelObject instanceof VsamIndex vsamIndex) {
        		memberRole = vsamIndex.getMemberRole();
        } else {
        		var connector = (Connector) modelObject;
        		memberRole = connector.getConnectionPart().getMemberRole();
        }		
		set = memberRole.getSet();
		return memberRole;
	}
	
}
