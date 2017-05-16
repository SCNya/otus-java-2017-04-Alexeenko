package com.otus.alexeenko.l5.ATM;

import com.otus.alexeenko.l5.ATM.money.CreditVolume1;
import com.otus.alexeenko.l5.ATM.money.CreditVolume2;
import com.otus.alexeenko.l5.ATM.money.CreditVolume5;
import com.otus.alexeenko.l5.ATM.money.Money;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 16/05/2017.
 */
public class ATM {
    private List<Money> creditsVol5;
    private List<Money> creditsVol2;
    private List<Money> creditsVol1;

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

    public void cashOut(long cashOutSize) {

        if (cashOutSize <= getBalance()) {

            if (cashOutSize > 0) {
                cashOutSize = cashOut(creditsVol5, Money.VOL_SIZE_5, cashOutSize);

                cashOutSize = cashOut(creditsVol2, Money.VOL_SIZE_2, cashOutSize);

                cashOutSize = cashOut(creditsVol1, Money.VOL_SIZE_1, cashOutSize);
            }

            if (cashOutSize > 0)
                System.out.println("\nSorry!\nYou can't take all money (not enough the Credit Volumes)");
        } else
            System.out.println("On You balance not enough money!");
    }

    private long cashOut(List<Money> creditsVol, int volSize, long cashOutSize) {
        int dec;
        int size;

        dec = (int) cashOutSize / volSize;
        size = creditsVol.size();

        if (dec >= size) {
            cashOutSize -= size * volSize;
            decrease(creditsVol, size);
        } else {
            cashOutSize -= dec * volSize;
            decrease(creditsVol, dec);
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