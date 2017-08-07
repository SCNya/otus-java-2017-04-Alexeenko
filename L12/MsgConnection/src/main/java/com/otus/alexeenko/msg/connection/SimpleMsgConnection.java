package com.otus.alexeenko.msg.connection;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.types.ClientTypes;
import com.otus.alexeenko.msg.types.Message;
import com.otus.alexeenko.msg.types.MsgTypes;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 04/08/2017.
 */
public class SimpleMsgConnection implements MsgConnection {
    private static final Logger LOGGER = getLogger("SimpleMsgConnection");
    private static final int WORK_THREADS = 2;

    private final int id;
    private final Socket clientSocket;
    private final ExecutorService executor;
    private final BlockingQueue<Message> output;
    private final BlockingQueue<Message> input;

    private ClientTypes type;

    public SimpleMsgConnection(int id, Socket client) {
        this.id = id;
        this.clientSocket = client;
        this.executor = Executors.newFixedThreadPool(WORK_THREADS);
        this.output = new ArrayBlockingQueue<>(1);
        this.input = new ArrayBlockingQueue<>(1);
    }

    @Override
    public void start() {
        executor.execute(this::receiver);
        executor.execute(this::sender);
    }

    private void receiver() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String inputLine;
            String type = "";
            while ((inputLine = in.readLine()) != null) {
                if (type.isEmpty()) {
                    type = inputLine;
                } else {
                    LOGGER.info("Id=" + id + " Message received: type = " + type + " " + inputLine);
                    Message msg = new Message(MsgTypes.valueOf(type), inputLine);
                    input.offer(msg);
                    type = "";
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
                out.println(msg.toString());
                out.flush();
            }
        } catch (InterruptedException | IOException e) {
            LOGGER.error("Id = " + id + " " + e.getMessage());
        }
    }

    @Override
    public void setType(ClientTypes type) {
        this.type = type;
    }

    @Override
    public ClientTypes getType() {
        return type;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void send(Message msg) {
        output.offer(msg);
    }

    @Override
    public Message pool() {
        return input.poll();
    }

    @Override
    public Message take() throws InterruptedException {
        return input.take();
    }

    @Override
    public boolean isClose() {
        return clientSocket.isClosed();
    }

    @Override
    public synchronized void dispose() {
        try {
            clientSocket.close();
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
