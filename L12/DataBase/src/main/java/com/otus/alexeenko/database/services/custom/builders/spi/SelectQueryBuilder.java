package com.otus.alexeenko.database.services.custom.builders.spi;

/**
 * Created by Vsevolod on 20/06/2017.
 */
public interface SelectQueryBuilder extends QueryBuilder {
    SelectQueryBuilder addID(String columnName, long id);
}
