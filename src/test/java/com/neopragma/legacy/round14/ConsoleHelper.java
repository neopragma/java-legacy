package com.neopragma.legacy.round14;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class ConsoleHelper {

    private static PrintStream originalSystemOut;
    private static PrintStream originalSystemErr;
    private static ByteArrayOutputStream baos;
    private static InputStream originalSystemIn;

    public static void recordSystemOut() {
        baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        originalSystemOut = System.out;
        System.setOut(ps);
    }

    public static String playbackSystemOut() {
        System.setOut(originalSystemOut);
        return new String(baos.toByteArray());
    }

    public static void recordSystemErr() {
        baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        originalSystemErr = System.err;
        System.setErr(ps);
    }

    public static void loadSystemIn(String data) {
        try {
            InputStream testInput = new ByteArrayInputStream( data.getBytes("UTF-8") );
            System.setIn( testInput );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String playbackSystemErr() {
        System.setErr(originalSystemErr);
        return new String(baos.toByteArray());
    }

    public static void resetSystemOut() {
        System.setOut(originalSystemOut);
    }

    public static void resetSystemErr() {
        System.setErr(originalSystemErr);
    }

    public static void resetSystemIn() {
        System.setIn(originalSystemIn);
    }

}