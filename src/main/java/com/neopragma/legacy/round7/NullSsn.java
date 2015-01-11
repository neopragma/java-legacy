package com.neopragma.legacy.round7;

public class NullSsn implements Ssn {

	@Override
	public String formatSsn() {
		return Constants.EMPTY_STRING;
	}

}
