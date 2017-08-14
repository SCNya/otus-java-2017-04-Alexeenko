package com.otus.alexeenko.msg;

import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;

import java.util.List;

public interface MsgConnection {
    void setType(ClientTypes type);

    ClientTypes getType();

    int getId();

    void sendRequest(List<Message> messages);

    void sendResponse(List<Message> messages);

    int getRequest(List<Message> messages);

    int getResponse(List<Message> messages);

    boolean isClose();

    void close();
}
