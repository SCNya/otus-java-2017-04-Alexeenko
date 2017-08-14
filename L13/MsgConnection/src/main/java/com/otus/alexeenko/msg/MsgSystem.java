package com.otus.alexeenko.msg;

/**
 * Created by Vsevolod on 02/08/2017.
 */
public interface MsgSystem {
    int ATTEMPTS_TO_OBTAIN = 5;
    int WORK_DELAY = 100;

    void start();

    void dispose();
}
