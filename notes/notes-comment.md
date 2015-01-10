# Smell: Worthless comment

[Back to top](notes/notes.md) | [Next: Main method](notes-main.md)

A rule of thumb for using source comments effectively is that they should be minimal and useful. The class-level javadoc comments in class JobApplicant are certainly minimal. But are they useful? This is what we have:

```java
/**
 * Job applicant class.
 */
public class JobApplicant {
```

The comment just repeats the class declaration. How can we improve this? There isn't much to say about the class. We could make the wording a bit more descriptive, but we don't want to get carried away. We might change the comment to read:

```java
/**
 * This class represents the domain concept of a job applicant. 
 */
public class JobApplicant {
```

That's not so great, but it's a little more descriptive than the original comment. Let's accept it as a first step toward improving the code. As we continue to clean up the code, the purpose of the class may become more clear, and we can improve the comment accordingly.

We're not quite finished improving the class-level javadoc comments.

According to the [Oracle style guide for javadoc comments](http://www.oracle.com/technetwork/java/javase/documentation/index-137868.html#styleguide), tags in class-level comments should be coded in the following order:

- @author
- @version
- @see
- @since
- @deprecated

The appropriate values for @author and @version will depend on the standards established in your organization or team. For this example, we will say the @author is "neopragma". 

Normally, the @version tag would match your source code version control system. Some older version control systems would look for special strings inside source files and substitute version and date values. For example, SCCS would look for the string %I% and substitute the current version number, and the string %G% and substitute the date. With modern version control systems, it isn't necessarily useful to repeat that information inside source files. Instead, it's more common to refer to different versions of a file by commit number or commit identifier. This does not require any modification of source files by the version control system. We will not use the @version tag in our example.

Use @see if it would help people to refer to the documentation for a related class or package.

@since denotes the earlist release of Java for which the code is known to work. It's generally helpful to include this.

Use @deprecated if you don't want people to use the class because you intend to phase it out of the application.

## Sample solution

Our sample solution ends up like this:

```java
/**
 * This class encapsulates the domain concept of a Job Applicant.
 *
 * @author neopragma
 * @since 1.7
 */
```

## Next smell

The next smell is that the main method is baked into the entity class. Let's see [what we can do about that](notes-main.md).

