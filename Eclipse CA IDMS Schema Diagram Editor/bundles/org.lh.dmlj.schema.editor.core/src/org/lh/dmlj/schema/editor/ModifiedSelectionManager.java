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

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.SelectionManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * A selection manager that prevents nested parts from being selected when their parent or grandparent part is selected.
 */
class ModifiedSelectionManager extends SelectionManager {
	private final GraphicalViewer viewer;

	private static boolean checkAncestors(EditPart partOfInterest, Predicate<EditPart> predicate) {
		return partOfInterest != null && Stream.iterate(partOfInterest.getParent(), Objects::nonNull, EditPart::getParent)
				.anyMatch(predicate);
	}	

	public ModifiedSelectionManager(GraphicalViewer viewer) {
		this.viewer = viewer;
	}
	
	/**
	 * Cycle through each of the selected EditParts and remove all that have a selected ancestor.
	 */
	@Override
	public void setSelection(ISelection selection) {
		List<?> currentlySelectedEditParts = ((IStructuredSelection) selection).toList();
		var selectedEditPartsThatAreAncestorFree = currentlySelectedEditParts.stream()
				.map(EditPart.class::cast)
				.filter(part -> !checkAncestors(part, currentlySelectedEditParts::contains))
				.toList();
		super.setSelection(new StructuredSelection(selectedEditPartsThatAreAncestorFree));
	}
	
	/**
	 * Adjust the selection based upon whether the editpart is a nested part of an already selected ancestor or
	 * is an ancestor of (an) already selected part(s).
	 */
	@Override
	public void appendSelection(EditPart part) {
		List<?> selection = ((IStructuredSelection) getSelection()).toList();
		
		// If "nothing" is selected then getSelection() returns the viewer's primary edit part in which case the 
		// specified part should be selected.		
		if (selection.size() == 1 && selection.get(0) == viewer.getContents()) {
			super.appendSelection(part);
			return;
		}
		
		// If the selection already contains an ancestor of the specified part then don't select the part
		if (checkAncestors(part, selection::contains)) {
			return;
		}

		// Deselect any currently selected parts which have the new part as an ancestor
		selection.stream()
				.map(EditPart.class::cast)
				.filter(aSelectedPart -> checkAncestors(part, ancestor -> ancestor == aSelectedPart))
				.forEach(this::deselect);
		
		super.appendSelection(part);
	}
	
}
