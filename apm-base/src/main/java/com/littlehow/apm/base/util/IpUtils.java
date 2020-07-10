package com.littlehow.apm.base.util;


import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 获取本系统运行所在环境的ip地址
 * @author littlehow
 */
public class IpUtils {
    private static String ip;

    public static String getIp(){
        if (ip != null) {
            return ip;
        }
        try{
            String dockerIp = null;
            Map</* name */String, /* ip */String> targetIp = new LinkedHashMap<>();
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                String name = netInterface.getName();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")){
                        if (name.startsWith("docker")) {
                            dockerIp = ip.getHostAddress();
                        } else {
                            targetIp.put(netInterface.getName(), ip.getHostAddress());
                        }
                    }
                }
            }
            ApmAssert.isTrue(!CollectionUtils.isEmpty(targetIp) || dockerIp != null, "找不到有效ip地址");
            String tmpIp = getPriority(targetIp);
            if (StringUtils.hasText(tmpIp)) {
                ip = tmpIp;
            } else {
                ip = dockerIp;
            }
            return ip;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private static String getPriority(Map<String, String> targetIp) {
        if (CollectionUtils.isEmpty(targetIp)) return null;
        //优先获取eth0的网卡地址
        String ip = targetIp.get("eth0");
        ip = ip == null ? targetIp.get("en0") : ip;
        if (ip == null) {
            for (String s : targetIp.keySet()) {
                ip = targetIp.get(s);
                break;
            }
        }
        return ip;
    }

}
