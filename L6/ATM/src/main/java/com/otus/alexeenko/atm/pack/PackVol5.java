package com.otus.alexeenko.atm.pack;

import com.otus.alexeenko.atm.money.CreditVolume5;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 19/05/2017.
 */
public class PackVol5 extends Pack {

    private static final int VOL_SIZE = 5;

    public PackVol5(Integer creditsVol) {
        super(VOL_SIZE, Stream
                .generate(CreditVolume5::new)
                .limit(creditsVol)
                .collect(Collectors.toList()));
    }
}
