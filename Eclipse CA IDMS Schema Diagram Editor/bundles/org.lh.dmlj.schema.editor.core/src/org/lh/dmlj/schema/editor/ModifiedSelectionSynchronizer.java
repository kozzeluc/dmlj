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
package org.lh.dmlj.schema.editor;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.lh.dmlj.schema.editor.outline.OutlinePage;

public class ModifiedSelectionSynchronizer extends SelectionSynchronizer {
	private final OutlinePage outlinePage;
	
	public ModifiedSelectionSynchronizer(OutlinePage outlinePage) {
		this.outlinePage = outlinePage;
	}
	
	@Override
	protected EditPart convert(EditPartViewer viewer, EditPart part) {
		if (outlinePage != null && outlinePage.canConvertEditPart(viewer, part)) {
			// make sure the most relevant edit part is selected in the outline page
			return outlinePage.convert(viewer, part);
		} else {
			// this request is not for the outline page, so have the standard selection synchronizer's pick the edit part
			return super.convert(viewer, part);
		}				
	}

}
