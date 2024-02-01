/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年8月17日 上午11:58:17
 */
package com.ruomm.javax.corex;

import com.ruomm.javax.corex.helper.ClassListHelper;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectUtils {
    private final static Log log = LogFactory.getLog(ObjectUtils.class);
    public final static int CLASS_DEEP_COUNT = 5;

    /**
     * 比较两个对象是否相等
     *
     * @param actual   实际
     * @param expected 预期
     * @return <ul>
     *         <li>如果为空，返回true</li>
     *         <li>返回实际。</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    public static boolean isBaseDataType(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        if (clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class
                || clazz == float.class || clazz == double.class || clazz == char.class || clazz == boolean.class
                || clazz == String.class) {
            return true;

        } else if (clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class
                || clazz == Float.class || clazz == Double.class || clazz == Character.class
                || clazz == Boolean.class) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBaseDataType(Object value) {
        if (null == value) {
            return false;
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long
                || value instanceof Float || value instanceof Double || value instanceof Character
                || value instanceof Boolean) {
            return true;
        }
        return false;
    }

    /**
     * 转换长阵列长阵列
     *
     * @param source 源
     * @return
     */
    public static Long[] transformLongArray(long[] source) {
        Long[] destin = new Long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * convert Long array to long array 转换成多头排列到多头排列
     *
     * @param source
     * @return
     */
    public static long[] transformLongArray(Long[] source) {
        long[] destin = new long[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 转换成int数组转换为整型数组
     *
     * @param source
     * @return
     */
    public static Integer[] transformIntArray(int[] source) {
        Integer[] destin = new Integer[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 转换成整型数组int数组
     *
     * @param source
     * @return
     */
    public static int[] transformIntArray(Integer[] source) {
        int[] destin = new int[source.length];
        for (int i = 0; i < source.length; i++) {
            destin[i] = source[i];
        }
        return destin;
    }

    /**
     * 公共静态<V>整型比较（V卷 V V2） 比较两个对象 关于结果 如果V1> V2，则返回1 如果V1 = V2，则返回0 如果V1 <V2，返回-1 关于规则 如果卷为null，v2是null，则返回0
     * 如果卷为null，v2是不为空，则返回-1 如果卷不为null，v2是null，则返回1 返回卷。Comparable.compareTo（对象） 参数： 卷 - V2 - 返回：
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? v2 == null ? 0 : -1 : v2 == null ? 1 : ((Comparable) v1).compareTo(v2);
    }

    public static String serializableToString(Serializable serializable) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
            return serStr;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }

    }

    public static <T extends Serializable> T stringToSerializable(String serStr) {
        try {
            String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            @SuppressWarnings("unchecked")
            T t = (T) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return t;

        } catch (Exception e) {
            return null;
        }

    }

    public static <T extends Serializable> T deepClone(Serializable serializable) {
        String serStr = serializableToString(serializable);
        return stringToSerializable(serStr);
    }

    /**
     * 解析对象为map
     *
     * @param object 对象
     * @return map列表
     * @paramDefault isParent 是否检索父类，默认false
     * @paramDefault isPutNullAndEmpty 是否null和empty放入，默认true
     */
    public static Map<String, Object> parseObjectToMap(Object object) {
        if (null == object) {
            return null;
        }
        Map<String, Object> mapObject = new HashMap<String, Object>();
        Field[] objFields = null;
        try {
            objFields = object.getClass().getDeclaredFields();
        } catch (Exception e) {
            log.error("Error:parseObjectToMap", e);
        }
        if (null != objFields && objFields.length > 0) {
            for (Field field : objFields) {
                try {
                    // 常量跳过
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    // 静态跳过
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object objTemp = field.get(object);
                    mapObject.put(field.getName(), objTemp);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    log.error("Error:parseObjectToMap", e);
                }
            }
        }
        return mapObject;
    }

    /**
     * 解析对象为map
     *
     * @param object            对象
     * @param isParent          是否检索父类
     * @param isPutNullAndEmpty 是否null和empty放入
     * @return map列表
     */
    public static Map<String, Object> parseObjectToMap(Object object, boolean isParent, boolean isPutNullAndEmpty) {
        if (null == object) {
            return null;
        }
        List<Class<?>> list = ObjectUtils.listClassByObject(object, ObjectUtils.CLASS_DEEP_COUNT, null);
        if (null == list || list.size() <= 0) {
            return null;
        }
        Map<String, Object> mapObject = new HashMap<>();
        for (Class<?> fieldClass : list) {
            Field[] fields = fieldClass.getDeclaredFields();
            if (null == fields || fields.length <= 0) {
                continue;
            }
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
                    field.setAccessible(true);
                    Object fieldVal = field.get(object);
                    if (isPutNullAndEmpty) {
                        if (null == fieldVal) {
                            continue;
                        }
                        if (fieldVal instanceof String) {
                            String fieldString = (String) fieldVal;
                            if (null != fieldString && fieldString.length() > 0) {
                                mapObject.put(field.getName(), fieldVal);
                            }
                        } else {
                            mapObject.put(field.getName(), fieldVal);
                        }
                    } else {
                        mapObject.put(field.getName(), fieldVal);
                    }
                } catch (Exception e) {
                    log.error("Error:parseObjectToMap", e);
                }
            }
        }
        return mapObject;
    }

    /**
     * 解析对象中的field字段值
     *
     * @param object 对象
     * @param key    字段的名称
     * @return field字段值
     * @paramDefault isParent 是否检索父类，默认false
     */
    public static Object parseObjectFieldValue(Object object, String key) {
        return parseObjectFieldValue(object, key, false);
    }

    /**
     * 解析对象中的field字段值
     *
     * @param object   对象
     * @param key      字段的名称
     * @param isParent 是否检索父类
     * @return field字段值
     */
    public static Object parseObjectFieldValue(Object object, String key, boolean isParent) {
        if (null == object || null == key || key.length() <= 0) {
            return null;
        }
        int deepCount = isParent ? ObjectUtils.CLASS_DEEP_COUNT : 1;
        List<Class<?>> list = ObjectUtils.listClassByObject(object, deepCount, null);
        if (null == list || list.size() <= 0) {
            return null;
        }
        Object resultFieldVal = null;
        boolean hasFind = false;
        for (Class<?> fieldClass : list) {
            Field[] fields = fieldClass.getDeclaredFields();
            if (null == fields || fields.length <= 0) {
                continue;
            }
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
                    if (!field.getName().equalsIgnoreCase(key)) {
                        continue;
                    }
                    field.setAccessible(true);
                    resultFieldVal = field.get(object);
                    hasFind = true;
                    break;
                } catch (Exception e) {
                    log.error("Error:parseObjectFieldValue", e);
                }
            }
            if (hasFind) {
                break;
            }
        }
        return resultFieldVal;
    }

    /**
     * 解析对象中的field对象
     *
     * @param object 对象
     * @param key    字段的名称
     * @return field对象
     * @paramDefault isParent 是否检索父类
     */
    public static Field parseObjectFieldItem(Object object, String key) {
        return parseObjectFieldItem(object, key, false);
    }

    /**
     * 解析对象中的field对象
     *
     * @param object   对象
     * @param key      字段的名称
     * @param isParent 是否检索父类
     * @return field对象
     */
    public static Field parseObjectFieldItem(Object object, String key, boolean isParent) {
        if (null == object || null == key || key.length() <= 0) {
            return null;
        }
        int deepCount = isParent ? ObjectUtils.CLASS_DEEP_COUNT : 1;
        List<Class<?>> list = ObjectUtils.listClassByObject(object, deepCount, null);
        if (null == list || list.size() <= 0) {
            return null;
        }
        Field resultField = null;
        boolean hasFind = false;
        for (Class<?> fieldClass : list) {
            Field[] fields = fieldClass.getDeclaredFields();
            if (null == fields || fields.length <= 0) {
                continue;
            }
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
                    if (!field.getName().equalsIgnoreCase(key)) {
                        continue;
                    }
                    resultField = field;
                    hasFind = true;
                    break;
                } catch (Exception e) {
                    log.error("Error:parseObjectFieldItem", e);
                }
            }
            if (hasFind) {
                break;
            }
        }
        return resultField;
    }

    /**
     * 设置对象中的field字段的值
     *
     * @param object 对象
     * @param key    字段的名称
     * @return 设置结果
     * @paramDefault isParent 是否检索父类
     */
    public static void setObjectFieldValue(Object object, String key, Object value) {
        setObjectFieldValue(object, key, value, false);
    }

    /**
     * 设置对象中的field字段的值
     *
     * @param object   对象
     * @param key      字段的名称
     * @param isParent 是否检索父类
     * @return 设置结果
     */
    public static void setObjectFieldValue(Object object, String key, Object value, boolean isParent) {
        if (null == object || null == key || key.length() <= 0) {
            return;
        }
        int deepCount = isParent ? ObjectUtils.CLASS_DEEP_COUNT : 1;
        List<Class<?>> list = ObjectUtils.listClassByObject(object, deepCount, null);
        if (null == list || list.size() <= 0) {
            return;
        }
        boolean hasFind = false;
        for (Class<?> fieldClass : list) {
            Field[] fields = fieldClass.getDeclaredFields();
            if (null == fields || fields.length <= 0) {
                continue;
            }
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
                    if (!field.getName().equalsIgnoreCase(key)) {
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(object, value);
                    hasFind = true;
                    break;
                } catch (Exception e) {
                    log.error("Error:setObjectFieldValue", e);
                }
            }
            if (hasFind) {
                break;
            }
        }
    }

    public static List<Class<?>> listClassByClass(Class<?> objCls, int deepCount, ClassListHelper classListHelper) {
        if (null == objCls) {
            return null;
        }
        List<Class<?>> listClass = new ArrayList<>();
        int whileCount = 0;
        if (deepCount <= 1) {
            whileCount = 1;
        } else if (deepCount > 5) {
            whileCount = 5;
        } else {
            whileCount = deepCount;
        }
        Class<?> fieldClass = null;
        for (int i = 0; i < whileCount; i++) {
            boolean isSuperClass;
            if (null == fieldClass) {
                fieldClass = objCls;
                isSuperClass = false;
            } else {
                fieldClass = fieldClass.getSuperclass();
                isSuperClass = true;
            }
            if (null == fieldClass) {
                break;
            } else if (fieldClass == Object.class) {
                break;
            } else if (null != classListHelper && classListHelper.isBreakList(isSuperClass, fieldClass)) {
                break;
            } else {
                listClass.add(fieldClass);
            }
        }
        return listClass;
    }

    public static List<Class<?>> listClassByObject(Object obj, int deepCount, ClassListHelper classListHelper) {
        if (null == obj) {
            return null;
        }
        List<Class<?>> listClass = new ArrayList<>();
        int whileCount = 0;
        if (deepCount <= 1) {
            whileCount = 1;
        } else if (deepCount > 5) {
            whileCount = 5;
        } else {
            whileCount = deepCount;
        }
        Class<?> fieldClass = null;
        for (int i = 0; i < whileCount; i++) {
            boolean isSuperClass;
            if (null == fieldClass) {
                fieldClass = obj.getClass();
                isSuperClass = false;
            } else {
                fieldClass = fieldClass.getSuperclass();
                isSuperClass = true;
            }
            if (null == fieldClass) {
                break;
            } else if (fieldClass == Object.class) {
                break;
            } else if (null != classListHelper && classListHelper.isBreakList(isSuperClass, fieldClass)) {
                break;
            } else {
                listClass.add(fieldClass);
            }
        }
        return listClass;
    }
}
