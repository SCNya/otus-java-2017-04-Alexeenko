package com.otus.alexeenko.frontend.net;

import com.otus.alexeenko.msg.service.MsgNetService;
import com.otus.alexeenko.msg.types.Message;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.otus.alexeenko.msg.types.ClientTypes.FRONTEND;
import static com.otus.alexeenko.msg.types.HeaderTypes.*;
import static com.otus.alexeenko.msg.types.MsgTypes.REQUEST;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 09/08/2017.
 */
public final class MsgNetFrontendService extends MsgNetService implements FrontendNetService {
    private static FrontendNetService instance;

    private final BlockingQueue<String> managementInfoQueue;
    private final BlockingQueue<String> statisticsQueue;
    private final BlockingQueue<Message> outQueue;

    private MsgNetFrontendService() {
        super(getLogger("MsgNetFrontendService [" + ManagementFactory.getRuntimeMXBean().getName() + ']'));

        managementInfoQueue = new LinkedBlockingQueue<>();
        statisticsQueue = new LinkedBlockingQueue<>();
        outQueue = new LinkedBlockingQueue<>();
    }

    public synchronized static FrontendNetService getInstance() {
        if (instance == null)
            instance = new MsgNetFrontendService();

        return instance;
    }

    @Override
    protected void msgProcessing() {
        getMessages();
        sendMessages();
    }

    private void getMessages() {
        List<Message> messages = new ArrayList<>();
        server.drainTo(messages);

        for (Message msg : messages)
            switch (msg.getType()) {
                case INFO:
                    sendInfo(FRONTEND);
                    break;
                case RESPONSE:
                    takeResponse(msg);
                    break;
                default:
                    LOGGER.error("Bad message type");
                    break;
            }
    }

    private void sendMessages() {
        List<Message> messages = new ArrayList<>();

        outQueue.drainTo(messages);
        server.send(messages);
    }

    private void takeResponse(Message msg) {
        switch (msg.getHeader()) {
            case STATISTICS:
                statisticsQueue.add(msg.getMessage());
                break;
            case MANAGEMENT_INFO:
                managementInfoQueue.add(msg.getMessage());
                break;
            default:
                LOGGER.error("Bad message header");
                break;
        }
    }

    @Override
    public String getStatistics() {
        try {
            String statistics = "";
            Message configurationMessage = new Message(REQUEST, STATISTICS, "");
            outQueue.add(configurationMessage);

            for (int i = 0; i < ATTEMPTS_TO_OBTAIN; ++i)
                if ((statistics = statisticsQueue.poll(1, TimeUnit.SECONDS)) != null)
                    break;

            return statistics;
        } catch (InterruptedException e) {
            LOGGER.info("dispose");
            return null;
        }
    }

    @Override
    public String getManagementInfo() {
        try {
            String managementInfo = "";
            Message configurationMessage = new Message(REQUEST, MANAGEMENT_INFO, "");
            outQueue.add(configurationMessage);

            for (int i = 0; i < ATTEMPTS_TO_OBTAIN; ++i)
                if ((managementInfo = managementInfoQueue.poll(1, TimeUnit.SECONDS)) != null)
                    break;

            return managementInfo;
        } catch (InterruptedException e) {
            LOGGER.info("dispose");
            return null;
        }
    }

    @Override
    public void setConfiguration(String data) {
        Message configurationMessage = new Message(REQUEST, CONFIGURATION, data);
        outQueue.add(configurationMessage);
    }
}
