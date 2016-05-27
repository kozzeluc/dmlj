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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import org.lh.dmlj.schema.Schema

import groovy.transform.CompileStatic;;

@CompileStatic
abstract class AbstractSyntaxBuilder<T> {
	
	protected static final String TAB = '    '

	protected StringBuilder output = new StringBuilder()
	protected int initialTabs = 0
	protected boolean generateName = true // not applicable for schemas
	
	protected static String replaceUnderscoresBySpaces(Object anObject) {
		anObject.toString().replaceAll("_", " ")
	}
	
	protected static String withQuotes(Object anObject) {
		if (!anObject) {
			return "'null'"
		}
		String value = anObject.toString()
		if (value.indexOf('\\') > -1) {
			'$' + '/' + value + '/' + '$'
		} else if (value.indexOf("'") > -1 && value.indexOf('"') > -1) {
			"'''" + value + "'''"
		} else if (value.indexOf("'") > -1) {
			'"' + value + '"'
		} else {	
			"'" + value + "'"
		}
	}
	
	protected void blank_line() {
		newLine()
	}
	
	String build(T model) {
		doBuild(model)		
		output.toString()
	}
	
	protected abstract String doBuild(T model);
	
	private void newLine() {
		output <<= '\n'
	}
	
	protected void with_1_tab(String text) {
		write(text, 1)
	}
	
	protected void with_2_tabs(String text) {
		write(text, 2)
	}
	
	protected void with_3_tabs(String text) {
		write(text, 3)
	}
	
	protected void with_4_tabs(String text) {
		write(text, 4)
	}
	
	protected void with_5_tabs(String text) {
		write(text, 5)
	}
	
	protected void without_tab(String text) {
		write(text, 0)
	}
	
	private void write(String text, int level) {
		int factor = initialTabs + level
		output <<= "${TAB * factor}$text"
		output <<= '\n'	
	}
	
	protected String xOrY(int value) {
		if (value < 0) {
			return "'$value'"
		} else {
			return "$value"
		}
	}
}
