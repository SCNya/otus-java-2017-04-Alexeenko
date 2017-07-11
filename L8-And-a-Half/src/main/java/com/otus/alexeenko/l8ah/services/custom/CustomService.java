package com.otus.alexeenko.l8ah.services.custom;

import com.otus.alexeenko.l8ah.services.DataBaseService;
import com.otus.alexeenko.l8ah.services.custom.cache.Cache;
import com.otus.alexeenko.l8ah.services.custom.cache.CacheEngine;
import com.otus.alexeenko.l8ah.services.custom.cache.MemoryStoreEvictionPolicy;
import com.otus.alexeenko.l8ah.services.custom.dao.UserDAO;
import com.otus.alexeenko.l8ah.services.datasets.BaseDataSet;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 11/06/2017.
 */
public class CustomService implements DataBaseService {
    private static class Key {
        private final long id;
        private final Class clazz;

        public Key(long id, Class clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return id == key.id &&
                    Objects.equals(clazz, key.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, clazz);
        }
    }

    private static final String DB_NAME = "otus";
    private static final Logger CACHE_LOGGER = getLogger("Cache");

    private final Server server;
    private final JdbcConnectionPool connections;
    private final Cache<Key, Object> cache;

    public CustomService() {
        check();

        cache = new CacheEngine<>("userCache", MemoryStoreEvictionPolicy.FIFO, Duration.ofSeconds(10), 100);

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost/mem:" + DB_NAME + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        server = startLocalServer();
        connections = JdbcConnectionPool.create(dataSource);
    }

    //For connect to own exist DataBase
    public CustomService(JdbcDataSource dataSource) {
        check();

        server = null;
        cache = new CacheEngine<>("userCache", MemoryStoreEvictionPolicy.FIFO, Duration.ofSeconds(10), 100);

        connections = JdbcConnectionPool.create(dataSource);
    }

    private Server startLocalServer() {
        Server TcpServer = null;

        try {
            TcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return TcpServer;
    }

    private void check() {
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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseDataSet> T load(long id, Class<T> clazz) {
        Key key = new Key(id, clazz);
        T value = (T) cache.get(key);

        if (value == null) {
            CACHE_LOGGER.info("Cache miss!");
            try (Connection connection = connections.getConnection()) {
                UserDAO dao = new UserDAO(connection);
                value = dao.load(id, clazz);
                cache.put(key, value);

                return value;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            CACHE_LOGGER.info("Cache hit!");
            return value;
        }
    }

    @Override
    public void dispose() {
        connections.dispose();

        if (server != null) {
            server.stop();
        }

        cache.dispose();
    }
}