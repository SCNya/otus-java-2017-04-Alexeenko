package com.otus.alexeenko.l5.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsevolod on 15/05/2017.
 */
public class Result {
    private final List<Info> results;

    Result() {
        this.results = new ArrayList<>();
    }

    void add(Info testInfo) {
        results.add(testInfo);
    }

    public void view() {
        if (results.size() == 0)
            System.out.println("No results");
        else {
            for (Info record : results) {
                sleep();
                record.view();
            }
        }
    }

    /**
     * Need for step by step printout
     */
    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
