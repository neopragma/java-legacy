package com.neopragma.legacy.round13;

import java.text.MessageFormat;

/**
 * Represents the domain concept Name, the name of a person.
 * It supports English and Spanish name formats, but in a hardcoded way.
 */
//TODO: Restructure to use internationalization
public class NameImpl implements Name, Constants {

	private final static String displayFormatPattern = "{0} {1}{2}";
	private final static String sortableFormatPattern = "{2} {1}{0}";
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;

	/**
     * Constructor for English names
	 * @param firstName
     * @param middleName
     * @param lastName
	 */
	public NameImpl(String firstName, String middleName, String lastName) {
		if (isEmpty(firstName) || isEmpty(lastName)) {
			throw new IllegalArgumentException();
		}
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
	}

	/**
     * Constructor for Spanish names
	 * @param primerNombre
     * @param segundoNombre
     * @param primerApellido
     * @param segundoApellido
	 */
	public NameImpl(String primerNombre, String segundoNombre,
							   String primerApellido, String segundoApellido) {
		this.firstName = primerNombre == null ? "" : primerNombre;
		this.middleName = segundoNombre == null ? "" : segundoNombre;
		if ( primerApellido != null ) {
  		    StringBuilder sb = new StringBuilder(primerApellido);
		    sb.append(segundoApellido == null ? null : " " + segundoApellido);
		    this.lastName = sb.toString();
		} else {
			this.lastName = "";
		}
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

	/**
	 * @return name in the form "Adams, John Quincy"
	 */
	@Override
	public String formatLastNameFirst() {
		StringBuilder sb = new StringBuilder(lastName);
		sb.append(", ");
		sb.append(firstName);
		if ( middleName.length() > 0 ) {
			sb.append(" ");
			sb.append(middleName);
		}
		return sb.toString();
	}
	
	@Override
	public int validateName() {
		if (firstName.length() > 0 && lastName.length() > 0) {
			return OK;
		} else {
			return BAD_NAME;
		}
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
