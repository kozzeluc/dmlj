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
package org.lh.dmlj.schema.editor.command.infrastructure;

import org.lh.dmlj.schema.ConnectionPart;

public final class ConnectionPartModelChangeContextChecker extends ModelChangeContextChecker<ConnectionPart> {

	@Override
	public boolean appliesTo(ModelChangeContext context, ConnectionPart connectionPart) {
		checkAppliesTo(context, connectionPart);
		var contextData = context.getContextData();
		var memberRole = connectionPart.getMemberRole();
		var set = memberRole.getSet();
		var schemaRecord = memberRole.getRecord();
		var connectionPartIndex = memberRole.getConnectionParts().indexOf(connectionPart);
		return contextData.size() == 3 &&
				set.getName().equals(contextData.get(ContextDataKeys.SET_NAME)) &&
				schemaRecord.getName().equals(contextData.get(ContextDataKeys.RECORD_NAME)) &&
				contextData.containsKey(ContextDataKeys.CONNECTION_PART_INDEX) &&
				connectionPartIndex == Integer.parseInt(contextData.get(ContextDataKeys.CONNECTION_PART_INDEX)) || contextData.size() == 4 &&
				contextData.containsKey(ContextDataKeys.PROPERTY_NAME) &&
				set.getName().equals(contextData.get(ContextDataKeys.SET_NAME)) &&
				schemaRecord.getName().equals(contextData.get(ContextDataKeys.RECORD_NAME)) &&
				contextData.containsKey(ContextDataKeys.CONNECTION_PART_INDEX) &&
				connectionPartIndex == Integer.parseInt(contextData.get(ContextDataKeys.CONNECTION_PART_INDEX));
	}
	
}
