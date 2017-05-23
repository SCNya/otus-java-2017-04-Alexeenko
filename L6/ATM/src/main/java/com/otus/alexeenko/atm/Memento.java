package com.otus.alexeenko.atm;

import com.otus.alexeenko.atm.pack.Pack;

import java.util.Map;

/**
 * Created by Vsevolod on 22/05/2017.
 */
public class Memento {
    private final Map<Class<? extends Pack>, Integer> state;

    public Memento(Map<Class<? extends Pack>, Integer> state) {
        this.state = state;
    }

    public Map<Class<? extends Pack>, Integer> getSate() {
        return state;
    }
}
