package com.otus.alexeenko.database.services.custom.beans.internal;

import com.otus.alexeenko.database.services.custom.beans.spi.MBeanConfiguration;
import net.sf.ehcache.management.CacheConfigurationMBean;
import net.sf.ehcache.management.CacheStatisticsMBean;
import org.slf4j.Logger;

import javax.json.*;
import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.StringReader;
import java.lang.management.ManagementFactory;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 07/08/2017.
 */
public class DataBaseMBeanConfiguration implements MBeanConfiguration {
    private final static String CACHE_STATISTICS_NAME =
            "net.sf.ehcache:CacheManager=__DEFAULT__,name=userCache,type=CacheStatistics";
    private final static String CACHE_CONFIGURATION_NAME =
            "net.sf.ehcache:CacheManager=__DEFAULT__,name=userCache,type=CacheConfiguration";

    private static final Logger LOGGER = getLogger("DataBaseMBeanConfiguration");

    private final MBeanServer mbs;
    private final CacheStatisticsMBean statisticsMBean;
    private final CacheConfigurationMBean configurationMBean;


    public DataBaseMBeanConfiguration() {
        mbs = ManagementFactory.getPlatformMBeanServer();
        statisticsMBean = getCacheCacheStatisticsMBean();
        configurationMBean = getCacheConfigurationMBean();
    }

    private CacheStatisticsMBean getCacheCacheStatisticsMBean() {
        CacheStatisticsMBean cacheStatisticsMBean = null;
        ObjectName cacheStatisticsObjectName;

        try {
            cacheStatisticsObjectName = new ObjectName(CACHE_STATISTICS_NAME);
            cacheStatisticsMBean = JMX.newMBeanProxy(mbs, cacheStatisticsObjectName,
                    CacheStatisticsMBean.class, true);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        return cacheStatisticsMBean;
    }

    private CacheConfigurationMBean getCacheConfigurationMBean() {
        CacheConfigurationMBean cacheConfigurationMBean = null;
        ObjectName cacheConfigurationObjectName;

        try {
            cacheConfigurationObjectName = new ObjectName(CACHE_CONFIGURATION_NAME);
            cacheConfigurationMBean = JMX.newMBeanProxy(mbs, cacheConfigurationObjectName,
                    CacheConfigurationMBean.class, true);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return cacheConfigurationMBean;
    }

    @Override
    public String getStatistics() {
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();

        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "AssociatedCacheName")
                        .add("value", statisticsMBean.getAssociatedCacheName())
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheHits")
                        .add("value", statisticsMBean.getCacheHits())
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheMisses")
                        .add("value", statisticsMBean.getCacheMisses())
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheHitPercentage")
                        .add("value", Math.round(statisticsMBean.getCacheHitPercentage() * 100d))
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheMissPercentage")
                        .add("value", Math.round(statisticsMBean.getCacheMissPercentage() * 100d))
        );

        return jArrayBuilder.build().toString();
    }

    @Override
    public String getManagementInfo() {
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();

        jObjectBuilder.add("heapName", "MaxBytesLocalHeap")
                .add("heapSize", configurationMBean.getMaxBytesLocalHeap())
                .add("memoryName", "MemoryStoreEvictionPolicy")
                .add("policy", configurationMBean.getMemoryStoreEvictionPolicy())
                .add("timeName", "TimeToLiveSeconds")
                .add("time", configurationMBean.getTimeToLiveSeconds());

        return jObjectBuilder.build().toString();
    }

    @Override
    public void setConfiguration(String data) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(data))) {
            JsonObject jData = jsonReader.readObject();
            configurationMBean.setMemoryStoreEvictionPolicy(jData.getString("policy"));
            configurationMBean.setTimeToLiveSeconds(jData.getInt("time"));
        } catch (Exception e) {
            LOGGER.error("Bad configuration data: " + e.getMessage());
        }
    }
}
