package com.neopragma.legacy.round8;

public class CityStateLookupException extends RuntimeException {
	private static final long serialVersionUID = 1888264122085012152L;
	private Throwable wrappedException;
	
	public CityStateLookupException(Throwable wrappedException) {
		this.wrappedException = wrappedException;
	}
	
	public Throwable getWrappedException() {
		return wrappedException;
	}
}
