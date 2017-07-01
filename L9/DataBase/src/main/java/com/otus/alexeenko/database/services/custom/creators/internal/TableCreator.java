package com.otus.alexeenko.database.services.custom.creators.internal;

import com.otus.alexeenko.database.services.ReflectionHelper;
import com.otus.alexeenko.database.services.custom.builders.internal.TableBuilder;
import com.otus.alexeenko.database.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.database.services.custom.builders.spi.TableQueryBuilder;
import com.otus.alexeenko.database.services.custom.creators.spi.Creator;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Vsevolod on 18/06/2017.
 */
public class TableCreator implements Creator {

    private static final Map<Class<?>, String> adapters;

    static {
        Map<Class<?>, String> typeAdaptersMap = new HashMap<>();
        typeAdaptersMap.put(Integer.class, "int(3)");
        typeAdaptersMap.put(int.class, "int(3)");
        typeAdaptersMap.put(long.class, "bigint(20)");
        typeAdaptersMap.put(Long.class, "bigint(20)");
        typeAdaptersMap.put(String.class, "varchar(255)");

        adapters = Collections.unmodifiableMap(typeAdaptersMap);
    }

    private final List<QueryBuilder> queries;
    private final Class<?> dataSetClazz;

    public TableCreator(Class<?> currentDataSetClazz) {
        this.queries = new ArrayList<>();
        this.dataSetClazz = currentDataSetClazz;
    }

    @Override
    public void createQueries() {
        queries.clear();
        TableQueryBuilder createTableQuery = createTableBuilder(dataSetClazz);
        queries.add(createTableQuery);
        buildTables(createTableQuery, dataSetClazz);
    }

    @Override
    public List<QueryBuilder> getQueries() {
        return queries;
    }

    private void buildTables(TableQueryBuilder query, Class<?> currentDataSetClazz) {
        if ((ReflectionHelper.isEntity(currentDataSetClazz) && ReflectionHelper.isTable(currentDataSetClazz)) || ReflectionHelper.isMappedSuperclass(currentDataSetClazz)) {
            for (Field field : ReflectionHelper.getFields(currentDataSetClazz)) {
                if (!ReflectionHelper.isTransient(field)) {
                    if (ReflectionHelper.isColumn(field))
                        addValue(query, field);
                    else {
                        if (ReflectionHelper.isOneToMany(field)) {
                            if (ReflectionHelper.isCollection(field.getType()))
                                buildOneToManyTable(currentDataSetClazz, field);
                        }
                        if (ReflectionHelper.isOneToOne(field))
                            buildOneToOneTable(query, currentDataSetClazz, field);
                    }
                }
            }
        }
    }

    private void addValue(TableQueryBuilder query, Field field) {
        String columnName = ReflectionHelper.getColumnName(field);
        String type = getType(field);

        if (ReflectionHelper.isID(field))
            query.addID(columnName);
        else
            query.addValue(columnName + type);
    }

    private void buildOneToManyTable(Class<?> currentDataSetClazz, Field field) {
        Class<?> valueGenericClass = ReflectionHelper.getGenericClass(field);
        String tableName = ReflectionHelper.getTableName(currentDataSetClazz);
        String joinColumnName = ReflectionHelper.getJoinColumnName(field, tableName);
        String inverseJoinColumnName = ReflectionHelper.getInverseJoinColumnName(field);
        TableQueryBuilder oneToManyTable = createTableBuilder(valueGenericClass);
        queries.add(oneToManyTable);

        buildTables(oneToManyTable, valueGenericClass);
        oneToManyTable.addValue(joinColumnName + getLongType());
        oneToManyTable.addReferenceOneToMay(tableName, joinColumnName, inverseJoinColumnName);
    }

    private void buildOneToOneTable(TableQueryBuilder query, Class<?> currentDataSetClazz, Field field) {
        String columnName = ReflectionHelper.getJoinColumnName(field, field.getName());
        Class<?> valueClazz = field.getType();
        TableQueryBuilder oneToOneTable = createTableBuilder(valueClazz);
        queries.add(oneToOneTable);

        query.addUniqueValue(columnName + getLongType());
        buildTables(oneToOneTable, valueClazz);
        oneToOneTable.addReferenceOneToOne(ReflectionHelper.getTableName(currentDataSetClazz), columnName);
    }

    private TableQueryBuilder createTableBuilder(Class<?> clazz) {
        String tableName = ReflectionHelper.getTableName(clazz);
        return new TableBuilder(tableName);
    }

    private String getType(Field field) {
        return " " + adapters.get(field.getType());
    }

    private String getLongType() {
        return " " + adapters.get(Long.class);
    }
}