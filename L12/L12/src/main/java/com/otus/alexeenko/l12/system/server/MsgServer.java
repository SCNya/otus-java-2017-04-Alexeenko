package com.otus.alexeenko.l12.system.server;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.MsgNetSystem;
import com.otus.alexeenko.msg.connection.SimpleMsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.otus.alexeenko.msg.types.MsgHeaders.HANDSHAKE;
import static com.otus.alexeenko.msg.types.MsgTypes.INFO;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class MsgServer implements MsgNetSystem {
    private static final Logger LOGGER = getLogger("MsgServer");
    private static final int WORK_THREADS = 2;
    private static final int PORT = 5050;

    private final ExecutorService executor;
    private final Queue<MsgConnection> backends;
    private final Queue<MsgConnection> frontends;
    private final List<Map.Entry<MsgConnection, MsgConnection>> clientPairs; //Map.Entry<Backend, Frontend>

    public MsgServer() {
        executor = Executors.newFixedThreadPool(WORK_THREADS);
        backends = new ConcurrentLinkedQueue<>();
        frontends = new ConcurrentLinkedQueue<>();
        clientPairs = new ArrayList<>();
    }

    @Override
    public void start() {
        executor.execute(this::listening);
        executor.execute(this::processing);
    }

    private void listening() {
        MsgConnection client = null;
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Integer id = 0;
            LOGGER.info("Server started on port: " + serverSocket.getLocalPort());

            while (!executor.isShutdown()) {
                Socket clientSocket = serverSocket.accept();

                client = new SimpleMsgConnection(id, clientSocket);
                client.start();

                if (isTyped(client)) {
                    if (isBackend(client))
                        backends.add(client);
                    else
                        frontends.add(client);

                    LOGGER.info("Client " + id + " added as " + client.getType());

                    ++id;
                }
            }
        } catch (InterruptedException | IOException e) {
            if (client != null)
                client.dispose();
            LOGGER.info("dispose");
        }
    }

    private boolean isTyped(MsgConnection client) throws InterruptedException {
        int i = 0;
        sendInfo(client);

        while (i < ATTEMPTS_TO_OBTAIN) {
            Message msg = client.poll();

            if (msg != null)
                if (msg.getType() == INFO) {
                    client.setType(ClientTypes.valueOf(msg.getMessage()));
                    return true;
                } else {
                    LOGGER.error("Bad client type");
                    break;
                }
            Thread.sleep(RECEIVE_DELAY);
            ++i;
        }

        client.dispose();
        return false;
    }

    private void sendInfo(MsgConnection client) {
        Message infoMessage = new Message(INFO, HANDSHAKE, "");
        client.send(infoMessage);
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
        pair.getValue().drainTo(messages);           //from Frontend

        for (Message msg : messages)
            pair.getKey().send(msg);                //to Backend
    }

    private void toFrontend(Map.Entry<MsgConnection, MsgConnection> pair) {
        List<Message> messages = new ArrayList<>();
        pair.getKey().drainTo(messages);           //from Backend

        for (Message msg : messages)
            pair.getValue().send(msg);            //to Frontend
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
        while (!frontends.isEmpty() && !backends.isEmpty()) {
            MsgConnection backend = backends.poll();
            MsgConnection frontend = frontends.poll();

            LOGGER.info("Pair created with clients: " + backend.getId() + " and " + frontend.getId());
            clientPairs.add(new AbstractMap.SimpleEntry<>(backend, frontend));
        }
    }

    private void destroyPair(MsgConnection backend, MsgConnection frontend) {
        if (!backend.isClose())
            backends.add(backend);
        else {
            LOGGER.info("Backend id = " + backend.getId() + " removed");
            backend.dispose();
        }

        if (!frontend.isClose())
            frontends.add(frontend);
        else {
            LOGGER.info("Frontend id = " + frontend.getId() + " removed");
            frontend.dispose();
        }
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
                client.dispose();
                it.remove();
            }
        }
    }

    @Override
    public synchronized void dispose() {
        executor.shutdownNow();

        for (Map.Entry<MsgConnection, MsgConnection> pair : clientPairs) {
            pair.getValue().dispose();
            pair.getKey().dispose();
        }

        for (MsgConnection backend : backends)
            backend.dispose();

        for (MsgConnection frontend : frontends)
            frontend.dispose();
    }
}
