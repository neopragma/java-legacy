# Smell: No API documentation

[Back to top](notes.md) | [Next: Magic numbers](notes-magic-numbers.md)

Methods intended to be called by client code should have API documentation so that programmers will be able to see what the methods do and how to use them. At the same time, we want to keep in mind the rule of thumb to avoid useless comments. Let's see how we might go about it.

My personal bias is to avoid comments. With that in mind, I would approach this task by looking at each class, interface, and enum to see how self-explanatory it is. It's likely that many of them will not require any javadoc comments to clarify what they do and how to use them. 

For example, consider this snippet:

```java
    /** 
     * This method returns the name
     * @return name
     */
    public String getName() {
        return this.name;
    }
```

In my opinion, the javadoc comments in this example add nothing useful. They only amount to clutter in the source file. We should use javadoc when it helps to disambiguate or clarify the meaning or usage of a piece of code. When developers use generally-accepted good practices, most of their code will be expressive of intent, and will not require a separate explanation.

Examining the code in the ```round8``` package, we see code that is clear enough without javadoc comments, like these examples:

```java
public interface Address {
	public abstract String getCity();
	public abstract String getState();
	public abstract String getZipCode();

```

```java
public class CityState {
	private String city;
	private String state;
	
	public CityState(String city, String state) {
		this.city = city;
		this.state = state;
	}
	
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
}
```

```java

public interface JobApplicant {

	Integer getId();
	Address getAddress();
    Name getName();
	Ssn getSsn();
}
```

```java
public interface CityStateLookup {	
	CityState lookup(String zipCode);
}

```

A question to ask yourself is how hard it would be for you to figure out how to use the class or method in question without any more explanation than the naming convention suggests. 

Consider the implementation of the ```CityStateLookup``` interface in class ```CityStateLookupImpl```. It's a long class, so we won't repeat it in this document. This is a crufty implementation that we isolated from the rest of the code by moving into this class, in an earlier refactoring step. Crufty implementations are rarely self-explanatory. The ```lookup()``` method warrants javadoc comments to inform programmers that the method calls an external service. It could look something like this:

```java
public class CityStateLookupImpl implements CityStateLookup {
    /** 
     * This method calls the internet-based service 
     * www.zip-codes.com/search to find the city and state
     * that correspond to the zip code passed in. 
     * 
     * @param zipCode 
     * @return CityState object containing the city and state
     * @throws CityStateLookupException
     */
	@Override
	public CityState lookup(String zipCode) {
		try {
			URI uri = new URIBuilder()
            .setScheme("http")
    // a lot of code here
            return new CityState(city, state);
	    } catch (Exception e) {
	    	throw new CityStateLookupException(e);
	    }
	}		
}    
```

Another class that could benefit from javadoc comments is ```Main```. It contains the bulk of the command-line interface processing for the application. A summary of that functionality would be useful for programmers who must work on the code. 

```java
public class Main {
    /**
     * Prompts the user to enter information about job applicants and 
     * saves the information for each applicant. It runs in a loop to
     * accept job application data until the user types 'quit' into 
     * the first input field.
     */
	public static void main(String[] args) {
		boolean done = false;
		Scanner scanner = new Scanner(System.in);
		String firstName = "";
    // a lot of code here 
            db.save(jobApplicant);
		}
	}
}    
```

The class ```NameImpl``` is another crufty implementation that we isolated by moving it into a separate class. It contains logic to deal with English and Spanish names. Ultimately we would want to restructure this using Java's internationalization support. For now, we can at least add some javadoc comments to clarify what's going on. 

Notice that I added a _TODO_ comment as a reminder to restructure the code. Normally I avoid _TODO_ comments, but in this case I think it serves the useful purpose of reminding me (or whoever else might pick up this class) that the code needs attention.

```java
/**
 * Represents the domain concept Name, the name of a person. 
 * It supports English and Spanish name formats, but in a hardcoded way.
 */
//TODO: Restructure to use internationalization
public class NameImpl implements Name {
	
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;

	/**
	 * Constructor for English names
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 */
	public NameImpl(String firstName, String middleName, String lastName) {
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
	}

	/**
	 * Constructor for Spanish names
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 */
	public NameImpl(String primerNombre, String segundoNombre,
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

	/**
	 * @return name in the form "Adams, John Quincy"
	 */
	@Override
	public String formatLastNameFirst() {
		StringBuilder sb = new StringBuilder(lastName);
		sb.append(", ");
		sb.append(firstName);
		if ( middleName.length() > 0 ) {
			sb.append(" ");
			sb.append(middleName);
		}
		return sb.toString();
	}	
	@Override
	public int validateName() {
		if ( firstName.length() > 0 && lastName.length() > 0 ) {
			return 0;
		} else {
			return 6;
		}
	}
}
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round9```.

## Next smell

In the last example above, you probably noticed _magic numbers_ in method, _validateName()_. Let's clean that up next. 

Next smell: [Magic numbers](notes-magic-numbers.md).
