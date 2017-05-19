package com.otus.alexeenko.l5.atm;

import com.otus.alexeenko.l5.atm.pack.Pack;

import java.util.*;

/**
 * Created by Vsevolod on 16/05/2017.
 */
public class ATM {
    private final List<Pack> packs;

    public ATM(Pack... creditPacks) {
        packs = new ArrayList<>();

        Collections.addAll(packs, creditPacks);
        Collections.sort(packs);
    }

    public void cashIn(Pack... creditPacks) {
        for (Pack pack : creditPacks)
            packs.forEach((n) -> {
                if (pack.VOL_SIZE == n.VOL_SIZE)
                    n.cashIn(pack);
            });
    }

    public void cashOut(final long cashOutSize) {
        if (cashOutSize > 0) {

            if (cashOutSize <= getBalance()) {

                long checkSize = getCashOut(cashOutSize, false);

                if (checkSize > 0)
                    System.out.println("\nSorry!\nYou can't take all money (not enough the Credit Volumes)");
                else getCashOut(cashOutSize, true);
            } else
                System.out.println("On You balance not enough money!");
        } else
            System.out.println("Get a zero credits?");
    }

    private long getCashOut(long cashOutSize, boolean forReal) {
        for (Pack pack : packs)
            cashOutSize = getCash(pack, cashOutSize, forReal);

        return cashOutSize;
    }

    private long getCash(Pack packVol, long cashOutSize, boolean forReal) {
        int dec = (int) cashOutSize / packVol.VOL_SIZE;
        int numberOfCredits = packVol.getNumberOfCredits();

        if (dec >= numberOfCredits) {
            cashOutSize -= numberOfCredits * packVol.VOL_SIZE;
            if (forReal)
                packVol.decrease(numberOfCredits);
        } else {
            cashOutSize -= dec * packVol.VOL_SIZE;
            if (forReal)
                packVol.decrease(dec);
        }

        return cashOutSize;
    }

    public long getBalance() {
        long balance = 0;

        for (Pack pack : packs)
            balance += pack.getNumberOfCredits() * pack.VOL_SIZE;

        return balance;
    }
}