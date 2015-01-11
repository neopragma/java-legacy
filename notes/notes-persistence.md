# Smell: Integration checks

[Back to top](notes.md) | [Next: Law of Demeter - checked exceptions](notes-checked-exceptions.md)

The ```JobApplicant``` class contains a method, ```save```, whose function is to store the information about the job applicant in a database or file. One can imagine methods to retrieve and delete information, too. The method isn't implemented in the sample code, but it illustrates the problem of multiple concerns - a violation of the _Single Responsibility Principle_. 

We'll pull the persistence logic out of ```JobApplicant``` and hide it behind an interface. Advantages:

- Easy to substitute different persistence implementations
- More clarity about the responsibilities of different classes in the application
- Easier to isolate code for automated checks

We want the persistence mechanism to support three operations:

- save
- find by id
- find by name
- delete

A Jav interface might look like this:

```java
public interface Persistence {
	
	void save(JobApplicant data);
	JobApplicant find(Integer id);
	JobApplicant findByName(Name name);
	void delete(Integer id);
}
```

How should the interface behave when an operation fails? Several alternatives are possible:

- return a value
- throw an exception
- silently continue

Let's reason through those alternatives.

An instance of ```JobApplicant``` contains the information for a job applicant. The applicant may or may not already be present in the database, but in either case the instance is valid by definition. Therefore, there is nothing in the data itself that will cause an error in storing to the database or deleting from the database. That means a failure on ```save``` or ```delete``` is not a normal occurrence. When an outcome is not a normal occurrence, it's customary to throw an exception. It's likely that the application cannot continue when the database is "broken," so it's reasonable to throw.

The ```retrieve``` method may or may not find matching data in the database. When it finds data, it returns an instance of ```JobApplicant```. Should ```retrieve``` throw an exception for a _not found_ condition? This is not really an abnormal occurrence. Therefore, ```retrieve``` should not throw when the job applicant is not found. We might want it to throw when the database is corrupt, or in case of some other truly abnormal situation. 

What should ```retrieve``` return for a _not found_ condition? It may be tempting to return a _null reference_. The problem is this has a tendency to cause ```NullPointerException``` errors at run time. Instead, this may be a good time to use the _Null Object Pattern_. The ```retrieve``` method can return a _null instance_ of ```JobApplicant```. This is not a null reference; it is a valid instance whose methods do nothing. The caller still has to check to see whether anything was found in the database, but the risk of runtime errors is much lower because the methods in the null instance will cause no harm if they are called.

We'll create an interface for ```JobApplicant``` so that we can implement ```JobApplicantImpl``` and ```NullJobApplicant``` concrete classes. ```JobApplicantImpl``` will be the same as the old ```JobApplicant``` class. We'll do the same for ```Address```, ```Name```, and ```Ssn``` so that we can create null instances for those, as well.

The idea of silently continuing might make sense for the ```delete``` method, if the application doesn't need to know whether the delete operation succeeded. Most of the time, this is not a good choice.

Should we require client code to declare checked exceptions our persistence logic can throw? It would be more convenient if client code could dispense with that. We'll define a runtime exception of our own to wrap any checked exceptions that might be thrown.

Your reasoning may differ, but in the sample solution we:

- Created interfaces for ```Address```, ```Name```, ```Ssn```, and ```JobApplicant```
- Created default implementation classes ```AddressImpl```, ```NameImpl```, ```SsnImpl```, and ```JobApplicantImpl```
- Created null implementation classes ```NullAddress```, ```NullName```, ```NullSsn```, and ```NullJobApplicant```.
- Added checks to validate the behavior of the null instances to ```AddressTest```, ```NameTest```, ```SsnTest```, and ```JobApplicantTest```.
- Created an interface to contain constant definitions, ```Constants```.
- Separated persistence logic into a new class, ```Persistence```.
- Created runtime exception ```InvalidSsnException``` and changed ```SsnImpl``` to throw the exception instead of returning integer return codes.
- Changed ```Main``` so it uses the ```Persistence``` class to save the job applicant information.


## Sample solution

The sample solution is in package ```com.neopragma.legacy.round7```.

## Next smell

Some of our application code can throw checked exceptions. The current implementation requires callers to declare any checked exceptions that might be thrown in the code they are calling. This is a violation of the _Law of Demeter_ in that it requires callers to "know too much" about the underlying implementation of methods they call. Let's [wrap checked exceptions](notes-checked-exceptions.md).
