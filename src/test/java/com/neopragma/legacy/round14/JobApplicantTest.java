package com.neopragma.legacy.round14;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Automated unit checks for the base version of the JobApplicant application.
 * This version of the code contains a number of code smells that may point to
 * potential improvements in the design of the code.
 * 
 * @author neopragma
 * @version 1.0.0
 * @since 1.7
 */

public class JobApplicantTest {
	
	private JobApplicant jobApplicant;
	
	@Test
	public void nullJobApplicantReturnsIdZero() {
		jobApplicant = new NullJobApplicant();
		assertEquals(0, (int) jobApplicant.getId());
	}

	@Test
	public void nullJobApplicantReturnsNullObjectForAddress() {
		jobApplicant = new NullJobApplicant();
		assertEquals(NullAddress.class, jobApplicant.getAddress().getClass());
	}

	@Test
	public void nullJobApplicantReturnsNullObjectForName() {
		jobApplicant = new NullJobApplicant();
		assertEquals(NullName.class, jobApplicant.getName().getClass());
	}

	@Test
	public void nullJobApplicantReturnsNullObjectForSsn() {
		jobApplicant = new NullJobApplicant();
		assertEquals(NullSsn.class, jobApplicant.getSsn().getClass());
	}

	@Test
	public void whenNoIdIsPassedOnCtor_itSetsIdToZero() {
		jobApplicant = new JobApplicantImpl(new NullAddress(), new NullSsn(), new NullName());
		assertEquals(0, (int) jobApplicant.getId());		
	}
	
	@Test
	public void whenAnIdIsPassedOnCtor_itUsesThatId() {
		jobApplicant = new JobApplicantImpl(new Integer(5), new NullAddress(), new NullSsn(), new NullName());
		assertEquals(5, (int) jobApplicant.getId());		
	}

}
