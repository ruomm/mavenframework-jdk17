/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2016年1月6日 上午11:43:43
 */
package com.ruomm.javax.httpx.httpurlconnect;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.httpurlconnect.core.UrlConnectClient;
import com.ruomm.javax.httpx.httpurlconnect.core.UrlConnectClientTrustAll;
import com.ruomm.javax.httpx.httpurlconnect.dal.UrlRequestEntry;
import com.ruomm.javax.httpx.httpurlconnect.dal.UrlStringReqEntry;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * AsyncHttpClient请求配置，提供网络请求Client配置，网络请求参数设置等
 *
 * @author Ruby
 */
public class HttpUrlConnectConfig {
    private final static Log log = LogFactory.getLog(HttpUrlConnectConfig.class);
    // 连接超时时间
    public final static int SET_connetionTimeOut = 20 * 1000;
    // 请求超时时间
    public final static int SET_readTimeout = 60 * 1000;

    private static UrlConnectClient DEFAULT_URL_CONNECT_CLIENT = null;

    public static UrlConnectClient getDefaultClient() {
//        return new UrlConnectClientTrustAll();
        if(null == DEFAULT_URL_CONNECT_CLIENT){
            DEFAULT_URL_CONNECT_CLIENT = new UrlConnectClientTrustAll();
        }
        return DEFAULT_URL_CONNECT_CLIENT;
    }

    // 构建Post FormBody请求
    public static UrlRequestEntry createPostFormBody(Map<String, ?> mapObject) {
        return createPostFormBody(mapObject, null, false);
    }

    public static UrlRequestEntry createPostFormBody(Map<String, ?> mapObject, String charsetName) {
        return createPostFormBody(mapObject, charsetName, false);
    }

    @SuppressWarnings("deprecation")
    public static UrlRequestEntry createPostFormBody(Map<String, ?> mapObject, String charsetName, boolean isPutEmpty) {
//		Charset charset = UtilHttp.parseRealCharset(charsetName, Charset.forName("UTF-8"));
//		Charset charset = UtilHttp.parseRealCharset(charsetName);

        String realCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "utf-8");
        StringBuilder sbText = new StringBuilder();
        Set<String> keySets = mapObject.keySet();
        boolean isFlag = true;
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
            try {
                if (sbText.length() > 0) {
                    sbText.append("&");
                }

//				sbText.append(key).append("=")
//						.append(URLEncoder.encode(JavaxHttpxUtils.nullStrToEmpty(valStr), realCharsetName));
                sbText.append(key).append("=");
                if (StringUtils.isEmpty(realCharsetName)) {
                    sbText.append(URLEncoder.encode(StringUtils.nullStrToEmpty(valStr)));
                } else {
                    sbText.append(URLEncoder.encode(StringUtils.nullStrToEmpty(valStr), realCharsetName));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                log.error("Error:createPostFormBody", e);
                isFlag = false;
            }
            if (!isFlag) {
                break;
            }
        }
        if (!isFlag) {
            return null;
        }
        UrlStringReqEntry urlStringReqEntry = new UrlStringReqEntry();
        urlStringReqEntry.setText(sbText.toString());
        urlStringReqEntry.setMimeType("application/x-www-form-urlencoded");
        urlStringReqEntry.setCharset(realCharsetName);
        return urlStringReqEntry;
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