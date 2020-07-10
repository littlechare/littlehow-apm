package com.littlehow.apm.collector.config;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 动态日志配置, 基于apollo配置中心
 * ref:https://github.com/ctripcorp/apollo-use-cases/blob/master/spring-boot-logger/src/main/java/com/ctrip/framework/apollo/use/cases/spring/boot/logger/LoggerConfiguration.java
 * 动态更新日志打印级别
 * @author littlehow
 */
@Component
public class DynamicLoggerLevelConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DynamicLoggerLevelConfiguration.class);
    /**
     * 在配置文件中设置的为logging.level.root，同时这个也设置到了logback.xml中
     */
    private static final String LOGGER_TAG = "logging.level.";

    @Autowired
    private LoggingSystem loggingSystem;

    @ApolloConfig
    private Config config;

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels();
    }

    @PostConstruct
    private void refreshLoggingLevels() {
        if (config == null) {
            //没有使用apollo配置
            return;
        }
        Set<String> keyNames = config.getPropertyNames();
        for (String key : keyNames) {
            if (containsIgnoreCase(key)) {
                String strLevel = config.getProperty(key, "info");
                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
                loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
                logger.info("log level key:{}, log level value:{}", key, strLevel);
            }
        }
    }

    private static boolean containsIgnoreCase(String str) {
        if (str == null) {
            return false;
        }
        int len = LOGGER_TAG.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, LOGGER_TAG, 0, len)) {
                return true;
            }
        }
        return false;
    }
}
