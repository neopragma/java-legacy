package com.neopragma.legacy.round14;

/**
 * Represents the domain concept of a person's name.
 * @since 1.8
 */
public interface Name {
	/**
	 * @return Name in the standard display format for the Locale.
	 */
	String displayName();

	/**
	 * @return Name in a form suitable for sorting.
	 */
    String sortableName();
}