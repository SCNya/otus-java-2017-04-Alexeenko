package com.otus.alexeenko.l5.test;

import com.otus.alexeenko.l5.atm.ATM;
import com.otus.alexeenko.l5.framework.AssertionError;
import com.otus.alexeenko.l5.framework.annotations.After;
import com.otus.alexeenko.l5.framework.annotations.Before;
import com.otus.alexeenko.l5.framework.annotations.Test;

import static com.otus.alexeenko.l5.framework.Assert.*;

/**
 * Created by Vsevolod on 13/05/2017.
 */
public class L5Test {
    private static ATM atm;

    @Before
    public void beforeL5Test() {
        atm = new ATM(10, 10, 10);
    }

    @Test
    public void getBalance() {
        assertTrue(atm.getBalance() == 80);
    }

    @Test
    public void cashIn() {
        atm.cashIn(50, 5,20);
        assertTrue(atm.getBalance() == 240);
    }

    @Test(expected = AssertionError.class)
    public void cashOut() {
        atm.cashOut(55);
        fail();
    }

    @After
    public void afterL5Test() {
        assertNotNull(atm);
    }
}
