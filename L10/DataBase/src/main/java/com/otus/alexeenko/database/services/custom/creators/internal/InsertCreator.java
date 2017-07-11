package com.otus.alexeenko.database.services.custom.creators.internal;

import com.otus.alexeenko.database.services.ReflectionHelper;
import com.otus.alexeenko.database.services.custom.builders.internal.InsertBuilder;
import com.otus.alexeenko.database.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.database.services.custom.creators.spi.Creator;
import com.otus.alexeenko.database.services.datasets.BaseDataSet;
import com.otus.alexeenko.database.services.custom.builders.spi.InsertQueryBuilder;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

        if ((ReflectionHelper.isEntity(currentDataSetClazz) && ReflectionHelper.isTable(currentDataSetClazz)) || ReflectionHelper.isMappedSuperclass(currentDataSetClazz)) {
            for (Field field : ReflectionHelper.getFields(currentDataSetClazz)) {
                if (!ReflectionHelper.isTransient(field)) {
                    if (ReflectionHelper.isColumn(field) && !ReflectionHelper.isID(field))
                        addValue(query, currentDataSet, field);
                    else {
                        if (ReflectionHelper.isOneToMany(field)) {
                            if (ReflectionHelper.isCollection(field.getType()))
                                buildOneToManyInsert(currentDataSet, field);
                        }
                        if (ReflectionHelper.isOneToOne(field))
                            buildOneToOneInsert(query, currentDataSet, field);
                    }
                }
            }
        }
    }

    private void addValue(InsertQueryBuilder query, Object currentDataSet, Field field) {
        String columnName = ReflectionHelper.getColumnName(field);
        Object value = ReflectionHelper.getValue(currentDataSet, field);

        if (ReflectionHelper.isNumber(value))
            query.addValue(columnName, String.valueOf(value));
        else
            query.addValue(columnName, "'" + String.valueOf(value) + "'");

    }

    private void buildOneToManyInsert(Object currentDataSet, Field field) {
        Object value = ReflectionHelper.getValue(currentDataSet, field);

        if (value != null) {
            Object[] array = ReflectionHelper.buildCollection(value);

            for (int i = 0; i < Array.getLength(array); ++i) {
                InsertQueryBuilder oneToManyInsert = createInsertBuilder(ReflectionHelper.getGenericClass(field));
                queries.add(oneToManyInsert);

                oneToManyInsert.addValue(ReflectionHelper.getJoinColumnName(field, ReflectionHelper.getTableName(currentDataSet.getClass())),
                        getID(currentDataSet));
                buildInsert(oneToManyInsert, Array.get(array, i));
            }
        }
    }

    private void buildOneToOneInsert(InsertQueryBuilder query, Object currentDataSet, Field field) {
        Object value = ReflectionHelper.getValue(currentDataSet, field);

        if (value != null) {
            String id = getID(currentDataSet);
            query.addValue(ReflectionHelper.getJoinColumnName(field, field.getName()), id);

            InsertQueryBuilder oneToOneInsert = createInsertBuilder(field.getType());
            queries.add(oneToOneInsert);

            oneToOneInsert.addValue(ReflectionHelper.getInverseJoinColumnName(field), id);

            buildInsert(oneToOneInsert, value);
        }
    }

    private String getID(Object currentDataSet) {
        return String.valueOf(((BaseDataSet) currentDataSet).getId());
    }

    private InsertQueryBuilder createInsertBuilder(Class<?> clazz) {
        String tableName = ReflectionHelper.getTableName(clazz);
        return new InsertBuilder(tableName);
    }
}