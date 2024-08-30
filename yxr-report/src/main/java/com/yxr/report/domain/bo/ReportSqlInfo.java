package com.yxr.report.domain.bo;

import com.yxr.report.domain.Layout;
import com.yxr.report.domain.ReportSqlColumn;
import lombok.Data;

import java.util.List;

@Data
public class ReportSqlInfo {
    private List<ReportSqlColumn> reportSqlColumns;
    private List<Layout> layout;
}
