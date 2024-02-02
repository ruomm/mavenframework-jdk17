/**
 * @copyright wanruome-2019
 * @author 牛牛-wanruome@163.com
 * @create 2019年12月9日 上午11:13:29
 */
package com.ruomm.javax.propertiesx;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;
import com.ruomm.javax.propertiesx.annotation.DefPropertyField;
import com.ruomm.javax.propertiesx.annotation.DefPropertySpace;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PropertyUtil {
    private final static Log log = LogFactory.getLog(PropertyUtil.class);
    public final static String READER_TYPE_TIME = "time";
    public final static String READER_TYPE_DATE = "date";
    public final static String READER_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    public static <T> T parseProperty(String propertyPath, Class<T> tCls, String charsetName) {
        T t = null;
        try {
            t = tCls.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            log.error("Error:parseProperty", e);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            log.error("Error:parseProperty", e);
        }
        if (null == t) {
            return null;
        }
        PropertyReaderCommon readerCharset = new PropertyReaderCommon(propertyPath, charsetName);
        readerCharset.loadProps(true);
        DefPropertySpace defPropertySpace = tCls.getAnnotation(DefPropertySpace.class);
        String spaceName = null == defPropertySpace || null == defPropertySpace.spaceName()
                || defPropertySpace.spaceName().trim().length() <= 0 ? "" : defPropertySpace.spaceName().trim();
        if (null != spaceName && spaceName.length() > 0 && !spaceName.endsWith(".")) {
            spaceName = spaceName + ".";
        }
        String excuteMethodOnEndName = null == defPropertySpace || StringUtils.isEmpty(defPropertySpace.excuteMethodOnEnd()) ? null : defPropertySpace.excuteMethodOnEnd();
        Method excuteMethodOnEnd = null;
        if (!StringUtils.isEmpty(excuteMethodOnEndName) && null == excuteMethodOnEnd) {
            try {
                excuteMethodOnEnd = tCls.getDeclaredMethod(excuteMethodOnEndName);
            } catch (NoSuchMethodException e) {
                log.error("Error:parseProperty", e);
                excuteMethodOnEnd = null;
            }
        }
        if (!StringUtils.isEmpty(excuteMethodOnEndName) && null == excuteMethodOnEnd) {
            try {
                excuteMethodOnEnd = tCls.getMethod(excuteMethodOnEndName);
            } catch (NoSuchMethodException e) {
                log.error("Error:parseProperty", e);
                excuteMethodOnEnd = null;
            }
        }
        Field[] fields = tCls.getDeclaredFields();
        for (Field tmpField : fields) {
            // 常量跳过
            if (Modifier.isFinal(tmpField.getModifiers())) {
                continue;
            }
            // 静态跳过
            if (Modifier.isStatic(tmpField.getModifiers())) {
                continue;
            }
            DefPropertyField tmpDefPropertyField = tmpField.getAnnotation(DefPropertyField.class);
            DefPropertySpace tmpDefPropertySpace = tmpField.getAnnotation(DefPropertySpace.class);
            if (null != tmpDefPropertyField && tmpDefPropertyField.readerIgnore()) {
                continue;
            }
            String readerMethod = StringUtils
                    .trim(null == tmpDefPropertyField ? null : tmpDefPropertyField.readerMethod());
            // 依据解析函数解析
            if (null != readerMethod && readerMethod.length() > 0) {
                try {
                    String tmpFieldName = null == tmpDefPropertyField || null == tmpDefPropertyField.name()
                            || tmpDefPropertyField.name().trim().length() <= 0 ? tmpField.getName()
                            : tmpDefPropertyField.name().trim();
                    String tmpSapce = null;
                    if (null != tmpDefPropertySpace) {
                        tmpSapce = null == tmpDefPropertySpace || null == tmpDefPropertySpace.spaceName()
                                || tmpDefPropertySpace.spaceName().trim().length() <= 0 ? ""
                                : tmpDefPropertySpace.spaceName().trim();
                        if (null != tmpSapce && tmpSapce.length() > 0 && !tmpSapce.endsWith(".")) {
                            tmpSapce = tmpSapce + ".";
                        }
                    } else {
                        tmpSapce = spaceName;
                    }
                    String tmpReaderKey = tmpSapce + tmpFieldName;
                    String tmpReaderValue = readerCharset.getProperty(tmpReaderKey);
                    if (null == tmpReaderValue) {
                        continue;
                    }
                    Method method = tCls.getDeclaredMethod(readerMethod, String.class);
                    if (null == method) {
                        continue;
                    }
                    method.setAccessible(true);
                    Object tmpParseResult = method.invoke(t, tmpReaderValue);
                    tmpField.setAccessible(true);
                    tmpField.set(t, tmpParseResult);

                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:parseProperty", e);
                }
            }
            // 依据字段解析
            else {
                try {
                    String tmpFieldName = null == tmpDefPropertyField || null == tmpDefPropertyField.name()
                            || tmpDefPropertyField.name().trim().length() <= 0 ? tmpField.getName()
                            : tmpDefPropertyField.name().trim();
                    String tmpSapce = null;
                    if (null != tmpDefPropertySpace) {
                        tmpSapce = null == tmpDefPropertySpace || null == tmpDefPropertySpace.spaceName()
                                || tmpDefPropertySpace.spaceName().trim().length() <= 0 ? ""
                                : tmpDefPropertySpace.spaceName().trim();
                        if (null != tmpSapce && tmpSapce.length() > 0 && !tmpSapce.endsWith(".")) {
                            tmpSapce = tmpSapce + ".";
                        }
                    } else {
                        tmpSapce = spaceName;
                    }
                    String tmpReaderKey = tmpSapce + tmpFieldName;
                    String tmpReaderValue = readerCharset.getProperty(tmpReaderKey);
                    if (null == tmpReaderValue) {
                        continue;
                    }
                    tmpField.setAccessible(true);
                    String readType = null == tmpDefPropertyField || null == tmpDefPropertyField.readerType()
                            || tmpDefPropertyField.readerType().trim().length() <= 0 ? ""
                            : tmpDefPropertyField.readerType().trim();

                    Class<?> cls = tmpField.getType();
                    if (null != readType && READER_TYPE_TIME.equalsIgnoreCase(readType)
                            && (cls == long.class || cls == Long.class)) {
                        Long TimeMillis = parseStrToTimeMillis(tmpReaderValue, 0l,true);
                        if (cls == long.class) {
                            tmpField.set(t, (long) TimeMillis);
                        } else {
                            tmpField.set(t, (long) TimeMillis);
                        }
                    } else if (null != readType && READER_TYPE_DATE.equalsIgnoreCase(readType) && cls == Date.class) {
                        String readFormat = null == tmpDefPropertyField || null == tmpDefPropertyField.readerFormat()
                                || tmpDefPropertyField.readerFormat().trim().length() <= 0 ? ""
                                : tmpDefPropertyField.readerFormat().trim();
                        SimpleDateFormat tmpDateFormat = null == readFormat || readFormat.length() <= 0
                                ? new SimpleDateFormat(READER_FORMAT_DATE)
                                : new SimpleDateFormat(readFormat);
                        Date tmpDate = tmpDateFormat.parse(tmpReaderValue);
                        tmpField.set(t, tmpDate);
                    } else if (cls == String.class) {
                        tmpField.set(t, tmpReaderValue);
                    } else if (cls == StringBuilder.class) {
                        tmpField.set(t, new StringBuilder().append(tmpReaderValue));
                    } else if (cls == StringBuffer.class) {
                        tmpField.set(t, new StringBuffer().append(tmpReaderValue));
                    } else if (cls == int.class) {
                        tmpField.set(t, (int) Integer.valueOf(tmpReaderValue));
                    } else if (cls == Integer.class) {
                        tmpField.set(t, Integer.valueOf(tmpReaderValue));
                    } else if (cls == long.class) {
                        tmpField.set(t, (long) Long.valueOf(tmpReaderValue));
                    } else if (cls == Long.class) {
                        tmpField.set(t, Long.valueOf(tmpReaderValue));
                    } else if (cls == float.class) {
                        tmpField.set(t, (float) Float.valueOf(tmpReaderValue));
                    } else if (cls == Float.class) {
                        tmpField.set(t, Float.valueOf(tmpReaderValue));
                    } else if (cls == double.class) {
                        tmpField.set(t, (double) Double.valueOf(tmpReaderValue));
                    } else if (cls == Double.class) {
                        tmpField.set(t, Double.valueOf(tmpReaderValue));
                    } else if (cls == short.class) {
                        tmpField.set(t, (short) Short.valueOf(tmpReaderValue));
                    } else if (cls == Short.class) {
                        tmpField.set(t, Short.valueOf(tmpReaderValue));
                    } else if (cls == char.class) {
                        tmpField.set(t, tmpReaderValue.charAt(0));
                    } else if (cls == Character.class) {
                        Character c = tmpReaderValue.charAt(0);
                        tmpField.set(t, c);
                    } else if (cls == byte.class) {
                        tmpField.set(t, (byte) Byte.valueOf(tmpReaderValue));
                    } else if (cls == Byte.class) {
                        tmpField.set(t, Byte.valueOf(tmpReaderValue));
                    } else if (cls == boolean.class) {
                        if ("1".equalsIgnoreCase(tmpReaderValue) || "true".equals(tmpReaderValue)) {
                            tmpField.set(t, true);
                        } else if ("0".equalsIgnoreCase(tmpReaderValue) || "false".equals(tmpReaderValue)) {
                            tmpField.set(t, false);
                        }
                    } else if (cls == Boolean.class) {
                        if ("1".equalsIgnoreCase(tmpReaderValue) || "true".equals(tmpReaderValue)) {
                            tmpField.set(t, true);
                        } else if ("0".equalsIgnoreCase(tmpReaderValue) || "false".equals(tmpReaderValue)) {
                            tmpField.set(t, false);
                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    log.error("Error:parseProperty", e);
                }
            }

        }
        if (null != excuteMethodOnEnd) {
            try {
                excuteMethodOnEnd.setAccessible(true);
                excuteMethodOnEnd.invoke(t);
            } catch (IllegalAccessException e) {
                log.error("Error:parseProperty", e);
            } catch (InvocationTargetException e) {
                log.error("Error:parseProperty", e);
            }
        }
        return t;
    }

    // 转换字符串为毫秒
    private static Long parseStrToTimeMillis(String timeStr, Long defaultMillis, boolean secondMode) {
        if (StringUtils.isEmpty(timeStr)) {
            return defaultMillis;
        }
        Long value = null;
        try {
            Double doubleVal = null;
            String timeStrLower = timeStr.trim().toLowerCase();
            if (timeStrLower.endsWith("ms")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim());
            } else if (timeStrLower.endsWith("s")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000;
            } else if (timeStrLower.endsWith("m")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 60;
            } else if (timeStrLower.endsWith("h")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600;
            } else if (timeStrLower.endsWith("d")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600 * 24;
            } else if (timeStrLower.endsWith("w")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600 * 24 * 7;
            } else if (timeStrLower.endsWith("mon")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 3).trim()) * 1000 * 3600 * 24 * 30;
            } else if (timeStrLower.endsWith("y")) {
                doubleVal = Double.valueOf(timeStrLower.substring(0, timeStrLower.length() - 1).trim()) * 1000 * 3600 * 24 * 365;
            } else if (secondMode) {
                doubleVal = Double.valueOf(timeStrLower.trim()) * 1000;
            } else {
                doubleVal = Double.valueOf(timeStrLower.trim());
            }
            if (null != doubleVal) {
                value = (long) (doubleVal + 0.5);
            }
        } catch (Exception e) {
            value = null;
            log.error("Error:parseStrToTimeMillis", e);
        }
        if (value == null) {
            return defaultMillis;
        } else {
            return value;
        }
    }

    /**
     * 读取Properties为字符串
     * @param properties 配置文件
     * @param key 读取的Key
     * @paramDefault defaultVal 默认值
     * @return 读取结果
     */
    public static String getPropertyString(Properties properties, String key) {
        return getPropertyString(properties, key, null);
    }

    /**
     * 读取Properties为字符串
     * @param properties 配置文件
     * @param key 读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static String getPropertyString(Properties properties, String key, String defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                return val;
            }
        }
    }

    /**
     * 读取Properties为Integer
     * @param properties 配置文件
     * @param key 读取的Key
     * @paramDefault defaultVal 默认值
     * @return 读取结果
     */
    public static Integer getPropertyInteger(Properties properties, String key) {
        return getPropertyInteger(properties, key, null);
    }

    /**
     * 读取Properties为Integer
     * @param properties 配置文件
     * @param key 读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Integer getPropertyInteger(Properties properties, String key, Integer defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Integer parseVal;
                try {
                    parseVal = Integer.parseInt(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyInteger", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Long
     * @param properties 配置文件
     * @param key 读取的Key
     * @paramDefault defaultVal 默认值
     * @return 读取结果
     */
    public static Long getPropertyLong(Properties properties, String key) {
        return getPropertyLong(properties, key, null);
    }

    /**
     * 读取Properties为Long
     * @param properties 配置文件
     * @param key 读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Long getPropertyLong(Properties properties, String key, Long defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Long parseVal;
                try {
                    parseVal = Long.parseLong(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyLong", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Float
     * @param properties 配置文件
     * @param key 读取的Key
     * @paramDefault defaultVal 默认值
     * @return 读取结果
     */
    public static Float getPropertyFloat(Properties properties, String key) {
        return getPropertyFloat(properties, key, null);
    }

    /**
     * 读取Properties为Float
     * @param properties 配置文件
     * @param key 读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Float getPropertyFloat(Properties properties, String key, Float defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Float parseVal;
                try {
                    parseVal = Float.parseFloat(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyFloat", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Double
     * @param properties 配置文件
     * @param key 读取的Key
     * @paramDefault defaultVal 默认值
     * @return 读取结果
     */
    public static Double getPropertyDouble(Properties properties, String key) {
        return getPropertyDouble(properties, key, null);
    }

    /**
     * 读取Properties为Double
     * @param properties 配置文件
     * @param key 读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Double getPropertyDouble(Properties properties, String key, Double defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                Double parseVal;
                try {
                    parseVal = Double.parseDouble(val);
                } catch (Exception e) {
                    log.error("Error:getPropertyDouble", e);
                    parseVal = defaultVal;
                }
                return parseVal;
            }
        }
    }

    /**
     * 读取Properties为Boolean
     * @param properties 配置文件
     * @param key 读取的Key
     * @paramDefault defaultVal 默认值
     * @return 读取结果
     */
    public static Boolean getPropertyBoolean(Properties properties, String key) {
        return getPropertyBoolean(properties, key, null);
    }

    /**
     * 读取Properties为Boolean
     * @param properties 配置文件
     * @param key 读取的Key
     * @param defaultVal 默认值
     * @return 读取结果
     */
    public static Boolean getPropertyBoolean(Properties properties, String key, Boolean defaultVal) {
        if (null == key || key.length() <= 0) {
            return null;
        } else if (null == properties) {
            return defaultVal;
        } else {
            String val = properties.getProperty(key);
            if (null == val || val.length() <= 0) {
                return defaultVal;
            } else {
                if ("true".equalsIgnoreCase(val) || "1".equalsIgnoreCase(val)) {
                    return true;
                } else if ("false".equalsIgnoreCase(val) || "0".equalsIgnoreCase(val)) {
                    return false;
                } else {
                    return defaultVal;
                }
            }
        }
    }
}
