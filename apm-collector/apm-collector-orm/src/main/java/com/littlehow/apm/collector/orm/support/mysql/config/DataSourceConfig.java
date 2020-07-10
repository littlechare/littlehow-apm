package com.littlehow.apm.collector.orm.support.mysql.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * 数据源配置
 * @author littlehow
 */
@Slf4j
@Setter
@Configuration
public class DataSourceConfig {

    @Autowired
    private DbConfig dbConfig;

    @Primary
    @Bean(name = "myDataSource")
    public DataSource dataSource() {
        // 如果密码加密，可以在此处进行解密
        // dbConfig.setPassword(decode(dbConfig.getPassword()));
        DruidDataSource druidDataSource = new DruidDataSource();
        BeanUtils.copyProperties(dbConfig, druidDataSource);
        return druidDataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * mysql默认隔离级别为rr
     * @return
     */
    @Bean(name = "txRequired")
    public TransactionTemplate txRequired() {
        return new TransactionTemplate(transactionManager());
    }

    @Bean(name = "txReadCommit")
    public TransactionTemplate txReadCommit() {
        TransactionTemplate template = new TransactionTemplate(transactionManager());
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        return template;
    }


}
