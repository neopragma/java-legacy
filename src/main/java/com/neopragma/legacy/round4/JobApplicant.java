package com.neopragma.legacy.round4;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This class represents the domain concept of a job applicant. 
 *
 * @author neopragma
 * @since 1.7
 */
public class JobApplicant {
	
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private CityStateLookup cityStateLookup;
	private Address address;
	private Ssn ssn;
	
	public JobApplicant(CityStateLookup cityStateLookup) {
		this.cityStateLookup = cityStateLookup;
	}

	public void setName(String firstName, String middleName, String lastName) {
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
	}
	
	public void setSpanishName(String primerNombre, String segundoNombre,
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
	
	public int validateName() {
		if ( firstName.length() > 0 && lastName.length() > 0 ) {
			return 0;
		} else {
			return 6;
		}
	}
	
	public void add(String firstName,
			       String middleName,
			       String lastName,
			       String ssn,
			       String zipCode) throws URISyntaxException, IOException {
		setName(firstName, middleName, lastName);
		setSsn(ssn);
		setAddress(zipCode);
		save();
	}
	
	void save() {
		//TODO save information to a database
		System.out.println("Saving to database: " + formatLastNameFirst());
	}

	public void setSsn(String ssn) {
		this.ssn = new Ssn(ssn);
	}
	
	public Ssn getSsn() {
		return ssn;
	}
	
	public void setAddress(String zipCode) throws URISyntaxException, IOException {
		CityState cityState = cityStateLookup.lookup(zipCode);
		address = new Address(zipCode, cityState.getCity(), cityState.getState());
		cityState = null;
	}

	public Address getAddress() {
		return address;
	}
		
}
