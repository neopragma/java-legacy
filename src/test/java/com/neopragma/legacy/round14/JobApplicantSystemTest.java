package com.neopragma.legacy.round14;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JobApplicantSystemTest {

    @Mock
    Persistence persistence;

    @Test
    public void itSavesNewJobApplicant() {
        JobApplicantSystem system = new JobApplicantSystem();
        system.setPersistence(persistence);
        Address address = new AddressImpl("Our Town", "NY", "10203");
        Ssn ssn = new SsnImpl("123456789");
        Name name = new EnglishName("Adams", "John", "Quincy");
        JobApplicant jobApplicant = new JobApplicantImpl(address, ssn, name);
        system.add(jobApplicant);
        verify(persistence).save(jobApplicant);
    }
}
