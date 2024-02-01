package com.ruomm.javax.jsonx;

import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/15 11:16
 */
public class XJSONServlet {
    private final static Log log = LogFactory.getLog(XJSONServlet.class);
    private final static int INOUT_BUFF_SIZE = 1024;

    /**
     * 解析请求内容为对象
     *
     * @param request     servlet请求
     * @param charsetName 编码方式
     * @param tClass      解析对象
     * @return 解析后的结果
     */
    public static <T> T parseRequestToObject(HttpServletRequest request, String charsetName, Class<T> tClass) {
        String str = readRequestToJson(request, charsetName);
        if (null == str || str.length() <= 0) {
            str = readParamToJson(request, charsetName, tClass);
        }
        if (null == str || str.length() <= 0) {
            return null;
        }
        try {
            T t = XJSON.parseObject(str, tClass);
            return t;
        } catch (Exception e) {
            if (null == tClass) {
                log.error("parseRequestToObject(" + "parseClassNULL" + ")Servlet请求数据解析为对象时候发生异常", e);
            } else {
                log.info("parseRequestToObject(" + tClass.getName() + ")Servlet请求数据解析为对象时候发生异常", e);
            }
            return null;
        }
    }

    /**
     * 解析请求内容为Map列表
     *
     * @param request     servlet请求
     * @param charsetName 编码方式
     * @return 解析后的Map列表
     */
    public static Map<String, String> parseRequestToMap(HttpServletRequest request, String charsetName) {
        String str = readRequestToJson(request, charsetName);
        if (null == str || str.length() <= 0) {
            str = readParamToJson(request, charsetName, null);
        }
        if (null == str || str.length() <= 0) {
            return null;
        }
        try {
            Map<String, Object> mapResult = XJSON.parseObject(str, Map.class);
            Map<String, String> map = new HashMap<>();
            for (String tmpKey : mapResult.keySet()) {
//                String tmpKey=null;
//                if(null==tmpKeyObj){
//                    continue;
//                } else if(tmpKeyObj instanceof String) {
//                    tmpKey=(String)tmpKeyObj;
//                } else {
//                    tmpKey=String.valueOf(tmpKeyObj);
//                }
//                Object tmpVal=mapResult.get(tmpKeyObj);
                Object tmpVal = mapResult.get(tmpKey);
                if (null == tmpVal) {
                    continue;
                } else if (tmpVal instanceof String) {
                    String tmpStr = (String) tmpVal;
                    map.put(tmpKey, tmpStr);
                } else {
                    String tmpStr = String.valueOf(tmpVal);
                    map.put(tmpKey, tmpStr);
                }
            }
            return map;
        } catch (Exception e) {
            log.error("parseRequestToMap->请求内容转换为JSON格式时候发生异常", e);
            return null;
        }
    }

    /**
     * JSON格式和 URLEncode格式通用接受
     *
     * @param request     请求输入流
     * @param charsetName 编码方式
     * @return JSON格式的数据
     */
    public static String readRequestToJson(HttpServletRequest request, String charsetName) {
        String returnStr = null;
        try {
            String resultStr = readRequestString(request, charsetName);
            log.info("readRequestToJson->请求内容：" + resultStr);
            if (null == resultStr || resultStr.length() <= 0) {
                log.info("readRequestToJson->请求内容为空，不能解析为JSON数据");
                returnStr = resultStr;
            } else if (resultStr.startsWith("{") && resultStr.endsWith("}")) {
                log.info("readRequestToJson->请求内容是JSON对象格式：" + resultStr);
                returnStr = resultStr;
            } else if (resultStr.startsWith("[") && resultStr.endsWith("]")) {
                log.info("readRequestToJson->请求内容是JSON数组格式：" + resultStr);
                returnStr = resultStr;
            } else if (resultStr.contains("&") && resultStr.contains("=")) {
                try {
                    Map<String, String> map = new HashMap<String, String>();
                    String[] pairs = resultStr.split("&");
                    for (String pair : pairs) {
                        if (null == pair || pair.length() == 0) {
                            continue;
                        }
                        int index = pair.indexOf("=");
                        if (index <= 0) {
                            continue;
                        }
                        String key = pair.substring(0, index);
                        String val = index >= pair.length() - 1 ? null : pair.substring(index + 1);
                        String realVal = decodeStr(val, charsetName);
                        if (index > 0) {
                            map.put(key, realVal);
                        }
                    }
                    String jsonStr = XJSON.toJSONString(map);
                    log.info("readRequestToJson->请求内容转换为JSON格式：" + jsonStr);
                    returnStr = jsonStr;
                } catch (Exception e) {
                    log.error("readRequestToJson->请求内容转换为JSON格式时候发生异常", e);
                    returnStr = resultStr;
                }
            } else if (resultStr.contains("=") && !resultStr.contains("&")) {
                try {
                    int index = resultStr.indexOf("=");
                    if (index > 0) {
                        Map<String, String> map = new HashMap<String, String>();
                        String key = resultStr.substring(0, index);
                        String val = index >= resultStr.length() - 1 ? null : resultStr.substring(index + 1);
                        map.put(key, val);
                        String jsonStr = XJSON.toJSONString(map);
                        log.info("readRequestToJson->请求内容转换为JSON格式：" + jsonStr);
                        returnStr = jsonStr;
                    } else {
                        log.info("readRequestToJson->请求内容不能转换为JSON格式，请求可能不正确：" + resultStr);
                        returnStr = resultStr;
                    }
                } catch (Exception e) {
                    log.error("readRequestToJson->请求内容转换为JSON格式时候发生异常", e);
                    returnStr = resultStr;
                }
            } else {
                log.info("readRequestToJson->请求内容不能转换为JSON格式，请求可能不正确：" + resultStr);
                returnStr = resultStr;
            }

        } catch (Exception e) {
            log.error("readRequestToJson->请求内容接收时候发生异常", e);
            returnStr = null;
        }
        return returnStr;

    }

