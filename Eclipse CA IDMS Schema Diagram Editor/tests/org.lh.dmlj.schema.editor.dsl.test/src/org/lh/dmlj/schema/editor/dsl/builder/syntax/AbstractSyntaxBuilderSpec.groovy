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

import org.eclipse.core.runtime.AssertionFailedException
import org.lh.dmlj.schema.SchemaPackage
import org.lh.dmlj.schema.editor.dsl.builder.AbstractBuilderSpec

abstract class AbstractSyntaxBuilderSpec extends AbstractBuilderSpec {
	
	protected String expected(String syntax) {
		String p;
		if (syntax.startsWith('\n')) {
			p = syntax
		} else {
			p = syntax
		}
		p.substring(1).replaceAll("\n \n", "\n\n").replaceAll("\\'", "\\\'")
	}
	
	protected String stripLeadingAndTrailingNewLine(String syntax) {
		syntax.substring(1, syntax.length() - 1)
	}
	
	protected boolean mustMatchSyntaxInFile(String syntax, String filePath) {
		BufferedReader brf = new BufferedReader(new FileReader(filePath))
		BufferedReader brs = new BufferedReader(new StringReader(syntax))
		int lineNumber = 0
		String message = null
		String lineF = brf.readLine()
		while(lineF != null && message == null) {
			lineNumber += 1
			String lineS = brs.readLine()
			if (lineS == null) {
				message = 'line ' + lineNumber + ' not available in syntax: ' + lineF
			} else if (lineS != lineF) {
				message = 'line ' + lineNumber + ' is different:\nexpected: ' + lineF + '\nwas:      ' + lineS
			} else {
				lineF = brf.readLine()
			}
		}
		if (lineF == null && message == null) {
			lineNumber += 1
			String lineS = brs.readLine()
			if (lineS != lineF) {
				message = 'line ' + lineNumber + ' not available in file ' + filePath + ': ' + lineF
			}
		}
		brs.close()
		brf.close() 
		if (message) {
			throw new AssertionFailedException(message)
		}
		true
	}
	
}
