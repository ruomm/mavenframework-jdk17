/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月21日 上午11:03:54
 */
package com.ruomm.javax.jsonx;

import com.google.gson.*;

import java.util.*;

public class JSONParseGson implements JSONParseHelper {

    @Override
    public String getWorkJsonName() {
        return constant.NAME_GSON;
    }

    @Override
    public <T> boolean isJSONClass(Class<T> cls) {
        if (cls == JsonObject.class || cls == JsonArray.class || cls == JsonElement.class) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parse(String str, Class<T> cls) {
        if (cls == JsonObject.class) {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonElement = jsonParser.parse(str).getAsJsonObject();
            return (T) jsonElement;
        } else if (cls == JsonArray.class) {
            JsonParser jsonParser = new JsonParser();
            JsonArray jsonElements = jsonParser.parse(str).getAsJsonArray();
            return (T) jsonElements;
        } else if (cls == JsonElement.class) {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(str);
            return (T) jsonElement;
        }
        return null;
    }

    @Override
    public <T> T parseObject(String str, Class<T> cls) {
        Gson gson = new Gson();
        return gson.fromJson(str, cls);
    }

    @Override
    public <T> List<T> parseArray(String str, Class<T> cls) {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(str).getAsJsonArray();// 获取JsonArray对象
        if (null != jsonElements) {
            ArrayList<T> beans = new ArrayList<>();
            for (JsonElement bean : jsonElements) {
                T bean1 = gson.fromJson(bean, cls);// 解析
                beans.add(bean1);
            }
            return beans;
        } else {
            return null;
        }
    }

    @Override
    public String toJSONString(Object obj) {
        Gson gson = new Gson();
        if (obj instanceof JsonElement) {
            return gson.toJson((JsonElement) obj);
        } else {
            return gson.toJson(obj);
        }
    }

    @Override
    public String toJSONStringPretty(Object obj) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (obj instanceof JsonElement) {
            return gson.toJson((JsonElement) obj);
        } else {
            return gson.toJson(obj);
        }
    }

    /**
     * 使用Gson解析JSON对象为properties属性Map列表。
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
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonElement = jsonParser.parse(str).getAsJsonObject();
            return jsonResultToPropertyMap(jsonElement);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, Object> jsonResultToPropertyMap(JsonObject jsonObject) {
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
                JsonElement tmpElement = jsonObject.get(tmpKey);
                if (null == tmpElement) {
                    continue;
                } else if (tmpElement.isJsonArray()) {
                    continue;
                } else if (tmpElement.isJsonNull()) {
                    continue;
                } else if (tmpElement.isJsonObject()) {
                    Map<String, Object> tmpMap = jsonResultToPropertyMap((JsonObject) tmpElement);
                    if (null != tmpMap && tmpMap.size() > 0) {
                        map.put(tmpKey, tmpMap);
                    }
                } else {
                    String tmpVal = tmpElement.getAsString();
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
