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

import org.eclipse.core.runtime.AssertionFailedException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl
import org.lh.dmlj.schema.Schema
import org.lh.dmlj.schema.SchemaPackage

import spock.lang.Specification

abstract class AbstractSyntaxBuilderSpec extends Specification{
	
	private SchemaPackage schemaPackage = SchemaPackage.eINSTANCE
	
	protected void capture(String syntax, String targetFilePath) {
		FileWriter fw = new FileWriter(new File(targetFilePath))
		PrintWriter pw = new PrintWriter(fw)
		pw.print(syntax)
		pw.flush()
		pw.close()
		fw.close()
	}

	protected Schema loadSchema(String path) {
		URI uri = URI.createFileURI(new File(path).getAbsolutePath())
		ResourceSet resourceSet = new ResourceSetImpl()
		resourceSet.resourceFactoryRegistry.extensionToFactoryMap.put('schema', new XMIResourceFactoryImpl())
		Resource resource = resourceSet.getResource(uri, true)
		return resource.contents[0]
	}
	
	protected String expected(String syntax) {
		String p;
		if (syntax.startsWith('\n')) {
			p = syntax
		} else {
			p = syntax
		}
		p.substring(1).replaceAll("\n \n", "\n\n").replaceAll("\\'", "\\\'")
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
