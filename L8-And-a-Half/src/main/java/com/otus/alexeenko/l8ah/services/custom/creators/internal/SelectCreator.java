package com.otus.alexeenko.l8ah.services.custom.creators.internal;

import com.otus.alexeenko.l8ah.services.custom.builders.internal.SelectBuilder;
import com.otus.alexeenko.l8ah.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.l8ah.services.custom.builders.spi.SelectQueryBuilder;
import com.otus.alexeenko.l8ah.services.custom.creators.spi.Creator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.otus.alexeenko.l8ah.services.ReflectionHelper.*;

/**
 * Created by Vsevolod on 20/06/2017.
 */
public class SelectCreator implements Creator {
    private final long id;
    private final Class<?> dataSetClazz;
    private final List<QueryBuilder> queries;

    public SelectCreator(long id, Class<?> dataSetClazz) {
        this.id = id;
        this.dataSetClazz = dataSetClazz;
        this.queries = new ArrayList<>();
    }

    @Override
    public List<QueryBuilder> getQueries() {
        return queries;
    }

    @Override
    public void createQueries() {
        queries.clear();
        SelectQueryBuilder createSelectQuery = createSelectBuilder(dataSetClazz);
        createSelectQuery.addID("id", id);
        queries.add(createSelectQuery);
        buildSelects(dataSetClazz);
    }

    private void buildSelects(Class<?> currentDataSetClazz) {
        if ((isEntity(currentDataSetClazz) && isTable(currentDataSetClazz)) || isMappedSuperclass(currentDataSetClazz)) {
            for (Field field : getFields(currentDataSetClazz)) {
                if (!isTransient(field)) {
                    if (isOneToMany(field)) {
                        if (isCollection(field.getType())) {
                            buildOneToManySelect(field);
                        }
                    }
                    if (isOneToOne(field)) {
                        buildOneToOneSelect(field);
                    }
                }
            }
        }
    }

    private void buildOneToOneSelect(Field field) {
        Class<?> valueClazz = field.getType();
        SelectQueryBuilder createSelectQuery = createSelectBuilder(field.getType());
        createSelectQuery.addID("id", id);
        queries.add(createSelectQuery);

        buildSelects(valueClazz);
    }

    private void buildOneToManySelect(Field field) {
        Class<?> valueGenericClass = getGenericClass(field);
        SelectQueryBuilder createSelectQuery = createSelectBuilder(valueGenericClass);
        createSelectQuery.addID(getJoinColumnName(field, getTableName(valueGenericClass)), id);
        queries.add(createSelectQuery);

        buildSelects(valueGenericClass);
    }

    private SelectQueryBuilder createSelectBuilder(Class<?> clazz) {
        String tableName = getTableName(clazz);
        return new SelectBuilder(tableName);
    }
}
