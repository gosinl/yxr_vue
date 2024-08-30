package com.yxr.report.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxr.report.domain.ReportSqlColumn;
import com.yxr.report.mapper.ReportSqlColumnMapper;
import com.yxr.report.service.IReportSqlColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportSqlColumnServiceImpl extends ServiceImpl<ReportSqlColumnMapper, ReportSqlColumn> implements IReportSqlColumnService {
}
