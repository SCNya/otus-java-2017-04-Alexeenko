package com.otus.alexeenko.l5.test;

import com.otus.alexeenko.l5.ATM.ATM;
import com.otus.alexeenko.l5.framework.annotations.After;
import com.otus.alexeenko.l5.framework.annotations.Before;
import com.otus.alexeenko.l5.framework.annotations.Test;

/**
 * Created by Vsevolod on 13/05/2017.
 */
public class L5Test {
    private static ATM atm;

    public L5Test() {
        atm = new ATM(10, 10, 10);
    }

    @Before
    public void beforeL5Test() {
    }

    @Test
    public void getBalance() {
        System.out.println(atm.getBalance());
    }

    @Test
    public void cashIn() {
        atm.cashIn(50, 5,20);
        System.out.println(atm.getBalance());
    }

    @Test
    public void cashOut() {
        atm.cashOut(55);
        System.out.println(atm.getBalance());
    }

    @After
    public void afterL5Test() {
        atm.cashOut(atm.getBalance());
    }
}
