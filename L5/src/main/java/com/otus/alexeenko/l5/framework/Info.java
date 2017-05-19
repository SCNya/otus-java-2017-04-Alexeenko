package com.otus.alexeenko.l5.framework;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Vsevolod on 15/05/2017.
 */
class Info {
    private final String testName;
    private final boolean testResult;
    private final Throwable exception;

    Info(String testName, boolean testResult, Throwable exception) {
        this.testName = testName;
        this.testResult = testResult;
        this.exception = exception;
    }

    Info(String testName, boolean testResult) {
        this(testName, testResult, null);
    }

    void view() {
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
