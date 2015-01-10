package com.neopragma.legacy.round3;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface CityStateLookup {
	
	CityState lookup(String zipCode) throws URISyntaxException, ClientProtocolException, IOException;

}
