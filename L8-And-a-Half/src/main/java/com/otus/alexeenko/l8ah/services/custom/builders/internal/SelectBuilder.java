package com.otus.alexeenko.l8ah.services.custom.builders.internal;

import com.otus.alexeenko.l8ah.services.custom.builders.spi.SelectQueryBuilder;

/**
 * Created by Vsevolod on 20/06/2017.
 */
public class SelectBuilder implements SelectQueryBuilder {
    private static final String CREATE_SELECT = "select * from ";
    private static final String WHERE = " where ";
    private static final String EQUAL = "=";

    private final StringBuilder query;

    public SelectBuilder(SelectBuilder selectBuilder ) {
        this.query = new StringBuilder(selectBuilder.query);
    }

    public SelectBuilder(String tableName) {
        this.query = new StringBuilder();

        query.append(CREATE_SELECT);
        query.append(tableName);
    }

    @Override
    public SelectBuilder addID(String columnName, long id) {
        query.append(WHERE);
        query.append(columnName);
        query.append(EQUAL);
        query.append(String.valueOf(id));

        return new SelectBuilder(this);
    }

    @Override
    public String build() {
        return query.toString();
    }
}
