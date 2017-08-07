package com.otus.alexeenko.database.services.custom.net;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.MsgNetSystem;
import com.otus.alexeenko.msg.connection.SimpleMsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;
import com.otus.alexeenko.msg.types.MsgTypes;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 05/08/2017.
 */
public class MsgNetDbService implements MsgNetSystem {
    private static final Logger LOGGER = getLogger("MsgNetDbService");
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5050;

    private final ExecutorService executor;
    private MsgConnection server;

    public MsgNetDbService() {
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
                receiveMsg();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            LOGGER.info("dispose");
        }
    }

    private void receiveMsg() {
        Message msg = server.pool();

        if (msg != null)
            switch (msg.getType()) {
                case INFO:
                    sendInfo();
                    break;
                case REQUEST:
                    break;
                default:
                    LOGGER.error("Bad message");
                    break;
            }
    }

    private void sendInfo() {
        Message infoMessage = new Message(MsgTypes.INFO, ClientTypes.BACKEND.toString());
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
            Thread.sleep(1000);
        }
    }


    @Override
    public synchronized void dispose() {
        executor.shutdownNow();
        if (server != null)
            server.dispose();
    }
}
