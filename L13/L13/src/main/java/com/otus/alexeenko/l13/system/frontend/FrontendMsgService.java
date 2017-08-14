package com.otus.alexeenko.l13.system.frontend;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.MsgSystem;

/**
 * Created by Vsevolod on 09/08/2017.
 */
public interface FrontendMsgService extends MsgSystem {
    void setMsgServer(MsgConnection msgServer);
    String getStatistics();
    String getManagementInfo();
    void setConfiguration(String data);
}
