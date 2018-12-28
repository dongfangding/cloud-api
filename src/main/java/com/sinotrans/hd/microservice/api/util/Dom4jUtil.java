package com.sinotrans.hd.microservice.api.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Map;

/**
 *
 * 针对Document提供一些常用的方法
 *
 * @author DDf on 2018/9/18
 */
public class Dom4jUtil {

    private Dom4jUtil() {}

    /**
     * 将字符串转换为Document对象并将根节点返回
     * @param content 需要转换的字符串
     */
    public static Element getDocument(String content) {
        try {
            Document document = DocumentHelper.parseText(content);
            return document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建文档
     */
    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        return document;
    }


    /**
     * 将Map里的数据添加到指定节点的子节点中
     * @param element 指定节点
     * @param childMap 子节点数据的Map形式
     */
    public static void addElement(Element element, Map<String, String> childMap) {
        if (childMap != null && !childMap.isEmpty()) {
            childMap.forEach((k, v) -> element.addElement(k).setText(v));
        }
    }
}
