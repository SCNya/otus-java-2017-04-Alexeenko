package com.otus.alexeenko.l13.system.frontend;

import com.otus.alexeenko.msg.service.MsgService;
import com.otus.alexeenko.msg.types.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.otus.alexeenko.msg.types.HeaderTypes.*;
import static com.otus.alexeenko.msg.types.MsgTypes.REQUEST;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 09/08/2017.
 */
public final class EmbeddedMsgFrontendService extends MsgService implements FrontendMsgService {
    private static FrontendMsgService instance;

    private final BlockingQueue<String> managementInfoQueue;
    private final BlockingQueue<String> statisticsQueue;
    private final BlockingQueue<Message> outQueue;

    private EmbeddedMsgFrontendService() {
        super(getLogger("EmbeddedMsgFrontendService"));

        managementInfoQueue = new LinkedBlockingQueue<>();
        statisticsQueue = new LinkedBlockingQueue<>();
        outQueue = new LinkedBlockingQueue<>();
    }

    public synchronized static FrontendMsgService getInstance() {
        if (instance == null)
            instance = new EmbeddedMsgFrontendService();

        return instance;
    }

    @Override
    protected void msgProcessing() {
        getMessages();
        sendMessages();
    }

    private void getMessages() {
        List<Message> messages = new ArrayList<>();
        msgServer.getResponse(messages);

        for (Message msg : messages)
            switch (msg.getType()) {
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
        msgServer.sendRequest(messages);
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
