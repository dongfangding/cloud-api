package com.sinotrans.hd.microservice.api.util;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 常量类
 *
 * @author DDf on 2018/9/21
 */
public class Constants {

    private Constants() {}

    /** 组织架构的服务名 */
    public static final String ORG_APPLICATION_NAME = "HD-ORG-SERVER";

    /** 收款人的服务名 */
    public static final String GEN_APPLICATION_NAME = "HD-GEN-SERVER";


    public static Map<String, Object> mapCopy(Map<String, Object> sourceMap) {
        if (sourceMap != null && !sourceMap.isEmpty()) {
            Map<String, Object> newMap = new HashMap<>();
            sourceMap.forEach(newMap::put);
            return newMap;
        }
        return Collections.emptyMap();
    }



    public static Map<String, Object> entityMapCopy(Map<String, Object> sourceMap) {
        if (sourceMap != null && !sourceMap.isEmpty()) {
            Map<String, Object> newMap = new HashMap<>();
            sourceMap.forEach((k, v) -> {
                if ("ID".equals(k) || "CREATE_BY".equals(k) || "CREATE_TIME".equals(k)
                        || "MODIFY_BY".equals(k) || "MODIFY_TIME".equals(k)
                        || "REMOVED".equals(k) || "VERSION".equals(k)
                        || "UUID".equals(k)) {
                    return;
                }
                newMap.put(k, v);
            });
            return newMap;
        }
        return Collections.emptyMap();
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
            if (!StringUtils.isEmpty(substring)) {
                retList.add(substring);
            }
            matchChar(leftChar, rightChar, retList, str, y + rightChar.length());
        }
    }

    /**
     * 判断对象是否为空
     * @param str
     * @return
     */
    public static boolean isNotNull(Object str) {
        return str != null && !"".equals(str.toString()) && !"".equals(str.toString().trim());
    }
}
