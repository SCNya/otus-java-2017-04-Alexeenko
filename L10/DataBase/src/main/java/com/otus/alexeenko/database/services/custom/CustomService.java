package com.otus.alexeenko.database.services.custom;

import com.otus.alexeenko.database.services.DataBaseService;
import com.otus.alexeenko.database.services.custom.cache.MyCache;
import com.otus.alexeenko.database.services.custom.dao.UserDAO;
import com.otus.alexeenko.database.services.datasets.BaseDataSet;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
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
            return Objects.hash(clazz, id);
        }
    }

    private static final String DB_NAME = "otus";
    private static final String CACHE_NAME = "userCache";
    private static final Logger CACHE_LOGGER = getLogger("Cache [" + ManagementFactory.getRuntimeMXBean().getName() + ']');

    private final ApplicationContext context;
    private final Server server;
    private final MyCache myCache;
    private final Cache cache;
    private final JdbcConnectionPool connections;

    public CustomService() {
        check();
        context = new ClassPathXmlApplicationContext("DataBaseBeans.xml");
        myCache = ((MyCache) context.getBean("cache"));
        myCache.createCache(CACHE_NAME);
        myCache.registerMBeans();
        cache = myCache.getCache(CACHE_NAME);
        server = getLocalServer();
        connections = JdbcConnectionPool.create(getDefaultDataSource());
    }

    //For connect to own exist DataBase
    public CustomService(JdbcDataSource dataSource) {
        check();
        server = null;
        context = new ClassPathXmlApplicationContext("DataBaseBeans.xml");
        myCache = ((MyCache) context.getBean("cache"));
        myCache.createCache(CACHE_NAME);
        myCache.registerMBeans();
        cache = myCache.getCache(CACHE_NAME);
        connections = JdbcConnectionPool.create(dataSource);
    }

    private JdbcDataSource getDefaultDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:tcp://localhost/mem:" + DB_NAME + ";DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    public void start() {
        try {
            if (server != null)
                server.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Server getLocalServer() {
        Server TcpServer = null;

        try {
            TcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
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
        T value;
        Key key = new Key(id, clazz);
        Element element = cache.get(key);

        if (element == null) {
            CACHE_LOGGER.info("Cache miss!");
            try (Connection connection = connections.getConnection()) {
                UserDAO dao = new UserDAO(connection);
                value = dao.load(id, clazz);
                cache.put(new Element(key, value));

                return value;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            CACHE_LOGGER.info("Cache hit!");
            return (T) element.getObjectValue();
        }
    }

    @Override
    public synchronized void dispose() {
        connections.dispose();

        if (server != null) {
            server.stop();
        }
    }
}
