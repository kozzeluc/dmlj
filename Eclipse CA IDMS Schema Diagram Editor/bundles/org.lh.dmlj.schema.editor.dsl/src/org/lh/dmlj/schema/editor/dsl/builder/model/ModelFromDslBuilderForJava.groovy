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
package org.lh.dmlj.schema.editor.dsl.builder.model

import org.lh.dmlj.schema.Element
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.Set

class ModelFromDslBuilderForJava {

	private static class HasModelBuilderShortcutsImplementor implements HasModelBuilderShortcuts {
	}
	private static HasModelBuilderShortcutsImplementor delegate = new HasModelBuilderShortcutsImplementor()

	public static SchemaArea area(String definition) {
		delegate.area(definition)
	}
	
	public static SchemaArea area(File definition) {
		delegate.area(definition.text)
	}

	public static Element element(String definition) {
		delegate.element(definition)
	}
	
	public static Element element(File definition) {
		delegate.element(definition.text)
	}

	public static SchemaRecord record(String definition) {
		delegate.record(definition)
	}
	
	public static SchemaRecord record(File definition) {
		delegate.record(definition.text)
	}

	public static Schema schema(String definition) {
		delegate.schema(definition)
	}
	
	public static Schema schema(File definition) {
		delegate.schema(Utils.getSchemadslFileContents(definition))
	}
	
	public static Set set(String definition) {
		delegate.set(definition)
	}
	
	public static Set set(File definition) {
		delegate.set(definition.text)
	}
	
}
