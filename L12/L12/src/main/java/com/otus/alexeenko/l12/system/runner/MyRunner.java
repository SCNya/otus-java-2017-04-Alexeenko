package com.otus.alexeenko.l12.system.runner;


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
    private StreamListener output;

    public MyRunner(String command) {
        pb = new ProcessBuilder(command.split(" "));
        pb.redirectErrorStream(true);
    }

    @Override
    public void start(boolean logging) throws IOException {
        process = pb.start();
        output = new StreamListener(process.getInputStream(), logging);

        output.start();
    }

    @Override
    public void enableLogging() {
        output.enableLogging();
    }

    @Override
    public void disableLogging() {
        output.disableLogging();
    }

    @Override
    public void stop() {
        process.destroy();
    }

    private class StreamListener extends Thread {

        private final InputStream is;
        private boolean logging;

        private StreamListener(InputStream is, boolean logging) {
            this.is = is;
            this.logging = logging;
        }

        private void enableLogging() {
            logging = true;
        }

        private void disableLogging() {
            logging = false;
        }

        @Override
        public void run() {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                BufferedReader br = new BufferedReader(isr);
                String line;

                while ((line = br.readLine()) != null)
                    if (logging)
                    LOGGER.info(line);

            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
