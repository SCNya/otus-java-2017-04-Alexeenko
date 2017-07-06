package com.otus.alexeenko.l8ah.services.custom.builders.spi;

import com.otus.alexeenko.l8ah.services.custom.builders.internal.SelectBuilder;

/**
 * Created by Vsevolod on 20/06/2017.
 */
public interface SelectQueryBuilder extends QueryBuilder {
    SelectBuilder addID(String columnName, long id);
}
