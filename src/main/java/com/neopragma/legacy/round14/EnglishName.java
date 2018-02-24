package com.neopragma.legacy.round14;

import java.text.MessageFormat;

/**
 * A person's name in English form.
 */
public class EnglishName implements Name, Constants {

	private final static String displayFormatPattern = "{0} {1}{2}";
	private final static String sortableFormatPattern = "{2} {1}{0}";
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;

	/**
     * Constructor for English names
	 * @param nameParts - first, middle, last
	 */
	public EnglishName(String...nameParts) {
		if (nameParts.length != 3 ||
				isEmpty(nameParts[0]) ||
				isEmpty(nameParts[2])) {
			throw new IllegalArgumentException();
		}
		firstName = nameParts[0];
		middleName = nameParts[1];
		lastName = nameParts[2];
	}

	@Override
	public String displayName() {
		return MessageFormat.format(displayFormatPattern,
				firstName,
				initial(middleName),
				lastName);
	}

	@Override
	public String sortableName() {
		return MessageFormat.format(sortableFormatPattern,
				firstName,
				isEmpty(middleName) ? EMPTY_STRING : middleName + SPACE,
				lastName);
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
