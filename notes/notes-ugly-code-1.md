# Smell: Crufty code in city/state lookup

[Back to top](notes.md) | [Next: Class members not organized](notes-organization.md)

The code we just looked at, in method ```lookup()``` in class ```CityStateLookupImpl```, is problematic. It's fragile, in that any change to the site at www.zip-codes.com will break it. It's clunky, in that it looks for substrings and increments an offset to try and locate the response data of interest. It's hard to understand, in that it's a series of hard-coded increments to bump through the response data. 

But we're in a better position to remediate this code now than we were at the start of this exercise. We've segregated the problematic logic and covered it with unit and integration checks. We can modify it without ripping up any other code, and our checks will tell us if we change the behavior of the code. Now, at last, it makes sense to remediate this chunk of code. 

The core problem is that it's using a web page designed for human interaction as if it were a web service with a programmatic interface. Can we find a proper service that provides the same functionality?

A quick Internet search turns up several candidates. Let's say our team checks a few of these out and decides to use [Zippopotamus](http://www.zippopotam.us).

Zippopotamus exposes a handful of RESTful URIs that support zip code lookups and city/state lookups. It doesn't take long to learn that this call will meet our needs:

```
GET http://api.zippopotam.us/us/85658
```

The service returns a JSON result like this:

```json
{
  "post code": "85658", 
  "country": "United States", 
  "country abbreviation": "US", 
  "places": [
    {
      "place name": "Marana", 
      "longitude": "-111.1459", 
      "state": "Arizona", 
      "state abbreviation": "AZ", 
      "latitude": "32.4305"
    }
  ]
}
```

Let's replace the crufty implementation with a cleaner one that issues a RESTful request to Zippopotamus and parses the JSON response. We can run our checks frequently to make sure we aren't going off-track. 

Note that it would have been risky to attempt this at the start of the refactoring exercise, as this code was embedded with other code and the unit checks were not complete or reliable. It's often a good idea to approach remediation in small steps and make sure each step is working properly before proceeding to the next. It's a common mistake for people to change too many things in one go. 

The current implementation builds the URI like this:

```java
URI uri = new URIBuilder()
    .setScheme("http")
    .setHost("www.zip-codes.com")
    .setPath("/search.asp")
    .setParameter("fld-zip", zipCode)
    .setParameter("selectTab", "0")
    .setParameter("srch-type", "city")
    .build();
```

The overall structure of the method won't have to change. The details of how the request is built and how the response is parsed will be different.

We'll take baby steps. Let's begin by adjusting the URI creation code to build the URI for Zippopotamus. 

Zippopotamus doesn't support 9-digit zip codes. That's not a problem for our use case, as we only need to find the city and state associated with a zip code. We don't need to zero in any closer than that. For now, we'll take the chance that fewer than 5 digits are passed in. That's something to cover later. Let's get the basic functionality in place first. 

```java
URI uri = new URIBuilder()
    .setScheme("http")
    .setHost("api.zippopotam.us")
    .setPath("/us/" + zipCode.substring(0,5))
    .build();
```

We need to handle JSON response data. Let's add a dependency to the Maven pom for a JSON library.

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.2</version>
</dependency>
```

Now we can replace the code within the _try_ block of the _lookup()_ method with something more standard, simpler, shorter, and easier to follow:

```java
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
. . .    
```

This isn't the first time we've tweaked this code. The first thing we did was to [pull it out of the monolithic JobApplicant class](https://github.com/neopragma/java-legacy/blob/master/notes/notes-external.md) that we started with. Later, we [isolated the code](https://github.com/neopragma/java-legacy/blob/master/notes/notes-isolation-1.md) for unit test purposes, to streamline our build and make the tests more reliable, and [tweaked our build script](https://github.com/neopragma/java-legacy/blob/master/notes/notes-isolation-2.md) to enable running unit and integration tests separately. In another step, we [removed misleading comments](https://github.com/neopragma/java-legacy/blob/master/notes/notes-bad-comments.md) that made it harder to understand what the code was doing. We [added javadoc comments](https://github.com/neopragma/java-legacy/blob/master/notes/notes-api-documentation.md) and a _TODO_ comment to clarify the code and remind us to remediate it. Later, we [added a test case](https://github.com/neopragma/java-legacy/blob/master/notes/notes-missing-case.md) for a condition that wasn't covered, and in the process discovered a bug in the tests. Finally, in this step, we replaced the fragile implementation with a better one. 

Why not just clean the whole thing up at the beginning? Remember the idea that we clean up little by little as we continue our normal work. Those initial steps could be completed without causing a halt in the work flow, and ultimately they set us up to make the final remediation of the _lookup()_ method a relatively trivial task that only took a couple of minutes. Imagine working with a real-world code base that has a couple of million lines of code and hundreds of examples of questionable design. Would it be reasonable to try and fix everything at once?

## Sample solution

The sample solution is in package ```com.neopragma.legacy.round11```.

## Next smell

The next smell is that class members are not organized in any particular way, making it harder for people to see what the application is doing. Let's see [Class members not organized](notes-organization.md).

