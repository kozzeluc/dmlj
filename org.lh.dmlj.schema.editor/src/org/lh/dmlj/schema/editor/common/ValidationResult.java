package org.lh.dmlj.schema.editor.common;

public class ValidationResult {
	
	public static enum Status {OK, ERROR};
	
	private String message;
	private Status status;

	@SuppressWarnings("unused")
	private ValidationResult() {
		// disabled constructor
		super();
	}
	
	ValidationResult(Status status) {
		this(status, null);
	}
	
	ValidationResult(Status status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Status getStatus() {
		return status;
	}
	
}