    /**
     * 读取请求内容
     *
     * @param request     servlet请求
     * @param charsetName 编码方式
     * @return 请求内容
     */
    public static String readRequestString(HttpServletRequest request, String charsetName) {
        if (null == request) {
            return null;
        }
        byte[] resultByte = null;
        InputStream is = null;
        BufferedInputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            is = request.getInputStream();
            in = new BufferedInputStream(is);
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[INOUT_BUFF_SIZE];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, INOUT_BUFF_SIZE))) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            resultByte = bos.toByteArray();
        } catch (Exception e) {
            log.error("Error:readRequestString", e);
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("Error:readRequestString", e);
            }
            try {
                if (null != is) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("Error:readRequestString", e);
            }
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:readRequestString", e);
            }
        }
        if (null == resultByte) {
            return null;
        }
        try {

            if (null == charsetName || charsetName.length() <= 0) {
                String resultStr = new String(resultByte);
                return resultStr;
            } else {
                Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
                String resultStr = new String(resultByte, charset);
                return resultStr;
            }

        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:readRequestString", e);
            return null;
        }
    }

    /**
     * 读取请求参数为JSON数据
     *
     * @param request     请求体
     * @param charsetName 编码方式
     * @return JSON数据
     */
    public static String readParamToJson(HttpServletRequest request, String charsetName) {
        return readParamToJson(request, charsetName, null);
    }

    /**
     * 读取请求参数为JSON数据
     *
     * @param request     请求体
     * @param charsetName 编码方式
     * @param tCls        依据对象读取
     * @return JSON数据
     */
    public static String readParamToJson(HttpServletRequest request, String charsetName, Class<?> tCls) {
        if (null == request) {
            return null;
        }
        if (null == tCls || XJSON.isJSONClass(tCls)) {
            try {
                Map<String, Object> jsonObject = new HashMap<String, Object>();
                Map<String, String[]> params = request.getParameterMap();
                for (String key : params.keySet()) {
                    String[] values = params.get(key);
                    if (null != values && values.length == 1) {
                        jsonObject.put(key, decodeStr(values[0], charsetName));
                    } else if (null != values && values.length > 1) {
                        jsonObject.put(key, decodeArray(values, charsetName));
                    }
                }
                String resultStr = XJSON.toJSONString(jsonObject);
                log.info("readParamToJson->请求参数转换为JSON格式：" + resultStr);
                return resultStr;
            } catch (Exception e) {
                log.error("Error:readParamToJson->请求参数读取时候发生异常，请求可能不正确。", e);
                return null;
            }
        } else {
            try {
                Map<String, Object> jsonObject = new HashMap<String, Object>();
                Field[] declaredFields = null;
                try {
                    declaredFields = tCls.getDeclaredFields();
                } catch (Exception e) {
                    declaredFields = null;
                    log.error("Error:readParamToJson", e);
                }
                if (null != declaredFields && declaredFields.length > 0) {
                    for (Field field : declaredFields) {
                        try {
                            // 常量跳过
                            if (Modifier.isFinal(field.getModifiers())) {
                                continue;
                            }
                            // 静态跳过
                            if (Modifier.isStatic(field.getModifiers())) {
                                continue;
                            }
                            String key = field.getName();
                            String value = request.getParameter(key);
                            if (null != value) {
                                jsonObject.put(key, decodeStr(value, charsetName));
                            }
                        } catch (Exception e) {
                            log.error("Error:readParamToJson", e);
                        }
                    }
                }
                Field[] fields = null;
                try {
                    fields = tCls.getFields();
                } catch (Exception e) {
                    fields = null;
                    log.error("Error:readParamToJson", e);
                }
                if (null != fields && fields.length > 0) {
                    for (Field field : fields) {
                        try {
                            // 常量跳过
                            if (Modifier.isFinal(field.getModifiers())) {
                                continue;
                            }
                            // 静态跳过
                            if (Modifier.isStatic(field.getModifiers())) {
                                continue;
                            }
                            String key = field.getName();
                            String value = request.getParameter(key);
                            if (null != value) {
                                jsonObject.put(key, decodeStr(value, charsetName));
                            }
                        } catch (Exception e) {
                            log.error("Error:readParamToJson", e);
                        }
                    }
                }
                String resultStr = XJSON.toJSONString(jsonObject);
                log.info("readParamToJson->请求参数转换为JSON格式：" + resultStr);
                return resultStr;
            } catch (Exception e) {
                log.error("Error:readParamToJson->请求参数读取时候发生异常，请求可能不正确。", e);
                return null;
            }
        }
    }

    private static List<String> decodeArray(String[] strs, String charsetName) {
        if (null == strs || strs.length <= 0) {
            return null;
        }
        List<String> decodeStrs = new ArrayList<>();
        for (String str : strs) {
            String decodeStr = decodeStr(str, charsetName);
            if (null != decodeStr) {
                decodeStrs.add(decodeStr);
            }
        }
        return decodeStrs;
    }

    private static String decodeStr(String str, String charsetName) {
        if (null == str || str.length() <= 0) {
            return str;
        }
        String decodeStr = null;
        try {
            if (null == charsetName || charsetName.length() <= 0) {
                decodeStr = URLDecoder.decode(str);
            } else {
                String decodeCharsetName = CharsetUtils.parseRealCharsetName(charsetName, "UTF-8");
                decodeStr = URLDecoder.decode(str, decodeCharsetName);
            }
        } catch (UnsupportedEncodingException e) {
            log.error("Error:decodeStr", e);
            decodeStr = null;
        }
        return decodeStr;
    }
}
