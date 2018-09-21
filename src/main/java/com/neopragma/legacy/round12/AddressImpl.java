package com.neopragma.legacy.round12;

public class AddressImpl implements Address {
	
	private String zipCode;
	private String city;
	private String state;

	public AddressImpl(CityStateLookup cityStateLookup, String zipCode) {
    	CityState cityState = cityStateLookup.lookup(zipCode);
    	city = cityState.getCity();
    	state = cityState.getState();
		cityState = null;
		this.zipCode = zipCode;
	}

	public AddressImpl(String city, String state, String zipCode) {
		this.city = city;
		this.state = state;
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
