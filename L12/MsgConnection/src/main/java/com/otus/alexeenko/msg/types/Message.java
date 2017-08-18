package com.otus.alexeenko.msg.types;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class Message {
    public enum Headers {
        HANDSHAKE,
        STATISTICS,
        MANAGEMENT_INFO,
        CONFIGURATION
    }

    private final MsgTypes type;
    private final Headers header;
    private final String message;

    public Message(MsgTypes type, Headers header, String message) {
        this.type = type;
        this.header = header;
        this.message = message;
    }

    public MsgTypes getType() {
        return type;
    }

    public Headers getHeader() {
        return header;
    }

    public String getMessage() {
        return message;
    }
}
