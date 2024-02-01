package com.ruomm.javax.basex;

import com.ruomm.javax.basex.annotation.DefConfigField;
import com.ruomm.javax.corex.CharsetUtils;
import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 牛牛-wanruome@163.com
 * @version 1.0
 * @copyright www.ruomm.com
 * @create 2021/1/8 9:34
 */
public class StringConfigHelper {
    private final static Log log = LogFactory.getLog(StringConfigHelper.class);
    //逗号
    private final static String SPLIT_TAG_OF_ITEM = ",";
    private final static String SPLIT_TAG_OF_KEYVALUE = ":";
    private String splitTagOfItem;
    private String splitTagOfKeyvalue;
    private String transTagOfItem;
    private String transTagOfKeyvalue;

    public StringConfigHelper() {
        this.splitTagOfItem = SPLIT_TAG_OF_ITEM;
        this.splitTagOfKeyvalue = SPLIT_TAG_OF_KEYVALUE;
        this.transTagOfItem = "&0x" + str2HexStr(SPLIT_TAG_OF_ITEM);
        this.transTagOfKeyvalue = "&0x" + str2HexStr(SPLIT_TAG_OF_KEYVALUE);
    }

    public StringConfigHelper(String splitTagOfItem, String splitTagOfKeyvalue) {
        this.splitTagOfItem = StringUtils.isEmpty(splitTagOfItem) ? SPLIT_TAG_OF_ITEM : splitTagOfItem;
        this.splitTagOfKeyvalue = StringUtils.isEmpty(splitTagOfKeyvalue) ? SPLIT_TAG_OF_KEYVALUE : splitTagOfKeyvalue;
        this.transTagOfItem = "&0x" + str2HexStr(this.splitTagOfItem);
        this.transTagOfKeyvalue = "&0x" + str2HexStr(this.splitTagOfKeyvalue);
    }

