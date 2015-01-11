package com.neopragma.legacy.round6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressTest {
	
	@Mock
	private CityStateLookup lookupService;
	
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
		when(lookupService.lookup("75001")).thenReturn(new CityState("Addison", "TX"));
		assertAddressFor("75001", "Addison", "TX");
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() throws URISyntaxException, IOException {
		when(lookupService.lookup("856585578")).thenReturn(new CityState("Marana", "AZ"));
		assertAddressFor("856585578", "Marana", "AZ");
	}
	
	private void assertAddressFor(String zipCode, String city, String state) {
		Address address = null;
		try {
			address = new Address(lookupService, zipCode);
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
