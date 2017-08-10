package com.otus.alexeenko.frontend.net;

import com.otus.alexeenko.msg.MsgNetSystem;

/**
 * Created by Vsevolod on 09/08/2017.
 */
public interface FrontendNetService extends MsgNetSystem {
    int ATTEMPTS_TO_OBTAIN = 5;

    String getStatistics();
    String getManagementInfo();
    void setConfiguration(String data);
}
