# Smell: Tight coupling with an external dependency

[Back to top](notes/notes.md) | [Next: Obsolete comments mislead the reader](notes-bad-comments.md)

The ```CityStateLookup``` interface provides for _loose coupling_ between our application and the Internet-based lookup service we use to get the city and state for a given zip code. But we still have a problem: Every time the automated unit checks run, they reach out to the Internet. Our intent is not to test the remote service, but to verify that our own code handles the return values from the service correctly. We _do_ need to verify that all the pieces work together, but we _don't_ need to do that over and over again at the unit level.

The solution is to _isolate the code under test_ by supplying _test doubles_ that mimic the behavior we need from the external service. 

With Java, one way to substitute test doubles for real objects is by using _mocks_ and _stubs_. A _stub_ is a test double that has no functionality; a kind of dummy object. A _mock_ is a test double that can return a specified value when called, and that counts the number of times it has been accessed.

Many libraries are available to support this kind of functionality in Java. In this exercise, we will use one called _mockito_. The Maven POM supplied with the project already has the necessary dependencies defined. 

We are responsible for the code that

1. Sets up the call to the remote service
2. Handles any exceptions
3. Stores the returned values correctly

We are _not_ responsible for the code that

1. Interfaces with the Internet (handled by library code)
2. Performs the city and state lookup (handled by the remote service)

Here is the ```AddressTest``` class modified to use a mock for ```CityStateLookup```:

```java
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
```

Run the unit checks in package ```com.neopragma.legacy.round5``` and then run the unit checks in package ```com.neopragma.legacy.round6```. Notice the difference in run times. The version in ```round6``` (using the mock) gives us the same information about the correctness of our code, but runs in far less time because it does not depend on an Internet service.

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round6```.

## Next smell

The next smell is that the source comments in method ```lookup``` in class ```CityStateLookupImpl``` do not accurately describe the functionality of the method. This can be very misleading for programmers who are trying to understand the code. We'll [correct the problem](notes-bad-comments.md).
