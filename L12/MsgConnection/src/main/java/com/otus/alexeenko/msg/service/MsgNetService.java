package com.otus.alexeenko.msg.service;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.MsgNetSystem;
import com.otus.alexeenko.msg.connection.SimpleMsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.otus.alexeenko.msg.types.MsgHeaders.HANDSHAKE;
import static com.otus.alexeenko.msg.types.MsgTypes.INFO;

/**
 * Created by Vsevolod on 05/08/2017.
 */
public abstract class MsgNetService implements MsgNetSystem {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5050;

    private final ExecutorService executor;
    protected final Logger LOGGER;
    protected MsgConnection server;

    public MsgNetService(Logger logger) {
        LOGGER = logger;
        executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void start() {
        executor.execute(this::work);
    }

    private void work() {
        try {
            while (!executor.isShutdown()) {
                checkConnection();
                msgProcessing();
                Thread.sleep(WORK_DELAY);
            }
        } catch (InterruptedException e) {
            LOGGER.info("dispose");
        }
    }

    protected abstract void msgProcessing();

    protected void sendInfo(ClientTypes type) {
        Message infoMessage = new Message(INFO, HANDSHAKE, type.toString());
        server.send(infoMessage);
    }

    private void checkConnection() throws InterruptedException {
        if (server != null) {
            if (server.isClose())
                getConnection();
        } else {
            getConnection();
        }
    }

    private void getConnection() throws InterruptedException {
        while (!executor.isShutdown()) {
            try {
                LOGGER.info("Try connection");
                server = new SimpleMsgConnection(0, new Socket(HOST, PORT));
                server.start();
                LOGGER.info("Success");
                break;
            } catch (IOException e) {
                if (server != null)
                    server.dispose();
                LOGGER.error(e.getMessage());
            }
            Thread.sleep(CONNECTION_DELAY);
        }
    }


    @Override
    public synchronized void dispose() {
        executor.shutdownNow();
        if (server != null)
            server.dispose();
    }
}