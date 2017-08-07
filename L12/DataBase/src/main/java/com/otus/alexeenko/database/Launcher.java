package com.otus.alexeenko.database;

import com.otus.alexeenko.database.services.DB;

/**
 * Created by Vsevolod on 02/08/2017.
 */
public class Launcher {
    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    public static void main(String[] args) {
        DB db = new DB();
        db.run();
    }
}
