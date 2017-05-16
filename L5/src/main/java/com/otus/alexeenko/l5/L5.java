package com.otus.alexeenko.l5;

import com.otus.alexeenko.l5.framework.Result;
import com.otus.alexeenko.l5.framework.TestFramework;
import com.otus.alexeenko.l5.test.L5Test;

public class L5 {

    public static void main(String[] args) {
        Result result;

        result = TestFramework.runTests(L5Test.class);
        result.view();

        result = TestFramework.runTests("com.otus.alexeenko.l5.test");
        result.view();
    }
}
