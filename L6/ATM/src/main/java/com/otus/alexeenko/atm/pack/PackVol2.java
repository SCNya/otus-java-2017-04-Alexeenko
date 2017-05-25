package com.otus.alexeenko.atm.pack;

import com.otus.alexeenko.atm.money.CreditVolume2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 19/05/2017.
 */
public class PackVol2 extends Pack {

    public PackVol2(Integer creditsVol) {
        super(CreditVolume2.VOL_SIZE, Stream
                .generate(CreditVolume2::new)
                .limit(creditsVol)
                .collect(Collectors.toList()));
    }
}
