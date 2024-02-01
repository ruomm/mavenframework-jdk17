package com.ruomm.javax.httpx.test.tools;

import com.ruomm.javax.corex.RemoteUrlUtils;
import com.ruomm.javax.httpx.dal.ResponseText;
import com.ruomm.javax.httpx.httpclient.TextHttpClient;
import com.ruomm.javax.httpx.httpurlconnect.TextUrlConnect;
import com.ruomm.javax.httpx.okhttp.TextOKHttp;
import com.ruomm.javax.jsonx.XJSON;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/1/31 10:00
 */
public class HttpUtils {
    public static enum HTTP_MODE{
        URL,HTTPCLIENT,OKHTTP
    }
    public static ResponseText doHttp(HTTP_MODE httpMode,String serverUrl, Object requestObj) {
        String url = RemoteUrlUtils.parseRemoteUrl(serverUrl, requestObj);
        String desc = RemoteUrlUtils.getRemoteApiDesc(requestObj);
        ResponseText responseText;
        if(HTTP_MODE.HTTPCLIENT==httpMode){
            responseText = new TextHttpClient().setUrl(url).setRequestBody(XJSON.toJSONString(requestObj)).doHttpSync(RemoteUrlUtils.getRemoteTargetClass(requestObj));
        } else if(HTTP_MODE.OKHTTP==httpMode){
            responseText = new TextOKHttp().setUrl(url).setRequestBody(XJSON.toJSONString(requestObj)).doHttpSync(RemoteUrlUtils.getRemoteTargetClass(requestObj));
        } else if(HTTP_MODE.URL==httpMode){
            responseText = new TextUrlConnect().setUrl(url).setRequestBody(XJSON.toJSONString(requestObj)).doHttpSync(RemoteUrlUtils.getRemoteTargetClass(requestObj));
        } else {
            responseText = new TextUrlConnect().setUrl(url).setRequestBody(XJSON.toJSONString(requestObj)).doHttpSync(RemoteUrlUtils.getRemoteTargetClass(requestObj));
        }
        System.out.println(desc + "请求结果：" + responseText);
        return responseText;
    }
}
