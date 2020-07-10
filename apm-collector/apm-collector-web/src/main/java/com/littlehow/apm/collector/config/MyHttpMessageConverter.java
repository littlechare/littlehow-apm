package com.littlehow.apm.collector.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @Description 自定义JSON转换器
 */
@Slf4j
public class MyHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public MyHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8, MediaType.TEXT_PLAIN);
    }

    protected boolean supports(Class<?> paramClass) {
        return true;
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        InputStream in = inputMessage.getBody();

        byte[] byteReqJson = FileCopyUtils.copyToByteArray(in);
        String strJson = new String(byteReqJson, StandardCharsets.UTF_8);
        in = new ByteArrayInputStream(strJson.getBytes());
        try {
            return JSON.parseObject(in, this.fastJsonConfig.getCharset(), type, this.fastJsonConfig.getFeatures());
        } catch (JSONException e) {
            throw new HttpMessageNotReadableException(strJson);
        }

    }

    protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        HttpHeaders headers = outputMessage.getHeaders();
        ByteArrayOutputStream outNew = new ByteArrayOutputStream();
        Object value = obj;
        SerializeFilter[] globalFilters = this.fastJsonConfig.getSerializeFilters();

        List<SerializeFilter> allFilters = Arrays.stream(globalFilters).collect(Collectors.toList());
        if (obj instanceof FastJsonContainer) {
            PropertyPreFilters filters = ((FastJsonContainer) obj).getFilters();
            allFilters.addAll(filters.getFilters());
            value = ((FastJsonContainer) obj).getValue();
        }

        SerializeFilter[] serializeFilters = new SerializeFilter[allFilters.size()];
        int len = JSON.writeJSONString(outNew, this.fastJsonConfig.getCharset(),
                value, this.fastJsonConfig.getSerializeConfig(), allFilters.toArray(serializeFilters),
                this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE,
                this.fastJsonConfig.getSerializerFeatures());
        if (this.fastJsonConfig.isWriteContentLength()) {
            headers.setContentLength((long) len);
        }

        OutputStream out = outputMessage.getBody();
        outNew.writeTo(out);
        outNew.close();
    }

    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        InputStream in = inputMessage.getBody();
        return JSON.parseObject(in, this.fastJsonConfig.getCharset(), clazz, this.fastJsonConfig.getFeatures());
    }

}
