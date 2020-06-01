package com.github.chengtengfei.onvif.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    // 匹配字符串中的ip:port
    private final static String REGEX_IP_PORT = "/((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)(:\\d+)?/";
    private static final Pattern PATTERN_REGEX_IP_PORTE = Pattern.compile(REGEX_IP_PORT);

    /**
     * 正则：IP地址
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);

    public static boolean isMatch(Pattern pattern, String str) {
        return StringUtils.isNotEmpty(str) && pattern.matcher(str).matches();
    }


    public static String getIpPortFromUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        Matcher matcher = PATTERN_REGEX_IP_PORTE.matcher(url);
        while (matcher.find()) {
            return matcher.group();
        }

        return "";
    }

    public static boolean isIp( String str) {
        return isMatch(PATTERN_REGEX_IP, str);
    }

    public static String extractIpFromString(String extractLine) {
        Matcher matcher = PATTERN_REGEX_IP.matcher(extractLine);
        if (matcher.find()) {
            return matcher.group();
        } else{
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(getIpPortFromUrl("http://192.168.88.209/onvif-http/snapshot?Profile_2"));
        System.out.println(extractIpFromString("http://192.168.101.231:8000/onvif/device_service/192.13.14.15/fewwq32"));
    }
}
