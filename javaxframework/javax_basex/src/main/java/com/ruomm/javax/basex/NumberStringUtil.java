/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年6月10日 上午11:36:17
 */
package com.ruomm.javax.basex;

import com.ruomm.javax.loggingx.Log;
import com.ruomm.javax.loggingx.LogFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NumberStringUtil {
    private final static Log log = LogFactory.getLog(NumberStringUtil.class);

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

    /**
     * 转换TB、GB、MB、KB、B等字符串为数字byte大小
     * @param sizeStr TB、GB、MB、KB、B等字符串
     * @param defaultVal 默认值
     * @return 数字byte大小
     */
    public static long parseSize(String sizeStr, long defaultVal) {
        long resultVal = 0;
        try {
            resultVal = parseSize(sizeStr);
        } catch (Exception e) {
            resultVal = defaultVal;
        }
        return resultVal;
    }

    /**
     * 转换TB、GB、MB、KB、B等字符串为数字byte大小
     * @param sizeStr TB、GB、MB、KB、B等字符串
     * @return 数字byte大小
     */
    public static long parseSize(String sizeStr) {
        String byteStr = null == sizeStr || sizeStr.length() <= 0 ? sizeStr : sizeStr.trim();
        if (null == byteStr || byteStr.length() <= 0) {
            log.error("输入内容为空，无法转换为字符串为自己大小");
            throw new RuntimeException("无法转换字符串为字节大小！");
        }
        try {
            String str = byteStr.toLowerCase();
            int strSize = str.length();
            long valRate = 1;
            String strVla = null;
            if (str.endsWith("t")) {
                strVla = byteStr.substring(0, strSize - 1);
                valRate = 1024l * 1024l * 1024l * 1024l;
            } else if (str.endsWith("tb")) {
                strVla = byteStr.substring(0, strSize - 2);
                valRate = 1024l * 1024l * 1024l * 1024l;
            } else if (str.endsWith("g")) {
                strVla = byteStr.substring(0, strSize - 1);
                valRate = 1024l * 1024l * 1024l;
            } else if (str.endsWith("gb")) {
                strVla = byteStr.substring(0, strSize - 2);
                valRate = 1024l * 1024l * 1024l;
            } else if (str.endsWith("m")) {
                strVla = byteStr.substring(0, strSize - 1);
                valRate = 1024l * 1024l;
            } else if (str.endsWith("mb")) {
                strVla = byteStr.substring(0, strSize - 2);
                valRate = 1024l * 1024l;
            } else if (str.endsWith("k")) {
                strVla = byteStr.substring(0, strSize - 1);
                valRate = 1024l;
            } else if (str.endsWith("kb")) {
                strVla = byteStr.substring(0, strSize - 2);
                valRate = 1024l;
            } else if (str.endsWith("b")) {
                strVla = byteStr.substring(0, strSize - 1);
                valRate = 1;
            } else {
                strVla = byteStr;
                valRate = 1;
            }
            BigDecimal bigDecimal = new BigDecimal(strVla.trim()).multiply(new BigDecimal(valRate)).setScale(0, BigDecimal.ROUND_HALF_UP);
            return bigDecimal.longValue();
        } catch (Exception e) {
            log.error("输入内容不合法，无法转换为字符串为自己大小", e);
            throw new RuntimeException("无法转换字符串为字节大小！");
        }
    }

    /**
     * 使用TB、GB、MB、KB、B等格式化byte大小，大于95%时候进位
     * @param nums byte大小
     * @paramDefault scale 小数点位数，默认2位
     * @return TB、GB、MB、KB、B等字符串
     */
    public static String formatSize(long nums) {
        return formatSize(nums, 2);
    }

    /**
     * 使用TB、GB、MB、KB、B等格式化byte大小，大于95%时候进位
     * @param nums byte大小
     * @param scale 小数点位数，默认2位
     * @return TB、GB、MB、KB、B等字符串
     */
    public static String formatSize(long nums, int scale) {
        long numsAbs = Math.abs(nums);
        if (numsAbs >= 1024l * 1024l * 1024l * 1024l * 95l / 100l) {
            return formatSizeByTag(nums, "TB", scale);
        } else if (numsAbs >= 1024l * 1024l * 1024l * 95l / 100l) {
            return formatSizeByTag(nums, "GB", scale);
        } else if (numsAbs >= 1024l * 1024l * 95l / 100l) {
            return formatSizeByTag(nums, "MB", scale);
        } else if (numsAbs >= 1024l * 95l / 100l) {
            return formatSizeByTag(nums, "KB", scale);
        } else {
            return formatSizeByTag(nums, "B", scale);
        }
    }

    /**
     * 使用TB、GB、MB、KB、B等格式化byte大小
     * @param nums byte大小
     * @param tag TB、GB、MB、KB、B等字符串
     * @paramDefault scale 小数点位数，默认2位
     * @return TB、GB、MB、KB、B等字符串
     */
    public static String formatSizeByTag(long nums, String tag) {
        return formatSizeByTag(nums, tag, 2);
    }

    /**
     * 使用TB、GB、MB、KB、B等格式化byte大小
     * @param nums byte大小
     * @param tag TB、GB、MB、KB、B等字符串
     * @param scale 小数点位数，默认2位
     * @return TB、GB、MB、KB、B等字符串
     */
    public static String formatSizeByTag(long nums, String tag, int scale) {
        String tagStr = null == tag || tag.length() <= 0 ? tag : tag.trim();
        if (null == tagStr || tagStr.length() <= 0) {
            return String.valueOf(nums);
        }
        tagStr = tagStr.toLowerCase();
        long valRate = 1;
        if (tagStr.endsWith("t")) {
            valRate = 1024l * 1024l * 1024l * 1024l;
        } else if (tagStr.endsWith("tb")) {
            valRate = 1024l * 1024l * 1024l * 1024l;
        } else if (tagStr.endsWith("g")) {
            valRate = 1024l * 1024l * 1024l;
        } else if (tagStr.endsWith("gb")) {
            valRate = 1024l * 1024l * 1024l;
        } else if (tagStr.endsWith("m")) {
            valRate = 1024l * 1024l;
        } else if (tagStr.endsWith("mb")) {
            valRate = 1024l * 1024l;
        } else if (tagStr.endsWith("k")) {
            valRate = 1024l;
        } else if (tagStr.endsWith("kb")) {
            valRate = 1024l;
        } else if (tagStr.endsWith("b")) {
            valRate = 1;
        } else {
            valRate = 1;
        }
        BigDecimal bigDecimalVal = new BigDecimal(nums);
        BigDecimal bigDecimalPerVal = new BigDecimal(valRate);
        BigDecimal tmp = bigDecimalVal.divide(bigDecimalPerVal, scale, BigDecimal.ROUND_HALF_UP);
        return tmp.toString() + tag;
    }
}
