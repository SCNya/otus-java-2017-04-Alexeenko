package com.otus.alexeenko.database.services.custom.builders.internal;

import com.otus.alexeenko.database.services.custom.builders.spi.TableQueryBuilder;

/**
 * Created by Vsevolod on 14/06/2017.
 */
public class TableBuilder implements TableQueryBuilder {
    private static final String CREATE_TABLE = "create table if not exists ";
    private static final String ID = " bigint(20) auto_increment primary key";
    private static final String UNIQUE = " unique";
    private static final String FOREIGN_KEY = "foreign key";
    private static final String REFERENCE = " references ";

    private final StringBuilder query;

    public TableBuilder(TableBuilder queryBuilder) {
        query = new StringBuilder(queryBuilder.query);
    }

    public TableBuilder(String tableName) {
        query = new StringBuilder();
        query.append(CREATE_TABLE);
        query.append(tableName);
        query.append(" (");
    }

    @Override
    public TableQueryBuilder addValue(String value) {
        query.append(", ");
        query.append(value);

        return new TableBuilder(this);
    }

    @Override
    public TableQueryBuilder addUniqueValue(String value) {
        query.append(", ");
        query.append(value);
        query.append(UNIQUE);

        return new TableBuilder(this);
    }

    @Override
    public TableQueryBuilder addID(String value) {
        query.append(value);
        query.append(ID);

        return new TableBuilder(this);
    }

    @Override
    public TableQueryBuilder addReferenceOneToOne(String tableName, String columnName) {
        query.append(", ");
        query.append(FOREIGN_KEY);
        appendKey("id");
        query.append(REFERENCE);
        query.append(tableName);
        appendKey(columnName);

        return new TableBuilder(this);
    }

    private void appendKey(String id) {
        query.append('(');
        query.append(id);
        query.append(')');
    }

    @Override
    public TableQueryBuilder addReferenceOneToMay(String tableName, String joinColumn, String inverseJoinColumn) {
        query.append(", ");
        query.append(FOREIGN_KEY);
        appendKey(joinColumn);
        query.append(REFERENCE);
        query.append(tableName);
        appendKey(inverseJoinColumn);

        return new TableBuilder(this);
    }

    @Override
    public String build() {
        return query.toString() + ')';
    }
}
