package com.otus.alexeenko.l5.test;

import com.otus.alexeenko.l5.atm.ATM;
import com.otus.alexeenko.l5.atm.pack.PackVol1;
import com.otus.alexeenko.l5.atm.pack.PackVol2;
import com.otus.alexeenko.l5.atm.pack.PackVol5;
import com.otus.alexeenko.l5.framework.AssertionError;
import com.otus.alexeenko.l5.framework.annotations.After;
import com.otus.alexeenko.l5.framework.annotations.Before;
import com.otus.alexeenko.l5.framework.annotations.Test;

import static com.otus.alexeenko.l5.framework.Assert.*;

/**
 * Created by Vsevolod on 13/05/2017.
 */
public class L5Test {
    private ATM atm;

    @Before
    public void beforeL5Test() {
        atm = new ATM(new PackVol1(10), new PackVol2(10),
                new PackVol5(10));
    }

    @Test
    public void getBalance() {
        assertTrue(atm.getBalance() == 80);
    }

    @Test
    public void cashIn() {
        atm.cashIn(new PackVol1(50), new PackVol2(5),
                new PackVol5(20));

        assertTrue(atm.getBalance() == 240);
    }

    @Test(expected = AssertionError.class)
    public void cashOut() {
        atm.cashOut(57);

        fail();
    }

    @Test
    public void create() {
        atm = new ATM(new PackVol1(55), new PackVol2(55),
                new PackVol5(100));

        assertNotNull(atm);
    }

    @Test
    public void cashIn2() {
        atm = new ATM(new PackVol1(10), new PackVol2(10));
        atm.cashIn(new PackVol5(5));
        atm.cashOut(50);

        assertTrue(atm.getBalance() == 5);

    }

    @After
    public void afterL5Test() {
        atm = null;
    }
}
