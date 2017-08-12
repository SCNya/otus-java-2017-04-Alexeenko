package com.otus.alexeenko.l12.system.runner;

import java.io.IOException;

/**
 * Created by tully.
 */
public interface ProcessRunner {
    void start(boolean logging) throws IOException;

    void enableLogging();

    void disableLogging();

    void stop();
}
