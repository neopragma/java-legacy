package com.neopragma.legacy.round14;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressTest {
	
	@Mock
	private CityStateLookup lookupService;
	
	@Test(expected=CityStateLookupException.class)
	public void itWrapsExceptionsThatOccurDuringCityStateLookup() {
		when(lookupService.lookup(anyString())).thenThrow(new CityStateLookupException(new Exception()));
		new AddressImpl(lookupService, "00000");
	}

	@Test
	public void nullAddressReturnsEmptyStringForCityName() {
		Address address = new NullAddress();
		assertEquals(Constants.EMPTY_STRING, address.getCity());
	}

	@Test
	public void nullAddressReturnsEmptyStringForStateName() {
		Address address = new NullAddress();
		assertEquals(Constants.EMPTY_STRING, address.getState());
	}

	@Test
	public void nullAddressReturnsEmptyStringForZipCode() {
		Address address = new NullAddress();
		assertEquals(Constants.EMPTY_STRING, address.getZipCode());
	}

	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() {
		when(lookupService.lookup("75001")).thenReturn(new CityState("Addison", "TX"));
		assertAddressFor("75001", "Addison", "TX");
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() {
		when(lookupService.lookup("856585578")).thenReturn(new CityState("Marana", "AZ"));
		assertAddressFor("856585578", "Marana", "AZ");
	}
	
	private void assertAddressFor(String zipCode, String city, String state) {
		Address address = null;
		try {
			address = new AddressImpl(lookupService, zipCode);
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
