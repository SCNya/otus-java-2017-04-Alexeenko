package com.otus.alexeenko.l8.services.custom.builders.spi;

/**
 * Created by Vsevolod on 17/06/2017.
 */
public interface InsertQueryBuilder extends QueryBuilder {
    InsertQueryBuilder addValue(String name, String value);
}
