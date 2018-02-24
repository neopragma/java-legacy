# Smell: Hard-coded internationalization, part 2

[Back to top](notes.md) 

At this point, the _NameImpl_ class contains all the logic necessary to support both English and Spanish name formats. The next step is for us to split the class into two. Then we'll be able to delete the obsolete methods from the _Name_ interface and the two implementation classes.

Let's start by doing a _rename_ refactoring to change _NameImpl_ to _EnglishName_. The we run all unit checks, and we see they are still passing. 

Now we want to create a new class, _SpanishName_ that implements the _Name_ interface. We'll do that by writing unit checks that express the behavior we want to see from the new class. To prevent the _NameTest_ class from growing too large to be easily readable, we'll create a separate _SpanishNameTest_ class, and rename _NameTest_ to _EnglishNameTest_. 

This doesn't imply it's generally a good practice to make a one-for-one correspondence between test classes and production classes. It just seems to make sense in this situation.

Running the checks, we discover we've created a regression in _PersistenceImpl_.


```java
    @Override
    public void save(JobApplicant data) {
        System.out.println("(fake) Saving job applicant " 
            + data.getName().formatLastNameFirst());
    }
```

The _formatLastNameFirst()_ no longer exists. _PersistenceImpl_ is calling it to display an informational message, so we should be able to replace that with a call to _displayName()_. 

After that change, all the checks are passing. 

At this point, we have a clean separation between English and Spanish name handling, but we don't have a mechanism to inject the appropriate implementation based on locale. 

The typical way to do that in Java is to use a Factory pattern; either _Factory Method_ or _Abstract Factory_. In keeping with YAGNI, we'll use the simpler of the two, a _Factory Method_.

First, we'll tweak the signatures of the constructors in the _Name_ implementations, like this:

```java
    /**
     * Constructor for English names
     * @param nameParts - first, middle, last
     */
    public EnglishName(String...nameParts) {
        if (nameParts.length != 3 ||
                isEmpty(nameParts[0]) ||
                isEmpty(nameParts[2])) {
            throw new IllegalArgumentException()
        }
        firstName = nameParts[0];
        middleName = nameParts[1];
        lastName = nameParts[2];
	}
```

...and for _SpanishName_:

```java
    /**
     * Constructor for Spanish names
     * @param nameParts - primer nombre, segundo nombre,
     *                  primer apellido, segundo apellido
     */
    public SpanishName(String...nameParts) {
        if (nameParts.length != 4 ||
                isEmpty(nameParts[0]) ||
                isEmpty(nameParts[2])) {
            throw new IllegalArgumentException();
        }
        primerNombre = nameParts[0];
        segundoNombre = nameParts[1];
        primerApellido = nameParts[2];
        segundoApellido = nameParts[3];
    }
```

Why make that change? We know we're moving toward a _Factory Method_ implementation that will instantiate a _Name_ object by reflection. We also know that names in different languages are made up of one or more string values, but not always the same number of string values. If all _Name_ constructors take a single argument of type ```String[]```, then the implementation of the factory method will be simpler than if each implementation's constructor has a unique signature. It will be easy to add support for more locales this way. 

Let's start test-driving the factory method by introducing a call in just one of the unit checks. In _EnglishNameTest_, method _itReturnsEnglishNameWithNoMiddleNameFormattedForDisplay()_ doesn't use the standard test object because it has to pass a null value, and it also doesn't throw any exceptions (intentionally). It's a good candidate to try our new factory method. 

We write a factory method call like this (knowing there's no factory method yet):

```java
    @Test
    public void itReturnsEnglishNameWithNoMiddleNameFormattedForDisplay() {
        name = NameFactory.newInstance(Locale locale, "First", null, "Last");
        assertEquals("First Last", name.displayName());
    }
```

To make this compile, we need to create a factory class, _NameFactory_, with a static method _newInstance()_ to instantiate a Locale-aware _Name_ object: 

```java
    public static Name newInstance(Locale locale, String...nameParts) {
        ResourceBundle rb = ResourceBundle.getBundle("locale", locale);
        String nameClassName = rb.getString("name.class");
        try {
            Class nameClass = Class.forName(nameClassName);
            Constructor constructor =
                    nameClass.getDeclaredConstructor(String[].class);
            return (Name) constructor.newInstance((Object) nameParts);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
```

There's something about this method that may not be obvious unless you've encountered it before. Look at the _return_ statement. We have to cast argument _nameParts_ to _Object_, although it is really a _vararg_ argument of type _String[]_. Without the cast to _Object_, Java will only pass the first String in the array, and that line will throw _IllegalArgumentException_ with "wrong number of arguments". Casting to _String[]_ results in a compiler error. This behavior is not very intuitive.

For our implementation, the _ResourceBundle_ will be populated by a _properties_ file. We create two files: 

- locale.properties
- locale_es.properties 

This naming convention will make the non-suffixed filename the default. That's where we'll define our English-language settings. Settings for other languages will live in files with locale-specific suffixes. 

So, _locale.properties_ contains: 

```
name.class = com.neopragma.legacy.round14.EnglishName
```

...and _locale_es.properties_ contains: 

```
name.class = com.neopragma.legacy.round14.SpanishName
```

Now we can extend this new syntax to the other places in the code where _Name_ objects are instantiated. 


## Sample solution

The sample solution is in package ```com.neopragma.legacy.round14```.

With that, we have completed the refactorings designed into the exercise. Can you think of more improvements to make? If so, let us know and we'll add them to the sample. 
