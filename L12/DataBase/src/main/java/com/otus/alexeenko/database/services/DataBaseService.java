package com.otus.alexeenko.database.services;

import com.otus.alexeenko.database.services.datasets.BaseDataSet;

/**
 * Created by Vsevolod on 11/06/2017.
 */
public interface DataBaseService {
    void start();

    <T extends BaseDataSet> void save(T dataSet);

    <T extends BaseDataSet> T load(long id, Class<T> clazz);

    void dispose();
}
