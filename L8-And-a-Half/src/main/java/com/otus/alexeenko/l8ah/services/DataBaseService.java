package com.otus.alexeenko.l8ah.services;

import com.otus.alexeenko.l8ah.services.datasets.BaseDataSet;

/**
 * Created by Vsevolod on 11/06/2017.
 */
public interface DataBaseService {
    <T extends BaseDataSet> void save(T dataSet);
    <T extends BaseDataSet> T load(long id, Class<T> clazz);
    void dispose();
}
