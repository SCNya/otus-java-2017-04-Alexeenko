package com.otus.alexeenko.l6;

import com.otus.alexeenko.atm.ATM;
import com.otus.alexeenko.atm.Memento;
import com.otus.alexeenko.atm.pack.Pack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vsevolod on 22/05/2017.
 */
public class ATMDepartment {
    private final List<ATM> atms;
    private final List<Memento> states;

    public ATMDepartment() {
        atms = new ArrayList<>();
        states = new ArrayList<>();
    }

    public void add(ATM... atms) {
        for (ATM atm : atms) {
            this.atms.add(atm);
            this.states.add(atm.getState());
        }
    }

    public void add(List<ATM> atms) {
        for (ATM atm : atms) {
            this.atms.add(atm);
            this.states.add(atm.getState());
        }
    }

    public void restoreATMs() {
        Iterator<ATM> itATM = atms.iterator();
        Iterator<Memento> itState = states.iterator();

        while (itATM.hasNext() && itState.hasNext()) {
            List<Pack> newCreditPacks = new ArrayList<>();

            itState.next().getSate()
                    .forEach((volType, numberOfCredits)
                            -> createPack(newCreditPacks, volType, numberOfCredits));

            itATM.next().setState(newCreditPacks.toArray(new Pack[newCreditPacks.size()]));
        }
    }

    private void createPack(List<Pack> newCreditPacks, Class<? extends Pack> volType,
                            Integer numberOfCredits) {
        try {
            newCreditPacks.add(volType.getDeclaredConstructor(numberOfCredits.getClass())
                    .newInstance(numberOfCredits));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getFullBalance() {
        long balance = 0;

        for (ATM atm : atms)
            balance += atm.getBalance();

        return balance;
    }
}
