/**
 * Copyright (C) 2013  Luc Hermans
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

import org.eclipse.core.runtime.Assert;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.editor.Plugin;
import org.lh.dmlj.schema.editor.property.IAreaProvider;

public abstract class AbstractAreaPropertiesSection 
	extends AbstractAttributesBasedPropertiesSection<SchemaArea> implements IAreaProvider {

	public AbstractAreaPropertiesSection() {
		super(Plugin.getDefault());
	}

	@Override
	public final SchemaArea getArea() {
		return target;
	}	
	
	@Override
	protected final SchemaArea getTarget(Object modelObject) {
		Assert.isTrue(modelObject instanceof SchemaArea, "not a SchemaArea");
		return (SchemaArea) modelObject;
	}
	
}
