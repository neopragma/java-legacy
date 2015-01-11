package com.neopragma.legacy.round7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class AddressIT {
	
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
		assertAddressFor("75001", "Addison", "TX");
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() throws URISyntaxException, IOException {
		assertAddressFor("856585578", "Marana", "AZ");
	}
	
	private void assertAddressFor(String zipCode, String city, String state) {
		Address address = null;
		try {
			address = new AddressImpl(new CityStateLookupImpl(), zipCode);
			assertEquals(city, address.getCity());
			assertEquals(state, address.getState());
			assertEquals(zipCode, address.getZipCode());
		} catch(AssertionError nope) {
			fail("Expected: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state +
					" Actual: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state);
		} catch(Throwable unexpected) {
			fail("Unexpected: " + unexpected);
		}
	}
}
