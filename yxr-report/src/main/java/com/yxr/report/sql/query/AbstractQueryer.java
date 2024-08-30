package com.yxr.report.sql.query;


import com.yxr.report.domain.ReportDatabase;
import com.yxr.report.domain.ReportSqlColumn;
import com.yxr.report.util.JdbcUtils;
import com.yxr.common.constant.GenConstants;
import com.yxr.common.enums.DataBaseMation;
import com.yxr.common.exception.ServiceException;
import com.yxr.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public abstract class AbstractQueryer {
    protected ReportDatabase database;

    protected AbstractQueryer(ReportDatabase dataSource) {
        this.database = dataSource;
    }

    /**
     * 获取sql查询出来的所有列
     *
     * @param sqlText sql语句
     * @return 元数据列集合
     */
    public List<ReportSqlColumn> parseMetaDataColumns(String sqlText) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            log.debug("Parse Report MetaDataColumns SQL:{},", sqlText);
            // 创建连接
            conn = this.getJdbcConnection();
            // 创建通讯
            stmt = conn.createStatement();
            // 执行sql
            rs = stmt.executeQuery(this.preprocessSqlText(sqlText));
            // 获取列
            return getReportMetaDataColumns(rs);
        } catch (SQLException throwables) {
            throw new ServiceException(throwables.getMessage());
        } finally {
            JdbcUtils.releaseJdbcResource(conn, stmt, rs);
        }
    }

    /**
     * 预处理获取报表列集合的sql语句，
     * 在这里可以拦截全表查询等sql， 因为如果表的数据量很大，将会产生过多的内存消耗，甚至性能问题
     *
     * @param sqlText 原sql语句
     * @return 预处理后的sql语句
     */
    protected String preprocessSqlText(String sqlText) {
        return sqlText;
    }

    /**
     * 获取当前报表查询器的JDBC Connection对象
     * @return Connection
     */
    public Connection getJdbcConnection() {
        try {
            Class.forName(DataBaseMation.getDricerClassByType(database.getDataType()));
            return JdbcUtils.getDataSource(database).getConnection();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<ReportSqlColumn> getReportMetaDataColumns(ResultSet rs) throws SQLException {
        // 获取结果
        // 获取结果集元数据
        ResultSetMetaData rsMataData = rs.getMetaData();
        int count = rsMataData.getColumnCount();
        List<ReportSqlColumn> columns = new ArrayList<>(count);

        // 遍历结果集的列
        for (int i = 1; i <= count; i++) {
            ReportSqlColumn column = new ReportSqlColumn();
            column.setSqlField(rsMataData.getColumnName(i));
            //column.setColumnName(rsMataData.getColumnLabel(i));
            // 获取列的注释信息
            DatabaseMetaData metaData = rs.getStatement().getConnection().getMetaData();
            String tableName = rsMataData.getTableName(i);
            String columnName = rsMataData.getColumnName(i);
            ResultSet columnResultSet = metaData.getColumns(null, null, tableName, columnName);
            if (columnResultSet.next()) {
                String remarks = columnResultSet.getString("REMARKS");
                column.setColumnName(remarks);
            }else {
                column.setColumnName(rsMataData.getColumnLabel(i));
            }
            //log.info("Column Comment:"+rs.getString("REMARKS"));
            column.setIsList("1");
            column.setIsExport("1");
            column.setIsQuery("0");
            // 查询字段类型
            if (StringUtils.endsWithIgnoreCase(rsMataData.getColumnLabel(i), "name")) {
                column.setIsQuery("1");
                column.setQueryType("LIKE");
                column.setQueryType(GenConstants.QUERY_LIKE);
            }
            // 时间类型设置时间空间
            if (Arrays.asList(GenConstants.COLUMNTYPE_TIME).contains(rsMataData.getColumnTypeName(i))) {
                column.setIsQuery("1");
                column.setQueryType("BETWEEN");
                column.setHtmlType(GenConstants.HTML_DATETIME);
            }
            // 类型&性别字段设置下拉框
            else if (StringUtils.endsWithIgnoreCase(rsMataData.getColumnLabel(i), "type")
                    || StringUtils.endsWithIgnoreCase(rsMataData.getColumnLabel(i), "sex")) {
                column.setIsQuery("1");
                column.setQueryType("EQ");
                column.setHtmlType(GenConstants.HTML_SELECT);
            }
            columns.add(column);
        }
        return columns;
    }

}
