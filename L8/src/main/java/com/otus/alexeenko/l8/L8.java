package com.otus.alexeenko.l8;

import com.otus.alexeenko.l8.services.DataBaseService;
import com.otus.alexeenko.l8.services.custom.CustomService;
import com.otus.alexeenko.l8.services.datasets.AddressDataSet;
import com.otus.alexeenko.l8.services.datasets.PhoneDataSet;
import com.otus.alexeenko.l8.services.datasets.UserDataSet;

import javax.naming.NamingException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vsevolod on 09/06/2017.
 */
public class L8 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, NamingException {

        List<PhoneDataSet> phones = Arrays.asList(new PhoneDataSet(911, "1"), new PhoneDataSet(921, "2"));

        DataBaseService db = new CustomService();

        UserDataSet dataSet = new UserDataSet(BigInteger.ONE, "First", 22,
                phones, new AddressDataSet("Sandyway", 200));

        db.save(dataSet);

        UserDataSet result = db.load(BigInteger.ONE, UserDataSet.class);

        System.out.println(result.toString());
    }
}
