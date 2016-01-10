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
package org.lh.dmlj.schema.editor.property.section;

import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.AreaSyntaxBuilder;

public class AreaDSLSection extends AbstractSectionWithStyledText {
	
	private static final Class<?>[] VALID_EDIT_PART_MODEL_OBJECTS = new Class[] {SchemaArea.class};

	public AreaDSLSection() {
		super(VALID_EDIT_PART_MODEL_OBJECTS);
	}

	@Override
	protected String getValue(Object editPartModelObject) {
		return new AreaSyntaxBuilder().build((SchemaArea) editPartModelObject);		
	}

}
