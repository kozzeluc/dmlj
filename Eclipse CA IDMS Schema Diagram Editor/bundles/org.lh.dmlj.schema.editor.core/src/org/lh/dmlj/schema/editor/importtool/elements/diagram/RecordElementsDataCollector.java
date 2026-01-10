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
package org.lh.dmlj.schema.editor.importtool.elements.diagram;

import java.util.Collection;
import java.util.List;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.IndexElement;
import org.lh.dmlj.schema.Usage;
import org.lh.dmlj.schema.editor.common.ElementValueTransformer;
import org.lh.dmlj.schema.editor.importtool.IElementDataCollector;

public class RecordElementsDataCollector implements IElementDataCollector<Element> {

	@Override
	public String getBaseName(Element element) {
		return element.getBaseName();
	}

	@Override
	public String getDependsOnElementName(Element element) {
		if (element.getOccursSpecification() == null) {
			return null;
		} else {
			var dependsOnElement = element.getOccursSpecification().getDependingOn();
			return dependsOnElement != null ? dependsOnElement.getName() : null;
		}
	}

	@Override
	public Collection<String> getIndexElementBaseNames(Element element) {
		return element.getOccursSpecification() == null ? List.of() : element.getOccursSpecification().getIndexElements().stream()
					.map(IndexElement::getBaseName)
					.toList();
	}

	@Override
	public Collection<String> getIndexElementNames(Element element) {
		return element.getOccursSpecification() == null ? List.of() : element.getOccursSpecification().getIndexElements().stream()
				.map(IndexElement::getName)
				.toList();
	}

	@Override
	public boolean getIsNullable(Element element) {
		return element.isNullable();
	}

	@Override
	public short getLevel(Element element) {
		return element.getLevel();
	}

	@Override
	public String getName(Element element) {
		return element.getName();
	}

	@Override
	public short getOccurrenceCount(Element element) {
		return element.getOccursSpecification() == null ? 0 : element.getOccursSpecification().getCount();
	}

	@Override
	public String getPicture(Element element) {
		return element.getPicture();
	}

	@Override
	public String getRedefinedElementName(Element element) {
		return element.getRedefines() != null ? element.getRedefines().getName() : null;
	}

	@Override
	public Usage getUsage(Element element) {
		return element.getUsage();
	}

	@Override
	public List<String> getValues(Element element) {
		return ElementValueTransformer.toValueList(element.getValue());
	}

}
