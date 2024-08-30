package com.yxr.framework.datasource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;

import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.strategy.LoadBalanceDynamicDataSourceStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import com.baomidou.dynamic.datasource.ds.GroupDataSource;
import com.baomidou.dynamic.datasource.strategy.DynamicDataSourceStrategy;
import io.seata.rm.datasource.DataSourceProxy;
import com.p6spy.engine.spy.P6DataSource;
/**
 * 动态数据源
 *
 * @author ruoyi
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final String UNDERLINE = "_";
    private String primary = "master";
    private Boolean strict = false;
    private Boolean p6spy = false;
    private Boolean seata = false;
    private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap();
    private final Map<String, GroupDataSource> groupDataSources = new ConcurrentHashMap();
    private Class<? extends DynamicDataSourceStrategy> strategy = LoadBalanceDynamicDataSourceStrategy.class;

    public Map<String, DataSource> getDataSources() {
        return this.dataSourceMap;
    }


    public synchronized void addDataSource(String ds, DataSource dataSource) {
        DataSource oldDataSource = (DataSource)this.dataSourceMap.put(ds, dataSource);
        this.addGroupDataSource(ds, dataSource);
        if (oldDataSource != null) {
            this.closeDataSource(ds, oldDataSource);
        }
    }

    private void addGroupDataSource(String ds, DataSource dataSource) {
        if (ds.contains("_")) {
            String group = ds.split("_")[0];
            GroupDataSource groupDataSource = (GroupDataSource)this.groupDataSources.get(group);
            if (groupDataSource == null) {
                try {
                    groupDataSource = new GroupDataSource(group, (DynamicDataSourceStrategy)this.strategy.getDeclaredConstructor().newInstance());
                    this.groupDataSources.put(group, groupDataSource);
                } catch (Exception var6) {
                    throw new RuntimeException("dynamic-datasource - add the datasource named " + ds + " error", var6);
                }
            }

            groupDataSource.addDatasource(ds, dataSource);
        }

    }

    private void closeDataSource(String ds, DataSource dataSource) {
        try {
            if (dataSource instanceof ItemDataSource) {
                ((ItemDataSource)dataSource).close();
            } else {
                if (this.seata && dataSource instanceof DataSourceProxy) {
                    DataSourceProxy dataSourceProxy = (DataSourceProxy)dataSource;
                    dataSource = dataSourceProxy.getTargetDataSource();
                }

                if (this.p6spy && dataSource instanceof P6DataSource) {
                    Field realDataSourceField = P6DataSource.class.getDeclaredField("realDataSource");
                    realDataSourceField.setAccessible(true);
                    dataSource = (DataSource)realDataSourceField.get(dataSource);
                }

                Method closeMethod = ReflectionUtils.findMethod(dataSource.getClass(), "close");
                if (closeMethod != null) {
                    closeMethod.invoke(dataSource);
                }
            }
        } catch (Exception var4) {
            log.warn("dynamic-datasource closed datasource named [{}] failed", ds, var4);
        }

    }

    public synchronized void removeDataSource(String ds) {
        if (!StringUtils.hasText(ds)) {
            throw new RuntimeException("remove parameter could not be empty");
        } else if (this.primary.equals(ds)) {
            throw new RuntimeException("could not remove primary datasource");
        } else {
            if (this.dataSourceMap.containsKey(ds)) {
                DataSource dataSource = (DataSource)this.dataSourceMap.remove(ds);
                this.closeDataSource(ds, dataSource);
                if (ds.contains("_")) {
                    String group = ds.split("_")[0];
                    if (this.groupDataSources.containsKey(group)) {
                        DataSource oldDataSource = ((GroupDataSource)this.groupDataSources.get(group)).removeDatasource(ds);
                        if (oldDataSource == null) {
                            log.warn("fail for remove datasource from group. dataSource: {} ,group: {}", ds, group);
                        }
                    }
                }
                log.info("dynamic-datasource - remove the database named [{}] success", ds);
            } else {
                log.warn("dynamic-datasource - could not find a database named [{}]", ds);
            }

        }
    }

    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources)
    {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey()
    {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
