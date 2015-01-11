package com.neopragma.legacy.round6;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws URISyntaxException, IOException {
		boolean done = false;
		Scanner scanner = new Scanner(System.in);
		String firstName = "";
		String middleName = "";
		String lastName = "";
		String ssn = "";
		String zipCode = "";
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
            JobApplicant jobApplicant = new JobApplicant(
            		new Address(new CityStateLookupImpl(), zipCode), 
            		new Ssn(ssn),
            		new Name(firstName, middleName, lastName));
            jobApplicant.save();
		}
	}

}
