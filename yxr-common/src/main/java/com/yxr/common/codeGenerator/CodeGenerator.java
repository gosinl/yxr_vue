package com.yxr.common.codeGenerator;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.yxr.common.annotation.DataSource;
import com.yxr.common.enums.DataSourceType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//代码生成器
public class CodeGenerator {


    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:oracle:thin:@192.168.138.146:1521/orcl", "race", "race2017")
                // 全局配置
                //.globalConfig((scanner, builder) -> builder.author(scanner.apply("请输入作者名称？")))
                .globalConfig((scanner, builder) -> builder.author("GS"))
                // 包配置
                //.packageConfig((scanner, builder) -> builder.parent(scanner.apply("请输入包名？")))
                .packageConfig((scanner, builder) -> builder.parent("com.yxr.business"))
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .entityBuilder()
                        .enableLombok()
                        .addTableFills(
                                new Column("create_time", FieldFill.INSERT)
                        )
                        .build())
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板gs
                .templateEngine(new FreemarkerTemplateEngine())
                //.templateEngine(new VelocityTemplateEngine())
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
