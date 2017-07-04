package com.otus.alexeenko.l9;

import com.otus.alexeenko.database.services.DataBaseService;
import com.otus.alexeenko.database.services.custom.CustomService;
import com.otus.alexeenko.database.services.datasets.AddressDataSet;
import com.otus.alexeenko.database.services.datasets.PhoneDataSet;
import com.otus.alexeenko.database.services.datasets.UserDataSet;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vsevolod on 25/06/2017.
 */
public class L9 {
    private static DataBaseService db;
    private static MyServer server;

    public static void main(String[] args) throws Exception {
        initLog();
        db = new CustomService();
        server = new MyServer();

        server.start();

        List<PhoneDataSet> phones = Arrays.asList(new PhoneDataSet(1L, 911, "1"),
                new PhoneDataSet(2L, 921, "2"));

        UserDataSet dataSet1 = new UserDataSet(1L, "First", 22,
                phones, new AddressDataSet(1L, "Kings Row", 90));

        UserDataSet dataSet2 = new UserDataSet(2L, "Second", 17,
                phones, new AddressDataSet(2L, "Dorado", 200));

        work(dataSet1, dataSet2);
    }

    private static void initLog() {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private static void work(UserDataSet dataSet1, UserDataSet dataSet2) throws Exception {
        try {
            db.save(dataSet1);

            db.save(dataSet2);

            for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                if (i % 5 == 3)
                    db.load(2L, UserDataSet.class);
                else
                    db.load(1L, UserDataSet.class);

                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dispose();
            server.stop();
        }
    }
}