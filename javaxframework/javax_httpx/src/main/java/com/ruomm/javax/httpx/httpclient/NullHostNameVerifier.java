package com.ruomm.javax.httpx.httpclient;

import org.apache.http.conn.ssl.X509HostnameVerifier;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * @author yw
 * @version V1.0.0
 * @date 2016/10/13 11:08
 */
@SuppressWarnings("deprecation")
public class NullHostNameVerifier implements X509HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;

    }

    @Override
    public void verify(String host, SSLSocket ssl) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void verify(String host, X509Certificate cert) throws SSLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
        // TODO Auto-generated method stub

    }
}