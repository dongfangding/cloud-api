package com.sinotrans.hd.microservice.api.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 针对String类提供一些常用的方法
 */
public class StringHelper {

    private StringHelper() {}


    public static boolean isNotNull(Object str) {
        return str != null && !"".equals(str.toString()) && !"".equals(str.toString().trim());
    }

    public static boolean isNull(Object str) {
        return !isNotNull(str);
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null|| map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static String nullToBlank(Object obj) {
        if (isNotNull(obj)) {
            return obj.toString();
        }
        return "";
    }

    public static String nullToDefault(Object obj, String defaultValue) {
        if (isNotNull(obj)) {
            return obj.toString();
        }
        return defaultValue;
    }

    public static Integer parseInt(String str) {
        return Integer.parseInt(str);
    }

    public static String insertLine() {
        return System.getProperty("line.separator");
    }

    public static Integer mappingSysFlag(String sysCode) {
        if ("EOC".equals(sysCode)) {
            return 0;
        }
        return 1;
    }

    public static Map<String, Object> successMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "success");
        map.put("message", "成功");
        return map;
    }

    /**
     * 用来获取指定两个字符之间的值，如${consNo}获取出conNo
     * @param leftChar 如${
     * @param rightChar 如}
     * @param retList 匹配的结果返回
     * @param str 完整字符串
     * @param i 需要从哪个位置开始匹配
     */
    public static void matchChar(String leftChar, String rightChar, List<String> retList, String str, int i) {
        int x = str.indexOf(leftChar, i);
        int y = str.indexOf(rightChar, x + leftChar.length());
        if (x >= 0 && y >= 0 && x <= y) {
            String substring = str.substring(x + leftChar.length(), y);
            if (isNotNull(substring)) {
                retList.add(substring);
            }
            matchChar(leftChar, rightChar, retList, str, y + rightChar.length());
        }
    }

    public static String substringWithChar(String str, String startChar, String endChar) {
        int i = str.indexOf(startChar);
        int j = str.indexOf(endChar);
        if (i != -1 && j != -1) {
            String substring = str.substring(i+startChar.length(), j);
            return substring;
        }
        return "";
    }
}
