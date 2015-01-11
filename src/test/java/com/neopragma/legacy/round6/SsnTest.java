package com.neopragma.legacy.round6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SsnTest {
	
	private Ssn ssn;
	
	@Test
	public void ssnFormattingTest() {
		ssn = new Ssn("123456789");
		assertEquals("123-45-6789", ssn.formatSsn());
	}

	@Test
	public void validSsnWithNoDashes() {
		ssn = new Ssn("123456789");
		assertEquals(0, ssn.validateSsn());
	}
	
	@Test
	public void ssnWithDashesInWrongPlaces() {
		ssn = new Ssn("12-3456-789");
		assertEquals(1, ssn.validateSsn());
	}

	@Test
	public void validSsnWithDashes() {
		ssn = new Ssn("123-45-6789");
		assertEquals(0, ssn.validateSsn());
	}
	
	@Test
	public void ssnIsTooShort() {
		ssn = new Ssn("12345678");
		assertEquals(1, ssn.validateSsn());
	}
	
	@Test
	public void ssnIsTooLong() {
		ssn = new Ssn("1234567890");
		assertEquals(1, ssn.validateSsn());
	}
	
	@Test
	public void ssnAreaNumberIs000() {
		ssn = new Ssn("000223333");
		assertEquals(2, ssn.validateSsn());
	}
	
	@Test
	public void ssnAreaNumberIs666() {
		ssn = new Ssn("666223333");
		assertEquals(2, ssn.validateSsn());
	}
	
	@Test
	public void ssnAreaNumberStartsWith9() {
		ssn = new Ssn("900223333");
		assertEquals(2, ssn.validateSsn());
	}
	
	@Test
	public void ssnSerialNumberIs0000() {
		ssn = new Ssn("111220000");
		assertEquals(3, ssn.validateSsn());
	}
	
	@Test
	public void itRejectsSsn078051120() {
		ssn = new Ssn("078051120");
		assertEquals(4, ssn.validateSsn());
	}
	
	@Test
	public void itRejectsSsn219099999() {
		ssn = new Ssn("219099999");
		assertEquals(4, ssn.validateSsn());
	}

}
