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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class UrlConnectClientTrustSelf implements UrlConnectClient {
    private final static Log log = LogFactory.getLog(UrlConnectClientTrustSelf.class);
//	private final static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//		@Override
//		public X509Certificate[] getAcceptedIssuers() {
//			return null;
//		}
//
//		@Override
//		public void checkClientTrusted(X509Certificate[] certs, String authType) {
//		}
//
//		@Override
//		public void checkServerTrusted(X509Certificate[] certs, String authType) {
//		}
//	} };

    private final static HostnameVerifier NOT_VERYFY = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };
    private String tlsVersion = null;
    private String caFile = null;
    private InputStream caIn = null;

    public UrlConnectClientTrustSelf() {
        super();
    }

    public UrlConnectClientTrustSelf(String tlsVersion) {
        super();
        this.tlsVersion = tlsVersion;
    }

    public UrlConnectClientTrustSelf(String tlsVersion, String caFile, InputStream caIn) {
        super();
        this.tlsVersion = tlsVersion;
        this.caFile = caFile;
        this.caIn = caIn;
    }

    public String getTlsVersion() {
        return tlsVersion;
    }

    public void setTlsVersion(String tlsVersion) {
        this.tlsVersion = tlsVersion;
    }

    public String getCaFile() {
        return caFile;
    }

    public void setCaFile(String caFile) {
        this.caFile = caFile;
    }

    public InputStream getCaIn() {
        return caIn;
    }

    public void setCaIn(InputStream caIn) {
        this.caIn = caIn;
    }

    private void setSSLSocketFactory(String caFile, HttpsURLConnection conn) throws CertificateException,
            KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException, FileNotFoundException {
        setSSLSocketFactory(new FileInputStream(caFile), conn);
    }

    private void setSSLSocketFactory(InputStream in, HttpsURLConnection conn) throws CertificateException,
            KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca = cf.generateCertificate(in);

        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(null, null);
        keystore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keystore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance(HttpConfig.parseTLSVersion(this.tlsVersion));
        context.init(null, tmf.getTrustManagers(), null);
        conn.setHostnameVerifier(NOT_VERYFY);
        conn.setSSLSocketFactory(context.getSocketFactory());
    }

    @Override
    public HttpURLConnection create(String urlString, boolean isPost, int connectTimeOut, int readTimeOut, boolean keepAlive) {
        // TODO Auto-generated method stub
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            if (connection instanceof HttpsURLConnection) {
                if (null != caFile & caFile.length() > 0) {
                    setSSLSocketFactory(caFile, (HttpsURLConnection) connection);
                } else if (null != caIn) {
                    setSSLSocketFactory(caIn, (HttpsURLConnection) connection);

                }
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

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException | CertificateException
                 | KeyStoreException e) {
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
        }

        return connection;
    }

}