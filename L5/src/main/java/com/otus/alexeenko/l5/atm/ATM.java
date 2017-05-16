package com.otus.alexeenko.l5.atm;

import com.otus.alexeenko.l5.atm.money.CreditVolume1;
import com.otus.alexeenko.l5.atm.money.CreditVolume2;
import com.otus.alexeenko.l5.atm.money.CreditVolume5;
import com.otus.alexeenko.l5.atm.money.Money;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 16/05/2017.
 */
public class ATM {
    private final List<Money> creditsVol5;
    private final List<Money> creditsVol2;
    private final List<Money> creditsVol1;

    public ATM(int creditsVol1, int creditsVol2, int creditsVol5) {
        this.creditsVol1 = Stream
                .generate(CreditVolume1::new)
                .limit(creditsVol1)
                .collect(Collectors.toList());

        this.creditsVol2 = Stream
                .generate(CreditVolume2::new)
                .limit(creditsVol2)
                .collect(Collectors.toList());

        this.creditsVol5 = Stream
                .generate(CreditVolume5::new)
                .limit(creditsVol5)
                .collect(Collectors.toList());
    }

    public void cashIn(int creditsVol1, int creditsVol2, int creditsVol5) {
        this.creditsVol1.addAll(Stream
                .generate(CreditVolume1::new)
                .limit(creditsVol1)
                .collect(Collectors.toList()));

        this.creditsVol2.addAll(Stream
                .generate(CreditVolume2::new)
                .limit(creditsVol2)
                .collect(Collectors.toList()));

        this.creditsVol5.addAll(Stream
                .generate(CreditVolume5::new)
                .limit(creditsVol5)
                .collect(Collectors.toList()));
    }

    public void cashOut(final long cashOutSize) {

        if (cashOutSize > 0) {

            if (cashOutSize <= getBalance()) {

                long checkSize = cashOutSize;

                checkSize = check(creditsVol5, Money.VOL_SIZE_5, checkSize);

                checkSize = check(creditsVol2, Money.VOL_SIZE_2, checkSize);

                checkSize = check(creditsVol1, Money.VOL_SIZE_1, checkSize);

                if (checkSize > 0)
                    System.out.println("\nSorry!\nYou can't take all money (not enough the Credit Volumes)");
                else getCashOut(cashOutSize);
            } else
                System.out.println("On You balance not enough money!");
        } else
            System.out.println("Get a zero credits?");
    }

    private long check(List<Money> credits, int volSize, long checkSize) {
        int dec;
        int size;

        dec = (int) checkSize / volSize;
        size = credits.size();

        if (dec >= size)
            checkSize -= size * volSize;
        else
            checkSize -= dec * volSize;
        return checkSize;
    }

    private void getCashOut(long cashOutSize) {

        cashOutSize = getCash(creditsVol5, Money.VOL_SIZE_5, cashOutSize);

        cashOutSize = getCash(creditsVol2, Money.VOL_SIZE_2, cashOutSize);

        getCash(creditsVol1, Money.VOL_SIZE_1, cashOutSize);
    }

    private long getCash(List<Money> credits, int volSize, long cashOutSize) {
        int dec;
        int size;

        dec = (int) cashOutSize / volSize;
        size = credits.size();

        if (dec >= size) {
            cashOutSize -= size * volSize;
            decrease(credits, size);
        } else {
            cashOutSize -= dec * volSize;
            decrease(credits, dec);
        }

        return cashOutSize;
    }

    private void decrease(List<Money> list, int numberOfVol) {
        int i = 0;

        while (i < numberOfVol) {
            list.remove(list.size() - 1);
            i++;
        }
    }

    public long getBalance() {
        return creditsVol1.size() + Money.VOL_SIZE_2 * creditsVol2.size() +
                Money.VOL_SIZE_5 * creditsVol5.size();
    }
}