/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月11日 下午4:24:36
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.util.List;
import java.util.Map;

/**
 * JSON解析工具类，支持fastjson2、fastjson、gson、jackson优先调用fastJson2
 *
 * @author 牛牛-wanruome@163.com
 */
public class JSONParseCommon {
    private final static Log log = LogFactory.getLog(JSONParseCommon.class);

    public enum JSONParseMode {
        FastJson2, FastJson, Gson, JackSon, NONE
    }

//    private static JSONParseMode jsonParseMode = JSONParseMode.NONE;


    final static JSONParseHelper helperFastJson2;
    final static JSONParseHelper helperFastJson;
    final static JSONParseHelper helperGson;
    final static JSONParseHelper helperJackSon;
    static JSONParseHelper parseHelper;

    static {
        boolean isF2 = false;
        try {
            Class<?> cls = Class.forName("com.alibaba.fastjson2.JSON");
            if (null != cls) {
                isF2 = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:JSONParseCommon.static->FastJson2 not found!", e);

        }
        helperFastJson2 = isF2 ? new JSONParseFastjson2() : null;
        boolean isF = false;
        try {
            Class<?> cls = Class.forName("com.alibaba.fastjson.JSON");
            if (null != cls) {
                isF = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:JSONParseCommon.static->FastJson not found!", e);
        }
        helperFastJson = isF ? new JSONParseFastjson() : null;
        boolean isG = false;
        try {
            Class<?> cls = Class.forName("com.google.gson.Gson");
            if (null != cls) {
                isG = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:JSONParseCommon.static->Gson not found!", e);
        }
        helperGson = isG ? new JSONParseGson() : null;
        boolean isJ = false;
        try {
            Class<?> cls = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            if (null != cls) {
                isJ = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            log.debug("Error:JSONParseCommon.static->JackSon not found!", e);
        }
        helperJackSon = isJ ? new JSONParseJackson() : null;
        if (null != helperFastJson2) {
            parseHelper = helperFastJson2;
        } else if (null != helperFastJson) {
            parseHelper = helperFastJson;
        } else if (null != helperGson) {
            parseHelper = helperGson;
        } else if (null != helperJackSon) {
            parseHelper = helperJackSon;
        } else {
            parseHelper = null;
        }
        if (null == parseHelper) {
            log.error(
                    "Error:JSONParseCommon.static->XJSON、XJSONParse初始化错误，没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
    }

    public static boolean supportMode(JSONParseMode parseMode) {
        if (null == parseMode) {
            return false;
        } else if (parseMode == JSONParseMode.FastJson2 && null != helperFastJson2) {
            return true;
        } else if (parseMode == JSONParseMode.FastJson && null != helperFastJson) {
            return true;
        } else if (parseMode == JSONParseMode.Gson && null != helperGson) {
            return true;
        } else if (parseMode == JSONParseMode.JackSon && null != helperJackSon) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置优先JSON解析工具类型
     *
     * @param primaryParse JSON优先解析模式
     */
    public static void setPrimaryParseMode(JSONParseMode primaryParse) {
        if (null != primaryParse && JSONParseMode.FastJson2 == primaryParse && null != helperFastJson2) {
            parseHelper = helperFastJson2;
            log.debug("提示->参数为FastJson2模式并找到FastJson2工具，设置解析为FastJson2模式");
        } else if (null != primaryParse && JSONParseMode.FastJson == primaryParse && null != helperFastJson) {
            parseHelper = helperFastJson;
            log.debug("提示->参数为FastJson模式并找到FastJson工具，设置解析为FastJson模式");
        } else if (null != primaryParse && JSONParseMode.Gson == primaryParse && null != helperGson) {
            parseHelper = helperGson;
            log.debug("提示->参数为Gson模式并找到Gson工具，设置解析为Gson模式");
        } else if (null != primaryParse && JSONParseMode.JackSon == primaryParse && null != helperJackSon) {
            parseHelper = helperJackSon;
            log.debug("提示->参数为JackSon模式并找到JackSon工具，设置解析为JackSon模式");
        } else {
            if (null != helperFastJson2) {
                parseHelper = helperFastJson2;
                log.debug("提示->参数JSON解析模式未输入，找到FastJson2工具，设置解析为FastJson2模式");
            } else if (null != helperFastJson) {
                parseHelper = helperFastJson;
                log.debug("提示->参数JSON解析模式未输入，找到FastJson工具，设置解析为FastJson模式");
            } else if (null != helperGson) {
                parseHelper = helperGson;
                log.debug("提示->参数JSON解析模式未输入，找到Gson工具，设置解析为Gson模式");
            } else if (null != helperJackSon) {
                parseHelper = helperJackSon;
                log.debug("提示->参数JSON解析模式未输入，找到JackSon工具，设置解析为JackSon模式");
            } else {
                parseHelper = null;
                log.error(
                        "Error:JSONParseCommon.setPrimaryParseMode->XJSON、XJSONParse初始化错误，没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            }
        }
    }

    /**
     * 获取真正解析时候的JSON工具类
     */
    private static JSONParseHelper generatorPraseHelper(JSONParseMode... parseModes) {
        if (null == parseModes || parseModes.length <= 0) {
            return parseHelper;
        }
        for (JSONParseMode tmpParseMode : parseModes) {
            if (null == tmpParseMode || tmpParseMode == JSONParseMode.NONE) {
                continue;
            } else if (tmpParseMode == JSONParseMode.FastJson2 && null != helperFastJson2) {
                return helperFastJson2;
            } else if (tmpParseMode == JSONParseMode.FastJson && null != helperFastJson) {
                return helperFastJson;
            } else if (tmpParseMode == JSONParseMode.Gson && null != helperGson) {
                return helperGson;
            } else if (tmpParseMode == JSONParseMode.JackSon && null != helperJackSon) {
                return helperJackSon;
            }
        }
        return parseHelper;
    }

    /**
     * 获取当前使用的JSON解析工具类型
     *
     * @return
     */
    public static String getWorkJsonName() {
        if (null != parseHelper) {
            return parseHelper.getWorkJsonName();
        } else {
            return constant.NAME_UNKNOWN;
        }
    }

    /**
     * 获取当前使用的JSON解析工具类型
     *
     * @param cls 当前类型
     * @return
     */
    public static String getWorkJsonName(Class<?> cls) {
        if (null == cls) {
            return getWorkJsonName();
        }
        if (null != helperFastJson2 && helperFastJson2.isJSONClass(cls)) {
            return helperFastJson2.getWorkJsonName();
        }
        if (null != helperFastJson && helperFastJson.isJSONClass(cls)) {
            return helperFastJson.getWorkJsonName();
        }
        if (null != helperGson && helperGson.isJSONClass(cls)) {
            return helperGson.getWorkJsonName();
        }
        if (null != helperJackSon && helperJackSon.isJSONClass(cls)) {
            return helperJackSon.getWorkJsonName();
        }
        return getWorkJsonName();
    }

    /**
     * 判断是否属于fastjson2、fastjson、gson、jackson类型，属于返回true
     * 符合要求的类型有FastJson2或FastJson的JSONObject、JSONArray类型，Gson的JsonObject、JsonArray、JsonElement类型，JackSon的JsonNode类型
     *
     * @param cls 类型信息
     * @return
     */
    public static <T> boolean isJSONClass(Class<T> cls) {
        if (null == cls) {
            return false;
        }
        if (null != helperFastJson2 && helperFastJson2.isJSONClass(cls)) {
            return true;
        }
        if (null != helperFastJson && helperFastJson.isJSONClass(cls)) {
            return true;
        }
        if (null != helperGson && helperGson.isJSONClass(cls)) {
            return true;
        }
        if (null != helperJackSon && helperJackSon.isJSONClass(cls)) {
            return true;
        }
        return false;
    }

    /**
     * 解析JSON字符串为fastjson2、fastjson、gson、jackson对象，无法解析抛出异常
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型->JSONObject、JSONArray、JsonObject、JsonArray、JsonElement类型
     * @return
     */
    public static <T> T parse(String str, Class<T> cls) {
        if (null == cls) {
            log.error("Error:parse->传入待解析类型cls为空，无法进行JSON解析。");
            throw new RuntimeException("传入待解析类型cls为空，无法进行JSON解析。");
        }
        if (null == str || str.length() <= 0) {
            log.error("Error:parse->传入待解析字符串str为空，无法进行JSON解析。");
            throw new RuntimeException("传入待解析字符串str为空，无法进行JSON解析。");
        }
        if (null == helperFastJson2 && null == helperFastJson && null == helperGson && null == helperJackSon) {
            log.error("Error:parse->没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            throw new RuntimeException("没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
        if (null != helperFastJson2 && helperFastJson2.isJSONClass(cls)) {
            if (XJSON.LOG_ENABLE) {
                log.debug(cls.getName() + "为 FastJson2 类，使用 FastJson2 解析。");
            }
            return helperFastJson2.parse(str, cls);

        }
        if (null != helperFastJson && helperFastJson.isJSONClass(cls)) {
            if (XJSON.LOG_ENABLE) {
                log.debug(cls.getName() + "为 FastJson 类，使用 FastJson 解析。");
            }
            return helperFastJson.parse(str, cls);

        }
        if (null != helperGson && helperGson.isJSONClass(cls)) {
            if (XJSON.LOG_ENABLE) {
                log.debug(cls.getName() + "为 Gson 类，使用 Gson 解析。");
            }
            return helperGson.parse(str, cls);

        }
        if (null != helperJackSon && helperJackSon.isJSONClass(cls)) {
            if (XJSON.LOG_ENABLE) {
                log.debug(cls.getName() + "为 JackSon 类，使用 JackSon 解析。");
            }
            return helperJackSon.parse(str, cls);

        }
        log.error(
                "Error:parse->无法解析，待解析类型必须为FastJson2或FastJson2或FastJson的JSONObject、JSONArray类型，Gson的JsonObject、JsonArray、JsonElement类型，JackSon的JsonNode类型。");
        throw new RuntimeException(
                "无法解析，待解析类型必须为FastJson2或FastJson2或FastJson的JSONObject、JSONArray类型，Gson的JsonObject、JsonArray、JsonElement类型，JackSon的JsonNode类型。");
    }

    /**
     * 解析JSON字符串为对象类型，无法解析抛出异常
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型
     * @return
     */
    public static <T> T parseObject(String str, Class<T> cls, JSONParseMode... parseModes) {
        if (null == cls) {
            log.error("Error:parse->传入待解析类型cls为空，无法进行JSON解析。");
            throw new RuntimeException("传入待解析类型cls为空，无法进行JSON解析。");
        }
        if (null == str || str.length() <= 0) {
            log.error("Error:parse->传入待解析字符串str为空，无法进行JSON解析。");
            throw new RuntimeException("传入待解析字符串str为空，无法进行JSON解析。");
        }
        JSONParseHelper jsonParseHelper = generatorPraseHelper(parseModes);
        if (null != jsonParseHelper) {
            if (XJSON.LOG_ENABLE) {
                log.debug("使用 " + jsonParseHelper.getWorkJsonName() + " 解析为对象。");
            }
            return jsonParseHelper.parseObject(str, cls);
        } else {
            log.error("Error:parse->没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            throw new RuntimeException("没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
    }

    /**
     * 解析JSON字符串为对象列表类型，无法解析抛出异常
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型
     * @return
     */
    public static <T> List<T> parseArray(String str, Class<T> cls, JSONParseMode... parseModes) {
        if (null == cls) {
            log.error("Error:parseArray->传入待解析类型cls为空，无法进行JSON解析。");
            throw new RuntimeException("传入待解析类型cls为空，无法进行JSON解析。");
        }
        if (null == str || str.length() <= 0) {
            log.error("Error:parseArray->传入待解析字符串str为空，无法进行JSON解析。");
            throw new RuntimeException("传入待解析字符串str为空，无法进行JSON解析。");
        }
        JSONParseHelper jsonParseHelper = generatorPraseHelper(parseModes);
        if (null != jsonParseHelper) {
            if (XJSON.LOG_ENABLE) {
                log.debug("使用 " + jsonParseHelper.getWorkJsonName() + " 解析为数组列表。");
            }
            return jsonParseHelper.parseArray(str, cls);
        } else {
            log.error("Error:parseArray->没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            throw new RuntimeException("没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
    }

    /**
     * 序列化对象为JSON字符串类型，无法序列化抛出异常
     *
     * @param obj 待序列化对象
     * @return
     */
    public static String toJSONString(Object obj, JSONParseMode... parseModes) {
        if (null == obj) {
            log.error("Error:toJSONString->传入对象为空，无法JSON序列化。");
            throw new RuntimeException("传入对象为空，无法JSON序列化。");
        }
        JSONParseHelper jsonParseHelper = generatorPraseHelper(parseModes);
        if (null != jsonParseHelper) {
            if (XJSON.LOG_ENABLE) {
                log.debug("使用 " + jsonParseHelper.getWorkJsonName() + " 进行序列化。");
            }
            return jsonParseHelper.toJSONString(obj);
        } else {
            log.error("Error:toJSONString->没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            throw new RuntimeException("没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
    }

    /**
     * 序列化对象为美化后的JSON字符串类型，无法序列化抛出异常
     *
     * @param obj 待序列化对象
     * @return
     */
    public static String toJSONStringPretty(Object obj, JSONParseMode... parseModes) {
        if (null == obj) {
            log.error("Error:toJSONStringPretty->传入对象为空，无法JSON序列化。");
            throw new RuntimeException("传入对象为空，无法JSON序列化。");
        }
        JSONParseHelper jsonParseHelper = generatorPraseHelper(parseModes);
        if (null != jsonParseHelper) {
            if (XJSON.LOG_ENABLE) {
                log.debug("使用 " + jsonParseHelper.getWorkJsonName() + " 进行序列化。");
            }
            return jsonParseHelper.toJSONStringPretty(obj);
        } else {
            log.error("Error:toJSONStringPretty->没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            throw new RuntimeException("没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
    }

    /**
     * 解析JSON对象为properties属性Map列表。
     *
     * @param str JSON字符串
     * @return properties属性Map列表。
     */
    public static Map<String, Object> parseToPropertyMap(String str, JSONParseMode... parseModes) {
        if (null == str || str.length() <= 0) {
            log.error("Error:parse->传入待解析字符串str为空，无法进行JSON解析为properties属性Map列表。");
            return null;
        }
        JSONParseHelper jsonParseHelper = generatorPraseHelper(parseModes);
        if (null != jsonParseHelper) {
            if (XJSON.LOG_ENABLE) {
                log.debug("使用 " + jsonParseHelper.getWorkJsonName() + " 解析为properties属性Map列表。");
            }
            return jsonParseHelper.parseToPropertyMap(str);
        } else {
            log.error("Error:parse->没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
            throw new RuntimeException("没有JSON序列化工具，请引入至少一种JSON序列化工具(fastjson2、fastjson、gson、jackson)。");
        }
    }
}
