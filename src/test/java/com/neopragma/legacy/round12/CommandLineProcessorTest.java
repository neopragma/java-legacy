package com.neopragma.legacy.round12;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;

import static com.neopragma.legacy.round12.ConsoleHelper.loadSystemIn;
import static com.neopragma.legacy.round12.ConsoleHelper.recordSystemOut;
import static org.junit.Assert.assertEquals;

public class CommandLineProcessorTest {

    private CommandLineProcessor processor;

    @Before
    public void beforeEach() {
        processor = new CommandLineProcessor();
    }

    @Test
    public void itPromptsForAndAcceptsUserInput() throws IOException {
        loadSystemIn("Ralph");
        processor.setScanner(new Scanner(System.in));
        assertEquals("Ralph", processor.promptFor("Prompt text"));
    }
}
