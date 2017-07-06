package com.otus.alexeenko.l8ah;

import com.otus.alexeenko.l8ah.services.DataBaseService;
import com.otus.alexeenko.l8ah.services.custom.CustomService;
import com.otus.alexeenko.l8ah.services.datasets.AddressDataSet;
import com.otus.alexeenko.l8ah.services.datasets.PhoneDataSet;
import com.otus.alexeenko.l8ah.services.datasets.UserDataSet;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vsevolod on 05/07/2017.
 */
public class L8AH {
    public static void main(String[] args) {
        org.apache.log4j.BasicConfigurator.configure();

        List<PhoneDataSet> phones = Arrays.asList(new PhoneDataSet(1L, 911, "1"),
                new PhoneDataSet(2L, 921, "2"));

        UserDataSet dataSet1 = new UserDataSet(1L, "First", 22,
                phones, new AddressDataSet(1L, "Kings Row", 90));

        UserDataSet dataSet2 = new UserDataSet(2L, "Second", 17,
                phones, new AddressDataSet(2L, "Dorado", 200));

        DataBaseService db = new CustomService();

        try {
            db.save(dataSet1);

            db.save(dataSet2);

            for (int i = 0; i < 10; ++i) {
                if (i % 5 == 3)
                    db.load(2L, UserDataSet.class);
                else
                    db.load(1L, UserDataSet.class);
                Thread.sleep(2000);
            }

            UserDataSet result = db.load(1L, UserDataSet.class);

            System.out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.dispose();
        }
    }
}
