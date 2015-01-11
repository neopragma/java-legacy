package com.neopragma.legacy.round7;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public class AddressImpl implements Address {
	
	private String zipCode;
	private String city;
	private String state;

	public AddressImpl(CityStateLookup cityStateLookup, String zipCode) throws ClientProtocolException, URISyntaxException, IOException {
    	CityState cityState = cityStateLookup.lookup(zipCode);
    	city = cityState.getCity();
    	state = cityState.getState();
		cityState = null;
		this.zipCode = zipCode;
	}

	@Override
	public String getZipCode() {
		return zipCode;
	}
	
	@Override
	public String getCity() {
		return city;
	}

	@Override
	public String getState() {
		return state;
	}

}
