package com.neopragma.legacy.round14;

public class NullName implements Name {

	@Override
	public String displayName() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String sortableName() {
		return Constants.EMPTY_STRING;
	}
}
