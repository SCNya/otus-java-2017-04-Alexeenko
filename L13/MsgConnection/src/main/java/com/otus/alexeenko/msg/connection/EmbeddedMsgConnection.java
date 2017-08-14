package com.otus.alexeenko.msg.connection;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class EmbeddedMsgConnection implements MsgConnection {
    private final int id;
    private final BlockingQueue<Message> input;
    private final BlockingQueue<Message> output;

    private boolean close;
    private ClientTypes clientType;

    public EmbeddedMsgConnection(int id) {
        this.close = false;
        this.id = id;
        this.input = new LinkedBlockingQueue<>();
        this.output = new LinkedBlockingQueue<>();
    }

    @Override
    public void setType(ClientTypes clientType) {
        this.clientType = clientType;
    }

    @Override
    public ClientTypes getType() {
        return clientType;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void sendRequest(List<Message> messages) {
        output.addAll(messages);
    }

    @Override
    public void sendResponse(List<Message> messages) {
        input.addAll(messages);
    }

    @Override
    public int getRequest(List<Message> messages) {
        return output.drainTo(messages);
    }

    @Override
    public int getResponse(List<Message> messages) {
        return input.drainTo(messages);
    }

    @Override
    public boolean isClose() {
        return close;
    }

    @Override
    public void close() {
        close = true;
    }
}
