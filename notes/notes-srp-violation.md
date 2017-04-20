# Smell: JobApplicant class has multiple responsibilities (part 1)

[Back to top](notes.md) | [Next: Extracting the Ssn class](notes-srp-violation-2.md)

The ```JobApplicant``` class ostensibly represents the domain concept of "job applicant," but it contains code to manage the concepts of "address," "name," and "social security number," as well. 

The colloquial definition of _Single Responsibility Principle_ is that a class should have exactly one reason to change. If we look at the ```JobApplicant``` class in that light, it's clear that a change in the required logic to support "job applicant," "address," "name," or "social security number" would create the need to change the ```JobApplicant``` class. That's _four_ reasons to change, not _one_.

Let's tease the logic apart and give each responsibility its very own home.

We'll consider the domain concept _address_ first. This represents the street address of a job applicant. Examining ```JobApplicant``` we find it doesn't have fields for all the usual components of a street address. Let's assume the reason is that the application doesn't yet support everything that will ultimately be necessary for street addresses. Our immediate task is _not_ to guess what the future requirements for street address might be, and write code for it. Our immediate task is the clean up the code that _already exists_, and that's all. Resist temptation.

The code that already exists supports city, state abbreviation, and zipcode for US addresses. The fields are not encapsulated together. There is an instance variable of type ```CityState``` that contains the city name and state abbreviation, and a separate instance variable of type String that contains the zipcode.

It would be a good idea to encapsulate all the information regarding the job applicant's street address into a separate class.

While we aren't supposed to make guesses about future requirements, we _are_ expected to consider reasonable future-proofing of the application. In this case, given what we know about US street addresses, it's likely that more functionality will be added to the application around this domain concept. This is another reason to encapsulate street address logic in its own class. That way, it will be easier to modify the logic in future. The way the code stands now, with bits and pieces of address information scattered throughout the ```JobApplicant``` class, we are practically _begging_ for late nights, weekends, and bugs.

If we take a test-first approach, the first thing we will consider is whether our unit checks are organized in a way that makes sense for the breakdown of functionality in the application. As we are about to extract _address_ functionality from ```JobApplicant```, we probably don't want the unit checks for street address processing to remain in the ```JobApplicantTest``` class. It makes more sense to house them in a separate class. 

We create a new class in the ```src/test/java``` tree named ```AddressTest``` and move the relevant unit checks into it from ```JobApplicantTest```.

```java
public class AddressTest {
	
	private JobApplicant jobApplicant;
	@Before

	public void beforeEach() {
		jobApplicant = new JobApplicant(new CityStateLookupImpl());
	}	
	
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
		jobApplicant.setZipCode("75001");
		assertEquals("Addison", jobApplicant.getCity());
		assertEquals("TX", jobApplicant.getState());
	}
	
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() throws URISyntaxException, IOException {
		jobApplicant.setZipCode("856585578");
		assertEquals("Marana", jobApplicant.getCity());
		assertEquals("AZ", jobApplicant.getState());
	}
}
```

Next, we run all the automated unit checks for package ```com.neopragma.legacy.round3``` to make sure we didn't break anything in the process of moving that code.

So far, so good.

The test-first approach is helping us with something else. The test class is _client code_ that calls the public API of the code under test. What a test case sees is the same thing that production client code would see. And in this case, what it sees is not what we really want.

The test cases in ```AddressTest``` aren't exactly checking _address_ functionality; they're checking _city and state lookup by zipcode_. By separating these test cases from the mass of others in ```JobApplicantTest```, it's much easier to see what they're doing, and much easier to see how to improve them. 

Once we have the String zipCode value and the populated ```CityState``` value object, we can use them to instantiate another object that handles street addresses. Why don't we call it ```Address```?

As we are using a test-first approach this time, let's begin by modifying the test cases to expect the behavior we _want_ the application to exhibit.

1. The method name ```setZipCode``` doesn't quite express what we want the method to do. We want the method to return an ```Address``` object containing all the values (that have been specified) that pertain to the job applicant's street address. We can begin by adding a new method on ```JobApplicant``` named ```setAddress```. (It might seem as it we could just change the name of ```setZipCode``` to ```setAddress```, but in the interest of safety we will leave that method alone until it become clear what we should do with it. If we were taking the traditional approach we would sketch out a fairly comprehensive design first, but with the test-first approach we want the automated checks to help guide the emerging design.)
2. Now, lets set the expectation that the ```setAddress``` method will store the correct values of zipcode, city name, and state abbreviation. At this point the compiler complains that there's no such class as ```Address```. We use Eclipe's _create class_ feature to fix that. That results in an empty class named ```Address```.
3. Now the compiler complains that there's no getter on ```JobApplicant``` to return the address. We use Eclipse to create the method, which returns null by default. We add code by hand in ```JobApplicant``` to store an instance variable of type ```Address``` and to return it from the ```getAddress``` method. 
4. The compiler complains again because there's no code in the ```Address``` class to do what we want it to do. Let's hand-code the variables and let Eclipse generate the getters for us. At the moment, zipcode is a String and the city and state are buried in a value object of type ```CityState```. We can easily think ahead and realize the ```Address``` class doesn't need the value object, but we want to exercise discipline and take just one small step at a time. So we'll have ```getZipCode``` and ```getCityState``` for now.
5. Our test case wants to call ```Address.getCity``` and ```Address.getState```. Client code doesn't care about the ```CityState``` object. This is an example of the _Law of Demeter_, which states that a caller should not have to know anything about the internal implementation of objects with which it interacts. Client code just wants "city" and "state." So, we'll add getters that wrap the accesses to the ```CityState``` object. This is an interim step only. Don't panic.
6. Now everything compiles. We've only modified one test case in ```AddressTest```, but the second one doesn't fail to compile because we left the original ```getZipCode``` method in place for safety. When we have the first test case working, we can use the venerable code reuse technique known as _copy and paste_ to update the second test case. (Not very orthodox? Too bad.)
7. But the test case fails. We haven't changed the production code in ```JobApplicant``` yet. We add code in ```setAddress``` to call ```setZipCode``` and to instantiate an ```Address``` object using existing getters. Clearly, this is not the end game, but we want to continue to exercise discipline in taking small steps. 
8. After making those changes, the test case passes. We use _copy and paste_ to make the second test case look like the first, and we see they both pass. We have pulled the _address_ functionality out of ```JobApplicant```, but the code still isn't very clean.

