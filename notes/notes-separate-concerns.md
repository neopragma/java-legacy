# Smell: Multiple concerns in Main class

[Back to top](notes.md) | [Next: Repetitious code](notes-dry-1.md).

One of the [first things we did](https://github.com/neopragma/java-legacy/blob/master/notes/notes-main.md) was to extract the main processing loop out of the original monolithic JobApplicant class. With that code out of the way, we've focused on remediating other parts of the code. Now it's time to address code smells in the Main class. 

Currently, the _main()_ method handles multiple concerns:

- starting and stopping the application 
- user interaction via the command line 
- calling _save()_ to persist data

What's a reasonable way to separate these concerns?

Ideally, the _main()_ method should be in a class that's only needed to initiate the application by some mechanism that depends on a _main()_ method, like standalone execution or an executable _war_. That class should have no responsibilities beyond those necessary to start and gracefully stop the application. 

That's low-hanging fruit. Let's pick it. We'll keep the "main" logic in the Main class and pull the rest out into a separate class to manage command-line user interaction. 

Once removed from the clutter, it becomes obvious that _main()_ hasn't been shutting down the application gracefully. We'll add code to do that, and define a constant in Constants for non-zero status codes. The Main class now looks like this:

```java
package com.neopragma.legacy.round12;
/**
 * Starts a command line processor and sets the status code on exit.
 */
public class Main implements Constants {
    private CommandLineProcessor processor;
    private static int status = OK;
    public static void main(String[] args) {
        processor = new CommandLineProcessor();
        try {
            processor.run();
        } catch (Exception e) {
            e.printStackTrace(); // could do logging here
            status = ERROR;
        }
        System.exit(status);
    }
}

```

...and results in a new class with the logic we pulled out of Main:

```java
package com.neopragma.legacy.round12;
import java.util.Scanner;
/**
 * Command line user interface for the job applicant application.
 */
public class CommandLineProcessor {
    /**
     * Prompts the user to enter information about job applicants and
     * saves the information for each applicant. It runs in a loop to
     * accept job application data until the user types 'quit' into
     * the first input field.
     */
    public void run() {
        boolean done = false;
        Scanner scanner = new Scanner(System.in);
        String firstName = "";
        String middleName = "";
        String lastName = "";
        String ssn = "";
        String zipCode = "";
        Persistence db = new PersistenceImpl();
        while (!done) {
            System.out.println("Please enter info about a job candidate or 'quit' to quit");
            System.out.println("First name?");
            firstName = scanner.nextLine();		
            if (firstName.equals("quit")) {
                scanner.close();
          	    System.out.println("Bye-bye!");
        	    done = true;
           	    break;
            }
            System.out.println("Middle name?");
            middleName = scanner.nextLine();
            System.out.println("Last name?");
            lastName = scanner.nextLine();			
            System.out.println("SSN?");
            ssn = scanner.nextLine();			
            System.out.println("Zip Code?");
            zipCode = scanner.nextLine();	
            JobApplicantImpl jobApplicant = new JobApplicantImpl(
                new AddressImpl(new CityStateLookupImpl(), zipCode),
                new SsnImpl(ssn),
                new NameImpl(firstName, middleName, lastName));
            db.save(jobApplicant);
        }
    }
}
```

The _run()_ method in the CommandLineProcessor class contains everything that was in the old _main()_ method in the Main class. Taking baby steps, we haven't modified any of that logic yet. 

Managing the interaction between the user and the system is one concern, and ingesting the data for a new job applicant is a different concern. It would be good to separate the two, but the way the code stands now I don't feel confident about that. I'd prefer to clean up the code as it is. After that, it might be more obvious how to approach separating those concerns.

This part of the application was baked into the _main()_ method, and there were no executable unit checks for any of it. Our first step will be to get some unit checks around the code, to characterize its behavior. 

Unsurprisingly, this sort of testing is called _characterization testing_. It's often a first (or early) step in the process of remediating legacy code. 

Looking at the _run()_ method in CommandLineProcessor, we find there's a bit of a challenge: We need to interrupt the loop in order to check what's happening on the console at each step. 

If we call _run()_ as-is, it will keep going until someone or something passes "quit" as the input for "first name." We need to do a little refactoring without the benefit of a safety net to enable testing each prompt and response separately. 

Reviewing the code, we see there's a recurring pattern of displaying a prompt with ```System.out.println()``` and then reading the user's input with ```Scanner```. That sort of repetition lends itself nicely to the _extract method_ refactoring, which also happens to be relatively safe to do without unit checks in place to act as a safety net. So, let's extract that pattern. While we're at it, we'll use _extract constant_ for those annoying string literals. Finally, we implement Constants so we can make use of the handy _EMPTY_STRING_ declaration to initialize variables.
 
 With those changes, the _run()_ method looks like this:
 
 
```java
	public void run() {
        boolean done = false;
        scanner = new Scanner(System.in);
        Persistence db = new PersistenceImpl();
        while (!done) {
            System.out.println(INITIAL_PROMPT);
            firstName = promptFor(FIRST_NAME);
            if (firstName.equals(QUIT)) {
            	scanner.close();
                System.out.println(SIGNOFF_MESSAGE);
                done = true;
                break;
            }
            middleName = promptFor(MIDDLE_NAME);
            lastName = promptFor(LAST_NAME);
            ssn = promptFor(SSN);
            zipCode = promptFor(ZIP_CODE);
            JobApplicantImpl jobApplicant = new JobApplicantImpl(
            		new AddressImpl(new CityStateLookupImpl(), zipCode),
                    new SsnImpl(ssn),
                    new NameImpl(firstName, middleName, lastName));
            db.save(jobApplicant);
        }
```

This is a little more self-describing and a little shorter than the original version, so it might make additional improvements easier to see. 

Now we have a single method that handles the prompts and user input values that we can check independently. To support that, we'll make the Scanner member of CommandLineProcessor injectable and add some helper methods to plug stream readers and writers into System.in and System.out. 

I created a class named ConsoleHelper with a method to override System.in:

```java
    public static void loadSystemIn(String data) {
        try {
            InputStream testInput = new ByteArrayInputStream( data.getBytes("UTF-8") );
            System.setIn( testInput );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

...and made Scanner injectable (see CommandLineProcessor):

```java
    . . .
    scanner = getScanner();
    . . .
    Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }
    void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
