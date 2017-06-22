package com.otus.alexeenko.l8.services.custom.creators.spi;

import com.otus.alexeenko.l8.services.custom.builders.spi.QueryBuilder;

import java.util.List;

/**
 * Created by Vsevolod on 20/06/2017.
 */
public interface Creator {
    List<QueryBuilder> getQueries();
    void createQueries();
}
