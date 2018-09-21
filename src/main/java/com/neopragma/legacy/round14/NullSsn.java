package com.neopragma.legacy.round14;

public class NullSsn implements Ssn {

	@Override
	public String formatSsn() {
		return Constants.EMPTY_STRING;
	}

}
