package com.otus.alexeenko.l8ah.services.custom.creators.internal;

import com.otus.alexeenko.l8ah.services.custom.builders.internal.TableBuilder;
import com.otus.alexeenko.l8ah.services.custom.builders.spi.QueryBuilder;
import com.otus.alexeenko.l8ah.services.custom.builders.spi.TableQueryBuilder;
import com.otus.alexeenko.l8ah.services.custom.creators.spi.Creator;

import java.lang.reflect.Field;
import java.util.*;

import static com.otus.alexeenko.l8ah.services.ReflectionHelper.*;

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
        if ((isEntity(currentDataSetClazz) && isTable(currentDataSetClazz)) || isMappedSuperclass(currentDataSetClazz)) {
            for (Field field : getFields(currentDataSetClazz)) {
                if (!isTransient(field)) {
                    if (isColumn(field))
                        addValue(query, field);
                    else {
                        if (isOneToMany(field)) {
                            if (isCollection(field.getType()))
                                buildOneToManyTable(currentDataSetClazz, field);
                        }
                        if (isOneToOne(field))
                            buildOneToOneTable(query, currentDataSetClazz, field);
                    }
                }
            }
        }
    }

    private void addValue(TableQueryBuilder query, Field field) {
        String columnName = getColumnName(field);
        String type = getType(field);

        if (isID(field))
            query.addID(columnName);
        else
            query.addValue(columnName + type);
    }

    private void buildOneToManyTable(Class<?> currentDataSetClazz, Field field) {
        Class<?> valueGenericClass = getGenericClass(field);
        String tableName = getTableName(currentDataSetClazz);
        String joinColumnName = getJoinColumnName(field, tableName);
        String inverseJoinColumnName = getInverseJoinColumnName(field);
        TableQueryBuilder oneToManyTable = createTableBuilder(valueGenericClass);
        queries.add(oneToManyTable);

        buildTables(oneToManyTable, valueGenericClass);
        oneToManyTable.addValue(joinColumnName + getLongType());
        oneToManyTable.addReferenceOneToMay(tableName, joinColumnName, inverseJoinColumnName);
    }

    private void buildOneToOneTable(TableQueryBuilder query, Class<?> currentDataSetClazz, Field field) {
        String columnName = getJoinColumnName(field, field.getName());
        Class<?> valueClazz = field.getType();
        TableQueryBuilder oneToOneTable = createTableBuilder(valueClazz);
        queries.add(oneToOneTable);

        query.addUniqueValue(columnName + getLongType());
        buildTables(oneToOneTable, valueClazz);
        oneToOneTable.addReferenceOneToOne(getTableName(currentDataSetClazz), columnName);
    }

    private TableQueryBuilder createTableBuilder(Class<?> clazz) {
        String tableName = getTableName(clazz);
        return new TableBuilder(tableName);
    }

    private String getType(Field field) {
        return " " + adapters.get(field.getType());
    }

    private String getLongType() {
        return " " + adapters.get(Long.class);
    }
}