```JobApplicant``` now has this code:

```java
    . . .
	private CityState cityState;
    . . .

	public void setZipCode(String zipCode) throws URISyntaxException, IOException {
		cityState = cityStateLookup.lookup(zipCode);
	}

	public String getCity() {
		return cityState.getCity();
	}

	public String getState() {
		return cityState.getState();
	}
    . . .
	public void setAddress(String zipCode) throws URISyntaxException, IOException {
		setZipCode(zipCode);
		address = new Address(zipCode, getCity(), getState());
	}

	public Address getAddress() {
		return address;
	}
```            

Examining this code, we see that the only reason we need the ```CityState``` value object is to hold the return value from the city and state lookup method, because that method needs to return two values. One we have the values, we don't need the object anymore. We can take it out of the picture at the point when we no longer need it. 

Let's change the ```Address``` class so that it has _city_ and _state_ instance variables, and doesn't keep the ```CityState``` object.

In ```JobApplicant``` we can move the logic from ```setZipCode``` into ```setAddress``` and get rid of the temporary ```CityState``` value object. We can also delete the ```setZipCode```, ```getCity```, and ```getState``` methods. ```JobApplicant``` is getting thinner!

Now we have this:

```java
	public void setAddress(String zipCode) throws URISyntaxException, IOException {
		CityState cityState = cityStateLookup.lookup(zipCode);
		address = new Address(zipCode, cityState.getCity(), cityState.getState());
		cityState = null;
	}
```

We've been tweaking the code a fair bit, so this might be a good time to run all the unit checks just to be on the safe side. 

It doesn't seem as if ```JobApplicant``` should be responsible for instantiating an ```Address``` object. Let's move that into the constructor of the ```Address``` class. 

After moving the code, our unit checks should still pass. 

Probably the most basic code smell is _duplication_. We also know we ought to keep our test code just as clean as our production code, or else it will become hard to understand and people will stop using it. There's duplication in the ```AddressTest``` class:

```java
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
		jobApplicant.setAddress("75001");
		assertEquals("Addison", jobApplicant.getAddress().getCity());
		assertEquals("TX", jobApplicant.getAddress().getState());
		assertEquals("75001", jobApplicant.getAddress().getZipCode());
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() throws URISyntaxException, IOException {
		jobApplicant.setAddress("856585578");
		assertEquals("Marana", jobApplicant.getAddress().getCity());
		assertEquals("AZ", jobApplicant.getAddress().getState());
		assertEquals("856585578", jobApplicant.getAddress().getZipCode());
	}
```

These two methods are very much alike. In addition, they have three asserts each. When an assert fails, the remaining ones will not be executed. We might miss something interesting.

We could move most of the code into a single helper method that both test cases call. If we were using a matcher library such as Hamcrest, we could write a custom matcher to validate the ```Address``` object. Either way, we can simplify this code.

We needn't get into writing custom matchers for purposes of this workshop. Lets' just extract the logic into a helper method, like this:

```java
public class AddressTest {
	
	private JobApplicant jobApplicant;
	@Before

	public void beforeEach() {
		jobApplicant = new JobApplicant(new CityStateLookupImpl());
	}	
	
	@Test
	public void itFindsAddisonTexasBy5DigitZipCode() throws URISyntaxException, IOException {
		assertAddressFor("75001", "Addison", "TX");
	}
		
	@Test
	public void itFindsMaranaArizonaBy9DigitZipCode() throws URISyntaxException, IOException {
		assertAddressFor("856585578", "Marana", "AZ");
	}
	
	private void assertAddressFor(String zipCode, String city, String state) {
		try {
			jobApplicant.setAddress(zipCode);
			assertEquals(city, jobApplicant.getAddress().getCity());
			assertEquals(state, jobApplicant.getAddress().getState());
			assertEquals(zipCode, jobApplicant.getAddress().getZipCode());
		} catch(AssertionError nope) {
			fail("Expected: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state +
					" Actual: zipCode <" + zipCode + ">, city <" + city + ">, state <" + state);
		} catch(Throwable unexpected) {
			fail("Unexpected: " + unexpected);
		}
	}
}
```

You can probably see several more improvements that could be made, but we'll save them for later. We still have two more responsibilities to extract from ```JobApplicant```.

You might wonder why we're checking methods on ```JobApplicant``` when we're really looking at functionality in ```Address```. The reason is the ```Address``` class is a value object, and its instances are immutable. There's nothing interesting to validate. The interesting part is to validate that ```JobApplicant``` still functions properly after we've substituted the new ```Address``` class for the original implementation.

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round3```.

## Next step for this smell

Next we'll [extract the logic pertaining to _social security number_](notes-srp-violation-2.md) from ```JobApplicant``` to reduce the number of responsibilities  in that class.
