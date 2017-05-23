package com.otus.alexeenko.atm;

import com.otus.alexeenko.atm.pack.Pack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Memento getState() {
        Map<Class<? extends Pack>, Integer> state = packs.stream()
                .collect(Collectors.toMap(Pack::getClass, Pack::getNumberOfCredits));

        return new Memento(state);
    }

    /**
     *
     * @param newPacks with money
     * @return all money packs
     */
    public Pack[] setState(Pack... newPacks) {
        Pack[] oldPacks = packs.toArray(new Pack[packs.size()]);
        packs.clear();

        Collections.addAll(packs, newPacks);
        Collections.sort(packs);

        return oldPacks;
    }

    public void cashIn(Pack... additionalPacks) {
        for (Pack additionalPack : additionalPacks) {
            boolean find = false;

            for (Pack existPack : packs)
                if (additionalPack.VOL_SIZE == existPack.VOL_SIZE) {
                    existPack.cashIn(additionalPack);
                    find = true;
                    break;
                }

            if (!find) {
                packs.add(additionalPack);
                Collections.sort(packs);
            }
        }
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
            cashOutSize = pack.getCash(cashOutSize, forReal);

        return cashOutSize;
    }

    public long getBalance() {
        long balance = 0;

        for (Pack pack : packs)
            balance += pack.getNumberOfCredits() * pack.VOL_SIZE;

        return balance;
    }
}