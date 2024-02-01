/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月11日 下午6:05:38
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.jsonx.JSONParseCommon.JSONParseMode;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.util.List;
import java.util.Map;

/**
 * JSON解析工具类，支持fastJson2、fastJson、gson、jackson优先调用FastJson
 *
 * @author 牛牛-wanruome@163.com
 */
public class XJSON {
    private final static Log log = LogFactory.getLog(XJSON.class);
    public static boolean LOG_ENABLE = false;

    public void configLogEnable(boolean logEnable) {
        LOG_ENABLE = logEnable;
    }

    /**
     * 设置优先JSON解析工具类型
     *
     * @param primaryParse JSON优先解析模式
     */
    public static void setPrimaryParseMode(JSONParseMode primaryParse) {
        JSONParseCommon.setPrimaryParseMode(primaryParse);
    }

    /**
     * 获取当前使用的JSON解析工具类型
     *
     * @return
     */
    public static String getWorkJsonName() {
        return JSONParseCommon.getWorkJsonName();
    }

    /**
     * 获取当前使用的JSON解析工具类型
     *
     * @param cls 当前类型
     * @return
     */
    public static String getWorkJsonName(Class<?> cls) {
        try {
            return JSONParseCommon.getWorkJsonName(cls);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:getWorkJsonName", e);
            return constant.NAME_UNKNOWN;
        }
    }

    public static boolean supportMode(JSONParseMode parseMode) {
        return JSONParseCommon.supportMode(parseMode);
    }

    /**
     * 判断是否属于fastJson2、fastJson、gson、jackson类型，属于返回true
     *
     * @param cls 类型信息
     * @return
     */
    public static <T> boolean isJSONClass(Class<T> cls) {
        try {
            return JSONParseCommon.isJSONClass(cls);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:isJSONClass", e);
            return false;
        }
    }

    /**
     * 判断是否属于fastJson2、fastJson、gson、jackson类型，属于返回true
     * 符合要求的类型有FastJson2或FastJson的JSONObject、JSONArray类型，Gson的JsonObject、JsonArray、JsonElement类型，JackSon的JsonNode类型
     *
     * @param cls 类型信息
     * @return
     */
    public static <T> T parse(String str, Class<T> cls) {
        try {
            return JSONParseCommon.parse(str, cls);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parse", e);
            return null;
        }
    }

    /**
     * 解析JSON字符串为对象类型，无法解析捕获异常返回NULL
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型
     * @return
     */
    public static <T> T parseObject(String str, Class<T> cls, JSONParseMode... parseModes) {
        try {
            return JSONParseCommon.parseObject(str, cls, parseModes);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parseObject", e);
            return null;
        }
    }

    /**
     * 解析JSON字符串为对象列表类型，无法解析捕获异常返回NULL
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型
     * @return
     */
    public static <T> List<T> parseArray(String str, Class<T> cls, JSONParseMode... parseModes) {
        try {
            return JSONParseCommon.parseArray(str, cls, parseModes);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:parseArray", e);
            return null;
        }
    }

    /**
     * 序列化对象为JSON字符串类型，无法序列化捕获异常返回NULL
     *
     * @param obj 待序列化对象
     * @return
     */
    public static String toJSONString(Object obj, JSONParseMode... parseModes) {
        try {
            return JSONParseCommon.toJSONString(obj, parseModes);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:toJSONString", e);
            return null;
        }
    }

    /**
     * 序列化对象为美化后的JSON字符串类型，无法序列化抛出异常
     *
     * @param obj 待序列化对象
     * @return
     */
    public static String toJSONStringPretty(Object obj, JSONParseMode... parseModes) {
        try {
            return JSONParseCommon.toJSONStringPretty(obj, parseModes);
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:toJSONStringPretty", e);
            return null;
        }
    }

    /**
     * 解析JSON对象为properties属性Map列表。
     *
     * @param str JSON字符串
     * @return properties属性Map列表。
     */
    public static Map<String, Object> parseToPropertyMap(String str, JSONParseMode... parseModes) {
        return JSONParseCommon.parseToPropertyMap(str, parseModes);
    }

}
