package com.otus.alexeenko.l8ah.services.custom;

import com.otus.alexeenko.l8ah.services.DataBaseService;
import com.otus.alexeenko.l8ah.services.datasets.AddressDataSet;
import com.otus.alexeenko.l8ah.services.datasets.PhoneDataSet;
import com.otus.alexeenko.l8ah.services.datasets.UserDataSet;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vsevolod on 23/06/2017.
 */
public class CustomServiceTest {
    private final static long ID_1 = 1L;
    private final static long ID_2 = 2L;

    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    private final static DataBaseService db = new CustomService();
    private final static UserDataSet dataSet = new UserDataSet();
    private final static AddressDataSet address = new AddressDataSet();
    private final static List<PhoneDataSet> phones = new ArrayList<>();

    @BeforeClass
    public static void initDB() {
        address.setId(ID_1);
        address.setStreet("Kings Row");
        address.setIndex(90);

        phones.add(new PhoneDataSet(ID_1, 911, "1"));
        phones.add(new PhoneDataSet(ID_2, 921, "2"));

        dataSet.setId(ID_1);
        dataSet.setName("First");
        dataSet.setAge(22);
        dataSet.setAddress(address);
        dataSet.setPhones(phones);

        db.save(dataSet);
    }

    @Test
    public void Save1() {
        UserDataSet userDataSet2 = new UserDataSet(2L, "Second", 17,
                phones, new AddressDataSet(2L, "Dorado", 200));

        db.save(userDataSet2);
    }


    @Test
    public void Load1() {
        UserDataSet userDataSet = db.load(ID_1, UserDataSet.class);

        assertEquals(dataSet, userDataSet);
    }

    @Test
    public void Load2() {
        PhoneDataSet phoneDataSet1 = db.load(ID_1, PhoneDataSet.class);
        PhoneDataSet phoneDataSet2 = db.load(ID_2, PhoneDataSet.class);

        assertEquals(phoneDataSet1, phones.get((int) ID_1 - 1));
        assertEquals(phoneDataSet2, phones.get((int) ID_2 - 1));
    }

    @Test
    public void Load3() {
        AddressDataSet addressDataSet = db.load(ID_1, AddressDataSet.class);

        assertEquals(addressDataSet, address);
    }

    @AfterClass
    public static void dispose() {
        db.dispose();
    }
}