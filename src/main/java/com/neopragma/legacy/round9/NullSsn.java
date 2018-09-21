package com.neopragma.legacy.round9;

public class NullSsn implements Ssn {

	@Override
	public String formatSsn() {
		return Constants.EMPTY_STRING;
	}

}
