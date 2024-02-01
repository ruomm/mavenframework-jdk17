package com.ruomm.javax.corex;

import com.ruomm.javax.corex.annotation.DefRemoteApiUrl;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/7/6 12:07
 */
public class RemoteUrlUtils {
    /**
     * 依据路径地址和接口地址解析请求地址
     *
     * @param baseUrl 路径地址
     * @param apiUrl  接口地址
     * @return 请求地址
     */
    private static String parseRemoteUrlByString(String baseUrl, String apiUrl) {
        StringBuilder sbUrl = new StringBuilder();
        if (null != baseUrl && baseUrl.length() > 0) {
            if (baseUrl.endsWith("/")) {
                sbUrl.append(baseUrl.substring(0, baseUrl.length() - 1));
            } else {
                sbUrl.append(baseUrl);
            }
        }
        if (null != apiUrl && apiUrl.length() > 0) {
            if (sbUrl.length() > 0) {
                if (apiUrl.startsWith("/")) {
                    sbUrl.append(apiUrl);
                } else {
                    sbUrl.append("/").append(apiUrl);
                }
            } else {
                if (apiUrl.startsWith("/")) {
                    sbUrl.append(apiUrl.substring(1));
                } else {
                    sbUrl.append(apiUrl);
                }
            }
        }
        return sbUrl.toString();
    }

    /**
     * 依据路径地址和请求对象解析请求地址
     *
     * @param baseUrl    路径地址
     * @param requestObj 请求对象
     * @return 请求地址
     */
    public static String parseRemoteUrl(String baseUrl, Object requestObj) {
        if (null == requestObj) {
            return parseRemoteUrlByString(baseUrl, null);
        }
        if (requestObj instanceof String) {
            return parseRemoteUrlByString(baseUrl, (String) requestObj);
        }
        DefRemoteApiUrl remoteApiUrl = requestObj.getClass().getAnnotation(DefRemoteApiUrl.class);
        if (null == remoteApiUrl) {
            return parseRemoteUrlByString(baseUrl, null);
        } else {
            return parseRemoteUrlByString(baseUrl, remoteApiUrl.apiUrl());
        }
    }

    /**
     * 依据请求对象解析请求接口地址
     *
     * @param requestObj 请求对象
     * @return 请求接口地址
     */
    public static String getRemoteApiUrl(Object requestObj) {
        if (null == requestObj) {
            return "";
        }
        if (requestObj instanceof String) {
            return (String) requestObj;
        }
        DefRemoteApiUrl remoteApiUrl = requestObj.getClass().getAnnotation(DefRemoteApiUrl.class);
        if (null == remoteApiUrl) {
            return "";
        } else {
            if (null == remoteApiUrl.apiUrl()) {
                return "";
            } else {
                return remoteApiUrl.apiUrl();
            }
        }
    }

    /**
     * 依据请求对象解析请求接口名称
     *
     * @param requestObj 请求对象
     * @return 请求接口名称
     */
    public static String getRemoteApiDesc(Object requestObj) {
        return getRemoteApiDesc(requestObj, null);
    }

    /**
     * 依据请求对象解析请求接口名称
     *
     * @param requestObj 请求对象
     * @param defaultVal 接口名称默认值
     * @return 请求接口名称
     */
    public static String getRemoteApiDesc(Object requestObj, String defaultVal) {
        if (null == requestObj) {
            return defaultVal;
        }
        DefRemoteApiUrl remoteApiUrl = requestObj.getClass().getAnnotation(DefRemoteApiUrl.class);
        String apiDesc;
        if (null == remoteApiUrl) {
            apiDesc = defaultVal;
        } else {
            apiDesc = remoteApiUrl.apiDesc();
            if (null == apiDesc || apiDesc.length() <= 0) {
                apiDesc = remoteApiUrl.apiUrl();
            }
            if (null == apiDesc || apiDesc.length() <= 0) {
                apiDesc = defaultVal;
            }
        }
        return apiDesc;
    }

    /**
     * 依据请求对象解析相应对象自动解析类
     *
     * @param requestObj 请求对象
     * @return 相应对象自动解析类
     */
    public static Class<?> getRemoteTargetClass(Object requestObj) {
        if (null == requestObj) {
            return null;
        }
        DefRemoteApiUrl remoteApiUrl = requestObj.getClass().getAnnotation(DefRemoteApiUrl.class);
        if (null == remoteApiUrl || null == remoteApiUrl.targetClass()) {
            return null;
        } else {
            if (remoteApiUrl.targetClass() == Object.class) {
                return null;
            } else {
                return remoteApiUrl.targetClass();
            }
        }
    }
}
