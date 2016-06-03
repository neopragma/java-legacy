package com.neopragma.legacy.round8;

/**
 * This class represents the domain concept of a job applicant. 
 *
 * @author neopragma
 * @since 1.7
 */
public class JobApplicantImpl implements JobApplicant {
	private Integer id;
	private Address address;
	private Ssn ssn;
	private Name name;
	
	public JobApplicantImpl(Address address, Ssn ssn, Name name) {
		this(0, address, ssn, name);
	}
	
	public JobApplicantImpl(Integer id, Address address, Ssn ssn, Name name) {
		this.id = id;
		this.address = address;
		this.ssn = ssn;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	public Name getName() {
		return name;
	}
	
	@Override
	public Ssn getSsn() {
		return ssn;
	}
		
}
