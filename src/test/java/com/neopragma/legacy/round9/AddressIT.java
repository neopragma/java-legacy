package com.neopragma.legacy.round9;

import com.neopragma.legacy.round8.Address;
import com.neopragma.legacy.round8.AddressImpl;
import com.neopragma.legacy.round8.CityStateLookupImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AddressIT {
	
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() {
		assertAddressFor("75001", "Addison", "TX");
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() {
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
