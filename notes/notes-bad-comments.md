# Smell: Bad comments

[Back to top](notes.md) | [Next: Dead code](notes-dead-code.md)

Method ```lookup``` in class ```CityStateLookupImpl``` contains this code:

```java
	public CityState lookup(String zipCode) throws URISyntaxException, ClientProtocolException, IOException {
		// Use a service to look up the city and state based on zip code.
		// Save the returned city and state if content length is greater than zero.
    . . .
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
    . . .
```

Note the comment that reads, "Save the returned city and state if content length is greater than zero." Later, the code reads from the returned entity without checking the content length. This is an example of the kind of problem we see when people depend on source comments to describe the functionality of the code. Often, when people change the code they neglect to change the comments. The solution is to delete the comments.

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round6```.

## Next smell

The next smell is dead code in method ```lookup``` in class ```CityStateLookupImpl```. Let's [remove it](notes-dead-code.md).
