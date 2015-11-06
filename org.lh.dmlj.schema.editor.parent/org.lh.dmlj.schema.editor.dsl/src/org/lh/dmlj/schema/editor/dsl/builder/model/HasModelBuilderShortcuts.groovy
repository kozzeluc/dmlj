/**
 * Copyright (C) 2015  Luc Hermans
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

trait HasModelBuilderShortcuts {
	
	SchemaArea area(Closure definition) {
		AreaModelBuilder builder = new AreaModelBuilder()
		builder.build definition
	}

	SchemaArea area(String definition) {
		AreaModelBuilder builder = new AreaModelBuilder()
		builder.buildFromString definition
	}

	Element element(Closure definition) {
		ElementModelBuilder builder = new ElementModelBuilder()
		builder.build definition
	}
	
	Element element(String definition) {
		ElementModelBuilder builder = new ElementModelBuilder()
		builder.buildFromString definition
	}

	SchemaRecord record(Closure definition) {
		RecordModelBuilder builder = new RecordModelBuilder()
		builder.build definition
	}
	
	SchemaRecord record(String definition) {
		RecordModelBuilder builder = new RecordModelBuilder()
		builder.buildFromString definition
	}
	
	Schema schema(Closure definition) {
		SchemaModelBuilder builder = new SchemaModelBuilder()
		builder.build definition
	}
	
	Schema schema(String definition) {
		SchemaModelBuilder builder = new SchemaModelBuilder()
		builder.buildFromString definition
	}
	
	Set set(Closure definition) {
		SetModelBuilder builder = new SetModelBuilder()
		builder.build definition
	}
	
	Set set(String definition) {
		SetModelBuilder builder = new SetModelBuilder()
		builder.buildFromString definition
	}
	
}
