package com.otus.alexeenko.l5.framework;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by vsevolod on 15/05/2017.
 */
public class Info {
    private final String testName;
    private final boolean testResult;
    private final Exception exception;

    public Info(String testName, boolean testResult, Exception exception) {
        this.testName = testName;
        this.testResult = testResult;
        this.exception = exception;
    }

    public void view() {
        if (exception != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);

            exception.printStackTrace(writer);
            System.err.println("\nTest name: \"" + testName + "\" passed: " + testResult
                    + "\n" + stringWriter.toString());
        } else
            System.out.println("\nTest name: \"" + testName + "\" passed: " + testResult);
    }
}
