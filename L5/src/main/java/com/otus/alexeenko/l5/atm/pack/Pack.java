package com.otus.alexeenko.l5.atm.pack;

import com.otus.alexeenko.l5.atm.money.Money;

import java.util.List;
import java.util.Objects;

/**
 * Created by Vsevolod on 19/05/2017.
 */
public abstract class Pack implements Comparable<Pack> {
    public final int VOL_SIZE;

    private final List<Money> creditsVol;

    Pack(int VOL_SIZE, List<Money> creditsVol) {
        this.VOL_SIZE = VOL_SIZE;
        this.creditsVol = creditsVol;
    }

    public void cashIn(Pack creditPack) {
        creditsVol.addAll(creditPack.creditsVol);
    }

    public int getNumberOfCredits() {
        return creditsVol.size();
    }

    private void decrease(int numberOfVol) {
        int i = 0;

        while (i < numberOfVol) {
            creditsVol.remove(creditsVol.size() - 1);
            i++;
        }
    }

    public long getCash(long cashOutSize, boolean forReal) {
        int dec = (int) cashOutSize / VOL_SIZE;
        int numberOfCredits = getNumberOfCredits();
        int decreaseSize;

        if (dec >= numberOfCredits) {
            cashOutSize -= numberOfCredits * VOL_SIZE;
            decreaseSize = numberOfCredits;
        } else {
            cashOutSize -= dec * VOL_SIZE;
            decreaseSize = dec;
        }

        if (forReal)
            decrease(decreaseSize);

        return cashOutSize;
    }

    private int compare(int x, int y) {
        return (x < y) ? 1 : ((x == y) ? 0 : -1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pack pack = (Pack) o;
        return VOL_SIZE == pack.VOL_SIZE &&
                Objects.equals(creditsVol, pack.creditsVol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(VOL_SIZE, creditsVol);
    }

    @Override
    public int compareTo(Pack anotherPack) {

        return compare(this.VOL_SIZE, anotherPack.VOL_SIZE);
    }
}