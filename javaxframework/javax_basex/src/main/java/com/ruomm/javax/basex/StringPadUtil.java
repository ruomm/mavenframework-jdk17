/**
 * @copyright www.ruomm.com
 * @author 牛牛-wanruome@163.com
 * @create 2020年7月6日 下午1:56:23
 */
package com.ruomm.javax.basex;

public class StringPadUtil {

    /**
     * String 定长左侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @paramDefault fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String lpad(String s, int padSize) {
        return subPadStringCommon(s, padSize, ' ', false, false);
    }

    /**
     * String 定长左侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @param fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String lpad(String s, int padSize, char fillChar) {
        return subPadStringCommon(s, padSize, fillChar, false, false);
    }

    /**
     * String 定长右侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @paramDefault fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String rpad(String s, int padSize) {
        return subPadStringCommon(s, padSize, ' ', true, false);
    }

    /**
     * String 定长右侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @param fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String rpad(String s, int padSize, char fillChar) {
        return subPadStringCommon(s, padSize, fillChar, true, false);
    }

    /**
     * String 定长左侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @paramDefault fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String lpadRSub(String s, int padSize) {
        return subPadStringCommon(s, padSize, ' ', false, true);
    }

    /**
     * String 定长左侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @param fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String lpadRSub(String s, int padSize, char fillChar) {
        return subPadStringCommon(s, padSize, fillChar, false, true);
    }

    /**
     * String 定长右侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @paramDefault fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String rpadRSub(String s, int padSize) {
        return subPadStringCommon(s, padSize, ' ', true, true);
    }

    /**
     * String 定长右侧补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param padSize 定长大小
     * @param fillChar 补全字符，默认空格(' ')
     * @return 定长字符串
     */
    public static String rpadRSub(String s, int padSize, char fillChar) {
        return subPadStringCommon(s, padSize, fillChar, true, true);
    }

    /**
     * String 左侧截取一定长度字符串，一个中文2英文字符
     * @param s 原始字符串
     * @param subSize 长度
     * @return 截取后的字符串
     */
    public static String lsub(String s, int subSize) {
        return subStringCommon(s, subSize, false);
    }

    /**
     * String 右侧截取一定长度字符串，一个中文2英文字符
     * @param s 原始字符串
     * @param subSize 长度
     * @return 截取后的字符串
     */
    public static String rsub(String s, int subSize) {
        return subStringCommon(s, subSize, true);
    }

    private static String subStringCommon(String s, int subSize, boolean isRSub) {
        if (null == s) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int length = 0;
        if (isRSub) {
            for (int i = s.length() - 1; i >= 0; i--) {
                int ascii = Character.codePointAt(s, i);
                int iSize = 0;
                if (ascii >= 0 && ascii <= 127) {
                    iSize = 1;
                } else {
                    iSize = 2;
                }
                if (length + iSize <= subSize) {
                    length = length + iSize;
                    sb.insert(0, s.substring(i, i + 1));
                } else {
                    break;
                }
            }
        } else {
            for (int i = 0; i < s.length(); i++) {
                int ascii = Character.codePointAt(s, i);
                int iSize = 0;
                if (ascii >= 0 && ascii <= 127) {
                    iSize = 1;
                } else {
                    iSize = 2;
                }
                if (length + iSize <= subSize) {
                    length = length + iSize;
                    sb.append(s.substring(i, i + 1));
                } else {
                    break;
                }
            }
        }
        return sb.toString();
    }

    /**
     * String 定长补全特定字符，若是大于定长截取则截取后补全
     * @param s 原始字符串
     * @param subSize 定长大小
     * @param fillChar 补全字符，默认空格(' ')
     * @param isRPad 是否右侧补全，默认false
     * @param isRSub 是否从右侧截取，默认false
     * @return 定长字符串
     */
    public static String subPadStringCommon(String s, int subSize, char fillChar, boolean isRPad, boolean isRSub) {
        if (null == s) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int length = 0;
        if (isRSub) {
            for (int i = s.length() - 1; i >= 0; i--) {
                int ascii = Character.codePointAt(s, i);
                int iSize = 0;
                if (ascii >= 0 && ascii <= 127) {
                    iSize = 1;
                } else {
                    iSize = 2;
                }
                if (length + iSize <= subSize) {
                    length = length + iSize;
                    sb.insert(0, s.substring(i, i + 1));
                } else {
                    break;
                }
            }
        } else {
            for (int i = 0; i < s.length(); i++) {
                int ascii = Character.codePointAt(s, i);
                int iSize = 0;
                if (ascii >= 0 && ascii <= 127) {
                    iSize = 1;
                } else {
                    iSize = 2;
                }
                if (length + iSize <= subSize) {
                    length = length + iSize;
                    sb.append(s.substring(i, i + 1));
                } else {
                    break;
                }
            }
        }
        for (int i = length; i < subSize; i++) {
            if (isRPad) {
                sb.append(fillChar);
            } else {
                sb.insert(0, fillChar);
            }
        }
        return sb.toString();
    }
}
