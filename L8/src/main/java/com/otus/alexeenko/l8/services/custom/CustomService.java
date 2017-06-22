package com.otus.alexeenko.l8.services.custom;

import com.otus.alexeenko.l8.services.DataBaseService;
import com.otus.alexeenko.l8.services.custom.dao.UserDAO;
import com.otus.alexeenko.l8.services.datasets.BaseDataSet;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Vsevolod on 11/06/2017.
 */
public class CustomService implements DataBaseService {
    private static final String DB_NAME = "otus";

    private final JdbcConnectionPool connections;

    public CustomService() {
        Check();

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost/mem:" + DB_NAME + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        startLocalServer();


        connections = JdbcConnectionPool.create(dataSource);
    }

    public CustomService(JdbcDataSource dataSource) {
        Check();

        connections = JdbcConnectionPool.create(dataSource);
    }

    /**
     * To stop a server close the application manually
     */
    private void startLocalServer() {
        try {
            Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Check() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Please install h2 database");
        }
    }

    @Override
    public <T extends BaseDataSet> void save(T dataSet) {
        try (Connection connection = connections.getConnection()) {
            connection.setAutoCommit(false);
            UserDAO dao = new UserDAO(connection);
            dao.save(dataSet);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends BaseDataSet> T load(long id, Class<T> clazz) {
        try (Connection connection = connections.getConnection()) {
            UserDAO dao = new UserDAO(connection);
            return dao.load(id, clazz);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}