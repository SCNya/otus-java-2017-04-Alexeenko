package com.otus.alexeenko.database.services;

import com.otus.alexeenko.database.services.custom.CustomService;
import com.otus.alexeenko.database.services.datasets.AddressDataSet;
import com.otus.alexeenko.database.services.datasets.BaseDataSet;
import com.otus.alexeenko.database.services.datasets.PhoneDataSet;
import com.otus.alexeenko.database.services.datasets.UserDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vsevolod on 01/08/2017.
 */
public class DB {
    private DataBaseService db;
    private List<BaseDataSet> dataSets;

    public DB() {
        db = new CustomService();
        dataSets = new ArrayList<>();

        List<PhoneDataSet> phones = Arrays.asList(new PhoneDataSet(1L, 911, "1"),
                new PhoneDataSet(2L, 921, "2"));

        dataSets.add(new UserDataSet(1L, "First", 22,
                phones, new AddressDataSet(1L, "Kings Row", 90)));

        dataSets.add(new UserDataSet(2L, "Second", 17,
                phones, new AddressDataSet(2L, "Dorado", 200)));
    }

    public void run() {
        try {
            db.start();
            for (BaseDataSet dataSet : dataSets)
                db.save(dataSet);

            while (true) {
                for (int i = 0; i < Integer.MAX_VALUE; ++i) {
                    if (i % 5 == 3)
                        db.load(2L, UserDataSet.class);
                    else
                        db.load(1L, UserDataSet.class);

                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            db.dispose();
        }
    }
}
