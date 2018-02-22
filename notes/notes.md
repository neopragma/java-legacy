# Legacy Code

These notes accompany a sample Java project that illustrates a number of code smells. The initial version of the code is in package com.neopragma.legacy.round0.

Smell 1. Worthless class-level comment. (See [notes](notes-comment.md))

Smell 2. Violation of Separation of Concerns: main method is baked directly into the entity class. Starting and stopping the application and handling input are different concerns than the _single responsibility_ of the JobApplicant class. (See [notes](notes-main.md))

Smell 3: Violation of Separation of Concerns: Class JobApplicant contains code to access a web-based resource directly in method setZipCode. Network communication is not part of the _single responsibility_ of the JobApplicant</span> class. (See [notes](notes-external.md))

Smell 4: Violation of Single Responsibility Principle (SRP): Class JobApplicant apparently represents the domain concept "Job Applicant," but contains functionality to deal with names, addresses, and Social Security Numbers. (See [notes](notes-srp-violation.md))

Smell 5: Error codes from SSN validation routines are all hard-coded literals. This is refactored in the course of remediating Smell 4.

Smell 6: Poor code isolation: Checks for zipcode functionality are dependent on a web-based resource. (See [notes](notes-isolation-1.md))

Smell 7: Comments that do not describe the functionality accurately. The code does not check the content length, as the comments state. (See [notes](notes-bad-comments.md)) 

Smell 8: Useless code in method setZipCode to retrieve the content length. The variable len is never referenced. (See [notes](notes-dead-code.md))

Smell 9: Integration tests posing as unit tests: Checks for zipcode functionality are dependent on a web-based resource. For that reason, these checks are not really "unit tests." Separate unit and integration checks and enable Maven to run them separately on demand. (See [notes](notes-isolation-2.md))

Smell 10: Violation of Separation of Concerns: Class JobApplicant contains code to handle persistence (the save method). Persistence is not part of the _single responsibility_ of the JobApplicant class. (See [notes](notes-persistence.md))

Smell 11: Violation of Law of Demeter: Checked exceptions must be handled by client code. Client code has to know too much about the internal implementation details of the JobApplicant class. (See [notes](notes-checked-exceptions.md))

Smell 12: No API documentation (javadoc comments). (See [notes](notes-api-documentation.md))

Smell 13: Code contains magic numbers. (See [notes](notes-magic-numbers.md))

Smell 14: Missing case: There is no check for the case when the supplied zipcode is not found in the lookup. (See [notes](notes-missing-case.md))

Smell 15: Crufty, undocumented, and non-self-describing code in method <span class="code">setZipCode</span> to interact with Apache HttpClient. (See [notes](notes-ugly-code-1.md))

Smell 16: Class members not organized in any particular way. (See [notes](notes-organization.md))

Smell 17: Poor separation of concerns: Class contains functionality to handle input validation as well as data formatting for output. (See [notes](notes-validation.md))

Smell 18: Repetitious code in methods setName and setSpanishName to avoid storing null values. (See [notes](notes-dry-1.md))

Smell 19: Clumsy way to jam support for Spanish names into the application (method setSpanishName) instead of using Java's support for Object Oriented design and internationalization. (See [notes](notes-internationalization.md))
