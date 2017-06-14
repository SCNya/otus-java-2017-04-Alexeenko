package com.otus.alexeenko.l8.services.custom.dao;

import com.otus.alexeenko.l8.services.custom.Executor;
import com.otus.alexeenko.l8.services.datasets.AddressDataSet;
import com.otus.alexeenko.l8.services.datasets.BaseDataSet;
import com.otus.alexeenko.l8.services.datasets.PhoneDataSet;
import com.otus.alexeenko.l8.services.datasets.UserDataSet;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsevolod on 13/06/2017.
 */
public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public <T extends BaseDataSet> void save(T dataSet) {
        Executor exec = new Executor(connection);
        UserDataSet userDataSet = (UserDataSet) dataSet;

        exec.execUpdate("create table if not exists users (id bigint(20) auto_increment primary key," +
                " name varchar(255), age int(3) default 0)");
        exec.execUpdate("create table if not exists phones (id bigint(20) auto_increment primary key," +
                " user_id bigint(20), foreign key(user_id) references users(id), code int(3), number varchar(255))");
        exec.execUpdate("create table if not exists address (id bigint(20) auto_increment primary key," +
                " user_id bigint(20) unique, foreign key(user_id) references users(id), street varchar(255), index int(6))");

        exec.execUpdate("insert into users (name,age) values('" + userDataSet.getName() + "'," + userDataSet.getAge() + ")");

        for (PhoneDataSet phone : userDataSet.getPhones()) {
            exec.execUpdate("insert into phones (user_id,code,number) values(" + dataSet.getId() + "," +
                    phone.getCode() + ",'" + phone.getNumber() + "')");
        }

        exec.execUpdate("insert into address (user_id,street,index) values(" + dataSet.getId() +
                ",'" + userDataSet.getAddress().getStreet() + "'," + userDataSet.getAddress().getIndex() + ")");

    }

    public <T extends BaseDataSet> T load(BigInteger id, Class<T> clazz) {
        Executor exec = new Executor(connection);

        List<PhoneDataSet> phones = exec.execQuery("select * from phones where user_id=" + id, resultSet -> {
            List<PhoneDataSet> phoneDataSets = new ArrayList<PhoneDataSet>();

            while (resultSet.next()) {
                phoneDataSets.add(new PhoneDataSet(resultSet.getInt("code"),
                        resultSet.getString("number")));
            }
            return phoneDataSets;
        });

        AddressDataSet address = exec.execQuery("select * from address where user_id=" + id, resultSet -> {
                    resultSet.next();
                    return new AddressDataSet(resultSet.getString("street"),
                            resultSet.getInt("index"));
                }
        );

        return clazz.cast(exec.execQuery("select * from users where id=" + id, resultSet -> {
            resultSet.next();
            return new UserDataSet(id,
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    phones,
                    address);
        }));
    }
}