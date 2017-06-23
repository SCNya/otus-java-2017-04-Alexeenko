package com.otus.alexeenko.l8.services.custom.creators.internal;

import com.otus.alexeenko.l8.services.custom.builders.internal.InsertBuilder;
import com.otus.alexeenko.l8.services.custom.builders.spi.InsertQueryBuilder;
import com.otus.alexeenko.l8.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.l8.services.custom.creators.spi.Creator;
import com.otus.alexeenko.l8.services.datasets.BaseDataSet;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.otus.alexeenko.l8.services.ReflectionHelper.*;

/**
 * Created by Vsevolod on 18/06/2017.
 */
public class InsertCreator implements Creator {
    private final Class<?> dataSetClazz;
    private final Object dataSet;
    private final List<QueryBuilder> queries;

    public InsertCreator(Object dataSet) {
        this.queries = new ArrayList<>();
        this.dataSetClazz = dataSet.getClass();
        this.dataSet = dataSet;
    }

    @Override
    public void createQueries() {
        queries.clear();
        InsertQueryBuilder InsertQuery = createInsertBuilder(dataSetClazz);
        queries.add(InsertQuery);
        buildInsert(InsertQuery, dataSet);
    }

    @Override
    public List<QueryBuilder> getQueries() {
        return queries;
    }

    private void buildInsert(InsertQueryBuilder query, Object currentDataSet) {
        Class<?> currentDataSetClazz = currentDataSet.getClass();

        if ((isEntity(currentDataSetClazz) && isTable(currentDataSetClazz)) || isMappedSuperclass(currentDataSetClazz)) {
            for (Field field : getFields(currentDataSetClazz)) {
                if (!isTransient(field)) {
                    if (isColumn(field) && !isID(field))
                        addValue(query, currentDataSet, field);
                    else {
                        if (isOneToMany(field)) {
                            if (isCollection(field.getType()))
                                buildOneToManyInsert(currentDataSet, field);
                        }
                        if (isOneToOne(field))
                            buildOneToOneInsert(query, currentDataSet, field);
                    }
                }
            }
        }
    }

    private void addValue(InsertQueryBuilder query, Object currentDataSet, Field field) {
        String columnName = getColumnName(field);
        Object value = getValue(currentDataSet, field);

        if (isNumber(value))
            query.addValue(columnName, String.valueOf(value));
        else
            query.addValue(columnName, "'" + String.valueOf(value) + "'");

    }

    private void buildOneToManyInsert(Object currentDataSet, Field field) {
        Object value = getValue(currentDataSet, field);

        if (value != null) {
            Object[] array = buildCollection(value);

            for (int i = 0; i < Array.getLength(array); ++i) {
                InsertQueryBuilder oneToManyInsert = createInsertBuilder(getGenericClass(field));
                queries.add(oneToManyInsert);

                oneToManyInsert.addValue(getJoinColumnName(field, getTableName(currentDataSet.getClass())),
                        getID(currentDataSet));
                buildInsert(oneToManyInsert, Array.get(array, i));
            }
        }
    }

    private void buildOneToOneInsert(InsertQueryBuilder query, Object currentDataSet, Field field) {
        Object value = getValue(currentDataSet, field);

        if (value != null) {
            String id = getID(currentDataSet);
            query.addValue(getJoinColumnName(field, field.getName()), id);

            InsertQueryBuilder oneToOneInsert = createInsertBuilder(field.getType());
            queries.add(oneToOneInsert);

            oneToOneInsert.addValue(getInverseJoinColumnName(field), id);

            buildInsert(oneToOneInsert, value);
        }
    }

    private String getID(Object currentDataSet) {
        return String.valueOf(((BaseDataSet) currentDataSet).getId());
    }

    private InsertQueryBuilder createInsertBuilder(Class<?> clazz) {
        String tableName = getTableName(clazz);
        return new InsertBuilder(tableName);
    }
}