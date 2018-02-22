package com.neopragma.legacy.round9;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class CityStateLookupImpl implements CityStateLookup {

    /**
     * This method calls the internet-based service
     * www.zip-codes.com/search to find the city and state
     * that correspond to the zip code passed in.
     *
     * @param zipCode
     * @return CityState object containing the city and state
     * @throws CityStateLookupException
     */
	@Override
	public CityState lookup(String zipCode) {
		try {
			URI uri = new URIBuilder()
            .setScheme("http")
            .setHost("www.zip-codes.com")
            .setPath("/search.asp")
            .setParameter("fld-zip", zipCode)
            .setParameter("selectTab", "0")
            .setParameter("srch-type", "city")
            .build();
            HttpGet request = new HttpGet(uri);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            CloseableHttpResponse response = httpclient.execute(request);
            String city = "";
            String state = "";
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
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
                }
            } finally {
                response.close();
            }	
            return new CityState(city, state);
	    } catch (Exception e) {
	    	throw new CityStateLookupException(e);
	    }
	}		
			
}
