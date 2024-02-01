/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月21日 上午11:03:54
 */
package com.ruomm.javax.jsonx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONParseFastjson implements JSONParseHelper {

    @Override
    public String getWorkJsonName() {
        return constant.NAME_FASTJSON;
    }

    @Override
    public <T> boolean isJSONClass(Class<T> cls) {
        if (cls == JSONObject.class || cls == JSONArray.class) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parse(String str, Class<T> cls) {
        if (cls == JSONObject.class) {
            JSONObject jsonObject = JSON.parseObject(str);
            return (T) jsonObject;
        } else if (cls == JSONArray.class) {
            JSONArray jsonArray = JSON.parseArray(str);
            return (T) jsonArray;
        }
        return null;
    }

    @Override
    public <T> T parseObject(String str, Class<T> cls) {
        return JSON.parseObject(str, cls);
    }

    @Override
    public <T> List<T> parseArray(String str, Class<T> cls) {
        return JSON.parseArray(str, cls);
    }

    @Override
    public String toJSONString(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public String toJSONStringPretty(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 解析JSON对象为properties属性Map列表。
     *
     * @param str JSON字符串
     * @return properties属性Map列表。
     */
    @Override
    public Map<String, Object> parseToPropertyMap(String str) {
        if (null == str || str.length() <= 0) {
            return null;
        }
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            return jsonResultToPropertyMap(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, Object> jsonResultToPropertyMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Set<String> keySets = jsonObject.keySet();
        for (String tmpKey : keySets) {
            try {
                if (null == tmpKey || tmpKey.length() <= 0) {
                    continue;
                }
                // 支持注释，注释的自动跳过，不解析
                if (tmpKey.startsWith("#") || tmpKey.startsWith("----") || tmpKey.startsWith("//")) {
                    continue;
                }
                Object tmpObj = jsonObject.get(tmpKey);
                try {
                    tmpObj = jsonObject.get(tmpKey);
                } catch (Exception e) {
                    e.printStackTrace();
                    tmpObj = null;
                }
                if (null == tmpObj) {
                    String tmpVal = jsonObject.getString(tmpKey);
                    if (null != tmpVal) {
                        map.put(tmpKey, tmpVal);
                    }
                } else if (tmpObj instanceof JSONArray) {
                    continue;
                } else if (tmpObj instanceof JSONObject) {
                    Map<String, Object> tmpMap = jsonResultToPropertyMap((JSONObject) tmpObj);
                    if (null != tmpMap && tmpMap.size() > 0) {
                        map.put(tmpKey, tmpMap);
                    }
                } else {
                    String tmpVal = jsonObject.getString(tmpKey);
                    if (null != tmpVal) {
                        map.put(tmpKey, tmpVal);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return map;
    }
}
