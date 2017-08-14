package com.otus.alexeenko.msg.service;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.MsgSystem;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vsevolod on 05/08/2017.
 */
public abstract class MsgService implements MsgSystem {
    private final ExecutorService executor;
    protected final Logger LOGGER;
    protected MsgConnection msgServer;

    public MsgService(Logger logger) {
        this.LOGGER = logger;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void setMsgServer(MsgConnection msgServer) {
        this.msgServer = msgServer;
    }

    @Override
    public void start() {
        executor.execute(this::work);
    }

    private void work() {
        try {
            while (!executor.isShutdown() & !msgServer.isClose()) {
                msgProcessing();
                Thread.sleep(WORK_DELAY);
            }
        } catch (InterruptedException e) {
            LOGGER.info("dispose");
        } finally {
            dispose();
        }
    }

    protected abstract void msgProcessing();

    @Override
    public synchronized void dispose() {
        executor.shutdownNow();
    }
}
