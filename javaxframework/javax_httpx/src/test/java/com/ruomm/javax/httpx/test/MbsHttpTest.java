package com.ruomm.javax.httpx.test;

import com.ruomm.javax.corex.ListUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.dal.ResponseText;
import com.ruomm.javax.httpx.test.dal.IdrEnvClientsGetRequest;
import com.ruomm.javax.httpx.test.dal.IdrEnvClientsGetResponse;
import com.ruomm.javax.httpx.test.tools.HttpUtils;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/31 10:12
 */
public class MbsHttpTest {
    public final static HttpConfig.LoggerLevel HTTP_LOGGER_LEVEL = HttpConfig.LoggerLevel.INFO;

    public static void main(String[] args) {
        doHttpIdrEnvClientsGet(HttpUtils.HTTP_MODE.URL,"http://10.0.199.199:8080/capturesync/");
        doHttpIdrEnvClientsGet(HttpUtils.HTTP_MODE.OKHTTP,"http://10.0.199.199:8080/capturesync/");
        doHttpIdrEnvClientsGet(HttpUtils.HTTP_MODE.HTTPCLIENT,"http://10.0.199.199:8080/capturesync/");
    }

    public static IdrEnvClientsGetResponse doHttpIdrEnvClientsGet(HttpUtils.HTTP_MODE httpMode, String url) {
        HttpConfig.configLoggerLevel(HTTP_LOGGER_LEVEL);
        IdrEnvClientsGetRequest idrEnvClientsGetRequest = new IdrEnvClientsGetRequest();
        idrEnvClientsGetRequest.setEnv("idr_test");
        idrEnvClientsGetRequest.setDeviceId("idr_master_pc");
        ResponseText responseText = HttpUtils.doHttp(httpMode, url, idrEnvClientsGetRequest);
        if (!HttpConfig.isSuccessful(responseText.getStatus())) {
            throw new RuntimeException("获取设备IP地址失败");
        }
        if (null == responseText.getResultObject() || !(responseText.getResultObject() instanceof IdrEnvClientsGetResponse)) {
            throw new RuntimeException("获取设备IP地址失败");
        }
        IdrEnvClientsGetResponse idrEnvClientsGetResponse = (IdrEnvClientsGetResponse) responseText.getResultObject();
        if (ListUtils.isEmpty(idrEnvClientsGetResponse.getItems())) {
            throw new RuntimeException("获取设备IP地址失败");
        }

        return idrEnvClientsGetResponse;

    }
}
