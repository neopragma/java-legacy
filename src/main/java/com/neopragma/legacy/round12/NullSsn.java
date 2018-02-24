package com.neopragma.legacy.round12;

public class NullSsn implements Ssn {

	@Override
	public String formatSsn() {
		return Constants.EMPTY_STRING;
	}

}
