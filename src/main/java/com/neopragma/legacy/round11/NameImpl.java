package com.neopragma.legacy.round11;

/**
 * Represents the domain concept Name, the name of a person.
 * It supports English and Spanish name formats, but in a hardcoded way.
 */
//TODO: Restructure to use internationalization
public class NameImpl implements Name, Constants {
	
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
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
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
}
