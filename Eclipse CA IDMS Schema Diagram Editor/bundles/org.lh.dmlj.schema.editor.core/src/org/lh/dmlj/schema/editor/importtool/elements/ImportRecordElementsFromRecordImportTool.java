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
package org.lh.dmlj.schema.editor.importtool.elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.lh.dmlj.schema.Element;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.editor.importtool.IDataEntryContext;
import org.lh.dmlj.schema.editor.importtool.elements.diagram.RecordElementsDataCollector;

public class ImportRecordElementsFromRecordImportTool implements IRecordElementsImportTool {

	private SchemaRecord record;
	
	public ImportRecordElementsFromRecordImportTool() {
		super();
	}

	@Override
	public void dispose() {
	}

	@Override
	public Collection<?> getRootElementContexts() {
		List<Element> rootElements = new ArrayList<>();
		rootElements.addAll(record.getRootElements());
		return rootElements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getSubordinateElementContexts(T elementContext) {
		if (elementContext instanceof Element) {
			Element element = (Element) elementContext;
			List<Element> subordinateElements = new ArrayList<>();
			subordinateElements.addAll(element.getChildren());
			return (Collection<T>) subordinateElements;			
		} else {
			throw new IllegalArgumentException("unknown element context type: " +
											   elementContext.getClass().getName());
		}
	}

	@Override
	public void init(Properties parameters, IRecordElementsDataCollectorRegistry dataCollectorRegistry) {
		RecordElementsDataCollector dataCollector = new RecordElementsDataCollector();
		dataCollectorRegistry.registerDataCollector(Element.class, dataCollector);
	}

	@Override
	public void setContext(IDataEntryContext dataEntryContext) {
		record = dataEntryContext.getAttribute(IDataEntryContext.RECORD);
	}

}
