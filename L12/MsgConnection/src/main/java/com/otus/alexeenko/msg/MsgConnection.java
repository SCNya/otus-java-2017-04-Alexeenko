package com.otus.alexeenko.msg;

import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;

import java.util.List;

public interface MsgConnection extends MsgNetSystem {
    void setType(ClientTypes type);

    ClientTypes getType();

    int getId();

    void send(Message message);

    void send(List<Message> messages);

    Message poll();

    int drainTo(List<Message> messages);

    boolean isClose();

    void dispose();
}
