package com.neopragma.legacy.round12;

import java.util.Scanner;

/**
 * Command line user interface for the job applicant application.
 */
public class CommandLineProcessor implements Constants{

	private static final String INITIAL_PROMPT = "Please enter info about a job candidate or 'quit' to quit";
	private static final String FIRST_NAME = "First name?";
	private static final String QUIT = "quit";
	private static final String SIGNOFF_MESSAGE = "Bye-bye!";
	private static final String MIDDLE_NAME = "Middle name?";
	private static final String LAST_NAME = "Last name?";
	private static final String SSN = "SSN?";
	private static final String ZIP_CODE = "Zip Code?";

	private Scanner scanner;
	private String firstName = EMPTY_STRING;
	private String middleName = EMPTY_STRING;
	private String lastName = EMPTY_STRING;
	private String ssn = EMPTY_STRING;
	private String zipCode = EMPTY_STRING;

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
            system.add(jobApplicant);
		}
	}

	Scanner getScanner() {
		if (scanner == null) {
			scanner = new Scanner(System.in);
		}
		return scanner;
	}

	void setScanner(Scanner scanner) {
		this.scanner = scanner;
	}

	String promptFor(String message) {
		System.out.println(message);
		return scanner.nextLine();
	}

}
