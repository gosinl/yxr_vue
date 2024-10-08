package com.yxr.report.util;


import com.yxr.framework.datasource.DynamicDataSource;
import com.yxr.report.domain.ReportDatabase;
import com.yxr.report.sql.dbpool.DataSourcePoolFactory;
import com.yxr.common.enums.PoolMation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbPoolUtils {

    private final DataSource dataSource;

    /**
     * 添加数据源 (通用)
     *
     * @param reportDatabase 数据源配置
     * @return
     */
    public Set<String> add(ReportDatabase reportDatabase) {
        DynamicDataSource ds = (DynamicDataSource) dataSource;
        if (!ds.getDataSources().containsKey(reportDatabase.getName())) {
            DataSource newDataSource = DataSourcePoolFactory.create(PoolMation.getPoolClassByType(reportDatabase.getPoolType())).wrap(reportDatabase);
            log.info(">>>>>>>>>>>>>> 创建数据源");
            ds.addDataSource(reportDatabase.getName(), newDataSource);
        }
        return ds.getDataSources().keySet();
    }

//    public static DataSource getDataSource(ReportDatabase reportDatabase) {
//        // 用数据源 用户名, 密码, jdbcUrl 做为 key
//        String key = String.format(DatasourceKeyFormat, reportDatabase.getUser(), reportDatabase.getPassword(), reportDatabase.getJdbcUrl())
//                .toLowerCase();
//        DataSource dataSource = dataSourceMap.get(key);
//        if (dataSource == null) {
//            dataSource = DataSourcePoolFactory.create(PoolMation.getPoolClassByType(reportDatabase.getPoolType())).wrap(reportDatabase);
//            dataSourceMap.put(key, dataSource);
//        }
//        return dataSource;
//    }

    /**
     * 删除数据源
     */
    public String remove(String name) {
        DynamicDataSource ds = (DynamicDataSource) dataSource;
        ds.removeDataSource(name);
        return "删除成功";
    }

}
