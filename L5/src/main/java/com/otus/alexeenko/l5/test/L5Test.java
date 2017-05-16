package com.otus.alexeenko.l5.test;

import com.otus.alexeenko.l5.framework.annotations.After;
import com.otus.alexeenko.l5.framework.annotations.Before;
import com.otus.alexeenko.l5.framework.annotations.Test;

import static com.otus.alexeenko.l5.framework.Assert.fail;

/**
 * Created by Vsevolod on 13/05/2017.
 */
public class L5Test {
    private static String str;

    public L5Test() {
        str = "BBC";
    }

    @Before
    public void beforeL5Test() {
        str = "Before";
        System.out.println(str);
    }

    @Test
    public void ATM() {
        str += "Test-1";
        fail();
        System.out.println(str);
    }

    @Test
    public void ATM1() {
        str += "Test-2";
        System.out.println(str);
    }

    @After
    public void afterL5Test() {
        str += "Afret";
        System.out.println(str);
    }
}
