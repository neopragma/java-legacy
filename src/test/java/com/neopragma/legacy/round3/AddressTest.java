package com.neopragma.legacy.round3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class AddressTest {
	
	private JobApplicant jobApplicant;
	@Before

	public void beforeEach() {
		jobApplicant = new JobApplicant(new CityStateLookupImpl());
	}	
	
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
		assertAddressFor("75001", "Addison", "TX");
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() throws URISyntaxException, IOException {
		assertAddressFor("856585578", "Marana", "AZ");
	}
	
	private void assertAddressFor(String zipCode, String city, String state) {
		try {
			jobApplicant.setAddress(zipCode);
			assertEquals(city, jobApplicant.getAddress().getCity());
			assertEquals(state, jobApplicant.getAddress().getState());
			assertEquals(zipCode, jobApplicant.getAddress().getZipCode());
		} catch(AssertionError nope) {
			fail("Expected: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state +
					" Actual: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state);
		} catch(Throwable unexpected) {
			fail("Unexpected: " + unexpected);
		}
	}
}
