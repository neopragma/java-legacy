package com.neopragma.legacy.round13;

public class NullJobApplicant implements JobApplicant {

	@Override
	public Address getAddress() {
		return new NullAddress();
	}

	@Override
	public Integer getId() {
		return 0;
	}

	@Override
	public Name getName() {
		return new NullName();
	}

	@Override
	public Ssn getSsn() {
		return new NullSsn();
	}

}
