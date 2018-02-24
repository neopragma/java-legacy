package com.neopragma.legacy.round13;

public class JobApplicantSystem {

    private Persistence persistence;

    public JobApplicantSystem() {
        persistence = new PersistenceImpl();
    }

    public void add(JobApplicant jobApplicant) {
        persistence.save(jobApplicant);
    }

    public Persistence persistence() {
        if (persistence == null) {
            persistence = new PersistenceImpl();
        }
        return persistence;
    }

    public void setPersistence(Persistence persistence) {
        this.persistence = persistence;
    }
}
