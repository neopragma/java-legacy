# Smell: Unit checks dependent on network resource

[Back to top](notes/notes.md) | [Next: JobApplicant has multiple responsibilities](notes-srp-violation.md)

Every time the automated unit checks run, they reach out to the Internet to perform a city and state lookup by zipcode. Our intent is not to test the remote service, but to verify that our own code handles the return values from the service correctly. We _do_ need to verify that all the pieces work together, but we _don't_ need to do that over and over again at the unit level.

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




## Sample solution

## Next smell

The next smell is that class JobApplicant has multiple responsibilities. Let's [put things where they rightfully belong](notes-srp-violation.md) to tighten cohesion and loosen coupling.
