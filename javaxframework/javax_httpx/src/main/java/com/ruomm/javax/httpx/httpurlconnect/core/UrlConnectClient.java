/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月16日 上午11:20:43
 */
package com.ruomm.javax.httpx.httpurlconnect.core;

import java.net.HttpURLConnection;

public interface UrlConnectClient {
    public HttpURLConnection create(String url, boolean isPost, int connectTimeOut, int readTimeOut, boolean keepAlive);
}