# Smell: Integration checks

[Back to top](notes.md) | [Next: Separation of concerns - persistence](notes-persistence.md)

We now have fast-running and reliable _unit checks_ for the ```Address``` class thanks to mocking the connection to the Internet-based lookup service. But we introduced a new problem when we mocked the service: Now we don't have an automated check to validate that our application interacts properly with the service.

What we need is an _integration check_. The earlier version of ```AddressTest``` contained integration checks because it accesses the real Internet-based service. If we drop that same code into another test class, we'll have our integration checks. 

We don't want the integration checks to run every time we run our unit checks because they have longer run times and they might not work if we are not connected to the Internet, or the service is unavailable, or the response time is too long. Therefore, we need a way to run just the unit checks or just the integration checks.

By default, Maven expects us to want to run the integration checks if the unit checks all pass. But we may not wish to do this. When we are using a test-first development approach, we will typically run the unit checks far more frequently than we run integration checks. 

There are several ways to set up unit and integration checks to run separately with Maven, or with other build tools. That is out of scope for this exercise. For this project, we are using a naming convention to distinguish between unit and integration checks. Unit check source files are named ```*Test.java``` and integration check source files are named ```*IT.java```. 

Based on this naming convention, we can tell Maven to run either the unit checks or the integration checks, but not both together. This will run the unit checks only:

```shell
mvn -Dtest="com.neopragma.legacy.round6.*Test" test
```

This will run the integration checks only:

```shell
mvn -Dtest="com.neopragma.legacy.round3.*IT" test
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round6```.

## Next smell

The ```JobApplicant``` class contains code to save the job applicant's information. (Well, it contains an empty method that represents that code.) Persistence (reading and writing) is a different _concern_ than processing a job applicant. Per the _Single Responsibility Principle_, the ```JobApplicant``` class should not contain persistence logic directly. Let's [separate the persistence logic from the job applicant logic](notes-persistence.md).
