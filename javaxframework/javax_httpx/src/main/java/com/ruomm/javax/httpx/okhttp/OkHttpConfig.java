/**
 * @copyright wanruome-2017
 * @author 牛牛-wanruome@163.com
 * @create 2017年3月23日 下午4:54:13
 */
package com.ruomm.javax.httpx.okhttp;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import okhttp3.FormBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class OkHttpConfig {
    private final static Log log = LogFactory.getLog(OkHttpConfig.class);
    //	public final static String encoding = "UTF-8";
    public final static long OKHTTP_CONNECT_TIMEOUT = 20l;
    public final static long OKHTTP_WRITE_TIMEOUT = 30l;
    public final static long OKHTTP_READ_TIMEOUT = 30l;
    private static OkHttpClient mOkHttpClient;

    public static OkHttpClient getDefaultHttpClient() {
        if (null == mOkHttpClient) {
//			mOkHttpClient = new OkHttpClient();
//			okhttp3.OkHttpClient.Builder builder = mOkHttpClient.newBuilder()
//					.connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
//					.writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
//					.readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS);
//			mOkHttpClient = builder.build();
            mOkHttpClient = new OkHttpClient().newBuilder().connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS).build();
        } else {
            mOkHttpClient = mOkHttpClient.newBuilder().connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS).build();
        }
        return mOkHttpClient;
    }

    @SuppressWarnings("deprecation")
    public static OkHttpClient getUnSafeHttpClient(long connectTimeOut, long writeTimeOut, long readTimeOut) {
        return getUnSafeHttpClient(null, connectTimeOut, writeTimeOut, readTimeOut);
    }

    @SuppressWarnings("deprecation")
    public static OkHttpClient getUnSafeHttpClient(String tlsVersion, long connectTimeOut, long writeTimeOut,
                                                   long readTimeOut) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance(HttpConfig.parseTLSVersion(tlsVersion));
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // Create a trust manager that does not validate certificate chains
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.hostnameVerifier(new NullHostNameVerifier());
            builder.sslSocketFactory(sslSocketFactory);
            if (connectTimeOut > 0) {
                builder.connectTimeout(connectTimeOut, TimeUnit.SECONDS);
            } else {
                builder.connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
            }
            if (writeTimeOut > 0) {
                builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);
            } else {
                builder.writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);
            }
            if (readTimeOut > 0) {
                builder.readTimeout(readTimeOut, TimeUnit.SECONDS);
            } else {
                builder.readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS);
            }
            // builder.retryOnConnectionFailure(false);
            // builder.followRedirects(false);
            // builder.followSslRedirects(false);
            mOkHttpClient = builder.build();
            return mOkHttpClient;
        } catch (Exception e) {
            log.error("Error:getUnSafeHttpClient", e);
            throw new RuntimeException(e);
        }
    }

    public static OkHttpClient getSafeHttpClient(String caFile, long connectTimeOut, long writeTimeOut,
                                                 long readTimeOut) {
        return getSafeHttpClient(null, caFile, connectTimeOut, writeTimeOut, readTimeOut);
    }

    public static OkHttpClient getSafeHttpClient(String tlsVersion, String caFile, long connectTimeOut,
                                                 long writeTimeOut, long readTimeOut) {
        InputStream in = null;
        try {
            in = new FileInputStream(caFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("Error:getUnSafeHttpClient", e);
            in = null;
        }
        return getSafeHttpClient(tlsVersion, in, connectTimeOut, writeTimeOut, readTimeOut);
    }

    public static OkHttpClient getSafeHttpClient(InputStream in, long connectTimeOut, long writeTimeOut,
                                                 long readTimeOut) {
        return getSafeHttpClient(null, in, connectTimeOut, writeTimeOut, readTimeOut);
    }

    @SuppressWarnings("deprecation")
    public static OkHttpClient getSafeHttpClient(String tlsVersion, InputStream in, long connectTimeOut,
                                                 long writeTimeOut, long readTimeOut) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(in);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keystore);
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance(HttpConfig.parseTLSVersion(tlsVersion));
            sslContext.init(null, tmf.getTrustManagers(), null);
            // Create an ssl socket factory with our all-trusting manager
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            // Create a trust manager that does not validate certificate chains
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.hostnameVerifier(new NullHostNameVerifier());
            builder.sslSocketFactory(sslSocketFactory);
            if (connectTimeOut > 0) {
                builder.connectTimeout(connectTimeOut, TimeUnit.SECONDS);
            } else {
                builder.connectTimeout(OKHTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
            }
            if (writeTimeOut > 0) {
                builder.writeTimeout(writeTimeOut, TimeUnit.SECONDS);
            } else {
                builder.writeTimeout(OKHTTP_WRITE_TIMEOUT, TimeUnit.SECONDS);
            }
            if (readTimeOut > 0) {
                builder.readTimeout(readTimeOut, TimeUnit.SECONDS);
            } else {
                builder.readTimeout(OKHTTP_READ_TIMEOUT, TimeUnit.SECONDS);
            }
            // builder.retryOnConnectionFailure(false);
            // builder.followRedirects(false);
            // builder.followSslRedirects(false);
            mOkHttpClient = builder.build();

        } catch (Exception e) {
            log.error("Error:getUnSafeHttpClient", e);
            mOkHttpClient = null;
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                log.error("Error:getUnSafeHttpClient", e);
            }
        }
        return mOkHttpClient;
    }

    // 依据对象自动构建Post 请求