```

...and now we have a unit check for console user interaction (see CommandLineProcessorTest):

```java
    @Test
    public void itPromptsForAndAcceptsUserInput() throws IOException {
        loadSystemIn("Ralph");
        processor.setScanner(new Scanner(System.in));
        assertEquals("Ralph", processor.promptFor("Prompt text"));
    }
```

Good so far, but the CommandLineProcessor still knows too much about the internals of the application. Specifically, it knows too much about persistence:

```java
    Persistence db = new PersistenceImpl();
    . . .
    JobApplicantImpl jobApplicant = new JobApplicantImpl(
        new AddressImpl(new CityStateLookupImpl(), zipCode),
        new SsnImpl(ssn),
        new NameImpl(firstName, middleName, lastName));
    db.save(jobApplicant);
    . . .
```

Let's introduce a layer of abstraction between the command line processing responsibility and the persistence responsibility. We'll create a class, JobApplicantSystem, that will provide the glue between JobApplicant and Persistence while exposing an API that "any" client can use. The first (and for now, the only) client will be CommandLineProcessor. 

This test case drove the modifications (see JobApplicantSystemTest):

```java
    @Test
    public void itSavesNewJobApplicant() {
        JobApplicantSystem system = new JobApplicantSystem();
        system.setPersistence(persistence);
        Address address = new AddressImpl("Our Town", "NY", "10203");
        Ssn ssn = new SsnImpl("123456789");
        Name name = new NameImpl("Adams", "John", "Quincy");
        JobApplicant jobApplicant = new JobApplicantImpl(address, ssn, name);
        system.add(jobApplicant);
        verify(persistence).save(jobApplicant);
    }
```

This results in only a small change to CommandLineProcessor. The improvement is that CommandLineProcessor no longer knows anything about persistence. It still has to know what a JobApplicant is, and some of the other entity types as well, but it's a little less tightly coupled to the rest of the system. We also adjust the class-level javadoc comments to reflect the modified functionality.

```java
    /**
     * Prompts the user to enter information about job applicants and
     * adds each applicant to the system. It runs in a loop to
     * accept job application data until the user types 'quit' into
     * the first input field.
     */
    public void run() {
        boolean done = false;
        scanner = getScanner();
        JobApplicantSystem system = new JobApplicantSystem();
    . . .
        system.add(jobApplicant);
    }
    . . .    
```

Finally, the new JobApplicantSystem class provides a layer of abstraction between CommandLineProcessor and Persistence:

```java
    public void add(JobApplicant jobApplicant) {
        persistence.save(jobApplicant);
    }
```

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round12```.

## Next smell

There is near-duplicate code to handle English and Spanish applicant names. [Next: Repetitious code](notes-dry-1.md).

