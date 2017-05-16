package com.otus.alexeenko.l5;

import com.otus.alexeenko.l5.framework.Result;
import com.otus.alexeenko.l5.framework.TestFramework;

public class L5 {

    public static void main(String[] args) {
        Result result;

        /*result = TestFramework.runTests(L5Test.class);
        result.view();*/ //work too

        result = TestFramework.runTests("com.otus.alexeenko.l5.test");
        result.view();
    }
}
