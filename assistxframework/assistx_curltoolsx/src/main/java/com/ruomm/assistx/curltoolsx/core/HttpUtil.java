/**
 * @copyright wanruome-2018
 * @author 牛牛-wanruome@163.com
 * @create 2018年1月26日 下午3:23:59
 */
package com.ruomm.assistx.curltoolsx.core;

import com.alibaba.fastjson.JSON;
import com.ruomm.javax.corex.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class HttpUtil {
    public final static int READ_TIMEOUT = 50000;
    public final static int CONNECT_TIMEOUT = 16000;

    public static String read(InputStream input, String charsetName) {
        String data = null;
        ByteArrayOutputStream bufos = null;
        try {
            bufos = new ByteArrayOutputStream();
            byte[] buffer = new byte[64];
            int count = 0;
            while ((count = input.read(buffer)) > 0) {
                bufos.write(buffer, 0, count);
            }
            bufos.flush();
            byte[] buf = bufos.toByteArray();
            data = new String(buf, charsetName);
        } catch (Exception e) {
            data = null;
        } finally {
            try {
                input.close();
                bufos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return data;
    }

    public static ResponseText get(String reqestUrl, Map<String, String> paramMap, String charsetName) {
        ResponseText responseText = new ResponseText();
        responseText.resultString = null;
        responseText.status = 499;
        if (StringUtils.isEmpty(reqestUrl)) {
            return responseText;
        }
        HttpURLConnection conn = null;
        try {// 注意这部分一定要
            String reqCharsetName = parseRealCharsetName(charsetName, "utf-8");
            String realUrl = attachFormRequestUrl(reqestUrl, paramMap, reqCharsetName);
            URL url = new URL(realUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestProperty("Connection", "close");
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=" + reqCharsetName);
            conn.connect();
            responseText.status = conn.getResponseCode();
            responseText.resultString = read(conn.getInputStream(), reqCharsetName);
        } catch (Exception e) {
            e.printStackTrace();
            responseText.resultString = null;
        } finally {
            try {
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return responseText;
    }

    // 获取真实的Get请求路径
    private static String attachFormRequestUrl(String url, Map<String, String> mapData, String charsetName) {
        if (null == mapData || mapData.size() <= 0) {
            return url;
        }
        if (url.contains("?") && url.contains("=")) {
            return url + "&" + attachFormRequestFormatString(mapData, true, charsetName);
        } else {
            return url + "?" + attachFormRequestFormatString(mapData, true, charsetName);
        }
    }

    // 构建Get请求参数
    private static String attachFormRequestFormatString(Map<String, String> mapData, boolean isUrlEncode,
                                                        String charsetName) {
        StringBuilder buf = new StringBuilder();
        Set<String> sets = mapData.keySet();
        int sizeSets = sets.size();
        int index = 0;
        for (String key : sets) {
            index++;
            buf.append(key).append("=");
            String value = mapData.get(key);
            if (isUrlEncode) {
                try {
                    value = URLEncoder.encode(value, charsetName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!StringUtils.isEmpty(value)) {
                buf.append(value);
            }
            if (index != sizeSets) {
                buf.append("&");
            }
        }
        return buf.toString();
    }

    public static ResponseText post(String reqestUrl, Map<String, String> paramMap, String charsetName) {

        ResponseText responseText = new ResponseText();
        responseText.resultString = null;
        responseText.status = 499;
        if (StringUtils.isEmpty(reqestUrl)) {
            return responseText;
        }
        HttpURLConnection conn = null;
        try {// 注意这部分一定要

            String reqCharsetName = parseRealCharsetName(charsetName, "utf-8");
            URL url = new URL(reqestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            // conn.setRequestProperty("Connection", "close");

            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=" + reqCharsetName);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            String body = attachFormRequestFormatString(paramMap, true, reqCharsetName);
            if (!StringUtils.isEmpty(body)) {
                Writer w = new OutputStreamWriter(conn.getOutputStream());
                w.write(body);
                w.flush();
                w.close();
            }
            responseText.status = conn.getResponseCode();
            responseText.resultString = read(conn.getInputStream(), reqCharsetName);
        } catch (Exception e) {
            e.printStackTrace();
            responseText.resultString = null;
        } finally {
            try {
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return responseText;
    }

    public static ResponseText postJson(String reqestUrl, Map<String, String> paramMap, String charsetName) {

        ResponseText responseText = new ResponseText();
        responseText.resultString = null;
        responseText.status = 499;
        if (StringUtils.isEmpty(reqestUrl)) {
            return responseText;
        }
        HttpURLConnection conn = null;
        try {// 注意这部分一定要

            String reqCharsetName = parseRealCharsetName(charsetName, "utf-8");
            URL url = new URL(reqestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            // conn.setRequestProperty("Connection", "close");

            conn.setRequestProperty("Content-type", "application/json; charset=" + reqCharsetName);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            String body = JSON.toJSONString(paramMap);
            if (!StringUtils.isEmpty(body)) {
                Writer w = new OutputStreamWriter(conn.getOutputStream(), reqCharsetName);
                w.write(body);
                w.flush();
                w.close();
            }
            responseText.status = conn.getResponseCode();
            responseText.resultString = read(conn.getInputStream(), reqCharsetName);
        } catch (Exception e) {
            e.printStackTrace();
            responseText.resultString = null;
        } finally {
            try {
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return responseText;
    }

    /**
     * 获取真实的String格式字符编码，若是找不到返回默认的String格式字符编码(defaultCharsetName)
     * @param charsetName 输入的字符编码名称
     * @param defaultCharsetName 默认的String格式字符编码。默认值为null;
     * @return String格式字符编码
     */
    private static String parseRealCharsetName(String charsetName, String defaultCharsetName) {
        String realCharsetName = null;
        try {
            if (null == charsetName || charsetName.length() <= 0 || !Charset.isSupported(charsetName)) {
                realCharsetName = null;
            } else {
                realCharsetName = charsetName;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            realCharsetName = null;
        }
        if (null == realCharsetName) {
            return defaultCharsetName;
        }
        return realCharsetName;
    }
}
