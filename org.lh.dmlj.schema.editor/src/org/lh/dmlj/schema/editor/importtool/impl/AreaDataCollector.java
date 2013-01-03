package org.lh.dmlj.schema.editor.importtool.impl;

import java.util.Collection;
import java.util.Collections;

import org.lh.dmlj.schema.AreaProcedureCallFunction;
import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.editor.importtool.IAreaDataCollector;

public class AreaDataCollector 
	implements IAreaDataCollector<SchemaSyntaxWrapper> {

	public AreaDataCollector() {
		super();
	}

	@Override
	public String getName(SchemaSyntaxWrapper context) {
		return context.getList().get(1).substring(18).trim();
	}

	@Override
	public Collection<ProcedureCallTime> getProcedureCallTimes(SchemaSyntaxWrapper context, 
															   String procedureName) {
		return null;
	}

	@Override
	public Collection<AreaProcedureCallFunction> getProcedureCallFunctions(SchemaSyntaxWrapper context, 
																		   String procedureName) {
		return null;
	}

	@Override
	public Collection<String> getProceduresCalled(SchemaSyntaxWrapper context) {
		return Collections.emptyList();
	}

}