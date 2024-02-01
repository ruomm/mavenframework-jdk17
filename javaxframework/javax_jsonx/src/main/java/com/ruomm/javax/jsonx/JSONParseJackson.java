/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年10月21日 上午11:03:54
 */
package com.ruomm.javax.jsonx;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONParseJackson implements JSONParseHelper {
    private final static Log log = LogFactory.getLog(JSONParseJackson.class);

    @Override
    public String getWorkJsonName() {
        return constant.NAME_JACKSON;
    }

    @Override
    public <T> boolean isJSONClass(Class<T> cls) {
        if (cls == JsonNode.class) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T parse(String str, Class<T> cls) {
        if (cls == JsonNode.class) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return (T) mapper.readTree(str);
            } catch (IOException e) {
                String clsName = null == cls ? " NULL " : " " + cls.getSimpleName() + " ";
                // TODO Auto-generated catch block
                log.error("Error:parse->" + "使用JackSon时候解析异常，字符串无法解析为" + clsName + "类型对象。", e);
                throw new RuntimeException("使用JackSon时候解析异常，字符串无法解析为" + clsName + "类型对象。");
            }
        }
        return null;
    }

    @Override
    public <T> T parseObject(String str, Class<T> cls) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 启用注释支持
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            // 单一数据也解析为数组
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            // 关闭解析时候属性在对象里面找不到解析目标时候抛出异常
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(str, cls);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            String clsName = null == cls ? " NULL " : " " + cls.getSimpleName() + " ";
            log.error("Error:parseObject->" + "使用JackSon解析异常，字符串无法解析为" + clsName + "类型对象。", e);
            throw new RuntimeException("使用JackSon解析异常，字符串无法解析为" + clsName + "类型对象。");
        }
    }

    @Override
    public <T> List<T> parseArray(String str, Class<T> cls) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 启用注释支持
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            // 关闭解析时候属性在对象里面找不到解析目标时候抛出异常
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {
            };
            return mapper.readValue(str, typeReference);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            String clsName = null == cls ? " NULL " : " " + cls.getSimpleName() + " ";
            log.error("Error:parseArray->" + "使用JackSon解析异常，字符串无法解析为" + clsName + "类型数组列表。", e);
            throw new RuntimeException("使用JackSon解析异常，字符串无法解析为" + clsName + "类型数组列表。");
        }
    }

    @Override
    public String toJSONString(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 序列化时候不序列化NULL对象
            mapper.setSerializationInclusion(Include.NON_NULL);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            String clsName = null == obj ? " NULL " : " " + obj.getClass().getSimpleName() + " ";
            log.error("Error:parseArray->" + "使用JackSon序列化异常，对象" + clsName + "无法序列化。", e);
            throw new RuntimeException("使用JackSon序列化异常，对象" + clsName + "无法序列化。");
        }
    }

    @Override
    public String toJSONStringPretty(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // 序列化时候不序列化NULL对象
            mapper.setSerializationInclusion(Include.NON_NULL);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            String clsName = null == obj ? " NULL " : " " + obj.getClass().getSimpleName() + " ";
            log.error("Error:parseArray->" + "使用JackSon序列化异常，对象" + clsName + "无法序列化。", e);
            throw new RuntimeException("使用JackSon序列化异常，对象" + clsName + "无法序列化。");
        }
    }

    /**
     * 使用JackSon解析JSON对象为properties属性Map列表。
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
            ObjectMapper mapper = new ObjectMapper();
            // 启用注释支持
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            // 关闭解析时候属性在对象里面找不到解析目标时候抛出异常
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode jsonNode = mapper.readTree(str);
            return jsonResultToPropertyMap(jsonNode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, Object> jsonResultToPropertyMap(JsonNode rootJsonNode) {
        Map<String, Object> map = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> iterators = rootJsonNode.fields();
        while (iterators.hasNext()) {
            try {
                Map.Entry<String, JsonNode> tmpEntry = iterators.next();
                String tmpKey = tmpEntry.getKey();
                if (null == tmpKey || tmpKey.length() <= 0) {
                    continue;
                }
                // 支持注释，注释的自动跳过，不解析
                if (tmpKey.startsWith("#") || tmpKey.startsWith("----") || tmpKey.startsWith("//")) {
                    continue;
                }
                JsonNode tmpNode = tmpEntry.getValue();
                if (null == tmpNode) {
                    continue;
                } else if (tmpNode.isArray()) {
                    continue;
                } else if (tmpNode.isObject()) {
                    map.put(tmpKey, jsonResultToPropertyMap(tmpNode));
                    continue;
                } else if (tmpNode.isContainerNode()) {
                    continue;
                } else if (tmpNode.isNull()) {
                    continue;
                } else if (tmpNode.isPojo()) {
                    continue;
                } else {
                    String tmpVal = tmpNode.asText();
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
