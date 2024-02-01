package com.ruomm.javax.basex;

import com.ruomm.javax.basex.annotation.DefMapTransObj;
import com.ruomm.javax.corex.ObjectUtils;
import com.ruomm.javax.corex.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/8/2 9:41
 */
public class MapParseUtils {
    // 转换MAP值到Object对象值
    public static void mapToObject(Object obj, Map<String, ?> map) {
        if (null == obj) {
            return;
        }
        if (null == map || map.size() <= 0) {
            return;
        }
        List<Class<?>> list = ObjectUtils.listClassByObject(obj, ObjectUtils.CLASS_DEEP_COUNT, null);
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Class<?> fieldClass : list) {
            Field[] fields = fieldClass.getDeclaredFields();
            if (null == fields || fields.length <= 0) {
                continue;
            }
            for (Field field : fields) {
                try {
                    // 静态跳过
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    // final类型跳过
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    String fieldName = field.getName();
                    if (!map.containsKey(fieldName)) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object mapObjVal = map.get(fieldName);
                    if (null == mapObjVal) {
                        continue;
                    }
                    if (mapObjVal instanceof String) {
                        String mapValStr = (String) mapObjVal;
                        Class<?> fieldType = field.getType();
                        DefMapTransObj defMapTransObj = field.getAnnotation(DefMapTransObj.class);
                        if (null != defMapTransObj && null != defMapTransObj.transMethod() && defMapTransObj.transMethod().length() > 0) {
                            Method method = null;
                            try {
                                method = fieldClass.getDeclaredMethod(defMapTransObj.transMethod(), Object.class);
                            } catch (Exception e) {
                                method = null;
                            }
                            if (null == method) {
                                try {
                                    method = fieldClass.getDeclaredMethod(defMapTransObj.transMethod(), String.class);
                                } catch (Exception e) {
                                    method = null;
                                }
                            }
                            if (null != method) {
                                method.setAccessible(true);
                                Object val = method.invoke(obj, mapValStr);
                                if (null != val) {
                                    field.set(obj, val);
                                }
                            }
                        } else if (fieldType == String.class) {
                            field.set(obj, mapValStr);
                        } else if (fieldType == byte.class || fieldType == Byte.class) {
                            if (mapValStr.length() > 0) {
                                byte val = Byte.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == short.class || fieldType == Short.class) {
                            if (mapValStr.length() > 0) {
                                short val = Short.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == int.class || fieldType == Integer.class) {
                            if (mapValStr.length() > 0) {
                                int val = Integer.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == long.class || fieldType == Long.class) {
                            if (mapValStr.length() > 0) {
                                long val = Long.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == float.class || fieldType == Float.class) {
                            if (mapValStr.length() > 0) {
                                float val = Float.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == double.class || fieldType == Double.class) {
                            if (mapValStr.length() > 0) {
                                double val = Double.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == char.class || fieldType == Character.class) {
                            if (mapValStr.length() > 0) {
                                char val = mapValStr.charAt(0);
                                field.set(obj, val);
                            }
                        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                            if (mapValStr.length() > 0) {
                                boolean val = Boolean.valueOf(mapValStr);
                                field.set(obj, val);
                            }
                        } else if (fieldType == BigDecimal.class) {
                            if (mapValStr.length() > 0) {
                                BigDecimal val = new BigDecimal(mapValStr);
                                field.set(obj, val);
                            }
                        }
                    } else {
                        DefMapTransObj defMapTransObj = field.getAnnotation(DefMapTransObj.class);
                        if (null != defMapTransObj && null != defMapTransObj.transMethod() && defMapTransObj.transMethod().length() > 0) {
                            boolean isStringMode = false;
                            Method method = null;
                            try {
                                method = fieldClass.getDeclaredMethod(defMapTransObj.transMethod(), Object.class);
                            } catch (NoSuchMethodException e) {
                                method = null;
                            }
                            if (null == method) {
                                try {
                                    method = fieldClass.getDeclaredMethod(defMapTransObj.transMethod(), String.class);
                                    isStringMode = true;
                                } catch (NoSuchMethodException e) {
                                    method = null;
                                }
                            }
                            if (null != method) {
                                method.setAccessible(true);
                                if (isStringMode) {
                                    String mapValStr = String.valueOf(mapObjVal);
                                    Object val = method.invoke(obj, mapValStr);
                                    if (null != val) {
                                        field.set(obj, val);
                                    }
                                } else {
                                    Object val = method.invoke(obj, mapObjVal);
                                    if (null != val) {
                                        field.set(obj, val);
                                    }
                                }
                            }
                        } else {
                            field.set(obj, mapObjVal);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 从MAP里面取得值，没法取得时候取默认值
     *
     * @param key        key值
     * @param map        map对象
     * @param defaultVal 默认值
     * @return 结果值
     */
    public static String parseMapWithDefaultValue(String key, Map<String, String> map, String defaultVal) {
        String resultVal = parseMapValue(key, map);
        if (StringUtils.isEmpty(resultVal)) {
            return defaultVal;
        } else {
            return resultVal;
        }
    }

    /**
     * 从MAP里面取得值，没法取得时候取NULL
     *
     * @param key key值
     * @param map map对象
     * @return 结果值
     */
    public static String parseMapValue(String key, Map<String, String> map) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        if (null == map || map.size() <= 0) {
            return null;
        }
        String resultVal = null;
        Set<String> keySet = map.keySet();
        for (String tmp : keySet) {
            if (key.equalsIgnoreCase(tmp)) {
                resultVal = map.get(tmp);
                break;
            }
        }
        return null == resultVal || resultVal.length() <= 0 || resultVal.trim().length() <= 0 ? null : resultVal;
    }
}
