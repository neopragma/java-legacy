package com.neopragma.legacy.round8;

public class SsnImpl implements Ssn {

	
	private String ssn;
	
	private String[] specialCases = new String[] {
	    "219099999", "078051120"
	};

	public SsnImpl(String ssn) {
		setSsn(ssn);
	}
	
	@Override
	public String formatSsn() {
		StringBuilder sb = new StringBuilder(ssn.substring(0,3));
		sb.append("-");
		sb.append(ssn.substring(3,5));
		sb.append("-");
		sb.append(ssn.substring(5));
		return sb.toString();
	}

	private void setSsn(String ssn) {
		if ( ssn.matches("(\\d{3}-\\d{2}-\\d{4}|\\d{9})") ) {
  		    this.ssn = ssn.replaceAll("-", "");
		} else {
  		    this.ssn = "";
		}    
		validateSsn();
	}

	private int validateSsn() {
		if ( !ssn.matches("\\d{9}") ) {
			throw new InvalidSsnException("Ssn must be exactly nine digits");
		}
		if ( "000".equals(ssn.substring(0,3)) || 
			 "666".equals(ssn.substring(0,3)) ||
			 "9".equals(ssn.substring(0,1)) ) {
			throw new InvalidSsnException("Ssn cannot start with 000, 666, or 9");
		}
		if ( "0000".equals(ssn.substring(5)) ) {
			throw new InvalidSsnException("Ssn serial number cannot be 0000");
		}
		for (int i = 0 ; i < specialCases.length ; i++ ) {
			if ( ssn.equals(specialCases[i]) ) {
				throw new InvalidSsnException(specialCases[i] + " is a reserved Ssn that cannot be used");
			}
		}
		return 0;
	}


}
