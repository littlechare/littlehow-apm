package com.littlehow.apm.collector.orm.support.mysql.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Setter
@Getter
public class DbConfig {
    private int minIdle;
    private int initialSize;
    private String validationQuery;
    private String driverClassName;
    private int maxActive;
    private int maxWait;
    private String url;
    private String username;
    private String password;
    /**
     * #//：1) Destroy线程会检测连接的间隔时间 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     */
    private long timeBetweenEvictionRunsMillis;
    /**
     * Destory线程中如果检测到当前连接的最后活跃时间和当前时间的差值大于minEvictableIdleTimeMillis，则关闭当前连接。
     */
    private long minEvictableIdleTimeMillis;
    /**
     * #建议配置为true，不影响性能，并且保证安全性
     */
    private boolean testWhileIdle;
    /**
     * #对于建立时间超过removeAbandonedTimeout的连接强制关闭
     */
    private boolean removeAbandoned;
    /**
     * #是否每次校验连接的有效性
     */
    private boolean testOnBorrow;
}
