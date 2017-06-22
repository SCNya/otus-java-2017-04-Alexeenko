package com.otus.alexeenko.l8.services.custom.dao;

import com.otus.alexeenko.l8.services.custom.Executor;
import com.otus.alexeenko.l8.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.l8.services.custom.creators.internal.InsertCreator;
import com.otus.alexeenko.l8.services.custom.creators.internal.SelectCreator;
import com.otus.alexeenko.l8.services.custom.creators.internal.TableCreator;
import com.otus.alexeenko.l8.services.custom.creators.spi.Creator;
import com.otus.alexeenko.l8.services.datasets.BaseDataSet;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsevolod on 13/06/2017.
 */
public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public <T extends BaseDataSet> void save(T dataSet) {
        Executor exec = new Executor(connection);
        List<QueryBuilder> UpdateQueries = new ArrayList<>();
        Creator tableCreator = new TableCreator(dataSet.getClass());
        Creator insertCreator = new InsertCreator(dataSet);

        tableCreator.createQueries();
        insertCreator.createQueries();

        UpdateQueries.addAll(tableCreator.getQueries());
        UpdateQueries.addAll(insertCreator.getQueries());

        for (QueryBuilder query : UpdateQueries)
            exec.execUpdate(query.build());
    }

    public <T extends BaseDataSet> T load(long id, Class<T> clazz) {
        Creator selectCreator = new SelectCreator(id, clazz);
        selectCreator.createQueries();
        List<QueryBuilder> executeQueries = selectCreator.getQueries();

        Loader loader = new Loader(new Executor(connection), executeQueries.iterator());

        return loader.getDataSet(clazz);
    }
}