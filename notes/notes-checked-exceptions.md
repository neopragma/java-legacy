# Smell: Law of Demeter - client code must know about checked exceptions

[Back to top](notes.md) | [Next: API documentation](notes-api-doc.md)

Take a look at the declaration of the ```main``` method in class ```Main```:

```java
	public static void main(String[] args) throws URISyntaxException, IOException {
	    . . .
	}    
```

If you examine the rest of the method, you won't find any code that can throw ```URISyntaxException``` or ```IOException```. Why must they be declared here?

It will not be obvious to a new team member who has to maintain this code, but the reason is:

1. ```main``` calls ```new AddressImpl(...)```
2. An instance of ```CityStateLookupImpl``` is passed on the constructor call for ```AddressImpl```
3. The ```lookup``` method in ```CityStateLookupImpl``` accesses an Internet service to do the lookup, using Apache's ```HttpClient``` library to connect to the service.
4. Methods in ```HttpClient``` can throw ```URISyntaxException``` and ```IOException```.

This is an example of _tight coupling_. Our ```main``` method is tightly coupled to a particular implementation of the ```CityStateLookup``` interface. 

The solution is to wrap the checked exceptions in our ```CityStateLookupImpl``` class in a runtime exception so that client code need not know how the lookup functionality is implemented. If we decide to use a different implementation - say, a local table lookup - then our code will not break because it does or doesn't declare certain checked exceptions.

The exception class could look something like this:

```java
public class CityStateLookupException extends RuntimeException {
	private static final long serialVersionUID = 1888264122085012152L;
	private Throwable wrappedException;
	
	public CityStateLookupException(Throwable wrappedException) {
		this.wrappedException = wrappedException;
	}
	
	public Throwable getWrappedException() {
		return wrappedException;
	}
}
```

This enables us to remove the declarations of checked exceptions, and remove imports for those exceptions, from:

- ```AddressImpl```
- ```AddressIT```
- ```AddressTest```
- ```Main```
- ```CityStateLookup```
- ```CityStateLookupImpl```

And we need a unit check in ```AddressTest``` to ensure our code catches and wraps any exceptions that might occur:

```java
	@Test(expected=CityStateLookupException.class)
	public void itWrapsExceptionsThatOccurDuringCityStateLookup() {
		when(lookupService.lookup(anyString())).thenThrow(new CityStateLookupException(new Exception()));
		new AddressImpl(lookupService, "00000");
	}
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round8```.

## Next smell

Let's get rid of [hard-coded literals](notes-api-doc.md).
