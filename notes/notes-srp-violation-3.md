# Smell: JobApplicant class has multiple responsibilities (part 3)

[Back to top](notes.md) | [Next: Poor code isolation](notes-isolation-1.md)

Now we're going to extract the logic pertaining to _name_ from the ```JobApplicant``` class.

We'll begin by extracting the unit checks from ```JobApplicantTest``` to a new class, ```NameTest```. Then we run the unit checks to make sure we didn't inadvertently break something in the process of creating the new test class.

Moving the unit checks for _name_ to the ```NameTest``` class leaves ```JobApplicantTest``` without any checks at all. We'll be examining the code to see what unit checks make sense for ```JobApplicant```. For now, we're going to focus on the _name_ checks and the corresponding functionality.

Now we create a new production class, ```Name```, and extract all the logic related to _name_ from ```Job Applicant``` to ```Name```. Initially we just move the code as-is. That results in a couple of compile-time errors in ```JobApplicant```, but we're going to work with the ```Name``` class first.

```Name``` now contains a couple of _setters_ that we moved from ```JobApplicant```:

```java
	public void setName(String firstName, String middleName, String lastName) {
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
	}
	
	public void setSpanishName(String primerNombre, String segundoNombre,
							   String primerApellido, String segundoApellido) {
		this.firstName = primerNombre == null ? "" : primerNombre;
		this.middleName = segundoNombre == null ? "" : segundoNombre;
		if ( primerApellido != null ) {
  		    StringBuilder sb = new StringBuilder(primerApellido);
		    sb.append(segundoApellido == null ? null : " " + segundoApellido);
		    this.lastName = sb.toString();
		} else {
			this.lastName = "";
		}
	}
```

We want instances of ```Name``` to be immutable, so we're going to change the _setters_ into _constructors_. Taking a _test-first_ approach, we first change code in ```NameTest``` that looks like this...

```java
	private JobApplicant jobApplicant;

	@Before
	public void beforeEach() {
		jobApplicant = new JobApplicant(new CityStateLookupImpl());
	}
	
	@Test
	public void completeNameProvided() {
		jobApplicant.setName("First", "Middle", "Last");
		assertEquals(0, jobApplicant.validateName());
	}
```

...into code that looks like this:

```java
	private Name name;
	
	@Test
	public void completeNameProvided() {
		name = new Name("First", "Middle", "Last");
		assertEquals(0, name.validateName());
	}
```

Next, we change the _setters_ in ```Name``` into _constructors_ to match what we are expecting in our unit checks:

```java
	public Name(String firstName, String middleName, String lastName) {
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
	}
	
	public Name(String primerNombre, String segundoNombre,
							   String primerApellido, String segundoApellido) {
		this.firstName = primerNombre == null ? "" : primerNombre;
		this.middleName = segundoNombre == null ? "" : segundoNombre;
		if ( primerApellido != null ) {
  		    StringBuilder sb = new StringBuilder(primerApellido);
		    sb.append(segundoApellido == null ? null : " " + segundoApellido);
		    this.lastName = sb.toString();
		} else {
			this.lastName = "";
		}
	}
```

Now we run the unit checks in ```NameTest```. We can't run _all_ the unit checks for the project, because we still haven't fixed ```JobApplicant```.

The ```add``` method of ```JobApplicant``` takes separate arguments for ```firstName```, ```middleName```, and ```lastName```. 

```java
	public void add(String firstName,
			       String middleName,
			       String lastName,
			       String ssn,
			       String zipCode) throws URISyntaxException, IOException {
		setName(firstName, middleName, lastName);
		setSsn(ssn);
		setAddress(zipCode);
		save();
	}
```

We want it to take a single argument of type ```Name``` and save the value in an instance variable.

```java
	public void add(Name name,
			       Ssn ssn,
			       Address address) {
		this.name = name;
		this.ssn = ssn;
		this.address = address;
		save();
	}
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round5```.

## Next smell

The ```lookup``` method in ```CityStateLookupImpl``` goes out to the Internet to use a remote service to perform the lookup. This represents _tight coupling_ with an external dependency. Our next improvement will be to loosen this coupling. We'll [isolate our code](notes-isolation-1.md) by introducing an interface, and mock the service in our unit checks.
