# Smell: JobApplicant class interacts with a network resource

[Back to top](notes.md) | [Next: JobApplicant has multiple responsibilities](notes-srp-violation.md)

The setZipCode method in the JobApplicant class contains hard-coded logic to access an Internet-based service to look up information based on a US zipcode. This creates a number of issues:

- The application code is tightly coupled to the Internet resource.
- Automated checks will be unreliable, as cases may fail when the Internet is not available or when response time is too long
- Automated checks will have long runtimes because of delay waiting for responses from the Internet resource. 
- If many automated checks run quickly, the Internet connection may become overloaded. This can cause timeouts during the test run leading to _false negatives_ in the test results. This tends to cause developers to ignore failing checks - they assume failures are due to network timeouts. 

The corrective action is to move the code that interacts with the network resource out of the JobApplicant class and define an interface for performing zipcode lookups. The existing lookup code will become the first concrete implementation of that interface.

With the code isolated, the automated checks can specify a _test double_ to represent the network resource. This will enable unit-level automated checks to run fast and to be reliable. At the integration or functional level, automated checks can access the actual network resource to ensure the application interacts properly with it.

Steps (test-first approach):

1. Ensure all the automated unit checks are passing.
2. Modify unit checks to reflect the behavior you intend to change in the application. You are going to inject an instance of ```CityStateLookup``` on the constructor of ```JobApplicant.``` You can use your IDE's features to create missing classes and methods to help you create the objects to which you are adding references in the test class. This is a good way to avoid mistakes in typing.
3. Run the automated checks and see that the only failures are due to the changes you made in Step 2.
4. Create a new interface named ```CityStateLookup``` that declares the method lookup and accepts a String argument ```zipcode```.
5. Create a class named ```CityStateLookupImpl``` that implements interface ```CityStateLookup```.
6. Add a constructor argument to class ```JobApplicant``` to accept a reference to a ```CityStateLookup``` object.
7. Move the city and state lookup code from ```JobApplicant.setZipCode``` to ```CityStateLookupImpl.lookup
8. Ensure all the automated unit checks are passing.

Steps (traditional approach):

1. Ensure all the automated unit checks are passing.
2. Create a new interface named ```CityStateLookup``` that declares the method lookup and accepts a String argument ```zipcode```.
3. Create a class named ```CityStateLookupImpl``` that implements interface ```CityStateLookup```.
4. Add a constructor argument to class ```JobApplicant``` to accept a reference to a ```CityStateLookup``` object.
5. Move the city and state lookup code from ```JobApplicant.setZipCode``` to ```CityStateLookupImpl.lookup```.
6. Adjust the unit checks so that they pass an implementation of ```CityStateLookup``` to the ```JobApplicant``` constructor.
7. Ensure all the automated unit checks are passing.

When creating the interface, you can either

1. Create the interface first and then create the concrete class to implement it, or
2. Create the concrete class first and then use your IDE's refactoring feature to do an _extract interface_ refactoring.

Notice that regardless of how you approach the code changes, you _begin and end_ with all automated unit checks passing. This is a _refactoring_ - a change to the internal design of the code that _does not_ change the behavior of the application. 

When refactoring, all automated must should pass before and after. This is your "safety net" for ensuring you haven't inadvertently changed the _behavior_ of the application when you only intended to change the _internal design_ of the code.

## Sample solution

The sample solution is in package com.neopragma.legacy.round2.

Here's what I did for the sample solution. I used the test-first approach to modify the code.

1. Added a private member to ```JobApplicantTest``` declaring an instance variable of type ```CityStateLookup```. That will be the name of the interface. References to objects of that type will be at the lowest level of abstraction that makes sense. In this case, we want to refer to the interface and not the specific concrete implementation classes.
2. Added code in the ```@Before``` method to pass a reference to a ```CityStateLookup``` object to the constructor of ```JobApplicant.```
3. Used Eclipse to _create interface CityStateClass_ in ```src/main/java```. At this point the interface specified no methods.
4. Used Eclipse to _create class CityStateClassImpl_ in ```src/main/java```. At this point the class was empty.
5. Used Eclipse to _create JobApplicant constructor_ with the ```CityStateLookup``` argument. Eclipse generated a reference to ```CityStateLookupImpl```, so I manually changed it to ```CityStateLookup```. Also added code to the constructor method to store the passed-in reference in an instance variable.
6. Made the same change in ```Main``` as in ```JobApplicantTest``` to pass an instance of ```CityStateLookup``` to the constructor of ```JobApplicant```.
7. At this point both ``JobApplicant``` and ```JobApplicantTest``` compiled, so I ran the automated checks. All passed.
8. Because of the change to ```Main```, which is not covered by the automated unit checks, I manually checked it by choosing _Run as... Java application_. It still worked as before.
9. The city and state lookup logic returns a city name and a state abbreviation. So that the new ```lookup``` method can return a single value, I needed a _value object_ to encapsulate these two values. I created a class named ```CityState``` and defined two fields named ```city``` and ```state```. Then I used Eclipse to generate _getter_ methods for these two fields, so that I would not accidently make a mistake typing in the code. The creation of the value class is almost entirely automated by the IDE. That means it is _generated code_. A rule of thumb is that we don't test-drive generated code. We test-drive code that we type with our own fallible fingers.
10. I want the value object to be immutable, so I defined a constructor that takes String arguments for city and state.
11. I added a declaration for ```lookup(String zipCode)``` to the ```CityStateLookup``` interface.
11. I moved the code in ```JobApplicant.setZipCode``` that performs the lookup into the new ```CityStateLookupImpl.lookup``` method and replaced it with a call to that method. 
12. At that point, ```JobApplicant.zipCode``` was no longer referenced, so I deleted it.
13. In ```CityStateLookupImpl.lookup```, changed reference from ```this.zipCode``` to ```zipCode``` (the argument passed in).
14. In ```CityStateLookupImpl.lookup```, tweaked the code to place the result values in the ```CityState``` value object and return that object.
15. Used Eclipse to add throws declarations where needed. This can be further improved later.
16. Changed getters for city and state in ```JobApplicant``` to pass through to the returned ```CityState``` value object.
17. Ran all automated checks.
18. Smoke tested the application manually.

If that sounds like a lot of work, it is...and it isn't. When we are dealing with legacy code, there will be many times when we have to take many small steps to accomplish a relatively minor clean-up. But as long as we work in small steps and check frequently as we go along to be sure we haven't overlooked something or broken something, then it really isn't that difficult. The idea that it's "impossible" to clean up legacy code is wrong, but on the other hand it isn't always quick and easy.

## Next smell


