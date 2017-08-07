package com.otus.alexeenko.database.services.custom.builders.spi;

import com.otus.alexeenko.database.services.custom.builders.internal.SelectBuilder;

/**
 * Created by Vsevolod on 20/06/2017.
 */
public interface SelectQueryBuilder extends QueryBuilder {
    SelectBuilder addID(String columnName, long id);
}
