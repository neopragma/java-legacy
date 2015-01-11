package com.neopragma.legacy.round7;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NameTest {
	
	private Name name;
	
	@Test
	public void nullNameReturnsEmptyStringForName() {
		name = new NullName();
		assertEquals(Constants.EMPTY_STRING, name.formatLastNameFirst());
	}
	
	@Test
	public void nullNameReturnsGoodReturnCodeForValidateName() {
		name = new NullName();
		assertEquals(Constants.OK, name.validateName());
	}
	
	@Test
	public void completeNameProvided() {
		name = new NameImpl("First", "Middle", "Last");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void firstAndLastNamesProvided() {
		name = new NameImpl("First", null, "Last");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void missingFirstName() {
		name = new NameImpl(null, null, "Last");
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void missingLastName() {
		name = new NameImpl("First", null, null);
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void completeSpanishNameProvided() {
		name = new NameImpl("PrimerNombre", "SegundoNombre", "PrimerApellido", "SegundoApellido");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void spanishNameWithOneFirstNameProvided() {
		name = new NameImpl("PrimerNombre", null, "PrimerApellido", "SegundoApellido");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void spanishNameWithOneLastNameProvided() {
		name = new NameImpl("PrimerNombre", null, "PrimerApellido", null);
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void spanishNameWithNoFirstNameProvided() {
		name = new NameImpl(null, null, "PrimerApellido", null);
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void spanishNameWithNoLastNameProvided() {
		name = new NameImpl("PrimerNombre", "SegundoNombre", null, null);
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void formatEnglishNameLastNameFirst() {
		name = new NameImpl("First", "Middle", "Last");
		assertEquals("Last, First Middle", name.formatLastNameFirst());
	}

}
