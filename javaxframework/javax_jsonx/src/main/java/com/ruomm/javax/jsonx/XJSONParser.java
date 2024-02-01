/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月11日 下午6:05:38
 */
package com.ruomm.javax.jsonx;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.util.List;
import java.util.Map;

/**
 * JSON解析工具类，支持FastJson、Gson、JackSon优先调用FastJson
 *
 * @author 牛牛-wanruome@163.com
 */
public class XJSONParser{
    private final static Log log = LogFactory.getLog(XJSONParser.class);
    private JSONParseHelper parserHelper;

    public XJSONParser(JSONParseHelper parserHelper) {
        super();
        this.parserHelper = parserHelper;
    }

    public JSONParseHelper getParserHelper() {
        return parserHelper;
    }

    public void setParserHelper(JSONParseHelper parserHelper) {
        this.parserHelper = parserHelper;
    }

    /**
     * 获取当前使用的JSON解析工具类型
     *
     * @return
     */
    public String getWorkJsonName() {
        if (null == parserHelper) {
            return JSONParseCommon.getWorkJsonName();
        } else {
            return parserHelper.getWorkJsonName();
        }
    }

    /**
     * 获取当前使用的JSON解析工具类型
     *
     * @param cls 当前类型
     * @return
     */
    public String getWorkJsonName(Class<?> cls) {
        if (null == parserHelper) {
            try {
                return JSONParseCommon.getWorkJsonName(cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:getWorkJsonName", e);
                return constant.NAME_UNKNOWN;
            }
        } else {
            return parserHelper.getWorkJsonName();
        }
    }

    /**
     * 判断是否属于fastjson2、fastjson、gson、jackson类型，属于返回true
     * 符合要求的类型有FastJson2或FastJson的JSONObject、JSONArray类型，Gson的JsonObject、JsonArray、JsonElement类型，JackSon的JsonNode类型
     *
     * @param cls 类型信息
     * @return
     */
    public <T> boolean isJSONClass(Class<T> cls) {
        if (null == parserHelper) {
            try {
                return JSONParseCommon.isJSONClass(cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:isJSONClass", e);
                return false;
            }
        } else {
            try {
                return parserHelper.isJSONClass(cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:isJSONClass", e);
                return false;
            }
        }
    }

    /**
     * 解析JSON字符串为fastjson2、fastjson、gson、jackson对象，无法解析捕获异常返回NULL
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型->JSONObject、JSONArray、JsonObject、JsonArray、JsonElement类型
     * @return
     */
    public <T> T parse(String str, Class<T> cls) {
        if (null == parserHelper) {
            try {
                return JSONParseCommon.parse(str, cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parse", e);
                return null;
            }
        } else {
            try {
                if (XJSON.LOG_ENABLE) {
                    log.debug(cls.getName() + "为 自定义XJSONParserHelper 解析。");
                }
                return parserHelper.parse(str, cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parse", e);
                return null;
            }
        }

    }

    /**
     * 解析JSON字符串为对象类型，无法解析捕获异常返回NULL
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型
     * @return
     */
    public <T> T parseObject(String str, Class<T> cls, JSONParseCommon.JSONParseMode... parseModes) {
        if (null == parserHelper) {
            try {
                return JSONParseCommon.parseObject(str, cls, parseModes);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parseObject", e);
                return null;
            }
        } else {
            try {
                if (XJSON.LOG_ENABLE) {
                    log.debug("使用 自定义XJSONParserHelper 解析为对象。");
                }
                return parserHelper.parseObject(str, cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parseObject", e);
                return null;
            }
        }

    }

    /**
     * 解析JSON字符串为对象列表类型，无法解析捕获异常返回NULL
     *
     * @param str 待解析JSON字符串
     * @param cls 待解析类型
     * @return
     */
    public <T> List<T> parseArray(String str, Class<T> cls, JSONParseCommon.JSONParseMode... parseModes) {

        if (null == parserHelper) {
            try {
                return JSONParseCommon.parseArray(str, cls, parseModes);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parseArray", e);
                return null;
            }
        } else {
            try {
                if (XJSON.LOG_ENABLE) {
                    log.debug("使用 自定义XJSONParserHelper 解析为数组列表。");
                }
                return parserHelper.parseArray(str, cls);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parseArray", e);
                return null;
            }
        }
    }

    /**
     * 序列化对象为JSON字符串类型，无法序列化捕获异常返回NULL
     *
     * @param obj 待序列化对象
     * @return
     */
    public String toJSONString(Object obj, JSONParseCommon.JSONParseMode... parseModes) {

        if (null == parserHelper) {
            try {
                return JSONParseCommon.toJSONString(obj, parseModes);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:toJSONString", e);
                return null;
            }
        } else {
            try {
                if (XJSON.LOG_ENABLE) {
                    log.debug("使用 自定义XJSONParserHelper 进行序列化。");
                }
                return parserHelper.toJSONString(obj);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:toJSONString", e);
                return null;
            }
        }
    }

    /**
     * 序列化对象为美化后的JSON字符串类型，无法序列化抛出异常
     *
     * @param obj 待序列化对象
     * @return
     */
    public String toJSONStringPretty(Object obj, JSONParseCommon.JSONParseMode... parseModes) {
        if (null == parserHelper) {
            try {
                return JSONParseCommon.toJSONStringPretty(obj, parseModes);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:toJSONString", e);
                return null;
            }
        } else {
            try {
                if (XJSON.LOG_ENABLE) {
                    log.debug("使用 自定义XJSONParserHelper 进行序列化。");
                }
                return parserHelper.toJSONStringPretty(obj);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:toJSONString", e);
                return null;
            }
        }
    }

    /**
     * 解析JSON对象为properties属性Map列表。
     *
     * @param str JSON字符串
     * @return properties属性Map列表。
     */
    public Map<String, Object> parseToPropertyMap(String str, JSONParseCommon.JSONParseMode... parseModes) {
        if (null == parserHelper) {
            try {
                return JSONParseCommon.parseToPropertyMap(str, parseModes);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parseToPropertyMap", e);
                return null;
            }
        } else {
            try {
                if (XJSON.LOG_ENABLE) {
                    log.debug("使用 自定义XJSONParserHelper 解析为properties属性Map列表。");
                }
                return parserHelper.parseToPropertyMap(str);
            } catch (Exception e) {
                // TODO: handle exception
                log.error("Error:parseToPropertyMap", e);
                return null;
            }
        }
    }
}
