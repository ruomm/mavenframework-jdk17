package com.ruomm.javax.httpx.okhttp;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author yw
 * @version V1.0.0
 * @date 2016/10/13 11:08
 */
public class NullHostNameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;

    }
}