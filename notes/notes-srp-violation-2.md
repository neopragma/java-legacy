# Smell: JobApplicant class has multiple responsibilities (part 2)

[Back to top](notes/notes.md) | [Next: Extracting the Name class](notes-srp-violation-3.md)

Now we're going to extract the logic pertaining to _social security number_ from the ```JobApplicant``` class, just as we did for _address_ logic.

We'll begin by extracting the unit checks from ```JobApplicantTest``` to a new class, ```SsnTest```. Then we run the unit checks to make sure we didn't inadvertently break something in the process of creating the new test class.

Now we create a new production class, ```Ssn```, and extract all the logic related to social security numbers from ```Job Applicant``` to ```Ssn```.

That didn't result in many problems in the production code, although it confused the test code. In ```JobApplicant``` we now have a problem in the constructor:

```java
	public void add(String firstName,
			       String middleName,
			       String lastName,
			       String ssn,
			       String zipCode) throws URISyntaxException, IOException {
		setName(firstName, middleName, lastName);
		setSsn(ssn);                      <== there's no longer a setSsn() method in this class
		setAddress(zipCode);
		save();
	}
```

Instead of setting the social security number as a String instance variable, we need to pass in an instance of ```Ssn``` as a constructor argument. That instance will already contain a valid social security number. 

But the rest of the code in class ```JobApplicant``` is not in good shape for us to introduce that functionality. As an interim step, we instantiate ```Ssn``` in the ```add``` method of ```JobApplicant```. 

```java
	public void setSsn(String ssn) {
		this.ssn = new Ssn(ssn);
	}
	
	public Ssn getSsn() {
		return ssn;
	}

```

Eventually it would be good to instantiate ```JobApplicant``` as a valid, immutable object. We will have to sneak up on that goal step by step.

Now our ```SsnTest``` class is broken. You might have noticed we violated our test-first process here. We've changed production code before the tests were failing for the right reason. They're failing, but it's because we trying to test social security number functionality through the ```JobApplicant``` class.

But...isn't that what we just did, _on purpose_, for the ```Address``` class? Yes. Remember the reason: Instances of ```Address``` are immutable value objects. The interesting logic pertaining to _address_ resides in the ```Job Applicant``` class. 

Now we have a different situation. The ```Ssn``` class is _not_ just a value object. It's a domain entity object that encapsulates data and operations. We should define our unit checks directly against ```Ssn``` rather than ```JobApplicant```. 

We accomplish this by changing _this_ sort of code...

```java
	@Test
	public void ssnFormattingTest() {
		jobApplicant.setSsn("123456789");
		assertEquals("123-45-6789", jobApplicant.formatSsn());
	}

	@Test
	public void validSsnWithNoDashes() {
		jobApplicant.setSsn("123456789");
		assertEquals(0, jobApplicant.validateSsn());
	}
```

...into _this_ sort of code:

```java
	public void ssnFormattingTest() {
		ssn = new Ssn("123456789");
		assertEquals("123-45-6789", ssn.formatSsn());
	}

	@Test
	public void validSsnWithNoDashes() {
		ssn = new Ssn("123456789");
		assertEquals(0, ssn.validateSsn());
	}
```

After these changes, all our unit checks run clean.

## Checkpoint

We've extracted two out of four responsibilities from the ```JobApplicant``` class into their own classes. These two cases illustrate some of the differences you will encounter when cleaning up legacy code:

1. In the case of ```Address```, there was a fair amount of clean-up work to do to eliminate duplicate code and to dispense with the ```CityState``` value object once the values have been returned from the lookup method. 
2. The unit checks for ```Address``` actually validate functionality in ```JobApplicant```, as that is where the action happens and that is where the risk of bugs exists. The ```Address``` class is nothing but a data container...at least for now.
3. In the case of ```Ssn```, the refactorings to extract the logic into a separate class were much simpler and we completed the task in a short time.
4. The unit checks for ```Ssn``` called for a separate test class because ```Ssn``` is not just a data container, it's a full-fledged class that carries out logic. 

When we extract the _name_ functionality from ```JobApplicant```, we'll encounter still another wrinkle.

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round4```.

## Next step for this smell

Next we'll [extract the logic pertaining to _name_](notes-srp-violation-3.md) from ```JobApplicant``` to reduce the number of responsibilities  in that class.
