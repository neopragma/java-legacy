package com.neopragma.legacy.round14;

import java.text.MessageFormat;

/**
 * A person's name in Spanish form.
 */
public class SpanishName implements Name, Constants {

	private final static String displayFormatPattern = "{0} {1}{2} {3}";
	private final static String sortableFormatPattern = "{2} {3}{0} {1}";
	private String primerNombre = null;
	private String segundoNombre = null;
	private String primerApellido = null;
	private String segundoApellido = null;

	/**
	 * Constructor for Spanish names
	 * @param nameParts - primer nombre, segundo nombre,
	 *                  primer apellido, segundo apellido
	 */
	public SpanishName(String...nameParts) {
		if (nameParts.length != 4 ||
				isEmpty(nameParts[0]) ||
				isEmpty(nameParts[2])) {
			throw new IllegalArgumentException();
		}
		primerNombre = nameParts[0];
		segundoNombre = nameParts[1];
		primerApellido = nameParts[2];
		segundoApellido = nameParts[3];
	}

	@Override
	public String displayName() {
		return MessageFormat.format(displayFormatPattern,
				primerNombre,
				initial(segundoNombre),
				primerApellido,
				initial(segundoApellido)).trim();
	}

	@Override
	public String sortableName() {
		return MessageFormat.format(sortableFormatPattern,
				primerNombre,
				isEmpty(segundoNombre) ? EMPTY_STRING : segundoNombre + SPACE,
				primerApellido,
				isEmpty(segundoApellido) ? EMPTY_STRING : segundoApellido + SPACE).trim();
	}

	private String initial(String value) {
		if (isEmpty(value)) {
			return EMPTY_STRING;
		}
		return value.substring(0,1) + PERIOD_SPACE;
	}

	private boolean isEmpty(String value) {
		return (value == null || value == EMPTY_STRING);
	}
}
