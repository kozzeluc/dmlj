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

import org.lh.dmlj.schema.Ruler;

public final class RulerModelChangeContextChecker extends ModelChangeContextChecker<Ruler> {

	@Override
	public boolean appliesTo(ModelChangeContext context, Ruler ruler) {
		checkAppliesTo(context, ruler);
		var contextData = context.getContextData();
		var rulerIndex = ruler.getDiagramData().getRulers().indexOf(ruler);
		return contextData.size() == 1 &&
				contextData.containsKey(ContextDataKeys.RULER_INDEX) &&
				rulerIndex == Integer.parseInt(contextData.get(ContextDataKeys.RULER_INDEX)) || contextData.size() == 2 &&
				contextData.containsKey(ContextDataKeys.PROPERTY_NAME) &&
				contextData.containsKey(ContextDataKeys.RULER_INDEX) &&
				rulerIndex == Integer.parseInt(contextData.get(ContextDataKeys.RULER_INDEX));		
	}
	
}
