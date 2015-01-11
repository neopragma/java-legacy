# Smell: Main method baked into entity class

[Back to top](notes/notes.md) | [Next: JobApplicant has multiple responsibilities](notes-srp-violation.md)

Notice that the main method to drive the application is included in the JobApplicant class. The purpose of the JobApplicant class is to represent the domain concept of a "job applicant." Its purpose is not to interact with the end user. This is a violation of _separation of concerns_ and the _Single Responsibility Principle_ (SRP).

The advantage of separating these concerns is that we can build any sort of interface to the application we wish without experiencing an integration nightmare. The existing command-line interface will still work the same way as it currently works.

Let's create a new class called Main and move the main method into it.

1. Ensure all the automated unit checks are passing.
2. Create a new class named Main.
3. Move the main method code and the variables it references from JobApplicant to Main.
4. Ensure all the automated unit checks are passing.

Something interesting happens when we complete Step 3. The save method in JobApplicant is not visible to the code in the Main class. 

Let's change the visibility to _package_ for the time being. We know this is an incremental refactoring step, and we will probably do something with the save method later. In the meantime, giving it _package_ visibility will keep the code in a working state.

## Sample solution

The sample solution is in package com.neopragma.legacy.round1.

## Next smell

The next smell is that the entity class ```JobApplicant``` has multiple responsibilities. That's a violation of the _Single Responsibility Principle_ Let's [fix it](notes-srp-violation.md).

