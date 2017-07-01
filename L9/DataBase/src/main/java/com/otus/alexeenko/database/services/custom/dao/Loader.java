package com.otus.alexeenko.database.services.custom.dao;

import com.otus.alexeenko.database.services.custom.Executor;
import com.otus.alexeenko.database.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.database.services.datasets.BaseDataSet;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.otus.alexeenko.database.services.ReflectionHelper.*;

/**
 * Created by Vsevolod on 21/06/2017.
 */
public class Loader {
    private final Executor exec;
    private final Iterator<QueryBuilder> it;

    public Loader(Executor exec, Iterator<QueryBuilder> it) {
        this.exec = exec;
        this.it = it;
    }

    public <T extends BaseDataSet> T getDataSet(Class<T> clazz) {
        return exec.execQuery(it.next().build(), resultSet -> {
            resultSet.next();
            return createDataSet(clazz, resultSet);
        });
    }

    private <T extends BaseDataSet> T createDataSet(Class<T> clazz, ResultSet resultSet) throws SQLException {
        T dataSetObject = instantiate(clazz);

        if ((isEntity(clazz) && isTable(clazz)) || isMappedSuperclass(clazz)) {
            for (Field field : getFields(clazz)) {
                if (!isTransient(field)) {
                    if (isColumn(field))
                        create(resultSet, dataSetObject, field);
                    else {
                        if (isOneToMany(field))
                            if (isCollection(field.getType())) {
                                createOneToMany(dataSetObject, field);
                            }
                        if (isOneToOne(field))
                            createOneToOne(dataSetObject, field);
                    }
                }
            }
        }
        return dataSetObject;
    }

    private <T extends BaseDataSet> void create(ResultSet resultSet, T dataSetObject, Field field) throws SQLException {
        Object value = resultSet.getObject(getColumnName(field));

        setFieldValue(dataSetObject, field, value);
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseDataSet> void createOneToOne(T dataSetObject, Field field) {
        Object oneToOneValue = getDataSet((Class<? extends BaseDataSet>) field.getType());

        setFieldValue(dataSetObject, field, oneToOneValue);
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseDataSet> void createOneToMany(T dataSetObject, Field field) {
        List<Object> oneToManyValues = exec.execQuery(it.next().build(), oneToManyResultSet -> {
            List<Object> values = new ArrayList<>();

            while (oneToManyResultSet.next()) {
                values.add(createDataSet((Class<? extends BaseDataSet>) getGenericClass(field),
                        oneToManyResultSet));
            }
            return values;
        });

        setFieldValue(dataSetObject, field, oneToManyValues);
    }
}