    public StringConfigHelper(String splitTagOfItem, String splitTagOfKeyvalue, String transTagOfItem, String transTagOfKeyvalue) {
        this.splitTagOfItem = StringUtils.isEmpty(splitTagOfItem) ? SPLIT_TAG_OF_ITEM : splitTagOfItem;
        this.splitTagOfKeyvalue = StringUtils.isEmpty(splitTagOfKeyvalue) ? SPLIT_TAG_OF_KEYVALUE : splitTagOfKeyvalue;
        this.transTagOfItem = StringUtils.isEmpty(transTagOfItem) ? "&0x" + str2HexStr(this.splitTagOfItem) : transTagOfItem;
        this.transTagOfKeyvalue = StringUtils.isEmpty(transTagOfKeyvalue) ? "&0x" + str2HexStr(this.splitTagOfKeyvalue) : transTagOfKeyvalue;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    private static String str2HexStr(String str, String charsetName) {
        try {
            char[] chars = "0123456789abcdef".toCharArray();
            StringBuilder sb = new StringBuilder("");
            Charset charset = CharsetUtils.parseRealCharset(charsetName, Charset.forName("UTF-8"));
            byte[] bs = str.getBytes(charset);
            int bit;

            for (int i = 0; i < bs.length; i++) {
                bit = (bs[i] & 0x0f0) >> 4;
                sb.append(chars[bit]);
                bit = bs[i] & 0x0f;
                sb.append(chars[bit]);
//			sb.append('');
            }
            return sb.toString().trim();
        } catch (Exception e) {
            // TODO: handle exception
            log.error("Error:str2HexStr", e);
            return null;
        }
    }

    private static String str2HexStr(String str) {
        return str2HexStr(str, null);
    }

    private static String parseSplitTag(String splitTag) {
        if (null == splitTag || splitTag.length() <= 0) {
            return splitTag;
        }
        if (splitTag.equals("|") || splitTag.equals(".") || splitTag.equals("*") || splitTag.equals("+")) {
            return "\\" + splitTag;
        } else {
            return splitTag;
        }
    }

    /**
     * 解析配置字符串为map列表
     *
     * @param str 配置字符串
     * @return map列表
     */
    public Map<String, String> parseConfigToMap(String str) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(str)) {
            return map;
        }
        String[] strArray = str.split(parseSplitTag(splitTagOfItem));
        if (null == strArray || strArray.length <= 0) {
            return map;
        }
        for (String tmpStr : strArray) {
            if (StringUtils.isEmpty(tmpStr)) {
                continue;
            }
            String[] tmpStrArray = tmpStr.split(parseSplitTag(splitTagOfKeyvalue));
            if (null == tmpStrArray || tmpStrArray.length < 2) {
                continue;
            }
            String realKey = tmpStrArray[0];
            if (!StringUtils.isEmpty(transTagOfItem) && !StringUtils.isEmpty(realKey)) {
                realKey = realKey.replace(transTagOfItem, splitTagOfItem);
            }
            if (!StringUtils.isEmpty(transTagOfKeyvalue) && !StringUtils.isEmpty(realKey)) {
                realKey = realKey.replace(transTagOfKeyvalue, splitTagOfKeyvalue);
            }
            if (StringUtils.isEmpty(realKey)) {
                continue;
            }
            String realVal = tmpStrArray[1];
            if (!StringUtils.isEmpty(transTagOfItem) && !StringUtils.isEmpty(realVal)) {
                realVal = realVal.replace(transTagOfItem, splitTagOfItem);
            }
            if (!StringUtils.isEmpty(transTagOfKeyvalue) && !StringUtils.isEmpty(realVal)) {
                realVal = realVal.replace(transTagOfKeyvalue, splitTagOfKeyvalue);
            }
            map.put(realKey, realVal);
        }
        return map;
    }

    /**
     * 解析配置字符串为list列表，对象使用DefConfigField来注解
     *
     * @param str        配置字符串
     * @param tClass     目标对象类型，若是fieldNames值为空，则使用对象的DefConfigField注解来解析
     * @param fieldNames 字段解析数组，若是此值为空，则使用对象的DefConfigField注解来解析
     * @return list列表
     */
    public <T> List<T> parseConfigToList(String str, Class<T> tClass, String... fieldNames) {
        List<T> list = new ArrayList<>();
        if (StringUtils.isEmpty(str)) {
            return list;
        }
        String[] strArray = str.split(parseSplitTag(splitTagOfItem));
        if (null == strArray || strArray.length <= 0) {
            return list;
        }
        Map<Integer, Field> mapFiled = new HashMap<>();
        if (null == fieldNames || fieldNames.length <= 0) {
            Field[] fields = tClass.getDeclaredFields();
            if (null != fields || fields.length > 0) {
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
                        DefConfigField defField = field.getAnnotation(DefConfigField.class);
                        if (null == defField) {
                            continue;
                        }
                        field.setAccessible(true);
                        mapFiled.put(defField.index(), field);
                    } catch (Exception e) {
                        log.error("parseConfigToList依据DefConfigField解析field时候异常", e);
                    }
                }
            }
        } else {
            int tmpSize = fieldNames.length;
            for (int i = 0; i < tmpSize; i++) {
                String tmpName = fieldNames[i];
                if (StringUtils.isEmpty(tmpName)) {
                    continue;
                }
                try {
                    Field field = tClass.getDeclaredField(tmpName);
                    if (null == field) {
                        continue;
                    }
                    // 常量跳过
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    // 静态跳过
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    mapFiled.put(i, field);
                } catch (Exception e) {
                    log.error("parseConfigToList依据fieldName解析field时候异常", e);
                }
            }
        }

        for (String tmpStr : strArray) {
            if (StringUtils.isEmpty(tmpStr)) {
                continue;
            }
            String[] tmpStrArray = tmpStr.split(parseSplitTag(splitTagOfKeyvalue));
            if (null == tmpStrArray || tmpStrArray.length <= 0) {
                continue;
            }
            T t = null;
            try {
                t = tClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                log.error("parseConfigToList目标实例化时候异常", e);
            }
            int tmpSize = tmpStrArray.length;
            for (int i = 0; i < tmpSize; i++) {
                Field filed = mapFiled.get(i);
                if (null == filed) {
                    continue;
                }
                String realVal = tmpStrArray[i];
                if (!StringUtils.isEmpty(transTagOfItem) && !StringUtils.isEmpty(realVal)) {
                    realVal = realVal.replace(transTagOfItem, splitTagOfItem);
                }
                if (!StringUtils.isEmpty(transTagOfKeyvalue) && !StringUtils.isEmpty(realVal)) {
                    realVal = realVal.replace(transTagOfKeyvalue, splitTagOfKeyvalue);
                }
                if (StringUtils.isEmpty(realVal)) {
                    continue;
                }
                try {
                    filed.set(t, realVal);
                } catch (IllegalAccessException e) {
                    log.error("parseConfigToList目标field赋值时候异常", e);
                }
            }
            list.add(t);
        }
        return list;
    }

}
