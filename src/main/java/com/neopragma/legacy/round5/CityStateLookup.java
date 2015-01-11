package com.neopragma.legacy.round5;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface CityStateLookup {
	
	CityState lookup(String zipCode) throws URISyntaxException, ClientProtocolException, IOException;

}
