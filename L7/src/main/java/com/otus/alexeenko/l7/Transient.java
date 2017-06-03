package com.otus.alexeenko.l7;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Vsevolod on 01/06/2017.
 */
public class Transient implements Serializable {

    private static final long serialVersionUID = -5841495242249536497L;

    private final transient char[] chars = {'q','w','e','r'};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transient that = (Transient) o;
        return Arrays.equals(chars, that.chars);
    }
}
