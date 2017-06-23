package com.otus.alexeenko.l8.services.custom;

import com.otus.alexeenko.l8.services.DataBaseService;
import com.otus.alexeenko.l8.services.datasets.AddressDataSet;
import com.otus.alexeenko.l8.services.datasets.PhoneDataSet;
import com.otus.alexeenko.l8.services.datasets.UserDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vsevolod on 23/06/2017.
 */
public class CustomServiceTest {
    private final long id = 1L;
    private final DataBaseService db = new CustomService();
    private final UserDataSet dataSet = new UserDataSet();

    @BeforeClass
    public static void turnOnLogger() {
        org.apache.log4j.BasicConfigurator.configure();
    }

    @Before
    public void beforeCustomServiceTest() {
        AddressDataSet address = new AddressDataSet(id, "Kings Row", 200);
        List<PhoneDataSet> phones = new ArrayList<>();
        phones.add(new PhoneDataSet(id, 911, "1"));
        phones.add(new PhoneDataSet(id + 1L, 921, "2"));

        dataSet.setId(id);
        dataSet.setName("First");
        dataSet.setAge(22);
        dataSet.setAddress(address);
        dataSet.setPhones(phones);
    }

    @Test
    public void saveAndLoad() {
        db.save(dataSet);

        UserDataSet userDataSet = db.load(id, UserDataSet.class);

        assertEquals(dataSet, userDataSet);
    }

    @After
    public void dispose() {
        db.dispose();
    }
}