package com.otus.alexeenko.l12.system.runner;


import com.otus.alexeenko.l12.system.ProcessRunner;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by tully.
 */
public class MyRunner implements ProcessRunner {
    private static final Logger LOGGER = getLogger("StreamListener");

    private final ProcessBuilder pb;
    private Process process;

    public MyRunner(String command) {
        pb = new ProcessBuilder(command.split(" "));
    }

    @Override
    public void start() throws IOException {
        process = pb.start();
    }

    @Override
    public void enableLogging() {
        StreamListener output = new StreamListener(process.getInputStream());

        output.start();
    }

    @Override
    public void stop() {
        process.destroy();
    }

    private class StreamListener extends Thread {

        private final InputStream is;

        private StreamListener(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;

                while ((line = br.readLine()) != null)
                    LOGGER.info(line);

            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
