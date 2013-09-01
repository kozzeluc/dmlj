package org.lh.dmlj.schema.editor.property.handler;

import org.eclipse.gef.commands.Command;

public interface IEditHandler {

	Command getEditCommand();
	
	String getMessage();
	
	boolean isValid();
	
}