package com.neopragma.legacy.round6;

/**
 * This class represents the domain concept of a job applicant. 
 *
 * @author neopragma
 * @since 1.7
 */
public class JobApplicant {
	private Address address;
	private Ssn ssn;
	private Name name;
	
	public JobApplicant(Address address, Ssn ssn, Name name) {
		this.address = address;
		this.ssn = ssn;
		this.name = name;
	}
	
	public void add(Name name,
			       Ssn ssn,
			       Address address) {
		this.name = name;
		this.ssn = ssn;
		this.address = address;
		save();
	}
	
	void save() {
		//TODO save information to a database
		System.out.println("Saving to database: " + name.formatLastNameFirst());
	}

	public void setSsn(String ssn) {
		this.ssn = new Ssn(ssn);
	}
	
	public Ssn getSsn() {
		return ssn;
	}

	public Address getAddress() {
		return address;
	}
		
}
