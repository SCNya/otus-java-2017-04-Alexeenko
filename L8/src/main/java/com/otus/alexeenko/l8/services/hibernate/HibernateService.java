package com.otus.alexeenko.l8.services.hibernate;

import com.otus.alexeenko.l8.services.DataBaseService;
import com.otus.alexeenko.l8.services.datasets.BaseDataSet;

import java.math.BigInteger;

/**
 * Created by Vsevolod on 11/06/2017.
 */
public class HibernateService implements DataBaseService {
    @Override
    public <T extends BaseDataSet> void save(T dataSet) {

    }

    @Override
    public <T extends BaseDataSet> T load(BigInteger id, Class<T> clazz) {
        return null;
    }
}
