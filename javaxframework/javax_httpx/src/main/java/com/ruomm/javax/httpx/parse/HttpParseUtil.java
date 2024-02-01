package com.ruomm.javax.httpx.parse;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.httpx.HttpConfig;
import com.ruomm.javax.httpx.annotation.DefHttpParseFormat;
import com.ruomm.javax.httpx.config.ResponseParseData;
import com.ruomm.javax.httpx.config.ResponseParseText;
import com.ruomm.javax.httpx.util.LoggerHttpUtils;
import com.ruomm.javax.jsonx.XJSON;
import com.ruomm.javax.jsonx.XUrlEncodedParse;
import com.ruomm.javax.jsonx.XXML;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpParseUtil {
    private final static Log log = LogFactory.getLog(HttpParseUtil.class);
    private static ResponseParseData CommonResponseParseData = null;
    private static ResponseParseText CommonResponseParseText = null;
    public final static String PARSE_FORMAT_JSON = "json";
    public final static String PARSE_FORMAT_XML = "xml";
    public final static String PARSE_FORMAT_URLENCODE = "urlencoded";
//	public final static String PARSE_FORMAT_XML_NOHEADER = "XML_NOHEADER";

    public static ResponseParseData getCommonResponseParseData() {
        if (null != CommonResponseParseData) {
            return CommonResponseParseData;
        }
//		CommonResponseParse = new ResponseParse() {
//
//			@Override
//			public Object parseResponseData(byte[] resultData, Class<?> cls, String respCharsetName,
//					String respFormat) {
//				// TODO Auto-generated method stub
//				log.debug("调用通用解析工具");
//				return HttpParseUtil.parseResponseData(resultData, cls, respCharsetName, respFormat);
//			}
//		};
        return CommonResponseParseData;
    }

    public static ResponseParseText getCommonResponseParseText() {
        if (null != CommonResponseParseText) {
            return CommonResponseParseText;
        }
        return CommonResponseParseText;
    }

    public static void setCommonResponseParseData(ResponseParseData CommonResponseParse) {
        HttpParseUtil.CommonResponseParseData = CommonResponseParse;
    }

    public static void setCommonResponseParseText(ResponseParseText CommonResponseParse) {
        HttpParseUtil.CommonResponseParseText = CommonResponseParse;
    }

    //
//	public static Object parseResponseData(byte[] resourceByte, Class<?> cls, String charsetName, String respFormat) {
//
//		return parseResponseDataCommon(resourceByte, cls, charsetName, respFormat);
//
//	}
    public static Object parseResponseText(String respStr, Class<?> cls, String respCharsetName, String respFormat,
                                           HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "响应内容->" + respStr);

        if (null == cls) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析错误->解析类没有设置");
            return respStr;
        }
        if (cls == String.class) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类定义为String.class，响应内容直接解析成String类型");
            return respStr;
        } else if (XJSON.isJSONClass(cls)) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类定义为" + cls.getName() + "，使用" + XJSON.getWorkJsonName(cls) + "解析成"
                    + cls.getSimpleName() + "类型");
            return XJSON.parse(getRealTrimString(respStr), cls);
        }
        // 获取目标类的注解
        Method method = null;
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->获取类注解DefHttpParseFormat");
        DefHttpParseFormat parseFormat = cls.getAnnotation(DefHttpParseFormat.class);
        if (null != parseFormat && !StringUtils.isEmpty(parseFormat.parseMethod())) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->获取类注解parseMethod的解析方法");
            if (null != cls) {
                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), String.class);
                    } catch (Exception e) {
                        method = null;
                    }
                }
                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), byte[].class, String.class);
                    } catch (Exception e) {
                        method = null;
                    }
                }

                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), byte[].class);
                    } catch (Exception e) {
                        method = null;
                    }
                }
                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), String.class, String.class);
                    } catch (Exception e) {
                        method = null;
                    }
                }
            }

        }

        if (null == method) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->没有自定义解析方法，依据定义进行解析。解析优先级：类注解format>Content-Type格式>智能判断格式");
            // 若是JSON自动解析为JSON
            String realTrimStr = getRealTrimString(respStr);
            if (null == realTrimStr || realTrimStr.length() <= 0) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->内容为空，返回NULL");
                return null;
            }
            if (null != parseFormat && (PARSE_FORMAT_JSON.equalsIgnoreCase(parseFormat.format()))) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类注解format为JSON格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            }
            // else if (PARSE_FORMAT_XML.equalsIgnoreCase(parseFormat.format())
            // || PARSE_FORMAT_XML_NOHEADER.equalsIgnoreCase(parseFormat.format())) {
            // return parseTextToXML(getRealJsonString(responseStr), cls);
            // }
            else if (null != parseFormat && PARSE_FORMAT_XML.equalsIgnoreCase(parseFormat.format())) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类注解format为XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (null != parseFormat && PARSE_FORMAT_URLENCODE.equalsIgnoreCase(parseFormat.format())) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类注解format为urlencode格式，使用URLDecoder进行decoder解析");
                return parseTextToUrlEncode(realTrimStr, cls, respCharsetName);
            } else if (!StringUtils.isEmpty(respFormat) && PARSE_FORMAT_JSON.equalsIgnoreCase(respFormat)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag,
                        "解析执行->请求声明parseFormat或响应Content-Type为JSON格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            } else if (!StringUtils.isEmpty(respFormat) && PARSE_FORMAT_XML.equalsIgnoreCase(respFormat)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->请求声明parseFormat或响应Content-Type为XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (!StringUtils.isEmpty(respFormat) && respFormat.contains(PARSE_FORMAT_URLENCODE)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->请求声明parseFormat或响应Content-Type为urlencode格式，使用URLDecoder进行decoder解析");
                return parseTextToUrlEncode(realTrimStr, cls, respCharsetName);
            } else if (isXmlStr(realTrimStr)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为标准XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (realTrimStr.startsWith("<") && realTrimStr.endsWith(">")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为非标准XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (realTrimStr.startsWith("{") && realTrimStr.endsWith("}")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为JSON对象格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            } else if (realTrimStr.startsWith("[") && realTrimStr.endsWith("]")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为JSON数组格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            } else if (realTrimStr.contains("&") && realTrimStr.contains("=")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为urlencode格式，使用URLDecoder进行decoder解析");
                return parseTextToUrlEncode(realTrimStr, cls, respCharsetName);
            }
            // 默认解析为JSON对象
            else {
                return parseTextToJSON(realTrimStr, cls);
            }
        } else {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->有自定义解析方法，依据有自定义解析方法进行解析");
            Object objinstance = null;
            try {
                Constructor<?> constructor = cls.getDeclaredConstructor();
                if (null != constructor) {
                    constructor.setAccessible(true);
                    objinstance = constructor.newInstance();
                }
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("解析执行->方法：");
                    sb.append(method.getName());
                    sb.append("；参数：");
                    StringBuilder sbPt = new StringBuilder();
                    for (Class<?> tmpType : parameterTypes) {
                        if (sbPt.length() > 0) {
                            sbPt.append("、");
                        }
                        sbPt.append(tmpType.getSimpleName());

                    }
                    sb.append(sbPt.toString());
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, sb.toString());
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (parameterTypes.length == 1) {
                    if (parameterTypes[0] == String.class) {
                        method.invoke(objinstance, respStr);
                    } else {
                        method.invoke(objinstance, stringToByte(respStr, respCharsetName));
                    }
                } else if (parameterTypes.length == 2) {
                    if (parameterTypes[0] == String.class) {
                        method.invoke(objinstance, respStr, respCharsetName);
                    } else {
                        method.invoke(objinstance, stringToByte(respStr, respCharsetName), respCharsetName);
                    }
                } else {
                    objinstance = null;
                }
            } catch (Exception e) {
                log.error("Error:setCommonResponseParseText", e);
                objinstance = null;
            }
            return objinstance;
        }

    }

    public static Object parseResponseData(byte[] resourceByte, Class<?> cls, String respCharsetName, String respFormat,
                                           HttpConfig.LoggerLevel loggerLevel, String loggerTag) {
        String respStr = byteToString(resourceByte, respCharsetName);
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "响应内容->" + respStr);

        if (null == cls) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析错误->解析类没有设置");
            return resourceByte;
        }
        if (cls == String.class) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类定义为String.class，使用byteToString解析成String类型");
            return respStr;
        }
