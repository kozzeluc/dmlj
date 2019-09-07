/**
 * Copyright (C) 2018  Luc Hermans
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
package org.lh.dmlj.schema.editor.property.section

import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SchemaSyntaxBuilder

public class SchemaDSLSection extends AbstractSectionWithStyledText {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS = [ Schema.class ]

	public SchemaDSLSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS)
	}

	@Override
	protected String getValue(Object editPartModelObject) {
		// TODO avoid showing the DSL for all areas, records and sets
		SchemaSyntaxBuilder builder = 
			new SchemaSyntaxBuilder(generateAreaDSL : false, generateRecordDSL : false, generateSetDSL : false) 
		return builder.build((Schema) editPartModelObject)	
	}

}
