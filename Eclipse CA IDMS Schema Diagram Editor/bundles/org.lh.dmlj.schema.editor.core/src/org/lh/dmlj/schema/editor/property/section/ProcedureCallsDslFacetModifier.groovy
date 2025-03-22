/**
 * Copyright (C) 2023  Luc Hermans
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

import org.eclipse.gef.commands.Command
import org.lh.dmlj.schema.SchemaArea
import org.lh.dmlj.schema.SchemaRecord
import org.lh.dmlj.schema.editor.command.ProcedureCallCommandFactory
import org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava
import org.lh.dmlj.schema.editor.dsl.builder.syntax.AreaSyntaxBuilder
import org.lh.dmlj.schema.editor.dsl.builder.syntax.RecordSyntaxBuilder
import org.lh.dmlj.schema.editor.property.exception.DSLFacetValidationException
import org.lh.dmlj.schema.editor.property.ui.IDslFacetModifier

class ProcedureCallsDslFacetModifier implements IDslFacetModifier {
	
	static areaSyntaxBuilder = { SchemaArea area -> new AreaSyntaxBuilder().build(area) }
	static areaModelBuilder = { String syntax -> ModelFromDslBuilderForJava.area(syntax) }
	
	static recordSyntaxBuilder = { SchemaRecord record -> new RecordSyntaxBuilder().build(record) }	
	static recordModelBuilder = { String syntax -> ModelFromDslBuilderForJava.record(syntax) }
	
	static relevant = { !it.trim().empty }
	static trim = { it.trim() }
	static procedureToUppercase = { String line ->  
		def i = line.indexOf(' ', 6)
		"${line.substring(0, 6)}${line.substring(6, i).toUpperCase()}${line.substring(i)}"
	} 
	
	def model	
	def buildModel
	def commandFactory = new ProcedureCallCommandFactory()
	
	String originalFacetDefinition
	String modifiedFacetDefinition
	boolean modifiedFacetDefinitionSet = false
	
	private static String buildOriginalFacetDefinition(model, buildSyntax) {
		String fullDsl = buildSyntax(model)
		StringBuilder facetDsl = new StringBuilder()
		for (String line : fullDsl.split("\n")) {
			if (line.trim().startsWith("call '")) {
				if (facetDsl.length() > 0) {
					facetDsl.append('\n')
				}
				facetDsl.append(line)
			}
		}
		facetDsl.toString()
	}
	
	static IDslFacetModifier forModel(SchemaArea area) {
		String facet = buildOriginalFacetDefinition(area, areaSyntaxBuilder)
		new ProcedureCallsDslFacetModifier(model : area,
										   originalFacetDefinition : facet,
										   buildModel : areaModelBuilder)
	}
	
	static IDslFacetModifier forModel(SchemaRecord record) {
		String facet = buildOriginalFacetDefinition(record, recordSyntaxBuilder)
		new ProcedureCallsDslFacetModifier(model : record,
										   originalFacetDefinition : facet,
										   buildModel : recordModelBuilder)
	}
	
	@Override
	public Command getCommand() {
		assert hasChanges(), 'command cannot be created: no changes'
		if (modifiedFacetDefinition.trim()) {
			commandFactory.createCommand(model, modifiedFacetDefinition.split('\n').collect( { it.substring(6, it.size() - 1) } ))
		} else {
			commandFactory.createCommand(model, [])
		} 		
	}

	@Override
	public boolean hasChanges() {
		modifiedFacetDefinitionSet && originalFacetDefinition != modifiedFacetDefinition
	}

	@Override
	String getModelName() {
		model.name
	}

	@Override
	String getModelType() {
		model instanceof SchemaArea ? 'area' : 'record'
	}

	@Override
	void setModifiedFacetDefinition(String modifiedFacetDefinition)
		throws DSLFacetValidationException {
		
		verifyCallStructure(modifiedFacetDefinition)
		try {
			buildModel(modifiedFacetDefinition.trim())
			this.modifiedFacetDefinition = 
				modifiedFacetDefinition.split('\n').findAll(relevant).collect(trim).collect(procedureToUppercase).join('\n')
				modifiedFacetDefinitionSet = true
		} catch (Exception e) {
			this.modifiedFacetDefinition = null
			throw new DSLFacetValidationException(e.message, e, modifiedFacetDefinition)
		}
	}
		
	private void verifyCallStructure(String modifiedFacetDefinition) throws DSLFacetValidationException {
		String[] lines = modifiedFacetDefinition.split('\n')
		for (int i = 0; i < lines.length; i++) {
			String trimmedLine = lines[i].trim()
			boolean valid = trimmedLine.isEmpty() ||
				  			trimmedLine.startsWith("call '") && trimmedLine.endsWith("'") ||
							trimmedLine.startsWith('call "') && trimmedLine.endsWith('"')
			if (!valid) {				
			  	String message = "line ${i + 1} does not start with \"call '\" (or 'call \"') or end with a single quote:\n${lines[i]}"
				throw new DSLFacetValidationException(message, null, modifiedFacetDefinition)
			}
		}
	}

}
