package com.otus.alexeenko.database.services.custom.net;

import com.otus.alexeenko.database.services.custom.beans.internal.DataBaseMBeanConfiguration;
import com.otus.alexeenko.database.services.custom.beans.spi.MBeanConfiguration;
import com.otus.alexeenko.msg.service.MsgNetService;
import com.otus.alexeenko.msg.types.Message;

import java.util.ArrayList;
import java.util.List;

import static com.otus.alexeenko.msg.types.ClientTypes.BACKEND;
import static com.otus.alexeenko.msg.types.Message.Headers.MANAGEMENT_INFO;
import static com.otus.alexeenko.msg.types.Message.Headers.STATISTICS;
import static com.otus.alexeenko.msg.types.MsgTypes.RESPONSE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 05/08/2017.
 */
public final class MsgNetDbService extends MsgNetService {
    private final MBeanConfiguration configuration;

    public MsgNetDbService() {
        super(getLogger("MsgNetDbService"));
        configuration = new DataBaseMBeanConfiguration();
    }

    @Override
    protected void msgProcessing() {
        getMessages();
    }

    private void getMessages() {
        List<Message> requests = new ArrayList<>();
        List<Message> responses = new ArrayList<>();

        server.drainTo(requests);
        for (Message msg : requests)
            switch (msg.getType()) {
                case INFO:
                    sendInfo(BACKEND);
                    break;
                case REQUEST:
                    response(msg, responses);
                    break;
                default:
                    LOGGER.error("Bad message type");
                    break;
            }

        if (!responses.isEmpty())
            server.send(responses);
    }

    private void response(Message msg, List<Message> responses) {
        switch (msg.getHeader()) {
            case STATISTICS:
                responses.add(new Message(RESPONSE, STATISTICS,
                        configuration.getStatistics()));
                break;
            case MANAGEMENT_INFO:
                responses.add(new Message(RESPONSE, MANAGEMENT_INFO,
                        configuration.getManagementInfo()));
                break;
            case CONFIGURATION:
                configuration.setConfiguration(msg.getMessage());
                break;
            default:
                LOGGER.error("Bad message header");
                break;
        }
    }
}
