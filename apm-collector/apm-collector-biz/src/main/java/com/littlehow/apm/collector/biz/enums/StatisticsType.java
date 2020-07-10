package com.littlehow.apm.collector.biz.enums;

public enum StatisticsType {
    SYSTEM("1"),
    INTERFACE("2");

    public final String v;

    StatisticsType(String v) {
        this.v = v;
    }

    public static boolean isSystem(String value) {
        return SYSTEM.v.equals(value) || SYSTEM.name().equals(value);
    }

    public static boolean isInterface(String value) {
        return INTERFACE.v.equals(value) || INTERFACE.name().equals(value);
    }
}
