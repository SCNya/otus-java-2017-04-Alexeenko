package com.otus.alexeenko.l8.services.custom;

import com.otus.alexeenko.l8.services.DataBaseService;
import com.otus.alexeenko.l8.services.custom.dao.UserDAO;
import com.otus.alexeenko.l8.services.datasets.BaseDataSet;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 11/06/2017.
 */
public class CustomService implements DataBaseService {
    private static final String DB_NAME = "otus";
    private static final Logger CACHE_LOGGER = getLogger("Cache");

    private final Server server;
    private final JdbcConnectionPool connections;
    private final CacheManager cacheManager;
    private final Cache<Key, Object> cache;

    public CustomService() {
        check();

        cacheManager = getCacheManager();

        cache = cacheManager.getCache("userCache", Key.class, Object.class);

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

        cacheManager = getCacheManager();

        cache = cacheManager.getCache("userCache", Key.class, Object.class);

        connections = JdbcConnectionPool.create(dataSource);
    }

    private CacheManager getCacheManager() {
        return CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("userCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Key.class, Object.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .heap(1, MemoryUnit.MB))
                                .withSizeOfMaxObjectGraph(100)
                                .withExpiry(Expirations.timeToLiveExpiration(new Duration(10, TimeUnit.SECONDS))))
                .build(true);
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

        cacheManager.close();
    }
}