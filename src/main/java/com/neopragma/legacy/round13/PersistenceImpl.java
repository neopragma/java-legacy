package com.neopragma.legacy.round13;

public class PersistenceImpl implements Persistence {

	@Override
	public void save(JobApplicant data) {
		System.out.println("(fake) Saving job applicant " + data.getName().formatLastNameFirst());
	}

	@Override
	public void delete(Integer id) {
		System.out.println("(fake) Deleting job applicant " + id);
	}

	@Override
	public JobApplicant findById(Integer id) {
		System.out.println("(fake) Finding job applicant by id " + id);
		return null;
	}

	@Override
	public JobApplicant findByName(String name) {
		System.out.println("(fake) Finding job applicant by name " + name);
		return null;
	}

}
