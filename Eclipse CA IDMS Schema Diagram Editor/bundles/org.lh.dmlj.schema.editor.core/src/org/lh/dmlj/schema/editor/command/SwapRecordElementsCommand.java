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
package org.lh.dmlj.schema.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.SchemaRecord;

public class SwapRecordElementsCommand extends ModelChangeBasicCommand {
	protected final SchemaRecord schemaRecord;
	protected final List<Element> newRootElements;
		
	private List<Element> oldRootElements = new ArrayList<>();
	private List<Element> oldAllElements = new ArrayList<>();
	private List<Element> newAllElements = new ArrayList<>();
	
	private static void deepCopyElement(Element element, List<Element> targetList) {
		targetList.add(element);
		for (Element childElement : element.getChildren()) {
			deepCopyElement(childElement, targetList);
		}
	}

	public SwapRecordElementsCommand(SchemaRecord schemaRecord, List<Element> newRootElements) {
		super("Edit Record Elements");
		this.schemaRecord = schemaRecord;
		this.newRootElements = newRootElements;
	}
	
	@Override
	public void execute() {	
		if (newRootElements.isEmpty()) {
			throw new IllegalStateException("record should contain at least 1 element: " + schemaRecord.getName());
		}
		saveOldElementLists();
		collectAllNewElements();
		replaceElements(newRootElements, newAllElements);
	}
	
	private void saveOldElementLists() {
		oldRootElements.addAll(schemaRecord.getRootElements());
		oldAllElements.addAll(schemaRecord.getElements());
	}

	private void collectAllNewElements() {
		for (var newRootElement : newRootElements) {
			deepCopyElement(newRootElement, newAllElements);
		}		
	}
	
	@Override
	public void undo() {
		replaceElements(oldRootElements, oldAllElements);
	}

	@Override
	public void redo() {
		replaceElements(newRootElements, newAllElements);
	}

	private void replaceElements(List<Element> desiredRootElements, List<Element> desiredAllElements) {
		if (!schemaRecord.getKeys().isEmpty()) {
			throw new IllegalStateException("record should NOT contain any keys: " + schemaRecord.getName());
		}
		schemaRecord.getRootElements().clear();
		schemaRecord.getElements().clear();
		schemaRecord.getRootElements().addAll(desiredRootElements);
		schemaRecord.getElements().addAll(desiredAllElements);
	}
	
}
