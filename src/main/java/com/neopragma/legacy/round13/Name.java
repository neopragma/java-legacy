package com.neopragma.legacy.round13;

public interface Name {

	/**
	 * @return Name in the standard display format for the Locale.
	 */
	String displayName();

	/**
	 * @return Name in a form suitable for sorting.
	 */
    String sortableName();

	/**
	 * @return Name in a form suitable for sorting.
	 * @deprecated use {@link #sortableName()} instead.
	 */
	@Deprecated	String formatLastNameFirst();

	/**
	 * @return 0 if valid, 6 if not valid.
	 * @deprecated no replacement method; constructor will validate
	 */
	@Deprecated int validateName();

}