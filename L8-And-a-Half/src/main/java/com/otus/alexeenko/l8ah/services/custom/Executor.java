package com.otus.alexeenko.l8ah.services.custom;

import com.otus.alexeenko.l8ah.services.custom.handler.ResultHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Vsevolod on 13/06/2017.
 */
public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String query) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> T execQuery(String query, ResultHandler<T> handler) {
        try (Statement stmt = connection.createStatement()) {
            try (ResultSet result = stmt.executeQuery(query)){
                return handler.handle(result);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
