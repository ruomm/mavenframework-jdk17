package com.ruomm.javax.httpx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.dal.HttpxMime;
import com.ruomm.javax.httpx.dal.HttpxMultipart;
import com.ruomm.javax.httpx.util.JavaxHttpxUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpConfig {
    private final static Log log = LogFactory.getLog(HttpConfig.class);

    /**
     * 日志输出级别枚举类
     * <p>
     * NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     */
    public enum LoggerLevel {
        OFF, DEBUG, INFO
    }

    /**
     * Http请求通用日志输出级别设置
     * <p>
     * NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     *
     * @param LOGGER_LEVEL 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     */
    private static LoggerLevel HTTP_COMMON_LOGGER_LEVEL = LoggerLevel.INFO;
    public static int LOG_SIZE_SHOW_MAX = 1024 * 5;
    public static int LOG_SIZE_MD5_MAX = 1024 * 10;
    public final static int CODE_CACHE_OK = 200;
    public final static int CODE_NO_SEND = -200;
    public final static int CODE_SEND_ERR = -400;
    public final static int CODE_FAIL = -404;
    public final static int CODE_FILE_ERR = -410;
    public final static int CODE_FILE_EXIST = -411;
    public final static int CODE_EXCEPTION = -499;
    public final static int CODE_FILTER = -999;
    public final static boolean IS_ADNROID;

    static {
        boolean isAndroid = false;
        try {
            Class<?> cls1 = Class.forName("android.text.TextUtils");
            Class<?> cls = Class.forName("android.os.Build");
            if (null != cls && null != cls1) {
                cls1 = null;
                cls = null;
                isAndroid = true;

            }
        } catch (Exception e) {
//			log.error("Error:",e);
        }
        IS_ADNROID = isAndroid;
    }

    private static String UPLOAD_PATH = null;

    /**
     * 获取文件上传时候临时文件夹路径
     *
     * @param uploadPath 文件上传时候临时文件夹路径
     */
    public static void configUploadPath(String uploadPath) {
        UPLOAD_PATH = uploadPath;
    }

    /**
     * 解析文件上传时候临时文件夹路径
     *
     * @return 文件上传时候临时文件夹路径
     */
    public static String parseUploadPath() {
        return JavaxHttpxUtils.parseHttpxUploadPath(UPLOAD_PATH);
    }

    /**
     * Http请求通用日志输出级别设置
     * <p>
     * NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     *
     * @param loggerLevel 日志输出级别设置。NONE不输出日志，DEBUG输出DEBUG日志，INFO输出INFO日志。
     */
    public static void configLoggerLevel(LoggerLevel loggerLevel) {
        HTTP_COMMON_LOGGER_LEVEL = loggerLevel;
    }

    /**
     * 配置日志输出时候最大显示的内容长度
     *
     * @param showMax 最大显示长度，此值小于等于0则不限制，如是超过此值则截取到showMax的长度，
     * @param md5Max  MD5模式最大显示长度，如是超过此值则显示MD5值长度，如是md5Max不大于showMax则是截取模式。
     */
    public static void configLoggerPrint(int showMax, int md5Max) {
//		int showMaxReal;
//		if(showMax>0){
//			showMaxReal = showMax;
//		} else {
//			showMaxReal = 1024*5;
//		}
//		int md5MaxReal;
//		if(md5Max>showMaxReal){
//			md5MaxReal = md5Max;
//		} else {
//			md5MaxReal = showMaxReal+1024*5;
//		}
//		LOG_SIZE_SHOW_MAX = showMaxReal;
//		LOG_SIZE_MD5_MAX = md5MaxReal;
        LOG_SIZE_SHOW_MAX = showMax;
        LOG_SIZE_MD5_MAX = md5Max;
    }

    /**
     * Http请求通用日志输出级别获取
     *
     * @return Http请求通用日志输出级别
     */
    public static LoggerLevel getLoggerLevel() {
        return HTTP_COMMON_LOGGER_LEVEL;
    }

    public static boolean isSuccessful(int status) {
        return status >= 200 && status < 300;
    }

    // 获取真实的Get请求路径
    public static String createGetRequest(String url, Map<String, ?> map) {
        return createGetRequest(url, map, null, false);
    }

    public static String createGetRequest(String url, Map<String, ?> map, String charsetName) {
        return createGetRequest(url, map, charsetName, false);
    }

    public static String createGetRequest(String url, Map<String, ?> map, String charsetName, boolean isPutEmpty) {
        if (null == map || map.size() <= 0) {
            return url;
        }
        if (StringUtils.isEmpty(url)) {
            return createFormUrlEncodedRequest(map, charsetName, isPutEmpty);
        } else if (url.contains("?")) {
            if (url.endsWith("?")) {
                return url + createFormUrlEncodedRequest(map, charsetName, isPutEmpty);
            } else {
                return url + "&" + createFormUrlEncodedRequest(map, charsetName, isPutEmpty);
            }
        } else {
            return url + "?" + createFormUrlEncodedRequest(map, charsetName, isPutEmpty);
        }
    }

    // 获取post请求字符串内容
    public static String createPostRequest(Map<String, ?> map) {
        return createFormUrlEncodedRequest(map, null, false);
    }

    public static String createPostRequest(Map<String, ?> map, String charsetName) {
        return createFormUrlEncodedRequest(map, charsetName, false);
    }

    public static String createPostRequest(Map<String, ?> map, String charsetName, boolean isPutEmpty) {
        return createFormUrlEncodedRequest(map, charsetName, isPutEmpty);
    }

    // 构建Get请求参数
    private static String createFormUrlEncodedRequest(Map<String, ?> mapObject, String charsetName,
                                                      boolean isPutEmpty) {
        if (null == mapObject || mapObject.size() <= 0) {
            return "";
        }
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        if (null != mapObject && mapObject.size() > 0) {
            // 先将参数以其参数名的字典序升序进行排序
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
                    log.error("Error:createGetRequestBody", e);
                    valStr = null;
                }
                if (StringUtils.isEmpty(valStr) && !isPutEmpty) {
                    continue;
                }
                if (basestring.length() > 0) {
                    basestring.append("&");
                }
                String encodeVal = null;
                try {
                    if (StringUtils.isEmpty(valStr)) {
                        encodeVal = "";
                    } else {
                        String charset = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
                        encodeVal = URLEncoder.encode(valStr, charset);
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:createGetRequestBody", e);
                    encodeVal = "";
                }
                basestring.append(key + "=" + encodeVal);
            }
        }
        return basestring.length() > 0 ? basestring.toString() : "";
    }

    /**
     * 依据字符串自动生成RequestContent </br>
     * 若是contentType不为空，依据contentType生成，若是charsetName不为空且有效，charset为charsetName，否则取contentType的charset </br>
     * 若是contentType为空，依据postStr类型和charsetName智能判断生成 </br>
     * 标准xml格式是application/xml，非标准格式xml为text/xml,JSON格式是application/json,</br>
     * form-url格式是application/x-www-form-urlencoded,其他格式为text/plain</br>
     * 若charsetName真实有效，contentType编码为charsetName的编码</br>
     * 若是charsetName不有效，</br>
     * json格式默认为utf-8编码 </br>
     * 标准xml格式取xml文本编码，若是取不到设置utf-8编码 ，取到且有效使用此编码，取到但是无效，不使用编码 </br>
     * 非标准xml格式默认为不使用编码 </br>
     * form-url格式默认为utf-8编码</br>
     * 其他格式默认为不使用编码
     *
     * @param postStr     请求字符串
     * @param charsetName 编码方式
     * @param contentType Content-Type类型
     * @return
     */
    public static HttpxMime parseRequestMimeNew(String postStr, String contentType, String charsetName) {
        if (null == postStr) {
            return null;
        }
        if (null == contentType || contentType.length() <= 0) {
            HttpxMime httpxMime = null;
            try {
                if (postStr.length() <= 0) {
                    httpxMime = new HttpxMime();
                    httpxMime.setMimeType("text/plain");
                    // 默认编码修订为UTF-8
                    httpxMime.setCharset(CharsetUtils.parseRealCharsetName(charsetName, "utf-8"));
                } else {
                    String postBodyTrim = postStr.trim();
                    if (isXmlStr(postBodyTrim)) {
                        httpxMime = parseMimeByXml(postBodyTrim, charsetName);
                    } else if (postBodyTrim.startsWith("<") && postBodyTrim.endsWith(">")) {
                        httpxMime = new HttpxMime();
                        httpxMime.setMimeType("text/xml");
                        // 默认编码修订为UTF-8
                        httpxMime.setCharset(CharsetUtils.parseRealCharsetName(charsetName, "utf-8"));
                    } else if (postBodyTrim.startsWith("{") && postBodyTrim.endsWith("}")) {
                        httpxMime = new HttpxMime();
                        httpxMime.setMimeType("application/json");
                        httpxMime.setCharset(CharsetUtils.parseRealCharsetName(charsetName, "utf-8"));
                    } else if (postBodyTrim.startsWith("[") && postBodyTrim.endsWith("]")) {
                        httpxMime = new HttpxMime();
                        httpxMime.setMimeType("application/json");
                        httpxMime.setCharset(CharsetUtils.parseRealCharsetName(charsetName, "utf-8"));
                    } else if (isFormUrlEncode(postStr)) {
                        httpxMime = new HttpxMime();
                        httpxMime.setMimeType("application/x-www-form-urlencoded");
                        httpxMime.setCharset(null);
                    } else {
                        httpxMime = new HttpxMime();
                        httpxMime.setMimeType("text/plain");
                        // 默认编码修订为UTF-8
                        httpxMime.setCharset(CharsetUtils.parseRealCharsetName(charsetName, "utf-8"));
                    }
                    if (StringUtils.isEmpty(httpxMime.getCharset())) {
                        httpxMime.setMimeTypeWithCharset(httpxMime.getMimeType());
                    } else {
                        httpxMime
                                .setMimeTypeWithCharset(httpxMime.getMimeType() + ";charset=" + httpxMime.getCharset());
                    }
                }
            } catch (Exception e) {
                log.error("Error:parseRequestMimeNew", e);
                httpxMime = null;
            }
            return httpxMime;
        } else {
            HttpxMime requestContent = parseMimeByType(contentType, charsetName);
            return requestContent;
        }
    }

    private static boolean isXmlStr(String postStr) {
        if (null == postStr) {
            return false;
        }
        String postBodyTrim = postStr.toLowerCase();
        if (postBodyTrim.startsWith("<?xml") && postBodyTrim.endsWith(">")) {
            int index = postBodyTrim.indexOf("?>");
            if (index > 0 && index < 100) {
                return true;
            }
        }
        return false;
    }

    private static boolean isFormUrlEncode(String postStr) {
        if (null == postStr) {
            return false;
        }
        String[] datas = postStr.split("&");
        int total = datas.length;
        int count = 0;
        for (String tmp : datas) {
            if (null == tmp || tmp.length() <= 0) {
                continue;
            }
            int firstIndex = tmp.indexOf("=");
            if (firstIndex <= 0) {
                continue;
            }
            int lastIndex = tmp.lastIndexOf("=");

            if (firstIndex != lastIndex) {
                continue;
            }
            count++;
        }
        return count == total;
//		if (total <= 3) {
//			return count == total;
//		}
//		else {
//			return count >= total * 2 / 3;
//		}
    }

    private static HttpxMime parseMimeByXml(String postStr, String charsetName) {

        String contentCharsetName = CharsetUtils.parseRealCharsetName(charsetName, null);
        int index = postStr.toLowerCase().indexOf("?>");
        if (index <= 0) {
            HttpxMime requestContent = new HttpxMime();
            requestContent.setMimeType("text/xml");
            requestContent.setCharset(contentCharsetName);
            return requestContent;
        }
        if (!StringUtils.isEmpty(contentCharsetName)) {
            HttpxMime requestContent = new HttpxMime();
            requestContent.setMimeType("application/xml");
            requestContent.setCharset(contentCharsetName);
            return requestContent;
        }

        String ecodingCharset = null;
        try {
            String xmlHeader = postStr.substring(0, index + 2);
            int indexEncoding = xmlHeader.toLowerCase().indexOf("encoding=\"");
            String ecodingStr = indexEncoding > 0 ? xmlHeader.substring(indexEncoding + 10) : "";
            int sysmolIndex = ecodingStr.indexOf("\"");
            ecodingCharset = indexEncoding > 0 ? ecodingStr.substring(0, sysmolIndex) : "";
            if (ecodingCharset.equalsIgnoreCase("ISO-10646-UCS-4")) {
                ecodingCharset = "UCS-4";
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parseMimeByXml", e);
            ecodingCharset = null;
        }
        HttpxMime requestContent = new HttpxMime();
        if (null == ecodingCharset || ecodingCharset.length() <= 0) {
            requestContent.setMimeType("application/xml");
            requestContent.setCharset(CharsetUtils.parseRealCharsetName(charsetName, "utf-8"));
        } else {
            requestContent.setMimeType("application/xml");
            requestContent.setCharset(ecodingCharset);
        }
        return requestContent;
    }

    private static HttpxMime parseMimeByType(String contentType, String charsetName) {
        if (null == contentType || contentType.length() <= 0) {
            return null;
        }
        String defCharsetName = CharsetUtils.parseRealCharsetName(charsetName, null);
        HttpxMime httpxMime = null;
        try {
            StringBuilder sbContent = new StringBuilder();
            String parseCharsetName = null;
            String[] datas = contentType.split(";");
            for (String tmp : datas) {
                if (StringUtils.isEmpty(tmp)) {
                    continue;
                }
                String tmpCharsetName = null;
                try {
                    if (tmp.trim().toLowerCase().startsWith("charset")) {
                        String[] charDatas = tmp.split("=");
                        if (null != charDatas && charDatas.length == 2 && null != charDatas[1]
                                && charDatas[1].length() > 0) {
                            tmpCharsetName = charDatas[1];
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:parseMimeByType", e);
                    tmpCharsetName = null;
                }
                if (StringUtils.isEmpty(tmpCharsetName)) {
                    if (sbContent.length() > 0) {
                        sbContent.append(";");
                    }
                    sbContent.append(tmp);
                } else {
                    parseCharsetName = tmpCharsetName;
                }
            }
            httpxMime = new HttpxMime();
            if (StringUtils.isEmpty(defCharsetName)) {
                httpxMime.setCharset(parseCharsetName);
                httpxMime.setMimeTypeWithCharset(contentType);
            } else {
                httpxMime.setCharset(defCharsetName);
                httpxMime.setMimeTypeWithCharset(sbContent.toString() + ";charset=" + defCharsetName);
            }
            httpxMime.setMimeType(sbContent.toString());
            return httpxMime;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parseMimeByType", e);
            httpxMime = null;
        }

        return httpxMime;
    }

    public static String parseTLSVersion(String tlsVersion) {
        if (null == tlsVersion || tlsVersion.length() <= 0) {
            return "TLS";
        } else if (tlsVersion.equalsIgnoreCase("tlsv1.0") || tlsVersion.equalsIgnoreCase("tls1.0")
                || tlsVersion.equalsIgnoreCase("v1.0") || tlsVersion.equalsIgnoreCase("1.0")) {
            return "TLSv1.0";
        } else if (tlsVersion.equalsIgnoreCase("tlsv1.1") || tlsVersion.equalsIgnoreCase("tls1.1")
                || tlsVersion.equalsIgnoreCase("v1.1") || tlsVersion.equalsIgnoreCase("1.1")) {
            return "TLSv1.1";
        } else if (tlsVersion.equalsIgnoreCase("tlsv1.2") || tlsVersion.equalsIgnoreCase("tls1.2")
                || tlsVersion.equalsIgnoreCase("v1.2") || tlsVersion.equalsIgnoreCase("1.2")) {
            return "TLSv1.2";
        } else if (tlsVersion.equalsIgnoreCase("tlsv1.3") || tlsVersion.equalsIgnoreCase("tls1.3")
                || tlsVersion.equalsIgnoreCase("v1.3") || tlsVersion.equalsIgnoreCase("1.3")) {
            return "TLSv1.3";
//		} else if (tlsVersion.equalsIgnoreCase("tlsv1.4") || tlsVersion.equalsIgnoreCase("tls1.4")
//				|| tlsVersion.equalsIgnoreCase("v1.4") || tlsVersion.equalsIgnoreCase("1.4")) {
//			return "TLSv1.4";
//		} else if (tlsVersion.equalsIgnoreCase("tlsv1.5") || tlsVersion.equalsIgnoreCase("tls1.5")
//				|| tlsVersion.equalsIgnoreCase("v1.5") || tlsVersion.equalsIgnoreCase("1.5")) {
//			return "TLSv1.5";
        } else {
            return tlsVersion;
        }
    }

    public static void deleteTmpUploadFile(List<HttpxMultipart> httpxMultipartList) {
        if (null == httpxMultipartList || httpxMultipartList.size() <= 0) {
            return;
        }
        for (HttpxMultipart httpxMultipart : httpxMultipartList) {
            if (null == httpxMultipartList || null == httpxMultipart.getDeleteFileLists() || httpxMultipart.getDeleteFileLists().size() <= 0) {
                continue;
            }
            for (File tmpFile : httpxMultipart.getDeleteFileLists()) {
                if (null == tmpFile || !tmpFile.exists() || !tmpFile.isFile()) {
                    return;
                }
                try {
                    tmpFile.delete();
                } catch (Exception e) {
                    log.error("Error:deleteTmpUploadFile", e);
                }
            }
        }
    }


}