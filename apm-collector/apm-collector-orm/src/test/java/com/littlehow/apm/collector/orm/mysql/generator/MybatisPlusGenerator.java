package com.littlehow.apm.collector.orm.mysql.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MybatisPlusGenerator {

    private static void run(String parentModule, String dbName, String pkgName, String[] tableNames) {
        //1. 全局配置
        String projectPath = System.getProperty("user.dir");
        System.out.println("projectPath: " + projectPath);

        String moduleName = parentModule + "-orm";

        GlobalConfig globalConfig = new GlobalConfig()
                .setOutputDir(projectPath + "/" + parentModule + "/" + moduleName + "/src/main/java")
                .setAuthor("littlehow")
                .setOpen(false)
                ;

        //2. 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)  // 设置数据库类型
                .setDriverName("com.mysql.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/" + dbName + "?useUnicode=true&useSSL=false&characterEncoding=utf8")
                .setUsername("root")
                .setPassword("root123");

        //3. 包名策略配置
        PackageConfig pkConfig = new PackageConfig()
                .setEntity("dao.po")
                .setMapper("dao.mapper")
                .setXml("dao.mapper.xml")
                .setParent(pkgName);

        //4. 策略配置
        StrategyConfig stConfig = new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityLombokModel(true)
                .setInclude(tableNames)
                .setControllerMappingHyphenStyle(true);

        //5. 整合配置
        AutoGenerator mpg = new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(stConfig)
                .setTemplate(new TemplateConfig().setController(null))
                .setPackageInfo(pkConfig);
        mpg.execute();
    }

    public static void main(String[] args) {
        //模块名称
        String parentModule = "apm-collector";
        //数据库名称
        String dbName = "apm";
        //包名
        String pkgName = "com.littlehow.apm.collector.orm.support.mysql";
        //表
        String[] tablesName = {"server_base", "server_info", "server_interface",
                "interface_dependence", "call_statistics", "server_change_log",
                "trace_log"};
        run(parentModule, dbName, pkgName, tablesName);
    }
}
