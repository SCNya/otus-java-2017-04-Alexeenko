package com.otus.alexeenko.l12.system.msg;

import com.otus.alexeenko.l12.system.runner.MyRunner;
import com.otus.alexeenko.l12.system.runner.ProcessRunner;
import com.otus.alexeenko.l12.system.server.MsgServer;
import com.otus.alexeenko.msg.MsgNetSystem;
import org.slf4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 01/08/2017.
 */
public class MessageSystem implements MsgNetSystem, MessageSystemMBean {
    private static final Logger LOGGER = getLogger("MessageSystem");
    private static final int NUMBER_OF_SERVICES = 3;
    private static final String DB_START_COMMAND = "java -jar ../DataBase/target/DataBase.jar";
    private static final String FRONTEND_START_COMMAND = "java -jar jetty/start.jar -Djetty.base=jetty/ -Djetty.http.port=808";

    private final List<ProcessRunner> dataBaseServices;
    private final List<ProcessRunner> frontendServices;
    private final MsgNetSystem msgServer;

    public MessageSystem() {
        dataBaseServices = new ArrayList<>();
        frontendServices = new ArrayList<>();
        msgServer = new MsgServer();

        for (int i = 0; i < NUMBER_OF_SERVICES; ++i)
            dataBaseServices.add(new MyRunner(DB_START_COMMAND));

        for (int i = 0; i < NUMBER_OF_SERVICES; ++i)
            frontendServices.add(new MyRunner(FRONTEND_START_COMMAND + i)); //8080 to 808+NUMBER_OF_SERVICES ports
    }

    @Override
    public synchronized void start() {
        msgServer.start();
        startBackend();
        startFrontend();
        enableMBean();
    }

    private void enableMBean() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName(this.getClass().getPackage().getName() +
                    ":type=" + this.getClass().getName());

            mbs.registerMBean(this, name);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void startFrontend() {
        try {
            for (ProcessRunner frontend : frontendServices)
                frontend.start(false);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void startBackend() {
        try {
            for (ProcessRunner backend : dataBaseServices)
                backend.start(false);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public synchronized void dispose() {
        for (ProcessRunner frontend : frontendServices)
            frontend.stop();

        for (ProcessRunner backend : dataBaseServices)
            backend.stop();

        msgServer.dispose();
    }

    @Override
    public void shutdown() {
        dispose();
    }
}
