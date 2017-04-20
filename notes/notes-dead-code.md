# Smell: Dead code

[Back to top](notes.md) | [Next: Integration tests](notes-isolation-2.md)

After deleting the misleading comments, method ```lookup``` in class ```CityStateLookupImpl``` contains this code:

```java
	public CityState lookup(String zipCode) throws URISyntaxException, ClientProtocolException, IOException {
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

Notice the line that reads

```java
        long len = entity.getContentLength();        
```

The variable ```len``` is never referenced. This is the kind of problem we see when people change code and neglect to deal with all the references to deleted or modified code. The solution is to delete the line.

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round6```.

## Next smell

When we broke the dependency on the Internet-based city and state lookup service, we made the unit check faster and more reliable, but now we don't have an automated check that exercises the connection to the service. We'd like to have a separate set of _integration checks_ that handle that level of validation, and we'd like to be able to run them separately from our unit checks. Let's [set that up](notes-isolation-2.md).
