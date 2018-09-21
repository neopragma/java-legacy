# Smell: Code contains magic numbers

[Back to top](notes.md) | [Next: Missing case](notes-missing-case.md)

Class ``NameImpl`` has a method, ```validateName()```, that returns 0 when the name passes validation checks, and 6 when the name does not pass. There is no explanation of the meaning of these codes. We can surmise 0 means success and 6 means failure. 

```java
	@Override
	public int validateName() {
		if ( firstName.length() > 0 && lastName.length() > 0 ) {
			return 0;
		} else {
			return 6;
		}
	}

```

We have a couple of choices. We could define constants representing _success_ and _failure_ and assign the values 0 and 6 to the constants, or we could "get fancy" and define a return value enum or throw an exception. 

According to [Hyrum's Law](http://www.hyrumslaw.com), there's a strong possibility that client code exists somewhere in the world that depends on these return values. Sometimes people get carried away when they refactor, and they change things that look questionable without verifying the change will not break clients. As we don't know what clients might exist for this code, the safest course is to clean up the literals without changing the way client code interacts with the method. 

We have a place to add constants. It's called ```Constants```. We'll add a constant there to represent the invalid name return code:

```java
public interface Constants {
	public static final String EMPTY_STRING = "";
	public static final int OK = 0;
	public static final int BAD_NAME = 6;
}
```

Now we'll have ```NameImpl``` implement the ```Constants``` interface and use the constants instead of hard-coded return values:

```java
public class NameImpl implements Name, Constants {	
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
// a lot of code here 
	@Override
	public int validateName() {
		if ( firstName.length() > 0 && lastName.length() > 0 ) {
			return OK;
		} else {
			return BAD_NAME;
		}
	}
}	
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round9```.

## Next smell

Next smell: [Missing case](notes-missing-case.md).
