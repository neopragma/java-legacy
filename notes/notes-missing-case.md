# Smell: Missing case

[Back to top](notes.md) | [Next: Crufty code](notes-ugly-code-1.md)

We want to treat our test suite as a first-class citizen of the code base, so we look for code smells there as well as in the production code. We have checks at the unit and integration levels to see if the city/state lookup by zip code works, and to see that it throws an exception in case of a technical problem accessing the remote service. That's good, but something is missing: What if there's no technical problem with the service, but it simply does not find a city and state that match the zip code we submit?

We could check this at the unit level, but it would be easy to fool ourselves by telling our mock to return what we want to see. Let's define a test case at the integration level, in ```AddressIT```, to express the behavior we'd like to see when the zip code is not found:

```java

	@Test(expected=CityStateLookupException.class)
	public void itThrowsWhenZipCodeIsNotFound() {
		new AddressImpl(new CityStateLookupImpl(), "99999");
	}
```

(The existing code base uses JUnit4, so we're sticking with that instead of introducing JUnit5 syntax piecemeal.)

The check is saying we want to see a CityStateLookupException in the case when the service works but the zip code is not found. 

Is it a _check_ or a _test_? It depends on how it's used. Right now, we're using it to explore the behavior of the system, so it's a _test_. Once we learn the behavior of the system, the same method will be a _check_. Not sure what I'm talking about? Read this: [Testing and Checking Refined](http://www.satisfice.com/blog/archives/856).

Whatever you choose to call it, let's run it and see what we can learn:

``` 
java.lang.AssertionError: Expected exception: 
    com.neopragma.legacy.round10.CityStateLookupException
```

So, it looks like the production code doesn't throw an exception when the zip code is not found. Well, what _does_ it do, then? Let's adjust our test case and find out:

```java
	@Test
	public void itThrowsWhenZipCodeIsNotFound() {
		assertAddressFor("99999", "Foo City", "AL");
	}
```

And the output from the test case is:

```java
java.lang.AssertionError: 
Expected: zipCode <99999>, city <Foo City>, state <AL 
Actual: zipCode <99999>, city <Foo City>, state <AL>
```

Um...wait...the case failed, but the actual and expected values are the same. Can that be right? 

No, it can't. We've discovered another problem with the existing code base: The assertion code in ```AddressIT``` is wrong. 

```java
fail("Expected: zipCode <" + zipCode + ">, city <" + city + ">, state <" 
    + state + " Actual: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state);
```

It's emitting the "expected" values for both "expected" and "actual" results. 

This often happens in real life. As we drill down through the code step by step, we encounter errors we didn't know were present. We have to fix them in order to accomplish our original goal. 

So...

```java
fail("Expected: zipCode <" + zipCode + ">, city <" + city + ">, state <" 
    + state + " Actual: zipCode <" + address.getZipCode() + ">, city <" + address.getCity() + ">, state <" + address.getState());
```

...and...

```
java.lang.AssertionError: Expected: zipCode <99999>, city <Foo City>, state <AL 
Actual: zipCode <99999>, city <Facts>, state <& 
```

The more we look at the implementation, the less we like it. I'll spare you the details, as you can see the code for yourself, but as we pursue this we'll soon discover the code isn't calling a _service_, it's merely posting a web form on a web page. The server returns data suitable for display on a web page. The implementation code plows through the web page stupidly. The "not found" result returns an HTTP 200 status code, so we can't distinguish "found" from "not found" by interrogating the status code. We learn that the text, "No records match your search parameters" is displayed on the web page. 

Clearly we need to re-think the city/state lookup code. The current implementation is fragile. But as usual we're refactoring incrementally in the course of our regular work. We don't have time to stop and chase this rabbit down a hole. As an expedient, we'll look for the "not found" text message in the data returned from the server, and throw an exception. 

Our test case ends up like this:

```java

	@Test(expected=CityStateLookupException.class)
	public void itThrowsWhenZipCodeIsNotFound() {
		new AddressImpl(new CityStateLookupImpl(), "99999");
	}
```

...and the implementation:

```java
    /**
     * This method calls the internet-based service
     * www.zip-codes.com/search to find the city and state
     * that correspond to the zip code passed in.
     *
     * @param zipCode
     * @return CityState object containing the city and state
     * @throws CityStateLookupException
     */
    //TODO: Re-think this implementation
	@Override
	public CityState lookup(String zipCode) {
		try {
			URI uri = new URIBuilder()
            .setScheme("http")
            // more code here
            String city = "";
            String state = "";
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                  	BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
           	    	StringBuffer result = new StringBuffer();
          		    String line = "";
           		    while ((line = rd.readLine()) != null) {
           			    result.append(line);
       		        }
                    if (result.toString().contains("No records match your search parameters.")) {
           		        throw new RuntimeException("No match");
                    }
                    int metaOffset = result.indexOf("<meta ");
                    int contentOffset = result.indexOf(" content=\"Zip Code ", metaOffset);
    // more code here
  }		
```





## Sample solution

The sample solution is in package ```com.neopragma.legacy.round10```.

## Next smell

Next smell: [Next: Crufty code](notes-ugly-code-1.md).
