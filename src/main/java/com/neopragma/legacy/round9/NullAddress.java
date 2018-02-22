package com.neopragma.legacy.round9;

public class NullAddress implements Address {
	
	public NullAddress() { }

	@Override
	public String getCity() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String getState() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String getZipCode() {
		return Constants.EMPTY_STRING;
	}

}
