package com.otus.alexeenko.database.services;

import com.otus.alexeenko.database.services.custom.CustomService;
import com.otus.alexeenko.database.services.datasets.AddressDataSet;
import com.otus.alexeenko.database.services.datasets.BaseDataSet;
import com.otus.alexeenko.database.services.datasets.PhoneDataSet;
import com.otus.alexeenko.database.services.datasets.UserDataSet;
import com.otus.alexeenko.msg.MsgConnection;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 01/08/2017.
 */
public class DB {
    private static final Logger LOGGER = getLogger("DB");

    private final ExecutorService executor;
    private final DataBaseService db;
    private final List<BaseDataSet> dataSets;

    public DB(MsgConnection msgServer) {
        executor = Executors.newSingleThreadExecutor();
        db = new CustomService(msgServer);
        dataSets = new ArrayList<>();

        List<PhoneDataSet> phones = Arrays.asList(new PhoneDataSet(1L, 911, "1"),
                new PhoneDataSet(2L, 921, "2"));

        dataSets.add(new UserDataSet(1L, "First", 22,
                phones, new AddressDataSet(1L, "Kings Row", 90)));

        dataSets.add(new UserDataSet(2L, "Second", 17,
                phones, new AddressDataSet(2L, "Dorado", 200)));
    }

    public void start() {
        executor.execute(this::work);
    }

    private void work() {
        try {
            db.start();
            for (BaseDataSet dataSet : dataSets)
                db.save(dataSet);

            while (!executor.isShutdown()) {
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    if (i % 5 == 3)
                        db.load(2L, UserDataSet.class);
                    else
                        db.load(1L, UserDataSet.class);

                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            LOGGER.info("close");
        }
    }

    public synchronized void dispose()  {
        executor.shutdownNow();
        db.dispose();
    }
}
