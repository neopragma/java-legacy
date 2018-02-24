# Smell: Hard-coded internationalization

[Back to top](notes.md) |  [Next: Hard-coded internationalization, part 2](notes-internationalization-2.md)

The original version of the code consisted of a single "god class" that contained logic for a variety of different responsibilities. It was obvious from the beginning that the system handled job applicant names in a clumsy way. 

We took the first step toward remediating that logic when we [separated the "name" processing from the original class](https://github.com/neopragma/java-legacy/blob/master/notes/notes-srp-violation-3.md).
Now we're ready to take another look at that code. 

When we put on our "software archaeologist" hat, we see evidence that the company expanded operations into a Spanish-speaking country and a programmer shoe-horned Spanish names into the existing code in a rush, without giving much thought to the design. 

This kind of thing happens because people think it's "faster" than restructuring the code properly. They may be right in the short term, as it's often challenging to retrofit internationalization into Java code that wasn't designed that way originally. Now we have inherited the consequences of their decision. 

Now, the company's leadership tells us there are plans to expand operations into Canada, where we will be required to support French. There have been numerous requests from the Mexican office to have the user prompts displayed in Spanish. Future plans include expanding into several other countries. The "right" way to handle this is by using Java's internationalization features. 

Differences in the structure and content of people's names aren't the only factor. Street addresses, phone numbers, decimal number formats, currency amounts, date and time formats, and text directionality also vary by locale. When we add graphical front-ends to the system, we'll also have to handle local differences in color schemes, images, and positioning of navigation elements that produce the desired culturally-sensitive reactions in our users. The time has come for us to set the stage for all this.

An English-speaking programmer might look at various name formats and conclude that they're basically the same, because most of the time there are three strings. Who cares if we call them "first name," "middle name," and "last name," as long as we store three strings?

The thing is, that's wrong. A name isn't just "three strings" (or two, or four, or whatever). The different parts of a person's name have different cultural significance. In addition, if our system needs to produce a formal letter to send to a job candidate, the way we format the salutation makes a difference. 

Java provides some default implementations to handle things like dates, times, and number formats. To support localized _names_ we must implement our own locale-specific classes, and structure our application to inject the appropriate implementation based on the locale in effect.

Let's start by examining the _Name_ interface. We want to define operations that will be common across all _name_ implementations. We see two methods declared, and both are problematic.

```java
public interface Name {
    public abstract String formatLastNameFirst();
    public abstract int validateName();
}
```

With _formatLastNameFirst()_ we're probably looking for a "standard" format that lends itself to _collation_ (that is, sorting). In English, we normally sequence people's names by surname ("last name") followed by given name ("first name") followed by middle name. But that will not produce a conceptually-equivalent result for names that have different structures. 

Even the terms "last name" and "first name" are culture-specific; we say "first name" simply because it's the part of an English name that comes first. That rule of thumb doesn't apply to all languages, and so the term "first name" isn't universally meaningful. And what does "middle" mean when the name has four parts?

The existence of a validation method suggests the _NameImpl_ class doesn't enforce validity in a constructor. It's possible to instantiate a _Name_ object that isn't valid. That's a red flag. 

Our approach will be to add method declarations to the _Name_ interface to represent functionality common to all names. We'll leave the two existing methods in place until we've completed the refactoring. Ultimately we'll be able to remove them safely.

```java
public interface Name {
    /**
     * @return Name in the standard display format for the Locale.
     */
    String displayName();
	
    /**
     * @return Name in a form suitable for sorting.
     */
    String sortableName();	
    

    /**
     * @return Name in a form suitable for sorting.
     * @deprecated use {@link #sortableName()} instead.  
     */
    @Deprecated String formatLastNameFirst();
    

    /**
     * @return 0 if valid, 6 if not valid.
     * @deprecated no replacement method; constructor will validate
     */
    @Deprecated int validateName();
}
```

If we're planning to remove these methods soon, in the course of the present refactoring, then why bother to mark them as deprecated? Because we're often interrupted in our work. We want to keep the code in a deployable state at all times. If we're interrupted and months pass before anyone returns to this piece of code, we'll want the intent to be perfectly clear. 

The changes in the _Name_ interface cause the _NameImpl_ and _NullName_ classes not to compile. We'll add minimal implementations of the new methods and run all unit checks to be sure we haven't broken anything so far.

```java
    @Override
    public String displayName() {
        return null;
    }
    @Override
    public String sortableName() {
        return null;
    }
```

The checks run green, so we can proceed. The next step is to implement English-language support in the new methods in _NameImpl_. Eventually we will have a separate implementation class for Spanish names. We're taking baby steps to get there. 

First, test cases for formatting an English name for display.  

```java
    @Test
    public void itReturnsEnglishNameWithMiddleInitialFormattedForDisplay() {
        assertEquals("First M. Last", testName.displayName());
    }
    @Test
    public void itReturnsEnglishNameWithNoMiddleNameFormattedForDisplay() {
        name = new NameImpl("First", null, "Last");
        assertEquals("First Last", name.displayName());
    }
```

...and the implementation:

```java
    private final static String displayFormatPattern 
        = "{0} {1}{2}";
    . . .
    @Override
    public String displayName() {
        return MessageFormat.format(displayFormatPattern, 
                firstName, 
                initial(middleName), 
                lastName);
    }
    . . .
    private String initial(String value) {
        if (isEmpty(value)) {
            return EMPTY_STRING;
        }
        return value.substring(0,1) + PERIOD_SPACE;
    }
    private boolean isEmpty(String value) {
        return (value == null || value == EMPTY_STRING);
    }    
```

Obviously, some details are missing from this implementation. We can't handle a suffix, like "Jr." or "III". We can't handle a double middle name, like "John Peter" abbreviated as "J.P.". The code did not handle those details before, and we're here to refactor, not to add functionality. 

And now for the _sortableName()_ method: 

```java
    @Test
    public void itReturnsSortableEnglishNameWithMiddleName() {
        assertEquals("Last Middle First", testName.sortableName());
    }

    @Test
    public void itReturnsSortableEnglishNameWithNoMiddleName() {
        name = new NameImpl("First", null, "Last");
        assertEquals("Last First", name.sortableName());
    }
```

...and the implementation: 

```java
    private final static String sortableFormatPattern = "{2} {1}{0}";
    . . .
    @Override
    public String sortableName() {
        return MessageFormat.format(sortableFormatPattern,
                firstName,
                isEmpty(middleName) ? EMPTY_STRING : middleName + SPACE,
                lastName);
    }
    
```

All the checks pass (both the new and the old), so we can proceed. 

Now let's handle validation in the constructor for English names, and make the _validateName()_ method obsolete. Instead of allowing an invalid Name object to be created, we'll throw _IllegalArgumentException_. 

As usual, we'll start by looking at the unit checks. We need four checks to cover all the bases. We find those four are already present, but they are verifying the behavior of the _validateName()_ method. 


```java
    @Test
    public void completeNameProvided() {
        assertEquals(0, testName.validateName());
    }
    @Test
    public void firstAndLastNamesProvided() {
        assertEquals(0, testName.validateName());
    }
    @Test
    public void missingFirstName() {
        name = new NameImpl(null, "Middle", "Last");
        assertEquals(6, name.validateName());
    }
    @Test
    public void missingLastName() {
        name = new NameImpl("First", "Middle", null);
        assertEquals(6, name.validateName());
    }
```

With the validation logic moved into the constructor, two of those checks become obsolete. The happy path is already covered; otherwise, none of the existing unit checks would pass. We can replace those four checks with two, to verify the _IllegalArgumentException_ exception is thrown when the first name or last name is missing. 

We'll replace those with the following:

```java
    @Test(expected=IllegalArgumentException.class)
    public void missingFirstName() {
        name = new NameImpl(null, "Middle", "Last");
    }
    @Test(expected=IllegalArgumentException.class)
    public void missingLastName() {
        name = new NameImpl("First", "Middle", null);
    }
```

Those checks fail for the right reason, so we're ready to implement the validation logic in the constructor for English names. 

```java
    public NameImpl(String firstName, String middleName, String lastName) {
        if (isEmpty(firstName) || isEmpty(lastName)) {
            throw new IllegalArgumentException();
        }
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round13```.

# Next smell

The next step is to split the English and Spanish Name implementations and introduce Java internationalization support. [Next: Hard-coded internationalization, part 2](notes-internationalization-2.md).