//	public static RequestBody createPostBodyByObject(Object bodyParameters, String charsetName) {
//		RequestBody requestBody = null;
//		String stringBody = "";
//		if (bodyParameters != null) {
//
//			if (bodyParameters instanceof String) {
//				stringBody = (String) bodyParameters;
//				requestBody = UtilHttp.getRequestBodyByString(stringBody, charsetName, null, null);
//			}
//			else if (bodyParameters instanceof CharSequence) {
//				CharSequence charSequence = (CharSequence) bodyParameters;
//				stringBody = charSequence.toString();
//				requestBody = UtilHttp.getRequestBodyByString(stringBody, charsetName, null, null);
//			}
//			else {
//				stringBody = HttpXJSON.toJSONString(bodyParameters);
//				requestBody = UtilHttp.getRequestBodyByString(stringBody, charsetName, null, null);
//			}
//		}
//		return requestBody;
//	}

    // 构建Post FormBody请求
    public static RequestBody createPostFormBody(Map<String, ?> mapObject) {
        return createPostFormBody(mapObject, null, false);
    }

    public static RequestBody createPostFormBody(Map<String, ?> mapObject, String charsetName) {
        return createPostFormBody(mapObject, charsetName, false);
    }

    public static RequestBody createPostFormBody(Map<String, ?> mapObject, String charsetName, boolean isPutEmpty) {
        Charset charset = CharsetUtils.parseRealCharset(charsetName);
        Builder mBuilder = null == charset ? new Builder() : new Builder(charset);
        Set<String> keySets = mapObject.keySet();
        for (String key : keySets) {
            String valStr = null;
            try {
                Object objVal = mapObject.get(key);
                if (null != objVal) {
                    if (objVal instanceof String) {
                        valStr = (String) objVal;
                    } else if (objVal instanceof CharSequence) {
                        CharSequence charSequence = (CharSequence) objVal;
                        valStr = charSequence.toString();
                    } else {
                        valStr = String.valueOf(objVal);
                    }
                }
            } catch (Exception e) {
                log.error("Error:createPostFormBody", e);
                valStr = null;
            }
            if (StringUtils.isEmpty(valStr) && !isPutEmpty) {
                continue;
            }
            mBuilder.add(key, StringUtils.nullStrToEmpty(valStr));
        }
        return mBuilder.build();
    }

    // 获取真实的Get请求路径
    public static String createGetRequest(String url, Map<String, ?> map) {
        return HttpConfig.createGetRequest(url, map, null, false);
    }

    public static String createGetRequest(String url, Map<String, ?> map, String charsetName) {
        return HttpConfig.createGetRequest(url, map, charsetName, false);
    }

    public static String createGetRequest(String url, Map<String, ?> map, String charsetName, boolean isPutEmpty) {
        return HttpConfig.createGetRequest(url, map, charsetName, isPutEmpty);
    }

}