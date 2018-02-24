package com.neopragma.legacy.round13;

public class NullName implements Name {

	@Override
	public String displayName() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String sortableName() {
		return Constants.EMPTY_STRING;
	}

	@Override
	public String formatLastNameFirst() {
        return Constants.EMPTY_STRING;
	}

	@Override
	public int validateName() {
		return Constants.OK;
	}

}
