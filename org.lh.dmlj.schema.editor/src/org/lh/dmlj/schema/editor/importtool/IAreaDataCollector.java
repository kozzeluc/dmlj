package org.lh.dmlj.schema.editor.importtool;

import java.util.Collection;

import org.lh.dmlj.schema.ProcedureCallTime;
import org.lh.dmlj.schema.AreaProcedureCallFunction;

public interface IAreaDataCollector<T> {	

	String getName(T context);	

	Collection<ProcedureCallTime> getProcedureCallTimes(T context);

	Collection<AreaProcedureCallFunction> getProcedureCallFunctions(T context);

	Collection<String> getProceduresCalled(T context);
	
}