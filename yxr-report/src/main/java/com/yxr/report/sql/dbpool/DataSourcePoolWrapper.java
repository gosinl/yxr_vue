package com.yxr.report.sql.dbpool;

import com.yxr.report.domain.ReportDatabase;

import javax.sql.DataSource;


public interface DataSourcePoolWrapper {

    DataSource wrap(ReportDatabase reportDatabase);

}
