package com.littlehow.apm.base.web;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据feign提供的url分析出系统以及uri
 * @author littlehow
 */
@Getter
public class MyApplicationUrl {

    private static final Pattern HTTP_APPLICATION_PATTERN = Pattern.compile("http(s)?://([\\w-.:]+)(/.+)");

    private String applicationName;

    private String uri;

    public MyApplicationUrl(String applicationUrl) {
        Matcher matcher = HTTP_APPLICATION_PATTERN.matcher(applicationUrl);
        if (matcher.find()) {
            this.applicationName = matcher.group(2);
            this.uri = matcher.group(3);
            int index = uri.indexOf("?");
            if (index > 0) {
                this.uri = this.uri.substring(0, index);
            }
        }
    }

    @Override
    public String toString() {
        return applicationName + "->" + uri;
    }
}
