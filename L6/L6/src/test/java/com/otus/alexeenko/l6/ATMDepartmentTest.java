package com.otus.alexeenko.l6;

import com.otus.alexeenko.atm.ATM;
import com.otus.alexeenko.atm.pack.PackVol1;
import com.otus.alexeenko.atm.pack.PackVol2;
import com.otus.alexeenko.atm.pack.PackVol5;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Vsevolod on 23/05/2017.
 */
public class ATMDepartmentTest {
    private final ATMDepartment department = new ATMDepartment();
    private final List<ATM> atms = new ArrayList<>();

    private long defaultBalance;

    @Before
    public void BeforeATMDepartmentTest() {
        atms.add(new ATM(new PackVol1(10), new PackVol2(10),
                new PackVol5(25)));

        atms.add(new ATM(new PackVol1(10),
                new PackVol5(10)));

        atms.add(new ATM(new PackVol1(50), new PackVol2(5),
                new PackVol5(5)));

        for (ATM atm : atms)
            defaultBalance += atm.getBalance();
    }

    @Test
    public void add() {
        department.add(atms);

        assertTrue(department.getFullBalance() == defaultBalance);
    }

    @Test
    public void restoreATMs() {
        long currentBalance = 0;
        department.add(atms);

        for (ATM atm : atms) {
            atm.cashOut(50);
            currentBalance += atm.getBalance();
        }

        assertTrue(department.getFullBalance() == currentBalance);

        department.restoreATMs();

        assertTrue(department.getFullBalance() == defaultBalance);

    }

    @Test
    public void getFullBalance() {
        assertTrue(department.getFullBalance() == 0);

        department.add(new ATM(new PackVol1(1)));

        assertTrue(department.getFullBalance() == 1);
    }

}