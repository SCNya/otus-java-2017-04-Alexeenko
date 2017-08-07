package com.otus.alexeenko.msg.types;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class Message {
    private MsgTypes type;

    public MsgTypes getType() {
        return type;
    }

    private String message;

    public Message(MsgTypes type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
