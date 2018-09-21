package com.neopragma.legacy.round10;

import java.util.Scanner;

/**
 * Prompts the user to enter information about job applicants and
 * saves the information for each applicant. It runs in a loop to
 * accept job application data until the user types 'quit' into
 * the first input field.
 */
public class Main {

	public static void main(String[] args) {
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
