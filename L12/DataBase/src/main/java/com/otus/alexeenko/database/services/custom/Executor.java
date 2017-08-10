package com.otus.alexeenko.database.services.custom;

import com.otus.alexeenko.database.services.custom.handler.ResultHandler;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 13/06/2017.
 */
public class Executor {
    private static final Logger LOGGER = getLogger("Executor");

    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String query) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet result = stmt.executeQuery(query)) {
                return handler.handle(result);
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }

        return null;
    }
}
