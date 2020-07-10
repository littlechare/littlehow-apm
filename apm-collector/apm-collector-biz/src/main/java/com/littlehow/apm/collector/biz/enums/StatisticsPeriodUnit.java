package com.littlehow.apm.collector.biz.enums;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public enum StatisticsPeriodUnit {
    DAY(1, "yyyyMMdd"),
    HOUR(2, "yyyyMMddHH"),
    MINUTE(3, "yyyyMMddHHmm"),
    SECOND(4, "yyyyMMddHHmmss");

    public final int v;

    public final DateTimeFormatter format;

    //写死东八区
    private ZoneOffset offset = ZoneOffset.ofHours(8);

    StatisticsPeriodUnit(int v, String format) {
        this.v = v;
        this.format = DateTimeFormatter.ofPattern(format);
    }

    public String getPeriod(long time) {
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(time / 1000, 0, offset);
        return getPeriod(localDateTime);
    }

    public String getPeriod(LocalDateTime localDateTime) {
        return localDateTime.format(this.format);
    }
}
