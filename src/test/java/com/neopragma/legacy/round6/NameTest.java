package com.neopragma.legacy.round6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NameTest {
	
	private Name name;
	
	@Test
	public void completeNameProvided() {
		name = new Name("First", "Middle", "Last");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void firstAndLastNamesProvided() {
		name = new Name("First", null, "Last");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void missingFirstName() {
		name = new Name(null, null, "Last");
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void missingLastName() {
		name = new Name("First", null, null);
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void completeSpanishNameProvided() {
		name = new Name("PrimerNombre", "SegundoNombre", "PrimerApellido", "SegundoApellido");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void spanishNameWithOneFirstNameProvided() {
		name = new Name("PrimerNombre", null, "PrimerApellido", "SegundoApellido");
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void spanishNameWithOneLastNameProvided() {
		name = new Name("PrimerNombre", null, "PrimerApellido", null);
		assertEquals(0, name.validateName());
	}
	
	@Test
	public void spanishNameWithNoFirstNameProvided() {
		name = new Name(null, null, "PrimerApellido", null);
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void spanishNameWithNoLastNameProvided() {
		name = new Name("PrimerNombre", "SegundoNombre", null, null);
		assertEquals(6, name.validateName());
	}
	
	@Test
	public void formatEnglishNameLastNameFirst() {
		name = new Name("First", "Middle", "Last");
		assertEquals("Last, First Middle", name.formatLastNameFirst());
	}

}
