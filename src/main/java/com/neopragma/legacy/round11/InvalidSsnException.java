package com.neopragma.legacy.round11;

public class InvalidSsnException extends RuntimeException {

	private static final long serialVersionUID = 7497182147644169802L;
	private String message;
	
	public InvalidSsnException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
