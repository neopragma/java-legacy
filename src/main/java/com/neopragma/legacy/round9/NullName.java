package com.neopragma.legacy.round9;

public class NullName implements Name {

	@Override
	public String formatLastNameFirst() {
        return Constants.EMPTY_STRING;
	}

	@Override
	public int validateName() {
		return Constants.OK;
	}

}
