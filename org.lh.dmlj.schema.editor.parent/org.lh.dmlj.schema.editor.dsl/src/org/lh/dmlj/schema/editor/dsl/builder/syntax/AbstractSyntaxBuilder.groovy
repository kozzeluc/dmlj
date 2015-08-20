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
package org.lh.dmlj.schema.editor.dsl.builder.syntax

import org.lh.dmlj.schema.Schema;

abstract class AbstractSyntaxBuilder<T> {
	
	private static final String TAB = '    '

	protected T model
	protected def output = ''
	protected int initialTabs = 0;
	protected boolean generateName = true; // not applicable for schemas
	
	protected static String replaceUnderscoresBySpaces(Object anObject) {
		anObject.toString().replaceAll("_", " ")
	}
	
	protected static String withQuotes(Object anObject) {
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
		this.model = model
		build()		
		output.toString()
	}
	
	protected abstract String build();
	
	def methodMissing(String name, arguments) {
		if (name.startsWith('with_') && name.endsWith('_tabs') && arguments.size() == 1 && 
			(arguments[0] instanceof String || arguments[0] instanceof GString)) {
			
			int i = name.indexOf('_tabs')
			int level = Integer.valueOf(name.substring(5, i))
			AbstractSyntaxBuilder.metaClass."$name" = { String text ->				
				delegate.write(text, level)					
			}
			return "$name"(arguments[0])
		}
		throw new MissingMethodException(name, getClass(), arguments)		
	}
	
	private void newLine() {
		output <<= '\n'
	}
	
	protected void with_1_tab(String text) {
		write(text, 1)
	}
	
	protected void without_tab(String text) {
		write(text, 0)
	}
	
	private void write(String text, int level) {
		int factor = initialTabs + level
		output <<= "${TAB * factor}$text"
		output <<= '\n'	
	}
	
}
