/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月19日 下午1:43:02
 */
package com.ruomm.javax.basex;

import com.ruomm.javax.corex.StringUtils;
import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class StringToNumberUtil {
    private final static Log log = LogFactory.getLog(StringToNumberUtil.class);

    public static Integer getValInteger(String valueString) {
        return getValInteger(valueString, null);
    }

    public static Integer getValInteger(String valueString, Integer defaultVal) {
        if (StringUtils.isEmpty(valueString)) {
            return defaultVal;
        }
        Integer value = null;
        try {
            value = Integer.valueOf(valueString);
        } catch (Exception e) {
            value = null;
            log.error("Error:getValInteger", e);
        }
        if (value == null) {
            return defaultVal;
        } else {
            return value;
        }
    }

    public static Long getValLong(String valueString) {
        return getValLong(valueString, null);
    }

    public static Long getValLong(String valueString, Long defaultVal) {
        if (StringUtils.isEmpty(valueString)) {
            return defaultVal;
        }
        Long value = null;
        try {
            value = Long.valueOf(valueString);
        } catch (Exception e) {
            value = null;
            log.error("Error:getValLong", e);
        }
        if (value == null) {
            return defaultVal;
        } else {
            return value;
        }
    }

    public static Float getValFloat(String valueString) {
        return getValFloat(valueString, null);
    }

    public static Float getValFloat(String valueString, Float defaultVal) {
        if (StringUtils.isEmpty(valueString)) {
            return defaultVal;
        }
        Float value = null;
        try {
            value = Float.valueOf(valueString);
        } catch (Exception e) {
            value = null;
            log.error("Error:getValFloat", e);
        }
        if (value == null) {
            return defaultVal;
        } else {
            return value;
        }
    }

    public static Double getValDouble(String valueString) {
        return getValDouble(valueString, null);
    }

    public static Double getValDouble(String valueString, Double defaultVal) {
        if (StringUtils.isEmpty(valueString)) {
            return defaultVal;
        }
        Double value = null;
        try {
            value = Double.valueOf(valueString);
        } catch (Exception e) {
            value = null;
            log.error("Error:getValDouble", e);
        }
        if (value == null) {
            return defaultVal;
        } else {
            return value;
        }
    }

    public static Boolean getValBoolean(String valueString) {
        return getValBoolean(valueString, null);

    }

    public static Boolean getValBoolean(String valueString, Boolean defaultVal) {
        if (StringUtils.isEmpty(valueString)) {
            return defaultVal;
        } else if ("1".equals(valueString) || "true".equals(valueString.toLowerCase())) {
            return true;
        } else if ("0".equals(valueString) || "false".equals(valueString.toLowerCase())) {
            return false;
        } else {
            return defaultVal;
        }

    }

    /**
     * 进制转换数字->字符串
     *
     * @param num         数字
     * @param numScaleStr 进制字符串至少2位
     * @return 特定进制字符串
     */
    public static String intToString(int num, String numScaleStr) {
        if (null == numScaleStr || numScaleStr.length() < 2) {
            log.error("Error:intToString->进位规则字符串至少需要2位");
            throw new NumberFormatException("Error:NumberStringUtil.intToString->进位规则字符串至少需要2位");
        }
        int numScaleBit = numScaleStr.length();
        int numVal = Math.abs(num);
        List<Integer> listScales = new ArrayList<Integer>();
        while (true) {
            listScales.add(numVal % numScaleBit);
            numVal = numVal / numScaleBit;
            if (numVal == 0l) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        int listSize = listScales.size();
        for (int i = listSize - 1; i >= 0; i--) {
            int tmpInt = listScales.get(i);
            sb.append(numScaleStr.substring(tmpInt, tmpInt + 1));
        }
        if (num < 0) {
            return "-" + sb.toString();
        } else {
            return sb.toString();
        }
    }

    /**
     * 进制转换数字->字符串
     *
     * @param num         数字
     * @param numScaleStr 进制字符串至少2位
     * @return 特定进制字符串
     */
    public static String longToString(long num, String numScaleStr) {
        if (null == numScaleStr || numScaleStr.length() < 2) {
            log.error("Error:longToString->进位规则字符串至少需要2位");
            throw new NumberFormatException("Error:NumberStringUtil.longToString->进位规则字符串至少需要2位");
        }
        int numScaleBit = numScaleStr.length();
        long numVal = Math.abs(num);
        List<Long> listScales = new ArrayList<Long>();
        while (true) {
            listScales.add(numVal % numScaleBit);
            numVal = numVal / numScaleBit;
            if (numVal == 0l) {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        int listSize = listScales.size();
        for (int i = listSize - 1; i >= 0; i--) {
            Long tmp = listScales.get(i);
            int tmpInt = tmp.intValue();
            sb.append(numScaleStr.substring(tmpInt, tmpInt + 1));
        }
        if (num < 0) {
            return "-" + sb.toString();
        } else {
            return sb.toString();
        }
    }

    /**
     * 进制转换字符串->数字
     *
     * @param numStr      特定进制字符串
     * @param numScaleStr 进制字符串至少2位
     * @return 数字
     */
    public static long stringToLong(String numStr, String numScaleStr) {
        if (null == numScaleStr || numScaleStr.length() < 2) {
            log.error("Error:stringToLong->进位规则字符串至少需要2位");
            throw new NumberFormatException("Error:NumberStringUtil.stringToLong->进位规则字符串至少需要2位");
        }
        if (null == numStr || numStr.length() <= 0) {
            log.error("Error:stringToLong->待解析为数字的字符串为空");
            throw new NumberFormatException("Error:NumberStringUtil.stringToLong->待解析为数字的字符串为空");
        }
        boolean isBegative = false;
        int numScaleBit = numScaleStr.length();
        if (null != numStr && numStr.startsWith("-")) {
            isBegative = true;
        }
        String parseStr = isBegative ? numStr.substring(1) : numStr;
        if (null == parseStr || parseStr.length() <= 0) {
            log.error("Error:stringToInt->待解析为数字的字符串为空");
            throw new NumberFormatException("Error:NumberStringUtil.stringToLong->待解析为数字的字符串为空");
        }
        long numVal = 0;
        for (int i = 0; i < parseStr.length(); i++) {
            String itemStr = parseStr.substring(i, i + 1);
            int index = numScaleStr.indexOf(itemStr);
            if (index < 0) {
                log.error("Error:stringToInt->待解析为数字的字符串不合法或进位规则字符串错误");
                throw new NumberFormatException("Error:NumberStringUtil.stringToLong->待解析为数字的字符串不合法或进位规则字符串错误");
            }
            long itemVal = index;
            for (int t = i; t < parseStr.length() - 1; t++) {
                itemVal = itemVal * numScaleBit;
            }
            numVal = numVal + itemVal;
        }
        return isBegative ? 0 - numVal : numVal;

    }

    /**
     * 进制转换字符串->数字
     *
     * @param numStr      特定进制字符串
     * @param numScaleStr 进制字符串至少2位
     * @return 数字
     */
    public static int stringToInt(String numStr, String numScaleStr) {
        if (null == numScaleStr || numScaleStr.length() < 2) {
            log.error("Error:stringToInt->进位规则字符串至少需要2位");
            throw new NumberFormatException("Error:NumberStringUtil.stringToInt->进位规则字符串至少需要2位");
        }
        if (null == numStr || numStr.length() <= 0) {
            log.error("Error:stringToInt->待解析为数字的字符串为空");
            throw new NumberFormatException("Error:NumberStringUtil.stringToInt->待解析为数字的字符串为空");
        }
        boolean isBegative = false;
        int numScaleBit = numScaleStr.length();
        if (null != numStr && numStr.startsWith("-")) {
            isBegative = true;
        }
        String parseStr = isBegative ? numStr.substring(1) : numStr;
        if (null == parseStr || parseStr.length() <= 0) {
            log.error("Error:stringToInt->待解析为数字的字符串为空");
            throw new NumberFormatException("Error:NumberStringUtil.stringToInt->待解析为数字的字符串为空");
        }
        int numVal = 0;
        for (int i = 0; i < parseStr.length(); i++) {
            String itemStr = parseStr.substring(i, i + 1);
            int index = numScaleStr.indexOf(itemStr);
            if (index < 0) {
                log.error("Error:stringToInt->待解析为数字的字符串不合法或进位规则字符串错误");
                throw new NumberFormatException("Error:NumberStringUtil.stringToInt->待解析为数字的字符串不合法或进位规则字符串错误");
            }
            int itemVal = index;
            for (int t = i; t < parseStr.length() - 1; t++) {
                itemVal = itemVal * numScaleBit;
            }
            numVal = numVal + itemVal;
        }
        return isBegative ? 0 - numVal : numVal;

    }
}
