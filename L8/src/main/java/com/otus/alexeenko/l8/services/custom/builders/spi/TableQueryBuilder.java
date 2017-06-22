package com.otus.alexeenko.l8.services.custom.builders.spi;

/**
 * Created by Vsevolod on 14/06/2017.
 */
public interface TableQueryBuilder extends QueryBuilder {
    TableQueryBuilder addValue(String value);
    TableQueryBuilder addUniqueValue(String value);
    TableQueryBuilder addID(String value);
    TableQueryBuilder addReferenceOneToOne(String tableName, String columnName);
    TableQueryBuilder addReferenceOneToMay(String tableName, String joinColumn, String inverseJoinColumn);
}
