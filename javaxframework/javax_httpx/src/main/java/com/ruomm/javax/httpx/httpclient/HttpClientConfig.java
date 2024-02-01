/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2016年1月6日 上午11:43:43
 */
package com.ruomm.javax.httpx.httpclient;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AsyncHttpClient请求配置，提供网络请求Client配置，网络请求参数设置等
 *
 * @author Ruby
 */
@SuppressWarnings("deprecation")
public class HttpClientConfig {
    private final static Log log = LogFactory.getLog(HttpClientConfig.class);
    // 连接超时时间
    public final static int SET_connetionTimeOut = 20 * 1000;
    // sock超时时间
    public final static int SET_sockTimeOut = 120 * 1000;
    // 请求超时时间
    public final static int SET_connectionRequestTimeout = 60 * 1000;

    public static CloseableHttpClient getDefaultHttpClient() {
        try {
            TrustStrategy trustStrategy = new TrustStrategy() {

                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    // TODO Auto-generated method stub
                    return true;
                }
            };
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, trustStrategy).build();
            // 有效的SSL会话并匹配到目标主机。
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            log.error("Error:getDefaultHttpClient", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("Error:getDefaultHttpClient", e);
        } catch (KeyStoreException e) {
            log.error("Error:getDefaultHttpClient", e);
        }
        return HttpClients.createDefault();
    }

    public static CloseableHttpClient getUnSafeHttpClient() {
        try {

            TrustStrategy trustStrategy = new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            };
            // 使用 loadTrustMaterial() 方法实现一个信任策略，信任所有证书

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, trustStrategy).build();
            sslContext.setDefault(SSLContext.getInstance("TLSv1.2"));
            // NoopHostnameVerifier类: 作为主机名验证工具，实质上关闭了主机名验证，它接受任何
            // 有效的SSL会话并匹配到目标主机。
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }
//		catch (KeyManagementException e) {
//			log.error("Error:getUnSafeHttpClient",e);
//			throw new RuntimeException(e);
//		} catch (NoSuchAlgorithmException e) {
//			log.error("Error:getUnSafeHttpClient",e);
//			throw new RuntimeException(e);
//		} catch (KeyStoreException e) {
//			log.error("Error:getUnSafeHttpClient",e);
//			throw new RuntimeException(e);
//		}
        catch (Exception e) {
            throw new RuntimeException(e);
        }
//		return HttpClients.createDefault();

    }
//	public static CloseableHttpClient getUnSafeHttpClient() {
//		try {
//			// Create a trust manager that does not validate certificate chains
//			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//				@Override
//				public void checkClientTrusted(X509Certificate[] certs, String authType) {
//				}
//
//				@Override
//				public void checkServerTrusted(X509Certificate[] certs, String authType) {
//				}
//
//				@Override
//				public X509Certificate[] getAcceptedIssuers() {
//					return new X509Certificate[] {};
//				}
//			} };
//			// Install the all-trusting trust manager
//			final SSLContext sslContext = SSLContext.getInstance("TLS");
//			sslContext.init(null, trustAllCerts, null);
//			// Create an ssl socket factory with our all-trusting manager
//			SSLSocketFactory sf = new SSLSocketFactory(sslContext);
//			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
////			Scheme https = new Scheme("https", sf, 443);
////			return HttpClients.custom().setHostnameVerifier(new NullHostNameVerifier()).setSSLSocketFactory(sf).build();
//			return HttpClients.custom().setSSLSocketFactory(sf).build();
//		}
//		catch (Exception e) {
//			log.error("Error:getUnSafeHttpClient",e);
//			throw new RuntimeException(e);
//		}
//	}

    public static CloseableHttpClient getSafeHttpClient(String caFile) {
        InputStream in = null;
        try {
            in = new FileInputStream(caFile);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log.error("Error:getSafeHttpClient", e);
            in = null;
        }
        return getSafeHttpClient(in);
    }

    public static CloseableHttpClient getSafeHttpClient(InputStream in) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(in);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(keystore, new TrustSelfSignedStrategy())
                    .build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);
            return HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();

        }
//		catch (CertificateException e) {
//			log.error("Error:getSafeHttpClient",e);
//			throw new RuntimeException(e);
//		}
//		catch (IOException e) {
//			log.error("Error:getSafeHttpClient",e);
//			throw new RuntimeException(e);
//		}
//		catch (KeyManagementException e) {
//			log.error("Error:getSafeHttpClient",e);
//			throw new RuntimeException(e);
//		} catch (NoSuchAlgorithmException e) {
//			log.error("Error:getSafeHttpClient",e);
//			throw new RuntimeException(e);
//		} catch (KeyStoreException e) {
//			log.error("Error:getSafeHttpClient",e);
//			throw new RuntimeException(e);
//		}
        catch (Exception e) {
            log.error("Error:getSafeHttpClient", e);
            throw new RuntimeException(e);
        }
    }

    // 构建Post FormBody请求
    public static HttpEntity createPostFormBody(Map<String, ?> mapObject) {
        return createPostFormBody(mapObject, null, false);
    }

    public static HttpEntity createPostFormBody(Map<String, ?> mapObject, String charsetName) {
        return createPostFormBody(mapObject, charsetName, false);
    }

    public static HttpEntity createPostFormBody(Map<String, ?> mapObject, String charsetName, boolean isPutEmpty) {
//		Charset charset = UtilHttp.parseRealCharset(charsetName, Charset.forName("UTF-8"));
        Charset charset = CharsetUtils.parseRealCharset(charsetName);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
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
            formparams.add(new BasicNameValuePair(key, StringUtils.nullStrToEmpty(valStr)));
        }
        UrlEncodedFormEntity uefEntity = null;
        try {
            if (null == charset) {
                uefEntity = new UrlEncodedFormEntity(formparams);
            } else {
                uefEntity = new UrlEncodedFormEntity(formparams, charset);
            }
        } catch (Exception e) {
            log.error("Error:createPostFormBody", e);
            uefEntity = null;
        }
        return uefEntity;
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