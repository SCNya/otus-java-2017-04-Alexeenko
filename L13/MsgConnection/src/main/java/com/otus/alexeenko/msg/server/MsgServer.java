package com.otus.alexeenko.msg.server;

import com.otus.alexeenko.msg.MsgConnection;
import com.otus.alexeenko.msg.MsgSystem;
import com.otus.alexeenko.msg.types.ClientTypes;

/**
 * Created by Vsevolod on 13/08/2017.
 */
public interface MsgServer extends MsgSystem {
    MsgConnection getConnection(ClientTypes type);
}
