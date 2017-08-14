package com.otus.alexeenko.database.services.custom.beans.spi;

/**
 * Created by Vsevolod on 07/08/2017.
 */
public interface MBeanConfiguration {
    String getStatistics();
    String getManagementInfo();
    void setConfiguration(String data);
}
