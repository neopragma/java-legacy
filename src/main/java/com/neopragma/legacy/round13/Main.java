package com.neopragma.legacy.round13;

/**
 * Starts a command line processor and sets the status code on exit.
 */
public class Main implements Constants {

	private static CommandLineProcessor processor;
	private static int status = OK;

	public static void main(String[] args) {
		processor = new CommandLineProcessor();
		try {
			processor.run();
		} catch (Exception e) {
			e.printStackTrace();
			status = ERROR;
		}
		System.exit(status);
    }

}
