package com.otus.alexeenko.database.services.custom.service;

import com.otus.alexeenko.database.services.custom.beans.internal.DataBaseMBeanConfiguration;
import com.otus.alexeenko.database.services.custom.beans.spi.MBeanConfiguration;
import com.otus.alexeenko.msg.service.MsgService;
import com.otus.alexeenko.msg.types.Message;

import java.util.ArrayList;
import java.util.List;

import static com.otus.alexeenko.msg.types.HeaderTypes.*;
import static com.otus.alexeenko.msg.types.MsgTypes.RESPONSE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 05/08/2017.
 */
public final class MsgDbService extends MsgService {
    private final MBeanConfiguration configuration;

    public MsgDbService() {
        super(getLogger("MsgDbService"));
        configuration = new DataBaseMBeanConfiguration();
    }

    @Override
    protected void msgProcessing() {
        getMessages();
    }

    private void getMessages() {
        List<Message> in = new ArrayList<>();
        List<Message> out = new ArrayList<>();

        msgServer.getResponse(in);

        for (Message msg : in)
            switch (msg.getType()) {
                case REQUEST:
                    response(msg, out);
                    break;
                default:
                    LOGGER.error("Bad message type");
                    break;
            }

        msgServer.sendRequest(out);
    }

    private void response(Message msg, List<Message> out) {
        switch (msg.getHeader()) {
            case STATISTICS:
                out.add(new Message(RESPONSE, STATISTICS,
                        configuration.getStatistics()));
                break;
            case MANAGEMENT_INFO:
                out.add(new Message(RESPONSE, MANAGEMENT_INFO,
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
