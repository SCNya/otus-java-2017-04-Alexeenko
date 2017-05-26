package com.otus.alexeenko.l5.atm.pack;

import com.otus.alexeenko.l5.atm.money.CreditVolume1;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vsevolod on 19/05/2017.
 */
public class PackVol1 extends Pack {

    public PackVol1(Integer creditsVol) {
        super(CreditVolume1.VOL_SIZE, Stream
                .generate(CreditVolume1::new)
                .limit(creditsVol)
                .collect(Collectors.toList()));
    }
}
