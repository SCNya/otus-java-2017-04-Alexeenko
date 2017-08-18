package com.otus.alexeenko.msg.types;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class Message {
    private final MsgTypes type;
    private final String header;
    private final String message;

    public Message(MsgTypes type, String header, String message) {
        this.type = type;
        this.header = header;
        this.message = message;
    }

    public MsgTypes getType() {
        return type;
    }

    public String getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
}
