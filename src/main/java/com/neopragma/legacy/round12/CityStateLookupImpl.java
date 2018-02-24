package com.neopragma.legacy.round12;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    //TODO: Re-think this implementation
	@Override
	public CityState lookup(String zipCode) {
		try {
            URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("api.zippopotam.us")
                .setPath("/us/" + zipCode.substring(0,5))
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
                    JsonElement jelement = new JsonParser().parse(result.toString());
                    JsonObject jobject = jelement.getAsJsonObject();
                    JsonArray jarray = jobject.getAsJsonArray("places");
                    jobject = jarray.get(0).getAsJsonObject();
                    city = jobject.get("place name").getAsString();
                    state = jobject.get("state abbreviation").getAsString();
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
