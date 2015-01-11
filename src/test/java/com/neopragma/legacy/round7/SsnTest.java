package com.neopragma.legacy.round7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SsnTest {
	
	private SsnImpl ssn;
	private static final String EXPECTED = "Expected InvalidSsnException but ";
	private static final String SSN_LENGTH_MSG = "Ssn must be exactly nine digits";
	private static final String SSN_AREA_NUMBER_MSG = "Ssn cannot start with 000, 666, or 9";
	private static final String SSN_SERIAL_NUMBER_MSG = "Ssn serial number cannot be 0000";
	private static final String SSN_RESERVED_MSG = " is a reserved Ssn that cannot be used";

	@Test
	public void nullSsnReturnsEmptyString() {
		Ssn ssn = new NullSsn();
		assertEquals(Constants.EMPTY_STRING, ssn.formatSsn());
	}
	
	
	@Test
	public void ssnWithoutDashes() {
		ssn = new SsnImpl("123456789");
		assertEquals("123-45-6789", ssn.formatSsn());
	}

	@Test
	public void validSsnWithDashes() {
		ssn = new SsnImpl("123-45-6789");
		assertEquals("123-45-6789", ssn.formatSsn());
	}
	
	@Test
	public void ssnWithDashesInWrongPlaces() {
		assertExceptionMessage("12-3456-789", SSN_LENGTH_MSG);
	}
	
	@Test
	public void ssnIsTooShort() {
		assertExceptionMessage("12345678", SSN_LENGTH_MSG);
	}
	
	@Test
	public void ssnIsTooLong() {
		assertExceptionMessage("1234567890", SSN_LENGTH_MSG);
	}
	
	@Test
	public void ssnAreaNumberIs000() {
		assertExceptionMessage("000223333", SSN_AREA_NUMBER_MSG);
	}
	
	@Test
	public void ssnAreaNumberIs666() {
		assertExceptionMessage("666223333", SSN_AREA_NUMBER_MSG);
	}
	
	@Test
	public void ssnAreaNumberStartsWith9() {
		assertExceptionMessage("900223333", SSN_AREA_NUMBER_MSG);
	}
	
	@Test
	public void ssnSerialNumberIs0000() {
		assertExceptionMessage("111220000", SSN_SERIAL_NUMBER_MSG);
	}
	
	@Test
	public void itRejectsSsn078051120() {
		assertExceptionMessage("078051120", "078051120" + SSN_RESERVED_MSG);
	}
	
	@Test
	public void itRejectsSsn219099999() {
		assertExceptionMessage("219099999", "219099999" + SSN_RESERVED_MSG);
	}
	
	private void assertExceptionMessage(String ssnValue, String message) {
		try {
			ssn = new SsnImpl(ssnValue);
			fail(EXPECTED + "no exception was thrown.");
		} catch (InvalidSsnException e1) {
			assertEquals(message, e1.getMessage());
		} catch (Exception e2) {
			fail(EXPECTED + "got " + e2.getClass().getName());
		}
		
	}

}
