package com.otus.alexeenko.l12.system;

import java.io.IOException;

/**
 * Created by tully.
 */
public interface ProcessRunner {
    void start() throws IOException;

    void enableLogging();

    void stop();
}
