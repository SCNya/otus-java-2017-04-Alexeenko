package com.otus.alexeenko.l6;

import com.otus.alexeenko.atm.ATM;
import com.otus.alexeenko.atm.pack.PackVol1;
import com.otus.alexeenko.atm.pack.PackVol2;
import com.otus.alexeenko.atm.pack.PackVol5;

/**
 * Created by Vsevolod on 22/05/2017.
 */
public class L6 {
    public static void main(String[] args) {
        ATMDepartment department = new ATMDepartment();
        ATM atm1 = new ATM(new PackVol1(10), new PackVol2(10),
                new PackVol5(101));

        department.add(atm1);

        atm1.cashOut(100);

        department.restoreATMs();


        System.out.println(department.getFullBalance());
    }
}
