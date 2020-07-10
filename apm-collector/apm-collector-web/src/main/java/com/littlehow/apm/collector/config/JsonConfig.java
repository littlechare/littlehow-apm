package com.littlehow.apm.collector.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 *
 */
public class JsonConfig {


    private static ZoneOffset zoneOffset = ZoneOffset.ofHours(8);

    private static long zoneOffsetMilliseconds = 8 * 60 * 60 * 1000L;

    public static final long DAY = 1000L * 60 * 60 * 24;

    public static void setZoneOffset(int hour) {
        zoneOffset = ZoneOffset.ofHours(hour);
        zoneOffsetMilliseconds = hour * 60 * 60 * 1000L;
    }

    private JsonConfig() {
    }

    public static void init() {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteBigDecimalAsPlain.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.WriteMapNullValue.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.SortField.getMask();
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.MapSortField.getMask();
    }

    public static FastJsonConfig getFastJsonConfig() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                //SerializerFeature.DisableCircularReferenceDetect,
                //SerializerFeature.WriteMapNullValue,
                //SerializerFeature.SortField,
                //SerializerFeature.MapSortField,
                //SerializerFeature.WriteBigDecimalAsPlain,
                //SerializerFeature.WriteNullListAsEmpty,
                //SerializerFeature.WriteNullStringAsEmpty
        );
        ValueFilter filter = (object, name, value) -> {
            if(value instanceof BigDecimal){
                return ((BigDecimal) value).stripTrailingZeros().toPlainString();
            } else if (value instanceof LocalDateTime) {
                return ((LocalDateTime) value).toEpochSecond(zoneOffset) * 1000L;
            } else if (value instanceof LocalDate) {
                return ((LocalDate) value).toEpochDay() * DAY - zoneOffsetMilliseconds;
            }
            return value;
        };

        fastJsonConfig.setSerializeFilters(filter);
        return fastJsonConfig;
    }

}