//		else if (JSONObject.class.getName().equals(cls.getName())) {
//			HttpLogUtil.logDebug(isDebug, debugTag, "解析执行->类定义为JSONObject.class，使用FASTJson解析成JSONObject类型");
//			return parseToJSONObject(getRealTrimString(respStr));
//		}
//		else if (JSONArray.class.getName().equals(cls.getName())) {
//			HttpLogUtil.logDebug(isDebug, debugTag, "解析执行->类定义为JSONArray.class，使用FASTJson解析成JSONArray类型");
//			return parseToJSONArray(getRealTrimString(respStr));
//		}
        else if (XJSON.isJSONClass(cls)) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类定义为" + cls.getName() + "，使用" + XJSON.getWorkJsonName(cls) + "解析成"
                    + cls.getSimpleName() + "类型");
            return XJSON.parse(getRealTrimString(respStr), cls);
        }

        // 获取目标类的注解
        Method method = null;
        LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->获取类注解DefHttpParseFormat");
        DefHttpParseFormat parseFormat = cls.getAnnotation(DefHttpParseFormat.class);
        if (null != parseFormat && !StringUtils.isEmpty(parseFormat.parseMethod())) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->获取类注解parseMethod的解析方法");
            if (null != cls) {
                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), String.class);
                    } catch (Exception e) {
                        method = null;
                    }
                }
                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), byte[].class, String.class);
                    } catch (Exception e) {
                        method = null;
                    }
                }

                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), byte[].class);
                    } catch (Exception e) {
                        method = null;
                    }
                }
                if (null == method) {
                    try {
                        method = cls.getDeclaredMethod(parseFormat.parseMethod(), String.class, String.class);
                    } catch (Exception e) {
                        method = null;
                    }
                }
            }

        }

        if (null == method) {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->没有自定义解析方法，依据定义进行解析。解析优先级：类注解format>Content-Type格式>智能判断格式");
            // 若是JSON自动解析为JSON
            String realTrimStr = getRealTrimString(respStr);
            if (null == realTrimStr || realTrimStr.length() <= 0) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->内容为空，返回NULL");
                return null;
            }
            if (null != parseFormat && (PARSE_FORMAT_JSON.equalsIgnoreCase(parseFormat.format()))) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类注解format为JSON格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            }
            // else if (PARSE_FORMAT_XML.equalsIgnoreCase(parseFormat.format())
            // || PARSE_FORMAT_XML_NOHEADER.equalsIgnoreCase(parseFormat.format())) {
            // return parseTextToXML(getRealJsonString(responseStr), cls);
            // }
            else if (null != parseFormat && PARSE_FORMAT_XML.equalsIgnoreCase(parseFormat.format())) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类注解format为XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (null != parseFormat && PARSE_FORMAT_URLENCODE.equalsIgnoreCase(parseFormat.format())) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->类注解format为urlencode格式，使用URLDecoder进行decoder解析");
                return parseTextToUrlEncode(realTrimStr, cls, respCharsetName);
            } else if (!StringUtils.isEmpty(respFormat) && PARSE_FORMAT_JSON.equalsIgnoreCase(respFormat)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag,
                        "解析执行->请求声明parseFormat或响应Content-Type为JSON格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            } else if (!StringUtils.isEmpty(respFormat) && respFormat.contains(PARSE_FORMAT_URLENCODE)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->请求声明parseFormat或响应Content-Type为XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (!StringUtils.isEmpty(respFormat) && PARSE_FORMAT_URLENCODE.equalsIgnoreCase(respFormat)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->请求声明parseFormat或响应Content-Type为urlencode格式，使用URLDecoder进行decoder解析");
                return parseTextToUrlEncode(realTrimStr, cls, respCharsetName);
            } else if (isXmlStr(realTrimStr)) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为标准XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (realTrimStr.startsWith("<") && realTrimStr.endsWith(">")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为非标准XML格式，使用JAXBContext进行XML解析");
                return parseTextToXML(realTrimStr, cls);
            } else if (realTrimStr.startsWith("{") && realTrimStr.endsWith("}")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为JSON对象格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            } else if (realTrimStr.startsWith("[") && realTrimStr.endsWith("]")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为JSON数组格式，使用" + XJSON.getWorkJsonName() + "解析");
                return parseTextToJSON(realTrimStr, cls);
            } else if (realTrimStr.contains("&") && realTrimStr.contains("=")) {
                LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析执行->响应内容智能判断为urlencode格式，使用URLDecoder进行decoder解析");
                return parseTextToUrlEncode(realTrimStr, cls, respCharsetName);
            }
            // 默认解析为JSON对象
            else {
                return parseTextToJSON(realTrimStr, cls);
            }
        } else {
            LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, "解析方法->有自定义解析方法，依据有自定义解析方法进行解析");
            Object objinstance = null;
            try {
                Constructor<?> constructor = cls.getDeclaredConstructor();
                if (null != constructor) {
                    constructor.setAccessible(true);
                    objinstance = constructor.newInstance();
                }
                method.setAccessible(true);
                Class<?>[] parameterTypes = method.getParameterTypes();
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("解析执行->方法：");
                    sb.append(method.getName());
                    sb.append("；参数：");
                    StringBuilder sbPt = new StringBuilder();
                    for (Class<?> tmpType : parameterTypes) {
                        if (sbPt.length() > 0) {
                            sbPt.append("、");
                        }
                        sbPt.append(tmpType.getSimpleName());

                    }
                    sb.append(sbPt.toString());
                    LoggerHttpUtils.loggerMessage(log, loggerLevel, loggerTag, sb.toString());
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (parameterTypes.length == 1) {
                    if (parameterTypes[0] == String.class) {
                        method.invoke(objinstance, byteToString(resourceByte, respCharsetName));
                    } else {
                        method.invoke(objinstance, resourceByte);
                    }
                } else if (parameterTypes.length == 2) {
                    if (parameterTypes[0] == String.class) {
                        method.invoke(objinstance, byteToString(resourceByte, respCharsetName), respCharsetName);
                    } else {
                        method.invoke(objinstance, resourceByte, respCharsetName);
                    }
                } else {
                    objinstance = null;
                }
            } catch (Exception e) {
                log.error("Error:setCommonResponseParseText", e);
                objinstance = null;
            }
            return objinstance;
        }

    }

    public static byte[] stringToByte(String resourceStr, String charsetName) {
        if (null == resourceStr || resourceStr.length() <= 0) {
            return null;
        }
        Charset charset = null;
        try {
            if (StringUtils.isEmpty(charsetName) || !Charset.isSupported(charsetName)) {
                charset = null;
            } else {
                charset = Charset.forName(charsetName);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:stringToByte", e);
            charset = null;
        }
        byte[] temp = null;
        try {
            if (null == charset) {
                temp = resourceStr.getBytes();
            } else {
                temp = resourceStr.getBytes(charset);
            }
        } catch (Exception e) {
            temp = null;
        }
        return temp;
    }

    public static String byteToString(byte[] resourceByte, String charsetName) {
        if (null == resourceByte || resourceByte.length <= 0) {
            return null;
        }
        Charset charset = null;
        try {
            if (StringUtils.isEmpty(charsetName) || !Charset.isSupported(charsetName)) {
                charset = null;
            } else {
                charset = Charset.forName(charsetName);
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:byteToString", e);
            charset = null;
        }

        String temp = null;
        try {
            if (null == charset) {
                temp = new String(resourceByte);
            } else {
                temp = new String(resourceByte, charset);
            }
        } catch (Exception e) {
            temp = null;
        }
        return temp;
    }

    private static String getRealTrimString(String respString) {
        if (StringUtils.isEmpty(respString)) {
            return null;
        }
        String trimString = respString.trim();
        if (StringUtils.isEmpty(trimString)) {
            return null;
        }
        String realString = null;
        if (trimString.startsWith("\"") && trimString.endsWith("\"")) {
            try {
                realString = trimString.substring(1, trimString.length() - 1);
            } catch (Exception e) {
                realString = null;
            }
        } else {
            realString = trimString;
        }
        return realString;
    }

//	private static JSONObject parseToJSONObject(String jsonString) {
//		if (StringUtils.isEmpty(jsonString)) {
//			return null;
//		}
//		JSONObject object = null;
//		try {
//			object = JSON.parseObject(jsonString);
//		}
//		catch (Exception e) {
//			object = null;
//		}
//		return object;
//	}
//
//	private static JSONArray parseToJSONArray(String jsonString) {
//		if (StringUtils.isEmpty(jsonString)) {
//			return null;
//		}
//		JSONArray object = null;
//		try {
//			object = JSON.parseArray(jsonString);
//		}
//		catch (Exception e) {
//			object = null;
//		}
//		return object;
//	}

    private static Object parseTextToJSON(String jsonString, Class<?> cls) {
//		String realJsonStr = getRealTrimString(jsonString);
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        Object object = null;
        try {
            if (jsonString.startsWith("[")) {
//				object = JSON.parseArray(jsonString, cls);
                object = XJSON.parseArray(jsonString, cls);
            } else {
//				object = JSON.parseObject(jsonString, cls);
                object = XJSON.parseObject(jsonString, cls);
            }
        } catch (Exception e) {
            object = null;
        }
        return object;
    }

    private static Object parseTextToXML(String xmlString, Class<?> cls) {
        try {
            return XXML.parseTextToXML(xmlString, cls);
        } catch (Exception e) {
            log.error("Error:parseTextToXML", e);
            return null;
        }

    }

    private static Object parseTextToUrlEncode(String urlEncodeString, Class<?> cls, String respCharset) {
        try {
            Map<String, String> map = XUrlEncodedParse.parseToMap(urlEncodeString, respCharset);
            String jsonStr = XJSON.toJSONString(map);
            return parseTextToJSON(jsonStr, cls);
        } catch (Exception e) {
            log.error("Error:parseTextToUrlEncode", e);
            return null;
        }

    }

    public static String urlToDomain(String url) {
        try {
            if (null == url || url.length() <= 0) {
                return null;
            }
            String realUrl = url.replaceAll("\\\\", "/").replaceAll("[/]+", "/").replaceFirst(":/", "://");
            if (null == realUrl | realUrl.length() <= 0) {
                return null;
            }
            realUrl = realUrl.toLowerCase();
            String domainStr = null;
            if (realUrl.startsWith("http://")) {
                realUrl = realUrl.substring(7);
                int index = realUrl.indexOf("/");
                if (index > 0) {
                    domainStr = realUrl.substring(index);
                } else {
                    domainStr = realUrl;
                }
            } else if (realUrl.startsWith("https://")) {
                realUrl = realUrl.substring(8);
                int index = realUrl.indexOf("/");
                if (index > 0) {
                    domainStr = realUrl.substring(index);
                } else {
                    domainStr = realUrl;
                }
            }
            if (realUrl.startsWith("http:")) {
                realUrl = realUrl.substring(5);
                int index = realUrl.indexOf("/");
                if (index > 0) {
                    domainStr = realUrl.substring(0, index);
                } else {
                    domainStr = realUrl;
                }
            } else if (realUrl.startsWith("https:")) {
                realUrl = realUrl.substring(6);
                int index = realUrl.indexOf("/");
                if (index > 0) {
                    domainStr = realUrl.substring(0, index);
                } else {
                    domainStr = realUrl;
                }
            } else if (realUrl.startsWith("/")) {
                realUrl = realUrl.substring(1);
                int index = realUrl.indexOf("/");
                if (index > 0) {
                    domainStr = realUrl.substring(0, index);
                } else {
                    domainStr = realUrl;
                }
            } else {
                int index = realUrl.indexOf("/");
                if (index > 0) {
                    domainStr = realUrl.substring(0, index);
                } else {
                    domainStr = realUrl;
                }
            }
            return domainStr;
        } catch (Exception e) {
            log.error("Error:urlToDomain", e);
            return null;
        }

    }

    public static String parseContentTypeFormat(String contentType) {
        if (null == contentType || contentType.length() <= 0) {
            return null;
        }
        String typeStr = null;
        String[] datas = contentType.split(";");
        for (String tmp : datas) {
            if (null == tmp || tmp.length() <= 0) {
                continue;
            }
            String tmpLower = tmp.trim().toLowerCase();
            if (tmpLower.toLowerCase().startsWith("charset")) {
                continue;
            }
            String[] charDatas = tmpLower.split("/");
            if (null != charDatas && charDatas.length == 2 && null != charDatas[0] && charDatas[0].length() > 0
                    && null != charDatas[1] && charDatas[1].length() > 0
                    && (charDatas[0].equals("text") || charDatas[0].equals("application"))) {
                typeStr = charDatas[1];
                break;
            }
        }
        return typeStr;

    }

    public static String parseContentTypeCharset(String contentType) {
        if (null == contentType || contentType.length() <= 0) {
            return null;
        }
        String charsetName = null;
        String[] datas = contentType.split(";");
        for (String tmp : datas) {
            if (null == tmp || tmp.length() <= 0) {
                continue;
            }
            if (tmp.trim().toLowerCase().startsWith("charset")) {
                String[] charDatas = tmp.split("=");
                if (null != charDatas && charDatas.length == 2 && null != charDatas[1] && charDatas[1].length() > 0) {
                    charsetName = charDatas[1];
                    break;
                }
            }
        }
        return charsetName;
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

}