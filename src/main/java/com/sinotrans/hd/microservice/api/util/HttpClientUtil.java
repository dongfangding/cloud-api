package com.sinotrans.hd.microservice.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 针对HttpClient提供一些简单的请求方法
 *
 * @author DDf on 2018/9/20
 */
public class HttpClientUtil {

    private HttpClientUtil() {}

    /**
     * 以utf-8的编码发送post请求
     *
     * @param url      请求地址
     * @param paramMap 请求参数
     */
    public static String executeDefaultPost(String url, Map<String, String> paramMap) {
        return executeDefaultPost(url, paramMap, "UTF-8");
    }


    /**
     * 以指定编码发送post请求
     *
     * @param url      请求地址
     * @param paramMap 请求参数
     * @param charset  请求编码
     */
    public static String executeDefaultPost(String url, Map<String, String> paramMap, String charset) {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            if (paramMap != null && !paramMap.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<>();
                paramMap.forEach((k, v) -> formParams.add(new BasicNameValuePair(k, v)));
                UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, charset);
                httpPost.setEntity(uefEntity);
            }
            response = httpclient.execute(httpPost);
            HttpEntity repEntity = response.getEntity();
            if (repEntity != null) {
                result = EntityUtils.toString(repEntity);
            }
            EntityUtils.consume(repEntity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
