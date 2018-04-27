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
package org.lh.dmlj.schema.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.lh.dmlj.schema.Schema;
import org.lh.dmlj.schema.SchemaArea;
import org.lh.dmlj.schema.SchemaRecord;
import org.lh.dmlj.schema.Set;
import org.lh.dmlj.schema.editor.dsl.builder.model.ModelFromDslBuilderForJava;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.AreaSyntaxBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.RecordSyntaxBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SchemaSyntaxBuilder;
import org.lh.dmlj.schema.editor.dsl.builder.syntax.SetSyntaxBuilder;
import org.lh.dmlj.schema.editor.log.Logger;

public class DSLWarmUpJob extends Job {
	
	private static final Logger logger = Logger.getLogger(Plugin.getDefault());
	
	public DSLWarmUpJob() {
		super("DSL warm up");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		
		logger.info("DSL warm up started");
		
		Schema schema = ModelFromDslBuilderForJava.schema("name 'DUMMY'");
		new SchemaSyntaxBuilder().build(schema);
		
		SchemaArea area = ModelFromDslBuilderForJava.area("name 'DUMMY'");
	  	new AreaSyntaxBuilder().build(area);
		
	  	SchemaRecord record = ModelFromDslBuilderForJava.record("name 'DUMMY'");
	  	new RecordSyntaxBuilder().build(record);
	  	
	  	Set set = ModelFromDslBuilderForJava.set("name 'DUMMY'");
	  	new SetSyntaxBuilder().build(set);
	  	
	  	logger.info("DSL warm up complete");
	  	
	  	return Status.OK_STATUS;
	}
	
}
