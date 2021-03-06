/**
 * Copyright (C) 2016  Luc Hermans
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
	
	protected SchemaRecord record;
	
	protected List<Element> newRootElements;
	private List<Element> newAllElements = new ArrayList<>();
	
	private List<Element> oldRootElements = new ArrayList<>();
	private List<Element> oldAllElements = new ArrayList<>();
	
	private static void deepCopyElement(Element element, List<Element> targetList) {
		targetList.add(element);
		for (Element childElement : element.getChildren()) {
			deepCopyElement(childElement, targetList);
		}
	}

	public SwapRecordElementsCommand(SchemaRecord record, List<Element> newRootElements) {
		super("Edit Record Elements");
		this.record = record;
		this.newRootElements = newRootElements;
	}
	
	@Override
	public void execute() {	
		if (newRootElements.isEmpty()) {
			throw new RuntimeException("record should contain at least 1 element: " + record.getName());
		}
		saveOldElementLists();
		collectAllNewElements();
		replaceElements(newRootElements, newAllElements);
	}
	
	private void saveOldElementLists() {
		oldRootElements.addAll(record.getRootElements());
		oldAllElements.addAll(record.getElements());
	}

	private void collectAllNewElements() {
		for (Element newRootElement : newRootElements) {
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
		if (!record.getKeys().isEmpty()) {
			throw new RuntimeException("record should NOT contain any keys: " + record.getName());
		}
		record.getRootElements().clear();
		record.getElements().clear();
		record.getRootElements().addAll(desiredRootElements);
		record.getElements().addAll(desiredAllElements);
	}
	
}
