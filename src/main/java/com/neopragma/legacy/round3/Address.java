package com.neopragma.legacy.round3;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public class Address {
	
	private String zipCode;
	private String city;
	private String state;

	public Address(CityStateLookup cityStateLookup, String zipCode) throws ClientProtocolException, URISyntaxException, IOException {
    	CityState cityState = cityStateLookup.lookup(zipCode);
    	city = cityState.getCity();
    	state = cityState.getState();
		cityState = null;
		this.zipCode = zipCode;
	}

	public String getZipCode() {
		return zipCode;
	}
	
	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

}
