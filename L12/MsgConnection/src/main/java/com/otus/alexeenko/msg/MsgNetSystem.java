package com.otus.alexeenko.msg;

/**
 * Created by Vsevolod on 02/08/2017.
 */
public interface MsgNetSystem {
    int ATTEMPTS_TO_OBTAIN = 5;
    int WORK_DELAY = 100;
    int RECEIVE_DELAY = 1000;
    int CONNECTION_DELAY= 1000;

    void start();

    void dispose();
}
