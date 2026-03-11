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

import org.eclipse.emf.ecore.EObject;
import org.lh.dmlj.schema.ConnectionPart;
import org.lh.dmlj.schema.Connector;
import org.lh.dmlj.schema.Guide;
import org.lh.dmlj.schema.MemberRole;
import org.lh.dmlj.schema.Ruler;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;

public abstract sealed class ModelChangeContextChecker <T extends EObject> permits ConnectionPartModelChangeContextChecker,
		ConnectorModelChangeContextChecker, GuideModelChangeContextChecker, MemberRoleModelChangeContextChecker, RulerModelChangeContextChecker,
		SchemaAreaModelChangeContextChecker, SchemaRecordModelChangeContextChecker, SetModelChangeContextChecker {
	
	protected static void checkAppliesTo(ModelChangeContext context, EObject model) {
		if (model == null) {
			throw new IllegalArgumentException("Invalid model: null");
		}
		if (context.getModelChangeType() == ModelChangeType.SET_PROPERTY && !context.getContextData().containsKey(ContextDataKeys.PROPERTY_NAME)) {
			throw new IllegalStateException("Feature name NOT found in context data");
		}
		if (context.getContextData().containsKey(ContextDataKeys.PROPERTY_NAME) && context.getModelChangeType() != ModelChangeType.SET_PROPERTY) {
			throw new IllegalStateException("Feature name should NOT be present in context data: " + context.getModelChangeType());
		}		
	}
		
	@SuppressWarnings("unchecked")
	public static <T extends EObject> ModelChangeContextChecker<T> forModel(T model) {
		ModelChangeContextChecker<?> result = null;
		if (model instanceof ConnectionPart) {
			result = new ConnectionPartModelChangeContextChecker();
		} else if (model instanceof Connector) {
			result = new ConnectorModelChangeContextChecker();
		} else if (model instanceof Guide) {
			result = new GuideModelChangeContextChecker();
		} else if (model instanceof MemberRole) {
			result = new MemberRoleModelChangeContextChecker();
		} else if (model instanceof Ruler) {
			result = new RulerModelChangeContextChecker();
		} else if (model instanceof SchemaArea) {
			result = new SchemaAreaModelChangeContextChecker();
		} else if (model instanceof SchemaRecord) {
			result = new SchemaRecordModelChangeContextChecker();
		} else if (model instanceof Set) {
			result = new SetModelChangeContextChecker();
		} else {
			throw new IllegalArgumentException("Invalid model: " + model);
		}
		return (ModelChangeContextChecker<T>) result;
	}
	
	abstract boolean appliesTo(ModelChangeContext context, T model);

}
