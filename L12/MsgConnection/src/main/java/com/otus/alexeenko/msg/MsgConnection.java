package com.otus.alexeenko.msg;

import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;

import java.util.concurrent.TimeUnit;

public interface MsgConnection extends MsgNetSystem {
    void setType(ClientTypes type);

    ClientTypes getType();

    int getId();

    void send(Message message);

    Message poll();

    Message poll(long timeout, TimeUnit unit) throws InterruptedException;

    boolean isClose();

    void dispose();
}
