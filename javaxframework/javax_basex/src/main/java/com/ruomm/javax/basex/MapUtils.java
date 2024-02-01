/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年9月2日 下午3:07:20
 */
package com.ruomm.javax.basex;

import com.ruomm.javax.corex.ObjectUtils;
import com.ruomm.javax.corex.StringUtils;

import java.util.*;
import java.util.Map.Entry;

public class MapUtils {
    /**
     * 键和值之间默认的分隔符
     **/
    public final static String DEFAULT_KEY_AND_VALUE_SEPARATOR = "=";
    /**
     * key-value对之间默认的分隔符
     **/
    public final static String DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR = ",";

    /**
     * 为空或它的大小为0
     *
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1, 2})    =   false;
     * </pre>
     *
     * @param sourceMap
     * @return 如果map为null或者它的大小为0，返回真，否则返回假。
     */
    public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
        return (sourceMap == null || sourceMap.size() == 0);
    }

    /**
     * 添加键值对映射，而关键需要不为空或空
     *
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null or empty, return false</li>
     * <li>return {@link Map#put}</li>
     * </ul>
     */
    public static boolean putMapNotEmptyKey(Map<String, String> map, String key, String value) {
        if (map == null || StringUtils.isEmpty(key)) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 添加键值对映射，无论是键和值不一定为零或空
     *
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null or empty, return false</li>
     * <li>if value is null or empty, return false</li>
     * <li>return {@link Map#put}</li>
     * </ul>
     */
    public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value) {
        if (map == null || StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 添加键值对映射，关键需要不为空或空
     *
     * @param map
     * @param key
     * @param value
     * @param defaultValue
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null or empty, return false</li>
     * <li>if value is null or empty, put defaultValue, return true</li>
     * <li>if value is neither null nor empty，put value, return true</li>
     * </ul>
     */
    public static boolean putMapNotEmptyKeyAndValue(Map<String, String> map, String key, String value,
                                                    String defaultValue) {
        if (map == null || StringUtils.isEmpty(key)) {
            return false;
        }

        map.put(key, StringUtils.isEmpty(value) ? defaultValue : value);
        return true;
    }

    /**
     * 添加键值对映射，关键需要不为空
     *
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null, return false</li>
     * <li>return {@link Map#put}</li>
     * </ul>
     */
    public static <K, V> boolean putMapNotNullKey(Map<K, V> map, K key, V value) {
        if (map == null || key == null) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 添加键值对映射，无论是键和值不一定空
     *
     * @param map
     * @param key
     * @param value
     * @return <ul>
     * <li>if map is null, return false</li>
     * <li>if key is null, return false</li>
     * <li>if value is null, return false</li>
     * <li>return {@link Map#put}</li>
     * </ul>
     */
    public static <K, V> boolean putMapNotNullKeyAndValue(Map<K, V> map, K key, V value) {
        if (map == null || key == null || value == null) {
            return false;
        }

        map.put(key, value);
        return true;
    }

    /**
     * 值获取键，匹配的第一个条目从前到后
     * <ul>
     * <strong>注意事项：</strong>
     * <li>对于HashMap的，不进入相同的顺序放顺序，因此您可能需要使用TreeMap的</li>
     * </ul>
     *
     * @param <V>
     * @param map
     * @param value
     * @return <ul>
     *         <li>if map is null, return null</li>
     *         <li>if value exist, return key</li>
     *         <li>return null</li>
     *         </ul>
     */
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        if (isEmpty(map)) {
            return null;
        }

        for (Entry<K, V> entry : map.entrySet()) {
            if (ObjectUtils.isEquals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 解析键值对映射，忽略空的关键
     *
     * <pre>
     * parseKeyAndValueToMap("","","",true)=null
     * parseKeyAndValueToMap(null,"","",true)=null
     * parseKeyAndValueToMap("a:b,:","","",true)={(a,b)}
     * parseKeyAndValueToMap("a:b,:d","","",true)={(a,b)}
     * parseKeyAndValueToMap("a:b,c:d","","",true)={(a,b),(c,d)}
     * parseKeyAndValueToMap("a=b, c = d","=",",",true)={(a,b),(c,d)}
     * parseKeyAndValueToMap("a=b, c = d","=",",",false)={(a, b),( c , d)}
     * parseKeyAndValueToMap("a=b, c=d","=", ",", false)={(a,b),( c,d)}
     * parseKeyAndValueToMap("a=b; c=d","=", ";", false)={(a,b),( c,d)}
     * parseKeyAndValueToMap("a=b, c=d", ",", ";", false)={(a=b, c=d)}
     * </pre>
     *
     * @param source                   key-value pairs
     * @param keyAndValueSeparator     separator between key and value
     * @param keyAndValuePairSeparator separator between key-value pairs
     * @param ignoreSpace              whether ignore space at the begging or end of key and value
     * @return
     */
    public static Map<String, String> parseKeyAndValueToMap(String source, String keyAndValueSeparator,
                                                            String keyAndValuePairSeparator, boolean ignoreSpace) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        String mapStr = null;
        if (source.startsWith("{") && source.endsWith("}")) {
            mapStr = source.substring(1, source.length() - 1);
        } else {
            mapStr = source;
        }

        if (StringUtils.isEmpty(keyAndValueSeparator)) {
            keyAndValueSeparator = DEFAULT_KEY_AND_VALUE_SEPARATOR;
        }
        if (StringUtils.isEmpty(keyAndValuePairSeparator)) {
            keyAndValuePairSeparator = DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR;
        }
        Map<String, String> keyAndValueMap = new HashMap<String, String>();
        String[] keyAndValueArray = mapStr.split(keyAndValuePairSeparator);
        if (keyAndValueArray == null) {
            return null;
        }

        int seperator;
        for (String valueEntity : keyAndValueArray) {
            if (!StringUtils.isEmpty(valueEntity)) {
                seperator = valueEntity.indexOf(keyAndValueSeparator);
                if (seperator != -1) {
                    {
                        if (ignoreSpace) {
                            MapUtils.putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator).trim(),
                                    valueEntity.substring(seperator + 1).trim());
                        } else {
                            MapUtils.putMapNotEmptyKey(keyAndValueMap, valueEntity.substring(0, seperator),
                                    valueEntity.substring(seperator + 1));
                        }
                    }
                }
            }
        }
        return keyAndValueMap;
    }

    /**
     * 解析键值对映射，忽略空的关键
     *
     * @param source      key-value pairs
     * @param ignoreSpace 是否忽略了在键和值的乞讨或年底空间
     * @return
     * @see {@link MapUtils#parseKeyAndValueToMap}, keyAndValueSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_SEPARATOR}, keyAndValuePairSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR}
     */
    public static Map<String, String> parseKeyAndValueToMap(String source, boolean ignoreSpace) {
        return parseKeyAndValueToMap(source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR,
                ignoreSpace);
    }

    /**
     * 解析键值对映射，忽略空的关键，忽略了空间的key和value 开始或结束
     *
     * @param source key-value pairs
     * @return
     * @see {@link MapUtils#parseKeyAndValueToMap}, keyAndValueSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_SEPARATOR}, keyAndValuePairSeparator is
     * {@link #DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR}, ignoreSpace is true
     */
    public static Map<String, String> parseKeyAndValueToMap(String source) {
        return parseKeyAndValueToMap(source, DEFAULT_KEY_AND_VALUE_SEPARATOR, DEFAULT_KEY_AND_VALUE_PAIR_SEPARATOR,
                true);
    }

    /**
     * 加入视图
     *
     * @param map
     * @return
     */
    public static String toJson(Map<String, String> map) {
        if (map == null || map.size() == 0) {
            return null;
        }

        StringBuilder paras = new StringBuilder();
        paras.append("{");
        Iterator<Entry<String, String>> ite = map.entrySet().iterator();
        while (ite.hasNext()) {
            Entry<String, String> entry = ite.next();
            paras.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\"");
            if (ite.hasNext()) {
                paras.append(",");
            }
        }
        paras.append("}");
        return paras.toString();
    }

    // 判断map是否含有指定的key值

    /**
     * 判断map是否含有指定的key值
     *
     * @param key key值，key为null则返回false
     * @param map map类别，map为null或空则返回false
     * @return map是否含有指定的key值
     */
    public static boolean containsKey(Object key, Map map) {
        if (null == map || map.size() <= 0) {
            return false;
        }
        if (null == key) {
            return false;
        }
        return map.containsKey(key);
    }

    /**
     * 转换map的key值为List列表
     *
     * @param map map集合
     * @return map的key值List列表
     */
    public static <T> List<T> mapKeyToList(Map<T, ?> map) {
        return mapKeyToList(map, false);
    }

    /**
     * 转换map的key值为List列表
     *
     * @param map         map集合
     * @param destNotNull 转换后的key列表是否不能为空
     * @return map的key值List列表
     */
    public static <T> List<T> mapKeyToList(Map<T, ?> map, boolean destNotNull) {
        if (null == map || map.size() <= 0) {
            if (destNotNull) {
                return new ArrayList<T>();
            } else {
                return null;
            }
        }
        Set<T> jobsMapKey = map.keySet();
        List<T> listKeys = new ArrayList<T>();
        for (T key : jobsMapKey) {
            listKeys.add(key);
        }
        return listKeys;
    }
}
