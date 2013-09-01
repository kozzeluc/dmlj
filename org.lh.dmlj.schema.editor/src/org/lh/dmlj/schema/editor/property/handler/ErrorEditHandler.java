package org.lh.dmlj.schema.editor.property.handler;

import org.eclipse.gef.commands.Command;

public class ErrorEditHandler implements IEditHandler {

	private String message;
	
	@SuppressWarnings("unused")
	private ErrorEditHandler() {
		super();
	}
	
	public ErrorEditHandler(String message) {
		super();
		this.message = message;
	}
	
	@Override
	public Command getEditCommand() {
		return null;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public boolean isValid() {
		return false;
	}

}