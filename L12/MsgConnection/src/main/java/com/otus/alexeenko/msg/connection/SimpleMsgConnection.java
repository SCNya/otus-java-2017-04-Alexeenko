package com.otus.alexeenko.msg.connection;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;
import com.otus.alexeenko.msg.types.MsgHeaders;
import com.otus.alexeenko.msg.types.MsgTypes;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class SimpleMsgConnection implements MsgConnection {
    private static final Logger LOGGER = getLogger("SimpleMsgConnection");
    private static final int WORK_THREADS = 2;
    private static final int TYPE = 0;
    private static final int HEADER = 1;
    private static final int MESSAGE = 2;
    private static final int DATA_SIZE = 3;

    private final int id;
    private final Socket clientSocket;
    private final ExecutorService executor;
    private final BlockingQueue<Message> output;
    private final BlockingQueue<Message> input;

    private ClientTypes clientType;

    public SimpleMsgConnection(int id, Socket client) {
        this.id = id;
        this.clientSocket = client;
        this.executor = Executors.newFixedThreadPool(WORK_THREADS);
        this.output = new LinkedBlockingQueue<>();
        this.input = new LinkedBlockingQueue<>();
    }

    @Override
    public void start() {
        executor.execute(this::receiver);
        executor.execute(this::sender);
    }

    private void receiver() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String[] data = new String[DATA_SIZE];
            String inputLine;
            int i = 0;

            while ((inputLine = in.readLine()) != null) {
                switch (i) {
                    case TYPE:
                        data[TYPE] = inputLine;
                        ++i;
                        break;
                    case HEADER:
                        data[HEADER] = inputLine;
                        ++i;
                        break;
                    case MESSAGE:
            /*            LOGGER.info("Id=" + id + " Message received: type = " + data[TYPE]
                                + " header = " + data[HEADER] + " body = \"" + inputLine + "\"\n");*/

                        Message msg = new Message(MsgTypes.valueOf(data[TYPE]),
                                MsgHeaders.valueOf(data[HEADER]), inputLine);
                        input.add(msg);
                        i = 0;
                        break;
                    default:
                        i = 0;
                        break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Id = " + id + " " + e.getMessage());
        }
    }

    private void sender() {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {
            while (clientSocket.isConnected()) {
                Message msg = output.take();
                out.println(msg.getType());
                out.println(msg.getHeader());
                out.println(msg.getMessage());
                out.flush();
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Id = " + id + " " + e.getMessage());
        }
    }

    @Override
    public void setType(ClientTypes clientType) {
        this.clientType = clientType;
    }

    @Override
    public ClientTypes getType() {
        return clientType;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void send(Message msg) {
        output.add(msg);
    }

    @Override
    public Message pool() {
        return input.poll();
    }

    @Override
    public boolean isClose() {
        return clientSocket.isClosed();
    }

    @Override
    public synchronized void dispose() {
        try {
            executor.shutdown();
            clientSocket.close();
        } catch (IOException e) {
            LOGGER.error("Id = " + id + " " + e.getMessage());
        }
    }
}
