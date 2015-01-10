package com.neopragma.legacy.round1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * This class represents the domain concept of a job applicant. 
 *
 * @author neopragma
 * @since 1.7
 */
public class JobApplicant {
	
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	
	public void setName(String firstName, String middleName, String lastName) {
		this.firstName = firstName == null ? "" : firstName;
		this.middleName = middleName == null ? "" : middleName;
		this.lastName = lastName == null ? "" : lastName;
	}
	
	public void setSpanishName(String primerNombre, String segundoNombre,
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
	
	private String ssn;
	
	private String[] specialCases = new String[] {
	    "219099999", "078051120"
	};
	
	private String zipCode;    
	private String city;
	private String state;

	public void setSsn(String ssn) {
		if ( ssn.matches("(\\d{3}-\\d{2}-\\d{4}|\\d{9})") ) {
  		    this.ssn = ssn.replaceAll("-", "");
		} else {
  		    this.ssn = "";
		}    
	}
	
	public String formatSsn() {
		StringBuilder sb = new StringBuilder(ssn.substring(0,3));
		sb.append("-");
		sb.append(ssn.substring(3,5));
		sb.append("-");
		sb.append(ssn.substring(5));
		return sb.toString();
	}

	public int validateSsn() {
		if ( !ssn.matches("\\d{9}") ) {
			return 1;
		}
		if ( "000".equals(ssn.substring(0,3)) || 
			 "666".equals(ssn.substring(0,3)) ||
			 "9".equals(ssn.substring(0,1)) ) {
			return 2;
		}
		if ( "0000".equals(ssn.substring(5)) ) {
			return 3;
		}
		for (int i = 0 ; i < specialCases.length ; i++ ) {
			if ( ssn.equals(specialCases[i]) ) {
				return 4;
			}
		}
		return 0;
	}

	public void setZipCode(String zipCode) throws URISyntaxException, IOException {
		this.zipCode = zipCode;
		// Use a service to look up the city and state based on zip code.
		// Save the returned city and state if content length is greater than zero.
		URI uri = new URIBuilder()
            .setScheme("http")
            .setHost("www.zip-codes.com")
            .setPath("/search.asp")
            .setParameter("fld-zip", this.zipCode)
            .setParameter("selectTab", "0")
            .setParameter("srch-type", "city")
            .build();
        HttpGet request = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
              	BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
           		StringBuffer result = new StringBuffer();
           		String line = "";
           		while ((line = rd.readLine()) != null) {
           			result.append(line);
       		    }
                int metaOffset = result.indexOf("<meta ");
                int contentOffset = result.indexOf(" content=\"Zip Code ", metaOffset);
                contentOffset += 19;
                contentOffset = result.indexOf(" - ", contentOffset);
                contentOffset += 3;
                int stateOffset = result.indexOf(" ", contentOffset);
                city = result.substring(contentOffset, stateOffset);
                stateOffset += 1;
                state = result.substring(stateOffset, stateOffset+2);
            } else {
            	city = "";
            	state = "";
            }
        } finally {
            response.close();
        }
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}
	
	public void add(String firstName,
			       String middleName,
			       String lastName,
			       String ssn,
			       String zipCode) throws URISyntaxException, IOException {
		setName(firstName, middleName, lastName);
		setSsn(ssn);
		setZipCode(zipCode);
		save();
	}
	
	void save() {
		//TODO save information to a database
		System.out.println("Saving to database: " + formatLastNameFirst());
	}
		
}
