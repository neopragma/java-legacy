package com.neopragma.legacy.round6;

public class Name {
	
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;

	public Name(String firstName, String middleName, String lastName) {
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
	}
	
	public Name(String primerNombre, String segundoNombre,
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
	
	public int validateName() {
		if ( firstName.length() > 0 && lastName.length() > 0 ) {
			return 0;
		} else {
			return 6;
		}
	}


}
