package com.otus.alexeenko.msg;

import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;

public interface MsgConnection extends MsgNetSystem {
    void setType(ClientTypes type);

    ClientTypes getType();

    int getId();

    void send(Message message);

    Message pool();

    boolean isClose();

    void dispose();
}
