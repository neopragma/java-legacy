package com.neopragma.legacy.round14;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class SpanishNameTest {

	private Locale locale = new Locale("es");
	private Name name;
	private Name testName = NameFactory.newInstance(locale, "PrimerNombre", "SegundoNombre", "PrimerApellido", "SegundoApellido");

	@Test
	public void itReturnsSpanishNameWithSegundoNombreFormattedForDisplay() {
		assertEquals("PrimerNombre S. PrimerApellido S.", testName.displayName());
	}

	@Test
	public void itReturnsSpanishNameWithNoSegundoNombreFormattedForDisplay() {
        name = NameFactory.newInstance(locale, "PrimerNombre", null, "PrimerApellido", "SegundoApellido");
		assertEquals("PrimerNombre PrimerApellido S.", name.displayName());
	}

	@Test
	public void itReturnsSortableSpanishNameWithMiddleName() {
		assertEquals("PrimerApellido SegundoApellido PrimerNombre SegundoNombre", testName.sortableName());
	}

	@Test
	public void itReturnsSortableSpanishNameWithNoMiddleName() {
		name = NameFactory.newInstance(locale, "PrimerNombre", null, "PrimerApellido", "SegundoApellido");
		assertEquals("PrimerApellido SegundoApellido PrimerNombre", name.sortableName());
	}

	@Test(expected=RuntimeException.class)
	public void missingFirstName() {
		name = NameFactory.newInstance(locale, null, "SegundoNombre", "PrimerApellido", "SegundoApellido");
	}

	@Test(expected=RuntimeException.class)
	public void missingLastName() {
		name = NameFactory.newInstance(locale, "PrimerNombre", "SegundoNombre", null, "SegundoApellido");
	}

}
