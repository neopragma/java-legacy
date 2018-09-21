package com.neopragma.legacy.round13;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NameTest {
	
	private Name name;
	private Name testName = new NameImpl("First", "Middle", "Last");

	@Test
	public void itReturnsEnglishNameWithMiddleInitialFormattedForDisplay() {
		assertEquals("First M. Last", testName.displayName());
	}

	@Test
	public void itReturnsEnglishNameWithNoMiddleNameFormattedForDisplay() {
        name = new NameImpl("First", null, "Last");
		assertEquals("First Last", name.displayName());
	}

	@Test
	public void itReturnsSortableEnglishNameWithMiddleName() {
		assertEquals("Last Middle First", testName.sortableName());
	}

	@Test
	public void itReturnsSortableEnglishNameWithNoMiddleName() {
		name = new NameImpl("First", null, "Last");
		assertEquals("Last First", name.sortableName());
	}

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

	@Test(expected=IllegalArgumentException.class)
	public void missingFirstName() {
		name = new NameImpl(null, "Middle", "Last");
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingLastName() {
		name = new NameImpl("First", "Middle", null);
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
