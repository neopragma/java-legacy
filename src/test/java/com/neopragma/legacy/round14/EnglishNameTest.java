package com.neopragma.legacy.round14;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import java.util.Locale;

public class EnglishNameTest {

	private Locale locale = new Locale("en_US");
	private Name name;
	private Name testName = NameFactory.newInstance(locale, "First", "Middle", "Last");

	@Test
	public void itReturnsEnglishNameWithMiddleInitialFormattedForDisplay() {
		assertEquals("First M. Last", testName.displayName());
	}

	@Test
	public void itReturnsEnglishNameWithNoMiddleNameFormattedForDisplay() {
        name = NameFactory.newInstance(locale, "First", null, "Last");
		assertEquals("First Last", name.displayName());
	}

	@Test
	public void itReturnsSortableEnglishNameWithMiddleName() {
		assertEquals("Last Middle First", testName.sortableName());
	}

	@Test
	public void itReturnsSortableEnglishNameWithNoMiddleName() {
		name = NameFactory.newInstance(locale, "First", null, "Last");
		assertEquals("Last First", name.sortableName());
	}

	@Test
	public void nullNameReturnsEmptyStringForName() {
		name = new NullName();
		assertEquals(Constants.EMPTY_STRING, name.displayName());
	}

	@Test(expected=RuntimeException.class)
	public void missingFirstName() {
		name = NameFactory.newInstance(locale, null, "Middle", "Last");
	}

	@Test(expected=RuntimeException.class)
	public void missingLastName() {
		name = NameFactory.newInstance(locale, "First", "Middle", null);
	}

}
