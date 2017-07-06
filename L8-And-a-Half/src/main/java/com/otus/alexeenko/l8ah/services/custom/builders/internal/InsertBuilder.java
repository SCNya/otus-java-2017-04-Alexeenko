package com.otus.alexeenko.l8ah.services.custom.builders.internal;

import com.otus.alexeenko.l8ah.services.custom.builders.spi.InsertQueryBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Vsevolod on 17/06/2017.
 */
public class InsertBuilder implements InsertQueryBuilder {
    private static final String EMPTY_QUERY = " () values ()";
    private static final String CREATE_INSERT = "insert into ";
    private static final String VALUES = " values";

    private final Map<String, String> bundles;
    private final String queryName;

    public InsertBuilder(InsertBuilder queryBuilder) {
        this.bundles = new LinkedHashMap<>();
        this.bundles.putAll(queryBuilder.bundles);
        this.queryName = queryBuilder.queryName;
    }

    public InsertBuilder(String tableName) {
        this.bundles = new LinkedHashMap<>();
        this.queryName = tableName;
    }

    @Override
    public InsertBuilder addValue(String name, String value) {
        if (bundles.isEmpty())
            bundles.put(name, value);
        else
            bundles.put(", " + name, ", " + value);

        return new InsertBuilder(this);
    }

    @Override
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append(CREATE_INSERT);
        builder.append(queryName);

        if (bundles.size() > 0) {
            addNames(builder);
            builder.append(VALUES);
            addValues(builder);

            return builder.toString();
        } else
            return builder.append(EMPTY_QUERY).toString();
    }

    private void addNames(StringBuilder builder) {
        builder.append('(');
        for (String key : bundles.keySet())
            builder.append(key);
        builder.append(')');
    }

    private void addValues(StringBuilder builder) {
        builder.append('(');
        for (String value : bundles.values())
            builder.append(value);
        builder.append(')');
    }
}
