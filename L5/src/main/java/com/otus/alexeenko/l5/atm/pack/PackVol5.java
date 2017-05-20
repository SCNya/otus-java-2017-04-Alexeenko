package com.otus.alexeenko.l5.atm.pack;

import com.otus.alexeenko.l5.atm.money.CreditVolume5;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 19/05/2017.
 */
public class PackVol5 extends Pack {

    public static final int VOL_SIZE = 5;

    public PackVol5(int creditsVol5) {
        super(VOL_SIZE, Stream
                .generate(CreditVolume5::new)
                .limit(creditsVol5)
                .collect(Collectors.toList()));
    }
}
