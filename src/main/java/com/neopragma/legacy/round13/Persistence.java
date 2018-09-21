package com.neopragma.legacy.round13;

public interface Persistence {
	
	void save(JobApplicant data);
	JobApplicant findById(Integer id);
	JobApplicant findByName(String name);
	void delete(Integer id);
}
