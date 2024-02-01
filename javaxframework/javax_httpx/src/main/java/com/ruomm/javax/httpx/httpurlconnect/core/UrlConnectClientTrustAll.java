/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月16日 上午11:23:55
 */
package com.ruomm.javax.httpx.httpurlconnect.core;

import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class UrlConnectClientTrustAll implements UrlConnectClient {
    private final static Log log = LogFactory.getLog(UrlConnectClientTrustAll.class);
    private final static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }};

    private final static HostnameVerifier NOT_VERYFY = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };
    private String tlsVersion = null;

    public UrlConnectClientTrustAll() {
        super();
    }

    public UrlConnectClientTrustAll(String tlsVersion) {
        super();
        this.tlsVersion = tlsVersion;
    }

    public String getTlsVersion() {
        return tlsVersion;
    }

    public void setTlsVersion(String tlsVersion) {
        this.tlsVersion = tlsVersion;
    }

    public void setSSLSocketFactory(HttpsURLConnection conns) throws NoSuchAlgorithmException, KeyManagementException {
        conns.setHostnameVerifier(NOT_VERYFY);
        SSLContext sc = SSLContext.getInstance(HttpConfig.parseTLSVersion(this.tlsVersion));
        sc.init(null, trustAllCerts, new SecureRandom());
        conns.setSSLSocketFactory(sc.getSocketFactory());
    }

    @Override
    public HttpURLConnection create(String urlString, boolean isPost, int connectTimeOut, int readTimeOut, boolean keepAlive) {
        // TODO Auto-generated method stub
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                setSSLSocketFactory((HttpsURLConnection) connection);
            }
            if (isPost) {
                connection.setRequestMethod("POST");
            } else {
                connection.setRequestMethod("GET");
            }
            connection.setInstanceFollowRedirects(true);
//			connection.setRequestProperty("Accept", "*/*");
//			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            if(keepAlive){
                connection.setRequestProperty("Connection", "keep-alive");
            } else {
                connection.setRequestProperty("Connection", "close");
            }
            connection.setConnectTimeout(connectTimeOut);
            connection.setReadTimeout(readTimeOut);

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            // TODO Auto-generated catch block
            log.error("Error:create", e);
            if (null != connection) {
                try {
                    connection.disconnect();
                } catch (Exception e2) {
                    // TODO: handle exception
                    log.error("Error:create", e2);
                }
            }
            connection = null;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:create", e);
            if (null != connection) {
                try {
                    connection.disconnect();
                } catch (Exception e2) {
                    // TODO: handle exception
                    log.error("Error:create", e2);
                }
            }
            connection = null;
        }

        return connection;
    }

}