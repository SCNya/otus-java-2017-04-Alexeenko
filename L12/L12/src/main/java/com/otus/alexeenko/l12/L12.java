package com.otus.alexeenko.l12;

import com.otus.alexeenko.l12.system.msg.MessageSystem;

/**
 * Created by Vsevolod on 01/08/2017.
 */
public class L12 {
    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        MessageSystem messageSystem = new MessageSystem();
        messageSystem.start();
    }
}
