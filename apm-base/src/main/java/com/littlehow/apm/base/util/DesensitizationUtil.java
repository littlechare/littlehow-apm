package com.littlehow.apm.base.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 主要对上报日志的脱敏工具
 * 次工具针对接口请求和响应都是基于json格式的请求
 *
 * @author littlehow
 */
@Slf4j
public class DesensitizationUtil {
    private static Pattern desensitizationRequestPattern;
    private static Pattern desensitizationResponsePattern;

    static {
        List<String> desenBodyList = Lists.newArrayList(
                // 身份证或电子邮件信息/账号等
                "certificateNo", "email", "account",
                // 手机号,验证码，token等信息
                "mobileNo", "mobileNum", "mobilePhone", "smsCode", "token", "verifyCode",
                // 密码公私钥等信息
                "password", "securityPwd", "privateKey", "publicKey", "pubKey", "newPwd", "secretKey", "accessKey"

        );

        desensitizationResponsePattern = buildPattern(desenBodyList);
        // 请求code需要脱敏,因为响应体的code表示状态码,所以需要单独对请求体的code进行脱敏
        desenBodyList.add("code");
        desensitizationRequestPattern = buildPattern(desenBodyList);
    }

    /**
     * 构建脱敏模式
     * @param desenBodyList - 脱敏关键字组合
     * @return - 脱敏模式
     */
    private static Pattern buildPattern(List<String> desenBodyList) {
        String desensitizationBodyString = desenBodyList.stream().collect(Collectors.joining("|", "\\s*(", ")\\s*"));
        return Pattern.compile("\"" + desensitizationBodyString + "\"\\s*:\\s*(\")?([\\w\\s\\u4e00-\\u9fa5#$!@+-<>?~&*^%\\{\\}\\[\\].]+)", Pattern.CASE_INSENSITIVE);
    }

    /**
     * 对请求体进行脱敏
     * @param body - 请求体(json格式才会脱敏)
     * @return -
     */
    public static String desensitizationRequest(String body) {
        return desensitization(body, desensitizationRequestPattern);
    }

    /**
     *  如果响应体不是以code作为状态码，请求体和响应体的脱敏方法可合并
     * @param body - 响应体(json才会脱敏,可以判定响应是否为application/json)
     * @return
     */
    public static String desensitizationResponse(String body) {
        return desensitization(body, desensitizationResponsePattern);
    }

    /**
     * 执行脱敏(脱敏主要保存前后两个字符串，如果字符只有两个，则会在中间填充***, 字符长度小于2位将原样输出)
     * 可修改replaceAll方法来对脱敏信息进行保留位数的设置
     * @param body      - 脱敏提
     * @param pattern   - 模式
     * @return - 被脱敏的字符串
     */
    private static String desensitization(String body, Pattern pattern) {
        try {
            Matcher matcher = pattern.matcher(body);
            while (matcher.find()) {
                String target = matcher.group(3);
                String desc = target.replaceAll("(.).*(.)", "$1***$2");
                body = body.replace(target, desc);
            }
        } catch (Exception e) {
            log.warn("脱敏请求体或响应体异常", e);
        }
        return body;
    }

    /**
     * 脱敏示例
     * 可能不满足所有的json脱敏场景，如果遇到脱敏失败或脱敏不正确的地方，可自行修改本脱敏方法
     * requestBody {"code":"101655", "email":"wangshow12@126.com", "name":"littlehow", "password":"我的密码16398729"}
     * responseBody {"code":"000000", "msg":"success", "data":{"userNo":"1234", "accessKey":"asdf1231ac1234242ssdfAqw32", "token": "1234sdfasdfwe123"}}
     *
     * @param args -
     */
    public static void main(String[] args) {
        // 对请求体进行脱敏
        String requestBody = "{\"code\":\"101655\", \"email\":\"wangshow12@126.com\", \"name\":\"littlehow\", \"password\":\"我的密码#@#$@#$}{16398729\"}";
        // {"code":"1***5", "email":"w***m", "name":"littlehow", "password":"我***9"}
        System.out.println(desensitizationRequest(requestBody));

        // 对响应体进行脱敏,code会被保留
        String responseBody = "{\"code\":\"000000\", \"msg\":\"success\", \"data\":{\"userNo\":\"1234\", \"accessKey\":\"asdf1231ac1234242ssdfAqw32\", \"token\": \"1234sdfasdfwe123\"}}";
        // {"code":"000000", "msg":"success", "data":{"userNo":"1234", "accessKey":"a***2", "token": "1***3"}}
        System.out.println(desensitizationResponse(responseBody));
    }
}
