package com.otus.alexeenko.l13.system;

import com.otus.alexeenko.database.services.DB;
import com.otus.alexeenko.l13.system.frontend.EmbeddedMsgFrontendService;
import com.otus.alexeenko.l13.system.frontend.FrontendMsgService;
import com.otus.alexeenko.msg.MsgSystem;
import com.otus.alexeenko.msg.server.EmbeddedMsgServer;
import com.otus.alexeenko.msg.server.MsgServer;
import org.slf4j.Logger;

import static com.otus.alexeenko.msg.types.ClientTypes.BACKEND;
import static com.otus.alexeenko.msg.types.ClientTypes.FRONTEND;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 13/08/2017.
 */
public class EmbeddedMsgSystem implements MsgSystem {
    private static final Logger LOGGER = getLogger("EmbeddedMsgSystem");

    private static MsgSystem instance;

    private final MsgServer msgServer;
    private final DB db;
    private final FrontendMsgService frontedService;

    private EmbeddedMsgSystem() {
        msgServer = new EmbeddedMsgServer();
        db = new DB(msgServer.getConnection(BACKEND));
        frontedService = EmbeddedMsgFrontendService.getInstance();
        frontedService.setMsgServer(msgServer.getConnection(FRONTEND));
    }

    public synchronized static MsgSystem getInstance() {
        if (instance == null)
            instance = new EmbeddedMsgSystem();

        return instance;
    }

    @Override
    public synchronized void start() {
        LOGGER.info("start");
        msgServer.start();
        db.start();
        frontedService.start();
    }

    @Override
    public synchronized void dispose() {
        LOGGER.info("dispose");
        frontedService.dispose();
        db.dispose();
        msgServer.dispose();
    }
}
