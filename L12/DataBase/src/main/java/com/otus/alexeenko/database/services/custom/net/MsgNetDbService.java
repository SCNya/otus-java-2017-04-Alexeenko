package com.otus.alexeenko.database.services.custom.net;

import com.otus.alexeenko.database.services.custom.beans.internal.DataBaseMBeanConfiguration;
import com.otus.alexeenko.database.services.custom.beans.spi.MBeanConfiguration;
import com.otus.alexeenko.msg.service.MsgNetService;
import com.otus.alexeenko.msg.types.Message;

import static com.otus.alexeenko.msg.types.MsgHeaders.MANAGEMENT_INFO;
import static com.otus.alexeenko.msg.types.MsgHeaders.STATISTICS;
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
        Message msg = server.pool();

        if (msg != null)
            switch (msg.getType()) {
                case INFO:
                    sendInfo();
                    break;
                case REQUEST:
                    response(msg);
                    break;
                default:
                    LOGGER.error("Bad message type");
                    break;
            }
    }

    private void response(Message msg) {
        switch (msg.getHeader()) {
            case STATISTICS:
                server.send(new Message(RESPONSE, STATISTICS,
                        configuration.getStatistics()));
                break;
            case MANAGEMENT_INFO:
                server.send(new Message(RESPONSE, MANAGEMENT_INFO,
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
