package com.otus.alexeenko.msg.server;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.connection.EmbeddedMsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class EmbeddedMsgServer implements MsgServer {
    private static final Logger LOGGER = getLogger("EmbeddedMsgServer");

    private int connectionCounter = 0;

    private final ExecutorService executor;
    private final Queue<MsgConnection> backends;
    private final Queue<MsgConnection> frontends;
    private final List<Map.Entry<MsgConnection, MsgConnection>> clientPairs; //Map.Entry<Backend, Frontend>

    public EmbeddedMsgServer() {
        executor = Executors.newSingleThreadExecutor();
        backends = new ConcurrentLinkedQueue<>();
        frontends = new ConcurrentLinkedQueue<>();
        clientPairs = new ArrayList<>();
    }

    @Override
    public void start() {
        executor.execute(this::processing);
    }

    @Override
    public MsgConnection getConnection(ClientTypes type) {
        MsgConnection connection = new EmbeddedMsgConnection(connectionCounter);

        connection.setType(type);
        add(connection);
        ++connectionCounter;
        return connection;
    }

    private void add(MsgConnection client) {
        if (isBackend(client))
            backends.add(client);
        else
            frontends.add(client);

        LOGGER.info("Client " + client.getId() + " added as " + client.getType());
    }

    private boolean isBackend(MsgConnection client) {
        return client.getType() == ClientTypes.BACKEND;
    }

    private void processing() {
        try {
            while (!executor.isShutdown()) {
                pairsCreation();
                forwarding();
                Thread.sleep(WORK_DELAY);
            }
        } catch (InterruptedException e) {
            LOGGER.info("dispose");
        }
    }

    private void forwarding() {
        for (Map.Entry<MsgConnection, MsgConnection> pair : clientPairs) {
            toBackend(pair);
            toFrontend(pair);
        }
    }

    private void toBackend(Map.Entry<MsgConnection, MsgConnection> pair) {
        List<Message> messages = new ArrayList<>();

        pair.getValue().getRequest(messages);           //from Frontend
        pair.getKey().sendResponse(messages);               //to Backend
    }

    private void toFrontend(Map.Entry<MsgConnection, MsgConnection> pair) {
        List<Message> messages = new ArrayList<>();

        pair.getKey().getRequest(messages);           //from Backend
        pair.getValue().sendResponse(messages);           //to Frontend
    }


    private void pairsCreation() {
        checkConnections();
        checkExistingPairs();
        createNewPairs();
    }

    private void checkExistingPairs() {
        Iterator<Map.Entry<MsgConnection, MsgConnection>> it = clientPairs.iterator();

        while (it.hasNext()) {
            Map.Entry<MsgConnection, MsgConnection> pair = it.next();
            MsgConnection backend = pair.getKey();
            MsgConnection frontend = pair.getValue();

            if (backend.isClose() | frontend.isClose()) {
                LOGGER.info("Pair destroyed clients: " + backend.getId() + " and " + frontend.getId());
                destroyPair(backend, frontend);
                it.remove();
            }
        }
    }

    private void createNewPairs() {
        while (!frontends.isEmpty() & !backends.isEmpty()) {
            MsgConnection backend = backends.poll();
            MsgConnection frontend = frontends.poll();

            LOGGER.info("Pair created with clients: " + backend.getId() + " and " + frontend.getId());
            clientPairs.add(new AbstractMap.SimpleEntry<>(backend, frontend));
        }
    }

    private void destroyPair(MsgConnection backend, MsgConnection frontend) {
        if (!backend.isClose())
            backends.add(backend);
        else
            LOGGER.info("Backend id = " + backend.getId() + " removed");

        if (!frontend.isClose())
            frontends.add(frontend);
        else
            LOGGER.info("Frontend id = " + frontend.getId() + " removed");
    }

    private void checkConnections() {
        removeLostConnections(frontends.iterator());
        removeLostConnections(backends.iterator());
    }

    private void removeLostConnections(Iterator<MsgConnection> it) {
        MsgConnection client;

        while (it.hasNext()) {
            client = it.next();
            if (client.isClose()) {
                LOGGER.info(client.getType() + " id = " + client.getId() + " removed");
                it.remove();
            }
        }
    }

    @Override
    public synchronized void dispose() {
        executor.shutdownNow();

        for (Map.Entry<MsgConnection, MsgConnection> pair : clientPairs) {
            pair.getValue().close();
            pair.getKey().close();
        }

        for (MsgConnection backend : backends)
            backend.close();

        for (MsgConnection frontend : frontends)
            frontend.close();
    }
}
