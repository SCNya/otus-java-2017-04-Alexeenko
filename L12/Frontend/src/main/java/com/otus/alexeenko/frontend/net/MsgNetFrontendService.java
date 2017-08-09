package com.otus.alexeenko.frontend.net;

import com.otus.alexeenko.msg.service.MsgNetService;
import com.otus.alexeenko.msg.types.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 09/08/2017.
 */
public final class MsgNetFrontendService extends MsgNetService implements FrontendNetService {
    private static MsgNetFrontendService instance;

    private final BlockingQueue<Message> output;
    private final BlockingQueue<Message> input;

    private MsgNetFrontendService() {
        super(getLogger("MsgNetFrontendService"));

        output = new LinkedBlockingQueue<>();
        input = new LinkedBlockingQueue<>();
    }

    public synchronized static MsgNetFrontendService getInstance() {
        if (instance == null)
            instance = new MsgNetFrontendService();

        return instance;
    }

    @Override
    protected void msgProcessing() {

    }

    @Override
    public String getStatistics() {
        return null;
    }

    @Override
    public String getManagementInfo() {
        return null;
    }

    @Override
    public void setConfiguration(String data) {

    }
}
