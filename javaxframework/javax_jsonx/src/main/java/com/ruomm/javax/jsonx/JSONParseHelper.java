package com.ruomm.javax.jsonx;

import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright 像衍科技-idr.ai
 * @create 2023/5/31 11:24
 */
public interface JSONParseHelper {
    public String getWorkJsonName();

    public <T> boolean isJSONClass(Class<T> cls);

    @SuppressWarnings("unchecked")
    public <T> T parse(String str, Class<T> cls);

    public <T> T parseObject(String str, Class<T> cls);

    public <T> List<T> parseArray(String str, Class<T> cls);

    public String toJSONString(Object obj);

    public String toJSONStringPretty(Object obj);

    /**
     * 解析JSON对象为properties属性Map列表。
     *
     * @param str JSON字符串
     * @return properties属性Map列表。
     */
    public Map<String, Object> parseToPropertyMap(String str);
}
