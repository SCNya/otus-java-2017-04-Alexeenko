package com.otus.alexeenko.atm.pack;

import com.otus.alexeenko.atm.money.CreditVolume2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 19/05/2017.
 */
public class PackVol2 extends Pack {

    private static final int VOL_SIZE = 2;

    public PackVol2(Integer creditsVol2) {
        super(VOL_SIZE, Stream
                .generate(CreditVolume2::new)
                .limit(creditsVol2)
                .collect(Collectors.toList()));
    }
}
